package l2s.gameserver.network.l2.s2c.ability;

import java.util.Collection;
import java.util.Collections;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.skills.SkillEntry;

public class ExAcquireAPSkillList implements IClientOutgoingPacket
{
	private int cResult;
	private int nAPresetRemainAP;
	private int nBPresetRemainAP;
	private int cCurrentPreset;
	private long nResetSP;
	private int nAP;
	private int nAcquiredAbilityCount;
	private final Collection<SkillEntry> _learnedSkills;
	
	public ExAcquireAPSkillList(Player player, int result)
	{
		cResult = result;
		nAPresetRemainAP = 0;//player.getAbilitiesManager().getRemainingPointsForSchemeA(); // оставшиеся очки для схемы A
		nBPresetRemainAP = 0;//player.getAbilitiesManager().getRemainingPointsForSchemeB(); // оставшиеся очки для схемы B
		cCurrentPreset = 0;//player.getAbilitiesManager().getActiveSchemeId();
		nResetSP = 0;//player.getAbilitiesManager().getAbilitiesRefreshPrice(); // цена сброса
		_learnedSkills = Collections.emptyList();//player.getAbilitiesManager().getLearnedAbilitiesSkills(); // изученные умения
		nAP = 0;//player.getAbilitiesManager().getAvailablePoints(); // доступные очки умений
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(cResult);
		packetWriter.writeD(nAPresetRemainAP); // оставшиеся очки для схемы A
		packetWriter.writeD(nBPresetRemainAP); // оставшиеся очки для схемы B
		packetWriter.writeC(cCurrentPreset);
		packetWriter.writeQ(nResetSP);
		packetWriter.writeD(nAP);
		packetWriter.writeD(nAcquiredAbilityCount);

		packetWriter.writeD(_learnedSkills.size());
		for(SkillEntry skill : _learnedSkills)
		{
			packetWriter.writeD(skill.getId());
			packetWriter.writeD(skill.getLevel());
		}
		return true;
	}
}
