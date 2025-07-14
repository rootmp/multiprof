package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;

public interface OnPlayerRandomCraft extends PlayerListener
{
	public void randomCraftSuccessful(Player player, int itemId, long itemCount);
}
