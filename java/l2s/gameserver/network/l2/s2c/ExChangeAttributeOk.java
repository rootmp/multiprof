package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 */
public class ExChangeAttributeOk implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExChangeAttributeOk();

	public ExChangeAttributeOk()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}