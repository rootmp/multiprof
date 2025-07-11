package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExClaschangeSetAlarm implements IClientOutgoingPacket
{
	public static final ExClaschangeSetAlarm STATIC = new ExClaschangeSetAlarm();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}
