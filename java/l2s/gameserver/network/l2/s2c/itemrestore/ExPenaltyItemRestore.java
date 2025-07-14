package l2s.gameserver.network.l2.s2c.itemrestore;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExPenaltyItemRestore implements IClientOutgoingPacket
{
	public ExPenaltyItemRestore()
	{

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(1);
		return true;
	}
}