package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExItemAuctionStatus implements IClientOutgoingPacket
{
	public ExItemAuctionStatus()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(0x00);
		packetWriter.writeH(0x00);
		packetWriter.writeH(0x00);
		packetWriter.writeH(0x00);
		packetWriter.writeH(0x00);
		packetWriter.writeH(0x00);
		packetWriter.writeD(0x00);
		packetWriter.writeC(0x00);
		return true;
	}
}