package l2s.gameserver.network.l2.s2c.privatestoresearch;

import java.util.List;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.items.TradeItem;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

public class ExPrivateStoreSearchItem implements IClientOutgoingPacket
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
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cCurrentPage);
		packetWriter.writeC(cMaxPage);
		packetWriter.writeD(items.size());
		for (Item item : items)
		{
			packetWriter.writeString(item.getOwnerName());
			packetWriter.writeC(item.getStoreType());
			packetWriter.writeQ(item.getTradeItem().getOwnersPrice());
			packetWriter.writeD(item.getLoc().getX());
			packetWriter.writeD(item.getLoc().getY());
			packetWriter.writeD(item.getLoc().getZ());
			// itemAssemble
			writeItemInfo(item.getTradeItem(), true, 4);
			packetWriter.writeD(item.getTradeItem().getObjectId());
		}
	}
}