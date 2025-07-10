/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package npc.model.events;

import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import events.FightClub.FightClubManager;
import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.Config;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.HtmlMessage;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class FightClubManagerInstance extends NpcInstance
{
	private static final String HTML_INDEX = "scripts/events/fightclub/index.htm";
	private static final String HTML_ACCEPT = "scripts/events/fightclub/accept.htm";
	private static final String HTML_MAKEBATTLE = "scripts/events/fightclub/makebattle.htm";
	private static final String HTML_INFO = "scripts/events/fightclub/info.htm";
	private static final String HTML_DISABLED = "scripts/events/fightclub/disabled.htm";
	private static final String HTML_LIST = "scripts/events/fightclub/fightslist.htm";
	private static final String HTML_RESULT = "scripts/events/fightclub/result.htm";

	public FightClubManagerInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player))
		{
			return;
		}

		if (Config.FIGHT_CLUB_ENABLED)
		{
			showChatWindow(player, HTML_DISABLED, false);
			return;
		}

		if (command.equalsIgnoreCase("index"))
		{
			showChatWindow(player, HTML_INDEX, false);
		}
		else if (command.equalsIgnoreCase("makebattle"))
		{
			sendHtml(makeBattleHtml(player), player);
		}
		else if (command.equalsIgnoreCase("info"))
		{
			showChatWindow(player, HTML_INFO, false);
		}
		else
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			String pageName = st.nextToken();
			if (pageName.equalsIgnoreCase("addbattle"))
			{
				int count = 0;
				try
				{
					count = Integer.parseInt(st.nextToken());
					if (count == 0)
					{
						sendResult(player, player.isLangRus() ? "Ошибка!" : "Error!", player.isLangRus() ? "Вы не ввели количество, или неправильное число." : "You did not enter the number or wrong number.");
						return;
					}
				}
				catch (NumberFormatException e)
				{
					sendResult(player, player.isLangRus() ? "Ошибка!" : "Error!", player.isLangRus() ? "Вы не ввели количество, или неправильное число." : "You did not enter the number or wrong number.");
					return;
				}
				String itemName = StringUtils.EMPTY;
				if (st.hasMoreTokens())
				{
					itemName = st.nextToken();
					while (st.hasMoreTokens())
					{
						itemName += " " + st.nextToken();
					}
				}
				String respone = FightClubManager.addApplication(player, itemName, count);
				if ("OK".equalsIgnoreCase(respone))
				{
					sendResult(player, player.isLangRus() ? "Выполнено!" : "Completed!", (player.isLangRus() ? "Вы создали заявку на участие.<br>Ваша ставка - <font color=\"LEVEL\">" : "You have created an application for participation.<br>Your bet - <font color=\"LEVEL\">") + count + " " + itemName + (player.isLangRus() ? "</font><br><center>Удачи!</center>" : "</font><br><center>Good luck!</center>"));
				}
				else if ("NoItems".equalsIgnoreCase(respone))
				{
					sendResult(player, player.isLangRus() ? "Ошибка!" : "Error!", player.isLangRus() ? "У вас недостаточно или отсутствуют требующиеся предметы!" : "You are not required or missing items!");
					return;
				}
				else if ("reg".equalsIgnoreCase(respone))
				{
					sendResult(player, player.isLangRus() ? "Ошибка!" : "Error!", player.isLangRus() ? "Вы уже зарегистрированы! Если вы хотите изменить ставку, удалите старую регистрацию." : "You are already registered! If you wish to bid, remove the old registration.");
					return;
				}
			}
			else if (pageName.equalsIgnoreCase("delete"))
			{
				if (FightClubManager.isRegistered(player))
				{
					FightClubManager.deleteRegistration(player);
					sendResult(player, player.isLangRus() ? "Выполнено!" : "Completed!", player.isLangRus() ? "<center>Вы удалены из списка регистрации.</center>" : "<center>You are removed from the list of registration.</center>");
				}
				else
				{
					sendResult(player, player.isLangRus() ? "Ошибка!" : "Error!", player.isLangRus() ? "<center>Вы не были зарегистрированы на участие.</center>" : "<center>You have not been registered to participate.</center>");
				}
			}
			else if (pageName.equalsIgnoreCase("openpage"))
			{
				sendHtml(makeOpenPage(player, Integer.parseInt(st.nextToken())), player);
			}
			else if (pageName.equalsIgnoreCase("tryaccept"))
			{
				sendHtml(makeAcceptHtml(player, Integer.parseInt(st.nextToken())), player);
			}
			else if (pageName.equalsIgnoreCase("accept"))
			{
				accept(player, Integer.parseInt(st.nextToken()));
			}
		}
	}

	@Override
	public void showChatWindow(Player player, int val, boolean firstTalk, Object... arg)
	{
		String html = HtmCache.getInstance().getHtml(HTML_INDEX, player);
		sendHtml(html, player);
	}

	private String makeOpenPage(Player player, int pageId)
	{
		String html = HtmCache.getInstance().getHtml(HTML_LIST, player);
		StringBuilder sb = new StringBuilder();

		final int count = FightClubManager.getRatesCount();
		int num = pageId * Config.PLAYERS_PER_PAGE;

		if (num > count)
		{
			num = count;
		}
		if (count > 0)
		{
			sb.append("<table width=300>");
			for (int i = pageId * Config.PLAYERS_PER_PAGE - Config.PLAYERS_PER_PAGE; i < num; i++)
			{
				final Rate rate = FightClubManager.getRateByIndex(i);
				sb.append("<tr>");
				sb.append("<td align=center width=95>");
				sb.append("<a action=\"bypass -h npc_%objectId%_tryaccept ").append(rate.getStoredId()).append("\">");
				sb.append("<font color=\"ffff00\">").append(rate.getPlayerName()).append("</font></a></td>");
				sb.append("<td align=center width=70>").append(rate.getPlayerLevel()).append("</td>");
				sb.append("<td align=center width=100><font color=\"ff0000\">");
				sb.append(rate.getPlayerClass().getName(player)).append("</font></td>");
				sb.append("<td align=center width=135><font color=\"00ff00\">");
				sb.append(rate.getItemCount()).append(" ").append(rate.getItemName());
				sb.append("</font></td></tr>");
			}

			sb.append("</table><br><br><br>");
			int pg = getPagesCount(count);
			sb.append("Страницы:&nbsp;");
			for (int i = 1; i <= pg; i++)
			{
				if (i == pageId)
				{
					sb.append(i).append("&nbsp;");
				}
				else
				{
					sb.append("<a action=\"bypass -h npc_%objectId%_openpage ").append(i).append("\">").append(i).append("</a>&nbsp;");
				}
			}
		}
		else
		{
			sb.append(player.isLangRus() ? "<br><center>Ставок пока не сделано</center>" : "<br><center>Rates have not yet done</center>");
		}

		html.replace("%data%", sb.toString());
		return html;
	}

	private String makeBattleHtml(Player player)
	{
		String html = HtmCache.getInstance().getHtml(HTML_MAKEBATTLE, player);
		html = html.replace("%items%", FightClubManager.getItemsList());
		return html;
	}

	private String makeAcceptHtml(Player player, long storedId)
	{
		final Rate rate = FightClubManager.getRateByStoredId(storedId);
		String html = HtmCache.getInstance().getHtml(HTML_ACCEPT, player);
		html.replace("%name%", rate.getPlayerName());
		html.replace("%class%", rate.getPlayerClass().getName(player));
		html.replace("%level%", String.valueOf(rate.getPlayerLevel()));
		html.replace("%rate%", rate.getItemCount() + " " + rate.getItemName());
		html.replace("%storedId%", String.valueOf(rate.getStoredId()));
		return html;
	}

	private void accept(Player player, int storedId)
	{
		if (player.getObjectId() == storedId)
		{
			sendResult(player, player.isLangRus() ? "Ошибка!" : "Error!", player.isLangRus() ? "Вы не можете вызвать на бой самого себя." : "You can not call the fight itself.");
			return;
		}
		if (FightClubManager.requestConfirmation(GameObjectsStorage.getPlayer(storedId), player))
		{
			sendResult(player, "Attention!", "You have sent a request to the opponent. If all conditions match, you will move into the arena<br><center><font color=\"LEVEL\">Good luck!</font></center><br>");
		}
	}

	private void sendResult(Player player, String title, String text)
	{
		String html = HtmCache.getInstance().getHtml(HTML_RESULT, player);
		html = html.replace("%title%", title);
		html = html.replace("%text%", text);
		sendHtml(html, player);
	}

	private int getPagesCount(int count)
	{
		if (count % Config.PLAYERS_PER_PAGE > 0)
		{
			return count / Config.PLAYERS_PER_PAGE + 1;
		}
		return count / Config.PLAYERS_PER_PAGE;
	}

	/**
	 * @param html   the html String
	 * @param player the Player
	 */
	public void sendHtml(String html, Player player)
	{
		html = html.replace("%objectId%", String.valueOf(getObjectId()));
		HtmlMessage packet = new HtmlMessage(this);
		packet.setHtml(html);
		player.sendPacket(packet);
	}

	public static class Rate
	{
		private String playerName;
		private int playerLevel;
		private ClassId playerClass;
		private int _itemId;
		private String itemName;
		private int _itemCount;
		private long playerStoredId;

		public Rate(Player player, int itemId, int itemCount)
		{
			playerName = player.getName();
			playerLevel = player.getLevel();
			playerClass = player.getClassId();
			_itemId = itemId;
			_itemCount = itemCount;
			itemName = ItemFunctions.createItem(itemId).getTemplate().getName();
			playerStoredId = player.getObjectId();
		}

		public String getPlayerName()
		{
			return playerName;
		}

		public int getPlayerLevel()
		{
			return playerLevel;
		}

		public ClassId getPlayerClass()
		{
			return playerClass;
		}

		public int getItemId()
		{
			return _itemId;
		}

		public int getItemCount()
		{
			return _itemCount;
		}

		public String getItemName()
		{
			return itemName;
		}

		public long getStoredId()
		{
			return playerStoredId;
		}
	}
}