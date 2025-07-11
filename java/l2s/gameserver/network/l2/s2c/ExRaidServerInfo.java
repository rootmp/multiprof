package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExRaidServerInfo implements IClientOutgoingPacket
{
	public ExRaidServerInfo()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(0x00); // UNK
		packetWriter.writeC(0x00); // UNK
		return true;
	}
}