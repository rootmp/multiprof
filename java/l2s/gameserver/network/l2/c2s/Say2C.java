package l2s.gameserver.network.l2.c2s;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graphbuilder.math.Expression;
import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.ExpressionTree;
import com.graphbuilder.math.VarMap;

import l2s.commons.ban.BanBindType;
import l2s.commons.ban.BanInfo;
import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.cache.ItemInfoCache;
import l2s.gameserver.dao.HidenItemsDAO;
import l2s.gameserver.handler.chat.BayleeInterfaceHandler;
import l2s.gameserver.handler.chat.IBayleeInterfaceHandler;
import l2s.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2s.gameserver.handler.voicecommands.VoicedCommandHandler;
import l2s.gameserver.instancemanager.GameBanManager;
import l2s.gameserver.instancemanager.PetitionManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.entity.olympiad.OlympiadGame;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.matching.MatchingRoom;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ActionFailPacket;
import l2s.gameserver.network.l2.s2c.SayPacket2;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ChatUtils;
import l2s.gameserver.utils.Log;
import l2s.gameserver.utils.Strings;
import l2s.gameserver.utils.TimeUtils;
import l2s.gameserver.utils.Util;

public class Say2C implements IClientIncomingPacket
{
	private static final Logger _log = LoggerFactory.getLogger(Say2C.class);

	/**
	 * RegExp для кэширования ссылок на предметы, пример ссылки: \b\tType=1
	 * \tID=268484598 \tColor=0 \tUnderline=0 \tTitle=\u001BAdena\u001B\b
	 */
	private static final Pattern EX_ITEM_LINK_PATTERN = Pattern.compile("[\b]\tType=[0-9]+[\\s]+\tID=([0-9]+)[\\s]+\tColor=[0-9]+[\\s]+\tUnderline=[0-9]+[\\s]+\tClassID=[0-9]+[\\s]+\tTitle=\u001B(.[^\u001B]*)[^\b]");
	private static final Pattern SKIP_ITEM_LINK_PATTERN = Pattern.compile("[\b]\tType=[0-9]+(.[^\b]*)[\b]");

