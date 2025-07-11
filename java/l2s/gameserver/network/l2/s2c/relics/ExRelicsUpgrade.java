package l2s.gameserver.network.l2.s2c.relics;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExRelicsUpgrade implements IClientOutgoingPacket
{
	private int cResult;
	private int nRelicsID;
	private int nLevel;

	public ExRelicsUpgrade(int cResult, int nRelicsID, int nLevel)
	{
		this.cResult = cResult;
		this.nRelicsID = nRelicsID;
		this.nLevel = nLevel;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cResult);
		packetWriter.writeD(nRelicsID);
		packetWriter.writeD(nLevel);
		return true;
	}
}