package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 */
public class ExShowOwnthingPos implements IClientOutgoingPacket
{
	public ExShowOwnthingPos()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x00);
	}
}