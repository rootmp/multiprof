package l2s.gameserver.listener.actor.player;

import l2s.gameserver.listener.PlayerListener;
import l2s.gameserver.model.Player;

/**
 * @author nexvill
 */
public interface OnPurgeRewardListener extends PlayerListener
{
	public void onPurgeReward(Player player, int zoneId);
}
