package l2s.gameserver.enums;

public enum WorldExchangeItemMainType
{
	ADENA(24),
	EQUIPMENT(0),
	ENCHANT(8),
	CONSUMABLE(20),
	COLLECTION(5);

	private final int _id;

	private WorldExchangeItemMainType(int id)
	{
		_id = id;
	}

	public int getId()
	{
		return _id;
	}

	public static WorldExchangeItemMainType getWorldExchangeItemMainType(int id)
	{
		for(WorldExchangeItemMainType type : values())
		{
			if(type.getId() == id)
			{ return type; }
		}
		return null;
	}
}
