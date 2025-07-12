package l2s.gameserver.network.l2.s2c.worldexchange;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExWorldExchangeInfo implements IClientOutgoingPacket
{
	private final int nMaxSlot;

	public ExWorldExchangeInfo(int maxSlot)
	{
		nMaxSlot = maxSlot;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nMaxSlot);
		return true;
	}
}
