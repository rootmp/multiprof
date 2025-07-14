package l2s.gameserver.network.l2.s2c;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.templates.randomCraft.RandomCraftRewardData;

public class ExCraftSlotProbList implements IClientOutgoingPacket
{
	private int nSlot;
	private List<RandomCraftRewardData> items;

	public ExCraftSlotProbList(int nSlot, List<RandomCraftRewardData> items)
	{
		this.nSlot = nSlot;
		this.items = items;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nSlot);
		packetWriter.writeD(items.size());
		for(RandomCraftRewardData item : items)
		{
			packetWriter.writeD(item.getItemId());
			packetWriter.writeQ(item.getCount());
			packetWriter.writeD((int) item.getChance());
		}
		return true;
	}
}