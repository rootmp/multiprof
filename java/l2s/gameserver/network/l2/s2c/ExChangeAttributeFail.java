package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 */
public class ExChangeAttributeFail implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExChangeAttributeFail();

	public ExChangeAttributeFail()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}