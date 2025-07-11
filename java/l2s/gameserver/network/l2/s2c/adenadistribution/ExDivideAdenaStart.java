package l2s.gameserver.network.l2.s2c.adenadistribution;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author Sdw
 */
public class ExDivideAdenaStart implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExDivideAdenaStart();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		//
		return true;
	}
}