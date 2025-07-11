package l2s.gameserver.network.l2.s2c.prot_507;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExCompress implements IClientOutgoingPacket
{
	public ExCompress()
	{}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}
