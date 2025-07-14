package l2s.gameserver.model.actor.instances.player.tasks;

import l2s.commons.lang.reference.HardReference;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;

/**
 * @author VISTALL
 * @date 17:29/03.10.2011
 */
public class EnableUserRelationTask implements Runnable
{
	private HardReference<Player> _playerRef;
	private SiegeEvent<?, ?> _siegeEvent;

	public EnableUserRelationTask(Player player, SiegeEvent<?, ?> siegeEvent)
	{
		_siegeEvent = siegeEvent;
		_playerRef = player.getRef();
	}

	@Override
	public void run()
	{
		Player player = _playerRef.get();
		if(player == null)
			return;

		// _siegeEvent.removeBlockFame(player);

		player.stopEnableUserRelationTask();
		player.broadcastUserInfo(true);
	}
}
