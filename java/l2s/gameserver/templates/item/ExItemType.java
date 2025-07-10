package l2s.gameserver.templates.item;

public enum ExItemType
{
	/* 0 */NONE_0(-1),

	// Оружие
	/* 1 */SWORD(0), // Одноручный меч
	/* 2 */MAGIC_SWORD(0), // Магический одноручный меч
	/* 3 */DAGGER(0), // Кинжал
	/* 4 */RAPIER(0), // Рапира
	/* 5 */BIG_SWORD(0), // Двуручный меч
	/* 6 */ANCIENT_SWORD(0), // Древний меч
	/* 7 */DUAL_SWORD(0), // Парные клинки
	/* 8 */DUAL_DAGGER(0), // Парные кинжалы
	/* 9 */BLUNT_WEAPON(0), // Одноручное дробящее
	/* 10 */MAGIC_BLUNT_WEAPON(0), // Одноручное магическое дробящее
	/* 11 */BIG_BLUNT_WEAPON(0), // Двуручное дробящее
	/* 12 */BIG_MAGIC_BLUNT_WEAPON(0), // Двуручное магическое дробящее
	/* 13 */DUAL_BLUNT_WEAPON(0), // Парное дробящее
	/* 14 */BOW(0), // Лук
	/* 15 */CROSSBOW(0), // Арбалет
	/* 16 */HAND_TO_HAND(0), // Кастеты
	/* 17 */POLE(0), // Древковые
	/* 18 */FIREARMS(0), // Pistols
	/* 19 */OTHER_WEAPON(0), // Другое оружие

	// Доспехи
	/* 20 */HELMET(1), // Шлем
	/* 21 */UPPER_PIECE(1), // Верхняя часть доспехов
	/* 22 */LOWER_PIECE(1), // Нижняя часть доспехов
	/* 23 */FULL_BODY(1), // Костюм
	/* 24 */GLOVES(1), // Перчатки
	/* 25 */FEET(1), // Обувь
	/* 26 */SHIELD(1), // Щит
	/* 27 */SIGIL(1), // Символ
	/* 28 */PENDANT(1), // Подвеска
	/* 29 */CLOAK(1), // Плащ

	// Аксессуары
	/* 30 */RING(2), // Кольцо
	/* 31 */EARRING(2), // Серьга
	/* 32 */NECKLACE(2), // Ожерелье
	/* 33 */BELT(2), // Пояс
	/* 34 */BRACELET(2), // Браслет
	/* 35 */AGATHION(2), // Браслет
	/* 36 */HAIR_ACCESSORY(2), // Головной убор

	// Припасы
	/* 37 */POTION(3), // Зелье
	/* 38 */SCROLL_ENCHANT_WEAPON(3), // Свиток: Модифицировать Оружие
	/* 39 */SCROLL_ENCHANT_ARMOR(3), // Свиток: Модифицировать Доспех
	/* 40 */SCROLL_OTHER(3), // Другой свиток
	/* 41 */SOULSHOT(3), // Заряды Душ
	/* 42 */SPIRITSHOT(3), // Заряды Духа
	/* 43 */NONE_41(-1),

	// Для питомцев
	/* 44 */PET_EQUIPMENT(4), // Доспехи питомца
	/* 45 */PET_SUPPLIES(4), // Припасы питомца

	// Остальное
	/* 46 */CRYSTAL(5), // Кристалл
	/* 47 */RECIPE(5), // Рецепт
	/* 48 */CRAFTING_MAIN_INGRIDIENTS(5), // Основные материалы для изготовления предметов
	/* 49 */LIFE_STONE(5), // Камень Жизни
	/* 50 */SOUL_CRYSTAL(5), // Кристалл Души
	/* 51 */ATTRIBUTE_STONE(5), // Кристалл Стихии
	/* 52 */WEAPON_ENCHANT_STONE(5), // Камень: Модифицировать Оружие
	/* 53 */ARMOR_ENCHANT_STONE(5), // Камень: Модифицировать Доспех
	/* 54 */SPELLBOOK(5), // Книга заклинаний
	/* 55 */GEMSTONE(5), // Самоцветы
	/* 56 */POUCH(5), // Кошель
	/* 57 */PIN(5), // Заколка
	/* 58 */MAGIC_RUNE_CLIP(5), // Магическая Заколка
	/* 59 */MAGIC_ORNAMENT(5), // Магическое Украшение
	/* 60 */DYES(5), // Цвет
	/* 61 */OTHER_ITEMS(5); // Другие Предметы

	public static final ExItemType[] VALUES = values();

	private int _mask;

	ExItemType(int mask)
	{
		_mask = mask;
	}

	public int mask()
	{
		return _mask;
	}
}