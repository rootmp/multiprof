package l2s.gameserver.model.base;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.ClassDataHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.templates.player.ClassData;

public enum ClassId
{
	/* Human Fighter 1st and 2nd class list */
	/* 0 */HUMAN_FIGHTER(ClassType.FIGHTER, Race.HUMAN, null, ClassLevel.NONE, null),
	/* 1 */WARRIOR(ClassType.FIGHTER, Race.HUMAN, HUMAN_FIGHTER, ClassLevel.FIRST, null, 1145),
	/* 2 */GLADIATOR(ClassType.FIGHTER, Race.HUMAN, WARRIOR, ClassLevel.SECOND, ClassType2.WARRIOR, 2627, 2734, 2762),
	/* 3 */WARLORD(ClassType.FIGHTER, Race.HUMAN, WARRIOR, ClassLevel.SECOND, ClassType2.WARRIOR, 2627, 2734, 3276),
	/*
	 * 4
	 */KNIGHT(ClassType.FIGHTER, Race.HUMAN, HUMAN_FIGHTER, ClassLevel.FIRST, null, 1161),
	/*
	 * 5
	 */PALADIN(ClassType.FIGHTER, Race.HUMAN, KNIGHT, ClassLevel.SECOND, ClassType2.KNIGHT, 2633, 2734, 2820),
	/*
	 * 6
	 */DARK_AVENGER(ClassType.FIGHTER, Race.HUMAN, KNIGHT, ClassLevel.SECOND, ClassType2.KNIGHT, 2633, 2734, 3307),
	/*
	 * 7
	 */ROGUE(ClassType.FIGHTER, Race.HUMAN, HUMAN_FIGHTER, ClassLevel.FIRST, null, 1190),
	/*
	 * 8
	 */TREASURE_HUNTER(ClassType.FIGHTER, Race.HUMAN, ROGUE, ClassLevel.SECOND, ClassType2.ROGUE, 2673, 2734, 2809),
	/*
	 * 9
	 */HAWKEYE(ClassType.FIGHTER, Race.HUMAN, ROGUE, ClassLevel.SECOND, ClassType2.ARCHER, 2673, 2734, 3293),

	/* Human Mage 1st and 2nd class list */
	/* 10 */HUMAN_MAGE(ClassType.MYSTIC, Race.HUMAN, null, ClassLevel.NONE, null),
	/* 11 */WIZARD(ClassType.MYSTIC, Race.HUMAN, HUMAN_MAGE, ClassLevel.FIRST, null, 1292),
	/* 12 */SORCERER(ClassType.MYSTIC, Race.HUMAN, WIZARD, ClassLevel.SECOND, ClassType2.WIZARD, 2674, 2734, 2840),
	/* 13 */NECROMANCER(ClassType.MYSTIC, Race.HUMAN, WIZARD, ClassLevel.SECOND, ClassType2.WIZARD, 2674, 2734, 3307),
	/*
	 * 14
	 */WARLOCK(ClassType.MYSTIC, Race.HUMAN, WIZARD, ClassLevel.SECOND, ClassType2.SUMMONER, 2674, 2734, 3336),
	/*
	 * 15
	 */CLERIC(ClassType.MYSTIC, Race.HUMAN, HUMAN_MAGE, ClassLevel.FIRST, null, 1201),
	/*
	 * 16
	 */BISHOP(ClassType.MYSTIC, Race.HUMAN, CLERIC, ClassLevel.SECOND, ClassType2.HEALER, 2721, 2734, 2820),
	/*
	 * 17
	 */PROPHET(ClassType.MYSTIC, Race.HUMAN, CLERIC, ClassLevel.SECOND, ClassType2.ENCHANTER, 2721, 2734, 2821),

	/* Elven Fighter 1st and 2nd class list */
	/* 18 */ELVEN_FIGHTER(ClassType.FIGHTER, Race.ELF, null, ClassLevel.NONE, null),
	/* 19 */ELVEN_KNIGHT(ClassType.FIGHTER, Race.ELF, ELVEN_FIGHTER, ClassLevel.FIRST, null, 1204),
	/* 20 */TEMPLE_KNIGHT(
							ClassType.FIGHTER,
							Race.ELF,
							ELVEN_KNIGHT,
							ClassLevel.SECOND,
							ClassType2.KNIGHT,
							2633,
							3140,
							2820),
	/* 21 */SWORDSINGER(
						ClassType.FIGHTER,
						Race.ELF,
						ELVEN_KNIGHT,
						ClassLevel.SECOND,
						ClassType2.ENCHANTER,
						2627,
						3140,
						2762),
	/*
	 * 22
	 */ELVEN_SCOUT(ClassType.FIGHTER, Race.ELF, ELVEN_FIGHTER, ClassLevel.FIRST, null, 1217),
	/*
	 * 23
	 */PLAIN_WALKER(ClassType.FIGHTER, Race.ELF, ELVEN_SCOUT, ClassLevel.SECOND, ClassType2.ROGUE, 2673, 3140, 2809),
	/*
	 * 24
	 */SILVER_RANGER(ClassType.FIGHTER, Race.ELF, ELVEN_SCOUT, ClassLevel.SECOND, ClassType2.ARCHER, 2673, 3140, 3293),

