package l2s.gameserver.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.napile.primitive.Containers;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.listener.Listener;
import l2s.commons.listener.ListenerList;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.data.xml.holder.SpawnHolder;
import l2s.gameserver.database.MySqlDataInsert;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.geometry.LocationsList;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.instancemanager.EventTriggersManager;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.instancemanager.SpawnManager;
import l2s.gameserver.listener.actor.door.impl.MasterOnOpenCloseListenerImpl;
import l2s.gameserver.listener.reflection.OnReflectionCollapseListener;
import l2s.gameserver.listener.zone.impl.EpicZoneListener;
import l2s.gameserver.listener.zone.impl.FishingZoneListener;
import l2s.gameserver.listener.zone.impl.NoLandingZoneListener;
import l2s.gameserver.listener.zone.impl.PresentSceneMovieZoneListener;
import l2s.gameserver.listener.zone.impl.ResidenceEnterLeaveListenerImpl;
import l2s.gameserver.listener.zone.impl.SiegeZoneListener;
import l2s.gameserver.listener.zone.impl.TeleportingZoneListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.HardSpawner;
import l2s.gameserver.model.ObservePoint;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.Spawner;
import l2s.gameserver.model.World;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.EventTriggerPacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.DoorTemplate;
import l2s.gameserver.templates.InstantZone;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.ZoneTemplate;
import l2s.gameserver.templates.spawn.SpawnTemplate;
import l2s.gameserver.utils.NpcUtils;

import gnu.trove.set.hash.TIntHashSet;

public class Reflection
{
	public class ReflectionListenerList extends ListenerList<Reflection>
	{
		public void onCollapse()
		{
			if (!getListeners().isEmpty())
				for (Listener<Reflection> listener : getListeners())
					((OnReflectionCollapseListener) listener).onReflectionCollapse(Reflection.this);
		}
	}

	private static final Logger _log = LoggerFactory.getLogger(Reflection.class);
	private final static AtomicInteger _nextId = new AtomicInteger();
	private final static AtomicBoolean _closed = new AtomicBoolean(false); // Параметр закрывает вход в инстанс.

	private final int _id;

	private String _name = StringUtils.EMPTY;
	private InstantZone _instance;
	private final int _geoIndex;

	private Location _resetLoc; // место, к которому кидает при использовании SoE/unstuck, иначе выбрасывает в
								// основной мир
	private Location _returnLoc; // если не прописано reset, но прописан return, то телепортит туда, одновременно
									// перемещая в основной мир
	private Location _teleportLoc; // точка входа

	protected Set<Spawner> _spawns = new HashSet<Spawner>();
	protected Set<GameObject> _objects = new HashSet<GameObject>();

	// vars
	protected IntObjectMap<DoorInstance> _doors = Containers.emptyIntObjectMap();
	protected Map<String, Zone> _zones = Collections.emptyMap();
	protected Map<String, List<Spawner>> _spawners = Collections.emptyMap();

	protected TIntHashSet _visitors = new TIntHashSet();

	protected final Lock lock = new ReentrantLock();
	protected int _playerCount;

	protected Party _party;

	private int _collapseIfEmptyTime;

	private boolean _isCollapseStarted;
	private ScheduledFuture<?> _collapseTask;
	private ScheduledFuture<?> _collapse1minTask;
	private ScheduledFuture<?> _hiddencollapseTask;

	private final ReflectionListenerList listeners = new ReflectionListenerList();

	private StatsSet _variables = StatsSet.EMPTY;

	public Reflection()
	{
		this(_nextId.incrementAndGet());
	}

	protected Reflection(int id)
	{
		_id = id;
		_geoIndex = GeoEngine.createGeoIndex();
	}

	public int getId()
	{
		return _id;
	}

	public int getInstancedZoneId()
	{
		return _instance == null ? -1 : _instance.getId();
	}

	public void setParty(Party party)
	{
		_party = party;
	}

	public Party getParty()
	{
		return _party;
	}

	public void setCollapseIfEmptyTime(int value)
	{
		_collapseIfEmptyTime = value;
	}

