package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExShowEnsoulWindow implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExShowEnsoulWindow();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// STATIC
		return true;
	}
}