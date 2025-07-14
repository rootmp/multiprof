/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package l2s.commons.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Reworked and adapted for L2Scripts
 * @author Mobius - Hl4p3x
 */
public class Rnd
{
	private static final int MINIMUM_POSITIVE_INT = 1;
	private static final long MINIMUM_POSITIVE_LONG = 1L;

	// get random number from 0 to 1
	public static double get()
	{
		return ThreadLocalRandom.current().nextDouble();
	}

	/**
	 * Gets a random number from 0(inclusive) to n(exclusive)
	 *
	 * @param n The superior limit (exclusive)
	 * @return A number from 0 to n-1
	 */
	public static int get(int n)
	{
		return n <= 0 ? 0 : ThreadLocalRandom.current().nextInt(n);
	}

	public static long get(long n)
	{
		return (long) (ThreadLocalRandom.current().nextDouble() * n);
	}

	/**
	 * @param origin (int)
	 * @param bound (int)
	 * @return a random int value between the specified origin (inclusive) and the specified bound (inclusive).
	 */
	public static int get(int origin, int bound)
	{
		return origin >= bound ? origin : ThreadLocalRandom.current().nextInt(origin, bound == Integer.MAX_VALUE ? bound : bound + MINIMUM_POSITIVE_INT);
	}

	/**
	 * @param origin (long)
	 * @param bound (long)
	 * @return a random long value between the specified origin (inclusive) and the specified bound (inclusive).
	 */
	public static long get(long origin, long bound)
	{
		return origin >= bound ? origin : ThreadLocalRandom.current().nextLong(origin, bound == Long.MAX_VALUE ? bound : bound + MINIMUM_POSITIVE_LONG);
	}

	public static int nextInt()
	{
		return ThreadLocalRandom.current().nextInt();
	}

	public static double nextDouble()
	{
		return ThreadLocalRandom.current().nextDouble();
	}

	public static double nextGaussian()
	{
		return ThreadLocalRandom.current().nextGaussian();
	}

	public static boolean nextBoolean()
	{
		return ThreadLocalRandom.current().nextBoolean();
	}

	public static boolean chance(int chance)
	{
		return chance >= 1 && (chance > 99 || ThreadLocalRandom.current().nextInt(99) + 1 <= chance);
	}

	public static boolean chance(double chance)
	{
		return ThreadLocalRandom.current().nextDouble() <= chance / 100.;
	}

	public static <E> E get(E[] list)
	{
		if(list.length == 0)
		{ return null; }
		if(list.length == 1)
		{ return list[0]; }
		return list[get(list.length)];
	}

	public static int get(int[] list)
	{
		return list[get(list.length)];
	}

	public static <E> E get(List<E> list)
	{
		if(list.isEmpty())
		{ return null; }
		if(list.size() == 1)
		{ return list.get(0); }
		return list.get(get(list.size()));
	}
}