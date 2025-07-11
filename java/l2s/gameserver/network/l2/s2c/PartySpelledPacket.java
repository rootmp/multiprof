package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import l2s.gameserver.model.Playable;
import l2s.gameserver.utils.AbnormalsComparator;

public class PartySpelledPacket implements IClientOutgoingPacket
{
	private final int _type;
	private final int _objId;
	private final List<Abnormal> _effects;

	public PartySpelledPacket(Playable activeChar, boolean full)
	{
		_objId = activeChar.getObjectId();
		_type = activeChar.isPet() ? 1 : activeChar.isSummon() ? 2 : 0;
		// 0 - L2Player // 1 - петы // 2 - саммоны
		_effects = new ArrayList<Abnormal>();
		if (full)
		{
			l2s.gameserver.model.actor.instances.creature.Abnormal[] effects = activeChar.getAbnormalList().toArray();
			Arrays.sort(effects, AbnormalsComparator.getInstance());
			for (l2s.gameserver.model.actor.instances.creature.Abnormal effect : effects)
			{
				if (effect != null)
					effect.addPartySpelledIcon(this);
			}
		}
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_type);
		packetWriter.writeD(_objId);
		packetWriter.writeD(_effects.size());
		for (Abnormal temp : _effects)
		{
			packetWriter.writeD(temp._skillId);
			packetWriter.writeH(temp._level);
			packetWriter.writeD(temp._abnormalType);
			writeOptionalD(temp._duration);
		}
		return true;
	}

	public void addPartySpelledEffect(int skillId, int level, int abnormalType, int duration)
	{
		_effects.add(new Abnormal(skillId, level, abnormalType, duration));
	}

	static class Abnormal
	{
		final int _skillId;
		final int _level;
		final int _abnormalType;
		final int _duration;

		public Abnormal(int skillId, int level, int abnormalType, int duration)
		{
			_skillId = skillId;
			_level = level;
			_abnormalType = abnormalType;
			_duration = duration;
		}
	}
}