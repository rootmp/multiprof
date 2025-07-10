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

import java.util.Map;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.templates.ZoneTemplate;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class FightClubMap
{
	private final String _name;
	private final String[] _events;
	private final int[] _teamsCount;
	private final int _minAllPlayers;
	private final int _maxAllPlayers;
	private final Map<Integer, Location[]> _teamSpawns;
	private final Map<Integer, Map<String, ZoneTemplate>> _territories;
	private final Map<Integer, Map<Integer, Location[]>> _npcWaypath;
	private final Location[] _keyLocations;

	public FightClubMap(MultiValueSet<String> params, Map<Integer, Location[]> teamSpawns, Map<Integer, Map<String, ZoneTemplate>> territories, Map<Integer, Map<Integer, Location[]>> npcWaypath, Location[] keyLocations)
	{
		_name = params.getString("name");
		_events = params.getString("events").split(";");
		_minAllPlayers = Integer.parseInt(params.getString("minAllPlayers", "-1"));
		_maxAllPlayers = Integer.parseInt(params.getString("maxAllPlayers", "-1"));

		String[] teamCounts = params.getString("teamsCount", "-1").split(";");
		_teamsCount = new int[teamCounts.length];

		for (int i = 0; i < teamCounts.length; i++)
		{
			_teamsCount[i] = Integer.parseInt(teamCounts[i]);
		}

		_teamSpawns = teamSpawns;
		_territories = territories;
		_npcWaypath = npcWaypath;
		_keyLocations = keyLocations;
	}

	private String getName()
	{
		return _name;
	}

	public String[] getEvents()
	{
		return _events;
	}

	public int[] getTeamCount()
	{
		return _teamsCount;
	}

	public int getMinAllPlayers()
	{
		return _minAllPlayers;
	}

	public int getMaxAllPlayers()
	{
		return _maxAllPlayers;
	}

	public Map<Integer, Location[]> getTeamSpawns()
	{
		return _teamSpawns;
	}

	public Location[] getPlayerSpawns()
	{
		return _teamSpawns.get(-1);
	}

	public Map<Integer, Map<String, ZoneTemplate>> getTerritories()
	{
		return _territories;
	}

	public Map<Integer, Map<Integer, Location[]>> getNpcWaypath()
	{
		return _npcWaypath;
	}

	public Location[] getKeyLocations()
	{
		return _keyLocations;
	}
}