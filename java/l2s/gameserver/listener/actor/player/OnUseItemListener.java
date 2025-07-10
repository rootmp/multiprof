package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

/**
 * @author nexvill
 **/
public interface OnUseItemListener extends PlayerListener
{
	public void onUseItem(Player player, ItemInstance item, boolean success);
}
