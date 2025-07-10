package handler.dailymissions;

import l2s.gameserver.listener.CharListener;
import l2s.gameserver.listener.actor.player.OnPurgeRewardListener;
import l2s.gameserver.model.Player;

/**
 * @author nexvill
 */
public class PurgeReward extends ProgressDailyMissionHandler
{
	private class HandlerListeners implements OnPurgeRewardListener
	{
		@Override
		public void onPurgeReward(Player player, int zoneId)
		{
			progressMission(player, 1, true, -1);
		}

	}

	private final HandlerListeners _handlerListeners = new HandlerListeners();

	@Override
	public CharListener getListener()
	{
		return _handlerListeners;
	}
}
