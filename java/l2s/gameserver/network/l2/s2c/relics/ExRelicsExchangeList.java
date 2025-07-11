package l2s.gameserver.network.l2.s2c.relics;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.relics.RelicsExchangeInfo;

public class ExRelicsExchangeList implements IClientOutgoingPacket
{
	private int nMaxList;
	private List<RelicsExchangeInfo> tradeList;

	public ExRelicsExchangeList()
	{

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nMaxList);

		packetWriter.writeD(tradeList.size());
		for(RelicsExchangeInfo t : tradeList)
		{
			packetWriter.writeD(t.nIndex);
			packetWriter.writeD(t.nRelicsID);
			packetWriter.writeD(t.nRemainCount);
			packetWriter.writeD(t.nMaxCount);
			packetWriter.writeD(t.nEndTime);
		}
		return true;
	}
}