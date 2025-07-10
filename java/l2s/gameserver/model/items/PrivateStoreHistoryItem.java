package l2s.gameserver.model.items;

/**
 * @author Bonux (bonuxq@gmail.com)
 * @date 27.12.2021
 **/
public class PrivateStoreHistoryItem extends ItemInfo
{
	private final int _storeType;
	private final int _time;

	private long _price = 0L;
	private int _tradesCount = 0;

	public PrivateStoreHistoryItem(int itemId, int storeType, int time)
	{
		super();
		setItemId(itemId);
		_storeType = storeType;
		_time = time;
	}

	public PrivateStoreHistoryItem(PrivateStoreHistoryItem item)
	{
		super();
		setItemId(item.getItemId());
		setCount(item.getCount());
		setEnchantLevel(item.getEnchantLevel());
		_storeType = item.getStoreType();
		_time = item.getTime();
		setPrice(item.getPrice());
		setTradesCount(item.getTradesCount());
	}

	public PrivateStoreHistoryItem(TradeItem item, int storeType, int time)
	{
		super();
		setItemId(item.getItemId());
		setCount(item.getCount());
		setEnchantLevel(item.getEnchantLevel());
		_storeType = storeType;
		_time = time;
		setPrice(item.getOwnersPrice());
	}

	public int getStoreType()
	{
		return _storeType;
	}

	public int getTime()
	{
		return _time;
	}

	public long getPrice()
	{
		return _price;
	}

	public void setPrice(long price)
	{
		_price = price;
	}

	public int getTradesCount()
	{
		return _tradesCount;
	}

	public void setTradesCount(int tradesCount)
	{
		_tradesCount = tradesCount;
	}
}