	private String _text;
	private ChatType _type;
	private static int _isLocSharing;
	private String _target;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_text = packet.readS(Config.CHAT_MESSAGE_MAX_LEN);
		_type = l2s.commons.lang.ArrayUtils.valid(ChatType.VALUES, packet.readD());
		_isLocSharing = packet.readC();
		_target = _type == ChatType.TELL ? packet.readS(Config.CNAME_MAXLEN) : null;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		writeToChat(activeChar, _text, _type, _target);
	}

	public static void writeToChat(Player activeChar, String text, ChatType type, String target)
	{
		SayPacket2 cs;
		if (type == null || text == null || text.length() == 0)
		{
			activeChar.sendActionFailed();
			return;
		}

		text = text.replaceAll("\\\\n", "\n");

		if (text.contains("\n"))
		{
			final String[] lines = text.split("\n");
			text = StringUtils.EMPTY;
			for (int i = 0; i < lines.length; i++)
			{
				lines[i] = lines[i].trim();
				if (lines[i].length() == 0)
				{
					continue;
				}
				if (text.length() > 0)
				{
					text += "\n  >";
				}
				text += lines[i];
			}
		}

		if (text.length() == 0)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (Config.BAN_FOR_CFG_USAGE)
			if (text.startsWith("//cfg") || text.startsWith("///cfg") || text.startsWith("////cfg"))
			{
				activeChar.kick();
			}

		if (text.startsWith("."))
		{
			if (Config.ALLOW_VOICED_COMMANDS)
			{
				final String fullcmd = text.substring(1).trim();
				final String command = fullcmd.split("\\s+")[0];
				final String args = fullcmd.substring(command.length()).trim();

				if (command.length() > 0)
				{
					// then check for VoicedCommands
					final IVoicedCommandHandler vch = VoicedCommandHandler.getInstance().getVoicedCommandHandler(command);
					if (vch != null)
					{
						vch.useVoicedCommand(command, activeChar, args);
						return;
					}
				}
				activeChar.sendMessage(new CustomMessage("common.command404"));
				return;
			}
		}
		if (type == ChatType.HANDLER)
		{
			final String command = text.contains(";") ? text.split(";")[0].toUpperCase() : text;
			final String args = command != text ? text.substring(command.length() + 1).trim() : text;
			final IBayleeInterfaceHandler BayleeInterface = BayleeInterfaceHandler.getInstance().getBayleeInterfaceHandler(command);
			if (BayleeInterface == null)
				return;
			else
			{
				BayleeInterface.useBayleeInterfaceCommand(command, activeChar, args);
				return;
			}
		}
		else if (text.startsWith("=="))
		{
			final String expression = text.substring(2);
			Expression expr = null;

			if (!expression.isEmpty())
			{
				try
				{
					expr = ExpressionTree.parse(expression);
				}
				catch (final ExpressionParseException epe)
				{

				}

				if (expr != null)
				{
					double result;

					try
					{
						final VarMap vm = new VarMap();
						vm.setValue("adena", activeChar.getAdena());
						result = expr.eval(vm, null);
						activeChar.sendMessage(expression);
						activeChar.sendMessage("=" + Util.formatDouble(result, "NaN", false));
					}
					catch (final Exception e)
					{

					}
				}
			}

			return;
		}

		if (activeChar.isChatBlocked())
		{
			activeChar.sendPacket(SystemMsg.YOU_ARE_NOT_ALLOWED_TO_CHAT_WITH_A_CONTACT_WHILE_A_CHATTING_BLOCK_IS_IMPOSED);
			return;
		}

		final boolean globalchat = type != ChatType.ALLIANCE && type != ChatType.CLAN && type != ChatType.PARTY;
		if (globalchat || ArrayUtils.contains(Config.BAN_CHANNEL_LIST, type.ordinal()))
		{
			final BanInfo banInfo = GameBanManager.getInstance().getBanInfoIfBanned(BanBindType.CHAT, activeChar.getObjectId());
			if (banInfo != null)
			{
				if (banInfo.getEndTime() == -1)
				{
					activeChar.sendMessage(new CustomMessage("common.ChatBannedPermanently"));
				}
				else
				{
					activeChar.sendMessage(new CustomMessage("common.ChatBanned").addString(TimeUtils.toSimpleFormat(banInfo.getEndTime() * 1000L)));
				}
				activeChar.sendActionFailed();
				return;
			}

			if (activeChar.getNoChannel() != 0L)
			{
				if (activeChar.getNoChannelRemained() > 0L || activeChar.getNoChannel() < 0L)
				{
					if (activeChar.getNoChannel() > 0L)
					{
						activeChar.sendMessage(new CustomMessage("common.ChatBanned").addString(TimeUtils.toSimpleFormat(System.currentTimeMillis() + activeChar.getNoChannelRemained())));
					}
					else
					{
						activeChar.sendMessage(new CustomMessage("common.ChatBannedPermanently"));
					}
					activeChar.sendActionFailed();
					return;
				}
				activeChar.updateNoChannel(0L);
			}
		}

		if (globalchat)
		{
			if (Config.ABUSEWORD_REPLACE)
			{
				text = Config.replaceAbuseWords(text, Config.ABUSEWORD_REPLACE_STRING);
			}
			else if (Config.ABUSEWORD_BANCHAT && Config.containsAbuseWord(text))
			{
				activeChar.sendMessage(new CustomMessage("common.ChatBanned").addNumber(Config.ABUSEWORD_BANTIME * 60));
				Log.add(activeChar + ": " + text, "abuse");
				activeChar.updateNoChannel(Config.ABUSEWORD_BANTIME * 60000);
				activeChar.sendActionFailed();
				return;
			}
		}

		// Кэширование линков предметов
		Matcher m = EX_ITEM_LINK_PATTERN.matcher(text);
		ItemInstance item;
		int objectId;

		while (m.find())
		{
			objectId = Integer.parseInt(m.group(1));
			item = activeChar.getInventory().getItemByObjectId(objectId);

			if (item == null)
			{
				activeChar.sendActionFailed();
				break;
			}
			if (HidenItemsDAO.isHidden(item))
			{
				activeChar.sendActionFailed();
				return;
			}
			ItemInfoCache.getInstance().put(item);
		}

		final String translit = activeChar.getVar("translit");
		if (translit != null)
		{
			// Исключаем из транслитерации ссылки на предметы
			m = SKIP_ITEM_LINK_PATTERN.matcher(text);
			final StringBuilder sb = new StringBuilder();
			int end = 0;
			while (m.find())
			{
				sb.append(Strings.fromTranslit(text.substring(end, end = m.start()), translit.equals("tl") ? 1 : 2));
				sb.append(text.substring(end, end = m.end()));
			}

			text = sb.append(Strings.fromTranslit(text.substring(end, text.length()), translit.equals("tl") ? 1 : 2)).toString();
		}

		Log.LogChat(type.name(), activeChar.getName(), target, text);

		if (activeChar.isInFightClub() && activeChar.getFightClubEvent().isHidePersonality())
		{
			cs = new SayPacket2(0, type, 0, "Player", text);
		}
		else
		{
			cs = new SayPacket2(activeChar.getObjectId(), type, _isLocSharing, activeChar.getName(), text);
		}
		if (_isLocSharing == 1)
		{
			if (activeChar.isInOlympiadMode() || activeChar.getReflectionId() != 0)
			{
				return;
			}

			final ItemInstance l2coin = activeChar.getInventory().getItemByItemId(ItemTemplate.ITEM_ID_MONEY_L);
			final int previousRank = activeChar.getPreviousPvpRank();
			boolean allowFree = false;
			if ((previousRank > 0) && (previousRank < 4))
			{
				allowFree = true;
			}

			if (!allowFree && ((l2coin == null) || (l2coin.getCount() < Config.SHARE_POSITION_COST)))
			{
				activeChar.sendPacket(new SystemMessage(SystemMsg.NOT_ENOUGH_L2_COINS));
				return;
			}
		}
		switch (type)
		{
			case TELL:
				final Player receiver = World.getPlayer(target);
				if (receiver != null && receiver.isInOfflineMode())
				{
					activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1_IS_NOT_CURRENTLY_LOGGED_IN).addString(target), ActionFailPacket.STATIC);
				}
				else if (receiver != null && !receiver.getBlockList().contains(activeChar) && !receiver.isBlockAll())
				{
					if (!receiver.getMessageRefusal())
					{
						if (!activeChar.getAntiFlood().canTell(receiver.getObjectId(), text))
							return;

						if (activeChar.canTalkWith(receiver))
						{
							cs.setSenderInfo(activeChar, receiver);
							if (receiver.isFakePlayer())
							{
								receiver.getListeners().onChatMessageReceive(type, activeChar.getName(), text);
							}
							else
							{
								receiver.sendPacket(cs);
							}
							receiver.getAntiFlood().addInterlocutorId(activeChar.getObjectId());
						}

						cs = new SayPacket2(activeChar.getObjectId(), type, _isLocSharing, "->" + receiver.getName(), text);
						cs.setSenderInfo(activeChar, receiver);
						activeChar.sendPacket(cs);
						activeChar.getAntiFlood().addInterlocutorId(activeChar.getObjectId());
					}
					else
					{
						activeChar.sendPacket(SystemMsg.THAT_PERSON_IS_IN_MESSAGE_REFUSAL_MODE);
					}
				}
				else if (receiver == null)
				{
					activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1_IS_NOT_CURRENTLY_LOGGED_IN).addString(target), ActionFailPacket.STATIC);
				}
				else
				{
					activeChar.sendPacket(SystemMsg.YOU_HAVE_BEEN_BLOCKED_FROM_CHATTING_WITH_THAT_CONTACT, ActionFailPacket.STATIC);
				}
				break;
			case SHOUT:
				if (activeChar.isInObserverMode())
				{
					activeChar.sendPacket(SystemMsg.YOU_CANNOT_CHAT_WHILE_IN_OBSERVATION_MODE);
					return;
				}

				if (!activeChar.getAntiFlood().canShout(text))
					return;

				if (Config.GLOBAL_SHOUT)
				{
					ChatUtils.announce(activeChar, cs);
				}
				else
				{
					ChatUtils.shout(activeChar, cs);
				}

				activeChar.sendPacket(cs);
				break;
			case TRADE:
				if (activeChar.isInObserverMode())
				{
					activeChar.sendPacket(SystemMsg.YOU_CANNOT_CHAT_WHILE_IN_OBSERVATION_MODE);
					return;
				}

				if (!activeChar.getAntiFlood().canTrade(text))
					return;

				if (Config.GLOBAL_TRADE_CHAT)
				{
					ChatUtils.announce(activeChar, cs);
				}
				else
				{
					ChatUtils.shout(activeChar, cs);
				}

				activeChar.sendPacket(cs);
				break;
			case ALL:
				if (activeChar.isInObserverMode())
				{
					activeChar.sendPacket(SystemMsg.YOU_CANNOT_CHAT_WHILE_IN_OBSERVATION_MODE);
					return;
				}

				if (!activeChar.getAntiFlood().canAll(text))
					return;

				if (activeChar.isInOlympiadMode())
				{
					final OlympiadGame game = activeChar.getOlympiadGame();
					if (game != null)
					{
						ChatUtils.say(activeChar, game.getAllPlayers(true), cs);
						break;
					}
				}

				if (activeChar.isInFightClub())
				{
					ChatUtils.say(activeChar, activeChar.getFightClubEvent().getAllFightingPlayers(), cs);
				}
				else
				{
					ChatUtils.say(activeChar, cs);
					cs.setCharName(activeChar.getVisibleName(activeChar));
					activeChar.sendPacket(cs);
				}
				break;
			case CLAN:
				if (activeChar.getClan() != null)
				{
					activeChar.getClan().broadcastToOnlineMembers(cs);
				}
				break;
			case ALLIANCE:
				if (activeChar.getClan() != null && activeChar.getClan().getAlliance() != null)
				{
					activeChar.getClan().getAlliance().broadcastToOnlineMembers(cs);
				}
				break;
			case PARTY:
				if (activeChar.isInParty())
				{
					activeChar.getParty().broadCast(cs);
				}
				break;
			case PARTY_ROOM:
				final MatchingRoom room = activeChar.getMatchingRoom();
				if (room == null || room.getType() != MatchingRoom.PARTY_MATCHING)
					return;

				for (final Player roomMember : room.getPlayers())
				{
					if (activeChar.canTalkWith(roomMember))
					{
						roomMember.sendPacket(cs);
					}
				}
				break;
			case COMMANDCHANNEL_ALL:
				if (!activeChar.isInParty() || !activeChar.getParty().isInCommandChannel())
				{
					activeChar.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_USE_THE_COMMAND_CHANNEL);
					return;
				}
				if (activeChar.getParty().getCommandChannel().getChannelLeader() == activeChar)
				{
					activeChar.getParty().getCommandChannel().broadCast(cs);
				}
				else
				{
					activeChar.sendPacket(SystemMsg.ONLY_THE_COMMAND_CHANNEL_CREATOR_CAN_USE_THE_RAID_LEADER_TEXT);
				}
				break;
			case COMMANDCHANNEL_COMMANDER:
				if (!activeChar.isInParty() || !activeChar.getParty().isInCommandChannel())
				{
					activeChar.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_USE_THE_COMMAND_CHANNEL);
					return;
				}
				if (activeChar.getParty().isLeader(activeChar))
				{
					activeChar.getParty().getCommandChannel().broadcastToChannelPartyLeaders(cs);
				}
				else
				{
					activeChar.sendPacket(SystemMsg.ONLY_A_PARTY_LEADER_CAN_ACCESS_THE_COMMAND_CHANNEL);
				}
				break;
			case HERO_VOICE:
				if (!activeChar.isHero() && !activeChar.getPlayerAccess().CanAnnounce)
					return;

				// Ограничение только для героев, гм-мы пускай говорят.
				if (!activeChar.getPlayerAccess().CanAnnounce)
				{
					if (!activeChar.getAntiFlood().canHero(text))
						return;
				}

				ChatUtils.announce(activeChar, cs);

				activeChar.sendPacket(cs);
				break;
			case PETITION_PLAYER:
			case PETITION_GM:
				if (!PetitionManager.getInstance().isPlayerInConsultation(activeChar))
				{
					activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_ARE_CURRENTLY_NOT_IN_A_PETITION_CHAT));
					return;
				}

				PetitionManager.getInstance().sendActivePetitionMessage(activeChar, text);
				break;
			case BATTLEFIELD:
				if (activeChar.isInFightClub())
				{
					List<Player> list = activeChar.getFightClubEvent().getMyTeamFightingPlayers(activeChar);
					for (Player player : list)
					{
						player.sendPacket(cs);
					}
					return;
				}
				if (activeChar.getBattlefieldChatId() == 0)
					return;

				for (final Player player : GameObjectsStorage.getPlayers(false, false))
					if (!player.getBlockList().contains(activeChar) && !player.isBlockAll() && activeChar.canTalkWith(player) && player.getBattlefieldChatId() == activeChar.getBattlefieldChatId())
					{
						player.sendPacket(cs);
					}
				break;
			case MPCC_ROOM:
				final MatchingRoom mpccRoom = activeChar.getMatchingRoom();
				if (mpccRoom == null || mpccRoom.getType() != MatchingRoom.CC_MATCHING)
					return;

				for (final Player roomMember : mpccRoom.getPlayers())
				{
					if (activeChar.canTalkWith(roomMember))
					{
						roomMember.sendPacket(cs);
					}
				}
				break;
			case WORLD:
				/*
				 * if(text.equals("servershowyourrealonline")) { int total =
				 * GameObjectsStorage.getPlayers(true, true).size(); int online =
				 * GameObjectsStorage.getPlayers(false, false).size(); int offtrade =
				 * GameObjectsStorage.getOfflinePlayers().size();
				 * activeChar.sendMessage("Online: " + online + ", offtrade: " + offtrade +
				 * ", fake: " + (total - online - offtrade)); return; }
				 */

				if (!Config.ALLOW_WORLD_CHAT)
					return;

				if (activeChar.isInObserverMode())
				{
					activeChar.sendPacket(SystemMsg.YOU_CANNOT_CHAT_WHILE_IN_OBSERVATION_MODE);
					return;
				}

				if (!activeChar.getAntiFlood().canWorld(text))
					return;

				ChatUtils.announce(activeChar, cs);

				activeChar.sendPacket(cs);
				activeChar.setUsedWorldChatPoints(activeChar.getUsedWorldChatPoints() + 1);
				break;
			case HANDLER:
				break;
			default:
				_log.warn("Character " + activeChar.getName() + " used unknown chat type: " + type.ordinal() + ".");
				break;
		}
	}
}