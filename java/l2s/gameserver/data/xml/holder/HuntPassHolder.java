package l2s.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.item.data.ItemData;

public class HuntPassHolder extends AbstractHolder
{
	private final List<ItemData> _rewards = new ArrayList<>();
	private final List<ItemData> _premiumRewards = new ArrayList<>();

	
	public List<ItemData> getRewards()
	{
		return _rewards;
	}
	
	public List<ItemData> getPremiumRewards()
	{
		return _premiumRewards;
	}
	
	@Override
	public int size()
	{
		return _rewards.size() + _premiumRewards.size();
	}

	@Override
	public void clear()
	{
		_rewards.clear();
		_premiumRewards.clear();
	}
	
	private static final HuntPassHolder INSTANCE = new HuntPassHolder();

	public static HuntPassHolder getInstance()
	{
		return INSTANCE;
	}

	public void addRewards(ItemData itemData)
	{
		_rewards.add(itemData);
	}

	public void addPremiumRewards(ItemData itemData)
	{
		_premiumRewards.add(itemData);
	}
}
