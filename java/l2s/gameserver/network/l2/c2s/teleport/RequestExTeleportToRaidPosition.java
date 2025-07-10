package l2s.gameserver.network.l2.c2s.teleport;

import l2s.gameserver.data.xml.holder.TeleportListHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.BookMarkList;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.teleport.ExRaidTeleportInfo;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.TeleportTemplate;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ChatUtils;

/**
 * @author nexvill
 */
public class RequestExTeleportToRaidPosition implements IClientIncomingPacket
{
	private int _raidId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_raidId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		TeleportTemplate teleportInfo = TeleportListHolder.getInstance().getTeleportInfo(_raidId);
		if (teleportInfo == null)
		{
			activeChar.sendActionFailed();
			if (activeChar.isGM())
				ChatUtils.sys(activeChar, "Not found teleport info for ID: " + _raidId);
			return;
		}
		else
		{
			if (!BookMarkList.checkFirstConditions(activeChar) || !BookMarkList.checkTeleportConditions(activeChar)) // TODO:
																														// Check
																														// conditions.
				return;

			int freeTeleportsUsed = activeChar.getVarInt(PlayerVariables.FREE_RAID_TELEPORTS_USED, 0);
			if (freeTeleportsUsed == 0)
			{
				activeChar.bookmarkLocation = teleportInfo.getLocations().get(0);
				if (activeChar.bookmarkLocation == null)
				{
					activeChar.sendActionFailed();
					if (activeChar.isGM())
						ChatUtils.sys(activeChar, "Not found teleport coordinates for ID: " + _raidId);
					return;
				}
				activeChar.setVar(PlayerVariables.FREE_RAID_TELEPORTS_USED, ++freeTeleportsUsed);
			}
			else if (activeChar.getInventory().destroyItemByItemId(ItemTemplate.ITEM_ID_MONEY_L, 10))
			{
				activeChar.getInventory().destroyItemByItemId(ItemTemplate.ITEM_ID_MONEY_L, 10);
				activeChar.bookmarkLocation = teleportInfo.getLocations().get(0);
				if (activeChar.bookmarkLocation == null)
				{
					activeChar.sendActionFailed();
					if (activeChar.isGM())
						ChatUtils.sys(activeChar, "Not found teleport coordinates for ID: " + _raidId);
					return;
				}
			}
			else
			{
				activeChar.sendPacket(new SystemMessagePacket(SystemMsg.NOT_ENOUGH_L2_COINS));
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

		activeChar.sendPacket(new ExRaidTeleportInfo(activeChar));
	}
}