	public String getName()
	{
		return _name;
	}

	protected void setName(String name)
	{
		_name = name;
	}

	public InstantZone getInstancedZone()
	{
		return _instance;
	}

	protected void setInstancedZone(InstantZone iz)
	{
		_instance = iz;
	}

	public int getGeoIndex()
	{
		return _geoIndex;
	}

	public void setCoreLoc(Location l)
	{
		_resetLoc = l;
	}

	public Location getCoreLoc()
	{
		return _resetLoc;
	}

	public void setReturnLoc(Location l)
	{
		_returnLoc = l;
	}

	public Location getReturnLoc()
	{
		return _returnLoc;
	}

	public void setTeleportLoc(Location l)
	{
		_teleportLoc = l;
	}

	public Location getTeleportLoc()
	{
		return _teleportLoc;
	}

	public Collection<Spawner> getSpawns()
	{
		return _spawns;
	}

	public Collection<DoorInstance> getDoors()
	{
		return _doors.valueCollection();
	}

	public DoorInstance getDoor(int id)
	{
		return _doors.get(id);
	}

	public void addZone(Zone zone)
	{
		if (zone.getReflection() == this)
		{
			if (_zones.isEmpty())
				_zones = new HashMap<String, Zone>();
			_zones.put(zone.getName(), zone);
		}
	}

	public Zone getZone(String name)
	{
		return _zones.get(name);
	}

	/**
	 * Время в минутах
	 *
	 * @param minutes
	 */
	public void startCollapseTimer(int minutes, boolean message)
	{
		if (isDefault())
		{
			new Exception("Basic reflection " + _id + " could not be collapsed!").printStackTrace();
			return;
		}

		lock.lock();
		try
		{
			if (_collapseTask != null)
			{
				_collapseTask.cancel(false);
				_collapseTask = null;
			}

			if (_collapse1minTask != null)
			{
				_collapse1minTask.cancel(false);
				_collapse1minTask = null;
			}

			if (message)
				broadcastExpireMsg(minutes);

			if (minutes > 0)
			{
				_collapseTask = ThreadPoolManager.getInstance().schedule(() -> collapse(), TimeUnit.MINUTES.toMillis(minutes));

				if (minutes > 1)
				{
					_collapse1minTask = ThreadPoolManager.getInstance().schedule(() ->
					{
						if (_isCollapseStarted)
							return;

						broadcastExpireMsg(1);
					}, TimeUnit.MINUTES.toMillis(minutes - 1));
				}
			}
			else
				_collapseTask = ThreadPoolManager.getInstance().schedule(() -> collapse(), 1000);
		}
		finally
		{
			lock.unlock();
		}
	}

	public void stopCollapseTimer()
	{
		lock.lock();
		try
		{
			if (_collapseTask != null)
			{
				_collapseTask.cancel(false);
				_collapseTask = null;
			}

			if (_collapse1minTask != null)
			{
				_collapse1minTask.cancel(false);
				_collapse1minTask = null;
			}
		}
		finally
		{
			lock.unlock();
		}
	}

	public long getDelayToCollapse()
	{
		if (_collapseTask != null)
			return _collapseTask.getDelay(TimeUnit.MILLISECONDS);
		return -1;
	}

