package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;

public interface OnPlayerUpVipLevel extends PlayerListener
{
	public void upVipLevel(Player player, int level_old, int level_new);
}
