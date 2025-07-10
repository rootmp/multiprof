package l2s.gameserver.templates.henna;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

/**
 * @author Bonux (bonuxq@gmail.com)
 * @date 04.11.2021
 **/
public class PotentialFee
{
	private final int step;
	private final int dailyCount;
	private final int itemId;
	private final int itemCount;
	private final List<Pair<Integer, Double>> expCounts = new ArrayList<>();

	public PotentialFee(int step, int dailyCount, int itemId, int itemCount)
	{
		this.step = step;
		this.dailyCount = dailyCount;
		this.itemId = itemId;
		this.itemCount = itemCount;
	}

	public int getStep()
	{
		return step;
	}

	public int getDailyCount()
	{
		return dailyCount;
	}

	public int getItemId()
	{
		return itemId;
	}

	public int getItemCount()
	{
		return itemCount;
	}

	public List<Pair<Integer, Double>> getExpCounts()
	{
		return expCounts;
	}

	public int getExpCount()
	{
		EnumeratedDistribution<Integer> distribution = new EnumeratedDistribution<>(expCounts);
		Integer count = distribution.sample();
		if (count != null)
			return count;
		return 0;
	}
}
