package l2s.gameserver.templates.luckygame;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.commons.util.Rnd;

/**
 * @author Bonux
 **/
public class LuckyGameData
{
	public static final String LUCKY_GAMES_COUNT_VAR = "@lucky_games_count";
	public static final String PLAYED_LUCKY_GAMES_VAR = "@played_lucky_games";
	public static final String LAST_LUCKY_GAME_TIME_VAR = "@last_lucky_game_time";

	private final LuckyGameType _type;
	private final int _feeItemId;
	private final long _feeItemCount;
	private final int _gamesLimit;
	private final SchedulingPattern _reusePattern;
	private final List<LuckyGameItem> _commonRewards = new ArrayList<LuckyGameItem>();
	private final List<LuckyGameItem> _uniqueRewards = new ArrayList<LuckyGameItem>();
	private final List<LuckyGameItem> _additionalRewards = new ArrayList<LuckyGameItem>();

	public LuckyGameData(LuckyGameType type, int feeItemId, long feeItemCount, int gamesLimit, String reuse)
	{
		_type = type;
		_feeItemId = feeItemId;
		_feeItemCount = feeItemCount;
		_gamesLimit = gamesLimit;
		_reusePattern = reuse == null ? null : new SchedulingPattern(reuse);
	}

	public LuckyGameType getType()
	{
		return _type;
	}

	public int getFeeItemId()
	{
		return _feeItemId;
	}

	public long getFeeItemCount()
	{
		return _feeItemCount;
	}

	public int getGamesLimit()
	{
		return _gamesLimit;
	}

	public SchedulingPattern getReusePattern()
	{
		return _reusePattern;
	}

	public void addCommonRewards(List<LuckyGameItem> items)
	{
		_commonRewards.addAll(items);
	}

	public List<LuckyGameItem> getCommonRewards()
	{
		return _commonRewards;
	}

	public void addUniqueRewards(List<LuckyGameItem> items)
	{
		_uniqueRewards.addAll(items);
	}

	public List<LuckyGameItem> getUniqueRewards()
	{
		return _uniqueRewards;
	}

	public void addAdditionalRewards(List<LuckyGameItem> items)
	{
		_additionalRewards.addAll(items);
	}

	public List<LuckyGameItem> getAdditionalRewards()
	{
		return _additionalRewards;
	}

	public static LuckyGameItem rollItem(List<LuckyGameItem> items, boolean fantastic)
	{
		if(items.isEmpty())
			return null;

		double chancesAmount = 0;
		for(LuckyGameItem item : items)
			chancesAmount += item.getChance();

		double chanceMod = (100. - chancesAmount) / items.size();
		List<LuckyGameItem> successItems = new ArrayList<LuckyGameItem>();
		int tryCount = 0;
		while(successItems.isEmpty())
		{
			tryCount++;
			for(LuckyGameItem item : items)
			{
				if((tryCount % 10) == 0) // Немного теряем шанс, но зато зацикливания будут меньше.
					chanceMod += 1.;
				if(Rnd.chance(item.getChance() + chanceMod))
					successItems.add(item);
			}
		}

		LuckyGameItem item = Rnd.get(successItems);
		return new LuckyGameItem(item.getId(), Rnd.get(item.getMinCount(), item.getMaxCount()), fantastic);
	}
}