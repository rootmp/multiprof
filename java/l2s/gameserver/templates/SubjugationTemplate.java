package l2s.gameserver.templates;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.templates.item.data.ItemData;

/**
 * @author nexvill
 **/
public class SubjugationTemplate
{
	private final int _zoneId, _pointsToKey, _maximumKeys, _minLevel, _maxLevel;
	private final List<Integer> _mobIds = new ArrayList<Integer>();
	private final List<ItemData> _rewardItems = new ArrayList<ItemData>();

	public SubjugationTemplate(int zoneId, int pointsToKey, int maximumKeys, int minLevel, int maxLevel)
	{
		_zoneId = zoneId;
		_pointsToKey = pointsToKey;
		_maximumKeys = maximumKeys;
		_minLevel = minLevel;
		_maxLevel = maxLevel;
	}

	public int getZoneId()
	{
		return _zoneId;
	}

	public int getPointsToKey()
	{
		return _pointsToKey;
	}

	public int getMaximumKeys()
	{
		return _maximumKeys;
	}

	public int getMinLevel()
	{
		return _minLevel;
	}

	public int getMaxLevel()
	{
		return _maxLevel;
	}

	public void addMobId(int mobId)
	{
		_mobIds.add(mobId);
	}

	public Integer[] getMobs()
	{
		return _mobIds.toArray(new Integer[_mobIds.size()]);
	}

	public void addRewardItem(ItemData item)
	{
		_rewardItems.add(item);
	}

	public ItemData[] getRewardItems()
	{
		return _rewardItems.toArray(new ItemData[_rewardItems.size()]);
	}
}