package l2s.gameserver.templates.item.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import l2s.gameserver.model.Player;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.item.ExItemType;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.Visual;

/**
 * @author Bonux
 */
public class AppearanceStone
{
	public static enum ShapeType
	{
		NONE,
		NORMAL,
		BLESSED,
		FIXED,
		RESTORE
	}

	public static enum ShapeTargetType
	{
		NONE,
		WEAPON,
		ARMOR,
		ACCESSORY,
		ALL
	}

	private final int _itemId;
	private final ShapeTargetType[] _targetTypes;
	private final ShapeTargetType _clientTargetType;
	private final ShapeType _type;
	private final ItemGrade[] _grades;
	private final long _cost;
	private final int _period;
	private final List<SkillEntry> _skills = new ArrayList<SkillEntry>();
	private final Map<ExItemType, Visual> _visual;

	public AppearanceStone(int itemId, ShapeTargetType[] targetTypes, ShapeType type, ItemGrade[] grades, long cost,  Map<ExItemType, Visual> visual, int period)
	{
		_itemId = itemId;
		_targetTypes = targetTypes;
		_type = type;
		_grades = grades;
		_cost = cost;
		_visual = visual;
		_period = period;

		if(_targetTypes.length > 1)
			_clientTargetType = ShapeTargetType.ALL;
		else
			_clientTargetType = _targetTypes[0];
	}

	public int getItemId()
	{
		return _itemId;
	}

	public ShapeTargetType[] getTargetTypes()
	{
		return _targetTypes;
	}

	public ShapeTargetType getClientTargetType()
	{
		return _clientTargetType;
	}

	public ShapeType getType()
	{
		return _type;
	}

	public ItemGrade[] getGrades()
	{
		return _grades;
	}

	public long getCost()
	{
		return _cost;
	}

	public int getPeriod()
	{
		return _period;
	}

	public void addSkill(int id, int level)
	{
		SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, id, level);
		if(skillEntry != null)
			_skills.add(skillEntry);
	}

	public List<SkillEntry> getSkills()
	{
		return _skills;
	}

	public Visual getVisual(ExItemType type)
	{
		return _visual.get(type);
	}
	
	public Map<ExItemType, Visual> getVisual()
	{
		return _visual;
	}

	public int getVisual(Player player, ExItemType type)
	{
		Visual visual = _visual.get(type);
		
		if(visual.getAlternative().containsKey(player.getRace()))
			return visual.getAlternative().get(player.getRace());

		return _visual.get(type).getExtractId();
	}
	
}
