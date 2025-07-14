package l2s.gameserver.instancemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.database.MySqlDataInsert;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.listener.actor.OnDeathListener;
import l2s.gameserver.listener.actor.OnMagicUseListener;
import l2s.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Spawner;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExSendUIEventPacket;
import l2s.gameserver.utils.ItemFunctions;

/**
 * @author nexvill
 */
public class TranscendentInstanceZone70 extends Reflection
{
	// Npcs
	private static final int BUNCH = 34122;

	// vars
	private ScheduledFuture<?> _runTask = null;
	private ScheduledFuture<?> _collapseTask = null;
	private long _timeStart;
	private boolean _isFirstEnter;
	private boolean _firstSpawn;
	private Player _player;
	private int _objId;

	// Zones
	protected Zone _instanceZone;

	// Listeners
	private final OnDeathListener _deathListener = new DeathListener();
	private final OnZoneEnterLeaveListener _instanceZoneListener = new ZoneListener();
	private final OnMagicUseListener _magicUseListener = new MagicUseListener();

	@Override
	protected void onCreate()
	{
		_instanceZone = getZone("[transcendent_instance_zone_70]");

		_instanceZone.addListener(_instanceZoneListener);
		CharListenerList.addGlobal(_magicUseListener);
		_isFirstEnter = true;

		super.onCreate();
	}

	private void stopTask()
	{
		if(_runTask != null)
		{
			_runTask.cancel(true);
			_runTask = null;
		}
		for(Player player : getPlayers())
		{
			player.sendPacket(new ExSendUIEventPacket(player, 1, 0, 0, 0));
		}
		ThreadPoolManager.getInstance().schedule(() -> clearReflection(1, true), 1000L);
	}

	@Override
	protected void onCollapse()
	{
		if(_player != null)
		{
			_player.setVar(PlayerVariables.RESTRICT_FIELD_USED, true);
		}
		else
		{
			MySqlDataInsert.set("REPLACE INTO character_variables (obj_id, name, value, expire_time) VALUES (?,?,?,?)", _objId, PlayerVariables.RESTRICT_FIELD_USED, "true", -1);
		}
		super.onCollapse();
	}

	private class MagicUseListener implements OnMagicUseListener
	{
		@Override
		public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt)
		{
			if(actor.isNpc())
			{
				if(actor.getNpcId() == BUNCH)
				{
					for(Player player : getPlayers())
					{
						if(_isFirstEnter)
						{
							player.sendPacket(new ExSendUIEventPacket(player, 0, 0, 600, 0, NpcString.TIME_REMAINING));
							_timeStart = System.currentTimeMillis();
							_isFirstEnter = false;
							_player = player;
							_objId = player.getObjectId();

							_runTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> {
								int variant = Rnd.get(3);
								List<Spawner> spawners = new ArrayList<Spawner>();
								for(int i = 1; i <= 15; i++)
								{
									if(Rnd.get(100) < 70)
									{
										spawners.addAll(spawnByGroup("transcendent_70_variant_" + (variant + 1) + "_" + i));
									}
								}
								for(int i = 1; i <= 12; i++)
								{
									if(Rnd.get(100) < 10)
									{
										spawners.addAll(spawnByGroup("transcendent_70_box_" + i));
									}
								}
								if(System.currentTimeMillis() >= (_timeStart + (240 * 1000)) && !_firstSpawn)
								{
									_firstSpawn = true;
									NpcInstance npc = addSpawnWithoutRespawn(22334, new Location(168248, 28376, -3624, 16578), 0);
									int result = Rnd.get(4);
									if(result == 0)
									{
										npc.setTitle("Enhanced with Spirit Ore");
									}
									else if(result == 1)
									{
										npc.setTitle("Enhanced with Spirit");
									}
									else if(result == 2)
									{
										npc.setTitle("Enhanced with Grace");
									}
									else if(result == 3)
									{
										npc.setTitle("Enhanced with Supplies");
									}
								}

								for(Spawner spawner : spawners)
								{
									for(NpcInstance npc : spawner.getAllSpawned())
									{
										npc.setTarget(player);
										npc.getAI().Attack(player, true, false);
										npc.getAggroList().addDamageHate(player, 1, 1000000);
									}
								}
							}, 10000, Rnd.get(5000, 10000));
							ThreadPoolManager.getInstance().schedule(() -> stopTask(), 600000);
						}
					}
				}
			}
		}
	}

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature victim, Creature killer)
		{
			if(!victim.isPlayer())
			{
				if(victim.isNpc())
				{
					if(victim.getNpcId() == 22334)
					{
						if(victim.getTitle() == "Enhanced with Spirit Ore")
						{
							ItemFunctions.addItem(killer.getPlayer(), 3031, Rnd.get(1500, 2500));
						}
						else if(victim.getTitle() == "Enhanced with Spirit")
						{
							ItemFunctions.addItem(killer.getPlayer(), 90907, Rnd.get(25, 35));
						}
						else if(victim.getTitle() == "Enhanced with Grace")
						{
							ItemFunctions.addItem(killer.getPlayer(), 93274, Rnd.get(250, 350));
						}
						else if(victim.getTitle() == "Enhanced with Supplies")
						{
							ItemFunctions.addItem(killer.getPlayer(), 3031, Rnd.get(600, 750));
							ItemFunctions.addItem(killer.getPlayer(), 90907, 20);
							ItemFunctions.addItem(killer.getPlayer(), 93274, Rnd.get(75, 100));
						}

						if(Rnd.get(100) < 30)
						{
							addSpawnWithoutRespawn(22334, new Location(168248, 28376, -3624, 16578), 0);
						}
					}
				}
				return;
			}

			for(Abnormal e : victim.getAbnormalList())
			{
				if((e.getSkill().getId() == 45197) || (e.getSkill().getId() == 45198) || (e.getSkill().getId() == 59829))
				{
					e.exit();
				}
			}
		}
	}

	private class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if(!cha.isPlayer())
				return;

			if(!_isFirstEnter)
			{
				cha.getPlayer().sendPacket(new ExSendUIEventPacket(cha.getPlayer(), 0, 0, (int) ((System.currentTimeMillis() - _timeStart)
						/ 1000), 0, NpcString.TIME_REMAINING));
			}

			if(zone == _instanceZone)
			{
				cha.addListener(_deathListener);
				if(_collapseTask != null)
				{
					_collapseTask.cancel(true);
					_collapseTask = null;
				}
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
			if(cha.isPlayer() && zone == _instanceZone)
			{
				cha.removeListener(_deathListener);
				for(Abnormal e : cha.getAbnormalList())
				{
					if((e.getSkill().getId() == 45197) || (e.getSkill().getId() == 45198) || (e.getSkill().getId() == 59829))
					{
						e.exit();
					}
				}
				cha.sendPacket(new ExSendUIEventPacket(cha.getPlayer(), 1, 0, 0, 0));
				_collapseTask = ThreadPoolManager.getInstance().schedule(() -> collapse(), 60000L);
			}
		}
	}
}