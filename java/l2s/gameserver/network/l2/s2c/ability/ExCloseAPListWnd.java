package l2s.gameserver.network.l2.s2c.ability;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExCloseAPListWnd implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExCloseAPListWnd();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		//
		return true;
	}
}