	public void collapse()
	{
		if (_id <= 0)
		{
			new Exception("Basic reflection " + _id + " could not be collapsed!").printStackTrace();
			return;
		}

		lock.lock();
		try
		{
			if (_isCollapseStarted)
				return;

			_isCollapseStarted = true;

			listeners.onCollapse();
			try
			{
				stopCollapseTimer();
				if (_hiddencollapseTask != null)
				{
					_hiddencollapseTask.cancel(false);
					_hiddencollapseTask = null;
				}

				for (Spawner s : _spawns)
					s.deleteAll();

				for (String group : _spawners.keySet())
					despawnByGroup(group);

				for (DoorInstance d : _doors.valueCollection())
					d.deleteMe();

				_doors.clear();

				for (Zone zone : _zones.values())
					zone.setActive(false);

				_zones.clear();

				EventTriggersManager.getInstance().removeTriggers(this);

				List<Player> teleport = new ArrayList<Player>();
				List<ObservePoint> observers = new ArrayList<ObservePoint>();
				List<GameObject> delete = new ArrayList<GameObject>();

				for (GameObject o : _objects)
				{
					if (o.isPlayer())
						teleport.add((Player) o);
					else if (o.isObservePoint())
						observers.add((ObservePoint) o);
					else if (!o.isPlayable())
						delete.add(o);
				}

				for (Player player : teleport)
				{
					if (player.getParty() != null)
					{
						if (equals(player.getParty().getReflection()))
							player.getParty().setReflection(null);
					}
					if (equals(player.getReflection()))
					{
						if (getReturnLoc() != null)
							player.teleToLocation(getReturnLoc(), ReflectionManager.MAIN);
						else
							player.setReflection(ReflectionManager.MAIN);
					}
					onPlayerExit(player);
				}

				for (Player player : GameObjectsStorage.getPlayers(true, true))
				{
					if (player.getActiveReflection() == this)
						player.setActiveReflection(null);
				}

				for (ObservePoint o : observers)
				{
					Player observer = o.getPlayer();
					if (observer != null)
						observer.leaveObserverMode();
				}

				if (_party != null)
				{
					_party.setReflection(null);
					_party = null;
				}

				for (GameObject o : delete)
					o.deleteMe();

				_spawns.clear();
				_objects.clear();
				_visitors.clear();
				_doors.clear();

				_playerCount = 0;

				onCollapse();
			}
			finally
			{
				ReflectionManager.getInstance().remove(this);
				GeoEngine.deleteGeoIndex(getGeoIndex());
			}
		}
		finally
		{
			lock.unlock();
		}
	}

	protected void onCollapse()
	{
		//
	}

	public void addObject(GameObject o)
	{
		if (_isCollapseStarted)
			return;

		lock.lock();
		try
		{
			if (!_objects.add(o))
				return;

			onAddObject(o);

			if (o.isPlayer())
			{
				_playerCount++;
				_visitors.add(o.getObjectId());

				Player player = o.getPlayer();
				if (!isDefault())
					player.setActiveReflection(this);
				onPlayerEnter(player);
			}

			if (_hiddencollapseTask != null)
			{
				_hiddencollapseTask.cancel(false);
				_hiddencollapseTask = null;
			}
		}
		finally
		{
			lock.unlock();
		}
	}

	public void removeObject(GameObject o)
	{
		if (_isCollapseStarted)
			return;

		lock.lock();
		try
		{
			if (!_objects.remove(o))
				return;

			onRemoveObject(o);

			if (o.isPlayer())
			{
				_playerCount--;
				onPlayerExit(o.getPlayer());

				if (_playerCount <= 0 && !isDefault() && _hiddencollapseTask == null && _collapseIfEmptyTime >= 0)
				{
					if (_collapseIfEmptyTime == 0)
						collapse();
					else
					{
						_hiddencollapseTask = ThreadPoolManager.getInstance().schedule(() ->
						{
							collapse();
						}, _collapseIfEmptyTime * 60 * 1000L);
					}
				}
			}
		}
		finally
		{
			lock.unlock();
		}
	}

	public void onAddObject(GameObject object)
	{
		//
	}

	public void onRemoveObject(GameObject object)
	{
		//
	}

	public void onPlayerEnter(Player player)
	{
		player.getInventory().validateItems();

		for (int triggerId : EventTriggersManager.getInstance().getTriggers(this, false))
			player.sendPacket(new EventTriggerPacket(triggerId, true));
	}

	public void onPlayerExit(Player player)
	{
		player.getInventory().validateItems();

		for (int triggerId : EventTriggersManager.getInstance().getTriggers(this, true))
			player.sendPacket(new EventTriggerPacket(triggerId, false));

		if (player.getActiveSubClass() != null)
		{
			// Для квеста _129_PailakaDevilsLegacy
			for (Servitor servitor : player.getServitors())
			{
				if (servitor != null && (servitor.getNpcId() == 14916 || servitor.getNpcId() == 14917))
					servitor.unSummon(false);
			}
		}
	}

