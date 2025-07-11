package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExRaidCharSelected implements IClientOutgoingPacket
{
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// just a trigger
		return true;
	}
}