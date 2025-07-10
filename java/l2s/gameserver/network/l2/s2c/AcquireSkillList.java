package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.gameserver.data.xml.holder.SkillAcquireHolder;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.SkillLearn;
import l2s.gameserver.model.base.AcquireType;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.templates.item.data.AlterItemData;

/**
 * @author VISTAL, Hl4p3x
 */
public class AcquireSkillList extends L2GameServerPacket
{
	private final Player _player;
	private final Collection<SkillLearn> _skills;
	private final Map<SkillLearn, List<Skill>> _blockedSkills = new HashMap<>();

	public AcquireSkillList(Player player)
	{
		_player = player;
		_skills = SkillAcquireHolder.getInstance().getAcquirableSkillListByClass(player);
		
		for (SkillLearn learn : _skills)
		{
			for (Skill skill : learn.getBlockedSkills())
			{
				SkillEntry knownSkill = player.getKnownSkill(skill.getId());
				if (knownSkill != null && knownSkill.getLevel() >= skill.getLevel())
				{
					_blockedSkills.computeIfAbsent(learn, k -> new ArrayList<>()).add(skill);
				}
			}
		}
	}

	@Override
	protected final void writeImpl()
	{
		if (_player == null)
		{
			return;
		}

		writeH(_skills.size());
		for (SkillLearn sk : _skills)
		{
			Skill skill = SkillHolder.getInstance().getSkill(sk.getId(), sk.getLevel());
			if (skill == null)
			{
				continue;
			}

			writeD(sk.getId());
			writeH(sk.getLevel()); // Main writeD, Essence writeH.
			writeQ(sk.getCost());
			writeC(sk.getMinLevel());
			writeC(0x00); // Dual-class min level.
			writeC(_player.getSkillLevel(sk.getId()) <= 0); // Belly - fix categories

			List<AlterItemData> requiredItems = sk.getRequiredItemsForLearn(AcquireType.NORMAL);
			writeC(requiredItems.size());
			for (AlterItemData item : requiredItems)
			{
				writeD(item.getId());
				writeQ(item.getCount());
			}

			List<Skill> blocked = _blockedSkills.getOrDefault(sk, Collections.emptyList());
			writeC(blocked.size());
			for (Skill s : blocked)
			{
				writeD(s.getId());
				writeH(s.getLevel()); // Main writeD, Essence writeH.
			}
		}
	}
}