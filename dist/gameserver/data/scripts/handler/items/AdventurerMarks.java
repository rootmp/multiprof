package handler.items;

import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.HtmlMessage;

/**
 * @author nexvill
 */
public class AdventurerMarks extends SimpleItemHandler
{
	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		int itemId = item.getItemId();

		switch (itemId)
		{
			case 91654:
			{
				HtmlMessage msg = new HtmlMessage(0);
				msg.setItemId(91654);
				msg.setFile(correctBypassLink(player, "adventurer_mark.htm"));
				player.sendPacket(msg);
				break;
			}
			case 91655:
			{
				HtmlMessage msg = new HtmlMessage(0);
				msg.setItemId(91655);
				msg.setFile(correctBypassLink(player, "adventurer_mark.htm"));
				player.sendPacket(msg);
				break;
			}
			case 91656:
			{
				HtmlMessage msg = new HtmlMessage(0);
				msg.setItemId(91656);
				msg.setFile(correctBypassLink(player, "adventurer_mark.htm"));
				player.sendPacket(msg);
				break;
			}
			case 91657:
			{
				HtmlMessage msg = new HtmlMessage(0);
				msg.setItemId(91657);
				msg.setFile(correctBypassLink(player, "adventurer_mark.htm"));
				player.sendPacket(msg);
				break;
			}
			case 91931:
			{
				HtmlMessage msg = new HtmlMessage(0);
				msg.setItemId(91931);
				msg.setFile(correctBypassLink(player, "adventurer_mark.htm"));
				player.sendPacket(msg);
				break;
			}
			default:
				return false;
		}
		return true;
	}

	private static String correctBypassLink(Player player, String link)
	{
		String path = "default/" + link;
		if (HtmCache.getInstance().getIfExists(path, player) != null)
			return path;
		return link;
	}
}
