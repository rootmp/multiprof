package l2s.gameserver.network.l2.s2c.relics;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExRelicsExchange implements IClientOutgoingPacket
{
	private int nIndex;
	private int cResult;
	private int nRelicsID;

	public ExRelicsExchange()
	{

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nIndex);
		packetWriter.writeC(cResult);
		packetWriter.writeD(nRelicsID);

		return true;
	}
}