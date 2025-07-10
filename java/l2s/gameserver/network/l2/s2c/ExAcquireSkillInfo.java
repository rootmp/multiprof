package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.SkillLearn;
import l2s.gameserver.model.base.AcquireType;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.templates.item.data.AlterItemData;
import l2s.gameserver.templates.item.data.ItemData;

public class ExAcquireSkillInfo implements IClientOutgoingPacket
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
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_learn.getId());
		packetWriter.writeD(_learn.getLevel());
		packetWriter.writeQ(_learn.getCost());
		packetWriter.writeH(_learn.getMinLevel());
		packetWriter.writeH(0x00); // Dual-class min level.
		packetWriter.writeD(_requiredItems.size());
		for (ItemData item : _requiredItems)
		{
			packetWriter.writeD(item.getId());
			packetWriter.writeQ(item.getCount());
		}
		packetWriter.writeD(_blockedSkills.size());
		for (Skill skill : _blockedSkills)
		{
			packetWriter.writeD(skill.getId());
			packetWriter.writeD(skill.getLevel());
		}
	}
}