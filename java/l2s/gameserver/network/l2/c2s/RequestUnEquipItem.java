package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.model.items.ItemInstance;

/**
 * @author Bonux
 **/
public class RequestUnEquipItem implements IClientIncomingPacket
{
	private long _slot;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slot = packet.readQ();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
		{
			return;
		}

		activeChar.setActive();

		int paperdollIndex = Inventory.getPaperdollIndex(_slot);
		if (paperdollIndex == -1)
		{
			activeChar.sendActionFailed();
			return;
		}

		ItemInstance item = activeChar.getInventory().getPaperdollItem(paperdollIndex);
		if (item == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (!item.isEquipped() || !item.isEquipable())
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.useItem(item, false, true);
	}
}