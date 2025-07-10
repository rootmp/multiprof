package zones;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.listener.script.OnInitScriptListener;
import l2s.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Zone;
import l2s.gameserver.utils.ReflectionUtils;

/**
 * @author nexvill
 */
public class HellboundPortal implements OnInitScriptListener
{
	private static final String TELEPORT_ZONE_NAME1 = "[hellbound_portal_1]";
	private static final String TELEPORT_ZONE_NAME2 = "[hellbound_portal_2]";
	private static final String TELEPORT_ZONE_NAME3 = "[hellbound_portal_3]";
	private static final String TELEPORT_ZONE_NAME4 = "[hellbound_portal_4]";

	private static ZoneListener _zoneListener1;
	private static ZoneListener _zoneListener2;
	private static ZoneListener _zoneListener3;
	private static ZoneListener _zoneListener4;

	private static final Location[] LOCS =
	{
		new Location(8168, 251576, -1789),
		new Location(7208, 251544, -1787),
		new Location(6712, 251032, -1797),
		new Location(6776, 250056, -1791),
		new Location(7304, 249544, -1787),
		new Location(8152, 249608, -1785)
	};

	private void init()
	{
		_zoneListener1 = new ZoneListener();
		_zoneListener2 = new ZoneListener();
		_zoneListener3 = new ZoneListener();
		_zoneListener4 = new ZoneListener();
		Zone zone1 = ReflectionUtils.getZone(TELEPORT_ZONE_NAME1);
		Zone zone2 = ReflectionUtils.getZone(TELEPORT_ZONE_NAME2);
		Zone zone3 = ReflectionUtils.getZone(TELEPORT_ZONE_NAME3);
		Zone zone4 = ReflectionUtils.getZone(TELEPORT_ZONE_NAME4);
		zone1.addListener(_zoneListener1);
		zone2.addListener(_zoneListener2);
		zone3.addListener(_zoneListener3);
		zone4.addListener(_zoneListener4);
	}

	@Override
	public void onInit()
	{
		init();
	}

	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (zone == null)
				return;

			if (cha == null)
				return;

			Player player = cha.getPlayer();
			if (player == null)
				return;

			boolean open = ServerVariables.getBool("hellbound_open", false);
			if (!Config.HELLBOUND_ENABLED_ALL_TIME && !open)
			{
				return;
			}

			if (zone.getName().equalsIgnoreCase("[hellbound_portal_1]") || zone.getName().equalsIgnoreCase("[hellbound_portal_2]") || zone.getName().equalsIgnoreCase("[hellbound_portal_3]") || zone.getName().equalsIgnoreCase("[hellbound_portal_4]"))
			{
				int locId = Rnd.get(LOCS.length);
				player.teleToLocation(LOCS[locId]);
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
			//
		}
	}
}