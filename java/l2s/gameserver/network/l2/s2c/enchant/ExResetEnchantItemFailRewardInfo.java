package l2s.gameserver.network.l2.s2c.enchant;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.data.xml.holder.EnchantItemHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.item.support.EnchantScroll;
import l2s.gameserver.templates.item.support.EnchantStone;
import l2s.gameserver.templates.item.support.EnchantVariation;
import l2s.gameserver.templates.item.support.FailResultType;
import l2s.gameserver.templates.item.support.EnchantVariation.EnchantLevel;
import l2s.gameserver.utils.ItemFunctions;


public class ExResetEnchantItemFailRewardInfo implements IClientOutgoingPacket
{
	private ItemInstance enchantItem;
	private int challengeGroup;
	private int challengePoints;
	private List<ItemData> items = new ArrayList<ItemData>();
	private boolean _write;
	
	public ExResetEnchantItemFailRewardInfo(Player player, int _itemObjId)
	{
		final EnchantScroll enchantScroll = EnchantItemHolder.getInstance().getEnchantScroll(player.getEnchantScroll().getItemId());
		if (enchantScroll == null)
			return;

		enchantItem = player.getEnchantItem();
		
		final EnchantVariation variation = EnchantItemHolder.getInstance().getEnchantVariation(enchantScroll.getVariationId(enchantItem.getItemId()));
		if(variation == null)
		{
			player.sendActionFailed();
			return;
		}
		
		final EnchantLevel enchantLevel = variation.getLevel(enchantItem.getEnchantLevel() + 1);
		if(enchantLevel == null)
		{
			player.sendActionFailed();
			return;
		}
		
		ItemInstance addedItem = player.getInventory().getItemByObjectId(_itemObjId);
		if (addedItem == null)
			return;

		if (enchantItem.getObjectId() != addedItem.getObjectId())
			return;

		EnchantStone stone = null;
		if (player.getSupportItem() != null)
			stone = ItemFunctions.getEnchantStone(player.getEnchantItem(), player.getSupportItem());
		
		
		challengeGroup = enchantScroll.getChallengeGroup(enchantItem.getItemId());
		challengePoints = enchantLevel.getChallengeItemCount();

		if ((enchantScroll.getResultType() == FailResultType.DROP_ENCHANT) || ((stone != null) && (stone.getResultType() == FailResultType.DROP_ENCHANT)))
		{
			int enchantDropCount = enchantScroll.getEnchantDropCount();
			if (stone != null && stone.getEnchantDropCount() < enchantDropCount)
				enchantDropCount = stone.getEnchantDropCount();
			
			addedItem.setEnchantLevel(Math.max(addedItem.getEnchantLevel() - enchantDropCount, 0));
		}
		else if (enchantScroll.getResultType() == FailResultType.NOTHING)
			addedItem.setEnchantLevel(enchantItem.getEnchantLevel());
		else
		{
			addedItem = null;
			if(enchantItem.getGrade().getCrystalId() > 0 && enchantItem.getCrystalCountOnEchant() > 0)
				items.add(new ItemData(enchantItem.getGrade().getCrystalId(), enchantItem.getCrystalCountOnEchant()));
			
			int[] fail_stone = enchantItem.getEnchantFailStone();
			if(fail_stone[0] > 0 && fail_stone[1] > 0)
				items.add(new ItemData(fail_stone[0], fail_stone[1]));
		}
		if (addedItem != null)
		{
			items.clear();
			items.add(new ItemData(enchantItem.getItemId(), enchantItem.getCount()));
		}
		
		switch(player.getEnchantChallengePoint())
		{
			case BLANK:
				break;
			case ENCHANT_SAVE_ENCHANT_LEVEL:
			case ENCHANT_MINUS_ONE_ON_FAIL:
				items.clear();
				items.add(new ItemData(enchantItem.getItemId(), enchantItem.getCount()));
				break;
			case ENCHANT_LEVEL_PLUS_ONE_TWO:
			case ENCHANT_SUCCESS_PLUS_4_PERCENTS:
			case ENCHANT_SUCCESS_PLUS_8_PERCENTS:
			case ENCHANT_TO_ZERO:
				items.clear();
				challengeGroup = 0;
				challengePoints = 0;
				break;
			default:
				break;
			
		}
		_write = true;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if(!_write)
			return false;
		packetWriter.writeD(enchantItem.getItemId()); // nItemServerId
		packetWriter.writeD(challengeGroup); // nEnchantChallengePointGroupId
		packetWriter.writeD(challengePoints); // nEnchantChallengePoint
		
		packetWriter.writeD(items.size()); // nSize
		for(ItemData item : items)
		{
			packetWriter.writeD(item.getId()); // nItemClassID
			packetWriter.writeD((int) item.getCount()); // nCount
		}

		return true;
	}
}