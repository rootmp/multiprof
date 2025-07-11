package l2s.gameserver.skills;

import l2s.gameserver.model.Skill;

public interface SkillInfo
{
	int getId();

	int getDisplayId();

	int getLevel();

	int getDisplayLevel();

	Skill getTemplate();

	int getSubLevel();
}
