package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ServerCloseSocketPacket implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ServerCloseSocketPacket();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}