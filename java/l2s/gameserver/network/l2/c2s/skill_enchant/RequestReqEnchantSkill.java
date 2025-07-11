package l2s.gameserver.network.l2.c2s.skill_enchant;

import l2s.commons.network.PacketReader;
import l2s.commons.util.Rnd;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExEnchantSkillResult;
import l2s.gameserver.network.l2.s2c.ExSkillEnchantInfo;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.service.StageService;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.skill.enchant.SkillEnchantInfo;
import l2s.gameserver.utils.ItemFunctions;

public class RequestReqEnchantSkill implements IClientIncomingPacket
{
	private int _skillId;
	private int _skillLevel;
	private int _skillSubLevel;
	@SuppressWarnings("unused")
	private int _type;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_type = packet.readD();
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
		if (_skillId <= 0 || _skillLevel <= 0 || _skillSubLevel < 0)
			return;
		if (player.isInOlympiadMode())
			return;
		Skill skill = player.getSkillById(_skillId);
		if (skill == null)
			return;

		if (!skill.isEnchantable())
			return;

		if ((skill.getLevel()==0 && _skillLevel != 1001) || skill.getLevel() != _skillLevel)
			return;
		
		if ((skill.getSubLevel() ==0 && _skillSubLevel != 1001) || (skill.getSubLevel() >0 && skill.getSubLevel()+1 != _skillSubLevel))
			return;
		
		if(StageService.getInstance().getCurrentStage().getSkillEnchantLimit().containsKey(skill.getGrade()))
		{
			int ench_max = StageService.getInstance().getCurrentStage().getSkillEnchantLimit().get(skill.getGrade());
			if(skill.getSubLevel() > ench_max*1000 )
			{
				player.sendPacket(new SystemMessagePacket(SystemMsg.AT_STAGE_S1_THE_SKILL_HAS_REACHED_ITS_MAXIMUM_VALUE).addInteger(StageService.getInstance().getCurrentStageId() + 1));
				return;
			}
		}
		
		SkillEnchantInfo enchant = player.getSkillEnchant().findEnchant(_skillId, skill.getSubLevel());

		if(enchant.getEXP() < 900000)
			return;

		int chance = 0;
		switch(skill.getSubLevel())
		{
			case 0:
				chance = 50;
				break;
			case 1001:
				chance = 30;
				break;
			case 1002:
				chance = 10;
				break;
			default:
				break;
		}

		if(!ItemFunctions.deleteItem(player,  57, 1_000_000, "RequestReqEnchantSkill"))
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ALL_OF_THE_ITEMS_NEEDED_TO_ENCHANT_THAT_SKILL);//TODO ??
			return;
		}

		if (Rnd.chance(chance))
		{
			SkillEntry enchantedSkill = SkillEntry.makeSkillEntry(SkillEntryType.NONE, _skillId, _skillLevel, _skillSubLevel);
			if (enchantedSkill == null)
				return;

			player.addSkill(enchantedSkill, true);
			player.enableSkill(enchantedSkill.getTemplate());
			
			enchant = player.getSkillEnchant().findEnchant(enchantedSkill.getId(), enchantedSkill.getSubLevel());

			player.sendUserInfo();
			player.updateStats();
			player.sendSkillList();
			
			player.sendPacket(ExEnchantSkillResult.STATIC_PACKET_TRUE);
		}
		else
		{
			enchant.setExp(90_000);
			player.getSkillEnchant().update(enchant);
			player.sendPacket(ExEnchantSkillResult.STATIC_PACKET_FALSE);
		}
		
		player.sendSkillList();
		player.broadcastUserInfo(false);

		skill = player.getSkillById(_skillId);

		player.sendPacket(new ExSkillEnchantInfo(enchant));
		//player.sendPacket(new ExEnchantSkillInfoDetailPacket(_type, skill.getId(), skill.getLevel(), skill.getSubLevel(), player));
		player.updateSkillShortcuts(skill.getId(), skill.getLevel(), skill.getSubLevel());
		
	}
}