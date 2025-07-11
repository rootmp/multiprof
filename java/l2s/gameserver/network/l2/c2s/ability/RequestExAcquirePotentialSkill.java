package l2s.gameserver.network.l2.c2s.ability;

import java.util.ArrayList;
import java.util.List;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;

public class RequestExAcquirePotentialSkill implements IClientIncomingPacket
{
	private List<Integer> skillIds = new ArrayList<>(); 
	private List<Integer> skillLevels = new ArrayList<>();

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		packet.readD();
		for (int i = 0; i < 3; i++)
		{
			int skillsCount = packet.readD();

			for (int j = 0; j < skillsCount; j++)
			{
				skillIds.add(packet.readD());
				skillLevels.add(packet.readD());
			}
		}

		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		List<SkillEntry> _skills = new ArrayList<>();
		for (int i = 0; i < skillIds.size(); i++)
		{
			SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, skillIds.get(i), skillLevels.get(i));
			if (skillEntry != null)
				_skills.add(skillEntry);
		}
	
		// Проверки на возможность изучения умений
	//	if(activeChar.getAbilitiesManager().getAvailablePoints() == 0/* || activeChar.getAbilitiesManager().getAvailablePoints() == activeChar.getAbilitiesManager().getUsedAbilitiesPoints()*/)
		//	return;

	/*	if(activeChar.getAbilitiesManager().getActiveSchemeId() == -1)
		{
			activeChar.sendPacket(SystemMsg.ABILITIES_CAN_BE_USED_BY_NOBLESSE_LV_99_OR_ABOVE);
			return;
		}

		if(activeChar.isInOlympiadMode())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_USE_OR_RESET_ABILITY_POINTS_WHILE_PARTICIPATING_IN_THE_OLYMPIAD_OR_CEREMONY_OF_CHAOS);
			return;
		}
		
		activeChar.getAbilitiesManager().setAbilities(_skills);*/
	}
}
