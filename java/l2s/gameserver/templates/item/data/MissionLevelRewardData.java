package l2s.gameserver.templates.item.data;

/**
 * @author nexvill
 */
public class MissionLevelRewardData extends ItemData
{
	private final int _level;
	private final int _value;
	private ItemData _additionalReward;

	public MissionLevelRewardData(int level, int value, int baseRewardId, long baseRewardCount, int additionalRewardId, long additionalRewardCount)
	{
		super(baseRewardId, baseRewardCount);
		_level = level;
		_value = value;
		_additionalReward = new ItemData(additionalRewardId, additionalRewardCount);
	}

	public int getLevel()
	{
		return _level;
	}

	public int getValue()
	{
		return _value;
	}

	public ItemData getAdditionalReward()
	{
		return _additionalReward;
	}
}
