package l2s.gameserver.templates.item.upgrade.normal;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.templates.item.upgrade.UpgradeItemData;

public class NormalUpgradeData
{
	private final int _id;
	private final int _type;
	private final int _itemId;
	private final int _enchantLevel;
	private final long _price;
	private final int[] _locationId;

	private final List<UpgradeItemData> _requiredItems = new ArrayList<>();
	private final List<Integer> _unkData = new ArrayList<>();

	private NormalUpgradeResult _successResult = null;
	private NormalUpgradeResult _failResult = null;
	private NormalUpgradeResult _bonusResult = null;

	public NormalUpgradeData(int id, int type, int itemId, int enchantLevel, long price, int[] locationId)
	{
		_id = id;
		_type = type;
		_itemId = itemId;
		_enchantLevel = enchantLevel;
		_price = price;
		_locationId = locationId;
	}

	public int getId()
	{
		return _id;
	}

	public int getType()
	{
		return _type;
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

	public NormalUpgradeResult getSuccessResult()
	{
		return _successResult;
	}

	public void setSuccessResult(NormalUpgradeResult successResult)
	{
		if (_successResult != null)
		{
			return;
		}
		_successResult = successResult;
	}

	public NormalUpgradeResult getFailResult()
	{
		return _failResult;
	}

	public void setFailResult(NormalUpgradeResult failResult)
	{
		if (_failResult != null)
		{
			return;
		}
		_failResult = failResult;
	}

	public NormalUpgradeResult getBonusResult()
	{
		return _bonusResult;
	}

	public void setBonusResult(NormalUpgradeResult bonusResult)
	{
		if (_bonusResult != null)
		{
			return;
		}
		_bonusResult = bonusResult;
	}
}
