package l2s.gameserver.templates.item.data;

public class ItemData
{
	private final int _id;
	private final int _price_id;
	private final long _count;
	private final int _enchant;
	private final double _chance;

	public ItemData(int id, long count)
	{
		this(id, count, 0);
	}

	public ItemData(int id, long count, int enchant)
	{
		_id = id;
		_count = count;
		_enchant = enchant;
		_price_id = 0;
		_chance = 0;
	}

	public ItemData(int id, long count, double chance)
	{
		_id = id;
		_count = count;
		_enchant = 0;
		_price_id = 0;
		_chance = chance;
	}

	public ItemData(int id, int price_id, long count, int enchant)
	{
		_id = id;
		_count = count;
		_enchant = enchant;
		_price_id = price_id;
		_chance = 0;
	}

	public ItemData(int id, int count, int enchant, double chance)
	{
		_id = id;
		this._price_id = 0;
		_count = count;
		_enchant = enchant;
		_chance = chance;
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

	public int getPriceId()
	{
		return _price_id;
	}

	public double getChance()
	{
		return _chance;
	}

	@Override
	public String toString()
	{
		return "ItemData{" + "_id=" + _id + ", _price_id=" + _price_id + ", _count=" + _count + ", _enchant=" + _enchant + ", _chance=" + _chance + "}";
	}
}
