package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.AppearanceStoneHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.PcInventory;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExPut_Shape_Shifting_Extraction_Item_Result;
import l2s.gameserver.network.l2.s2c.ExShape_Shifting_Result;
import l2s.gameserver.templates.item.ExItemType;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.support.AppearanceStone;
import l2s.gameserver.templates.item.support.AppearanceStone.ShapeType;

/**
 * @author Bonux
 **/
public class RequestExTryToPutShapeShiftingEnchantSupportItem implements IClientIncomingPacket
{
	private int _targetItemObjId;
	private int _extracItemObjId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_targetItemObjId = packet.readD();
		_extracItemObjId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
		{ return; }

		if(player.isActionsDisabled() || player.isInStoreMode() || player.isInTrade())
		{
			player.sendPacket(ExShape_Shifting_Result.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		PcInventory inventory = player.getInventory();
		ItemInstance targetItem = inventory.getItemByObjectId(_targetItemObjId);
		ItemInstance extracItem = inventory.getItemByObjectId(_extracItemObjId);
		ItemInstance stone = player.getAppearanceStone();
		if((targetItem == null) || (extracItem == null) || (stone == null))
		{
			player.sendPacket(ExShape_Shifting_Result.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		if(!extracItem.canBeAppearance())
		{
			player.sendPacket(ExPut_Shape_Shifting_Extraction_Item_Result.FAIL);
			return;
		}

		if((extracItem.getLocation() != ItemLocation.INVENTORY) && (extracItem.getLocation() != ItemLocation.PAPERDOLL))
		{
			player.sendPacket(ExShape_Shifting_Result.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		if((stone = inventory.getItemByObjectId(stone.getObjectId())) == null)
		{
			player.sendPacket(ExShape_Shifting_Result.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		AppearanceStone appearanceStone = AppearanceStoneHolder.getInstance().getAppearanceStone(stone.getItemId());
		if(appearanceStone == null)
		{
			player.sendPacket(ExShape_Shifting_Result.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		if((appearanceStone.getType() == ShapeType.RESTORE) || (appearanceStone.getType() == ShapeType.FIXED))
		{
			player.sendPacket(ExShape_Shifting_Result.FAIL);
			return;
		}

		/*
		 * if(!extracItem.getTemplate().isHairAccessory() && extracItem.getGrade() ==
		 * ItemGrade.NONE) { player.sendPacket(SystemMsg.ITEM_GRADES_DO_NOT_MATCH);
		 * return; }
		 */

		if(!extracItem.getTemplate().isHairAccessory() && (targetItem.getGrade().ordinal() < extracItem.getGrade().ordinal()))
		{
			player.sendPacket(SystemMsg.YOU_CANNOT_EXTRACT_FROM_ITEMS_THAT_ARE_HIGHERGRADE_THAN_ITEMS_TO_BE_MODIFIED);
			return;
		}

		if(extracItem.getVisualId() > 0)
		{
			player.sendPacket(SystemMsg.YOU_CANNOT_EXTRACT_FROM_A_MODIFIED_ITEM);
			return;
		}

		if(targetItem.getExType() != extracItem.getExType())
		{
			if((targetItem.getExType() != ExItemType.UPPER_PIECE) && (extracItem.getExType() != ExItemType.FULL_BODY))
			{
				player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
				return;
			}
		}

		if(targetItem.isWeapon())
		{
			if(targetItem.getTemplate().getItemType() != extracItem.getTemplate().getItemType())
			{
				player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
				return;
			}
		}
		else if(targetItem.isArmor())
		{
			if(Config.APPEARANCE_STONE_CHECK_ARMOR_TYPE)
			{
				if((targetItem.getBodyPart() == ItemTemplate.SLOT_CHEST) || (targetItem.getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR)
						|| (targetItem.getBodyPart() == ItemTemplate.SLOT_LEGS))
				{
					if(extracItem.getTemplate().isArmor() && ((extracItem.getTemplate().getBodyPart() == ItemTemplate.SLOT_CHEST)
							|| (extracItem.getTemplate().getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR)
							|| (extracItem.getTemplate().getBodyPart() == ItemTemplate.SLOT_LEGS)))
					{
						if(targetItem.getTemplate().getItemType() != extracItem.getTemplate().getItemType())
						{
							player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
							return;
						}
					}
				}
			}
		}

		// Запрет на обработку чужих вещей, баг может вылезти на серверных лагах
		if(extracItem.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExShape_Shifting_Result.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		player.setAppearanceExtractItem(extracItem);
		player.sendPacket(ExPut_Shape_Shifting_Extraction_Item_Result.SUCCESS);
	}
}