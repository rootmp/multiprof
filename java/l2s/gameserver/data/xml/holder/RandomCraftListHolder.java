package l2s.gameserver.data.xml.holder;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import l2s.commons.data.xml.AbstractHolder;
import l2s.commons.util.Rnd;
import l2s.gameserver.templates.randomCraft.RandomCraftRewardData;
import l2s.gameserver.templates.randomCraft.RandomCraftRewardItem;

public final class RandomCraftListHolder extends AbstractHolder
{
	private static final RandomCraftListHolder _instance = new RandomCraftListHolder();

	private static final Map<Integer, List<RandomCraftRewardData>> REWARD_DATA = new HashMap<>();
	private static final Set<Integer> REWARD_DATA_ANNOUNCE = new HashSet<>();

	public static RandomCraftListHolder getInstance()
	{
		return _instance;
	}

	public RandomCraftRewardItem getNewReward(int slot, List<RandomCraftRewardItem> rewardList)
	{
		List<RandomCraftRewardData> candidates = REWARD_DATA.get(slot);
		if(candidates == null || candidates.isEmpty())
			return null;

		List<RandomCraftRewardData> filtered = candidates.stream().filter(c -> rewardList.stream().noneMatch(r -> r.getItemId()
				== c.getItemId())).collect(Collectors.toList());

		if(filtered.isEmpty())
			return null;

		long totalChance = filtered.stream().mapToLong(RandomCraftRewardData::getChance).sum();
		if(totalChance <= 0)
			return null;

		Collections.shuffle(filtered);

		long roll = Rnd.get(totalChance);
		long cumulative = 0;

		for(RandomCraftRewardData data : filtered)
		{
			cumulative += data.getChance();
			if(roll < cumulative)
			{ return new RandomCraftRewardItem(data.getItemId(), data.getCount(), 0, false, 20); }
		}
		return null;
	}

	@Override
	public int size()
	{
		return REWARD_DATA.values().stream().mapToInt(List::size).sum();
	}

	@Override
	public void clear()
	{
		REWARD_DATA.clear();
	}

	public boolean isEmpty()
	{
		return REWARD_DATA.isEmpty();
	}

	public List<RandomCraftRewardData> getRewardData(int slot)
	{
		return REWARD_DATA.get(slot);
	}

	public void addRandomCraftInfo(int slot, List<RandomCraftRewardData> data, long prob)
	{
		REWARD_DATA.put(slot, data);
	}

	public boolean isAnnounce(int itemId)
	{
		return REWARD_DATA_ANNOUNCE.contains(itemId);
	}

	public void addAnnounce(int id)
	{
		REWARD_DATA_ANNOUNCE.add(id);
	}
}
