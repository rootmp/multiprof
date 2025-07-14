package l2s.gameserver.model.entity.events.fightclubmanager.enums;

import l2s.gameserver.model.base.ClassId;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public enum PlayerClass
{
	FIGHTERS(13113, new ClassId[] {
			ClassId.DUELIST,
			ClassId.GLADIATOR,
			ClassId.WARLORD,
			ClassId.DREADNOUGHT,
			ClassId.DESTROYER,
			ClassId.TITAN,
			ClassId.TYRANT,
			ClassId.GRAND_KHAVATARI,
			ClassId.WARSMITH,
			ClassId.MAESTRO,
			ClassId.DOOMBRINGER,
			ClassId.KAMAEL_SOLDIER,
			ClassId.TROOPER,
			ClassId.SOUL_FINDER,
			ClassId.WARDER,
			ClassId.BERSERKER,
			ClassId.SOUL_BREAKER,
			ClassId.SOUL_HOUND,
			ClassId.H_DEATH_PILGRIM,
			ClassId.H_DEATH_BLADE,
			ClassId.H_DEATH_MESSENGER,
			ClassId.H_DEATH_KNIGHT,
			ClassId.E_DEATH_PILGRIM,
			ClassId.E_DEATH_BLADE,
			ClassId.E_DEATH_MESSENGER,
			ClassId.E_DEATH_KNIGHT,
			ClassId.DE_DEATH_PILGRIM,
			ClassId.DE_DEATH_BLADE,
			ClassId.DE_DEATH_MESSENGER,
			ClassId.DE_DEATH_KNIGHT
	}),
	TANKS(13112, new ClassId[] {
			ClassId.PALADIN,
			ClassId.PHOENIX_KNIGHT,
			ClassId.DARK_AVENGER,
			ClassId.HELL_KNIGHT,
			ClassId.TEMPLE_KNIGHT,
			ClassId.EVAS_TEMPLAR,
			ClassId.SHILLEN_KNIGHT,
			ClassId.SHILLIEN_TEMPLAR
	}),
	ARCHERS(13114, new ClassId[] {
			ClassId.HAWKEYE,
			ClassId.SAGITTARIUS,
			ClassId.SILVER_RANGER,
			ClassId.MOONLIGHT_SENTINEL,
			ClassId.PHANTOM_RANGER,
			ClassId.GHOST_SENTINEL,
			ClassId.BOUNTY_HUNTER,
			ClassId.FORTUNE_SEEKER,
			ClassId.TRICKSTER,
			ClassId.SOUL_RANGER
	}),
	DAGGERS(13114, new ClassId[] {
			ClassId.TREASURE_HUNTER,
			ClassId.ADVENTURER,
			ClassId.PLAIN_WALKER,
			ClassId.WIND_RIDER,
			ClassId.ABYSS_WALKER,
			ClassId.GHOST_HUNTER,
			ClassId.OVERLORD,
			ClassId.DOMINATOR
	}),
	MAGES(13116, new ClassId[] {
			ClassId.SORCERER,
			ClassId.ARCHMAGE,
			ClassId.NECROMANCER,
			ClassId.SOULTAKER,
			ClassId.SPELLSINGER,
			ClassId.MYSTIC_MUSE,
			ClassId.SPELLHOWLER,
			ClassId.STORM_SCREAMER
	}),
	SUMMONERS(13118, new ClassId[] {
			ClassId.WARLOCK,
			ClassId.ARCANA_LORD,
			ClassId.ELEMENTAL_SUMMONER,
			ClassId.ELEMENTAL_MASTER,
			ClassId.PHANTOM_SUMMONER,
			ClassId.SPECTRAL_MASTER
	}),
	HEALERS(13115, new ClassId[] {
			ClassId.BISHOP,
			ClassId.CARDINAL,
			ClassId.ELDER,
			ClassId.EVAS_SAINT,
			ClassId.SHILLEN_ELDER,
			ClassId.SHILLIEN_SAINT
	}),
	SUPPORTS(13117, new ClassId[] {
			ClassId.PROPHET,
			ClassId.HIEROPHANT,
			ClassId.SWORDSINGER,
			ClassId.SWORD_MUSE,
			ClassId.BLADEDANCER,
			ClassId.SPECTRAL_DANCER,
			ClassId.WARCRYER,
			ClassId.DOOMCRYER
	});

	private int _transformId;
	private ClassId[] _classes;

	private PlayerClass(int transformId, ClassId[] ids)
	{
		_transformId = transformId;
		_classes = ids;
	}

	public ClassId[] getClasses()
	{
		return _classes;
	}

	public int getTransformId()
	{
		return _transformId;
	}
}
