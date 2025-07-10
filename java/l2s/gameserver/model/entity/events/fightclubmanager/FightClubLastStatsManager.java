/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package l2s.gameserver.model.entity.events.fightclubmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import l2s.gameserver.model.Player;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class FightClubLastStatsManager
{
	private List<FightClubLastPlayerStats> _allStats;

	private static class SortRanking implements Comparator<FightClubLastPlayerStats>, Serializable
	{
		@Override
		public int compare(FightClubLastPlayerStats o1, FightClubLastPlayerStats o2)
		{
			return Integer.compare(o2.getScore(), o1.getScore());
		}
	}

	public static enum FightClubStatType
	{
		KILL_PLAYER("Kill Player");

		private final String _name;

		private FightClubStatType(String name)
		{
			_name = name;
		}

		public String getName()
		{
			return _name;
		}
	}

	public FightClubLastStatsManager()
	{
		_allStats = new CopyOnWriteArrayList<FightClubLastPlayerStats>();
	}

	public void updateStat(Player player, FightClubStatType type, int score)
	{
		FightClubLastPlayerStats myStat = getMyStat(player);

		if (myStat == null)
		{
			myStat = new FightClubLastPlayerStats(player, type.getName(), score);
			_allStats.add(myStat);
		}
		else
		{
			myStat.setScore(score);
		}
	}

	private FightClubLastPlayerStats getMyStat(Player player)
	{
		for (FightClubLastPlayerStats stat : _allStats)
		{
			if (stat.isMyStat(player))
			{
				return stat;
			}
		}
		return null;
	}

	public List<FightClubLastPlayerStats> getStats(boolean sortByScore)
	{
		List<FightClubLastPlayerStats> listToSort = new ArrayList<FightClubLastPlayerStats>();
		listToSort.addAll(_allStats);
		if (sortByScore)
		{
			Collections.sort(listToSort, new SortRanking());
		}
		return listToSort;
	}

	public void clearStats()
	{
		_allStats.clear();
	}

	private static FightClubLastStatsManager _instance;

	public static FightClubLastStatsManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new FightClubLastStatsManager();
		}
		return _instance;
	}
}