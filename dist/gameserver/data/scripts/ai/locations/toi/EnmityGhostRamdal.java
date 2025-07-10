package ai.locations.toi;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.NpcUtils;

import ai.BossBarrier;

/**
 * @author nexvill
 */
public class EnmityGhostRamdal extends BossBarrier
{
	private static final int[] BOSSES =
	{
		25220, // Death Lord Hallate
		25447, // Immortal Savior Mardil
		25092, // Korim
		25054, // Kernon
		25126, // Longhorn Golkonda
		25143, // Fire of Wrath Shuriel
		25450 // Cherub Galaxia
	};

	protected final List<NpcInstance> _bosses = new ArrayList<NpcInstance>();

	private static final Location[] LOCS =
	{
		new Location(114635, 18179, -2896), // Death Lord Hallate
		new Location(114660, 13914, 64), // Immortal Savior Mardil
		new Location(115968, 16003, 1944), // Korim
		new Location(113489, 16586, 3960), // Kernon
		new Location(113058, 15603, 5984), // Longhorn Golkonda
		new Location(114243, 14482, 7992), // Fire of Wrath Shuriel
		new Location(113430, 14836, 9560) // Cherub Galaxia
	};

	public EnmityGhostRamdal(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		for (NpcInstance boss : _bosses)
		{
			boss.deleteMe();
		}
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);

		for (int i = 0; i < BOSSES.length; i++)
		{
			if (Rnd.get(100) < 30)
			{
				_bosses.add(NpcUtils.spawnSingle(BOSSES[i], LOCS[i]));
			}
		}
	}
}
