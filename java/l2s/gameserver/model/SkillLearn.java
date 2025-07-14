package l2s.gameserver.model;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.model.base.AcquireType;
import l2s.gameserver.model.base.ClassLevel;
import l2s.gameserver.model.base.PledgeRank;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.skills.SkillInfo;
import l2s.gameserver.stats.Env;
import l2s.gameserver.stats.conditions.Condition;
import l2s.gameserver.templates.item.data.AlterItemData;

/**
 * @author VISTALL
 */
public final class SkillLearn implements SkillInfo, Comparable<SkillLearn>
{
	private final int _id;
	private final int _level;
	private final int _hashCode;
	private final int _minLevel;
	private final int _cost;
	private final Race _race;
	private final boolean _autoGet;
	private final ClassLevel _classLevel;
	private final PledgeRank _pledgeRank;
	private final List<AlterItemData> _additionalRequiredItems = new ArrayList<>();
	private final List<AlterItemData> _allRequiredItems = new ArrayList<>();
	private final List<Skill> _blockedSkills = new ArrayList<>();
	private final List<Condition> _conditions = new ArrayList<Condition>();

	public SkillLearn(int id, int level, int minLevel, int cost, boolean autoGet, Race race, ClassLevel classLevel, PledgeRank pledgeRank)
	{
		_id = id;
		_level = level;
		_hashCode = SkillHolder.getInstance().getHashCode(_id, _level);
		_minLevel = minLevel;
		_cost = cost;
		_autoGet = autoGet;
		_race = race;
		_classLevel = classLevel;
		_pledgeRank = pledgeRank;
	}

	public SkillLearn(int id, int lvl, int minLvl, int cost, boolean autoGet, Race race)
	{
		this(id, lvl, minLvl, cost, autoGet, race, ClassLevel.NONE, PledgeRank.VAGABOND);
	}

	@Override
	public int getId()
	{
		return _id;
	}

	@Override
	public int getLevel()
	{
		return _level;
	}

	@Override
	public int getDisplayId()
	{
		return getId();
	}

	@Override
	public int getDisplayLevel()
	{
		return getLevel();
	}

	@Override
	public Skill getTemplate()
	{
		return SkillHolder.getInstance().getSkill(getId(), getLevel());
	}

	public int getMinLevel()
	{
		return _minLevel;
	}

	public int getCost()
	{
		return _cost;
	}

	public boolean isAutoGet()
	{
		return _autoGet;
	}

	public Race getRace()
	{
		return _race;
	}

	public boolean isFreeAutoGet(AcquireType type)
	{
		return isAutoGet() && getCost() == 0 && !haveRequiredItemsForLearn(type);
	}

	public boolean isOfRace(Race race)
	{
		return _race == null || _race == race;
	}

	public ClassLevel getClassLevel()
	{
		return _classLevel;
	}

	public PledgeRank getPledgeRank()
	{
		return _pledgeRank;
	}

	public void addRequiredItem(int[] id, long count)
	{
		if(id.length > 0 && count > 0)
		{
			_allRequiredItems.add(new AlterItemData(id, count));
		}
	}

	public void addRequiredItems(List<AlterItemData> items)
	{
		_allRequiredItems.addAll(items);
	}

	public List<AlterItemData> getRequiredItems()
	{
		return _allRequiredItems;
	}

	public void addAdditionalRequiredItem(int id, long count)
	{
		if(id > 0 && count > 0)
		{
			AlterItemData item = new AlterItemData(new int[] {
					id
			}, count);

			_additionalRequiredItems.add(item);
			_allRequiredItems.add(item);
		}
	}

	public void addAdditionalRequiredItems(List<AlterItemData> items)
	{
		_additionalRequiredItems.addAll(items);
		_allRequiredItems.addAll(items);
	}

	public List<AlterItemData> getAdditionalRequiredItems()
	{
		return _additionalRequiredItems;
	}

	public List<AlterItemData> getRequiredItemsForLearn(AcquireType type)
	{
		if(Config.DISABLED_SPELLBOOKS_FOR_ACQUIRE_TYPES.contains(type))
		{ return _additionalRequiredItems; }

		return _allRequiredItems;
	}

	public boolean haveRequiredItemsForLearn(AcquireType type)
	{
		return !getRequiredItemsForLearn(type).isEmpty();
	}

	public List<Skill> getBlockedSkills()
	{
		return _blockedSkills;
	}

	public void addCondition(Condition condition)
	{
		_conditions.add(condition);
	}

	public boolean testCondition(Player player)
	{
		if(_conditions.isEmpty())
		{ return true; }

		Env env = new Env();
		env.character = player;
		for(Condition condition : _conditions)
		{
			if(!condition.test(env))
			{ return false; }
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		return _hashCode;
	}

	@Override
	public int compareTo(SkillLearn o)
	{
		if(getId() == o.getId())
		{
			return getLevel() - o.getLevel();
		}
		else
		{
			return getId() - o.getId();
		}
	}

	@Override
	public int getSubLevel()
	{
		return 0;
	}
}