package l2s.gameserver.model.instances;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.CoupleManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.Couple;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.components.HtmlMessage;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.npc.NpcTemplate;

public class WeddingManagerInstance extends NpcInstance
{
	public WeddingManagerInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	public void showChatWindow(Player player, int val, boolean firstTalk, Object... arg)
	{
		String filename = "wedding/start.htm";
		String replace = "";
		HtmlMessage html = new HtmlMessage(this).setPlayVoice(firstTalk);
		html.setFile(filename);
		html.replace("%replace%", replace);
		player.sendPacket(html);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		// standard msg
		String filename = "wedding/start.htm";
		String replace = "";

		// if player has no partner
		if(player.getPartnerId() == 0)
		{
			filename = "wedding/nopartner.htm";
			sendHtmlMessage(player, filename, replace);
			return;
		}

		Player ptarget = GameObjectsStorage.getPlayer(player.getPartnerId());

		// partner online ?
		if(ptarget == null || !ptarget.isOnline())
		{
			filename = "wedding/notfound.htm";
			sendHtmlMessage(player, filename, replace);
			return;
		}
		else if(player.isMaried()) // already married ?
		{
			filename = "wedding/already.htm";
			sendHtmlMessage(player, filename, replace);
			return;
		}
		else if(command.startsWith("AcceptWedding"))
		{
			// accept the wedding request
			player.setMaryAccepted(true);
			Couple couple = CoupleManager.getInstance().getCouple(player.getCoupleId());
			couple.marry();

			// messages to the couple
			player.sendMessage(new CustomMessage("l2s.gameserver.model.instances.L2WeddingManagerMessage"));
			player.setMaried(true);
			player.setMaryRequest(false);
			ptarget.sendMessage(new CustomMessage("l2s.gameserver.model.instances.L2WeddingManagerMessage"));
			ptarget.setMaried(true);
			ptarget.setMaryRequest(false);

			// wedding march
			player.broadcastPacket(new MagicSkillUse(player, player, 2230, 1, 1, 0));
			ptarget.broadcastPacket(new MagicSkillUse(ptarget, ptarget, 2230, 1, 1, 0));

			// fireworks
			player.broadcastPacket(new MagicSkillUse(player, player, 2025, 1, 1, 0));
			ptarget.broadcastPacket(new MagicSkillUse(ptarget, ptarget, 2025, 1, 1, 0));

			Announcements.announceToAllFromStringHolder("l2s.gameserver.model.instances.L2WeddingManagerMessage.announce", player.getName(), ptarget.getName());

			filename = "wedding/accepted.htm";
			replace = ptarget.getName();
			sendHtmlMessage(ptarget, filename, replace);
			return;
		}
		else if(player.isMaryRequest())
		{
			// check for formalwear
			if(Config.WEDDING_FORMALWEAR && !isWearingFormalWear(player))
			{
				filename = "wedding/noformal.htm";
				sendHtmlMessage(player, filename, replace);
				return;
			}
			filename = "wedding/ask.htm";
			player.setMaryRequest(false);
			ptarget.setMaryRequest(false);
			replace = ptarget.getName();
			sendHtmlMessage(player, filename, replace);
			return;
		}
		else if(command.startsWith("AskWedding"))
		{
			// check for formalwear
			if(Config.WEDDING_FORMALWEAR && !isWearingFormalWear(player))
			{
				filename = "wedding/noformal.htm";
				sendHtmlMessage(player, filename, replace);
				return;
			}
			else if(player.getAdena() < Config.WEDDING_PRICE)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			else
			{
				player.setMaryAccepted(true);
				ptarget.setMaryRequest(true);
				replace = ptarget.getName();
				filename = "wedding/requested.htm";
				player.reduceAdena(Config.WEDDING_PRICE, true);
				sendHtmlMessage(player, filename, replace);
				return;
			}
		}
		else if(command.startsWith("DeclineWedding"))
		{
			player.setMaryRequest(false);
			ptarget.setMaryRequest(false);
			player.setMaryAccepted(false);
			ptarget.setMaryAccepted(false);
			player.sendMessage("You declined");
			ptarget.sendMessage("Your partner declined");
			replace = ptarget.getName();
			filename = "wedding/declined.htm";
			sendHtmlMessage(ptarget, filename, replace);
			return;
		}
		else if(player.isMaryAccepted())
		{
			filename = "wedding/waitforpartner.htm";
			sendHtmlMessage(player, filename, replace);
			return;
		}
		sendHtmlMessage(player, filename, replace);
	}

	private static boolean isWearingFormalWear(Player player)
	{
		if(player != null && player.getInventory() != null
				&& player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST) == ItemTemplate.ITEM_ID_FORMAL_WEAR)
			return true;
		return false;
	}

	private void sendHtmlMessage(Player player, String filename, String replace)
	{
		HtmlMessage html = new HtmlMessage(this);
		html.setFile(filename);
		html.replace("%replace%", replace);
		player.sendPacket(html);
	}
}