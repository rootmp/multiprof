package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.SkillLearn;
import l2s.gameserver.model.base.AcquireType;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.templates.item.data.AlterItemData;
import l2s.gameserver.templates.item.data.ItemData;

public class ExAcquireSkillInfo extends L2GameServerPacket
{
	private final SkillLearn _learn;
	private final List<AlterItemData> _requiredItems;
	private final List<Skill> _blockedSkills = new ArrayList<>();

	public ExAcquireSkillInfo(Player player, AcquireType type, SkillLearn learn)
	{
		_learn = learn;
		_requiredItems = _learn.getRequiredItemsForLearn(type);
		for (Skill skill : learn.getBlockedSkills())
		{
			SkillEntry knownSkill = player.getKnownSkill(skill.getId());
			if (knownSkill != null && knownSkill.getLevel() >= skill.getLevel())
			{
				_blockedSkills.add(skill);
			}
		}
	}

	@Override
	public void writeImpl()
	{
		writeD(_learn.getId());
		writeD(_learn.getLevel());
		writeQ(_learn.getCost());
		writeH(_learn.getMinLevel());
		writeH(0x00); // Dual-class min level.
		writeD(_requiredItems.size());
		for (ItemData item : _requiredItems)
		{
			writeD(item.getId());
			writeQ(item.getCount());
		}
		writeD(_blockedSkills.size());
		for (Skill skill : _blockedSkills)
		{
			writeD(skill.getId());
			writeD(skill.getLevel());
		}
	}
}