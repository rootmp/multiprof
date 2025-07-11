package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExPledgeCount implements IClientOutgoingPacket
{
	private final int _count;

	public ExPledgeCount(int count)
	{
		_count = count;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_count);
		return true;
	}
}