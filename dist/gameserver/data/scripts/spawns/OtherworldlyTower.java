package spawns;

import org.apache.commons.lang3.ArrayUtils;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.instancemanager.SpawnManager;
import l2s.gameserver.listener.actor.OnDeathListener;
import l2s.gameserver.listener.script.OnInitScriptListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.utils.Functions;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author nexvill
 */
public class OtherworldlyTower implements OnInitScriptListener
{
	private static int _stage;
	private static int _kills;

	private static final String TOWER_STAGE = "otherworldly_tower_stage";
	private static final String MOB_KILLS = "otherworldly_tower_kills";

	private static final OnDeathListener MOB_KILL_LISTENER = new DeathListener();

	// mobs
	private static final int OTHERWORLDLY_SHARD = 18554;
	// shard locs
	private static final Location SHARD_LOCATION_1 = new Location(15048, 250040, -1599);
	private static final Location SHARD_LOCATION_2 = new Location(18376, 249336, -1401);
	private static final Location SHARD_LOCATION_3 = new Location(22664, 252968, -1721);
	private static final Location SHARD_LOCATION_4 = new Location(18664, 256152, -1364);

	private static final int[] MOBS =
	{
		22336, // Steel Warrior
		22337, // Steel Worker
		22338, // Steel Stalker
		22339, // Steel Swordsman
		22340, // Zamad
		22341, // Vegskytt
		22342, // Shisuck
		22343, // Drayzak
		22344 // Beleth' Eye
	};

	private static class DeathListener implements OnDeathListener
	{

		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (self.isNpc())
			{
				if ((self.getNpcId() == OTHERWORLDLY_SHARD) && (_stage > 0))
				{
					_stage--;
					ServerVariables.set(TOWER_STAGE, _stage);
					ServerVariables.set(MOB_KILLS, 0);
				}
				else if (ArrayUtils.contains(MOBS, self.getNpcId()))
				{
					_kills++;
					switch (_stage)
					{
						case 0:
						{
							if (_kills >= 100_000)
							{
								_stage++;
								_kills = 0;
								ServerVariables.set(TOWER_STAGE, _stage);
								ServerVariables.set(MOB_KILLS, _kills);
								SpawnManager.getInstance().despawn("otherworldly_tower_0");
								SpawnManager.getInstance().spawn("otherworldly_tower_1");
								NpcInstance npc1 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_1.getX(), SHARD_LOCATION_1.getY(), SHARD_LOCATION_1.getZ(), 30000);
								NpcInstance npc2 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_2.getX(), SHARD_LOCATION_2.getY(), SHARD_LOCATION_2.getZ(), 30000);
								NpcInstance npc3 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_3.getX(), SHARD_LOCATION_3.getY(), SHARD_LOCATION_3.getZ(), 30000);
								NpcInstance npc4 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_4.getX(), SHARD_LOCATION_4.getY(), SHARD_LOCATION_4.getZ(), 30000);
								Functions.npcShout(npc1, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc2, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc3, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc4, "Hellbound stage increased and now are " + _stage + "!");
							}
							break;
						}
						case 1:
						{
							if (_kills >= 500_000)
							{
								_stage++;
								_kills = 0;
								ServerVariables.set(TOWER_STAGE, _stage);
								ServerVariables.set(MOB_KILLS, _kills);
								SpawnManager.getInstance().despawn("otherworldly_tower_1");
								SpawnManager.getInstance().spawn("otherworldly_tower_2");
								NpcInstance npc1 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_1.getX(), SHARD_LOCATION_1.getY(), SHARD_LOCATION_1.getZ(), 30000);
								NpcInstance npc2 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_2.getX(), SHARD_LOCATION_2.getY(), SHARD_LOCATION_2.getZ(), 30000);
								NpcInstance npc3 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_3.getX(), SHARD_LOCATION_3.getY(), SHARD_LOCATION_3.getZ(), 30000);
								NpcInstance npc4 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_4.getX(), SHARD_LOCATION_4.getY(), SHARD_LOCATION_4.getZ(), 30000);
								Functions.npcShout(npc1, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc2, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc3, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc4, "Hellbound stage increased and now are " + _stage + "!");
							}
							break;
						}
						case 2:
						{
							if (_kills >= 1_500_000)
							{
								_stage++;
								_kills = 0;
								ServerVariables.set(TOWER_STAGE, _stage);
								ServerVariables.set(MOB_KILLS, _kills);
								SpawnManager.getInstance().despawn("otherworldly_tower_2");
								SpawnManager.getInstance().spawn("otherworldly_tower_3");
								NpcInstance npc1 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_1.getX(), SHARD_LOCATION_1.getY(), SHARD_LOCATION_1.getZ(), 30000);
								NpcInstance npc2 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_2.getX(), SHARD_LOCATION_2.getY(), SHARD_LOCATION_2.getZ(), 30000);
								NpcInstance npc3 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_3.getX(), SHARD_LOCATION_3.getY(), SHARD_LOCATION_3.getZ(), 30000);
								NpcInstance npc4 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_4.getX(), SHARD_LOCATION_4.getY(), SHARD_LOCATION_4.getZ(), 30000);
								Functions.npcShout(npc1, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc2, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc3, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc4, "Hellbound stage increased and now are " + _stage + "!");
							}
							break;
						}
						case 3:
						{
							if (_kills >= 4_000_000)
							{
								_stage++;
								_kills = 0;
								ServerVariables.set(TOWER_STAGE, _stage);
								ServerVariables.set(MOB_KILLS, _kills);
								SpawnManager.getInstance().despawn("otherworldly_tower_3");
								SpawnManager.getInstance().spawn("otherworldly_tower_4");
								NpcInstance npc1 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_1.getX(), SHARD_LOCATION_1.getY(), SHARD_LOCATION_1.getZ(), 30000);
								NpcInstance npc2 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_2.getX(), SHARD_LOCATION_2.getY(), SHARD_LOCATION_2.getZ(), 30000);
								NpcInstance npc3 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_3.getX(), SHARD_LOCATION_3.getY(), SHARD_LOCATION_3.getZ(), 30000);
								NpcInstance npc4 = NpcUtils.spawnSingle(OTHERWORLDLY_SHARD, SHARD_LOCATION_4.getX(), SHARD_LOCATION_4.getY(), SHARD_LOCATION_4.getZ(), 30000);
								Functions.npcShout(npc1, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc2, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc3, "Hellbound stage increased and now are " + _stage + "!");
								Functions.npcShout(npc4, "Hellbound stage increased and now are " + _stage + "!");
							}
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void onInit()
	{
		_stage = ServerVariables.getInt(TOWER_STAGE, 0);
		_kills = ServerVariables.getInt(MOB_KILLS, 0);
		CharListenerList.addGlobal(MOB_KILL_LISTENER);
		SpawnManager.getInstance().spawn("otherworldly_tower_0");
	}
}