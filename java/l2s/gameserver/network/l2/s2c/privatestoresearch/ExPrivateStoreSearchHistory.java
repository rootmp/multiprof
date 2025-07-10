package l2s.gameserver.network.l2.s2c.privatestoresearch;

import java.util.List;

import l2s.gameserver.model.items.PrivateStoreHistoryItem;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExPrivateStoreSearchHistory extends L2GameServerPacket
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
	protected final void writeImpl()
	{
		writeC(cCurrentPage);
		writeC(cMaxPage);
		writeD(items.size());
		for (PrivateStoreHistoryItem item : items)
		{
			writeD(item.getItemId());
			writeC(item.getStoreType());
			writeC(item.getEnchantLevel());
			writeQ(item.getPrice());
			writeQ(item.getCount());
		}
	}
}