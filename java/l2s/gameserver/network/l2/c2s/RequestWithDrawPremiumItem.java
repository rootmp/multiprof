package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.PremiumItem;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExGetPremiumItemListPacket;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author nexvill
 */
public final class RequestWithDrawPremiumItem implements IClientIncomingPacket
{
	private int _index;
	private int _charId;
	private long _itemCount;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_index = packet.readD();
		_charId = packet.readD();
		_itemCount = packet.readQ();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;

		if(_itemCount <= 0)
		{
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.getObjectId() != _charId) // audit
		{
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.getPremiumItemList().isEmpty())// audit
		{
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.getWeightPenalty() >= 3 || activeChar.getInventoryLimit() * 0.8 <= activeChar.getInventory().getSize())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_RECEIVE_THE_VITAMIN_ITEM_BECAUSE_YOU_HAVE_EXCEED_YOUR_INVENTORY_WEIGHTQUANTITY_LIMIT);
			return;
		}

		if(activeChar.isProcessingRequest())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_RECEIVE_A_VITAMIN_ITEM_DURING_AN_EXCHANGE);
			return;
		}

		PremiumItem item = activeChar.getPremiumItemList().get(_index);
		if(item == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		if(!activeChar.getPremiumItemList().remove(item, _itemCount))
		{
			activeChar.sendActionFailed();
			return;
		}

		ItemFunctions.addItem(activeChar, item.getItemId(), _itemCount);

		if(activeChar.getPremiumItemList().isEmpty())
			activeChar.sendPacket(SystemMsg.THERE_ARE_NO_MORE_VITAMIN_ITEMS_TO_BE_FOUND);
		else
			activeChar.sendPacket(new ExGetPremiumItemListPacket(activeChar));
	}
}