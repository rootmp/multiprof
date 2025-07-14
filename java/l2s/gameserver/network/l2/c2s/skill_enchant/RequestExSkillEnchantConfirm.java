package l2s.gameserver.network.l2.c2s.skill_enchant;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExSkillEnchantConfirm implements IClientIncomingPacket
{
	private int nSkillID;
	private int nConfirmItemClassID;
	@SuppressWarnings("unused")
	private int nCommisionClassID;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nSkillID = packet.readD();
		nConfirmItemClassID = packet.readD();
		nCommisionClassID = packet.readD();

		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;
		/*Skill skill = player.getSkillById(nSkillID);
		if(skill == null)
			return;
		
		CouponSetting setting = SkillEnchantSettingsHolder.getInstance().getCouponSetting(nConfirmItemClassID);
		if(setting!=null)
		{
			if(!ItemFunctions.haveItem(player, nConfirmItemClassID, 1) || setting.getSublevel() == skill.getSubLevel())
			{
				player.sendPacket(new ExRequestSkillEnchantConfirm(1,nSkillID, skill.getLevel(), skill.getSubLevel()));
				return;
			}
			if(!ItemFunctions.haveItem(player, 57, setting.getAdena()))
			{
				player.sendPacket(new ExRequestSkillEnchantConfirm(1,nSkillID, skill.getLevel(), skill.getSubLevel()));
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			if(!ItemFunctions.haveItem(player, setting.getLcoinId(), setting.getLcoinCount()))
			{
				player.sendPacket(new ExRequestSkillEnchantConfirm(1,nSkillID, skill.getLevel(), skill.getSubLevel()));
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_L2_COINS_ADD_MORE_L2_COINS_AND_TRY_AGAIN);
				return;
			}
			
			ItemFunctions.deleteItem(player, 57, setting.getAdena(), "RequestExExtractSkillEnchant");
			ItemFunctions.deleteItem(player, setting.getLcoinId(), setting.getLcoinCount(), "RequestExExtractSkillEnchant");
			ItemFunctions.deleteItem(player, setting.getCouponId(), 1, "ExExtractSkillEnchant");
			
			SkillEntry enchantedSkill = SkillEntry.makeSkillEntry(SkillEntryType.NONE, nSkillID, skill.getLevel(), setting.getSublevel());
			if (enchantedSkill == null)
			{
				player.sendPacket(new ExRequestSkillEnchantConfirm(1,nSkillID, skill.getLevel(), skill.getSubLevel()));
				return;
			}
		
			player.addSkill(enchantedSkill, true);
			player.enableSkill(enchantedSkill.getTemplate());
			
			player.sendUserInfo();
			player.updateStats();
			player.sendSkillList();
			
			player.sendPacket(new ExRequestSkillEnchantConfirm(0,enchantedSkill.getId(), enchantedSkill.getLevel(), enchantedSkill.getSubLevel()));
		}*/
	}
}