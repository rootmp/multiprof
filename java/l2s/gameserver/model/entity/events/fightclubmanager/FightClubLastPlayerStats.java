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

import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class FightClubLastPlayerStats
{
	private String _playerNickName;
	private ClassId _classId;
	private String _clanName;
	private String _allyName;
	private String _typeName;
	private int _score;

	public FightClubLastPlayerStats(Player player, String typeName, int score)
	{
		_playerNickName = player.getName();
		_clanName = player.getClan() != null ? player.getClan().getName() : "<br>";
		_allyName = player.getAlliance() != null ? player.getAlliance().getAllyName() : "<br>";
		_classId = player.getClassId();
		_typeName = typeName;
		_score = score;
	}

	public boolean isMyStat(Player player)
	{
		return _playerNickName.equals(player.getName());
	}

	public String getPlayerName()
	{
		return _playerNickName;
	}

	public String getClanName()
	{
		return _clanName;
	}

	public String getAllyName()
	{
		return _allyName;
	}

	public ClassId getClassId()
	{
		return _classId;
	}

	public String getTypeName()
	{
		return _typeName;
	}

	public int getScore()
	{
		return _score;
	}

	public void setScore(int i)
	{
		_score = i;
	}
}