	/* Elven Mage 1st and 2nd class list */
	/* 25 */ELVEN_MAGE(ClassType.MYSTIC, Race.ELF, null, ClassLevel.NONE, null),
	/* 26 */ELVEN_WIZARD(ClassType.MYSTIC, Race.ELF, ELVEN_MAGE, ClassLevel.FIRST, null, 1230),
	/* 27 */SPELLSINGER(
						ClassType.MYSTIC,
						Race.ELF,
						ELVEN_WIZARD,
						ClassLevel.SECOND,
						ClassType2.WIZARD,
						2674,
						3140,
						2840),
	/* 28 */ELEMENTAL_SUMMONER(
								ClassType.MYSTIC,
								Race.ELF,
								ELVEN_WIZARD,
								ClassLevel.SECOND,
								ClassType2.SUMMONER,
								2674,
								3140,
								3336),
	/*
	 * 29
	 */ORACLE(ClassType.MYSTIC, Race.ELF, ELVEN_MAGE, ClassLevel.FIRST, null, 1235),
	/*
	 * 30
	 */ELDER(ClassType.MYSTIC, Race.ELF, ORACLE, ClassLevel.SECOND, ClassType2.HEALER, 2721, 3140, 2820),

	/* Darkelf Fighter 1st and 2nd class list */
	/* 31 */DARK_FIGHTER(ClassType.FIGHTER, Race.DARKELF, null, ClassLevel.NONE, null),
	/* 32 */PALUS_KNIGHT(ClassType.FIGHTER, Race.DARKELF, DARK_FIGHTER, ClassLevel.FIRST, null, 1244),
	/* 33 */SHILLEN_KNIGHT(
							ClassType.FIGHTER,
							Race.DARKELF,
							PALUS_KNIGHT,
							ClassLevel.SECOND,
							ClassType2.KNIGHT,
							2633,
							3172,
							3307),
	/*
	 * 34
	 */BLADEDANCER(
					ClassType.FIGHTER,
					Race.DARKELF,
					PALUS_KNIGHT,
					ClassLevel.SECOND,
					ClassType2.ENCHANTER,
					2627,
					3172,
					2762),
	/*
	 * 35
	 */ASSASIN(ClassType.FIGHTER, Race.DARKELF, DARK_FIGHTER, ClassLevel.FIRST, null, 1252),
	/*
	 * 36
	 */ABYSS_WALKER(ClassType.FIGHTER, Race.DARKELF, ASSASIN, ClassLevel.SECOND, ClassType2.ROGUE, 2673, 3172, 2809),
	/*
	 * 37
	 */PHANTOM_RANGER(ClassType.FIGHTER, Race.DARKELF, ASSASIN, ClassLevel.SECOND, ClassType2.ARCHER, 2673, 3172, 3293),

	/* Darkelf Mage 1st and 2nd class list */
	/* 38 */DARK_MAGE(ClassType.MYSTIC, Race.DARKELF, null, ClassLevel.NONE, null),
	/* 39 */DARK_WIZARD(ClassType.MYSTIC, Race.DARKELF, DARK_MAGE, ClassLevel.FIRST, null, 1261),
	/* 40 */SPELLHOWLER(
						ClassType.MYSTIC,
						Race.DARKELF,
						DARK_WIZARD,
						ClassLevel.SECOND,
						ClassType2.WIZARD,
						2674,
						3172,
						2840),
	/* 41 */PHANTOM_SUMMONER(
								ClassType.MYSTIC,
								Race.DARKELF,
								DARK_WIZARD,
								ClassLevel.SECOND,
								ClassType2.SUMMONER,
								2674,
								3172,
								3336),
	/*
	 * 42
	 */SHILLEN_ORACLE(ClassType.MYSTIC, Race.DARKELF, DARK_MAGE, ClassLevel.FIRST, null, 1270),
	/*
	 * 43
	 */SHILLEN_ELDER(
						ClassType.MYSTIC,
						Race.DARKELF,
						SHILLEN_ORACLE,
						ClassLevel.SECOND,
						ClassType2.HEALER,
						2721,
						3172,
						2821),

