package l2s.gameserver.utils;

import java.util.List;

import l2s.commons.geometry.GeometryUtils;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;

/**
 * @author VISTALL
 * @date 16:06/03.05.2011
 */
public class PositionUtils
{
	public enum TargetDirection
	{
		NONE,
		FRONT,
		SIDE,
		BEHIND
	}

	private static final int MAX_ANGLE = 360;

	private static final double FRONT_MAX_ANGLE = 100;
	private static final double BACK_MAX_ANGLE = 40;

	public static TargetDirection getDirectionTo(Creature target, Creature attacker)
	{
		if(target == null || attacker == null)
			return TargetDirection.NONE;
		if(isBehind(target, attacker))
			return TargetDirection.BEHIND;
		if(isInFrontOf(target, attacker))
			return TargetDirection.FRONT;
		return TargetDirection.SIDE;
	}

	/**
	 * Those are altered formulas for blow lands Return True if the target is IN
	 * FRONT of the L2Character.<BR>
	 * <BR>
	 */
	public static boolean isInFrontOf(Creature target, Creature attacker)
	{
		if(target == null)
			return false;

		double angleChar, angleTarget, angleDiff;
		angleTarget = calculateAngleFrom(target, attacker);
		angleChar = convertHeadingToDegree(target.getHeading());
		angleDiff = angleChar - angleTarget;
		if(angleDiff <= -MAX_ANGLE + FRONT_MAX_ANGLE)
			angleDiff += MAX_ANGLE;
		if(angleDiff >= MAX_ANGLE - FRONT_MAX_ANGLE)
			angleDiff -= MAX_ANGLE;
		if(Math.abs(angleDiff) <= FRONT_MAX_ANGLE)
			return true;
		return false;
	}

	/**
	 * Those are altered formulas for blow lands Return True if the L2Character is
	 * behind the target and can't be seen.<BR>
	 * <BR>
	 */
	public static boolean isBehind(Creature target, Creature attacker)
	{
		if(target == null)
			return false;

		double angleChar, angleTarget, angleDiff;
		angleChar = calculateAngleFrom(attacker, target);
		angleTarget = convertHeadingToDegree(target.getHeading());
		angleDiff = angleChar - angleTarget;
		if(angleDiff <= -MAX_ANGLE + BACK_MAX_ANGLE)
			angleDiff += MAX_ANGLE;
		if(angleDiff >= MAX_ANGLE - BACK_MAX_ANGLE)
			angleDiff -= MAX_ANGLE;
		if(Math.abs(angleDiff) <= BACK_MAX_ANGLE)
			return true;
		return false;
	}

	/** Returns true if target is in front of L2Character (shield def etc) */
	public static boolean isFacing(Creature attacker, GameObject target, int maxAngle)
	{
		double angleChar, angleTarget, angleDiff, maxAngleDiff;
		if(target == null)
			return false;
		if(maxAngle >= 360)
			return true;
		maxAngleDiff = maxAngle / 2;
		angleTarget = calculateAngleFrom(attacker, target);
		angleChar = convertHeadingToDegree(attacker.getHeading());
		angleDiff = angleChar - angleTarget;
		if(angleDiff <= -360 + maxAngleDiff)
			angleDiff += 360;
		if(angleDiff >= 360 - maxAngleDiff)
			angleDiff -= 360;
		if(Math.abs(angleDiff) <= maxAngleDiff)
			return true;
		return false;
	}

	public static int calculateHeadingFrom(GameObject obj1, GameObject obj2)
	{
		return calculateHeadingFrom(obj1.getX(), obj1.getY(), obj2.getX(), obj2.getY());
	}

	public static int calculateHeadingFrom(int obj1X, int obj1Y, int obj2X, int obj2Y)
	{
		double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
		if(angleTarget < 0)
			angleTarget = MAX_ANGLE + angleTarget;
		return (int) (angleTarget * 182.044444444);
	}

	public static double calculateAngleFrom(GameObject obj1, GameObject obj2)
	{
		return calculateAngleFrom(obj1.getX(), obj1.getY(), obj2.getX(), obj2.getY());
	}

	public static double calculateAngleFrom(int obj1X, int obj1Y, int obj2X, int obj2Y)
	{
		return GeometryUtils.calculateAngleFrom(obj1X, obj1Y, obj2X, obj2Y);
	}

	public static boolean checkIfInRange(int range, int x1, int y1, int x2, int y2)
	{
		return checkIfInRange(range, x1, y1, 0, x2, y2, 0, false);
	}

