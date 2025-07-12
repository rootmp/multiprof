package l2s.gameserver.network.l2.c2s;
import java.util.List;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.EnsoulHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExEnSoulExtractionResult;
import l2s.gameserver.network.l2.s2c.InventoryUpdatePacket;
import l2s.gameserver.templates.item.support.Ensoul;
import l2s.gameserver.templates.item.support.EnsoulFee;
import l2s.gameserver.templates.item.support.EnsoulFee.EnsoulFeeInfo;
import l2s.gameserver.templates.item.support.EnsoulFee.EnsoulFeeItem;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author Bonux
 **/
public class RequestTryEnSoulExtraction implements IClientIncomingPacket
{
	private int _itemObjectId;
	private int _ensoulType;
	private int _ensoulId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_itemObjectId = packet.readD();
		_ensoulType = packet.readC();
		_ensoulId = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (NpcUtils.canPassPacket(activeChar, this) == null)
		{
			activeChar.sendPacket(ExEnSoulExtractionResult.FAIL);
			return;
		}

		if (activeChar.isActionsDisabled())
		{
			activeChar.sendPacket(ExEnSoulExtractionResult.FAIL);
			return;
		}

		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(ExEnSoulExtractionResult.FAIL);
			return;
		}

		if (activeChar.isInTrade())
		{
			activeChar.sendPacket(ExEnSoulExtractionResult.FAIL);
			return;
		}

		activeChar.getInventory().writeLock();
		try
		{
			ItemInstance targetItem = activeChar.getInventory().getItemByObjectId(_itemObjectId);
			if (targetItem == null)
			{
				activeChar.sendPacket(ExEnSoulExtractionResult.FAIL);
				return;
			}

			Ensoul ensoul = targetItem.getEnsoul(_ensoulType, _ensoulId);
			if (ensoul == null)
			{
				activeChar.sendPacket(ExEnSoulExtractionResult.FAIL);
				return;
			}

			int extractionItemId = ensoul.getExtractionItemId();
			if (extractionItemId <= 0)
			{
				activeChar.sendPacket(ExEnSoulExtractionResult.FAIL);
				return;
			}

			EnsoulFee ensoulFee = EnsoulHolder.getInstance().getEnsoulFee(targetItem.getGrade());
			if (ensoulFee == null)
			{
				activeChar.sendPacket(ExEnSoulExtractionResult.FAIL);
				return;
			}

			EnsoulFeeInfo feeInfo = ensoulFee.getFeeInfo(_ensoulType, _ensoulId);
			if (feeInfo == null)
			{
				activeChar.sendPacket(ExEnSoulExtractionResult.FAIL);
				return;
			}

			List<EnsoulFeeItem> feeItems = feeInfo.getRemoveFee();
			for (EnsoulFeeItem feeItem : feeItems)
			{
				if (feeItem.getLevel() == ensoul.getLevel())
				{
					if (!ItemFunctions.haveItem(activeChar, feeItem.getId(), feeItem.getCount()))
					{
						activeChar.sendPacket(ExEnSoulExtractionResult.FAIL);
						return;
					}
				}
			}

			for (EnsoulFeeItem feeItem : feeItems)
			{
				if (feeItem.getLevel() == ensoul.getLevel())
				{
					ItemFunctions.deleteItem(activeChar, feeItem.getId(), feeItem.getCount());
				}
			}

			targetItem.removeEnsoul(_ensoulType, _ensoulId, true);

			activeChar.getInventory().refreshEquip(targetItem);

			ItemFunctions.addItem(activeChar, extractionItemId, 1);

			activeChar.sendPacket(new InventoryUpdatePacket(activeChar).addModifiedItem(targetItem));
			activeChar.sendPacket(new ExEnSoulExtractionResult(targetItem.getNormalEnsouls(), targetItem.getSpecialEnsouls()));
		}
		finally
		{
			activeChar.getInventory().writeUnlock();
		}
	}
}