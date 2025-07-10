package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.model.items.ItemInstance;

/**
 * Written by Berezkin Nikolay, on 20.02.2021
 */
public class ExPetEquipItem extends L2GameClientPacket
{
	private int itemObjId;

	@Override
	protected boolean readImpl() throws Exception
	{
		itemObjId = readD();
		return true;
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player activeChar = getClient().getActiveChar();
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
