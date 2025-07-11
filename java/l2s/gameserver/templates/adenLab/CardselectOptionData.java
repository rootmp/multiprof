package l2s.gameserver.templates.adenLab;

public class CardselectOptionData
{
	private final int level;
	private final double chance;

	public CardselectOptionData(int level, double chance)
	{
		this.level = level;
		this.chance = chance;
	}

	public int getLevel()
	{
		return level;
	}

	public double getChance()
	{
		return chance;
	}
}
