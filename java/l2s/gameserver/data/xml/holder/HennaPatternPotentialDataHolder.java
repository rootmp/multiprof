package l2s.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.model.Skill;
import l2s.gameserver.templates.item.henna.DyePotential;
import l2s.gameserver.templates.item.henna.DyePotentialFee;


public class HennaPatternPotentialDataHolder extends AbstractHolder
{
	public static HennaPatternPotentialDataHolder getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final HennaPatternPotentialDataHolder INSTANCE = new HennaPatternPotentialDataHolder();
	}

	
	private final Map<Integer, Integer> _potenExpTable = new HashMap<>();
	private final Map<Integer, DyePotentialFee> _potenFees = new HashMap<>();
	private final Map<Integer, DyePotential> _potentials = new HashMap<>();
	
	private int MAX_POTEN_LEVEL = 0;
	
	
	public DyePotentialFee getFee(int step)
	{
		return _potenFees.get(step);
	}
	
	public int getMaxPotenEnchantStep()
	{
		return _potenFees.size();
	}
	
	public int getExpForLevel(int level)
	{
		return _potenExpTable.get(level);
	}
	
	public int getMaxPotenLevel()
	{
		return MAX_POTEN_LEVEL;
	}
	
	public void setMaxPotenLevel(int i)
	{
		MAX_POTEN_LEVEL=i;
	}
	
	public DyePotential getPotential(int potenId)
	{
		return _potentials.get(potenId);
	}
	
	public Skill getPotentialSkill(int potenId, int slotId, int level)
	{
		final DyePotential potential = _potentials.get(potenId);
		if (potential == null)
		{
			return null;
		}
		if (potential.getSlotId() == slotId)
		{
			return potential.getSkill(level);
		}
		return null;
	}
	
	public Collection<Integer> getSkillIdsBySlotId(int slotId)
	{
		final List<Integer> skillIds = new ArrayList<>();
		for (DyePotential potential : _potentials.values())
		{
			if (potential.getSlotId() == slotId)
			{
				skillIds.add(potential.getSkillId());
			}
		}
		return skillIds;
	}
	
	@Override
	public int size()
	{
		return _potentials.size();
	}

	@Override
	public void clear()
	{
		_potenFees.clear();
		_potenExpTable.clear();
		_potentials.clear();
	}

	public void addPotenFees(int step, DyePotentialFee dyePotentialFee)
	{
		_potenFees.put(step, dyePotentialFee);
	}

	public void addPotenExpTable(int level, int exp)
	{
		_potenExpTable.put(level, exp);
	}

	public void addPotentials(int id, DyePotential dyePotential)
	{
		_potentials.put(id,dyePotential);
	}
}