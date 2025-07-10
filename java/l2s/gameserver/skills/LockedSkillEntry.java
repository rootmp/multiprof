package l2s.gameserver.skills;

import l2s.gameserver.model.Skill;
import l2s.gameserver.skills.enums.SkillEntryType;

public class LockedSkillEntry extends SkillEntry
{
	private final Skill _lockedSkill;

	protected LockedSkillEntry(SkillEntryType key, Skill value, Skill locked)
	{
		super(key, value);

		_lockedSkill = locked;
	}

	@Override
	public Skill getLockedSkill()
	{
		return _lockedSkill;
	}
}