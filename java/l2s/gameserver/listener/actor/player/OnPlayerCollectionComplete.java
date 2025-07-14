package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;

public interface OnPlayerCollectionComplete extends PlayerListener
{
	public void onCollectionComplete(Player actor, int collectionId);
}
