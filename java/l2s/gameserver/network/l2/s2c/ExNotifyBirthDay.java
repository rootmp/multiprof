package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExNotifyBirthDay implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExNotifyBirthDay();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0); // Actor OID
		return true;
	}
}