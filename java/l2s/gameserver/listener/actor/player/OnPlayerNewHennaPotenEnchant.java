package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;

public interface OnPlayerNewHennaPotenEnchant extends PlayerListener
{
	public void onPotenEnchant(Player player, int slot, int old_level, int enchantLevel);
}
