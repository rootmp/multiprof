package l2s.gameserver.templates.randomCraft;

/**
 * @author Mobius
 */
public class RandomCraftExtractData
{
	private final long _points;
	private final long _fee;

	public RandomCraftExtractData(long points, long fee)
	{
		_points = points;
		_fee = fee;
	}

	public long getPoints()
	{
		return _points;
	}

	public long getFee()
	{
		return _fee;
	}
}
