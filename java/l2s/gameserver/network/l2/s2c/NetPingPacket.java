package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class NetPingPacket implements IClientOutgoingPacket
{
	private final int timestamp;

	public NetPingPacket(final int timestamp)
	{
		this.timestamp = timestamp;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(timestamp);
		return true;
	}

}
