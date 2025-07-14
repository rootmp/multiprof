package l2s.dataparser.data.holder.upgrade_system;

import l2s.dataparser.data.annotations.value.IntValue;

public class UpgradeItem
{
	@IntValue(withoutName = true)
	public int id;
	@IntValue(withoutName = true)
	public int enchant;
}