package l2s.gameserver.geometry;

import org.dom4j.Element;

import l2s.commons.geometry.Point3D;
import l2s.commons.util.Rnd;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.World;
import l2s.gameserver.templates.spawn.SpawnRange;
import l2s.gameserver.utils.PositionUtils;

public class Location extends Point3D implements ILocation, SpawnRange
{
	public int h;

	public Location()
	{}

	/**
	 * Позиция (x, y, z, heading)
	 */
	public Location(int x, int y, int z, int heading)
	{
		super(x, y, z);
		h = heading;
	}

	public Location(int x, int y, int z)
	{
		this(x, y, z, 0);
	}

	public Location(int[] xyz)
	{
		this(xyz[0], xyz[1], xyz[2], 0);
	}

	public Location(ILocation loc)
	{
		this(loc.getX(), loc.getY(), loc.getZ(), loc.getHeading());
	}

	public Location changeZ(int zDiff)
	{
		z += zDiff;
		return this;
	}

	public Location correctGeoZ(int geoIndex)
	{
		z = GeoEngine.correctGeoZ(x, y, z, geoIndex);
		return this;
	}

	public Location setX(int x)
	{
		this.x = x;
		return this;
	}

	public Location setY(int y)
	{
		this.y = y;
		return this;
	}

	public Location setZ(int z)
	{
		this.z = z;
		return this;
	}

	public Location setH(int h)
	{
		this.h = h;
		return this;
	}

