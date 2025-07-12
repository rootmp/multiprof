package l2s.gameserver.network.l2.s2c.RaidAuction;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExRaidAuctionPostAlarm implements IClientOutgoingPacket
{
	private int nOrder;
	private boolean bSuccess;
	private String sName;

	public ExRaidAuctionPostAlarm(int nOrder, boolean bSuccess, String sName)
	{
		this.nOrder = nOrder;
		this.bSuccess = bSuccess;
		this.sName = sName;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nOrder);
		packetWriter.writeC(bSuccess);
		packetWriter.writeS(sName);
		return true;
	}
}
