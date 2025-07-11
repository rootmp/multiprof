package l2s.gameserver.network.l2.s2c.relics;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.relics.RelicsInfo;

public class ExRelicsList implements IClientOutgoingPacket
{
	private int nIndex;
	private int nMaxIndex;
	private List<RelicsInfo> relicsList;

	public ExRelicsList(int nIndex, int nMaxIndex, List<RelicsInfo> relicsList)
	{
		this.nIndex = nIndex;
		this.nMaxIndex = nMaxIndex;
		this.relicsList = relicsList;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nIndex);
		packetWriter.writeD(nMaxIndex);

		packetWriter.writeD(relicsList.size());
		for(RelicsInfo relic : relicsList)
		{
			packetWriter.writeD(relic.nRelicsID);
			packetWriter.writeD(relic.nLevel);
			packetWriter.writeD(relic.nCount);
		}
		return true;
	}
}