package events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.util.Rnd;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Spawner;
import l2s.gameserver.model.entity.events.objects.SpawnExObject;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author Bonux
 **/
public class TowerOfInsolenceEvent extends LairOfAntharasEvent
{
	public TowerOfInsolenceEvent(MultiValueSet<String> set)
	{
		super(set);
	}

	@Override
	protected void spawnRaidBosses()
	{
		List<SpawnExObject> spawnExObjects = new ArrayList<SpawnExObject>(getObjects(SPAWN_GROUP_OBJECT));
		Collections.shuffle(spawnExObjects);
		int raidCount = Math.min(Rnd.get(3, 4), spawnExObjects.size());
		for (SpawnExObject spawnExObject : spawnExObjects)
		{
			List<Spawner> spawners = new ArrayList<Spawner>(spawnExObject.getSpawns());
			if (spawners.isEmpty())
				continue;

			Collections.shuffle(spawners);

			Spawner spawner = spawners.get(0);
			spawner.deleteAll();

			Location spawnLoc = spawner.getRandomSpawnRange().getRandomLoc(spawner.getReflection().getGeoIndex(), false);
			NpcInstance raidBoss = NpcUtils.spawnSingle(RAID_BOSS_NPC_ID, spawnLoc, spawner.getReflection());

			_spawnedRaidBosses.put(raidBoss, spawner);

			if (_spawnedRaidBosses.size() >= raidCount)
				break;
		}
	}
}