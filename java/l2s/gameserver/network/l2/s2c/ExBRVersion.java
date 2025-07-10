package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExBRVersion implements IClientOutgoingPacket
{
	public ExBRVersion()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(1);
		return true;
	}
}