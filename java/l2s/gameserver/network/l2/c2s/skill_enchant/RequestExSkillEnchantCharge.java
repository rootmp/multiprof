package l2s.gameserver.network.l2.c2s.skill_enchant;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.clientDat.DatParser;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.ExSkillEnchantCharge;
import l2s.gameserver.network.l2.s2c.ExSkillEnchantInfo;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.skill.enchant.SkillEnchantCharge;
import l2s.gameserver.templates.skill.enchant.SkillEnchantInfo;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExSkillEnchantCharge implements IClientIncomingPacket
{
	private int nSkillID;
	private int nLevel;
	private int nSubLevel;
	private final List<ItemData> items = new LinkedList<>();

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nSkillID = packet.readD();
	  nLevel = packet.readD();
	  nSubLevel = packet.readD();
		final int size = packet.readD();
		for (int index = 0; index < size; index++)
		{
			int nItemServerId = packet.readD();
			long nAmount = packet.readQ();
			items.add(new ItemData(nItemServerId,nAmount));
		}
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;
		/*
		Skill skill = SkillHolder.getInstance().getSkill(nSkillID, nLevel, nSubLevel);
		if (skill == null || skill.getId() != nSkillID)
			return;
		Skill ench_skill = player.getSkillById(nSkillID);
		if(ench_skill == null)
			return; 
		SkillEnchantInfo enchant = player.getSkillEnchant().findEnchant(nSkillID, nSubLevel);

		for (ItemData itemData : items) 
		{
			ItemInstance item = player.getInventory().getItemByObjectId(itemData.getId());
			if(item == null || item.getCount() < itemData.getCount())
				continue;
			
	  	SkillEnchantCharge e_charge = DatParser.getInstance().getEnchantChargeData().get(item.getItemId());
	  	Pair<Integer, Integer> _grade = e_charge.getExpGrade().get(skill.getGrade());

			if(ItemFunctions.getItemCount(player, e_charge.getCommissionItemId()) < _grade.getRight() * itemData.getCount())
				continue;
			
			if(ItemFunctions.deleteItem(player,item.getItemId(), itemData.getCount(), "ExSkillEnchantCharge"))
			{
				if(ItemFunctions.deleteItem(player, e_charge.getCommissionItemId(), _grade.getRight() * itemData.getCount(), "ExSkillEnchantCharge"))
					enchant.addExp((int) (_grade.getLeft()*itemData.getCount()));
			}
		}
		
		player.getSkillEnchant().update(enchant);
		player.sendPacket(new ExSkillEnchantInfo(enchant));
		player.sendPacket(new ExSkillEnchantCharge(nSkillID));*/
	}

}