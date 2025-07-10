package l2s.gameserver.templates.item.upgrade.normal;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.templates.item.upgrade.UpgradeItemData;

public class NormalUpgradeResult
{
	private final NormalUpgradeResultType _type;
	private final double _chance;
	private final List<UpgradeItemData> _items = new ArrayList<>();

	public NormalUpgradeResult(NormalUpgradeResultType type, double chance)
	{
		_type = type;
		_chance = chance;
	}

	public NormalUpgradeResultType getType()
	{
		return _type;
	}

	public double getChance()
	{
		return _chance;
	}

	public void addItem(UpgradeItemData item)
	{
		_items.add(item);
	}

	public List<UpgradeItemData> getItems()
	{
		return _items;
	}
}
