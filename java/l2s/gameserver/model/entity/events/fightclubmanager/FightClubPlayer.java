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
import java.util.Map;

import javolution.util.FastMap;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class FightClubPlayer implements Serializable
{
	private Player _player;
	private FightClubTeam _team;
	private Party _myParty = null;
	private int _score;
	private int _playerKills;
	private int _petKills;
	private Map<String, Integer> _otherCreaturesScores = new FastMap<String, Integer>();
	private int _deaths;
	private boolean _invisible = false;
	private boolean _isShowRank = false;
	private boolean _isShowTutorial = false;
	private int _secondsSpentOnEvent = 0;
	private int _secondsOutsideZone = 0;

	private boolean _afk = false;
	private long _afkStartTime = 0L;
	private int _totalAfkSeconds = 0;

	public FightClubPlayer(Player player)
	{
		_player = player;
	}

	public void setPlayer(Player player)
	{
		_player = player;
	}

	public Player getPlayer()
	{
		return _player;
	}

	public void setTeam(FightClubTeam team)
	{
		_team = team;
	}

	public FightClubTeam getTeam()
	{
		return _team;
	}

	public Party getParty()
	{
		return _myParty;
	}

	public void setParty(Party party)
	{
		_myParty = party;
	}

	public void increaseScore(int byHowMany)
	{
		_score += byHowMany;
	}

	public void decreaseScore(int byHowMany)
	{
		_score -= byHowMany;
	}

	public void setScore(int value)
	{
		_score = value;
	}

	public int getScore()
	{
		return _score;
	}

	public void increaseKills(boolean player1Pet0)
	{
		if(player1Pet0)
		{
			_playerKills++;
		}
		else
		{
			_petKills++;
		}
	}

	public void setKills(int value, boolean player1Pet0)
	{
		if(player1Pet0)
		{
			_playerKills = value;
		}
		else
		{
			_petKills = value;
		}
	}

	public int getKills(boolean player1Pet0)
	{
		if(player1Pet0)
		{ return _playerKills; }
		return _petKills;
	}

	public void increaseEventSpecificScore(String scoreKey)
	{
		int value = _otherCreaturesScores.get(scoreKey);
		if(!_otherCreaturesScores.containsKey(scoreKey))
		{
			_otherCreaturesScores.put(scoreKey, 0);
		}
		_otherCreaturesScores.put(scoreKey, value + 1);
	}

	public void setEventSpecificScore(String scoreKey, int value)
	{
		_otherCreaturesScores.put(scoreKey, Integer.valueOf(value));
	}

	public int getEventSpecificScore(String scoreKey)
	{
		if(!_otherCreaturesScores.containsKey(scoreKey))
		{ return 0; }
		return _otherCreaturesScores.get(scoreKey);
	}

	public void increaseDeaths()
	{
		_deaths++;
	}

	public void setDeaths(int value)
	{
		_deaths = value;
	}

	public int getDeaths()
	{
		return _deaths;
	}

	public void setInvisible(boolean val)
	{
		_invisible = val;
	}

	public boolean isInvisible()
	{
		return _invisible;
	}

	public void setAfk(boolean val)
	{
		_afk = val;
	}

	public boolean isAfk()
	{
		return _afk;
	}

	public void setAfkStartTime(long startTime)
	{
		_afkStartTime = startTime;
	}

	public long getAfkStartTime()
	{
		return _afkStartTime;
	}

	public void addTotalAfkSeconds(int secsAfk)
	{
		_totalAfkSeconds += secsAfk;
	}

	public int getTotalAfkSeconds()
	{
		return _totalAfkSeconds;
	}

	public void setShowRank(boolean b)
	{
		_isShowRank = b;
	}

	public boolean isShowRank()
	{
		return _isShowRank;
	}

	public void setShowTutorial(boolean b)
	{
		_isShowTutorial = b;
	}

	public boolean isShowTutorial()
	{
		return _isShowTutorial;
	}

	public void incSecondsSpentOnEvent(int by)
	{
		_secondsSpentOnEvent += by;
	}

	public int getSecondsSpentOnEvent()
	{
		return _secondsSpentOnEvent;
	}

	public void increaseSecondsOutsideZone()
	{
		_secondsOutsideZone++;
	}

	public int getSecondsOutsideZone()
	{
		return _secondsOutsideZone;
	}

	public void clearSecondsOutsideZone()
	{
		_secondsOutsideZone = 0;
	}
}