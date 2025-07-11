package l2s.gameserver.network.l2.s2c.pets;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.pet.PetSkillData;

/**
 * Written by Eden, on 02.03.2021
 */
public class ExPetSkillList implements IClientOutgoingPacket
{
	private final Map<Integer, SkillInfo> _skills = new HashMap<>();
	boolean _acquireSkillByEnterWorld = false;

	public ExPetSkillList(PetInstance pet, boolean acquireSkillByEnterWorld)
	{
		_acquireSkillByEnterWorld = acquireSkillByEnterWorld;
		for (PetSkillData skillData : pet.getData().getSkills())
		{
			if (!_skills.containsKey(Integer.valueOf(skillData.getId())))
			{
				SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillData.getId(), skillData.getLevel());
				if (skillEntry != null)
				{
					int haveSkillLevel = pet.getSkillLevel(skillEntry.getId(), 0);
					if (skillEntry.getLevel() == haveSkillLevel)
					{
						_skills.put(Integer.valueOf(skillData.getId()), new SkillInfo(skillData.getId(), skillData.getLevel(), skillEntry.getTemplate().getReuseSkillId(), 0, false));
					}
				}
			}
		}
		// TODO Implement for Passive Skills
	}

	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_acquireSkillByEnterWorld);
		packetWriter.writeD(_skills.size());
		for (SkillInfo skill : _skills.values())
		{
			packetWriter.writeD(skill._id);
			packetWriter.writeD(skill._level);
			packetWriter.writeD(skill._reuseGroup);
			packetWriter.writeC(skill._enchant);
			packetWriter.writeC(skill._locked);
		}
		return true;
	}

	private static class SkillInfo
	{
		final int _id;
		final int _level;
		final int _reuseGroup;
		final int _enchant;
		final boolean _locked;

		public SkillInfo(int id, int level, int reuseGroup, int enchant, boolean locked)
		{
			_id = id;
			_level = level;
			_reuseGroup = reuseGroup;
			_enchant = enchant;
			_locked = locked;
		}
	}
}