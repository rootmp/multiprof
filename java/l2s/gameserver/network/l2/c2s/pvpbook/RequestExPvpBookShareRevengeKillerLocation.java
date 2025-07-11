package l2s.gameserver.network.l2.c2s.pvpbook;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Pvpbook;
import l2s.gameserver.model.actor.instances.player.PvpbookInfo;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.pvpbook.ExPvpBookShareRevengeKillerLocation;
import l2s.gameserver.network.l2.s2c.pvpbook.ExPvpBookShareRevengeList;

/**
 * @author nexvill
 */
public class RequestExPvpBookShareRevengeKillerLocation implements IClientIncomingPacket
{
	private String killedName;
	private String killerName;

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

		if (activeChar.getPvpbook().getLocationShowCount() <= 0)
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
			activeChar.sendPacket(SystemMsg.THE_CHARACTER_IS_IN_A_LOCATION_WHERE_IT_IS_IMPOSSIBLE_TO_USE_THIS_FUNCTION);
			return;
		}

		if (!activeChar.reduceAdena(Pvpbook.LOCATION_SHOW_PRICE, true))
		{
			activeChar.sendPacket(SystemMsg.NOT_ENOUGH_MONEY_TO_USE_THE_FUNCTION);
			return;
		}

		activeChar.getPvpbook().reduceLocationShowCount();
		activeChar.sendPacket(new ExPvpBookShareRevengeList(activeChar));
		activeChar.sendPacket(new ExPvpBookShareRevengeKillerLocation(pvpbookInfo.getKillerName(), killerPlayer.getLoc()));
	}
}