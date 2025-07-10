package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 */
public class ExDissmissMpccRoom implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExDissmissMpccRoom();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
	}
}