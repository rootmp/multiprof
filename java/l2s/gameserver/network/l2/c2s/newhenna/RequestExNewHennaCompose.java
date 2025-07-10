package l2s.gameserver.network.l2.c2s.newhenna;

import l2s.commons.util.Rnd;
import l2s.gameserver.data.xml.holder.HennaHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Henna;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.newhenna.ExNewHennaCompose;
import l2s.gameserver.templates.henna.DyeCombintation;
import l2s.gameserver.templates.henna.HennaTemplate;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExNewHennaCompose extends L2GameClientPacket
{
	private int nSlotOneIndex;
	private int nSlotOneItemID;
	private int nSlotTwoItemID;

	@Override
	protected boolean readImpl()
	{
		nSlotOneIndex = readD();
		nSlotOneItemID = readD();
		nSlotTwoItemID = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		if (activeChar.isActionsDisabled())
		{ // TODO: Check.
			activeChar.sendPacket(new ExNewHennaCompose(0, -1, false));
			return;
		}

		if (activeChar.isInStoreMode())
		{ // TODO: Check.
			activeChar.sendPacket(new ExNewHennaCompose(0, -1, false));
			return;
		}

		if (activeChar.isInTrade())
		{ // TODO: Check.
			activeChar.sendPacket(new ExNewHennaCompose(0, -1, false));
			return;
		}

		if (activeChar.isFishing())
		{ // TODO: Check.
			activeChar.sendPacket(new ExNewHennaCompose(0, -1, false));
			return;
		}

		if (activeChar.isInTrainingCamp())
		{ // TODO: Check.
			activeChar.sendPacket(new ExNewHennaCompose(0, -1, false));
			return;
		}

		Henna henna = activeChar.getHennaList().get(nSlotOneIndex);
		if (henna == null)
		{
			activeChar.sendPacket(new ExNewHennaCompose(0, -1, false));
			return;
		}

		HennaTemplate template = henna.getTemplate();
		if (template == null)
		{
			activeChar.sendPacket(new ExNewHennaCompose(0, -1, false));
			return;
		}

		activeChar.getInventory().writeLock();
		try
		{
			ItemInstance item1 = null;
			if (nSlotOneItemID != -1)
			{
				item1 = activeChar.getInventory().getItemByObjectId(nSlotOneItemID);
				if (item1 == null)
				{
					activeChar.sendPacket(new ExNewHennaCompose(0, -1, false));
					return;
				}
			}

			ItemInstance item2 = activeChar.getInventory().getItemByObjectId(nSlotTwoItemID);
			if (item2 == null)
			{
				activeChar.sendPacket(new ExNewHennaCompose(0, -1, false));
				return;
			}

			DyeCombintation combination = null;
			for (DyeCombintation c : HennaHolder.getInstance().getCombinations())
			{
				if (item1 == null)
				{
					if (c.getSlotOne() != template.getDyeId())
						continue;
				}
				else if (c.getSlotOne() != item1.getItemId())
					continue;

				if (c.getSlotTwo() != item2.getItemId())
					continue;

				combination = c;
				break;
			}

			if (combination == null)
			{
				activeChar.sendPacket(new ExNewHennaCompose(0, -1, false));
				return;
			}

			HennaTemplate newTemplate = HennaHolder.getInstance().getHenna(combination.getResultDyeId());
			if (newTemplate == null)
			{
				activeChar.sendPacket(new ExNewHennaCompose(0, -1, false));
				return;
			}

			if (item1 != null)
				ItemFunctions.deleteItem(activeChar, item1, 1, true);
			ItemFunctions.deleteItem(activeChar, item2, 1, true);
			ItemFunctions.deleteItem(activeChar, ItemTemplate.ITEM_ID_ADENA, combination.getAdena(), true);

			if (!Rnd.chance(combination.getChance()))
			{
				activeChar.sendPacket(new ExNewHennaCompose(newTemplate.getSymbolId(), -1, false));
				return;
			}

			henna.setTemplate(newTemplate);
			henna.updated(true);
			activeChar.sendPacket(new ExNewHennaCompose(newTemplate.getSymbolId(), -1, true));
		}
		finally
		{
			activeChar.getInventory().writeUnlock();
		}
	}
}