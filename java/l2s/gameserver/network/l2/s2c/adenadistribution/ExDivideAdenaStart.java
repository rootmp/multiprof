package l2s.gameserver.network.l2.s2c.adenadistribution;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

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