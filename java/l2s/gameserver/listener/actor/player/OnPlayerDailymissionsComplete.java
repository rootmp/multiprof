package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;

public interface OnPlayerDailymissionsComplete extends PlayerListener
{
	public void onDailymissionsComplete(Player actor, int dailymissionsId);
}