	/* Orc Fighter 1st and 2nd class list */
	/* 44 */ORC_FIGHTER(ClassType.FIGHTER, Race.ORC, null, ClassLevel.NONE, null),
	/* 45 */ORC_RAIDER(ClassType.FIGHTER, Race.ORC, ORC_FIGHTER, ClassLevel.FIRST, null, 1592),
	/* 46 */DESTROYER(ClassType.FIGHTER, Race.ORC, ORC_RAIDER, ClassLevel.SECOND, ClassType2.WARRIOR, 2627, 3203, 3276),
	/* 47 */ORC_MONK(ClassType.FIGHTER, Race.ORC, ORC_FIGHTER, ClassLevel.FIRST, null, 1615),
	/*
	 * 48
	 */TYRANT(ClassType.FIGHTER, Race.ORC, ORC_MONK, ClassLevel.SECOND, ClassType2.WARRIOR, 2627, 3203, 2762),

	/* Orc Mage 1st and 2nd class list */
	/* 49 */ORC_MAGE(ClassType.MYSTIC, Race.ORC, null, ClassLevel.NONE, null),
	/* 50 */ORC_SHAMAN(ClassType.MYSTIC, Race.ORC, ORC_MAGE, ClassLevel.FIRST, null, 1631),
	/* 51 */OVERLORD(ClassType.MYSTIC, Race.ORC, ORC_SHAMAN, ClassLevel.SECOND, ClassType2.ENCHANTER, 2721, 3203, 3390),
	/* 52 */WARCRYER(ClassType.MYSTIC, Race.ORC, ORC_SHAMAN, ClassLevel.SECOND, ClassType2.ENCHANTER, 2721, 3203, 2879),

	/* Dwarf Fighter 1st and 2nd class list */
	/* 53 */DWARVEN_FIGHTER(ClassType.FIGHTER, Race.DWARF, null, ClassLevel.NONE, null),
	/* 54 */SCAVENGER(ClassType.FIGHTER, Race.DWARF, DWARVEN_FIGHTER, ClassLevel.FIRST, null, 1642),
	/* 55 */BOUNTY_HUNTER(
							ClassType.FIGHTER,
							Race.DWARF,
							SCAVENGER,
							ClassLevel.SECOND,
							ClassType2.ROGUE,
							3119,
							3238,
							2809),
	/* 56 */ARTISAN(ClassType.FIGHTER, Race.DWARF, DWARVEN_FIGHTER, ClassLevel.FIRST, null, 1635),
	/*
	 * 57
	 */WARSMITH(ClassType.FIGHTER, Race.DWARF, ARTISAN, ClassLevel.SECOND, ClassType2.WARRIOR, 3119, 3238, 2867),

	/* Dummy Entries */
	/* 58 */DUMMY_ENTRY_58,
	/* 59 */DUMMY_ENTRY_59,
	/* 60 */DUMMY_ENTRY_60,
	/* 61 */DUMMY_ENTRY_61,
	/* 62 */DUMMY_ENTRY_62,
	/* 63 */DUMMY_ENTRY_63,
	/* 64 */DUMMY_ENTRY_64,
	/* 65 */DUMMY_ENTRY_65,
	/* 66 */DUMMY_ENTRY_66,
	/* 67 */DUMMY_ENTRY_67,
	/* 68 */DUMMY_ENTRY_68,
	/* 69 */DUMMY_ENTRY_69,
	/* 70 */DUMMY_ENTRY_70,
	/*
	 * 71
	 */DUMMY_ENTRY_71,
	/*
	 * 72
	 */DUMMY_ENTRY_72,
	/*
	 * 73
	 */DUMMY_ENTRY_73,
	/*
	 * 74
	 */DUMMY_ENTRY_74,
	/*
	 * 75
	 */DUMMY_ENTRY_75,
	/*
	 * 76
	 */DUMMY_ENTRY_76,
	/*
	 * 77
	 */DUMMY_ENTRY_77,
	/*
	 * 78
	 */DUMMY_ENTRY_78,
	/*
	 * 79
	 */DUMMY_ENTRY_79,
	/*
	 * 80
	 */DUMMY_ENTRY_80,
	/*
	 * 81
	 */DUMMY_ENTRY_81,
	/*
	 * 82
	 */DUMMY_ENTRY_82,
	/*
	 * 83
	 */DUMMY_ENTRY_83,
	/*
	 * 84
	 */DUMMY_ENTRY_84,
	/*
	 * 85
	 */DUMMY_ENTRY_85,
	/*
	 * 86
	 */DUMMY_ENTRY_86,
	/*
	 * 87
	 */DUMMY_ENTRY_87,

