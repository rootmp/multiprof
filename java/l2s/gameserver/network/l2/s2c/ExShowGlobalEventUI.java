package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExShowGlobalEventUI implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExShowGlobalEventUI();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// STATIC
		return true;
	}
}