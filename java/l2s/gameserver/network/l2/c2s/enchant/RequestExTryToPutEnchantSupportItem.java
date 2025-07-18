package l2s.gameserver.network.l2.c2s.enchant;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.PcInventory;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.ExPutEnchantSupportItemResult;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExTryToPutEnchantSupportItem implements IClientIncomingPacket
{
	private int _itemId;
	private int _catalystId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_catalystId = packet.readD();
		_itemId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		PcInventory inventory = activeChar.getInventory();
		ItemInstance itemToEnchant = inventory.getItemByObjectId(_itemId);
		ItemInstance catalyst = inventory.getItemByObjectId(_catalystId);

		if(ItemFunctions.getEnchantStone(itemToEnchant, catalyst) != null)
			activeChar.sendPacket(new ExPutEnchantSupportItemResult(1));
		else
			activeChar.sendPacket(new ExPutEnchantSupportItemResult(0));
	}
}