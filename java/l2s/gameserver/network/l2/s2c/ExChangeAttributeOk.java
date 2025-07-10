package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 */
public class ExChangeAttributeOk implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExChangeAttributeOk();

	public ExChangeAttributeOk()
	{
		//
	}

	public boolean write(PacketWriter packetWriter)
	{
	}
}