package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExShowQuestInfoPacket implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExShowQuestInfoPacket();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}