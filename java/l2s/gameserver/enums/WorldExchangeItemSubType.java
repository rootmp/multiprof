package l2s.gameserver.enums;

public enum WorldExchangeItemSubType
{
	WEAPON(0),
	ARMOR(1),
	ACCESSORY(2),
	ETC(3),
	ARTIFACT_B1(4),
	ARTIFACT_C1(5),
	ARTIFACT_D1(6),
	ARTIFACT_A1(7),
	ENCHANT_SCROLL(8),
	BLESS_ENCHANT_SCROLL(9),
	MULTI_ENCHANT_SCROLL(10),
	ANCIENT_ENCHANT_SCROLL(11),
	SPIRITSHOT(12),
	SOULSHOT(13),
	BUFF(14),
	VARIATION_STONE(15),
	DYE(16),
	SOUL_CRYSTAL(17),
	SKILLBOOK(18),
	ETC_ENCHANT(19),
	POTION_AND_ETC_SCROLL(20),
	TICKET(21),
	CRAFT(22),
	INC_ENCHANT_PROP(23),
	ADENA(24),
	ETC_SUB_TYPE(25);
	
	private final int _id;
	
	private WorldExchangeItemSubType(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public static WorldExchangeItemSubType getWorldExchangeItemSubType(int id)
	{
		for (WorldExchangeItemSubType type : values())
		{
			if (type.getId() == id)
			{
				return type;
			}
		}

		System.out.println("WorldExchangeSortType sortType == null " + id);
		return null;
	}
}
