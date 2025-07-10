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
import java.util.List;

import l2s.gameserver.geometry.Location;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class FightClubTeam implements Serializable
{
	public static enum TEAM_NAMES
	{
		Red(1453793),
		Blue(11877953),
		Green(4109633),
		Yellow(3079679),
		Gray(8421504),
		Orange(34809),
		Black(1447446),
		White(16777215),
		Violet(12199813),
		Cyan(14934326),
		Pink(14577135);

		public int _nameColor;

		private TEAM_NAMES(int nameColor)
		{
			_nameColor = nameColor;
		}
	}

	private int _index;
	private String _name;
	private List<FightClubPlayer> _players = new ArrayList<FightClubPlayer>();
	private Location _spawnLoc;
	private int _score;

	public FightClubTeam(int index)
	{
		_index = index;
		chooseName();
	}

	public int getIndex()
	{
		return _index;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public String chooseName()
	{
		_name = TEAM_NAMES.values()[_index - 1].toString();
		return _name;
	}

	public int getNickColor()
	{
		return TEAM_NAMES.values()[_index - 1]._nameColor;
	}

	public List<FightClubPlayer> getPlayers()
	{
		return _players;
	}

	public void addPlayer(FightClubPlayer player)
	{
		_players.add(player);
	}

	public void removePlayer(FightClubPlayer player)
	{
		_players.remove(player);
	}

	public void setSpawnLoc(Location loc)
	{
		_spawnLoc = loc;
	}

	public Location getSpawnLoc()
	{
		return _spawnLoc;
	}

	public void setScore(int newScore)
	{
		_score = newScore;
	}

	public void incScore(int by)
	{
		_score += by;
	}

	public int getScore()
	{
		return _score;
	}
}