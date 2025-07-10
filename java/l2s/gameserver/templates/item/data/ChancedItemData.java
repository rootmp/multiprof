package l2s.gameserver.templates.item.data;

/**
 * @author Bonux
 */
public class ChancedItemData extends ItemData
{
	private final double _chance;

	public ChancedItemData(int id, long count, int enchant, double chance)
	{
		super(id, count, enchant);
		_chance = chance;
	}

	public ChancedItemData(int id, long count, double chance)
	{
		this(id, count, 0, chance);
	}

	public double getChance()
	{
		return _chance;
	}
}
