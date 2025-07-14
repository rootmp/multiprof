package l2s.gameserver.network.l2.c2s.pvpbook;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.PvpbookInfo;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.pvpbook.ExPvpBookList;
import l2s.gameserver.network.l2.s2c.pvpbook.ExPvpbookKillerLocation;

public class RequestExPvpbookKillerLocation implements IClientIncomingPacket
{
	private String killerName;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		killerName = packet.readSizedString();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;


		PvpbookInfo pvpbookInfo = activeChar.getPvpbook().getInfo(killerName, 1);
		if (pvpbookInfo == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (pvpbookInfo.getLocationShowCount() <= 0)
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

		if(!activeChar.getPvpbook().reduceAdenaLocationShowCount(pvpbookInfo))
			return;

		pvpbookInfo.reduceLocationShowCount();
		activeChar.sendPacket(new ExPvpBookList(activeChar));
		activeChar.sendPacket(new ExPvpbookKillerLocation(pvpbookInfo.getKillerName(), killerPlayer.getLoc()));
	}
}