package l2s.gameserver.templates;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.templates.item.data.CollectionItemData;

/**
 * @author nexvill
 */
public class CollectionTemplate
{
	private final int _id;
	private final int _tabId;
	private final int _optionId;
	private List<CollectionItemData> _itemData = new ArrayList<>();

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

	public List<CollectionItemData> getItems()
	{
		return _itemData;
	}
}
