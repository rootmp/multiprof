package l2s.gameserver.network.l2.s2c.privatestoresearch;

import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.items.PrivateStoreHistoryItem;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExPrivateStoreSearchHistory implements IClientOutgoingPacket
{
	public static final int ITEMS_LIMIT_PER_PAGE = 100;

	private final int cCurrentPage;
	private final int cMaxPage;
	private final List<PrivateStoreHistoryItem> items;

	public ExPrivateStoreSearchHistory(int cCurrentPage, int cMaxPage, List<PrivateStoreHistoryItem> items)
	{
		this.cCurrentPage = cCurrentPage;
		this.cMaxPage = cMaxPage;
		this.items = items;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cCurrentPage);
		packetWriter.writeC(cMaxPage);
		packetWriter.writeD(items.size());
		for(PrivateStoreHistoryItem item : items)
		{
			packetWriter.writeD(item.getItemId());
			packetWriter.writeC(item.getStoreType());
			packetWriter.writeC(item.getEnchantLevel());
			packetWriter.writeQ(item.getPrice());
			packetWriter.writeQ(item.getCount());
		}
		return true;
	}
}