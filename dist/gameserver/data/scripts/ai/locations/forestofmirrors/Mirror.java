package ai.locations.forestofmirrors;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author Bonux
 **/
public class Mirror extends Fighter
{
	// Monster ID's
	private static final int MIRROR_NPC_ID = 20639; // Зекрало

	public Mirror(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		final NpcInstance actor = getActor();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(() ->
		{
			for (int i = 0; i < 4; i++)
			{
				final NpcInstance npc = NpcUtils.spawnSingle(MIRROR_NPC_ID, Rnd.chance(75) ? new Location(actor.getLoc().getX(), actor.getLoc().getY(), (actor.getLoc().getZ() - 3000)) : actor.getLoc());
				if (npc.getAI() instanceof Mirror)
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, killer, 200);
				}
			}
		}, 1000L, 3000);
	}
}
