package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public final class ExNotifyFlyMoveStart implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExNotifyFlyMoveStart();

	public ExNotifyFlyMoveStart()
	{
		// trigger
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}