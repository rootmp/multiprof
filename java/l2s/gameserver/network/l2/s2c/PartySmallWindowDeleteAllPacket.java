package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class PartySmallWindowDeleteAllPacket implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new PartySmallWindowDeleteAllPacket();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
	}
}