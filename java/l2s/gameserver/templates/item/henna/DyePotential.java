package l2s.gameserver.templates.item.henna;

import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.model.Skill;

/**
 * @author Serenitty
 */
public class DyePotential
{
	private final int _id;
	private final int _slotId;
	private final int _skillId;
	private final Skill[] _skills;
	private final int _maxSkillLevel;
	
	public DyePotential(int id, int slotId, int skillId, int maxSkillLevel)
	{
		_id = id;
		_slotId = slotId;
		_skillId = skillId;
		_skills = new Skill[maxSkillLevel];
		for (int i = 1; i <= maxSkillLevel; i++)
		{
			_skills[i - 1] = SkillHolder.getInstance().getSkill(skillId, i);
		}
		_maxSkillLevel = maxSkillLevel;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getSlotId()
	{
		return _slotId;
	}
	
	public int getSkillId()
	{
		return _skillId;
	}
	
	public Skill getSkill(int level)
	{
		return _skills[level - 1];
	}
	
	public int getMaxSkillLevel()
	{
		return _maxSkillLevel;
	}
}