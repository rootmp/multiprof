package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.NpcAI;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;

/**
 * @author Bonux
 **/
public class Toma extends NpcAI
{
	private static final long TELEPORT_DELAY = 1800; // 30 минут
	private final Location[] TELEPORT_POINTS =
	{
		new Location(151680, -174891, 63729, 41400),
		new Location(154153, -220105, 62134, 0),
		new Location(178834, -184336, 65184, 0)
	};

	private long _lastTeleport = System.currentTimeMillis();

	public Toma(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		if ((System.currentTimeMillis() - _lastTeleport) < TELEPORT_DELAY)
			return false;

		final NpcInstance actor = getActor();
		final Location loc = Rnd.get(TELEPORT_POINTS);

		if (actor.isInRange(loc, 50))
			return false;

		_lastTeleport = System.currentTimeMillis();

		actor.broadcastPacket(new MagicSkillUse(actor, 4671, 1, 1000,0, 0L));
		ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> actor.teleToLocation(loc), 10L, 1000L);
		return true;
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}
