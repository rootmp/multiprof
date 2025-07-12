package l2s.gameserver.network.l2.s2c.RaidAuction;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExRaidAuctionBidInfo implements IClientOutgoingPacket
{
	private int nID;
	private int nOrder;
	private int nMaxNum;
	private int nClassID;
	private long nAmount;

	public ExRaidAuctionBidInfo(int nID, int nOrder, int nMaxNum, int nClassID, long nAmount)
	{
		this.nID = nID;
		this.nOrder = nOrder;
		this.nMaxNum = nMaxNum;
		this.nClassID = nClassID;
		this.nAmount = nAmount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nID);
		packetWriter.writeD(nOrder);
		packetWriter.writeD(nMaxNum);
		packetWriter.writeD(nClassID);
		packetWriter.writeQ(nAmount);
		return true;
	}
}
