package l2s.gameserver.network.l2.c2s.skill_enchant;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExSkillEnchantInfo implements IClientIncomingPacket
{
	private int _skillId;
	private int _skillLevel;
	private int _skillSubLevel;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_skillId = packet.readD();
		_skillLevel = packet.readD();
		_skillSubLevel = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		if(_skillId <= 0 || _skillLevel <= 0 || _skillSubLevel < 0)
			return;
		/*
				final Skill skill = SkillHolder.getInstance().getSkill(_skillId, _skillLevel, _skillSubLevel);
				if (skill == null || skill.getId() != _skillId)
					return;
		
				Skill skill_p = player.getSkillById(_skillId);
				
				if(skill_p == null || skill_p.getLevel() != _skillLevel || skill_p.getSubLevel() != _skillSubLevel)
					return;
		
				player.sendPacket(new ExSkillEnchantInfo(player.getSkillEnchant().findEnchant(_skillId, _skillSubLevel)));*/
	}
}