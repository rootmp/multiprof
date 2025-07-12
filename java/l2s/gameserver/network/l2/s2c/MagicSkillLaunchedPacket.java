package l2s.gameserver.network.l2.s2c;

import java.util.Collection;
import java.util.Collections;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.skills.enums.SkillCastingType;

public class MagicSkillLaunchedPacket implements IClientOutgoingPacket
{
	private final int _casterId;
	private final int _skillId;
	private final int _skillLevel;
	private final int _skillSubLevel;
	private final Collection<Creature> _targets;
	private final SkillCastingType _castingType;

	public MagicSkillLaunchedPacket(int casterId, int skillId, int skillLevel, int skillSubLevel, Creature target, SkillCastingType castingType)
	{
		_casterId = casterId;
		_skillId = skillId;
		_skillLevel = skillLevel;
		_skillSubLevel = skillSubLevel;
		_targets = Collections.singletonList(target);
		_castingType = castingType;
	}

	public MagicSkillLaunchedPacket(int casterId, int skillId, int skillLevel, int skillSubLevel, Collection<Creature> targets, SkillCastingType castingType)
	{
		_casterId = casterId;
		_skillId = skillId;
		_skillLevel = skillLevel;
		_skillSubLevel = skillSubLevel;
		_targets = targets;
		_castingType = castingType;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_castingType.getClientBarId()); // Casting bar type: 0 - default, 1 - default up, 2 - blue, 3 - green, 4 - red.
		packetWriter.writeD(_casterId);
		packetWriter.writeD(_skillId);
		//(_skillSubLevel << 16) | (_skillLevel & 0xFFFF)
		packetWriter.writeH(_skillLevel);
		packetWriter.writeH(_skillSubLevel);
		packetWriter.writeD(_targets.size());
		for(Creature target : _targets)
		{
			if(target != null)
				packetWriter.writeD(target.getObjectId());
		}
		return true;
	}

	@Override
	public IClientOutgoingPacket packet(Player player)
	{
		if(player != null)
		{
			if(player.isNotShowBuffAnim())
				return _casterId == player.getObjectId() ? this : null;
		}

		return this;
	}
}