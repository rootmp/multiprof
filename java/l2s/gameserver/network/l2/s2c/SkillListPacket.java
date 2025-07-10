package l2s.gameserver.network.l2.s2c;

import java.util.Collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.skills.SkillEntry;

/**
 * format d (dddc) d dddcc
 */
public class SkillListPacket extends L2GameServerPacket
{
	private final Collection<SkillEntry> _skills;
	private final Player _player;
	private final int _learnedSkillId;

	public SkillListPacket(Player player)
	{
		_skills = player.getAllSkills();
		_player = player;
		_learnedSkillId = 0;
	}

	public SkillListPacket(Player player, int learnedSkillId)
	{
		_skills = player.getAllSkills();
		_player = player;
		_learnedSkillId = learnedSkillId;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_skills.size() - _player.getDisabledSkillsToReplace().size() - _player.getHiddenSkills().size());
		for (SkillEntry skillEntry : _skills)
		{
			if (_player.isDisabledSkillToReplace(skillEntry.getId()))
				continue;

			if (_player.getHiddenSkills().contains(skillEntry.getId()))
				continue;

			Skill temp = skillEntry.getTemplate();
			writeD(temp.isActive() || temp.isToggle() ? 0 : 1); // deprecated? клиентом игнорируется
			writeD(temp.getDisplayLevel());
			writeD(temp.getDisplayId());
			writeD(temp.getReuseSkillId());
			writeC(_player.isUnActiveSkill(temp.getId()) ? 0x01 : 0x00); // иконка скилла серая если не 0
			writeC(0x00); // для заточки: если 1 скилл можно точить
		}
		writeD(_learnedSkillId);
	}
}