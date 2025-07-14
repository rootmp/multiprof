package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExMercenaryCastlewarCastleSiegeAttackerList;

public class RequestExMercenaryCastlewarCastleSiegeAttackerList implements IClientIncomingPacket
{
	private int castleId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		castleId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, castleId);
		if(castle == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.sendPacket(new ExMercenaryCastlewarCastleSiegeAttackerList(castle));
	}
}
