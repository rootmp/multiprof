package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class WareHouseDonePacket implements IClientOutgoingPacket
{

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0); // ?
		return true;
	}
}