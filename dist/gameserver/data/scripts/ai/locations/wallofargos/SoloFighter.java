package ai.locations.wallofargos;

import java.util.concurrent.TimeUnit;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author Bonux
 **/
public class SoloFighter extends Fighter
{
	// Monster ID's
	private static final int BUFFALO_NPC_ID = 21774; // Buffalo
	private static final double SPAWN_CHANCE = 10.;
	private static final long DESPAWN_TIME = TimeUnit.MINUTES.toMillis(2);

	public SoloFighter(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		if (Rnd.chance(SPAWN_CHANCE))
		{
			final NpcInstance actor = getActor();
			ThreadPoolManager.getInstance().schedule(() ->
			{
				NpcInstance npc = NpcUtils.spawnSingle(BUFFALO_NPC_ID, actor.getLoc(), DESPAWN_TIME);
			}, 3000L);
		}
	}
}