	public List<Player> getPlayers()
	{
		List<Player> result = new ArrayList<Player>();
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isPlayer())
					result.add((Player) o);
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}

	public List<Creature> getPlayersAndObservers()
	{
		List<Creature> result = new ArrayList<Creature>();
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isPlayer() || o.isObservePoint())
					result.add((Creature) o);
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}

	public List<Creature> getObservers()
	{
		List<Creature> result = new ArrayList<Creature>();
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isObservePoint())
					result.add((Creature) o);
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}

	public List<NpcInstance> getNpcs()
	{
		List<NpcInstance> result = new ArrayList<NpcInstance>();
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isNpc())
					result.add((NpcInstance) o);
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}

	public List<NpcInstance> getNpcs(boolean onlyAlive, int... npcIds)
	{
		return getNpcs(onlyAlive, onlyAlive, npcIds);
	}

	public List<NpcInstance> getNpcs(boolean onlyAlive, boolean onlySpawned, int... npcIds)
	{
		List<NpcInstance> result = new ArrayList<NpcInstance>();
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isNpc())
				{
					NpcInstance npc = (NpcInstance) o;
					if ((npcIds.length == 0 || ArrayUtils.contains(npcIds, npc.getNpcId())) && (!onlyAlive || !npc.isDead()) && (!onlySpawned || npc.isVisible()))
						result.add(npc);
				}
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}

	public boolean canChampions()
	{
		return _id <= 0;
	}

	public boolean isAutolootForced()
	{
		return false;
	}

	public boolean isCollapseStarted()
	{
		return _isCollapseStarted;
	}

	public void addSpawn(Spawner spawn)
	{
		if (spawn != null)
			_spawns.add(spawn);
	}

	public void fillSpawns(List<InstantZone.SpawnInfo> si)
	{
		if (si == null)
			return;
		for (InstantZone.SpawnInfo s : si)
		{
			switch (s.getSpawnType())
			{
				case 0: // точечный спаун, в каждой указанной точке
					for (Location loc : s.getCoords())
					{
						addSpawn(NpcUtils.spawnSimple(s.getNpcId(), loc, this, 1, s.getRespawnDelay(), s.getRespawnRnd(), /*
																															 * TODO
																															 */null));
					}
					break;
				case 1: // один точечный спаун в рандомной точке
					addSpawn(NpcUtils.spawnSimple(s.getNpcId(), new LocationsList(s.getCoords()), this, 1, s.getRespawnDelay(), s.getRespawnRnd(), /*
																																					 * TODO
																																					 */null));
					break;
				case 2: // локационный спаун
					addSpawn(NpcUtils.spawnSimple(s.getNpcId(), s.getLoc(), this, s.getCount(), s.getRespawnDelay(), s.getRespawnRnd(), /*
																																		 * TODO
																																		 */null));
					break;
			}
		}
	}

	// FIXME [VISTALL] сдвинуть в один?
	public void init(IntObjectMap<DoorTemplate> doors, Map<String, ZoneTemplate> zones)
	{
		if (!doors.isEmpty())
			_doors = new HashIntObjectMap<DoorInstance>(doors.size());

		for (DoorTemplate template : doors.valueCollection())
		{
			DoorInstance door = new DoorInstance(IdFactory.getInstance().getNextId(), template);
			door.setReflection(this);
			door.getFlags().getInvulnerable().start();
			door.spawnMe(template.getLoc());
			if (template.isOpened())
				door.openMe();

			_doors.put(template.getId(), door);
		}

		initDoors();

		if (!zones.isEmpty())
			_zones = new HashMap<String, Zone>(zones.size());

		for (ZoneTemplate template : zones.values())
		{
			Zone zone = new Zone(template);
			zone.setReflection(this);
			switch (zone.getType())
			{
				case no_landing:
					zone.addListener(NoLandingZoneListener.STATIC);
					break;
				case epic:
					zone.addListener(EpicZoneListener.STATIC);
					break;
				case RESIDENCE:
					zone.addListener(ResidenceEnterLeaveListenerImpl.STATIC);
					break;
				case FISHING:
					zone.addListener(FishingZoneListener.STATIC);
					break;
				case SIEGE:
					zone.addListener(NoLandingZoneListener.STATIC);
					zone.addListener(SiegeZoneListener.STATIC);
					break;
				case TELEPORT:
					zone.addListener(TeleportingZoneListener.STATIC);
					break;
			}

			if (template.getPresentSceneMovie() != null)
				zone.addListener(new PresentSceneMovieZoneListener(template.getPresentSceneMovie()));

			if (template.isEnabled())
				zone.setActive(true);

			_zones.put(template.getName(), zone);
		}

		onCreate();
	}

	// FIXME [VISTALL] сдвинуть в один?
	private void init0(IntObjectMap<InstantZone.DoorInfo> doors, Map<String, InstantZone.ZoneInfo> zones)
	{
		if (!doors.isEmpty())
		{
			_doors = new HashIntObjectMap<DoorInstance>(doors.size());
		}

		for (InstantZone.DoorInfo info : doors.valueCollection())
		{
			DoorInstance door = new DoorInstance(IdFactory.getInstance().getNextId(), info.getTemplate());
			door.setReflection(this);
			if (info.isInvulnerable() && !door.isInvulnerable())
				door.getFlags().getInvulnerable().start();
			else if (!info.isInvulnerable() && door.isInvulnerable())
				door.getFlags().getInvulnerable().stop();
			door.spawnMe(info.getTemplate().getLoc());
			if (info.isOpened())
				door.openMe();
			_doors.put(info.getTemplate().getId(), door);
		}

		initDoors();

		if (!zones.isEmpty())
			_zones = new HashMap<String, Zone>(zones.size());

		for (InstantZone.ZoneInfo t : zones.values())
		{
			Zone zone = new Zone(t.getTemplate());
			zone.setReflection(this);
			switch (zone.getType())
			{
				case no_landing:
					zone.addListener(NoLandingZoneListener.STATIC);
					break;
				case epic:
					zone.addListener(EpicZoneListener.STATIC);
					break;
				case RESIDENCE:
					zone.addListener(ResidenceEnterLeaveListenerImpl.STATIC);
					break;
				case FISHING:
					zone.addListener(FishingZoneListener.STATIC);
					break;
				case SIEGE:
					zone.addListener(NoLandingZoneListener.STATIC);
					zone.addListener(SiegeZoneListener.STATIC);
					break;
				case TELEPORT:
					zone.addListener(TeleportingZoneListener.STATIC);
					break;
			}

			if (t.getTemplate().getPresentSceneMovie() != null)
				zone.addListener(new PresentSceneMovieZoneListener(t.getTemplate().getPresentSceneMovie()));

			if (t.isActive())
				zone.setActive(true);

			_zones.put(t.getTemplate().getName(), zone);
		}
	}

	private void initDoors()
	{
		for (DoorInstance door : _doors.valueCollection())
		{
			if (door.getTemplate().getMasterDoor() > 0)
			{
				DoorInstance masterDoor = getDoor(door.getTemplate().getMasterDoor());

				masterDoor.addListener(new MasterOnOpenCloseListenerImpl(door));
			}
		}
	}

	/**
	 * Открывает дверь в отражении
	 */
	public void openDoor(int doorId)
	{
		DoorInstance door = _doors.get(doorId);
		if (door != null)
			door.openMe();
	}

	/**
	 * Закрывает дверь в отражении
	 */
	public void closeDoor(int doorId)
	{
		DoorInstance door = _doors.get(doorId);
		if (door != null)
			door.closeMe();
	}

	/**
	 * Удаляет все спауны из рефлекшена и запускает коллапс-таймер. Время
	 * указывается в минутах.
	 */
	public void clearReflection(int timeInMinutes, boolean message)
	{
		if (isDefault())
			return;

		for (NpcInstance n : getNpcs())
			n.deleteMe();

		startCollapseTimer(timeInMinutes, message);
	}

	public void broadcastExpireMsg(int minutesToExpire)
	{
		IBroadcastPacket msg;
		if (isDungeon())
			msg = new SystemMessagePacket(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES_YOU_WILL_BE_FORCED_OUT_OF_THE_DUNGEON_WHEN_THE_TIME_EXPIRES).addInteger(minutesToExpire);
		else
			msg = new SystemMessagePacket(SystemMsg.THIS_INSTANT_ZONE_WILL_BE_TERMINATED_IN_S1_MINUTES_YOU_WILL_BE_FORCED_OUT_OF_THE_DUNGEON_WHEN_THE_TIME_EXPIRES).addInteger(minutesToExpire);

		for (Player player : getPlayers())
			player.sendPacket(msg);
	}

	public NpcInstance addSpawnWithoutRespawn(int npcId, Location loc, int randomOffset)
	{
		if (_isCollapseStarted)
			return null;

		Location newLoc;
		if (randomOffset > 0)
			newLoc = Location.findPointToStay(loc, 0, randomOffset, getGeoIndex()).setH(loc.h);
		else
			newLoc = loc;

		return NpcUtils.spawnSingle(npcId, newLoc, this);
	}

	public NpcInstance addSpawnWithRespawn(int npcId, Location loc, int randomOffset, int respawnDelay)
	{
		if (_isCollapseStarted)
			return null;

		Spawner sp = NpcUtils.spawnSimple(npcId, randomOffset > 0 ? Location.findPointToStay(loc, 0, randomOffset, getGeoIndex()) : loc, this, 1, respawnDelay, 0);
		return sp.getLastSpawn();
	}

	public boolean isMain()
	{
		return getId() == 0;
	}

	public boolean isDefault()
	{
		return getId() <= 0;
	}

	public boolean isVisitor(Player player)
	{
		return _visitors.contains(player.getObjectId());
	}

	public int[] getVisitors()
	{
		return _visitors.toArray();
	}

	public void removeVisitors(Player player)
	{
		_visitors.remove(player.getObjectId());
	}

	public void setReenterTime(long time, boolean notify)
	{
		int[] players = null;
		lock.lock();
		try
		{
			players = _visitors.toArray();
		}
		finally
		{
			lock.unlock();
		}

		if (players != null)
		{
			Player player;

			for (int objectId : players)
			{
				try
				{
					player = World.getPlayer(objectId);
					if (player != null)
						player.setInstanceReuse(getInstancedZoneId(), time, notify);
					else
						MySqlDataInsert.set("REPLACE INTO character_instances (obj_id, id, reuse) VALUES (?,?,?)", objectId, getInstancedZoneId(), time);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	protected void onCreate()
	{
		ReflectionManager.getInstance().add(this);
	}

	/**
	 * Только для статических рефлектов.
	 *
	 * @param id <= 0
	 * @return ref
	 */
	public static Reflection createReflection(int id)
	{
		if (id > 0)
			throw new IllegalArgumentException("id should be <= 0");

		return new Reflection(id);
	}

	public void init(InstantZone instantZone)
	{
		setName(instantZone.getName());
		setInstancedZone(instantZone);
		setTeleportLoc(instantZone.getTeleportCoord());
		if (instantZone.getReturnCoords() != null)
			setReturnLoc(instantZone.getReturnCoords());
		fillSpawns(instantZone.getSpawnsInfo());

		if (instantZone.getSpawns().size() > 0)
		{
			_spawners = new HashMap<String, List<Spawner>>(instantZone.getSpawns().size());
			for (Map.Entry<String, InstantZone.SpawnInfo2> entry : instantZone.getSpawns().entrySet())
			{
				List<Spawner> spawnList = new ArrayList<Spawner>(entry.getValue().getTemplates().size());
				_spawners.put(entry.getKey(), spawnList);

				for (SpawnTemplate template : entry.getValue().getTemplates())
				{
					HardSpawner spawner = new HardSpawner(template);
					spawnList.add(spawner);

					spawner.setAmount(template.getCount());
					spawner.setRespawnDelay(template.getRespawn(), template.getRespawnRandom());
					spawner.setRespawnPattern(template.getRespawnPattern());
					spawner.setReflection(this);
					spawner.setRespawnTime(0);
				}

				if (entry.getValue().isSpawned())
					spawnByGroup(entry.getKey());
			}
		}

		init0(instantZone.getDoors(), instantZone.getZones());
		setCollapseIfEmptyTime(instantZone.getCollapseIfEmpty());
		if (instantZone.getTimelimit() > 0)
			startCollapseTimer(instantZone.getTimelimit(), false);

		onCreate();
	}

	public List<Spawner> spawnByGroup(String name)
	{
		if (isMain())
			return SpawnManager.getInstance().spawn(name, false);

		List<Spawner> list = _spawners.get(name);
		if (list == null)
		{
			if (_spawners.isEmpty())
				_spawners = new HashMap<String, List<Spawner>>(1);

			List<SpawnTemplate> templates = SpawnHolder.getInstance().getSpawn(name);
			List<Spawner> spawnList = new ArrayList<Spawner>(templates.size());

			_spawners.put(name, spawnList);

			for (SpawnTemplate template : templates)
			{
				HardSpawner spawner = new HardSpawner(template);
				spawnList.add(spawner);

				spawner.setAmount(template.getCount());
				spawner.setRespawnDelay(template.getRespawn(), template.getRespawnRandom());
				spawner.setRespawnPattern(template.getRespawnPattern());
				spawner.setReflection(this);
				spawner.setRespawnTime(0);
				spawner.init();
			}
			return spawnList;
		}

		for (Spawner s : list)
			s.init();

		return list;
	}

	public void despawnByGroup(String name)
	{
		if (isMain())
			SpawnManager.getInstance().despawn(name);
		else
		{
			List<Spawner> list = _spawners.get(name);
			if (list != null)
			{
				for (Spawner s : list)
					s.deleteAll();
			}
		}
	}

	public void despawnAll()
	{
		if (isMain())
			SpawnManager.getInstance().despawnAll();
		else
		{
			for (List<Spawner> list : _spawners.values())
			{
				for (Spawner s : list)
					s.deleteAll();
			}
		}
	}

	public List<Spawner> getSpawners(String group)
	{
		if (isMain())
			return SpawnManager.getInstance().getSpawners(group);

		List<Spawner> list = _spawners.get(group);
		return list == null ? Collections.<Spawner>emptyList() : list;
	}

	public Collection<Zone> getZones()
	{
		return _zones.values();
	}

	public <T extends Listener<Reflection>> boolean addListener(T listener)
	{
		return listeners.add(listener);
	}

	public <T extends Listener<Reflection>> boolean removeListener(T listener)
	{
		return listeners.remove(listener);
	}

	public void clearVisitors()
	{
		_visitors.clear();
	}

	public void broadcastPacket(IBroadcastPacket... packets)
	{
		for (Player player : getPlayers())
		{
			if (player != null)
				player.sendPacket(packets);
		}
	}

	public void broadcastPacket(List<IBroadcastPacket> packets)
	{
		for (Player player : getPlayers())
		{
			if (player != null)
				player.sendPacket(packets);
		}
	}

	public final StatsSet getVariables()
	{
		return _variables;
	}

	public final void setVariable(String name, Object value)
	{
		if (_variables == StatsSet.EMPTY)
			_variables = new StatsSet();
		_variables.set(name, value);
	}

	public boolean addEventTrigger(int triggerId)
	{
		return EventTriggersManager.getInstance().addTrigger(this, triggerId);
	}

	public boolean removeEventTrigger(int triggerId)
	{
		return EventTriggersManager.getInstance().removeTrigger(this, triggerId);
	}

	public boolean isClosed()
	{
		return _closed.get();
	}

	public boolean close()
	{
		return _closed.compareAndSet(false, true);
	}

	public boolean isDungeon()
	{
		return false;
	}
}