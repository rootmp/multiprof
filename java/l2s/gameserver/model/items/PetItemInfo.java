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
package l2s.gameserver.model.items;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class PetItemInfo
{
	private int _evolveStep;
	private int _nameId;
	private int _statSkillId;
	private int _statSkillLevel;
	private int _petIndex;
	private long _exp;

	public PetItemInfo(int evolveStep, int nameId, int statSkillId, int statSkillLevel, int petIndex, long exp)
	{
		_evolveStep = evolveStep;
		_nameId = nameId;
		_statSkillId = statSkillId;
		_statSkillLevel = statSkillLevel;
		_petIndex = petIndex;
		_exp = exp;
	}

	public int getEvolveStep()
	{
		return _evolveStep;
	}

	public void setEvolveStep(int evolveStep)
	{
		_evolveStep = evolveStep;
	}

	public int getNameId()
	{
		return _nameId;
	}

	public void setNameId(int nameId)
	{
		_nameId = nameId;
	}

	public int getStatSkillId()
	{
		return _statSkillId;
	}

	public void setStatSkillId(int statSkillId)
	{
		_statSkillId = statSkillId;
	}

	public int getStatSkillLevel()
	{
		return _statSkillLevel;
	}

	public void setStatSkillLevel(int statSkillLevel)
	{
		_statSkillLevel = statSkillLevel;
	}

	public int getPetIndex()
	{
		return _petIndex;
	}

	public void setPetIndex(int petIndex)
	{
		_petIndex = petIndex;
	}

	public long getExp()
	{
		return _exp;
	}

	public void setExp(long exp)
	{
		_exp = exp;
	}
}
