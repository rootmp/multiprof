package l2s.gameserver.network.l2.c2s.teleport;

import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.TeleportListHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.BookMarkList;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.TeleportTemplate;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ChatUtils;

/**
 * @author nexvill
 **/
public class RequestExRequestTeleport extends L2GameClientPacket
{
	private int teleportId;

	@Override
	protected boolean readImpl()
	{
		teleportId = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		TeleportTemplate teleportInfo = TeleportListHolder.getInstance().getTeleportInfo(teleportId);
		if (teleportInfo == null)
		{
			activeChar.sendActionFailed();
			if (activeChar.isGM())
				ChatUtils.sys(activeChar, "Not found teleport info for ID: " + teleportId);
			return;
		}

		if (!BookMarkList.checkFirstConditions(activeChar) || !BookMarkList.checkTeleportConditions(activeChar)) // TODO:
																													// Check
																													// conditions.
			return;

		List<Location> locations = teleportInfo.getLocations();
		int itemId = teleportInfo.getItemId();
		long itemCount = teleportInfo.getPrice();
		if (locations.size() > 1)
		{
			int rndLoc = Rnd.get(locations.size());
			activeChar.bookmarkLocation = locations.get(rndLoc);
		}
		else
		{
			activeChar.bookmarkLocation = locations.get(0);
		}

		if (activeChar.bookmarkLocation == null)
		{
			activeChar.sendActionFailed();
			if (activeChar.isGM())
				ChatUtils.sys(activeChar, "Not found teleport coordinates for ID: " + teleportId);
			return;
		}

		if (itemId == ItemTemplate.ITEM_ID_ADENA)
		{
			if (activeChar.getLevel() > Config.GATEKEEPER_FREE && !activeChar.reduceAdena(teleportInfo.getPrice(), true))
			{
				activeChar.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA); // TODO: Check message.
				return;
			}
		}
		else
		{
			if (!activeChar.getInventory().destroyItemByItemId(itemId, itemCount))
			{
				activeChar.sendPacket(SystemMsg.NOT_ENOUGH_ITEMS); // TODO: Check message.
				return;
			}
		}

		SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 60018, 1);
		if (!skillEntry.checkCondition(activeChar, activeChar, false, true, true))
		{
			activeChar.bookmarkLocation = null;
			return;
		}

		activeChar.getAI().Cast(skillEntry, activeChar, false, true);
	}
}
