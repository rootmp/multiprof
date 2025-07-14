package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.commons.network.PacketWriter;
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
public class AcquireSkillListPacket implements IClientOutgoingPacket
{
	private final Player _player;
	private final Collection<SkillLearn> _skills;
	private final Map<SkillLearn, List<Skill>> _blockedSkills = new HashMap<>();

	public AcquireSkillListPacket(Player player)
	{
		_player = player;
		_skills = SkillAcquireHolder.getInstance().getAcquirableSkillListByClass(player);

		for(SkillLearn learn : _skills)
		{
			for(Skill skill : learn.getBlockedSkills())
			{
				SkillEntry knownSkill = player.getKnownSkill(skill.getId());
				if(knownSkill != null && knownSkill.getLevel() >= skill.getLevel())
				{
					_blockedSkills.computeIfAbsent(learn, k -> new ArrayList<>()).add(skill);
				}
			}
		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(_skills.size());
		for(SkillLearn sk : _skills)
		{
			Skill skill = SkillHolder.getInstance().getSkill(sk.getId(), sk.getLevel());
			if(skill == null)
				continue;

			packetWriter.writeD(sk.getId());
			packetWriter.writeH(sk.getLevel()); // Main writeD, Essence writeH.
			packetWriter.writeQ(sk.getCost());
			packetWriter.writeC(sk.getMinLevel());
			packetWriter.writeC(0x00); // Dual-class min level.
			packetWriter.writeC(_player.getSkillLevel(sk.getId()) <= 0); // Belly - fix categories

			List<AlterItemData> requiredItems = sk.getRequiredItemsForLearn(AcquireType.NORMAL);
			packetWriter.writeC(requiredItems.size());
			for(AlterItemData item : requiredItems)
			{
				packetWriter.writeD(item.getId());
				packetWriter.writeQ(item.getCount());
			}

			List<Skill> blocked = _blockedSkills.getOrDefault(sk, Collections.emptyList());
			packetWriter.writeC(blocked.size());
			for(Skill s : blocked)
			{
				packetWriter.writeD(s.getId());
				packetWriter.writeH(s.getLevel()); // Main writeD, Essence writeH.
			}
		}
		return true;
	}
}