	/* Human Fighter 3th class list */
	/* 88 */DUELIST(ClassType.FIGHTER, Race.HUMAN, GLADIATOR, ClassLevel.THIRD, ClassType2.WARRIOR),
	/* 89 */DREADNOUGHT(ClassType.FIGHTER, Race.HUMAN, WARLORD, ClassLevel.THIRD, ClassType2.WARRIOR),
	/* 90 */PHOENIX_KNIGHT(ClassType.FIGHTER, Race.HUMAN, PALADIN, ClassLevel.THIRD, ClassType2.KNIGHT),
	/* 91 */HELL_KNIGHT(ClassType.FIGHTER, Race.HUMAN, DARK_AVENGER, ClassLevel.THIRD, ClassType2.KNIGHT),
	/*
	 * 92
	 */SAGITTARIUS(ClassType.FIGHTER, Race.HUMAN, HAWKEYE, ClassLevel.THIRD, ClassType2.ARCHER),
	/*
	 * 93
	 */ADVENTURER(ClassType.FIGHTER, Race.HUMAN, TREASURE_HUNTER, ClassLevel.THIRD, ClassType2.ROGUE),

	/* Human Mage 3th class list */
	/* 94 */ARCHMAGE(ClassType.MYSTIC, Race.HUMAN, SORCERER, ClassLevel.THIRD, ClassType2.WIZARD),
	/* 95 */SOULTAKER(ClassType.MYSTIC, Race.HUMAN, NECROMANCER, ClassLevel.THIRD, ClassType2.WIZARD),
	/* 96 */ARCANA_LORD(ClassType.MYSTIC, Race.HUMAN, WARLOCK, ClassLevel.THIRD, ClassType2.SUMMONER),
	/* 97 */CARDINAL(ClassType.MYSTIC, Race.HUMAN, BISHOP, ClassLevel.THIRD, ClassType2.HEALER),
	/*
	 * 98
	 */HIEROPHANT(ClassType.MYSTIC, Race.HUMAN, PROPHET, ClassLevel.THIRD, ClassType2.ENCHANTER),

	/* Elven Fighter 3th class list */
	/* 99 */EVAS_TEMPLAR(ClassType.FIGHTER, Race.ELF, TEMPLE_KNIGHT, ClassLevel.THIRD, ClassType2.KNIGHT),
	/* 100 */SWORD_MUSE(ClassType.FIGHTER, Race.ELF, SWORDSINGER, ClassLevel.THIRD, ClassType2.ENCHANTER),
	/* 101 */WIND_RIDER(ClassType.FIGHTER, Race.ELF, PLAIN_WALKER, ClassLevel.THIRD, ClassType2.ROGUE),
	/* 102 */MOONLIGHT_SENTINEL(ClassType.FIGHTER, Race.ELF, SILVER_RANGER, ClassLevel.THIRD, ClassType2.ARCHER),

	/* Elven Mage 3th class list */
	/* 103 */MYSTIC_MUSE(ClassType.MYSTIC, Race.ELF, SPELLSINGER, ClassLevel.THIRD, ClassType2.WIZARD),
	/* 104 */ELEMENTAL_MASTER(ClassType.MYSTIC, Race.ELF, ELEMENTAL_SUMMONER, ClassLevel.THIRD, ClassType2.SUMMONER),
	/* 105 */EVAS_SAINT(ClassType.MYSTIC, Race.ELF, ELDER, ClassLevel.THIRD, ClassType2.HEALER),

	/* Darkelf Fighter 3th class list */
	/* 106 */SHILLIEN_TEMPLAR(ClassType.FIGHTER, Race.DARKELF, SHILLEN_KNIGHT, ClassLevel.THIRD, ClassType2.KNIGHT),
	/* 107 */SPECTRAL_DANCER(ClassType.FIGHTER, Race.DARKELF, BLADEDANCER, ClassLevel.THIRD, ClassType2.ENCHANTER),
	/* 108 */GHOST_HUNTER(ClassType.FIGHTER, Race.DARKELF, ABYSS_WALKER, ClassLevel.THIRD, ClassType2.ROGUE),
	/*
	 * 109
	 */GHOST_SENTINEL(ClassType.FIGHTER, Race.DARKELF, PHANTOM_RANGER, ClassLevel.THIRD, ClassType2.ARCHER),

