package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExPutItemResultForVariationMake;

public class RequestConfirmTargetItem implements IClientIncomingPacket
{
	// format: (ch)d
	private int _itemObjId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_itemObjId = packet.readD(); // object_id шмотки
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(!Config.ALLOW_AUGMENTATION)
		{
			activeChar.sendActionFailed();
			return;
		}

		ItemInstance item = activeChar.getInventory().getItemByObjectId(_itemObjId);

		if(item == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		// check if the item is augmentable
		if(item.isAugmented())
		{
			activeChar.sendPacket(SystemMsg.ONCE_AN_ITEM_IS_AUGMENTED_IT_CANNOT_BE_AUGMENTED_AGAIN);
			return;
		}
		// TODO: can do better? : currently: using isdestroyable() as a check for hero /
		// cursed weapons
		else if(!item.canBeAugmented(activeChar))
		{
			activeChar.sendPacket(SystemMsg.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}

		// check if the player can augment
		if(activeChar.isInStoreMode())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_AUGMENT_ITEMS_WHILE_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP_IS_IN_OPERATION);
			return;
		}
		if(activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}
		if(activeChar.isDead())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_AUGMENT_ITEMS_WHILE_DEAD);
			return;
		}
		if(activeChar.isParalyzed())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_AUGMENT_ITEMS_WHILE_PARALYZED);
			return;
		}
		if(activeChar.isFishing())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_AUGMENT_ITEMS_WHILE_FISHING);
			return;
		}
		if(activeChar.isInTrainingCamp())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_TAKE_OTHER_ACTION_WHILE_ENTERING_THE_TRAINING_CAMP);
			return;
		}
		if(activeChar.isSitting())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_AUGMENT_ITEMS_WHILE_SITTING_DOWN);
			return;
		}
		if(activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}
		activeChar.sendPacket(new ExPutItemResultForVariationMake(_itemObjId), SystemMsg.SELECT_THE_CATALYST_FOR_AUGMENTATION);
	}
}