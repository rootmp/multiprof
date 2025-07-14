package l2s.gameserver.utils;

import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.HtmlMessage;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExEnSoulExtractionShow;
import l2s.gameserver.network.l2.s2c.ExGetPremiumItemListPacket;
import l2s.gameserver.network.l2.s2c.ExShowEnsoulWindow;
import l2s.gameserver.network.l2.s2c.ExShowGlobalEventUI;
import l2s.gameserver.network.l2.s2c.ExShowUpgradeSystemNormal;

public class DimensionalMerchantUtils
{
	public final static String DM_HTML_FILE_PATH = "dimensional_merchant/";

	public static void showLimitShopHtml(NpcInstance npc, Player player, String fileName, boolean premium)
	{
		// if (npc == null)
		// {
		HtmlMessage msg = new HtmlMessage(0);
		msg.setItemId(premium ? -1 : 0);
		msg.setFile(correctBypassLink(player, fileName));
		player.sendPacket(msg);
		// } else
		// {
		// npc.showChatWindow(player, correctBypassLink(player, fileName), false);
		// }
	}

	public static void showLimitShopHtml(Player player, String fileName, boolean premium)
	{
		showLimitShopHtml(null, player, fileName, premium);
	}

	public static String correctBypassLink(Player player, String link)
	{
		String path = DM_HTML_FILE_PATH + link;
		if(HtmCache.getInstance().getIfExists(path, player) != null)
			return path;
		return link;
	}

	public static boolean onMenuSelect(NpcInstance npc, Player player, int ask, long reply, int state)
	{
		if(ask == 1) // Receive incoming premium items.
		{
			if(reply == 1)
			{
				if(player.getPremiumItemList().isEmpty())
				{
					player.sendPacket(SystemMsg.THERE_ARE_NO_MORE_VITAMIN_ITEMS_TO_BE_FOUND);
					return true;
				}
				player.sendPacket(new ExGetPremiumItemListPacket(player));
				return true;
			}
		}
		else if(ask == -201903153)
		{
			if(reply == 300)
			{
				player.sendPacket(ExShowEnsoulWindow.STATIC);
				return true;
			}
			else if(reply == 302)
			{
				player.sendPacket(ExEnSoulExtractionShow.STATIC);
				return true;
			}
		}
		else if((ask == -20200320) && (reply == 99))
		{
			player.sendPacket(new ExShowUpgradeSystemNormal(1));
			return true;
		}
		else if((ask == -9999) && (reply == 3))
		{
			player.sendPacket(ExShowGlobalEventUI.STATIC);
			return true;
		}

		return false;
	}
}
