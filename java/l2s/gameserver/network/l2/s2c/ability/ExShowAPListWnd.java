package l2s.gameserver.network.l2.s2c.ability;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExShowAPListWnd implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExShowAPListWnd();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		//
		return true;
	}
}