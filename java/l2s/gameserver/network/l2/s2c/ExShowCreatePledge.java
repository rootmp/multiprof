package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

/**
 * @author Bonux (bonuxq@gmail.com)
 * @date 25.09.2019
 **/
public class ExShowCreatePledge implements IClientOutgoingPacket
{
	public static final ExShowCreatePledge STATIC = new ExShowCreatePledge();

	private ExShowCreatePledge()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		//
	}
}