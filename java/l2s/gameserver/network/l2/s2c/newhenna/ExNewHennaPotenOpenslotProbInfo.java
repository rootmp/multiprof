package l2s.gameserver.network.l2.s2c.newhenna;

import java.util.Collections;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.item.data.ItemData;

public class ExNewHennaPotenOpenslotProbInfo implements IClientOutgoingPacket
{
	private int nReqOpenSlotStep;
	private int nReqOpenSlotProb;
	private List<ItemData> vReqOpenSlotCostItemList = Collections.emptyList();

	public ExNewHennaPotenOpenslotProbInfo()
	{
		//System.out.println("NOTDONE " + this.getClass().getSimpleName()); 
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(nReqOpenSlotStep);
		packetWriter.writeD(nReqOpenSlotProb);

		packetWriter.writeD(vReqOpenSlotCostItemList.size());
		for(ItemData failItem : vReqOpenSlotCostItemList)
		{
			packetWriter.writeD(failItem.getId());
			packetWriter.writeD((int) failItem.getCount());
		}

		return true;
	}
}
