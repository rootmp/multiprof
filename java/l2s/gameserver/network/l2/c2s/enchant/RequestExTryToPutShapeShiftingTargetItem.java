package l2s.gameserver.network.l2.c2s.enchant;

import org.apache.commons.lang3.ArrayUtils;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.AppearanceStoneHolder;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.PcInventory;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExPut_Shape_Shifting_Target_Item_Result;
import l2s.gameserver.network.l2.s2c.ExShape_Shifting_Result;
import l2s.gameserver.templates.item.ExItemType;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.support.AppearanceStone;
import l2s.gameserver.templates.item.support.AppearanceStone.ShapeTargetType;
import l2s.gameserver.templates.item.support.AppearanceStone.ShapeType;

/**
 * @author Bonux
**/
public class RequestExTryToPutShapeShiftingTargetItem implements IClientIncomingPacket
{
	private int _targetItemObjId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_targetItemObjId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if(player == null)
			return;

		if(player.isBlocked() || player.isAlikeDead() || player.isInStoreMode() || player.isInTrade())
		{
			player.sendPacket(ExShape_Shifting_Result.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		PcInventory inventory = player.getInventory();
		ItemInstance targetItem = inventory.getItemByObjectId(_targetItemObjId);
		ItemInstance stone = player.getAppearanceStone();
		if(targetItem == null || stone == null)
		{
			player.sendPacket(ExShape_Shifting_Result.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		if(!targetItem.canBeAppearance())
		{
			player.sendPacket(ExPut_Shape_Shifting_Target_Item_Result.FAIL);
			return;
		}

		if(targetItem.getLocation() != ItemInstance.ItemLocation.INVENTORY && targetItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL)
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

		if(appearanceStone.getType() != ShapeType.RESTORE && targetItem.getVisualId() > 0 || appearanceStone.getType() == ShapeType.RESTORE && targetItem.getVisualId() == 0)
		{
			player.sendPacket(ExPut_Shape_Shifting_Target_Item_Result.FAIL);
			return;
		}

		if(!targetItem.getTemplate().isHairAccessory() && targetItem.getGrade() == ItemGrade.NONE)
		{
			player.sendPacket(SystemMsg.YOU_CANNOT_MODIFY_OR_RESTORE_NOGRADE_ITEMS);
			return;
		}

		ItemGrade[] stoneGrades = appearanceStone.getGrades();
		if(stoneGrades != null && stoneGrades.length > 0)
		{
			if(!ArrayUtils.contains(stoneGrades, targetItem.getGrade()))
			{
				player.sendPacket(SystemMsg.ITEM_GRADES_DO_NOT_MATCH);
				return;
			}
		}

		ShapeTargetType[] targetTypes = appearanceStone.getTargetTypes();
		if(targetTypes == null || targetTypes.length == 0)
		{
			player.sendPacket(ExShape_Shifting_Result.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		if(!ArrayUtils.contains(targetTypes, ShapeTargetType.ALL))
		{
			if(targetItem.isWeapon())
			{
				if(!ArrayUtils.contains(targetTypes, ShapeTargetType.WEAPON))
				{
					player.sendPacket(SystemMsg.WEAPONS_ONLY);
					return;
				}
			}
			else if(targetItem.isArmor())
			{
				if(!ArrayUtils.contains(targetTypes, ShapeTargetType.ARMOR))
				{
					player.sendPacket(SystemMsg.ARMOR_ONLY);
					return;
				}
			}
			else
			{
				if(!ArrayUtils.contains(targetTypes, ShapeTargetType.ACCESSORY))
				{
					player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
					return;
				}
			}
		}
		if(appearanceStone.getType() !=ShapeType.RESTORE && appearanceStone.getVisual(targetItem.getExType())==null)
		{
			player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
			return;
		}

		if(Config.APPEARANCE_STONE_CHECK_ARMOR_TYPE && appearanceStone.getType() == ShapeType.FIXED && appearanceStone.getVisual().size() > 0)
		{
			if(targetItem.isArmor() && (targetItem.getBodyPart() == ItemTemplate.SLOT_CHEST || targetItem.getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR || targetItem.getBodyPart() == ItemTemplate.SLOT_LEGS))
			{
				ItemTemplate extracItemTemplate = ItemHolder.getInstance().getTemplate(appearanceStone.getVisual(player, ExItemType.FULL_BODY));//TODO ??
				if(extracItemTemplate != null && extracItemTemplate.isArmor() && (extracItemTemplate.getBodyPart() == ItemTemplate.SLOT_CHEST || extracItemTemplate.getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR || extracItemTemplate.getBodyPart() == ItemTemplate.SLOT_LEGS))
				{
					if(targetItem.getTemplate().getItemType() != extracItemTemplate.getItemType())
					{
						player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
						return;
					}
				}
			}
		}

		// Запрет на обработку чужих вещей, баг может вылезти на серверных лагах
		if(targetItem.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExShape_Shifting_Result.FAIL);
			player.setAppearanceStone(null);
			return;
		}

		player.sendPacket(new ExPut_Shape_Shifting_Target_Item_Result(ExPut_Shape_Shifting_Target_Item_Result.SUCCESS_RESULT, appearanceStone.getCost()));
	}
}