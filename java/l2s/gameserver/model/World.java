package l2s.gameserver.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.collections.LazyArrayList;
import l2s.gameserver.Config;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.geometry.Territory;
import l2s.gameserver.instancemanager.EventTriggersManager;
import l2s.gameserver.model.Zone.ZoneType;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.EventTriggerPacket;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.utils.MapUtils;

/**
 * Переработанный класс мира
 *
 * @author G1ta0
 */
public class World
{
	private static final Logger _log = LoggerFactory.getLogger(World.class);

	/** Map dimensions */
	public static final int MAP_MIN_X = Config.GEO_X_FIRST - 20 << 15;
	public static final int MAP_MAX_X = (Config.GEO_X_LAST - 20 + 1 << 15) - 1;
	public static final int MAP_MIN_Y = Config.GEO_Y_FIRST - 18 << 15;
	public static final int MAP_MAX_Y = (Config.GEO_Y_LAST - 18 + 1 << 15) - 1;
	public static final int MAP_MIN_Z = Config.MAP_MIN_Z;
	public static final int MAP_MAX_Z = Config.MAP_MAX_Z;

	public static final int WORLD_SIZE_X = Config.GEO_X_LAST - Config.GEO_X_FIRST + 1;
	public static final int WORLD_SIZE_Y = Config.GEO_Y_LAST - Config.GEO_Y_FIRST + 1;

	public static final int SHIFT_BY = Config.SHIFT_BY;
	public static final int SHIFT_BY_Z = Config.SHIFT_BY_Z;

	/** calculated offset used so top left region is 0,0 */
	public static final int OFFSET_X = Math.abs(MAP_MIN_X >> SHIFT_BY);
	public static final int OFFSET_Y = Math.abs(MAP_MIN_Y >> SHIFT_BY);
	public static final int OFFSET_Z = Math.abs(MAP_MIN_Z >> SHIFT_BY_Z);

	/** Размерность массива регионов */
	private static final int REGIONS_X = (MAP_MAX_X >> SHIFT_BY) + OFFSET_X;
	private static final int REGIONS_Y = (MAP_MAX_Y >> SHIFT_BY) + OFFSET_Y;
	private static final int REGIONS_Z = (MAP_MAX_Z >> SHIFT_BY_Z) + OFFSET_Z;

	private static volatile WorldRegion[][][] _worldRegions = new WorldRegion[REGIONS_X + 1][REGIONS_Y + 1][REGIONS_Z + 1];

	public static void init()
	{
		_log.info("World: Creating regions: [" + (REGIONS_X + 1) + "][" + (REGIONS_Y + 1) + "][" + (REGIONS_Z + 1) + "].");
	}

	private static WorldRegion[][][] getRegions()
	{
		return _worldRegions;
	}

	private static int validX(int x)
	{
		if (x < 0)
			x = 0;
		else if (x > REGIONS_X)
			x = REGIONS_X;
		return x;
	}

	private static int validY(int y)
	{
		if (y < 0)
			y = 0;
		else if (y > REGIONS_Y)
			y = REGIONS_Y;
		return y;
	}

	private static int validZ(int z)
	{
		if (z < 0)
			z = 0;
		else if (z > REGIONS_Z)
			z = REGIONS_Z;
		return z;
	}

	public static int validCoordX(int x)
	{
		if (x < MAP_MIN_X)
			x = MAP_MIN_X + 1;
		else if (x > MAP_MAX_X)
			x = MAP_MAX_X - 1;
		return x;
	}

	public static int validCoordY(int y)
	{
		if (y < MAP_MIN_Y)
			y = MAP_MIN_Y + 1;
		else if (y > MAP_MAX_Y)
			y = MAP_MAX_Y - 1;
		return y;
	}

	public static int validCoordZ(int z)
	{
		if (z < MAP_MIN_Z)
			z = MAP_MIN_Z + 1;
		else if (z > MAP_MAX_Z)
			z = MAP_MAX_Z - 1;
		return z;
	}

	private static int regionX(int x)
	{
		return (x >> SHIFT_BY) + OFFSET_X;
	}

	private static int regionY(int y)
	{
		return (y >> SHIFT_BY) + OFFSET_Y;
	}

	private static int regionZ(int z)
	{
		return (z >> SHIFT_BY_Z) + OFFSET_Z;
	}

