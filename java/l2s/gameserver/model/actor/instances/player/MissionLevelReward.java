package l2s.gameserver.model.actor.instances.player;

import l2s.gameserver.dao.CharacterMissionLevelRewardDAO;
import l2s.gameserver.model.Player;

/**
 * @author nexvill
 */
public class MissionLevelReward
{
	private final Player _owner;
	private int _level;
	private int _points;
	private int _lastTakenBasic;
	private int _lastTakenAdditional;
	private int _lastTakenBonus;
	private boolean _takenFinal;

	public MissionLevelReward(Player owner)
	{
		_owner = owner;
	}

	public void restore()
	{
		CharacterMissionLevelRewardDAO.getInstance().restore(_owner);
	}

	public void store()
	{
		CharacterMissionLevelRewardDAO.getInstance().store(_owner, _level, _points, _lastTakenBasic, _lastTakenAdditional, _lastTakenBonus, _takenFinal);
	}

	public void delete()
	{
		CharacterMissionLevelRewardDAO.getInstance().delete(_owner);
	}

	public int getLevel()
	{
		return _level;
	}

	public void setLevel(int level)
	{
		_level = level;
	}

	public int getPoints()
	{
		return _points;
	}

	public void setPoints(int points)
	{
		_points = points;
	}

	public int lastTakenBasic()
	{
		return _lastTakenBasic;
	}

	public void setLastTakenBasic(int lastTakenBasic)
	{
		_lastTakenBasic = lastTakenBasic;
	}

	public int lastTakenAdditional()
	{
		return _lastTakenAdditional;
	}

	public void setLastTakenAdditional(int lastTakenAdditional)
	{
		_lastTakenAdditional = lastTakenAdditional;
	}

	public int lastTakenBonus()
	{
		return _lastTakenBonus;
	}

	public void setLastTakenBonus(int lastTakenBonus)
	{
		_lastTakenBonus = lastTakenBonus;
	}

	public boolean takenFinal()
	{
		return _takenFinal;
	}

	public void setTakenFinal(boolean takenFinal)
	{
		_takenFinal = takenFinal;
	}
}