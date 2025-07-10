package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.NpcUtils;

public class Comanders extends Fighter
{
	public Comanders(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();
		if (Rnd.chance(100))
			spawnC(actor, 1);
		super.onEvtDead(killer);
	}

	private void spawnC(NpcInstance actor, int count)
	{
		int npcId = actor.getNpcId();
		if (npcId == 21752)
			NpcUtils.spawnSingle(21754, actor.getLoc());
		if (npcId == 21753)
			NpcUtils.spawnSingle(21755, actor.getLoc());
	}
}