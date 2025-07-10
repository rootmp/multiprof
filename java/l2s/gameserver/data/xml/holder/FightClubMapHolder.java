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
package l2s.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubMap;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public final class FightClubMapHolder extends AbstractHolder
{
	private final List<FightClubMap> _maps = new ArrayList<FightClubMap>();

	public void addMap(FightClubMap map)
	{
		_maps.add(map);
	}

	public List<FightClubMap> getMapsForEvent(String eventName)
	{
		List<FightClubMap> maps = new ArrayList<FightClubMap>();
		for (FightClubMap map : _maps)
		{
			for (String possibleName : map.getEvents())
			{
				if (!possibleName.equalsIgnoreCase(eventName))
				{
					continue;
				}
				maps.add(map);
			}
		}

		return maps;
	}

	public List<Integer> getTeamPossibilitiesForEvent(String eventName)
	{
		List<FightClubMap> allMaps = getMapsForEvent(eventName);
		List<Integer> teams = new ArrayList<Integer>();
		for (FightClubMap map : allMaps)
		{
			for (int possibility : map.getTeamCount())
			{
				if (!teams.contains(possibility))
				{
					teams.add(possibility);
				}
			}
		}

		Collections.sort(teams);
		return teams;
	}

	public int getMinPlayersForEvent(String eventName)
	{
		List<FightClubMap> allMaps = getMapsForEvent(eventName);
		int minPlayers = Integer.MAX_VALUE;
		for (FightClubMap map : allMaps)
		{
			int newMin = map.getMinAllPlayers();
			if (newMin < minPlayers)
			{
				minPlayers = newMin;
			}
		}
		return minPlayers;
	}

	public int getMaxPlayersForEvent(String eventName)
	{
		List<FightClubMap> allMaps = getMapsForEvent(eventName);
		int maxPlayers = 0;
		for (FightClubMap map : allMaps)
		{
			int newMax = map.getMaxAllPlayers();
			if (newMax > maxPlayers)
			{
				maxPlayers = newMax;
			}
		}
		return maxPlayers;
	}

	@Override
	public int size()
	{
		return _maps.size();
	}

	@Override
	public void clear()
	{
		_maps.clear();
	}

	private static final FightClubMapHolder _instance = new FightClubMapHolder();

	public static FightClubMapHolder getInstance()
	{
		return _instance;
	}
}