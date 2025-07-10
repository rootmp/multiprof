package ai.locations.toi.heavenlyrift;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.NpcAI;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.NpcUtils;

import manager.HeavenlyRift;

/**
 * @reworked by Bonux, this mob isn't supposed to do anything
 **/
public class Bomb extends NpcAI
{
	private static int[] ITEM_DROP_1 =
	{
		49756,
		49762,
		49763
	};
	private static int[] ITEM_DROP_2 =
	{
		49760,
		49761
	};

	public Bomb(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		getActor().setNpcState(1);
		super.onEvtSpawn();
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		if (Rnd.chance(50))
		{
			for (Playable playable : World.getAroundPlayables(getActor(), 200, 150))
			{
				if (playable == null || playable.isDead())
					continue;
				playable.reduceCurrentHp(Rnd.get(300, 400), getActor(), null, true, true, false, false, false, false, true);
			}
		}
		if (Rnd.chance(33))
		{
			NpcUtils.spawnSingle(20139, getActor().getSpawnedLoc(), 1800000L);
		}
		else
		{
			if (Rnd.chance(90))
				getActor().dropItem(killer.getPlayer(), ITEM_DROP_1[Rnd.get(ITEM_DROP_1.length)], 1);
			else
				getActor().dropItem(killer.getPlayer(), ITEM_DROP_2[Rnd.get(ITEM_DROP_2.length)], 1);
		}

		if (HeavenlyRift.getInstance().getAliveNpcCount(getActor().getNpcId()) == 0)// Last
		{
			ServerVariables.set("heavenly_rift_complete", ServerVariables.getInt("heavenly_rift_level", 0));
			ServerVariables.set("heavenly_rift_level", 0);
		}
		super.onEvtDead(killer);
	}
}
