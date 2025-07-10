package l2s.gameserver.enums.privatestore;

public enum StoreType
{
	SELL(0),
	BUY(1),
	WHOLESALE(2),
	ALL_STORE_TYPE(3);

	private final int _storeType;

	private StoreType(int storeType)
	{
		_storeType = storeType;
	}

	public int getValue()
	{
		return _storeType;
	}

	public static StoreType findById(int id)
	{
		for(StoreType storeType : StoreType.values())
		{
			if(storeType.getValue() != id)
				continue;
			return storeType;
		}
		return null;
	}

}