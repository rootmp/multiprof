package l2s.gameserver.network.l2.c2s.pvpbook;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Pvpbook;
import l2s.gameserver.model.actor.instances.player.PvpbookInfo;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.skills.skillclasses.Call;

/**
 * @author nexvill
 */
public class RequestExPvpBookShareRevengeSharedTeleportToKiller implements IClientIncomingPacket
{
	private String killedName;
	private String killerName;
	private static final SkillEntry HIDE_SKILLENTRY = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 922, 1);

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		killedName = packet.readString();
		killerName = packet.readString();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.getPvpbook().getTeleportHelpCount() <= 0)
		{
			activeChar.sendActionFailed();
			return;
		}

		PvpbookInfo pvpbookInfo = activeChar.getPvpbook().getInfo(killerName);
		if (pvpbookInfo == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		Player killerPlayer = pvpbookInfo.getKiller();
		if (killerPlayer == null || !killerPlayer.isOnline())
		{
			activeChar.sendPacket(SystemMsg.THE_TARGET_IS_NO_ONLINE_YOU_CANT_USE_THIS_FUNCTION);
			return;
		}

		if (!killerPlayer.getReflection().isMain())
		{
			activeChar.sendPacket(SystemMsg.THE_CHARACTER_IS_IN_A_LOCATION_WHERE_IT_IS_IMPOSSIBLE_TO_USE_THIS_FUNCTION_2);
			return;
		}

		if (Call.canSummonHere(killerPlayer) != null || Call.canBeSummoned(activeChar) != null)
		{
			activeChar.sendPacket(SystemMsg.THE_CHARACTER_IS_IN_A_LOCATION_WHERE_IT_IS_IMPOSSIBLE_TO_USE_THIS_FUNCTION_2);
			return;
		}

		if (!activeChar.getInventory().destroyItemByItemId(91663, Pvpbook.TELEPORT_HELP_PRICE))
		{
			activeChar.sendPacket(SystemMsg.NOT_ENOUGH_L2_COINS);
			return;
		}

		HIDE_SKILLENTRY.getEffects(activeChar, activeChar);
		activeChar.getPvpbook().reduceTeleportCount();
		activeChar.teleToLocation(Location.findPointToStay(killerPlayer, 100, 150), ReflectionManager.MAIN);
	}
}