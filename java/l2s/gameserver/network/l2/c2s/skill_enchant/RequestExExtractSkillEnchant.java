package l2s.gameserver.network.l2.c2s.skill_enchant;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExExtractSkillEnchant implements IClientIncomingPacket
{
	private int _skillId;
	private int _skillLevel;
	private int _skillSubLevel;
	@SuppressWarnings("unused")
	private int _itemClassID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_skillId = packet.readD();
		_skillLevel = packet.readD();
		_skillSubLevel = packet.readD();
		_itemClassID = packet.readD();

		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		/*final Skill skill = SkillHolder.getInstance().getSkill(_skillId, _skillLevel, _skillSubLevel);
		if (skill == null || skill.getId() != _skillId || skill.getSubLevel() != _skillSubLevel)
			return;

		SkillEnchantSetting setting = SkillEnchantSettingsHolder.getInstance().getExtractSetting(skill.getGrade(), _skillSubLevel);
		if(setting!=null)
		{
			if(!ItemFunctions.haveItem(player, setting.getItemId(), setting.getCount()))
			{
				player.sendPacket(new ExExtractSkillEnchant(1, _skillId, _skillLevel, _skillSubLevel));
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_L2_COINS_ADD_MORE_L2_COINS_AND_TRY_AGAIN);
				return;
			}
			
			if(!ItemFunctions.haveItem(player, 57, setting.getAdena()))
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				player.sendPacket(new ExExtractSkillEnchant(1, _skillId, _skillLevel, _skillSubLevel));
				return;
			}
			SkillEntry enchantedSkill = SkillEntry.makeSkillEntry(SkillEntryType.NONE, _skillId, _skillLevel, 0);
			if (enchantedSkill == null)
			{
				player.sendPacket(new ExExtractSkillEnchant(1, _skillId, _skillLevel, _skillSubLevel));
				return;
			}
			
			//сбросить накопленый опыт 
			SkillEnchantInfo enchant = player.getSkillEnchant().findEnchant(enchantedSkill.getId(), 0);
			if(enchant!=null)
			{
				enchant.setExp(0);
				player.getSkillEnchant().update(enchant);
			}
			if(_skillSubLevel > 0)
			{
				for(int i =_skillSubLevel; i>1000; i--)
				{
					enchant = player.getSkillEnchant().findEnchant(enchantedSkill.getId(), i);
					if(enchant!=null)
					{
						enchant.setExp(0);
						player.getSkillEnchant().update(enchant);
					}
				}
			}
			
			player.removeSkill(enchantedSkill.getId(), true);
			
			player.addSkill(enchantedSkill, true);
			player.enableSkill(enchantedSkill.getTemplate());
			
			player.sendChanges();

			ItemFunctions.deleteItem(player, 57, setting.getAdena(), "RequestExExtractSkillEnchant");
			ItemFunctions.deleteItem(player, setting.getItemId(), setting.getCount(), "RequestExExtractSkillEnchant");
			
			ItemFunctions.addItem(player, setting.getSuccessItemId(), 1, "ExExtractSkillEnchant");
			player.sendPacket(new ExExtractSkillEnchant(0, enchantedSkill.getId(), enchantedSkill.getLevel(), enchantedSkill.getSubLevel()));
		}else
			player.sendPacket(new ExExtractSkillEnchant(1, _skillId, _skillLevel, _skillSubLevel));*/
	}
}