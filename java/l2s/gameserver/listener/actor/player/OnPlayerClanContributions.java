package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;

public interface OnPlayerClanContributions extends PlayerListener
{
	public void onClanContributions(Player player, int type);
}
