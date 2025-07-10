package l2s.gameserver.templates.item.upgrade.rare;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.templates.item.upgrade.UpgradeItemData;

public class RareUpgradeData
{
	private final int _id;
	private final int _itemId;
	private final int _enchantLevel;
	private final long _price;
	private final int[] _locationId;
	private final int _resultItemId;
	private final long _resultItemCount;
	private final int _resultItemEnchant;

	private final List<UpgradeItemData> _requiredItems = new ArrayList<>();
	private final List<Integer> _unkData = new ArrayList<>();

	public RareUpgradeData(int id, int itemId, int enchantLevel, long price, int[] locationId, int resultItemId, long resultItemCount, int resultItemEnchant)
	{
		_id = id;
		_itemId = itemId;
		_enchantLevel = enchantLevel;
		_price = price;
		_locationId = locationId;
		_resultItemId = resultItemId;
		_resultItemCount = resultItemCount;
		_resultItemEnchant = resultItemEnchant;
	}

	public int getId()
	{
		return _id;
	}

	public int getItemId()
	{
		return _itemId;
	}

	public int getEnchantLevel()
	{
		return _enchantLevel;
	}

	public long getPrice()
	{
		return _price;
	}

	public int[] getLocationId()
	{
		return _locationId;
	}

	public int getResultItemId()
	{
		return _resultItemId;
	}

	public long getResultItemCount()
	{
		return _resultItemCount;
	}

	public int getResultItemEnchant()
	{
		return _resultItemEnchant;
	}

	public void addRequiredItem(UpgradeItemData item)
	{
		_requiredItems.add(item);
	}

	public List<UpgradeItemData> getRequiredItems()
	{
		return _requiredItems;
	}

	public void addUnkData(int data)
	{
		_unkData.add(data);
	}

	public List<Integer> getUnkData()
	{
		return _unkData;
	}
}
