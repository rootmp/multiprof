package l2s.gameserver.enums.privatestore;

public enum StoreSubItemType
{
	ALL(-1),
	WEAPON(0),
	ARMOR(1),
	ACCESSARY(2),
	ETC_EQUIPMENT(3),
	ARTIFACT_B1(4),
	ARTIFACT_C1(5),
	ARTIFACT_D1(6),
	ARTIFACT_A1(7),
	ENCHANT_SCROLL(8),
	BLESS_ENCHANT_SCROLL(9),
	MULTIE_NCHANT_SCROLL(10),
	ANCIENTE_NCHANT_SCROLL(11),
	SPIRITSHOT(12),
	SOULSHOT(13),
	BUFF(14),
	VARIATION_STONE(15),
	DYE(16),
	SOUL_CRYSTAL(17),
	SKILL_BOOK(18),
	ETCENCHANT(19),
	POTION_AND_ETC_SCROLL(20),
	TICKET(21),
	CRAFT(22),
	INC_ENCHANT_PROP(23),
	ETC_SUBTYPE(24),
	ETC_SUBTYPE2(255);//TODO ??

	private final int _storeSubItemType;

	private StoreSubItemType(int storeSubItemType)
	{
		_storeSubItemType = storeSubItemType;
	}

	public int getValue()
	{
		return _storeSubItemType;
	}

	public static StoreSubItemType findById(int id)
	{
		for(StoreSubItemType storeSubItemType : StoreSubItemType.values())
		{
			if(storeSubItemType.getValue() != id)
				continue;
			return storeSubItemType;
		}
		return null;
	}
}