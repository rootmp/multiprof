package l2s.gameserver.enums.privatestore;

public enum StoreItemType
{
	ALL(-1),
	EQUIPMENT(0),
	ARTIFACT(1),
	ENCHANT(2),
	CONSUMABLE(3),
	ETCTYPE(4),
	ETCTYPE2(255); //TODO?

	private final int _storeItemType;

	private StoreItemType(int storeItemType)
	{
		_storeItemType = storeItemType;
	}

	public int getValue()
	{
		return _storeItemType;
	}

	public static StoreItemType findById(int id)
	{
		for(StoreItemType storeItemType : StoreItemType.values())
		{
			if(storeItemType.getValue() != id)
				continue;
			return storeItemType;
		}
		return null;
	}

}
