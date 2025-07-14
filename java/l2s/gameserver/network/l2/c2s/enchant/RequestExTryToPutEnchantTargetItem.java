package l2s.gameserver.network.l2.c2s.enchant;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.EnchantItemHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.PcInventory;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExPutEnchantTargetItemResult;
import l2s.gameserver.network.l2.s2c.enchant.ExChangedEnchantTargetItemProbabilityList;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.support.EnchantScroll;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.Log;

public class RequestExTryToPutEnchantTargetItem implements IClientIncomingPacket
{
	private int _objectId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_objectId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
		{ return; }

		if(player.isActionsDisabled() || player.isInStoreMode() || player.isInTrade())
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			return;
		}

		final PcInventory inventory = player.getInventory();
		final ItemInstance itemToEnchant = inventory.getItemByObjectId(_objectId);

		ItemInstance scroll = player.getEnchantScroll();

		if((itemToEnchant == null) || (scroll == null))
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			return;
		}

		Log.add(player.getName() + "|Trying to put enchant|" + itemToEnchant.getItemId() + "|+" + itemToEnchant.getEnchantLevel() + "|"
				+ itemToEnchant.getObjectId(), "enchants");

		final int scrollId = scroll.getItemId();
		final int itemId = itemToEnchant.getItemId();

		final EnchantScroll enchantScroll = EnchantItemHolder.getInstance().getEnchantScroll(scrollId);

		if((!enchantScroll.getItems().contains(itemId) && !itemToEnchant.canBeEnchanted()) || itemToEnchant.isStackable())
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
			player.setEnchantScroll(null);
			return;
		}

		if((itemToEnchant.getLocation() != ItemLocation.INVENTORY) && (itemToEnchant.getLocation() != ItemLocation.PAPERDOLL))
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
			return;
		}

		if(player.isInStoreMode())
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			player.sendPacket(SystemMsg.YOU_CANNOT_ENCHANT_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			return;
		}

		if((scroll = inventory.getItemByObjectId(scroll.getObjectId())) == null)
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			return;
		}

		if(enchantScroll.getItems().size() > 0)
		{
			if(!enchantScroll.getItems().contains(itemId))
			{
				player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
				player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
				return;
			}
		}
		else
		{
			if(!enchantScroll.containsGrade(itemToEnchant.getGrade()))
			{
				player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
				player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
				return;
			}

			int itemType = itemToEnchant.getTemplate().getType2();
			switch(enchantScroll.getType())
			{
				case ARMOR:
					if((itemType == ItemTemplate.TYPE2_WEAPON) || itemToEnchant.getTemplate().isHairAccessory())
					{
						player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
						player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
						return;
					}
					break;
				case WEAPON:
					if((itemType == ItemTemplate.TYPE2_SHIELD_ARMOR) || (itemType == ItemTemplate.TYPE2_ACCESSORY)
							|| itemToEnchant.getTemplate().isHairAccessory())
					{
						player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
						player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
						return;
					}
					break;
				case HAIR_ACCESSORY:
					if(!itemToEnchant.getTemplate().isHairAccessory())
					{
						player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
						player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
						return;
					}
					break;
			}
		}

		if((itemToEnchant.getEnchantLevel() < enchantScroll.getMinEnchant())
				|| ((enchantScroll.getMaxEnchant() != -1) && (itemToEnchant.getEnchantLevel() >= enchantScroll.getMaxEnchant())))
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
			return;
		}

		// Запрет на заточку чужих вещей, баг может вылезти на серверных лагах
		if(itemToEnchant.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			return;
		}

		player.setEnchantItem(itemToEnchant);
		player.sendPacket(ExPutEnchantTargetItemResult.SUCCESS);
		player.sendPacket(new ExChangedEnchantTargetItemProbabilityList(ItemFunctions.getEnchantProbInfo(player, false, false)));
	}
}
