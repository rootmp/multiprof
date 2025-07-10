package handler.items;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

/**
 * @author SanyaDC
 */
public class TitleColor extends SimpleItemHandler
{
	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		final String newColor = "0094FF";
		final String bgrNewColor = newColor.substring(4, 6) + newColor.substring(2, 4) + newColor.substring(0, 2);
		final int newColorInt = Integer.decode("0x" + bgrNewColor);
		player.setTitleColor(newColorInt);
		return true;
	}
}