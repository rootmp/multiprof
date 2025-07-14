package l2s.dataparser.data.holder.synthesis;

import l2s.dataparser.data.annotations.array.IntArray;
import l2s.dataparser.data.annotations.value.DoubleValue;
import l2s.dataparser.data.annotations.value.IntValue;
import l2s.dataparser.data.annotations.value.ObjectValue;

public class SynthesisData
{
	@IntArray
	public int[] main_slot;
	@IntArray
	public int[] sub_slot;

	@ObjectValue
	public ItemResult successItem;
	@ObjectValue
	public ItemResult failItem;
	@IntValue
	public int commission;

	public static class ItemResult
	{
		@IntValue(withoutName = true)
		public int id;
		@IntValue(withoutName = true)
		public int enchant;
		@IntValue(withoutName = true)
		public int count;
		@IntValue(withoutName = true)
		public int bless;
		@DoubleValue(withoutName = true)
		public double chance;

		public int getId()
		{
			return id;
		}

		public long getCount()
		{
			return count;
		}

		public int getEnchant()
		{
			return enchant;
		}

		public double getChance()
		{
			return chance;
		}

		public void setChance(double chance)
		{
			this.chance = chance;
		}
	}

	public int getItem1Id()
	{
		return main_slot[0];
	}

	public int getItem2Id()
	{
		return sub_slot[0];
	}

	public ItemResult getSuccessItemData()
	{
		return successItem;
	}

	public ItemResult getFailItemData()
	{
		return failItem;
	}

	public int getPrice()
	{
		return commission;
	}

	public int getItem1IdEnchant()
	{
		return main_slot[1];
	}

	public int getItem2IdEnchant()
	{
		return sub_slot[1];
	}
}