	/* Darkelf Mage 3th class list */
	/* 110 */STORM_SCREAMER(ClassType.MYSTIC, Race.DARKELF, SPELLHOWLER, ClassLevel.THIRD, ClassType2.WIZARD),
	/* 111 */SPECTRAL_MASTER(ClassType.MYSTIC, Race.DARKELF, PHANTOM_SUMMONER, ClassLevel.THIRD, ClassType2.SUMMONER),
	/* 112 */SHILLIEN_SAINT(ClassType.MYSTIC, Race.DARKELF, SHILLEN_ELDER, ClassLevel.THIRD, ClassType2.HEALER),

	/* Orc Fighter 3th class list */
	/* 113 */TITAN(ClassType.FIGHTER, Race.ORC, DESTROYER, ClassLevel.THIRD, ClassType2.WARRIOR),
	/* 114 */GRAND_KHAVATARI(ClassType.FIGHTER, Race.ORC, TYRANT, ClassLevel.THIRD, ClassType2.WARRIOR),

	/* Orc Mage 3th class list */
	/* 115 */DOMINATOR(ClassType.MYSTIC, Race.ORC, OVERLORD, ClassLevel.THIRD, ClassType2.ENCHANTER),
	/* 116 */DOOMCRYER(ClassType.MYSTIC, Race.ORC, WARCRYER, ClassLevel.THIRD, ClassType2.ENCHANTER),

	/* Dwarf Fighter 3th class list */
	/* 117 */FORTUNE_SEEKER(ClassType.FIGHTER, Race.DWARF, BOUNTY_HUNTER, ClassLevel.THIRD, ClassType2.ROGUE),
	/* 118 */MAESTRO(ClassType.FIGHTER, Race.DWARF, WARSMITH, ClassLevel.THIRD, ClassType2.WARRIOR),

	/* Kamael class list */
	// TODO: Проверить ClassType2 профессий.
	/* 119 */KAMAEL_SOLDIER(192, ClassType.FIGHTER, Race.KAMAEL, null, ClassLevel.NONE, null),
	/* 120 */TROOPER(125, ClassType.FIGHTER, Race.KAMAEL, KAMAEL_SOLDIER, ClassLevel.FIRST, null),
	/* 121 */SOUL_FINDER(193, ClassType.FIGHTER, Race.KAMAEL, KAMAEL_SOLDIER, ClassLevel.FIRST, null),
	/* 122 */WARDER(126, ClassType.FIGHTER, Race.KAMAEL, KAMAEL_SOLDIER, ClassLevel.FIRST, null),
	/*
	 * 123
	 */BERSERKER(127, ClassType.FIGHTER, Race.KAMAEL, TROOPER, ClassLevel.SECOND, ClassType2.WARRIOR),
	/*
	 * 124
	 */SOUL_BREAKER(194, ClassType.FIGHTER, Race.KAMAEL, SOUL_FINDER, ClassLevel.SECOND, ClassType2.ROGUE),
	/*
	 * 125
	 */SOUL_RANGER(130, ClassType.FIGHTER, Race.KAMAEL, WARDER, ClassLevel.SECOND, ClassType2.ARCHER),
	/*
	 * 126
	 */DOOMBRINGER(131, ClassType.FIGHTER, Race.KAMAEL, BERSERKER, ClassLevel.THIRD, ClassType2.WARRIOR),
	/*
	 * 127
	 */SOUL_HOUND(195, ClassType.FIGHTER, Race.KAMAEL, SOUL_BREAKER, ClassLevel.THIRD, ClassType2.ROGUE),
	/*
	 * 128
	 */TRICKSTER(134, ClassType.FIGHTER, Race.KAMAEL, SOUL_RANGER, ClassLevel.THIRD, ClassType2.ARCHER),

