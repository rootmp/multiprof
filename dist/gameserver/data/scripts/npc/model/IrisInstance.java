package npc.model;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.ReflectionUtils;

import instances.KingNebula;
import instances.KingNebulaExtreme;

/**
 * @author Bonux
 **/
public class IrisInstance extends NpcInstance
{
	private static final Location TELEPORT_LOC = new Location(79691, 256144, -9328);
	private static final int INSTANTZONE_ID = 196;
	private static final int INSTANTZONE_ID2 = 202;

	public IrisInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	public void onMenuSelect(Player player, int ask, long reply, int state)
	{
		if (ask == -2017072601)
		{
			if (reply == 1)
			{
				player.teleToLocation(TELEPORT_LOC, ReflectionManager.MAIN);
			}
			else if (reply == 2)
			{
				showChatWindow(player, "default/" + getNpcId() + "-nebula.htm", false);
			}
		}
		else if (ask == -2017072602)
		{
			// showChatWindow(player, "default/" + getNpcId() + "-no_solo_nebula.htm",
			// false);

			if (reply == 1)
			{
				Reflection r = player.getActiveReflection();
				if (r != null)
				{
					if (player.canReenterInstance(INSTANTZONE_ID))
						player.teleToLocation(r.getTeleportLoc(), r);
					return;
				}
				if (player.canEnterInstance(INSTANTZONE_ID))
				{
					ReflectionUtils.enterReflection(player, new KingNebula(), INSTANTZONE_ID);
					return;
				}
			}
			if (reply == 2)
			{
				Reflection r = player.getActiveReflection();
				if (r != null)
				{
					if (player.canReenterInstance(INSTANTZONE_ID2))
						player.teleToLocation(r.getTeleportLoc(), r);
					return;
				}
				if (player.canEnterInstance(INSTANTZONE_ID2))
				{
					ReflectionUtils.enterReflection(player, new KingNebulaExtreme(), INSTANTZONE_ID2);
					return;
				}
			}
		}
		else
			super.onMenuSelect(player, ask, reply, state);
	}
}
