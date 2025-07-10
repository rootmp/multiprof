package l2s.gameserver.templates.item;

/**
 * @author nexvill
 */
public enum VariationType
{
	/* 0 */WEAPON,
	/* 1 */ARMOR,
	/* 2 */ARMOR_CHEST,
	/* 3 */ARMOR_LEGS,
	/* 4 */ARMOR_HELMET,
	/* 5 */ARMOR_GLOVES,
	/* 6 */ARMOR_BOOTS,
	/* 7 */ACCESSORY_STANDARD_RING,
	/* 8 */ACCESSORY_STANDARD_EARRING,
	/* 9 */ACCESSORY_STANDARD_NECKLACE,
	/* 10 */ACCESSORY_RARE_RING,
	/* 11 */ACCESSORY_RARE_EARRING,
	/* 12 */ACCESSORY_RARE_NECKLACE,
	/*
	 * 13
	 */HEROIC_CIRCLET;

	public static final VariationType[] VALUES = values();
}