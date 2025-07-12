package l2s.gameserver.network.l2.c2s.RaidAuction;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExRaidAuctionBid implements IClientIncomingPacket
{
	private int nID;
	private int nOrder;
	private long nAmount;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nID = packet.readD();
		nOrder = packet.readD();
		nAmount = packet.readQ();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;
	}
}
