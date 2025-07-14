package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class LogOutOkPacket implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new LogOutOkPacket();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}