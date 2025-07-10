package npc.model;

import java.util.concurrent.Future;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.NpcUtils;

public class NahirInstance extends MonsterInstance
{
	private Future<?> resetTask = null;

	public NahirInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		if (resetTask == null)
		{
			resetTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Task(), 1000L, Rnd.get(1, 10) * 1000);
		}
		NpcUtils.spawnSingle(27144, new Location(176648, -185096, -3726));
		NpcUtils.spawnSingle(getNpcId(), getLoc());
		decayMe();
	}

	public static class Task implements Runnable
	{
		public Task()
		{
		}

		@Override
		public void run()
		{
			for (int i = 0; i < Rnd.get(1, 10); i++)
			{
				NpcUtils.spawnSingle(20117, new Location(91240, -218344, -8877));
				for (final NpcInstance obj : GameObjectsStorage.getNpcs())
				{
					if ((obj == null) || obj.isAlikeDead())
					{
						continue;
					}
					for (final NpcInstance around : World.getAroundNpc(obj))
					{
						if (around == null)
						{
							continue;
						}
					}
				}
			}
		}
	}
}