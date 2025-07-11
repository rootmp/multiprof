package l2s.gameserver.network.l2.s2c.relics;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.relics.RelicsInfo;

public class ExRelicsUpdateList implements IClientOutgoingPacket
{
	private List<RelicsInfo> relicsList;

	public ExRelicsUpdateList(List<RelicsInfo> relicsList)
	{
		this.relicsList = relicsList;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
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
