package l2s.gameserver.templates.randomCraft;

public class RandomCraftRewardData
{
	private final int _itemId;
	private final long _count;
	private final long _chance;
	private final int _stage;

	public RandomCraftRewardData(int itemId, long count, long chance, int stage)
	{
		_itemId = itemId;
		_count = count;
		_chance = chance;
		_stage = stage;
	}

	public int getItemId()
	{
		return _itemId;
	}

	public long getCount()
	{
		return _count;
	}

	public long getChance()
	{
		return _chance;
	}

	public int getStage()
	{
		return _stage;
	}

}
