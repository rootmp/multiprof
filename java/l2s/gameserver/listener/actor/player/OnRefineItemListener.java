package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

public interface OnRefineItemListener extends PlayerListener
{
	public void onRefineItem(Player player, ItemInstance item);
}
