package l2s.gameserver.instancemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2s.gameserver.data.xml.holder.DoorHolder;
import l2s.gameserver.data.xml.holder.TimeRestrictFieldHolder;
import l2s.gameserver.data.xml.holder.ZoneHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.templates.TimeRestrictFieldInfo;

public class ReflectionManager
{
	public static final Reflection MAIN = Reflection.createReflection(0);
	public static final Reflection PARNASSUS = Reflection.createReflection(-1);
	public static final Reflection GIRAN_HARBOR = Reflection.createReflection(-2);
	public static final Reflection JAIL = Reflection.createReflection(-3);
	public static final Reflection PRIMEVAL_ISLE = Reflection.createReflection(-1000);
	public static final Reflection ALLIGATOR_ISLAND = Reflection.createReflection(-1001);
	public static final Reflection FORGOTTEN_PRIMEVAL_GARDEN = Reflection.createReflection(-1002);
	public static final Reflection FROST_LORD_CASTLE = Reflection.createReflection(-1003);
	public static final Reflection ANTHARAS_LAIR = Reflection.createReflection(-1004);

	public static final Reflection _40_49_ZONE = Reflection.createReflection(-9000);
	public static final Reflection _50_59_ZONE = Reflection.createReflection(-9001);
	public static final Reflection _60_69_ZONE = Reflection.createReflection(-9002);
	public static final Reflection _70_79_ZONE = Reflection.createReflection(-9003);
	public static final Reflection _80_89_ZONE = Reflection.createReflection(-9004);
	public static final Reflection _90_99_ZONE = Reflection.createReflection(-9005);

	private final TIntObjectHashMap<Reflection> _reflections = new TIntObjectHashMap<Reflection>();

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();

	private ReflectionManager()
	{
		//
	}

	public void init()
	{
		add(MAIN);
		add(PARNASSUS);
		add(GIRAN_HARBOR);
		add(JAIL);
		add(PRIMEVAL_ISLE);
		add(ALLIGATOR_ISLAND);
		add(FORGOTTEN_PRIMEVAL_GARDEN);
		add(FROST_LORD_CASTLE);
		add(ANTHARAS_LAIR);

		// создаем в рефлекте все зоны, и все двери
		MAIN.init(DoorHolder.getInstance().getDoors(), ZoneHolder.getInstance().getZones());

		TimeRestrictFieldInfo field = TimeRestrictFieldHolder.getInstance().getFields().get(1);
		PRIMEVAL_ISLE.setTeleportLoc(field.getEnterLoc());
		PRIMEVAL_ISLE.setReturnLoc(field.getExitLoc());
		// -------------------------------------------------------------------------------------
		field = TimeRestrictFieldHolder.getInstance().getFields().get(11);
		ALLIGATOR_ISLAND.setTeleportLoc(field.getEnterLoc());
		ALLIGATOR_ISLAND.setReturnLoc(field.getExitLoc());
		// -------------------------------------------------------------------------------------
		field = TimeRestrictFieldHolder.getInstance().getFields().get(4);
		FORGOTTEN_PRIMEVAL_GARDEN.setTeleportLoc(field.getEnterLoc());
		FORGOTTEN_PRIMEVAL_GARDEN.setReturnLoc(field.getExitLoc());
		// -------------------------------------------------------------------------------------
		field = TimeRestrictFieldHolder.getInstance().getFields().get(18);
		FROST_LORD_CASTLE.setTeleportLoc(field.getEnterLoc());
		FROST_LORD_CASTLE.setReturnLoc(field.getExitLoc());
		// -------------------------------------------------------------------------------------
		field = TimeRestrictFieldHolder.getInstance().getFields().get(12);
		ANTHARAS_LAIR.setTeleportLoc(field.getEnterLoc());
		ANTHARAS_LAIR.setReturnLoc(field.getExitLoc());

		JAIL.setCoreLoc(new Location(-114648, -249384, -2984));
	}

	public Reflection get(int id)
	{
		readLock.lock();
		try
		{
			return _reflections.get(id);
		}
		finally
		{
			readLock.unlock();
		}
	}

	public Reflection add(Reflection ref)
	{
		writeLock.lock();
		try
		{
			return _reflections.put(ref.getId(), ref);
		}
		finally
		{
			writeLock.unlock();
		}
	}

	public Reflection remove(Reflection ref)
	{
		writeLock.lock();
		try
		{
			return _reflections.remove(ref.getId());
		}
		finally
		{
			writeLock.unlock();
		}
	}

	public Reflection[] getAll()
	{
		readLock.lock();
		try
		{
			return _reflections.values(new Reflection[_reflections.size()]);
		}
		finally
		{
			readLock.unlock();
		}
	}

	public List<Reflection> getAllByIzId(int izId)
	{
		final List<Reflection> reflections = new ArrayList<Reflection>();

		readLock.lock();
		try
		{
			for(final Reflection r : getAll())
			{
				if(r.getInstancedZoneId() == izId)
				{
					reflections.add(r);
				}
			}
		}
		finally
		{
			readLock.unlock();
		}
		return reflections;
	}

	public int getCountByIzId(int izId)
	{
		readLock.lock();
		try
		{
			int i = 0;
			for(final Reflection r : getAll())
				if(r.getInstancedZoneId() == izId)
				{
					i++;
				}
			return i;
		}
		finally
		{
			readLock.unlock();
		}
	}

	public int size()
	{
		return _reflections.size();
	}

	private static final ReflectionManager _instance = new ReflectionManager();

	public static ReflectionManager getInstance()
	{
		return _instance;
	}
}