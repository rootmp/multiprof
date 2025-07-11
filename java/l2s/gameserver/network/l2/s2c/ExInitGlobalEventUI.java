package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExInitGlobalEventUI implements IClientOutgoingPacket
{
	public ExInitGlobalEventUI()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(0); // eventList size
		return true;
	}
}