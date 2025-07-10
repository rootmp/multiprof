package l2s.gameserver.listener.zone.impl;

import java.util.concurrent.ScheduledFuture;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import l2s.commons.lang.reference.HardReference;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.Zone.ZoneType;
import l2s.gameserver.network.l2.s2c.ExAutoFishAvailable;

/**
 * @author Bonux
 */
public class FishingZoneListener implements OnZoneEnterLeaveListener
{
	public static final OnZoneEnterLeaveListener STATIC = new FishingZoneListener();
	private final TIntObjectMap<ScheduledFuture<?>> _notifyTasks = new TIntObjectHashMap<ScheduledFuture<?>>();

	private class NotifyPacketTask implements Runnable
	{
		private final Zone _zone;
		private final int _objectId;
		private final HardReference<Player> _playerRef;

		public NotifyPacketTask(Zone zone, Player player)
		{
			_zone = zone;
			_objectId = player.getObjectId();
			_playerRef = player.getRef();
		}

		@Override
		public void run()
		{
			Player player = _playerRef.get();
			if (player == null)
			{
				stopAndRemoveNotifyTask(_objectId);
				return;
			}

			if (!player.isInZone(ZoneType.FISHING))
			{
				player.sendPacket(ExAutoFishAvailable.REMOVE);
				stopAndRemoveNotifyTask(player.getObjectId());
				return;
			}

			if (player.isFishing())
			{
				player.sendPacket(ExAutoFishAvailable.FISHING);
			}
			else
			{
				player.sendPacket(ExAutoFishAvailable.SHOW);
			}
		}
	}

	@Override
	public void onZoneEnter(Zone zone, Creature actor)
	{
		if (!actor.isPlayer())
		{
			return;
		}

		if (_notifyTasks.containsKey(actor.getObjectId()))
		{
			return;
		}

		_notifyTasks.put(actor.getObjectId(), ThreadPoolManager.getInstance().scheduleAtFixedRate(new NotifyPacketTask(zone, actor.getPlayer()), 0L, 5000L));
	}

	@Override
	public void onZoneLeave(Zone zone, Creature actor)
	{
		if (!actor.isPlayer())
		{
			return;
		}

		// На оффе при выходе из зоны не посылается, посылается во время таска, что не
		// сильно красиво.
		actor.sendPacket(ExAutoFishAvailable.REMOVE);
		stopAndRemoveNotifyTask(actor.getObjectId());
	}

	private void stopAndRemoveNotifyTask(int objectId)
	{
		ScheduledFuture<?> notifyTask = _notifyTasks.remove(objectId);
		if (notifyTask != null)
		{
			notifyTask.cancel(false);
		}
	}
}
