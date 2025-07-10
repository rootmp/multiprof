package l2s.gameserver.network.l2.s2c.privatestoresearch;

import java.util.List;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.items.TradeItem;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExPrivateStoreSearchItem extends L2GameServerPacket
{
	public static class Item
	{
		private final String ownerName;
		private final int storeType;
		private final Location loc;
		private final TradeItem tradeItem;

		public Item(String ownerName, int storeType, Location loc, TradeItem tradeItem)
		{
			this.ownerName = ownerName;
			this.storeType = storeType;
			this.loc = loc;
			this.tradeItem = tradeItem;
		}

		public String getOwnerName()
		{
			return ownerName;
		}

		public int getStoreType()
		{
			return storeType;
		}

		public Location getLoc()
		{
			return loc;
		}

		public TradeItem getTradeItem()
		{
			return tradeItem;
		}
	}

	public static final int ITEMS_LIMIT_PER_PAGE = 100;

	private final int cCurrentPage;
	private final int cMaxPage;
	private final List<Item> items;

	public ExPrivateStoreSearchItem(int cCurrentPage, int cMaxPage, List<Item> items)
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
		for (Item item : items)
		{
			writeString(item.getOwnerName());
			writeC(item.getStoreType());
			writeQ(item.getTradeItem().getOwnersPrice());
			writeD(item.getLoc().getX());
			writeD(item.getLoc().getY());
			writeD(item.getLoc().getZ());
			// itemAssemble
			writeItemInfo(item.getTradeItem(), true, 4);
			writeD(item.getTradeItem().getObjectId());
		}
	}
}