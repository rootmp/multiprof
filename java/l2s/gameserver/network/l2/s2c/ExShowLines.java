package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExShowLines implements IClientOutgoingPacket
{
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// TODO hdcc cx[ddd]
		return true;
	}
}