package l2s.gameserver.enums;

public enum CapsuledItemType
{
	FIXED(0),
	RND(1),
	CHANCE(2),
	DELAY_FIXED(3),
	DELAY_RND(4),
	CAPSULED_ITEMS(5),
	CREATE_ITEMS(6),
	;

	private final int _id;

	CapsuledItemType(int id)
	{
		_id = id;
	}

	public static CapsuledItemType findById(int id)
	{
		for(CapsuledItemType type : CapsuledItemType.values())
		{
			if(type._id == id)
				return type;
		}
		return null;
	}
}