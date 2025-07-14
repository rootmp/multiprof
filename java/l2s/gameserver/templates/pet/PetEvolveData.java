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
package l2s.gameserver.templates.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.skills.SkillInfo;
import l2s.gameserver.stats.conditions.Condition;
import l2s.gameserver.stats.conditions.ConditionLogicAnd;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.skill.SkillData;

/**
 * @author Hl4p3x
 */
public class PetEvolveData
{
	private final int _step;
	private final ConditionLogicAnd _conditions = new ConditionLogicAnd();
	private final List<ItemData> _cost = new ArrayList<>();
	private final List<Pair<SkillData, Double>> _randomStats = new ArrayList<>();
	private final List<Pair<Integer, Double>> _randomNames = new ArrayList<>();

	public PetEvolveData(int step)
	{
		_step = step;
	}

	public int getStep()
	{
		return _step;
	}

	public void addCondition(Condition condition)
	{
		_conditions.add(condition);
	}

	public boolean checkConditions(PetInstance pet)
	{
		return _conditions.checkCondition(pet, pet, pet, null, 0., false, false, false);
	}

	public void addCost(ItemData itemData)
	{
		_cost.add(itemData);
	}

	public ItemData[] getCost()
	{
		return _cost.toArray(new ItemData[0]);
	}

	public void addRandomStat(SkillData skillData, double chance)
	{
		_randomStats.add(new Pair<SkillData, Double>(skillData, chance));
	}

	public SkillData getRandomStat()
	{
		if(_randomStats.isEmpty())
		{ return null; }
		EnumeratedDistribution<SkillData> distribution = new EnumeratedDistribution<SkillData>(_randomStats);
		return distribution.sample();
	}

	public boolean isRandomStat(SkillInfo skillInfo)
	{
		for(Pair<SkillData, Double> pair : _randomStats)
		{
			SkillData skillData = pair.getKey();
			if(skillData.getId() != skillInfo.getId())
			{
				continue;
			}
			if(skillData.getLevel() != skillInfo.getLevel())
			{
				continue;
			}
			return true;
		}
		return false;
	}

	public void addRandomName(int nameId, double chance)
	{
		_randomNames.add(new Pair<Integer, Double>(nameId, chance));
	}

	public int getRandomName()
	{
		if(_randomNames.isEmpty())
		{ return -1; }
		EnumeratedDistribution<Integer> distribution = new EnumeratedDistribution<Integer>(_randomNames);
		return distribution.sample().intValue();
	}
}