	public static boolean checkIfInRange(int range, int x1, int y1, int z1, int x2, int y2, int z2, boolean includeZAxis)
	{
		long dx = x1 - x2;
		long dy = y1 - y2;

		if(includeZAxis)
		{
			long dz = z1 - z2;
			return dx * dx + dy * dy + dz * dz <= range * range;
		}
		return dx * dx + dy * dy <= range * range;
	}

	public static boolean checkIfInRange(int range, GameObject obj1, GameObject obj2, boolean includeZAxis)
	{
		if(obj1 == null || obj2 == null)
			return false;
		return checkIfInRange(range, obj1.getX(), obj1.getY(), obj1.getZ(), obj2.getX(), obj2.getY(), obj2.getZ(), includeZAxis);
	}

	public static double convertHeadingToDegree(int heading)
	{
		return heading / 182.044444444;
	}

	public static double convertHeadingToRadian(int heading)
	{
		return Math.toRadians(convertHeadingToDegree(heading) - 90);
	}

	public static int convertDegreeToClientHeading(double degree)
	{
		if(degree < 0)
			degree = 360 + degree;
		return (int) (degree * 182.044444444);
	}

	public static int calculateDistance(int x1, int y1, int x2, int y2)
	{
		return GeometryUtils.calculateDistance(x1, y1, x2, y2);
	}

	public static int calculateDistance(int x1, int y1, int z1, int x2, int y2, int z2, boolean includeZAxis)
	{
		return GeometryUtils.calculateDistance(x1, y1, z1, x2, y2, z2, includeZAxis);
	}

	public static int calculateDistance(GameObject obj1, GameObject obj2, boolean includeZAxis)
	{
		if(obj1 == null || obj2 == null)
			return Integer.MAX_VALUE;
		return calculateDistance(obj1.getX(), obj1.getY(), obj1.getZ(), obj2.getX(), obj2.getY(), obj2.getZ(), includeZAxis);
	}

	public static int getDistance(GameObject a1, GameObject a2)
	{
		return getDistance(a1.getX(), a2.getY(), a2.getX(), a2.getY());
	}

	public static int getDistance(Location loc1, Location loc2)
	{
		return getDistance(loc1.getX(), loc1.getY(), loc2.getX(), loc2.getY());
	}

	public static int getDistance(int x1, int y1, int x2, int y2)
	{
		return (int) Math.hypot(x1 - x2, y1 - y2);
	}

	public static int getHeadingTo(GameObject actor, GameObject target)
	{
		if(actor == null || target == null || target == actor)
			return -1;
		return getHeadingTo(actor.getLoc(), target.getLoc());
	}

	public static int getHeadingTo(Location actor, Location target)
	{
		if(actor == null || target == null || target.equals(actor))
			return -1;

		int dx = target.x - actor.x;
		int dy = target.y - actor.y;
		int heading = target.h - (int) (Math.atan2(-dy, -dx) * Creature.HEADINGS_IN_PI + 32768);

		if(heading < 0)
			heading = heading + 1 + Integer.MAX_VALUE & 0xFFFF;
		else if(heading > 0xFFFF)
			heading &= 0xFFFF;

		return heading;
	}

	public static Location applyOffset(Creature activeChar, Location point, int offset)
	{
		if(offset <= 0)
			return point;

		long dx = point.x - activeChar.getX();
		long dy = point.y - activeChar.getY();
		long dz = point.z - activeChar.getZ();

		double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

		if(distance <= offset)
		{
			point.set(activeChar.getX(), activeChar.getY(), activeChar.getZ());
			return point;
		}

		if(distance >= 1)
		{
			double cut = offset / distance;
			point.x -= (int) (dx * cut + 0.5);
			point.y -= (int) (dy * cut + 0.5);
			point.z -= (int) (dz * cut + 0.5);

			if(!activeChar.isFlying() && !activeChar.isInBoat() && !activeChar.isInWater() && !activeChar.isBoat())
				point.correctGeoZ(activeChar.getGeoIndex());
		}

		return point;
	}

	public static List<Location> applyOffset(List<Location> points, int offset)
	{
		offset = offset >> 4;
		if(offset <= 0)
			return points;

		long dx = points.get(points.size() - 1).x - points.get(0).x;
		long dy = points.get(points.size() - 1).y - points.get(0).y;
		long dz = points.get(points.size() - 1).z - points.get(0).z;

		double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
		if(distance <= offset)
		{
			Location point = points.get(0);
			points.clear();
			points.add(point);
			return points;
		}

		if(distance >= 1)
		{
			double cut = offset / distance;
			int num = (int) (points.size() * cut + 0.5);
			for(int i = 1; i <= num && points.size() > 0; i++)
				points.remove(points.size() - 1);
		}

		return points;
	}
}
