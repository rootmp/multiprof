package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExDominionWarStart implements IClientOutgoingPacket
{
	public ExDominionWarStart()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00);
		packetWriter.writeD(0x00);
		packetWriter.writeD(0x00); // territory Id
		packetWriter.writeD(0x00);
		packetWriter.writeD(0x00); // territory Id
		return true;
	}
}
