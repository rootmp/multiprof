package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * Opens the CommandChannel Information window
 */
public class ExOpenMPCCPacket implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExOpenMPCCPacket();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}