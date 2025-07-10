package handler.items;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

public class Cocktails extends SimpleItemHandler
{
	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		int itemId = item.getItemId();

		if (!reduceItem(player, item))
			return false;

		sendUseMessage(player, item);

		switch (itemId)
		{
			case 91641: // Sayha's Blessing
				if (((player.getSayhasGrace() + 35000) > Integer.MAX_VALUE) || ((player.getMagicLampPoints() + 100000) > Integer.MAX_VALUE) || ((player.getCraftGaugePoints() + 6500) >= 99000000))
					return false;
				player.setSayhasGrace(player.getSayhasGrace() + 35000);
				player.setMagicLampPoints(player.getMagicLampPoints() + 100000);
				player.setCraftGaugePoints(player.getCraftGaugePoints() + 6500, null);
				break;
			case 49845: // Sayha's Blessing (Exchangeable)
			case 91910: // Sayha's Blessing Lv. 1-75
			case 92397: // Sayha's Blessing Lv. 1-75 (Event)
			case 92398: // Sayha's Blessing (Event)
				if (((player.getSayhasGrace() + 35000) > player.MAX_SAYHAS_GRACE_POINTS) || ((player.getMagicLampPoints() + 50000) > Integer.MAX_VALUE) || ((player.getCraftGaugePoints() + 3250) >= 99000000))
					return false;
				player.setSayhasGrace(player.getSayhasGrace() + 35000);
				player.setMagicLampPoints(player.getMagicLampPoints() + 100000);
				player.setCraftGaugePoints(player.getCraftGaugePoints() + 6500, null);
				break;
			default:
				return false;
		}

		return true;
	}
}