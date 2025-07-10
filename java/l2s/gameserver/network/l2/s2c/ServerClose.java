package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.commons.network.PacketWriter;

/**
 * @author devScarlet, mrTJO
 */
public class ServerClose implements IClientOutgoingPacket
{
	public static final ServerClose STATIC_PACKET = new ServerClose();
	
	private ServerClose()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		//OutgoingPackets.SEVER_CLOSE.writeId(packet);
		return true;
	}
}
