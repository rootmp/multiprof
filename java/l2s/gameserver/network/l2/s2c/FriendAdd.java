package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class FriendAdd implements IClientOutgoingPacket
{
	public FriendAdd()
	{}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}