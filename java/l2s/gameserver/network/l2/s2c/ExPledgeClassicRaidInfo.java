package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.pledge.Clan;

/**
 * @author Bonux
 **/
public class ExPledgeClassicRaidInfo implements IClientOutgoingPacket
{
	private final int _lastRaidPhase;
	private final List<Skill> _skills;

	public ExPledgeClassicRaidInfo(Player player)
	{
		Clan clan = player.getClan();
		_lastRaidPhase = clan == null ? 0 : clan.getArenaStage();
		_skills = new ArrayList<Skill>(5);
		_skills.add(SkillHolder.getInstance().getSkill(1867, 1));
		_skills.add(SkillHolder.getInstance().getSkill(1867, 2));
		_skills.add(SkillHolder.getInstance().getSkill(1867, 3));
		_skills.add(SkillHolder.getInstance().getSkill(1867, 4));
		_skills.add(SkillHolder.getInstance().getSkill(1867, 5));
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_lastRaidPhase);
		packetWriter.writeD(_skills.size());
		for(Skill skill : _skills)
		{
			packetWriter.writeD(skill.getId());
			packetWriter.writeD(skill.getLevel());
		}
		return true;
	}
}