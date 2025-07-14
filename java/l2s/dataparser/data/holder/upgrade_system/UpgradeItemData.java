package l2s.dataparser.data.holder.upgrade_system;

import l2s.dataparser.data.annotations.class_annotations.ParseSuper;
import l2s.dataparser.data.annotations.value.IntValue;
import l2s.dataparser.data.annotations.value.LongValue;

@ParseSuper
public class UpgradeItemData
{
	@IntValue(withoutName = true)
	private int id;
	@LongValue(withoutName = true)
	private long count;
	@IntValue(withoutName = true)
	private int enchant;

	public long getCount()
	{
		return count;
	}

	public int getId()
	{
		return id;
	}

	public int getEnchantLevel()
	{
		return enchant;
	}
}
