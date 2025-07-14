package l2s.gameserver.network.l2.s2c.randomcraft;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExCraftRandomLockSlot implements IClientOutgoingPacket
{
	public ExCraftRandomLockSlot()
	{}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(0);
		return true;
	}
}
