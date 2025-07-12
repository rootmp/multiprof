package l2s.gameserver.network.l2.s2c.RaidAuction;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExRaidAuctionResult implements IClientOutgoingPacket
{
	public ExRaidAuctionResult()
	{}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}
