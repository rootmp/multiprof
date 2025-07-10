package l2s.gameserver.templates.item.upgrade;

import l2s.gameserver.templates.item.data.ItemData;

public class UpgradeItemData extends ItemData
{
	private final int _enchantLevel;

	public UpgradeItemData(int id, long count, int enchantLevel)
	{
		super(id, count);
		_enchantLevel = enchantLevel;
	}

	public int getEnchantLevel()
	{
		return _enchantLevel;
	}
}
