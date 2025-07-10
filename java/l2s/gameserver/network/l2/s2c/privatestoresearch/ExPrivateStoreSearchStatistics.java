package l2s.gameserver.network.l2.s2c.privatestoresearch;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.instancemanager.PrivateStoreHistoryManager;
import l2s.gameserver.model.items.PrivateStoreHistoryItem;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExPrivateStoreSearchStatistics implements IClientOutgoingPacket
{
	private final List<PrivateStoreHistoryItem> mostItems;
	private final List<PrivateStoreHistoryItem> highestItems;

	public ExPrivateStoreSearchStatistics()
	{
		mostItems = new ArrayList<>(PrivateStoreHistoryManager.getInstance().getMostItems());
		highestItems = new ArrayList<>(PrivateStoreHistoryManager.getInstance().getHighestItems());
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(mostItems.size());
		for (PrivateStoreHistoryItem item : mostItems)
		{
			packetWriter.writeD(item.getTradesCount());
			writeItemInfo(item, true, 4);
			packetWriter.writeD(item.getObjectId());
		}
		packetWriter.writeD(highestItems.size());
		for (PrivateStoreHistoryItem item : highestItems)
		{
			packetWriter.writeQ(item.getPrice());
			writeItemInfo(item, true, 4);
			packetWriter.writeD(item.getObjectId());
		}
	}
}