package l2s.gameserver.templates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.data.CollectionItemData;
import l2s.gameserver.templates.item.data.ItemData;

public class CollectionTemplate
{
	private final int _id;
	private final int _tabId;
	private final int _optionId;
	private int _maxSlot = 0;

	private List<CollectionItemData> _itemData = new ArrayList<>();
	private List<ItemData> _rewardItems = new ArrayList<>();

	public CollectionTemplate(int id, int tabId, int optionId)
	{
		_id = id;
		_tabId = tabId;
		_optionId = optionId;
	}

	public int getId()
	{
		return _id;
	}

	public int getTabId()
	{
		return _tabId;
	}

	public int getOptionId()
	{
		return _optionId;
	}

	public void addItem(CollectionItemData itemData)
	{
		_itemData.add(itemData);
	}

	public void setItem(List<CollectionItemData> itemData)
	{
		_itemData = itemData;
	}

	public List<CollectionItemData> getItems()
	{
		return _itemData;
	}

	public void addReward(ItemData itemData)
	{
		_rewardItems.add(itemData);
	}

	public void setReward(List<ItemData> rewardItems)
	{
		_rewardItems = rewardItems;
	}

	public List<ItemData> getRewardItems()
	{
		return _rewardItems;
	}

	public int getMaxSlot()
	{
		return _maxSlot;
	}

	public void setMaxSlot(int slot)
	{
		_maxSlot = slot;
	}

	@Override
	public CollectionTemplate clone()
	{
		CollectionTemplate _new = new CollectionTemplate(_id, _tabId, _optionId);
		_new.setItem(_itemData);
		_new.setReward(_rewardItems);
		return _new;
	}

	public List<CollectionItemData> filterByUniqueSlotId(Player player)
	{
		List<CollectionItemData> uniqueItems = new ArrayList<>();
		Set<Integer> uniqueSlotIds = new HashSet<>();
		for(CollectionItemData itemData : _itemData)
		{
			int slotId = itemData.getSlotId();

			if(!uniqueSlotIds.contains(slotId) && player.getCollectionList().checkSlot(_id, slotId))
			{
				uniqueSlotIds.add(slotId);
				uniqueItems.add(itemData);
			}
		}
		return uniqueItems;
	}
}
