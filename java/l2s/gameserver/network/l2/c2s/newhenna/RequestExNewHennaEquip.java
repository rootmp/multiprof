package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.commons.network.PacketReader;
import l2s.dataparser.data.common.ItemRequiredId;
import l2s.dataparser.data.holder.DyeDataHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.ActionFailPacket;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.newhenna.NewHennaEquip;
import l2s.gameserver.templates.item.henna.Henna;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExNewHennaEquip implements IClientIncomingPacket
{
	private int _slotId;
	private int _symbolId;
	private int _nCostItemId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slotId = packet.readC();
		_symbolId = packet.readD();
		_nCostItemId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		if(player.getHennaEmptySlots() == 0)
		{
			player.sendPacket(new SystemMessage(SystemMessage.THE_SYMBOL_CANNOT_BE_DRAWN));
			client.sendPacket(ActionFailPacket.STATIC);
			return;
		}

		ItemInstance item = player.getInventory().getItemByObjectId(_symbolId);
		if(item == null)
		{
			player.sendPacket(ActionFailPacket.STATIC);
			player.sendPacket(new NewHennaEquip(_slotId, 0, false));
			return;
		}

		final Henna henna = DyeDataHolder.getInstance().getHennaByItemId(item.getItemId());
		if(henna == null)
		{
			client.sendPacket(ActionFailPacket.STATIC);
			player.sendPacket(new SystemMessage(SystemMessage.THE_SYMBOL_CANNOT_BE_DRAWN));
			return;
		}

		final long count = player.getInventory().getInventoryItemCount(henna.getDyeItemId(), -1, false);

		ItemRequiredId _wearFee = henna.getWearFee(_nCostItemId);

		if(henna.isAllowedClass(player) && count >= henna.getNeedCount() && _wearFee != null
				&& ItemFunctions.haveItem(player, _wearFee.itemName, _wearFee.count) && player.addHenna(_slotId, henna, false))
		{
			ItemFunctions.deleteItem(player, henna.getDyeItemId(), henna.getNeedCount());
			ItemFunctions.deleteItem(player, _wearFee.itemName, _wearFee.count);

			/*final InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(player.getInventory().getAdenaInstance());
			player.sendInventoryUpdate(iu);*/
			player.sendPacket(new NewHennaEquip(_slotId, henna.getDyeId(), true));
			//player.sendPacket(new SystemMessage(SystemMessage.THE_SYMBOL_HAS_BEEN_ADDED));
			player.updateStatBonus();
			player.applyDyePotenSkills();
		}
		else
		{
			player.sendPacket(new SystemMessage(SystemMessage.THE_SYMBOL_CANNOT_BE_DRAWN));
			/*	if (!player.canOverrideCond(PlayerCondOverride.ITEM_CONDITIONS) && !henna.isAllowedClass(player))
				{
					Util.handleIllegalPlayerAction(player, "Exploit attempt: Character " + player.getName() + " of account " + player.getAccountName() + " tryed to add a forbidden henna.", Config.DEFAULT_PUNISH);
				}*/
			client.sendPacket(ActionFailPacket.STATIC);
		}
	}

}
