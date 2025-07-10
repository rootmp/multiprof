package l2s.gameserver.network.l2.c2s.enchant;

import l2s.gameserver.data.xml.holder.EnchantItemHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.PcInventory;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExPutEnchantScrollItemResult;
import l2s.gameserver.templates.item.support.EnchantScroll;

/**
 * @author nexvill
 **/
public class RequestExAddEnchantScrollItem extends L2GameClientPacket
{
	private int _scrollObjectId;

	@Override
	protected boolean readImpl()
	{
		_scrollObjectId = readD();
		readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		if (player.isActionsDisabled() || player.isInStoreMode() || player.isInTrade())
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			return;
		}

		PcInventory inventory = player.getInventory();
		ItemInstance scroll = inventory.getItemByObjectId(_scrollObjectId);

		if (scroll == null)
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			return;
		}

		int scrollId = scroll.getItemId();

		EnchantScroll enchantScroll = EnchantItemHolder.getInstance().getEnchantScroll(scrollId);

		if (enchantScroll == null)
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
			return;
		}

		if (player.isInStoreMode())
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			player.sendPacket(SystemMsg.YOU_CANNOT_ENCHANT_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			return;
		}

		if ((scroll = inventory.getItemByObjectId(scroll.getObjectId())) == null)
		{
			player.sendPacket(ExPutEnchantScrollItemResult.FAIL);
			return;
		}

		player.setEnchantScroll(scroll);
		player.sendPacket(new ExPutEnchantScrollItemResult(scroll.getObjectId()));
	}
}