	private static int regionToCordX(int x)
	{
		return (x - OFFSET_X) << SHIFT_BY;
	}

	private static int regionToCordY(int y)
	{
		return (y - OFFSET_Y) << SHIFT_BY;
	}

	private static int regionToCordZ(int z)
	{
		return (z - OFFSET_Z) << SHIFT_BY_Z;
	}

	static boolean isNeighbour(int x1, int y1, int z1, int x2, int y2, int z2)
	{
		return x1 <= x2 + 1 && x1 >= x2 - 1 && y1 <= y2 + 1 && y1 >= y2 - 1 && z1 <= z2 + 1 && z1 >= z2 - 1;
	}

	/**
	 * @param loc локация для поиска региона
	 * @return Регион, соответствующий локации
	 */
	public static WorldRegion getRegion(Location loc)
	{
		return getRegion(validX(regionX(loc.x)), validY(regionY(loc.y)), validZ(regionZ(loc.z)));
	}

	/**
	 * @param obj обьект для поиска региона
	 * @return Регион, соответствующий координатам обьекта
	 */
	public static WorldRegion getRegion(GameObject obj)
	{
		return getRegion(validX(regionX(obj.getX())), validY(regionY(obj.getY())), validZ(regionZ(obj.getZ())));
	}

	/**
	 * @param x координата на карте регионов
	 * @param y координата на карте регионов
	 * @param z координата на карте регионов
	 * @return Регион, соответствующий координатам
	 */
	private static WorldRegion getRegion(int x, int y, int z)
	{
		WorldRegion[][][] regions = getRegions();
		WorldRegion region = null;
		region = regions[x][y][z];
		if (region == null)
			synchronized (regions)
			{
				region = regions[x][y][z];
				if (region == null)
					region = regions[x][y][z] = new WorldRegion(x, y, z);
			}
		return region;
	}

	/**
	 * Находит игрока по имени Регистр символов любой.
	 * 
	 * @param name имя
	 * @return найденый игрок или null если игрока нет
	 */
	public static Player getPlayer(String name)
	{
		return GameObjectsStorage.getPlayer(name);
	}

	/**
	 * Находит игрока по objectId
	 *
	 * @param objId
	 * @return найденый игрок или null если игрока нет
	 */
	public static Player getPlayer(int objId)
	{
		return GameObjectsStorage.getPlayer(objId);
	}

	/**
	 * Проверяет, сменился ли регион в котором находится обьект Если сменился -
	 * удаляет обьект из старого региона и добавляет в новый.
	 * 
	 * @param object  обьект для проверки
	 * @param dropper - если это L2ItemInstance, то будет анимация дропа с перса
	 */
	public static void addVisibleObject(GameObject object, Creature dropper)
	{
		if (object == null || !object.isVisible())
			return;

		WorldRegion region = getRegion(object);
		WorldRegion currentRegion = object.getCurrentRegion();

		if (currentRegion == region)
			return;

		if (currentRegion == null) // Новый обьект (пример - игрок вошел в мир, заспаунился моб, дропнули вещь)
		{
			// Добавляем обьект в список видимых
			object.setCurrentRegion(region);
			region.addObject(object);

			// Показываем обьект в текущем и соседних регионах
			// Если обьект игрок, показываем ему все обьекты в текущем и соседних регионах
			for (int x = validX(region.getX() - 1); x <= validX(region.getX() + 1); x++)
				for (int y = validY(region.getY() - 1); y <= validY(region.getY() + 1); y++)
					for (int z = validZ(region.getZ() - 1); z <= validZ(region.getZ() + 1); z++)
						getRegion(x, y, z).addToObservers(object, dropper);
		}
		else
		// Обьект уже существует, перешел из одного региона в другой
		{
			currentRegion.removeObject(object); // Удаляем обьект из старого региона
			object.setCurrentRegion(region);
			region.addObject(object); // Добавляем обьект в список видимых

			// Убираем обьект из старых соседей.
			for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
				for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
					for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
						if (!isNeighbour(region.getX(), region.getY(), region.getZ(), x, y, z))
							getRegion(x, y, z).removeFromObservers(object);

			// Показываем обьект, но в отличие от первого случая - только для новых соседей.
			for (int x = validX(region.getX() - 1); x <= validX(region.getX() + 1); x++)
				for (int y = validY(region.getY() - 1); y <= validY(region.getY() + 1); y++)
					for (int z = validZ(region.getZ() - 1); z <= validZ(region.getZ() + 1); z++)
						if (!isNeighbour(currentRegion.getX(), currentRegion.getY(), currentRegion.getZ(), x, y, z))
							getRegion(x, y, z).addToObservers(object, dropper);
		}

		if (object.isPlayer() && object.getReflection().isMain())
		{
			int regionMapX = MapUtils.regionX(regionToCordX(region.getX()));
			int regionMapY = MapUtils.regionY(regionToCordY(region.getY()));
			int currentRegionMapX = currentRegion == null ? 0 : MapUtils.regionX(regionToCordX(currentRegion.getX()));
			int currentRegionMapY = currentRegion == null ? 0 : MapUtils.regionY(regionToCordY(currentRegion.getY()));
			if (regionMapX != currentRegionMapX || regionMapY != currentRegionMapY)
			{
				for (int triggerId : EventTriggersManager.getInstance().getTriggers(regionMapX, regionMapY))
					object.getPlayer().sendPacket(new EventTriggerPacket(triggerId, true));
			}
		}
	}

