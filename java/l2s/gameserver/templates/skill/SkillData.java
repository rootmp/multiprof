/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2s.gameserver.templates.skill;

/**
 * @author Hl4p3x
 */
public class SkillData
{
	private final int _id;
	private final int _level;

	public SkillData(int id, int level)
	{
		_id = id;
		_level = level;
	}

	public int getId()
	{
		return _id;
	}

	public int getLevel()
	{
		return _level;
	}
}
