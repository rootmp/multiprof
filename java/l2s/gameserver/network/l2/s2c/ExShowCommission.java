package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 */
public class ExShowCommission implements IClientOutgoingPacket
{
	public ExShowCommission()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0x01); // ??Open??
		return true;
	}
}