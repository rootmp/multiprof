package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

public interface OnPlayerEquipListener extends PlayerListener
{
	public void onEquipItem(Player player, ItemInstance item);

	public void onUnEquipItem(Player player, ItemInstance item);
}
