package l2s.gameserver.templates;

import l2s.gameserver.enums.WorldExchangeItemStatusType;
import l2s.gameserver.enums.WorldExchangeItemSubType;
import l2s.gameserver.model.items.ItemInfo;
import l2s.gameserver.model.items.ItemInstance;


public class WorldExchangeHolder
{
	private final long _worldExchangeId;
	private final ItemInstance _itemInstance;
	private final ItemInfo _itemInfo;
	private final long _price;
	private final int _oldOwnerId;
	private WorldExchangeItemStatusType _storeType;
	private final WorldExchangeItemSubType _category;
	private final long _startTime;
	private long _endTime;
	private boolean _hasChanges;
	private int _currencyType;
	private int _listingType;
	
	public WorldExchangeHolder(long worldExchangeId, ItemInstance itemInstance, ItemInfo itemInfo, long price, int oldOwnerId, WorldExchangeItemStatusType storeType, WorldExchangeItemSubType category, long startTime, long endTime, boolean hasChanges, int listingType, int currencyType)
	{
		_worldExchangeId = worldExchangeId;
		_itemInstance = itemInstance;
		_itemInfo = itemInfo;
		_price = price;
		_oldOwnerId = oldOwnerId;
		_storeType = storeType;
		_category = category;
		_startTime = startTime;
		_endTime = endTime;
		_hasChanges = hasChanges;
		_listingType=listingType;
		_currencyType = currencyType;
	}
	
	public long getWorldExchangeId()
	{
		return _worldExchangeId;
	}
	
	public ItemInstance getItemInstance()
	{
		return _itemInstance;
	}
	
	public ItemInfo getItemInfo()
	{
		return _itemInfo;
	}
	
	public long getPrice()
	{
		return _price;
	}
	
	public int getOldOwnerId()
	{
		return _oldOwnerId;
	}
	
	public WorldExchangeItemStatusType getStoreType()
	{
		return _storeType;
	}
	
	public void setStoreType(WorldExchangeItemStatusType storeType)
	{
		_storeType = storeType;
	}
	
	public WorldExchangeItemSubType getCategory()
	{
		return _category;
	}
	
	public long getStartTime()
	{
		return _startTime;
	}
	
	public long getEndTime()
	{
		return _endTime;
	}
	
	public void setEndTime(long endTime)
	{
		_endTime = endTime;
	}
	
	public boolean hasChanges()
	{
		if (_hasChanges) // TODO: Fix logic.
		{
			_hasChanges = false;
			return true;
		}
		return false;
	}
	
	public void setHasChanges(boolean hasChanges)
	{
		_hasChanges = hasChanges;
	}

	public int getCurrencyType()
	{
		return _currencyType;
	}

	public int getListingType()
	{
		return _listingType;
	}
}
