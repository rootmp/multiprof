package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExEnSoulExtractionShow implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExEnSoulExtractionShow();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// STATIC
		return true;
	}
}