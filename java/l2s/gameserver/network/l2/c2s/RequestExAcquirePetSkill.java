package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.pets.ExPetSkillList;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;

/**
 * Written by Eden, on 25.02.2021
 */
public class RequestExAcquirePetSkill implements IClientIncomingPacket
{
	private int skillId, skillLevel;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		skillId = packet.readD();
		skillLevel = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player activeChar = client.getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		final PetInstance pet = activeChar.getPet();
		if (pet == null)
		{
			return;
		}
		final SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillId, skillLevel);
		if (skillEntry == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		if (pet.getSkillLevel(skillEntry.getId(), 0) >= skillEntry.getLevel())
		{
			activeChar.sendActionFailed();
			return;
		}

		pet.addSkill(skillEntry);
		pet.storePetSkills(skillId, skillLevel, pet.getNpcId());
		activeChar.sendPacket(new ExPetSkillList(pet, false));
		activeChar.sendPacket((new SystemMessagePacket(SystemMsg.YOUR_PET_HAS_ACQUIRED_THE_S1_SKILL)).addSkillName(skillEntry));
	}
}
