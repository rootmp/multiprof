package l2s.gameserver.network.l2.s2c.relics;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExRelicsExchangeConfirm implements IClientOutgoingPacket
{
	private int nIndex;
	private int cResult;
	private int nRelicsID;

	public ExRelicsExchangeConfirm()
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