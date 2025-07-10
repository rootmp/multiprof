
package l2s.gameserver.enums;

public enum PrivateStoreType
{
	STORE_PRIVATE_NONE(0),
	STORE_PRIVATE_SELL(1),
	STORE_PRIVATE_SELL_MANAGE(2),
	STORE_PRIVATE_BUY(3),
	STORE_PRIVATE_BUY_MANAGE(4),
	STORE_PRIVATE_MANUFACTURE(5),
	STORE_OBSERVING_GAMES(7),
	STORE_PRIVATE_SELL_PACKAGE(8);

	private final int _id;

	PrivateStoreType(int id)
	{
		_id = id;
	}

	public int getId()
	{
		return _id;
	}

	public static PrivateStoreType findById(int id)
	{
		for(PrivateStoreType storeType : PrivateStoreType.values())
		{
			if(storeType.getId() != id)
				continue;
			return storeType;
		}
		return null;
	}
}