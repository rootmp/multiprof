package l2s.gameserver.network.l2.c2s.itemrestore;

import l2s.gameserver.dao.ItemsToRestoreDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.itemrestore.ExPenaltyItemInfo;
import l2s.gameserver.network.l2.s2c.itemrestore.ExPenaltyItemList;
import l2s.gameserver.network.l2.s2c.itemrestore.ExPenaltyItemRestore;
import l2s.gameserver.templates.item.ItemTemplate;

public class RequestExPenaltyItemRestore implements IClientIncomingPacket
{
	private int itemObjId;
	private boolean isAdena;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		itemObjId = packet.readD();
		isAdena = packet.readC() == 1 ? true : false;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		ItemInstance item = ItemsToRestoreDAO.getInstance().load(itemObjId);
		if (item == null)
			return;

		int price = item.getTemplate().getReferencePrice() > 0 ? item.getTemplate().getReferencePrice() : 10000;

		if (isAdena)
		{
			if (!activeChar.getInventory().destroyItemByItemId(57, price))
			{
				activeChar.sendPacket(new SystemMessagePacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA));
				return;
			}
			else
				activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1_ADENA_DISAPPEARED).addLong(price));
		}
		else if (!activeChar.getInventory().destroyItemByItemId(ItemTemplate.ITEM_ID_MONEY_L, 5))
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.NOT_ENOUGH_L2_COINS));
			return;
		}
		else
		{
			activeChar.sendPacket(new SystemMessagePacket(SystemMsg.S1_S2_DISAPPEARED).addLong(5).addItemName(ItemTemplate.ITEM_ID_MONEY_L));
		}

		activeChar.getInventory().addItem(item);
		ItemsToRestoreDAO.getInstance().delete(item);

		activeChar.sendPacket(new ExPenaltyItemRestore());
		activeChar.sendPacket(new ExPenaltyItemList(activeChar));
		activeChar.sendPacket(new ExPenaltyItemInfo(activeChar));
	}
}