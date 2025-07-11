package l2s.gameserver.network.l2.s2c.relics;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExRelicsActiveInfo implements IClientOutgoingPacket
{
	private int nRelicsID;
	private int nLevel;

	public ExRelicsActiveInfo(int nRelicsID, int nLevel)
	{
		this.nRelicsID = nRelicsID;
		this.nLevel = nLevel;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nRelicsID);
		packetWriter.writeD(nLevel);
		return true;
	}
}