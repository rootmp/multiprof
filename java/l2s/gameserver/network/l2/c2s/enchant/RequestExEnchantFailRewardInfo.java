package l2s.gameserver.network.l2.c2s.enchant;

import l2s.gameserver.data.xml.holder.EnchantItemHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.enchant.ExResetEnchantItemFailRewardInfo;
import l2s.gameserver.templates.item.support.EnchantScroll;
import l2s.gameserver.templates.item.support.EnchantStone;
import l2s.gameserver.templates.item.support.FailResultType;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author nexvill
 */
public class RequestExEnchantFailRewardInfo implements IClientIncomingPacket
{
	private int _itemObjId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_itemObjId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		if ((player.getEnchantItem() == null) || (player.getEnchantScroll() == null))
		{
			return;
		}

		final EnchantScroll enchantScroll = EnchantItemHolder.getInstance().getEnchantScroll(player.getEnchantScroll().getItemId());
		if (enchantScroll == null)
		{
			return;
		}
		final ItemInstance enchantItem = player.getEnchantItem();
		ItemInstance addedItem = player.getInventory().getItemByObjectId(_itemObjId);
		if (addedItem == null)
		{
			return;
		}
		if (enchantItem.getObjectId() != addedItem.getObjectId())
		{
			return;
		}
		EnchantStone stone = null;
		if (player.getSupportItem() != null)
		{
			stone = ItemFunctions.getEnchantStone(player.getEnchantItem(), player.getSupportItem());
		}
		int crystalId = 0;
		int crystalCount = 0;

		if ((enchantScroll.getResultType() == FailResultType.DROP_ENCHANT) || ((stone != null) && (stone.getResultType() == FailResultType.DROP_ENCHANT)))
		{
			int enchantDropCount = enchantScroll.getEnchantDropCount();
			if (stone != null && stone.getEnchantDropCount() < enchantDropCount)
			{
				enchantDropCount = stone.getEnchantDropCount();
			}
			addedItem.setEnchantLevel(Math.max(addedItem.getEnchantLevel() - enchantDropCount, 0));
		}
		else if (enchantScroll.getResultType() == FailResultType.NOTHING)
		{
			addedItem.setEnchantLevel(enchantItem.getEnchantLevel());
		}
		else
		{
			addedItem = null;
			crystalId = enchantItem.getGrade().getCrystalId();
			crystalCount = enchantItem.getCrystalCountOnEchant();
		}
		if (addedItem != null)
		{
			player.sendPacket(new ExResetEnchantItemFailRewardInfo(enchantItem, crystalId, crystalCount, true));
		}
		else
		{
			player.sendPacket(new ExResetEnchantItemFailRewardInfo(enchantItem, crystalId, crystalCount, false));
		}
	}
}