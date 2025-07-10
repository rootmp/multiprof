package l2s.gameserver.model.items;

import l2s.gameserver.templates.item.data.ItemData;

public class EnchantBrokenItem extends ItemData
{
	private final int _enchant;
	private final int _time;

	public EnchantBrokenItem(int id, int enchant, int time)
	{
		super(id, 1);
		_enchant = enchant;
		_time = time;
	}

	@Override
	public int getEnchant()
	{
		return _enchant;
	}

	public int getTime()
	{
		return _time;
	}
}
