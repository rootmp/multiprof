package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExChangeAttributeInfo;

public class SendChangeAttributeTargetItem implements IClientIncomingPacket
{
	public int _crystalItemId;
	public int _itemObjId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_crystalItemId = packet.readD(); // Change Attribute Crystall ID
		_itemObjId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		ItemInstance item = activeChar.getInventory().getItemByObjectId(_itemObjId);
		if(item == null || !item.isWeapon())
		{
			activeChar.sendActionFailed();
			return;
		}
		activeChar.sendPacket(new ExChangeAttributeInfo(_crystalItemId, item));
	}
}