	public Location set(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Location set(int x, int y, int z, int h)
	{
		set(x, y, z);
		this.h = h;
		return this;
	}

	public Location set(Location loc)
	{
		x = loc.x;
		y = loc.y;
		z = loc.z;
		h = loc.h;
		return this;
	}

	@Override
	public int getHeading()
	{
		return h;
	}

	public Location world2geo()
	{
		x = x - World.MAP_MIN_X >> 4;
		y = y - World.MAP_MIN_Y >> 4;
		return this;
	}

	public Location geo2world()
	{
		// размер одного блока 16*16 точек, +8*+8 это его средина
		x = (x << 4) + World.MAP_MIN_X + 8;
		y = (y << 4) + World.MAP_MIN_Y + 8;
		return this;
	}

	@Override
	public Location clone()
	{
		return new Location(x, y, z, h);
	}

	@Override
	public final String toString()
	{
		return x + "," + y + "," + z + "," + h;
	}

	public boolean isNull()
	{
		return x == 0 || y == 0 || z == 0;
	}

	public final String toXYZString()
	{
		return x + " " + y + " " + z;
	}

	/**
	 * Парсит Location из строки, где координаты разделены пробелами или запятыми
	 */
	public static Location parseLoc(String s) throws IllegalArgumentException
	{
		String[] xyzh = s.split("[\\s,;]+");
		if(xyzh.length < 3)
			throw new IllegalArgumentException("Can't parse location from string: " + s);
		int x = Integer.parseInt(xyzh[0]);
		int y = Integer.parseInt(xyzh[1]);
		int z = Integer.parseInt(xyzh[2]);
		int h = xyzh.length < 4 ? 0 : Integer.parseInt(xyzh[3]);
		return new Location(x, y, z, h);
	}

	public static Location parse(Element element)
	{
		int x = Integer.parseInt(element.attributeValue("x"));
		int y = Integer.parseInt(element.attributeValue("y"));
		int z = Integer.parseInt(element.attributeValue("z"));
		int h = element.attributeValue("h") == null ? 0 : Integer.parseInt(element.attributeValue("h"));
		return new Location(x, y, z, h);
	}

	/**
	 * Найти стабильную точку перед объектом obj1 для спавна объекта obj2, с учетом
	 * heading
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param radiusmin
	 * @param radiusmax
	 * @param geoIndex
	 * @return
	 */
	public static Location findFrontPosition(GameObject obj, GameObject obj2, int radiusmin, int radiusmax)
	{
		if(radiusmax <= 0 || radiusmax < radiusmin)
			return new Location(obj);

		double collision = obj.getCurrentCollisionRadius() + obj2.getCurrentCollisionRadius();
		int randomRadius, randomAngle, tempz;
		int minangle = 0;
		int maxangle = 360;

		if(!obj.equals(obj2))
		{
			double angle = PositionUtils.calculateAngleFrom(obj, obj2);
			minangle = (int) angle - 45;
			maxangle = (int) angle + 45;
		}

		Location pos = new Location();
		for(int i = 0; i < 100; i++)
		{
			randomRadius = Rnd.get(radiusmin, radiusmax);
			randomAngle = Rnd.get(minangle, maxangle);
			pos.x = obj.getX() + (int) ((collision + randomRadius) * Math.cos(Math.toRadians(randomAngle)));
			pos.y = obj.getY() + (int) ((collision + randomRadius) * Math.sin(Math.toRadians(randomAngle)));
			pos.z = obj.getZ();
			tempz = GeoEngine.getLowerHeight(pos.x, pos.y, pos.z, obj.getGeoIndex());
			if(Math.abs(pos.z - tempz) < 200 && GeoEngine.getLowerNSWE(pos.x, pos.y, tempz, obj.getGeoIndex()) == GeoEngine.NSWE_ALL)
			{
				pos.z = tempz;
				if(!obj.equals(obj2))
					pos.h = PositionUtils.getHeadingTo(pos, obj2.getLoc());
				else
					pos.h = obj.getHeading();
				return pos;
			}
		}

		return new Location(obj);
	}

	/**
	 * Найти точку в пределах досягаемости от начальной
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param radiusmin
	 * @param radiusmax
	 * @param geoIndex
	 * @return
	 */
	public static Location findAroundPosition(int x, int y, int z, int radiusmin, int radiusmax, int geoIndex)
	{
		if(radiusmax <= 0 || radiusmax < radiusmin)
			return new Location(x, y, z);

		Location pos;
		int tempz;
		for(int i = 0; i < 100; i++)
		{
			pos = Location.coordsRandomize(x, y, z, 0, radiusmin, radiusmax);
			tempz = GeoEngine.getLowerHeight(pos.x, pos.y, pos.z, geoIndex);
			if(GeoEngine.canMoveToCoord(x, y, z, pos.x, pos.y, tempz, geoIndex) && GeoEngine.canMoveToCoord(pos.x, pos.y, tempz, x, y, z, geoIndex))
			{
				pos.z = tempz;
				return pos;
			}
		}
		return new Location(x, y, z);
	}

	public static Location findAroundPosition(Location loc, int radius, int geoIndex)
	{
		return findAroundPosition(loc.x, loc.y, loc.z, 0, radius, geoIndex);
	}

	public static Location findAroundPosition(Location loc, int radiusmin, int radiusmax, int geoIndex)
	{
		return findAroundPosition(loc.x, loc.y, loc.z, radiusmin, radiusmax, geoIndex);
	}

	public static Location findAroundPosition(GameObject obj, Location loc, int radiusmin, int radiusmax)
	{
		return findAroundPosition(loc.x, loc.y, loc.z, radiusmin, radiusmax, obj.getGeoIndex());
	}

	public static Location findAroundPosition(GameObject obj, int radiusmin, int radiusmax)
	{
		return findAroundPosition(obj, obj.getLoc(), radiusmin, radiusmax);
	}

	public static Location findAroundPosition(GameObject obj, int radius)
	{
		return findAroundPosition(obj, 0, radius);
	}

	/**
	 * Найти стабильную точку в пределах радиуса от начальной
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param radiusmin
	 * @param radiusmax
	 * @param geoIndex
	 * @return
	 */
	public static Location findPointToStay(int x, int y, int z, int radiusmin, int radiusmax, int geoIndex, int maxZDiff)
	{
		if(radiusmax <= 0 || radiusmax < radiusmin)
			return new Location(x, y, z);

		Location pos;
		int tempz;
		for(int i = 0; i < 100; i++)
		{
			pos = Location.coordsRandomize(x, y, z, 0, radiusmin, radiusmax);
			tempz = GeoEngine.getLowerHeight(pos.x, pos.y, pos.z, geoIndex);
			if(Math.abs(pos.z - tempz) < maxZDiff && GeoEngine.getLowerNSWE(pos.x, pos.y, tempz, geoIndex) == GeoEngine.NSWE_ALL)
			{
				pos.z = tempz;
				return pos;
			}
		}
		return new Location(x, y, z);
	}

	public static Location findPointToStay(int x, int y, int z, int radiusmin, int radiusmax, int geoIndex)
	{
		return findPointToStay(x, y, z, radiusmin, radiusmax, geoIndex, 200);
	}

	public static Location findPointToStay(Location loc, int radius, int geoIndex)
	{
		return findPointToStay(loc.x, loc.y, loc.z, 0, radius, geoIndex);
	}

	public static Location findPointToStay(Location loc, int radiusmin, int radiusmax, int geoIndex)
	{
		return findPointToStay(loc.x, loc.y, loc.z, radiusmin, radiusmax, geoIndex);
	}

	public static Location findPointToStay(GameObject obj, Location loc, int radiusmin, int radiusmax)
	{
		return findPointToStay(loc.x, loc.y, loc.z, radiusmin, radiusmax, obj.getGeoIndex());
	}

	public static Location findPointToStay(GameObject obj, int radiusmin, int radiusmax)
	{
		return findPointToStay(obj, obj.getLoc(), radiusmin, radiusmax);
	}

	public static Location findPointToStay(GameObject obj, int radius)
	{
		return findPointToStay(obj, 0, radius);
	}

	public static Location coordsRandomize(ILocation loc, int radiusmin, int radiusmax)
	{
		return coordsRandomize(loc.getX(), loc.getY(), loc.getZ(), loc.getHeading(), radiusmin, radiusmax);
	}

	public static Location coordsRandomize(int x, int y, int z, int heading, int radiusmin, int radiusmax)
	{
		if(radiusmax <= 0 || radiusmax < radiusmin)
			return new Location(x, y, z, heading);
		int radius = Rnd.get(radiusmin, radiusmax);
		double angle = Rnd.nextDouble() * 2 * Math.PI;
		return new Location((int) (x + radius * Math.cos(angle)), (int) (y + radius * Math.sin(angle)), z, heading);
	}

	public static Location findNearest(ILocation loc, Location[] locs)
	{
		Location defloc = null;
		for(Location l : locs)
		{
			if(defloc == null)
				defloc = l;
			else if(loc.getDistance(l) < loc.getDistance(defloc))
				defloc = l;
		}
		return defloc;
	}

	public static int getRandomHeading()
	{
		return Rnd.get(0xFFFF);
	}

	@Override
	public Location getRandomLoc(int geoIndex, boolean fly)
	{
		Location loc = clone();
		if(loc.h == -1)
			loc.h = getRandomHeading();
		return loc;
	}
}