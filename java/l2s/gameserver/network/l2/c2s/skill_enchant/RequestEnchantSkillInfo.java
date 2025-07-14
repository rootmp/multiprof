package l2s.gameserver.network.l2.c2s.skill_enchant;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestEnchantSkillInfo implements IClientIncomingPacket
{
	private int _skillId;
	private int _skillLevel;
	private int _skillSubLevel;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_skillId = packet.readD();
		_skillLevel = packet.readH();
		_skillSubLevel = packet.readH();

		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		final Skill skill = SkillHolder.getInstance().getSkill(_skillId, _skillLevel, _skillSubLevel);
		if((skill == null) || (skill.getId() != _skillId))
			return;
		/*
				final Set<Integer> route = EnchantSkillGroupsData.getInstance().getRouteForSkill(_skillId, _skillLevel);
				if (route.isEmpty())
				{
					return;
				}
				
				final Skill playerSkill = player.getKnownSkill(_skillId);
				if ((playerSkill.getLevel() != _skillLevel) || (playerSkill.getSubLevel() != _skillSubLevel))
				{
					return;
				}
				
				player.sendPacket(new ExEnchantSkillInfo(_skillId, _skillLevel, _skillSubLevel, playerSkill.getSubLevel()));*/
	}
}