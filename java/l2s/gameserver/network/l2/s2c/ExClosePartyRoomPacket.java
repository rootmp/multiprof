package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExClosePartyRoomPacket implements IClientOutgoingPacket
{
	public static L2GameServerPacket STATIC = new ExClosePartyRoomPacket();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}