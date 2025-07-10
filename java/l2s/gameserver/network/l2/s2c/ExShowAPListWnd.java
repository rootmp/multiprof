package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExShowAPListWnd implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExShowAPListWnd();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		//
	}
}