package l2s.gameserver.network.l2.c2s.pvpbook;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.PvpbookInfo;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author nexvill
 */
public class RequestExPvpbookShareRevengeSharedTeleportToKiller implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private String killedName;
	private String killerName;
	private static final SkillEntry HIDE_SKILLENTRY = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 922, 1);

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		killedName = packet.readSizedString();
		killerName = packet.readSizedString();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		PvpbookInfo pvpbookInfo = activeChar.getPvpbook().getInfo(killerName, 2);
		if (pvpbookInfo == null)
		{
			pvpbookInfo = activeChar.getPvpbook().getInfo(killerName, 1);
			if (pvpbookInfo == null)
			{
				activeChar.sendActionFailed();
				return;
			}
		}

		
		if (pvpbookInfo.getTeleportHelpCount() <= 0)
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

		if (!killerPlayer.getReflection().isMain() || !activeChar.getReflection().isMain())
		{
			activeChar.sendPacket(SystemMsg.THE_CHARACTER_IS_IN_A_LOCATION_WHERE_IT_IS_IMPOSSIBLE_TO_USE_THIS_FUNCTION_2);
			return;
		}
		
		if(killerPlayer.isInPeaceZone() || killerPlayer.isInBoat()|| killerPlayer.isInOlympiadMode() || killerPlayer.isInObserverMode() || killerPlayer.isFlying())
		{
			activeChar.sendPacket(SystemMsg.THE_CHARACTER_IS_IN_A_LOCATION_WHERE_IT_IS_IMPOSSIBLE_TO_USE_THIS_FUNCTION_2);
			return;
		}
		
		if(activeChar.isFlying() || activeChar.isOutOfControl()/* || activeChar.isInZone(ZoneType.hellbound)*/ || activeChar.isInOlympiadMode())
		{
			activeChar.sendPacket(SystemMsg.THE_CHARACTER_IS_IN_A_LOCATION_WHERE_IT_IS_IMPOSSIBLE_TO_USE_THIS_FUNCTION_2);
			return;
		}

		if (/*killerPlayer.isInZone(ZoneType.hellbound) ||*/ !checkRaid(killerPlayer))
		{
			activeChar.sendPacket(SystemMsg.THE_CHARACTER_IS_IN_A_LOCATION_WHERE_IT_IS_IMPOSSIBLE_TO_USE_THIS_FUNCTION_2);
			return;
		}

		if (!ItemFunctions.deleteItem(activeChar, 91663, Config.PVPBOOK_TELEPORT_HELP_PRICE))
		{
			activeChar.sendPacket(SystemMsg.NOT_ENOUGH_L2_COINS);
			return;
		}

		HIDE_SKILLENTRY.getEffects(activeChar, activeChar);
		pvpbookInfo.reduceTeleportHelpCount();
		activeChar.teleToLocation(Location.findPointToStay(killerPlayer, 100, 150), ReflectionManager.MAIN);
	}
	
	private boolean checkRaid(Player killerPlayer)
	{
		for(MonsterInstance m : killerPlayer.getAroundMonsters(3000, 300))
		{
			if(m.isRaid()&& !m.isDead()&& m.isVisible())
				return false;
		}
		return true;
	}
}