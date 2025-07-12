package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExShowCreatePledge implements IClientOutgoingPacket
{
	public ExShowCreatePledge()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}