	/* Human Death Knights */
	/* 129 */H_DEATH_PILGRIM(196, ClassType.DEATH_KNIGHT, Race.HUMAN, null, ClassLevel.NONE, ClassType2.DEATH_KNIGHT),
	/* 130 */H_DEATH_BLADE(
							197,
							ClassType.DEATH_KNIGHT,
							Race.HUMAN,
							H_DEATH_PILGRIM,
							ClassLevel.FIRST,
							ClassType2.DEATH_KNIGHT),
	/* 131 */H_DEATH_MESSENGER(
								198,
								ClassType.DEATH_KNIGHT,
								Race.HUMAN,
								H_DEATH_BLADE,
								ClassLevel.SECOND,
								ClassType2.DEATH_KNIGHT),
	/*
	 * 132
	 */H_DEATH_KNIGHT(
						199,
						ClassType.DEATH_KNIGHT,
						Race.HUMAN,
						H_DEATH_MESSENGER,
						ClassLevel.THIRD,
						ClassType2.DEATH_KNIGHT),

	/* Elf Death Knights */
	/* 133 */E_DEATH_PILGRIM(200, ClassType.DEATH_KNIGHT, Race.ELF, null, ClassLevel.NONE, ClassType2.DEATH_KNIGHT),
	/* 134 */E_DEATH_BLADE(
							201,
							ClassType.DEATH_KNIGHT,
							Race.ELF,
							E_DEATH_PILGRIM,
							ClassLevel.FIRST,
							ClassType2.DEATH_KNIGHT),
	/* 135 */E_DEATH_MESSENGER(
								202,
								ClassType.DEATH_KNIGHT,
								Race.ELF,
								E_DEATH_BLADE,
								ClassLevel.SECOND,
								ClassType2.DEATH_KNIGHT),
	/*
	 * 136
	 */E_DEATH_KNIGHT(
						203,
						ClassType.DEATH_KNIGHT,
						Race.ELF,
						E_DEATH_MESSENGER,
						ClassLevel.THIRD,
						ClassType2.DEATH_KNIGHT),

	/* Dark Elf Death Knights */
	/* 137 */DE_DEATH_PILGRIM(
								204,
								ClassType.DEATH_KNIGHT,
								Race.DARKELF,
								null,
								ClassLevel.NONE,
								ClassType2.DEATH_KNIGHT),
	/* 138 */DE_DEATH_BLADE(
							205,
							ClassType.DEATH_KNIGHT,
							Race.DARKELF,
							DE_DEATH_PILGRIM,
							ClassLevel.FIRST,
							ClassType2.DEATH_KNIGHT),
	/* 139 */DE_DEATH_MESSENGER(
								206,
								ClassType.DEATH_KNIGHT,
								Race.DARKELF,
								DE_DEATH_BLADE,
								ClassLevel.SECOND,
								ClassType2.DEATH_KNIGHT),
	/*
	 * 140
	 */DE_DEATH_KNIGHT(
						207,
						ClassType.DEATH_KNIGHT,
						Race.DARKELF,
						DE_DEATH_MESSENGER,
						ClassLevel.THIRD,
						ClassType2.DEATH_KNIGHT),

	/* Sylphs */
	/* 141 */SYLPH_GUNNER(208, ClassType.FIGHTER, Race.SYLPH, null, ClassLevel.NONE, ClassType2.ARCHER),
	/* 142 */SHARPSHOOTER(209, ClassType.FIGHTER, Race.SYLPH, SYLPH_GUNNER, ClassLevel.FIRST, ClassType2.ARCHER),
	/* 143 */WIND_SNIPER(210, ClassType.FIGHTER, Race.SYLPH, SHARPSHOOTER, ClassLevel.SECOND, ClassType2.ARCHER),
	/*
	 * 144
	 */STORM_BLASTER(211, ClassType.FIGHTER, Race.SYLPH, WIND_SNIPER, ClassLevel.THIRD, ClassType2.ARCHER),

	/* 145 */DUMMY_ENTRY_212,
	/* 146 */DUMMY_ENTRY_213,
	/* 147 */DUMMY_ENTRY_214,
	/* 148 */DUMMY_ENTRY_215,
	/* 149 */DUMMY_ENTRY_216,

	/* Vanguard Rider */
	/* 150 */ORC_LANCER(217, ClassType.VANGUARD_RIDER, Race.ORC, null, ClassLevel.NONE, ClassType2.VANGUARD_RIDER),
	/* 151 */WOLF_MASTER(
							218,
							ClassType.VANGUARD_RIDER,
							Race.ORC,
							ORC_LANCER,
							ClassLevel.FIRST,
							ClassType2.VANGUARD_RIDER),
	/* 152 */DRAGOON(
						219,
						ClassType.VANGUARD_RIDER,
						Race.ORC,
						WOLF_MASTER,
						ClassLevel.SECOND,
						ClassType2.VANGUARD_RIDER),
	/*
	 * 153
	 */VANGUARD_RIDER(220, ClassType.VANGUARD_RIDER, Race.ORC, DRAGOON, ClassLevel.THIRD, ClassType2.VANGUARD_RIDER);

