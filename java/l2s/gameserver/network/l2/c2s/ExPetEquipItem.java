package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.model.items.ItemInstance;

/**
 * Written by Berezkin Nikolay, on 20.02.2021
 */
public class ExPetEquipItem implements IClientIncomingPacket
{
	private int itemObjId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		itemObjId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		PetInstance pet = activeChar.getPet();

		if (pet == null)
			return;

		activeChar.setActive();

		ItemInstance item = activeChar.getInventory().getItemByObjectId(itemObjId);
		if (item == null)
		{
			activeChar.sendActionFailed();
			return;
		}

		if (item.isEquipped() || !item.isEquipable())
		{
			activeChar.sendActionFailed();
			return;
		}

		pet.useItem(item, false, true);
	}
}
