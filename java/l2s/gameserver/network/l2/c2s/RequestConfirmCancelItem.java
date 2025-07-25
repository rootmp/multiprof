package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExPutItemResultForVariationCancel;

public class RequestConfirmCancelItem implements IClientIncomingPacket
{
	// format: (ch)d
	int _itemId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_itemId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(!Config.ALLOW_AUGMENTATION)
		{
			activeChar.sendActionFailed();
			return;
		}

		ItemInstance item = activeChar.getInventory().getItemByObjectId(_itemId);

		if(item == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		if(!item.isAugmented())
		{
			activeChar.sendPacket(SystemMsg.AUGMENTATION_REMOVAL_CAN_ONLY_BE_DONE_ON_AN_AUGMENTED_ITEM);
			return;
		}

		activeChar.sendPacket(new ExPutItemResultForVariationCancel(item));
	}
}