	/**
	 * Удаляет обьект из текущего региона
	 * 
	 * @param object обьект для удаления
	 */
	public static void removeVisibleObject(GameObject object)
	{
		if (object == null || object.isVisible())
			return;

		WorldRegion currentRegion;
		if ((currentRegion = object.getCurrentRegion()) == null)
			return;

		object.setCurrentRegion(null);
		currentRegion.removeObject(object);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					getRegion(x, y, z).removeFromObservers(object);
			}
		}
	}

	public static void forgetObject(GameObject object)
	{
		if (object == null)
			return;

		WorldRegion currentRegion;
		if ((currentRegion = object.getCurrentRegion()) == null)
			return;

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					getRegion(x, y, z).forgetObject(object);
			}
		}
	}

	public static GameObject getAroundObjectById(GameObject object, int objId)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return null;

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (obj.getObjectId() == objId)
						{
							return obj;
						}
					}

		return null;
	}

	public static List<GameObject> getAroundObjects(GameObject object)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int oid = object.getObjectId();
		int rid = object.getReflectionId();

		List<GameObject> result = new LazyArrayList<GameObject>(128);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;

						result.add(obj);
					}

		return result;
	}

	public static List<GameObject> getAroundObjects(GameObject object, int radius, int height, Predicate<GameObject> predicate)
	{
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		if (radius == -1)
		{
			List<GameObject> objects = new ArrayList<GameObject>();
			for (GameObject o : GameObjectsStorage.getObjects())
			{
				if (o.getObjectId() == oid || o.getReflectionId() != rid)
					continue;
				objects.add(o);
			}
			return objects;
		}

		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;

		List<GameObject> result = new LazyArrayList<GameObject>(128);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;
						if (Math.abs(obj.getZ() - oz) > height)
							continue;
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
							continue;
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
							continue;
						if (dx * dx + dy * dy > sqrad)
							continue;
						if (predicate != null && !predicate.test(obj))
							continue;

						result.add(obj);
					}

		return result;
	}

	public static List<GameObject> getAroundObjects(GameObject object, int radius, int height)
	{
		return getAroundObjects(object, radius, height, null);
	}

	public static List<Creature> getAroundCharacters(Location loc, int objectId, int reflectionId)
	{
		WorldRegion currentRegion = getRegion(loc);
		if (currentRegion == null)
			return Collections.emptyList();

		List<Creature> result = new LazyArrayList<Creature>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isCreature() || obj.getObjectId() == objectId || obj.getReflectionId() != reflectionId)
							continue;

						result.add((Creature) obj);
					}

		return result;
	}

	public static List<Creature> getAroundCharacters(GameObject object)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int oid = object.getObjectId();
		int rid = object.getReflectionId();

		List<Creature> result = new LazyArrayList<Creature>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isCreature() || obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;

						result.add((Creature) obj);
					}

		return result;
	}

	public static List<Creature> getAroundCharacters(Location loc, int objectId, int reflectionId, int radius, int height)
	{
		if (radius == -1)
		{
			List<Creature> characters = new ArrayList<Creature>();
			for (GameObject object : GameObjectsStorage.getObjects())
			{
				if (!object.isCreature() || object.getObjectId() == objectId || object.getReflectionId() != reflectionId)
					continue;
				characters.add((Creature) object);
			}
			return characters;
		}

		WorldRegion currentRegion = getRegion(loc);
		if (currentRegion == null)
			return Collections.emptyList();

		int ox = loc.getX();
		int oy = loc.getY();
		int oz = loc.getZ();
		int sqrad = radius * radius;

		List<Creature> result = new LazyArrayList<Creature>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isCreature() || obj.getObjectId() == objectId || obj.getReflectionId() != reflectionId)
							continue;
						if (Math.abs(obj.getZ() - oz) > height)
							continue;
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
							continue;
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
							continue;
						if (dx * dx + dy * dy > sqrad)
							continue;

						result.add((Creature) obj);
					}
		return result;
	}

	public static List<Creature> getAroundCharacters(GameObject object, int radius, int height)
	{
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		if (radius == -1)
		{
			List<Creature> characters = new ArrayList<Creature>();
			for (GameObject o : GameObjectsStorage.getObjects())
			{
				if (!o.isCreature() || o.getObjectId() == oid || o.getReflectionId() != rid)
					continue;
				characters.add((Creature) o);
			}
			return characters;
		}

		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;

		List<Creature> result = new LazyArrayList<Creature>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isCreature() || obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;
						if (Math.abs(obj.getZ() - oz) > height)
							continue;
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
							continue;
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
							continue;
						if (dx * dx + dy * dy > sqrad)
							continue;

						result.add((Creature) obj);
					}
		return result;
	}

	/*
	 * Используется в AI монстров для поиска цели для агра.
	 */
	public static List<Creature> getAroundAttackableCreatures(NpcInstance npc, int radius, int height)
	{
		int oid = npc.getObjectId();
		int rid = npc.getReflectionId();
		if (radius == -1)
		{
			List<Creature> creatures = new ArrayList<Creature>();
			for (GameObject o : GameObjectsStorage.getObjects())
			{
				if (!o.isCreature() || o.getObjectId() == oid || o.getReflectionId() != rid)
					continue;

				Creature creature = (Creature) o;
				if (!npc.getAI().canAttackCharacter(creature))
					continue;

				creatures.add(creature);
			}
			return creatures;
		}

		WorldRegion currentRegion = npc.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int ox = npc.getX();
		int oy = npc.getY();
		int oz = npc.getZ();
		int sqrad = radius * radius;

		List<Creature> result = new LazyArrayList<Creature>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isCreature() || obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;
						if (Math.abs(obj.getZ() - oz) > height)
							continue;
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
							continue;
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
							continue;
						if (dx * dx + dy * dy > sqrad)
							continue;

						Creature creature = (Creature) obj;
						if (!npc.getAI().canAttackCharacter(creature))
							continue;

						result.add(creature);
					}
				}
			}
		}
		return result;
	}

	public static List<NpcInstance> getAroundNpc(GameObject object)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int oid = object.getObjectId();
		int rid = object.getReflectionId();

		List<NpcInstance> result = new LazyArrayList<NpcInstance>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isNpc() || obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;

						result.add((NpcInstance) obj);
					}
		return result;
	}

	public static List<NpcInstance> getAroundNpc(GameObject object, int radius, int height)
	{
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		if (radius == -1)
		{
			List<NpcInstance> npcs = new ArrayList<NpcInstance>();
			for (NpcInstance npc : GameObjectsStorage.getNpcs())
			{
				if (npc.getObjectId() == oid || npc.getReflectionId() != rid)
					continue;
				npcs.add(npc);
			}
			return npcs;
		}

		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;

		List<NpcInstance> result = new LazyArrayList<NpcInstance>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isNpc() || obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;
						if (Math.abs(obj.getZ() - oz) > height)
							continue;
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
							continue;
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
							continue;
						if (dx * dx + dy * dy > sqrad)
							continue;

						result.add((NpcInstance) obj);
					}
		return result;
	}

	public static List<NpcInstance> getAroundNpc(Location loc, WorldRegion region, int reflect, int radius, int height)
	{
		int rid = reflect;
		if (radius == -1)
		{
			List<NpcInstance> npcs = new ArrayList<NpcInstance>();
			for (NpcInstance npc : GameObjectsStorage.getNpcs())
			{
				if (npc.getReflectionId() != rid)
					continue;
				npcs.add(npc);
			}
			return npcs;
		}

		WorldRegion currentRegion = region;
		if (currentRegion == null)
			return Collections.emptyList();

		int ox = loc.x;
		int oy = loc.y;
		int oz = loc.z;
		int sqrad = radius * radius;

		List<NpcInstance> result = new LazyArrayList<NpcInstance>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isNpc() || obj.getReflectionId() != rid)
							continue;
						if (Math.abs(obj.getZ() - oz) > height)
							continue;
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
							continue;
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
							continue;
						if (dx * dx + dy * dy > sqrad)
							continue;

						result.add((NpcInstance) obj);
					}
		return result;
	}

	public static List<Playable> getAroundPlayables(GameObject object)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int oid = object.getObjectId();
		int rid = object.getReflectionId();

		List<Playable> result = new LazyArrayList<Playable>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isPlayable() || obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;

						result.add((Playable) obj);
					}
		return result;
	}

	public static List<Playable> getAroundPlayables(GameObject object, int radius, int height)
	{
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		if (radius == -1)
		{
			List<Playable> playables = new ArrayList<Playable>();
			for (GameObject o : GameObjectsStorage.getObjects())
			{
				if (!o.isPlayable() || o.getObjectId() == oid || o.getReflectionId() != rid)
					continue;
				playables.add((Playable) o);
			}
			return playables;
		}

		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;

		List<Playable> result = new LazyArrayList<Playable>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isPlayable() || obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;
						if (Math.abs(obj.getZ() - oz) > height)
							continue;
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
							continue;
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
							continue;
						if (dx * dx + dy * dy > sqrad)
							continue;

						result.add((Playable) obj);
					}
		return result;
	}

	public static List<Player> getAroundPlayers(GameObject object)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int oid = object.getObjectId();
		int rid = object.getReflectionId();

		List<Player> result = new LazyArrayList<Player>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isPlayer() || obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;

						result.add((Player) obj);
					}
		return result;
	}

	public static List<Player> getAroundPlayers(GameObject object, int radius)
	{
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		if (radius == -1)
		{
			List<Player> players = new ArrayList<Player>();
			for (Player player : GameObjectsStorage.getPlayers(true, true))
			{
				if (player.getObjectId() == oid || player.getReflectionId() != rid)
					continue;
				players.add(player);
			}
			return players;
		}

		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;

		List<Player> result = new LazyArrayList<Player>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isPlayer() || obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
							continue;
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
							continue;
						if (dx * dx + dy * dy > sqrad)
							continue;

						result.add((Player) obj);
					}
		return result;
	}

	public static List<Player> getAroundPlayers(GameObject object, int radius, int height)
	{
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		if (radius == -1)
		{
			List<Player> players = new ArrayList<Player>();
			for (Player player : GameObjectsStorage.getPlayers(true, true))
			{
				if (player.getObjectId() == oid || player.getReflectionId() != rid)
					continue;
				players.add(player);
			}
			return players;
		}

		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;

		List<Player> result = new LazyArrayList<Player>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isPlayer() || obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;
						if (Math.abs(obj.getZ() - oz) > height)
							continue;
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
							continue;
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
							continue;
						if (dx * dx + dy * dy > sqrad)
							continue;

						result.add((Player) obj);
					}
		return result;
	}

	/**
	 * Получить список игроков для координат, а не объекта Используется для рассылки
	 * броадкаста кораблей
	 * 
	 * @param loc
	 * @return
	 */
	public static List<Player> getAroundObservers(Location loc)
	{
		final WorldRegion currentRegion = getRegion(loc);
		if (currentRegion == null)
			return Collections.emptyList();

		final List<Player> result = new LazyArrayList<Player>(64);

		final int x1 = validX(currentRegion.getX() + 1);
		final int y0 = validY(currentRegion.getY() - 1);
		final int y1 = validY(currentRegion.getY() + 1);
		final int z0 = validZ(currentRegion.getZ() - 1);
		final int z1 = validZ(currentRegion.getZ() + 1);
		for (int x = validX(currentRegion.getX() - 1); x <= x1; x++)
			for (int y = y0; y <= y1; y++)
				for (int z = z0; z <= z1; z++)
					for (GameObject obj : getRegion(x, y, z))
						if (obj.isObservePoint() || obj.isPlayer())
						{
							if (obj.isPlayer() && ((Player) obj).isInObserverMode())
								continue;
							result.add(obj.getPlayer());
						}
		return result;
	}

	/**
	 * Получить список игроков для отправки или обновлении информации (пакетный
	 * уровень)
	 * 
	 * @param object
	 * @return
	 */
	public static List<Player> getAroundObservers(GameObject object, Predicate<Player> condition)
	{
		final WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		final int oid = object.getObjectId();
		final int rid = object.getReflectionId();

		final List<Player> result = new LazyArrayList<Player>(64);

		final int x1 = validX(currentRegion.getX() + 1);
		final int y0 = validY(currentRegion.getY() - 1);
		final int y1 = validY(currentRegion.getY() + 1);
		final int z0 = validZ(currentRegion.getZ() - 1);
		final int z1 = validZ(currentRegion.getZ() + 1);
		for (int x = validX(currentRegion.getX() - 1); x <= x1; x++)
			for (int y = y0; y <= y1; y++)
				for (int z = z0; z <= z1; z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;

						if (obj.isObservePoint() || obj.isPlayer())
						{
							if (obj.isPlayer() && ((Player) obj).isInObserverMode())
								continue;
							Player player = obj.getPlayer();
							if (condition != null && !condition.test(player))
								continue;
							result.add(player);
						}
					}
		return result;
	}

	public static List<Player> getAroundObservers(GameObject object)
	{
		return getAroundObservers(object, null);
	}

	public static List<ItemInstance> getAroundItems(GameObject object, int radius, int height)
	{
		if (radius < 0)
			return Collections.emptyList();

		int oid = object.getObjectId();
		int rid = object.getReflectionId();

		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
			return Collections.emptyList();

		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;

		List<ItemInstance> result = new LazyArrayList<>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isItem() || obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;
						if (Math.abs(obj.getZ() - oz) > height)
							continue;
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
							continue;
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
							continue;
						if (dx * dx + dy * dy > sqrad)
							continue;

						result.add((ItemInstance) obj);
					}
				}
			}
		}
		return result;
	}

	public static List<Player> getPlayersOnMap(int mapX, int mapY)
	{
		return getPlayersOnMap(mapX, mapY, 0, null);
	}

	public static List<Player> getPlayersOnMap(int mapX, int mapY, int offset)
	{
		return getPlayersOnMap(mapX, mapY, offset, null);
	}

	public static List<Player> getPlayersOnMap(int mapX, int mapY, int offset, Reflection reflection)
	{
		List<Player> list = new ArrayList<Player>();

		for (Player player : GameObjectsStorage.getPlayers(true, true))
		{
			if (reflection != null && player.getReflection() != reflection)
				continue;

			int tx = MapUtils.regionX(player);
			int ty = MapUtils.regionY(player);

			if (tx >= mapX - offset && tx <= mapX + offset && ty >= mapY - offset && ty <= mapY + offset)
				list.add(player);
		}

		return list;
	}

	/**
	 * Проверить, пустые ли соседние регионы от игроков, включая текущий
	 * 
	 * @return
	 */
	public static boolean isNeighborsEmpty(WorldRegion region)
	{
		for (int x = validX(region.getX() - 1); x <= validX(region.getX() + 1); x++)
			for (int y = validY(region.getY() - 1); y <= validY(region.getY() + 1); y++)
				for (int z = validZ(region.getZ() - 1); z <= validZ(region.getZ() + 1); z++)
					if (!getRegion(x, y, z).isEmpty())
						return false;
		return true;
	}

	public static void activate(WorldRegion currentRegion)
	{
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					getRegion(x, y, z).setActive(true);
	}

	public static void deactivate(WorldRegion currentRegion)
	{
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					if (isNeighborsEmpty(getRegion(x, y, z)))
						getRegion(x, y, z).setActive(false);
	}

	/**
	 * Показывает игроку все видимые обьекты в текущем регионе и соседних
	 * 
	 * @param player
	 */
	public static void showObjectsToPlayer(Player player)
	{
		final WorldRegion currentRegion = player.getCurrentRegion();
		if (currentRegion == null)
			return;

		final int oid = player.getObjectId();
		final int rid = player.getReflectionId();

		final int x1 = validX(currentRegion.getX() + 1);
		final int y0 = validY(currentRegion.getY() - 1);
		final int y1 = validY(currentRegion.getY() + 1);
		final int z0 = validZ(currentRegion.getZ() - 1);
		final int z1 = validZ(currentRegion.getZ() + 1);
		for (int x = validX(currentRegion.getX() - 1); x <= x1; x++)
			for (int y = y0; y <= y1; y++)
				for (int z = z0; z <= z1; z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;

						player.sendPacket(player.addVisibleObject(obj, null));
					}
	}

	/**
	 * Убирает у игрока все видимые обьекты в текущем регионе и соседних
	 * 
	 * @param player
	 */
	public static void removeObjectsFromPlayer(Player player)
	{
		final WorldRegion currentRegion = player.getCurrentRegion();
		if (currentRegion == null)
			return;

		final int oid = player.getObjectId();
		final int rid = player.getReflectionId();

		final int x1 = validX(currentRegion.getX() + 1);
		final int y0 = validY(currentRegion.getY() - 1);
		final int y1 = validY(currentRegion.getY() + 1);
		final int z0 = validZ(currentRegion.getZ() - 1);
		final int z1 = validZ(currentRegion.getZ() + 1);
		for (int x = validX(currentRegion.getX() - 1); x <= x1; x++)
			for (int y = y0; y <= y1; y++)
				for (int z = z0; z <= z1; z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (obj.getObjectId() == oid || obj.getReflectionId() != rid)
							continue;

						player.sendPacket(player.removeVisibleObject(obj, null));
					}
	}

	/**
	 * Убирает обьект у всех игроков в регионе
	 */
	public static void removeObjectFromPlayers(GameObject object)
	{
		List<IClientOutgoingPacket> d = null;
		for (Player p : World.getAroundObservers(object))
			p.sendPacket(p.removeVisibleObject(object, d == null ? d = object.deletePacketList(p) : d));
	}

	static void addZone(Zone zone)
	{
		Reflection reflection = zone.getReflection();

		Territory territory = zone.getTerritory();
		if (territory == null)
		{
			_log.info("World: zone - " + zone.getName() + " not has territory.");
			return;
		}

		for (int x = validX(regionX(territory.getXmin())); x <= validX(regionX(territory.getXmax())); x++)
		{
			for (int y = validY(regionY(territory.getYmin())); y <= validY(regionY(territory.getYmax())); y++)
			{
				for (int z = validZ(regionZ(territory.getZmin())); z <= validZ(regionZ(territory.getZmax())); z++)
				{
					WorldRegion region = getRegion(x, y, z);
					region.addZone(zone);
					for (GameObject obj : region)
					{
						if (!obj.isCreature() || obj.getReflection() != reflection)
							continue;

						((Creature) obj).updateZones();
					}
				}
			}
		}

		if (zone.getTemplate().getEventTriggerId() != 0)
		{
			for (int x = MapUtils.regionX(territory.getXmin()); x <= MapUtils.regionX(territory.getXmax()); x++)
			{
				for (int y = MapUtils.regionY(territory.getYmin()); y <= MapUtils.regionY(territory.getYmax()); y++)
					EventTriggersManager.getInstance().addTrigger(x, y, zone.getTemplate().getEventTriggerId());
			}
		}
	}

	static void removeZone(Zone zone)
	{
		Reflection reflection = zone.getReflection();

		Territory territory = zone.getTerritory();
		if (territory == null)
		{
			_log.info("World: zone - " + zone.getName() + " not has territory.");
			return;
		}

		for (int x = validX(regionX(territory.getXmin())); x <= validX(regionX(territory.getXmax())); x++)
		{
			for (int y = validY(regionY(territory.getYmin())); y <= validY(regionY(territory.getYmax())); y++)
			{
				for (int z = validZ(regionZ(territory.getZmin())); z <= validZ(regionZ(territory.getZmax())); z++)
				{
					WorldRegion region = getRegion(x, y, z);
					region.removeZone(zone);
					for (GameObject obj : region)
					{
						if (!obj.isCreature() || obj.getReflection() != reflection)
							continue;

						((Creature) obj).updateZones();
					}
				}
			}
		}

		if (zone.getTemplate().getEventTriggerId() != 0)
		{
			for (int x = MapUtils.regionX(territory.getXmin()); x <= MapUtils.regionX(territory.getXmax()); x++)
			{
				for (int y = MapUtils.regionY(territory.getYmin()); y <= MapUtils.regionY(territory.getYmax()); y++)
					EventTriggersManager.getInstance().removeTrigger(x, y, zone.getTemplate().getEventTriggerId());
			}
		}
	}

	/**
	 * Создает и возвращает список территорий для точек x, y, z
	 */
	public static void getZones(Collection<Zone> inside, Location loc, Reflection reflection)
	{
		WorldRegion region = getRegion(loc);
		Zone[] zones = region.getZones();
		if (zones.length == 0)
			return;
		for (Zone zone : zones)
			if (zone.checkIfInZone(loc.x, loc.y, loc.z, reflection))
				inside.add(zone);
	}

	public static void getZones(Collection<Zone> inside, int x, int y, Reflection reflection)
	{
		WorldRegion[][] regionsByX = _worldRegions[validX(regionX(x))];
		if (regionsByX == null)
			return;

		WorldRegion[] regionsByXY = regionsByX[validY(regionY(y))];
		if (regionsByXY == null)
			return;

		for (WorldRegion region : regionsByXY)
		{
			if (region == null)
				continue;

			Zone[] zones = region.getZones();
			if (zones.length == 0)
				continue;

			for (Zone zone : zones)
			{
				if (zone.isActive() && zone.getReflection() == reflection && zone.checkIfInZone(x, y))
					inside.add(zone);
			}
		}
	}

	public static boolean isWater(Location loc, Reflection reflection)
	{
		return getWater(loc, reflection) != null;
	}

	public static Zone getWater(Location loc, Reflection reflection)
	{
		WorldRegion region = getRegion(loc);
		Zone[] zones = region.getZones();
		if (zones.length == 0)
			return null;
		for (Zone zone : zones)
			if (zone != null && zone.getType() == ZoneType.water && zone.checkIfInZone(loc.x, loc.y, loc.z, reflection))
				return zone;
		return null;
	}

	/**
	 * Возвращает статистку по регионам
	 * 
	 * @return int[], где<br>
	 *         [0] количество активных регионов<br>
	 *         [1] количество неактивных регионов<br>
	 *         [2] количество неинициализированных регионов<br>
	 *         [10] количество объектов<br>
	 *         [11] количество персонажей<br>
	 *         [12] количество игроков<br>
	 *         [13] количество игроков в оффлайн-режиме<br>
	 *         [14] количество NPC<br>
	 *         [15] количество активных NPC<br>
	 *         [16] количество монстров<br>
	 *         [17] количество минионов<br>
	 *         [18] количество саммонов/петов<br>
	 *         [19] количество дверей<br>
	 *         [20] количество вещей<br>
	 */
	public static int[] getStats()
	{
		WorldRegion region;
		int[] ret = new int[32];

		for (int x = 0; x <= REGIONS_X; x++)
			for (int y = 0; y <= REGIONS_Y; y++)
				for (int z = 0; z <= REGIONS_Z; z++)
				{
					ret[0]++;

					region = _worldRegions[x][y][z];

					if (region != null)
					{
						if (region.isActive())
							ret[1]++;
						else
							ret[2]++;

						for (GameObject obj : region)
						{
							ret[10]++;

							if (obj.isCreature())
							{
								ret[11]++;

								if (obj.isPlayer())
								{
									ret[12]++;
									Player p = (Player) obj;

									if (p.isInOfflineMode())
										ret[13]++;
								}
								else if (obj.isNpc())
								{
									ret[14]++;

									if (obj.isMonster())
									{
										ret[16]++;
										if (obj.isMinion())
											ret[17]++;
									}

									NpcInstance npc = (NpcInstance) obj;
									if (npc.hasAI())
									{
										if (npc.getAI().isActive())
											ret[15]++;
									}
								}
								else if (obj.isPlayable())
								{
									ret[18]++;
								}
								else if (obj.isDoor())
								{
									ret[19]++;
								}
							}
							else if (obj.isItem())
							{
								ret[20]++;
							}
						}
					}
					else
						ret[3]++;

				}
		return ret;
	}
}