	public static final ClassId[] VALUES = values();

	private static final Map<Integer, ClassId> VALUES_MAP = new HashMap<>();
	static
	{
		for (ClassId classId : VALUES)
		{
			VALUES_MAP.put(classId.getId(), classId);
		}
	}

	public static ClassId valueOf(int id)
	{
		return VALUES_MAP.get(id);
	}

	private final int _id;
	private final Race _race;
	private final ClassId _parent;
	private final ClassId _firstParent;
	private final ClassLevel _level;
	private final ClassType _type;
	private final ClassType2 _type2;
	private final boolean _isDummy;
	private final int[] _changeClassItemIds;

	private ClassId()
	{
		this(null, null, null, null, null, true);
	}

	private ClassId(ClassType classType, Race race, ClassId parent, ClassLevel level, ClassType2 type2, int... changeClassItemIds)
	{
		this(classType, race, parent, level, type2, false, changeClassItemIds);
	}

	private ClassId(ClassType classType, Race race, ClassId parent, ClassLevel level, ClassType2 type2, boolean isDummy, int... changeClassItemIds)
	{
		_id = ordinal();
		_type = classType;
		_race = race;
		_parent = parent;
		_level = level;
		_type2 = type2;
		_isDummy = isDummy;
		_firstParent = _parent == null ? this : _parent.getFirstParent(0);
		_changeClassItemIds = changeClassItemIds;
	}

	private ClassId(int id, ClassType classType, Race race, ClassId parent, ClassLevel level, ClassType2 type2, int... changeClassItemIds)
	{
		this(id, classType, race, parent, level, type2, false, changeClassItemIds);
	}

	private ClassId(int id, ClassType classType, Race race, ClassId parent, ClassLevel level, ClassType2 type2, boolean isDummy, int... changeClassItemIds)
	{
		_id = id;
		_type = classType;
		_race = race;
		_parent = parent;
		_level = level;
		_type2 = type2;
		_isDummy = isDummy;
		_firstParent = _parent == null ? this : _parent.getFirstParent(0);
		_changeClassItemIds = changeClassItemIds;
	}

	public final int getId()
	{
		return _id;
	}

	public final Race getRace()
	{
		return _race;
	}

	public final boolean isOfRace(Race race)
	{
		return _race == race;
	}

	public final ClassLevel getClassLevel()
	{
		return _level;
	}

	public final boolean isOfLevel(ClassLevel level)
	{
		return _level == level;
	}

	public final ClassType getType()
	{
		return _type;
	}

	public final boolean isOfType(ClassType type)
	{
		return _type == type;
	}

	public ClassType2 getType2()
	{
		return _type2;
	}

	public final boolean isOfType2(ClassType2 type)
	{
		return _type2 == type;
	}

	public final boolean isMage()
	{
		return _type.isMagician();
	}

	public final boolean isDummy()
	{
		return _isDummy;
	}

	public boolean childOf(ClassId cid)
	{
		if (_parent == null)
			return false;

		if (_parent == cid)
			return true;

		return _parent.childOf(cid);

	}

	public final boolean equalsOrChildOf(ClassId cid)
	{
		return this == cid || childOf(cid);
	}

	public final ClassId getParent(int sex)
	{
		return _parent;
	}
	
	public final ClassId getParent()
	{
		return _parent;
	}
	
	public final ClassId getFirstParent(int sex)
	{
		return _firstParent;
	}

	public ClassData getClassData()
	{
		return ClassDataHolder.getInstance().getClassData(getId());
	}

	public double getBaseCp(int level)
	{
		return getClassData().getHpMpCpData(level).getCP();
	}

	public double getBaseHp(int level)
	{
		return getClassData().getHpMpCpData(level).getHP();
	}

	public double getBaseMp(int level)
	{
		return getClassData().getHpMpCpData(level).getMP();
	}

	public final String getName(Player player)
	{
		if (isDummy())
			return "N/A";
		if (player == null)
			return new CustomMessage("l2s.gameserver.model.base.ClassId.name." + getId()).toString(Config.DEFAULT_LANG);
		return new CustomMessage("l2s.gameserver.model.base.ClassId.name." + getId()).toString(player);
	}

