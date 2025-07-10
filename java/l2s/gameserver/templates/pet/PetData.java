package l2s.gameserver.templates.pet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import l2s.gameserver.model.base.MountType;
import l2s.gameserver.model.base.PetType;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.item.data.RewardItemData;
import l2s.gameserver.templates.skill.SkillData;

/**
 * @author Bonux
 */
public class PetData
{
	private final int _index;
	private final int _npcId;
	private final int _controlItemId;
	private final int[] _syncLevels;
	private final List<PetSkillData> _skills;

	private final List<SkillData> _canShowPassiveSkills = new ArrayList<>();
	private final Map<Integer, PetEvolveData> _evolveSteps = new HashMap<>();
	private final Map<Integer, List<Pair<Integer, Double>>> _randomShapes = new HashMap<>();
	private final List<ItemData> _evolveCost = new ArrayList<>();

	private final TIntObjectMap<PetLevelData> _lvlData;
	private final PetType _type;
	private final MountType _mountType;

	private int _minLvl = Integer.MAX_VALUE;
	private int _maxLvl = 0;

	private final int _petType;
	private final List<RewardItemData> _expirationRewardItems = new ArrayList<RewardItemData>();

	public PetData(int index, int npcId, int controlItemId, int[] syncLevels, PetType type, MountType mountType, int petType)
	{
		_index = index;
		_npcId = npcId;
		_controlItemId = controlItemId;
		_syncLevels = syncLevels;
		_skills = new ArrayList<PetSkillData>();
		_lvlData = new TIntObjectHashMap<PetLevelData>();
		_type = type;
		_petType = petType;
		_mountType = mountType;
	}

	public int getIndex()
	{
		return _index;
	}

	public int getNpcId()
	{
		return _npcId;
	}

	public int getControlItemId()
	{
		return _controlItemId;
	}

	public void addSkill(PetSkillData skill)
	{
		_skills.add(skill);
	}

	public PetSkillData[] getSkills()
	{
		return _skills.toArray(new PetSkillData[_skills.size()]);
	}

	public void addLvlData(int lvl, PetLevelData lvlData)
	{
		if (_minLvl > lvl)
		{
			_minLvl = lvl;
		}
		if (_maxLvl < (lvl - 1))
		{
			_maxLvl = lvl - 1;
		}
		_lvlData.put(lvl, lvlData);
	}

	public PetLevelData getLvlData(int level)
	{
		return _lvlData.get(Math.max(_minLvl, Math.min(_maxLvl, level)));
	}

	public int getMaxMeal(int level)
	{
		return getLvlData(level).getMaxMeal();
	}

	public long getExp(int level)
	{
		return getLvlData(level).getExp();
	}

	public int getExpType(int level)
	{
		return getLvlData(level).getExpType();
	}

	public int getBattleMealConsume(int level)
	{
		return getLvlData(level).getBattleMealConsume();
	}

	public int getNormalMealConsume(int level)
	{
		return getLvlData(level).getNormalMealConsume();
	}

	public double getPAtk(int level)
	{
		return getLvlData(level).getPAtk();
	}

	public double getPDef(int level)
	{
		return getLvlData(level).getPDef();
	}

	public double getMAtk(int level)
	{
		return getLvlData(level).getMAtk();
	}

	public double getMDef(int level)
	{
		return getLvlData(level).getMDef();
	}

	public double getHP(int level)
	{
		return getLvlData(level).getHP();
	}

	public double getMP(int level)
	{
		return getLvlData(level).getMP();
	}

	public double getHPRegen(int level)
	{
		return getLvlData(level).getHPRegen();
	}

	public double getMPRegen(int level)
	{
		return getLvlData(level).getMPRegen();
	}

	public int[] getFood(int level)
	{
		return getLvlData(level).getFood();
	}

	public int getHungryLimit(int level)
	{
		return getLvlData(level).getHungryLimit();
	}

	public int getSoulshotCount(int level)
	{
		return getLvlData(level).getSoulshotCount();
	}

	public int getSpiritshotCount(int level)
	{
		return getLvlData(level).getSpiritshotCount();
	}

	public int getMaxLoad(int level)
	{
		return getLvlData(level).getMaxLoad();
	}

	public PetType getType()
	{
		return _type;
	}

	public boolean isOfType(PetType type)
	{
		return _type == type;
	}

	public MountType getMountType()
	{
		return _mountType;
	}

	public int getMinLvl()
	{
		return _minLvl;
	}

	public int getMaxLvl()
	{
		return _maxLvl;
	}

	public int getFormId(int level)
	{
		for (int i = 0; i < _syncLevels.length; i++)
		{
			if (level >= _syncLevels[i])
			{
				return i + 1;
			}
		}
		return 0;
	}

	public void addExpirationRewardItem(RewardItemData item)
	{
		_expirationRewardItems.add(item);
	}

	public RewardItemData[] getExpirationRewardItems()
	{
		return _expirationRewardItems.toArray(new RewardItemData[_expirationRewardItems.size()]);
	}

	public int getPetType()
	{
		return _petType;
	}

	public void addCanShowPassiveSkill(SkillData skillData)
	{
		_canShowPassiveSkills.add(skillData);
	}

	public SkillData[] getCanShowPassiveSkills()
	{
		return _canShowPassiveSkills.toArray(new SkillData[0]);
	}

	public void addEvolveStep(PetEvolveData petEvolveData)
	{
		_evolveSteps.put(petEvolveData.getStep(), petEvolveData);
	}

	public PetEvolveData getEvolveStep(int step)
	{
		return _evolveSteps.get(step);
	}

	public int getRandomShape(int previousShapeId)
	{
		List<Pair<Integer, Double>> randomShape = _randomShapes.get(Integer.valueOf(previousShapeId));
		if ((randomShape == null) || randomShape.isEmpty())
		{
			return -1;
		}

		EnumeratedDistribution<Integer> distribution = new EnumeratedDistribution<Integer>(randomShape);
		return distribution.sample().intValue();
	}

	public Collection<List<Pair<Integer, Double>>> getRandomShapes()
	{
		return _randomShapes.values();
	}

	public void addEvolveCost(ItemData itemData)
	{
		_evolveCost.add(itemData);
	}

	public ItemData[] getEvolveCost()
	{
		return _evolveCost.toArray(new ItemData[0]);
	}
}