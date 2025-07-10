package handler.items;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

public class BlackCoupon extends SimpleItemHandler
{
	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		return player.getEnchantBrokenItemList().showRestoreWindow(item.getItemId());
	}
}