	public int getClassMinLevel(boolean forNextClass)
	{
		ClassLevel classLevel = getClassLevel();
		if (forNextClass)
		{
			if (classLevel == ClassLevel.THIRD)
				return -1;

			classLevel = ClassLevel.VALUES[classLevel.ordinal() + 1];
		}

		switch (classLevel)
		{
			case FIRST:
				return 20;
			case SECOND:
				return 40;
			case THIRD:
				return 76;
		}
		return 1;
	}

	public boolean isLast()
	{
		return isOfLevel(ClassLevel.THIRD);
	}

	public int[] getChangeClassItemIds()
	{
		return _changeClassItemIds;
	}

	public static boolean isKnight(int id)
	{
		if (id >= 0)
		{
			ClassId classId = valueOf(id);
			if (classId == PALADIN)
				return true;
			if (classId == PHOENIX_KNIGHT)
				return true;
			if (classId == DARK_AVENGER)
				return true;
			if (classId == HELL_KNIGHT)
				return true;
			if (classId == TEMPLE_KNIGHT)
				return true;
			if (classId == EVAS_TEMPLAR)
				return true;
			if (classId == SHILLEN_KNIGHT)
				return true;
			if (classId == SHILLIEN_TEMPLAR)
				return true;
		}
		return false;
	}

	public static boolean isDagger(int id)
	{
		if (id >= 0)
		{
			ClassId classId = valueOf(id);
			if (classId == TREASURE_HUNTER)
				return true;
			if (classId == ADVENTURER)
				return true;
			if (classId == PLAIN_WALKER)
				return true;
			if (classId == WIND_RIDER)
				return true;
			if (classId == ABYSS_WALKER)
				return true;
			if (classId == GHOST_HUNTER)
				return true;
		}
		return false;
	}

	public static boolean isBow(int id)
	{
		if (id >= 0)
		{
			ClassId classId = valueOf(id);
			if (classId == HAWKEYE)
				return true;
			if (classId == SAGITTARIUS)
				return true;
			if (classId == SILVER_RANGER)
				return true;
			if (classId == MOONLIGHT_SENTINEL)
				return true;
			if (classId == PHANTOM_RANGER)
				return true;
			if (classId == GHOST_SENTINEL)
				return true;
		}
		return false;
	}

	public static boolean isFirearms(int id)
	{
		if (id >= 0)
		{
			ClassId classId = valueOf(id);
			if (classId == SYLPH_GUNNER)
				return true;
			if (classId == SHARPSHOOTER)
				return true;
			if (classId == WIND_SNIPER)
				return true;
			if (classId == STORM_BLASTER)
				return true;
		}
		return false;
	}

	public static boolean isDance(int id)
	{
		if (id >= 0)
		{
			ClassId classId = valueOf(id);
			if (classId == SWORDSINGER)
				return true;
			if (classId == SWORD_MUSE)
				return true;
			if (classId == BLADEDANCER)
				return true;
			if (classId == SPECTRAL_DANCER)
				return true;
		}
		return false;
	}

	public static boolean isWizard(int id)
	{
		if (id >= 0)
		{
			ClassId classId = valueOf(id);
			if (classId == SORCERER)
				return true;
			if (classId == ARCHMAGE)
				return true;
			if (classId == SPELLSINGER)
				return true;
			if (classId == MYSTIC_MUSE)
				return true;
			if (classId == SPELLHOWLER)
				return true;
			if (classId == STORM_SCREAMER)
				return true;
		}
		return false;
	}

	public static boolean isSummoner(int id)
	{
		if (id >= 0)
		{
			ClassId classId = valueOf(id);
			if (classId == WARLOCK)
				return true;
			if (classId == ARCANA_LORD)
				return true;
			if (classId == ELEMENTAL_SUMMONER)
				return true;
			if (classId == ELEMENTAL_MASTER)
				return true;
			if (classId == PHANTOM_SUMMONER)
				return true;
			if (classId == SPECTRAL_MASTER)
				return true;
		}
		return false;
	}

	public static boolean isHalfHealer(int id)
	{
		if (id >= 0)
		{
			ClassId classId = valueOf(id);
			if (classId == ELDER)
				return true;
			if (classId == EVAS_SAINT)
				return true;
			if (classId == SHILLEN_ELDER)
				return true;
			if (classId == SHILLIEN_SAINT)
				return true;
		}
		return false;
	}

	public int getLevel()
	{
		return getClassLevel().ordinal() + 1;
	}
	
	/**
	 * @return the child level of this Class (0=root, 1=child leve 1...)
	 */
	public int level()
	{
		if (_parent == null)
			return 0;
		return 1 + _parent.level();
	}
}