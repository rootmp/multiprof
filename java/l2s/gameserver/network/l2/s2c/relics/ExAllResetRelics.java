package l2s.gameserver.network.l2.s2c.relics;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExAllResetRelics implements IClientOutgoingPacket
{
	public ExAllResetRelics()
	{

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}