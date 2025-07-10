package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExVariationResult;
import l2s.gameserver.templates.item.support.variation.VariationFee;
import l2s.gameserver.utils.VariationUtils;

public final class RequestRefine extends L2GameClientPacket
{
	// format: (ch)dddd
	private int _targetItemObjId, _refinerItemObjId;
	private long _feeItemCount;

	@Override
	protected boolean readImpl()
	{
		_targetItemObjId = readD();
		_refinerItemObjId = readD();
		readD(); // now 0, QQ
		_feeItemCount = readQ();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null || _feeItemCount < 1)
			return;

		if (!Config.ALLOW_AUGMENTATION)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (activeChar.isActionsDisabled())
		{
			activeChar.sendPacket(new ExVariationResult(0, 0, 0));
			return;
		}

		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(new ExVariationResult(0, 0, 0));
			return;
		}

		if (activeChar.isInTrade())
		{
			activeChar.sendPacket(new ExVariationResult(0, 0, 0));
			return;
		}

		ItemInstance targetItem = activeChar.getInventory().getItemByObjectId(_targetItemObjId);
		ItemInstance refinerItem = activeChar.getInventory().getItemByObjectId(_refinerItemObjId);

		if (refinerItem == null) // block if player press "Continue" button and try again place life stone on
									// same item, that not exist or smth.
		{
			activeChar.sendPacket(new ExVariationResult(0, 0, 0), SystemMsg.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}

		ItemInstance feeItem = null;

		switch (refinerItem.getItemId())
		{
			case 94185: // Life Stone Lv. 1 - Weapon
			case 94186: // Life Stone Lv. 2 - Weapon
			case 94187: // Life Stone Lv. 1 - Armor
			case 94188: // Life Stone Lv. 2 - Armor
			case 94209: // Life Stone Lv. 1 - Weapon (Event)
			case 94210: // Life Stone Lv. 2 - Weapon (Event)
			case 94211: // Life Stone Lv. 1 - Armor (Event)
			case 94212: // Life Stone Lv. 2 - Armor (Event)
			case 94303: // Life Stone - Heroic Circlet
			case 94304: // Life Stone - Heroic Circlet (Event)
			case 94423: // Life Stone - Accessory
			case 94424: // Life Stone - Accessory (Event)
			{
				feeItem = activeChar.getInventory().getItemByItemId(57); // Adena
				break;
			}
		}

		if (targetItem == null || refinerItem == null || feeItem == null)
		{
			activeChar.sendPacket(new ExVariationResult(0, 0, 0), SystemMsg.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}

		VariationFee fee = VariationUtils.getVariationFee(targetItem, refinerItem);
		if (fee == null)
		{
			activeChar.sendPacket(new ExVariationResult(0, 0, 0), SystemMsg.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);
			return;
		}

		activeChar.getInventory().addItem(refinerItem.getItemId(), 1);
		activeChar.getInventory().addItem(feeItem.getItemId(), 1);

		if (VariationUtils.tryAugmentItem(activeChar, targetItem, refinerItem, feeItem, fee.getFeeItemCount()))
			activeChar.sendPacket(new ExVariationResult(targetItem.getVariation1Id(), targetItem.getVariation2Id(), 1), SystemMsg.THE_ITEM_WAS_SUCCESSFULLY_AUGMENTED);
		else
			activeChar.sendPacket(new ExVariationResult(0, 0, 0), SystemMsg.AUGMENTATION_FAILED_DUE_TO_INAPPROPRIATE_CONDITIONS);

		if (!activeChar.getInventory().destroyItem(refinerItem, 1) || !activeChar.getInventory().destroyItem(feeItem, 1))
		{
			VariationUtils.setVariation(activeChar, targetItem, 0, 0, 0);
		}
	}
}