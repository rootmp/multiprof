package l2s.gameserver.templates.item.data;

/**
 * @author Bonux
 */
public class ItemData
{
	private final int _id;
	private final long _count;
	protected int _enchant;

	public ItemData(int id, long count)
	{
		_id = id;
		_count = count;
		_enchant = -1;
	}

	public ItemData(int id, long count, int enchant)
	{
		_id = id;
		_count = count;
		_enchant = enchant;
	}

	public int getId()
	{
		return _id;
	}

	public long getCount()
	{
		return _count;
	}

	public int getEnchant()
	{
		return _enchant;
	}
}
