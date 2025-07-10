package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.data.xml.holder.EnsoulHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.ExEnsoulResult;
import l2s.gameserver.network.l2.s2c.InventoryUpdatePacket;
import l2s.gameserver.templates.item.support.Ensoul;
import l2s.gameserver.templates.item.support.EnsoulFee;
import l2s.gameserver.templates.item.support.EnsoulFee.EnsoulFeeInfo;
import l2s.gameserver.templates.item.support.EnsoulFee.EnsoulFeeItem;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author Bonux
 **/
public class RequestItemEnsoul implements IClientIncomingPacket
{
	private static class EnsoulInfo
	{
		public int type;
		public int id;
		public int itemObjectId;
		public int ensoulId;
	}

	private int _itemObjectId;
	private List<EnsoulInfo> _ensoulsInfo;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_itemObjectId = packet.readD();
		int changesCount = packet.readC();
		_ensoulsInfo = new ArrayList<EnsoulInfo>(changesCount);
		for (int i = 0; i < changesCount; i++)
		{
			EnsoulInfo info = new EnsoulInfo();
			info.type = packet.readC();
			info.id = packet.readC();
			info.itemObjectId = packet.readD();
			info.ensoulId = packet.readD();
			_ensoulsInfo.add(info);
		}
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.isActionsDisabled())
		{
			activeChar.sendPacket(ExEnsoulResult.FAIL);
			return;
		}

		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(ExEnsoulResult.FAIL);
			return;
		}

		if (activeChar.isInTrade())
		{
			activeChar.sendPacket(ExEnsoulResult.FAIL);
			return;
		}

		activeChar.getInventory().writeLock();
		try
		{
			ItemInstance targetItem = activeChar.getInventory().getItemByObjectId(_itemObjectId);
			if (targetItem == null)
			{
				activeChar.sendPacket(ExEnsoulResult.FAIL);
				return;
			}

			EnsoulFee ensoulFee = EnsoulHolder.getInstance().getEnsoulFee(targetItem.getGrade());

			boolean success = false;
			loop: for (EnsoulInfo info : _ensoulsInfo)
			{
				ItemInstance ensoulItem = activeChar.getInventory().getItemByObjectId(info.itemObjectId);
				if (ensoulItem == null)
					continue;

				Ensoul ensoul = EnsoulHolder.getInstance().getEnsoul(info.ensoulId);
				if (ensoul == null)
					continue;

				if (ensoul.getItemId() != ensoulItem.getItemId())
					continue;

				if (!targetItem.canBeEnsoul(ensoul.getItemId()))
					continue;

				if (ensoulFee != null)
				{
					EnsoulFeeInfo feeInfo = ensoulFee.getFeeInfo(info.type, info.id);
					if (feeInfo != null)
					{
						List<EnsoulFeeItem> feeItems;
						if (!targetItem.containsEnsoul(info.type, info.id))
							feeItems = feeInfo.getInsertFee();
						else
							feeItems = feeInfo.getChangeFee();

						for (EnsoulFeeItem feeItem : feeItems)
						{
							if (feeItem.getLevel() == ensoul.getLevel())
							{
								if (!ItemFunctions.haveItem(activeChar, feeItem.getId(), feeItem.getCount()))
									continue loop;
							}
						}

						for (EnsoulFeeItem feeItem : feeItems)
						{
							if (feeItem.getLevel() == ensoul.getLevel())
							{
								ItemFunctions.deleteItem(activeChar, feeItem.getId(), feeItem.getCount());
							}
						}
					}
				}

				if (!ItemFunctions.deleteItem(activeChar, ensoulItem, 1))
					continue;

				targetItem.addEnsoul(info.type, info.id, ensoul, true);
				success = true;
			}

			activeChar.getInventory().refreshEquip(targetItem);

			if (success)
			{
				activeChar.sendPacket(new InventoryUpdatePacket().addModifiedItem(activeChar, targetItem));
				activeChar.sendPacket(new ExEnsoulResult(targetItem.getNormalEnsouls(), targetItem.getSpecialEnsouls()));
			}
			else
				activeChar.sendPacket(ExEnsoulResult.FAIL);
		}
		finally
		{
			activeChar.getInventory().writeUnlock();
		}
	}
}