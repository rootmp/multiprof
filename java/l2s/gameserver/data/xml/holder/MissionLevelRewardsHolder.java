package l2s.gameserver.data.xml.holder;

import java.util.Map;
import java.util.TreeMap;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.dailymissions.MissionLevelRewardTemplate;

/**
 * @author nexvill
 */
public final class MissionLevelRewardsHolder extends AbstractHolder
{
	private static final MissionLevelRewardsHolder INSTANCE = new MissionLevelRewardsHolder();

	public static MissionLevelRewardsHolder getInstance()
	{
		return INSTANCE;
	}

	private final Map<Integer, MissionLevelRewardTemplate> _rewards = new TreeMap<>();

	public void addReward(MissionLevelRewardTemplate rewardTemplate)
	{
		_rewards.put(rewardTemplate.getMonth(), rewardTemplate);
	}

	public MissionLevelRewardTemplate getRewardsInfo(int month)
	{
		return _rewards.get(month);
	}

	@Override
	public int size()
	{
		return _rewards.size();
	}

	@Override
	public void clear()
	{
		_rewards.clear();
	}
}
