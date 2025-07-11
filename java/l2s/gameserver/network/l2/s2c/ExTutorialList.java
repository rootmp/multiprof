package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExTutorialList implements IClientOutgoingPacket
{
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS("");
		packetWriter.writeD(0x00);
		packetWriter.writeD(0x00);
		packetWriter.writeD(0x00);
		return true;
	}
}
