package l2s.gameserver.network.l2.s2c.relics;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.item.data.ItemData;

public class ExRelicsCombination implements IClientOutgoingPacket
{
	private int cResult;
	private List<Integer> relicsList;
	private List<ItemData> failItemList;

	public ExRelicsCombination(int cResult, List<Integer> relicsList, List<ItemData> failItemList)
	{
		this.cResult = cResult;
		this.relicsList = relicsList;
		this.failItemList = failItemList;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cResult);
		packetWriter.writeD(relicsList.size());
		for(int relic : relicsList)
		{
			packetWriter.writeD(relic);
		}
		packetWriter.writeD(failItemList.size());
		for(ItemData failItem : failItemList)
		{
			packetWriter.writeD(failItem.getId());
			packetWriter.writeD((int) failItem.getCount());
		}

		return true;
	}
}
