package l2s.gameserver.stats;

import java.util.NoSuchElementException;

import l2s.gameserver.Config;

public enum Stats
{
	MAX_HP("maxHp", 0., Double.POSITIVE_INFINITY, 1.), // maximum hp
	MAX_MP("maxMp", 0., Double.POSITIVE_INFINITY, 1.), // maximum mp
	MAX_CP("maxCp", 0., Double.POSITIVE_INFINITY, 1.), // maximum cp
	MAX_DP("maxDp", 0, 1000, 0), // maximum dp
	MAX_BP("maxBp", 0., Double.POSITIVE_INFINITY, 0.), // maximum bp

	// лимит на максимум возможного хп\мп\сп
	PLAYER_MAX_HP_LIMIT("max_hp_limit", 0., Double.POSITIVE_INFINITY, 1.),
	PLAYER_MAX_MP_LIMIT("max_mp_limit", 0., Double.POSITIVE_INFINITY, 1.),
	PLAYER_MAX_CP_LIMIT("max_cp_limit", 0., Double.POSITIVE_INFINITY, 1.),

	// Для эффектов типа Seal of Limit
	HP_LIMIT("hpLimit", 1., 100., 100.),
	MP_LIMIT("mpLimit", 1., 100., 100.),
	CP_LIMIT("cpLimit", 1., 100., 100.),

	REGENERATE_HP_RATE("regHp"),
	REGENERATE_CP_RATE("regCp"),
	REGENERATE_MP_RATE("regMp"),
	REGENERATE_BP_RATE("regBp"),

	MANAHEAL_EFFECTIVNESS("mpEff", 0., 1000.),
	CPHEAL_EFFECTIVNESS("cpEff", 0., 1000.),

	// Stats from PTS server-pack
	HEAL_EFFECT("heal_effect", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	POTION_HP_HEAL_EFFECT("potion_hp_heal_effect", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	POTION_CP_HEAL_EFFECT("potion_cp_heal_effect", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	POTION_MP_HEAL_EFFECT("potion_mp_heal_effect", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	//

	RUN_SPEED("runSpd"),

	POWER_DEFENCE("pDef"),
	MAGIC_DEFENCE("mDef"),
	POWER_ATTACK("pAtk"),
	MAGIC_ATTACK("mAtk"),
	POWER_ATTACK_SPEED("pAtkSpd"),
	MAGIC_ATTACK_SPEED("mAtkSpd"),
	MAGIC_ATTACK_BY_PHYSIC("mAtkByPAtk"), // прибавка в %
											// к м.атаке в
											// зависимости
											// от количества
											// физ.атаки
											// (<add
											// stat="mAtkByPAtk"
											// value="#value"/>)

	MAGIC_REUSE_RATE("mReuse"),
	PHYSIC_REUSE_RATE("pReuse"),
	MUSIC_REUSE_RATE("musicReuse"),
	ATK_REUSE("atkReuse"),
	BASE_P_ATK_SPD("basePAtkSpd"), // тоже самое что и у параметров без base, но тут прибавка дается именно к
									// базовому значению, без учета всяких модификаторов
	BASE_M_ATK_SPD("baseMAtkSpd"),

	P_EVASION_RATE("pEvasRate"),
	M_EVASION_RATE("mEvasRate"),
	P_ACCURACY_COMBAT("pAccCombat"),
	M_ACCURACY_COMBAT("mAccCombat"),

	BASE_P_CRITICAL_RATE("basePCritRate", 0., Double.POSITIVE_INFINITY), // static crit rate. Use it to ADD some crit
																			// points. Sample: <add order="0x40"
																			// stat="baseCrit" val="27.4" />
	BASE_M_CRITICAL_RATE("baseMCritRate", 0., Double.POSITIVE_INFINITY),

	P_CRITICAL_RATE("pCritRate", 0., Double.POSITIVE_INFINITY, 100), // dynamic crit rate. Use it to MULTIPLE crit for
																		// 1.3, 1.5 etc. Sample: <add
																		// order="0x40" stat="rCrit" val="50" /> =
																		// (x1.5)
	M_CRITICAL_RATE("mCritRate", 0., Double.POSITIVE_INFINITY, 100),
	M_CRITICAL_RATE_FROM_P_CRITICAL_RATE("mCritRateFromPcritRate", 0., Double.POSITIVE_INFINITY, 100),

	P_CRIT_DAMAGE_RECEPTIVE("pCritDamRcpt", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 100.), // отвечает за
																										// получаемый
																										// крит урон
	P_SKILL_CRIT_DAMAGE_RECEPTIVE("pSkillCritDamRcpt", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 100.), // отвечает
																													// за
																													// получаемый
																													// крит
																													// урон
																													// от
																													// физ
																													// скилов
	M_CRIT_DAMAGE_RECEPTIVE("mCritDamRcpt", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), // тотвечает за
																									// получаемый крит
																									// урон от магии
	P_CRIT_CHANCE_RECEPTIVE("pCritChanceRcpt", 10., 190., 100.), // отвечает за шанс получить крит урон
	P_SKILL_CRIT_CHANCE_RECEPTIVE("pSkillCritChanceRcpt", 10., 190., 100.), // отвечает за шанс получить крит урон от
																			// физ скилов
	M_CRIT_CHANCE_RECEPTIVE("mCritChanceRcpt", 10., 190., 100.), // отвечает за шанс получить крит урон от магии

	P_SKILL_CRIT_RATE_DEX_DEPENDENCE("p_skill_crit_rate_dex_dependence", 0., 1., 0.),
	P_CRIT_RATE_LIMIT("p_crit_rate_limit"),

	// Stats from PTS server-pack
	CRITICAL_DAMAGE("critical_damage", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	MAGIC_CRITICAL_DMG("magic_critical_dmg", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	SKILL_CRITICAL_DAMAGE("skill_critical_damage", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	//

	INFLICTS_P_DAMAGE_POWER("inflicts_p_damage_power"), // позволяет регулировать силу физ дамага от цели с этим статом,
														// относительно
														// стандартных значений
	INFLICTS_M_DAMAGE_POWER("inflicts_m_damage_power"),
	RECEIVE_P_DAMAGE_POWER("receive_p_damage_power"), // позволяет
														// регулировать
														// получаемый
														// дамаг,
														// относительно
														// стандартных
														// значений
	RECEIVE_M_DAMAGE_POWER("receive_m_damage_power"),

	CAST_INTERRUPT("concentration", 0., 100.), // шанс сбить каст
	SHIELD_DEFENCE("sDef"),
	SHIELD_RATE("rShld", 0., 90.),
	SHIELD_ANGLE("shldAngle", 0., 360., 60.),

	POWER_ATTACK_RANGE("pAtkRange", 0., 1500.),
	MAGIC_ATTACK_RANGE("mAtkRange", 0., 1500.),
	P_ATTACK_RADIUS("p_attack_radius", 0., 1500.),
	POLE_ATTACK_ANGLE("poleAngle", 0., 180.), // радиус атаки по
												// множественным целям с
												// пики
	ATTACK_TARGETS_COUNT("attack_targets_count"), // количество целей, по которым одновременно прилетит атакой с пухи
	POLE_TARGET_COUNT("poleTargetCount"), // отдельно для пики

	STAT_STR("STR", 1., 100.),
	STAT_CON("CON", 1., 100.),
	STAT_DEX("DEX", 1., 100.),
	STAT_INT("INT", 1., 100.),
	STAT_WIT("WIT", 1., 100.),
	STAT_MEN("MEN", 1., 100.),

	BREATH("breath"), // сколько под воздухом дыхалки хватит
	FALL("fall"), // защита при падении с высоты
	EXP_LOST("expLost"), // увеличение\ уменьшение теряемого опыта при смерти

	CANCEL_RESIST("cancelResist", -200., 300.), // защита от скилов, снимающих баффы
	MAGIC_RESIST("magicResist", -200., 300.), // шанс сфейлить атаку магией
	BLOW_RESIST("blow_resist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), // защита от blow скилов

	CANCEL_POWER("cancelPower", -200., 200.),
	MAGIC_POWER("magicPower", -200., 200.),
	BLOW_POWER("blow_power", -200., 200.),

	RESIST_ABNORMAL_BUFF("resist_abnormal_buff", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	RESIST_ABNORMAL_DEBUFF("resist_abnormal_debuff", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),

	FATALBLOW_RATE("blowRate", 0., 10., 1.), // шанс нанести летальный урон
	P_SKILL_CRITICAL_RATE("p_skill_critical_rate"),
	DEATH_VULNERABILITY("deathVuln", 10., 190., 100.), // защита от
														// леталов

	DEFENCE_FIRE("defenceFire", -600, 600), // Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_WATER("defenceWater", -600, 600), // Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_WIND("defenceWind", -600, 600), // Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_EARTH("defenceEarth", -600, 600), // Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_HOLY("defenceHoly", -600, 600), // Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_UNHOLY("defenceUnholy", -600, 600), // Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	BASE_ELEMENTS_DEFENCE("elements_defence", -600, 600), // Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),

	ATTACK_FIRE("attackFire", 0., Config.ELEMENT_ATTACK_LIMIT), // Double.POSITIVE_INFINITY),
	ATTACK_WATER("attackWater", 0., Config.ELEMENT_ATTACK_LIMIT), // Double.POSITIVE_INFINITY),
	ATTACK_WIND("attackWind", 0., Config.ELEMENT_ATTACK_LIMIT), // Double.POSITIVE_INFINITY),
	ATTACK_EARTH("attackEarth", 0., Config.ELEMENT_ATTACK_LIMIT), // Double.POSITIVE_INFINITY),
	ATTACK_HOLY("attackHoly", 0., Config.ELEMENT_ATTACK_LIMIT), // Double.POSITIVE_INFINITY),
	ATTACK_UNHOLY("attackUnholy", 0., Config.ELEMENT_ATTACK_LIMIT), // Double.POSITIVE_INFINITY),

	TRANSFER_TO_SUMMON_DAMAGE_PERCENT("transferPetDam", 0., 100.),
	TRANSFER_TO_EFFECTOR_DAMAGE_PERCENT("transferToEffectorDam", 0., 100.),
	TRANSFER_TO_MP_DAMAGE_PERCENT("p_mp_shield", 0., 100.), // для манащитов (т.е. заместо хп урон по мп)

	// Отражение урона с шансом. Урон получает только атакующий.
	REFLECT_AND_BLOCK_DAMAGE_CHANCE("reflectAndBlockDam", 0., Config.REFLECT_AND_BLOCK_DAMAGE_CHANCE_CAP), // Ближний
																											// урон без
																											// скиллов
	REFLECT_AND_BLOCK_PSKILL_DAMAGE_CHANCE(
											"reflectAndBlockPSkillDam",
											0.,
											Config.REFLECT_AND_BLOCK_PSKILL_DAMAGE_CHANCE_CAP), // Ближний урон скиллами
	REFLECT_AND_BLOCK_MSKILL_DAMAGE_CHANCE(
											"reflectAndBlockMSkillDam",
											0.,
											Config.REFLECT_AND_BLOCK_MSKILL_DAMAGE_CHANCE_CAP), // Любой урон магией

	// Отражение урона в процентах. Урон получает и атакующий и цель
	REFLECT_DAMAGE_PERCENT("reflectDam", 0., Config.REFLECT_DAMAGE_PERCENT_CAP), // Ближний урон без скиллов
	REFLECT_BOW_DAMAGE_PERCENT("reflectBowDam", 0., Config.REFLECT_BOW_DAMAGE_PERCENT_CAP), // Урон луком без скиллов
	REFLECT_PSKILL_DAMAGE_PERCENT("reflectPSkillDam", 0., Config.REFLECT_PSKILL_DAMAGE_PERCENT_CAP), // Ближний урон
																										// скиллами
	REFLECT_MSKILL_DAMAGE_PERCENT("reflectMSkillDam", 0., Config.REFLECT_MSKILL_DAMAGE_PERCENT_CAP), // Любой урон
																										// магией

	REFLECT_PHYSIC_SKILL("reflectPhysicSkill", 0., 60.), // шанс увернуться от физ скила
	REFLECT_MAGIC_SKILL("reflectMagicSkill", 0., 60.), // от магии

	REFLECT_PHYSIC_DEBUFF("reflectPhysicDebuff", 0., 60.), // шанс вернуть физ дебафф обратно кастеру
	REFLECT_MAGIC_DEBUFF("reflectMagicDebuff", 0., 60.), // маг дебафф

	P_SKILL_EVASION("pSkillEvasion", 100., 200.), // уворот от физ скилов
	COUNTER_ATTACK("counterAttack", 0., 100.),

	P_SKILL_POWER("p_skill_power"),
	P_SKILL_POWER_STATIC("pSkillPowerStatic"),
	M_SKILL_POWER("mSkillPower"),
	CHARGED_P_SKILL_POWER("charged_p_skill_power"), // регулировка мощности для скилов использующих зарядки

	// PvP Dmg bonus
	PVP_PHYS_DMG_BONUS("pvpPhysDmgBonus"),
	PVP_PHYS_SKILL_DMG_BONUS("pvpPhysSkillDmgBonus"),
	PVP_MAGIC_SKILL_DMG_BONUS("pvpMagicSkillDmgBonus"),
	// PvP Def bonus
	PVP_PHYS_DEFENCE_BONUS("pvpPhysDefenceBonus"),
	PVP_PHYS_SKILL_DEFENCE_BONUS("pvpPhysSkillDefenceBonus"),
	PVP_MAGIC_SKILL_DEFENCE_BONUS("pvpMagicSkillDefenceBonus"),

	// PvE Dmg bonus
	PVE_PHYS_DMG_BONUS("pvePhysDmgBonus"),
	PVE_PHYS_SKILL_DMG_BONUS("pvePhysSkillDmgBonus"),
	PVE_MAGIC_SKILL_DMG_BONUS("pveMagicSkillDmgBonus"),
	// PvE Def bonus
	PVE_PHYS_DEFENCE_BONUS("pvePhysDefenceBonus"),
	PVE_PHYS_SKILL_DEFENCE_BONUS("pvePhysSkillDefenceBonus"),
	PVE_MAGIC_SKILL_DEFENCE_BONUS("pveMagicSkillDefenceBonus"),

	MP_MAGIC_SKILL_CONSUME("mpConsum"), // расход мп на маг умения
	MP_PHYSICAL_SKILL_CONSUME("mpConsumePhysical"), // расход мп на физ умения
	MP_DANCE_SKILL_CONSUME("mpDanceConsume"), // расход мп на песни танцы умения

	CHEAP_SHOT("cheap_shot"), // шанс выстрелить с лука автоатакой без расхода мп

	MAX_LOAD("maxLoad"), // сколько веса носить можно
	MAX_NO_PENALTY_LOAD("maxNoPenaltyLoad"), // сколько носить можно без штрафа на вес
	INVENTORY_LIMIT("inventoryLimit"), // размер инвентаря
	STORAGE_LIMIT("storageLimit"), // вместимость инвентаря
	TRADE_LIMIT("tradeLimit"), // количество слотов в трайде
	COMMON_RECIPE_LIMIT("CommonRecipeLimit"), // рецепты
	DWARVEN_RECIPE_LIMIT("DwarvenRecipeLimit"), // рецепты
	BUFF_LIMIT("buffLimit"), // лимит бафов
	TALISMANS_LIMIT("talismansLimit", 0., 6.),
	JEWELS_LIMIT("jewels_limit", 0., 6.),
	AGATHIONS_LIMIT("agathions_limit", 0., 5.),
	CUBICS_LIMIT("cubicsLimit", 0., 3., 1.),
	MAX_INCREASED_FORCE("max_increased_force"),

	// Рейты
	GRADE_EXPERTISE_LEVEL("gradeExpertiseLevel"),
	EXP_RATE_MULTIPLIER("exp_rate_multiplier"),
	SP_RATE_MULTIPLIER("sp_rate_multiplier"),
	ADENA_RATE_MULTIPLIER("adena_rate_multiplier"),
	DROP_RATE_MULTIPLIER("drop_rate_multiplier"),
	SPOIL_RATE_MULTIPLIER("spoil_rate_multiplier"),
	DROP_CHANCE_MODIFIER("drop_chance_modifier"),
	DROP_COUNT_MODIFIER("drop_count_modifier"),
	SPOIL_CHANCE_MODIFIER("spoil_chance_modifier"),
	SPOIL_COUNT_MODIFIER("spoil_count_modifier"),

	CRIT_CRAFT_CHANCE("crit_craft_chance", -100, 100),
	CRAFT_CHANCE("craft_chance", -100, 100),

	ENCHANT_CHANCE_MODIFIER("enchant_chance_modifier", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.),

	// Elemental
	ELEMENTAL_EXP_RATE_MULTIPLIER("elemental_exp_rate_multiplier"),

	// Лампы
	MAGIC_LAMP_EXP_RATE_MULTIPLIER("magic_lamp_exp_rate_multiplier"),
	MAGIC_LAMP_CHARGING_RATE_MULTIPLIER("magic_lamp_charging_rate_multiplier"),

	// Сайха
	SAYHAS_GRACE_CONSUME("sayhas_grace_consume"), // множителя расхода сайхи
	SAYHAS_GRACE_CHARGING_RATE_MULTIPLIER("sayhas_grace_charging_rate_multiplier"),

	SKILLS_ELEMENT_ID("skills_element_id", -1., 100., -1.),

	DAMAGE_AGGRO_PERCENT("damageAggroPercent", 0., 300., 0.),
	RECIEVE_DAMAGE_LIMIT("recieveDamageLimit", -1, Double.POSITIVE_INFINITY, -1),
	RECIEVE_DAMAGE_LIMIT_P_SKILL("recieveDamageLimitPSkill", -1, Double.POSITIVE_INFINITY, -1),
	RECIEVE_DAMAGE_LIMIT_M_SKILL("recieveDamageLimitMSkill", -1, Double.POSITIVE_INFINITY, -1),
	KILL_AND_RESTORE_HP("killAndRestoreHp", 0., 100., 0.),
	RESIST_REFLECT_DAM("resistRelectDam", 0., 100., 0.),

	BUFF_TIME_MODIFIER("buff_time_modifier", 1., Double.POSITIVE_INFINITY, 1.),
	DEBUFF_TIME_MODIFIER("debuff_time_modifier", 1., Double.POSITIVE_INFINITY, 1.),

	SPEED_ON_DEX_DEPENDENCE("speed_on_dex_dependence", 0., 1., 0.),

	SOULSHOT_POWER("soulshot_power", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	SPIRITSHOT_POWER("spiritshot_power", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),

	DAMAGE_BLOCK_RADIUS("damage_block_radius", -1, Double.POSITIVE_INFINITY, -1.),
	DAMAGE_BLOCK_FRONT("block_front", -1, Double.POSITIVE_INFINITY, 0.),
	DAMAGE_BLOCK_BACK("block_back", -1, Double.POSITIVE_INFINITY, 0.),
	DAMAGE_BLOCK_COUNT("damage_block_count", 0., Double.POSITIVE_INFINITY),
	DAMAGE_HATE_BONUS("DAMAGE_HATE_BONUS"),

	ShillienProtection("shillienProtection", 0, 1, 0),
	SacrificialSoul("sacrificialSoul", 0, 1, 0),
	RestoreHPGiveDamage("restoreHPGiveDamage", 0, 1, 0),
	MarkOfTrick("MarkOfTrick", 0, 1, 0),
	DivinityOfEinhasad("DivinityOfEinhasad", 0, 1, 0),
	BlockFly("blockFly", 0, 1, 0),
	BORN_TO_BE_DEAD("bornToBeDead", 0, 1, 0),

	ADDITIONAL_EXPERTISE_INDEX("additional_expertise_index"),

	PHYSICAL_ABNORMAL_RESIST("p_physical_abnormal_resist", -100., 100.),
	MAGIC_ABNORMAL_RESIST("p_magic_abnormal_resist", -100., 100.),

	WORLD_CHAT_POINTS("world_chat_points"),

	FIRE_ELEMENTAL_ATTACK("fire_elemental_attack"),
	WATER_ELEMENTAL_ATTACK("water_elemental_attack"),
	WIND_ELEMENTAL_ATTACK("wind_elemental_attack"),
	EARTH_ELEMENTAL_ATTACK("earth_elemental_attack"),

	FIRE_ELEMENTAL_DEFENCE("fire_elemental_defence"),
	WATER_ELEMENTAL_DEFENCE("water_elemental_defence"),
	WIND_ELEMENTAL_DEFENCE("wind_elemental_defence"),
	EARTH_ELEMENTAL_DEFENCE("earth_elemental_defence"),

	FIRE_ELEMENTAL_CRIT_RATE("fire_elemental_crit_rate"),
	WATER_ELEMENTAL_CRIT_RATE("water_elemental_crit_rate"),
	WIND_ELEMENTAL_CRIT_RATE("wind_elemental_crit_rate"),
	EARTH_ELEMENTAL_CRIT_RATE("earth_elemental_crit_rate"),

	FIRE_ELEMENTAL_CRIT_ATTACK("fire_elemental_crit_attack"),
	WATER_ELEMENTAL_CRIT_ATTACK("water_elemental_crit_attack"),
	WIND_ELEMENTAL_CRIT_ATTACK("wind_elemental_crit_attack"),
	EARTH_ELEMENTAL_CRIT_ATTACK("earth_elemental_crit_attack"),

	VAMPIRIC_ATTACK("vampiric_attack", 0., 100.),
	MP_VAMPIRIC_ATTACK("mp_vampiric_attack", 0., 100.),

	ATTACK_TRAIT_SWORD("attack_trait_sword", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_SWORD("defence_trait_sword", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_BLUNT("attack_trait_blunt", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_BLUNT("defence_trait_blunt", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_DAGGER("attack_trait_dagger", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_DAGGER("defence_trait_dagger", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_POLE("attack_trait_pole", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_POLE("defence_trait_pole", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_FIST("attack_trait_fist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_FIST("defence_trait_fist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_BOW("attack_trait_bow", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_BOW("defence_trait_bow", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_ETC("attack_trait_etc", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_ETC("defence_trait_etc", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_POISON("attack_trait_poison", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_POISON("defence_trait_poison", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_HOLD("attack_trait_hold", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_HOLD("defence_trait_hold", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_BLEED("attack_trait_bleed", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_BLEED("defence_trait_bleed", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_SLEEP("attack_trait_sleep", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_SLEEP("defence_trait_sleep", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_SHOCK("attack_trait_shock", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_SHOCK("defence_trait_shock", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_DERANGEMENT("attack_trait_derangement", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_DERANGEMENT("defence_trait_derangement", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_BUG_WEAKNESS("attack_trait_bug_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_BUG_WEAKNESS("defence_trait_bug_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_ANIMAL_WEAKNESS("attack_trait_animal_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_ANIMAL_WEAKNESS("defence_trait_animal_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_PLANT_WEAKNESS("attack_trait_plant_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_PLANT_WEAKNESS("defence_trait_plant_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_BEAST_WEAKNESS("attack_trait_beast_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_BEAST_WEAKNESS("defence_trait_beast_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_DRAGON_WEAKNESS("attack_trait_dragon_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_DRAGON_WEAKNESS("defence_trait_dragon_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_PARALYZE("attack_trait_paralyze", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_PARALYZE("defence_trait_paralyze", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_DUAL("attack_trait_dual", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_DUAL("defence_trait_dual", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_DUALFIST("attack_trait_dualfist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_DUALFIST("defence_trait_dualfist", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_BOSS("attack_trait_boss", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_BOSS("defence_trait_boss", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_GIANT_WEAKNESS("attack_trait_giant_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_GIANT_WEAKNESS("defence_trait_giant_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_CONSTRUCT_WEAKNESS(
									"attack_trait_construct_weakness",
									Double.NEGATIVE_INFINITY,
									Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_CONSTRUCT_WEAKNESS(
										"defence_trait_construct_weakness",
										Double.NEGATIVE_INFINITY,
										Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_DEATH("attack_trait_death", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_DEATH("defence_trait_death", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_VALAKAS("attack_trait_valakas", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_VALAKAS("defence_trait_valakas", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_ROOT_PHYSICALLY("attack_trait_root_physically", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_ROOT_PHYSICALLY("defence_trait_root_physically", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_RAPIER("attack_trait_rapier", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_RAPIER("defence_trait_rapier", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_CROSSBOW("attack_trait_crossbow", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_CROSSBOW("defence_trait_crossbow", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_ANCIENTSWORD("attack_trait_ancientsword", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_ANCIENTSWORD("defence_trait_ancientsword", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_TURN_STONE("attack_trait_turn_stone", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_TURN_STONE("defence_trait_turn_stone", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_GUST("attack_trait_gust", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_GUST("defence_trait_gust", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_PHYSICAL_BLOCKADE(
									"attack_trait_physical_blockade",
									Double.NEGATIVE_INFINITY,
									Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_PHYSICAL_BLOCKADE(
									"defence_trait_physical_blockade",
									Double.NEGATIVE_INFINITY,
									Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_TARGET("attack_trait_target", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_TARGET("defence_trait_target", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_PHYSICAL_WEAKNESS(
									"attack_trait_physical_weakness",
									Double.NEGATIVE_INFINITY,
									Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_PHYSICAL_WEAKNESS(
									"defence_trait_physical_weakness",
									Double.NEGATIVE_INFINITY,
									Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_MAGICAL_WEAKNESS("attack_trait_magical_weakness", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_MAGICAL_WEAKNESS(
									"defence_trait_magical_weakness",
									Double.NEGATIVE_INFINITY,
									Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_DUALDAGGER("attack_trait_dualdagger", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_DUALDAGGER("defence_trait_dualdagger", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_DUALBLUNT("attack_trait_dualblunt", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_DUALBLUNT("defence_trait_dualblunt", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_KNOCKBACK("attack_trait_knockback", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_KNOCKBACK("defence_trait_knockback", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_KNOCKDOWN("attack_trait_knockdown", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_KNOCKDOWN("defence_trait_knockdown", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_PULL("attack_trait_pull", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_PULL("defence_trait_pull", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_HATE("attack_trait_hate", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_HATE("defence_trait_hate", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_AGGRESSION("attack_trait_aggression", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_AGGRESSION("defence_trait_aggression", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_AIRBIND("attack_trait_airbind", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_AIRBIND("defence_trait_airbind", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_DISARM("attack_trait_disarm", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_DISARM("defence_trait_disarm", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_DEPORT("attack_trait_deport", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_DEPORT("defence_trait_deport", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_CHANGEBODY("attack_trait_changebody", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_CHANGEBODY("defence_trait_changebody", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_TWOHANDCROSSBOW("attack_trait_twohandcrossbow", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_TWOHANDCROSSBOW("defence_trait_twohandcrossbow", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	ATTACK_TRAIT_FIREARMS("attack_trait_firearms", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	DEFENCE_TRAIT_FIREARMS("defence_trait_firearms", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	BLESSED_BY_SAYHA("blessedBySayha"),
	VITAL_RATE("vital_rate", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	VITAL_ADD("vital_add", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	VITAL_SUB("vital_sub", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
	// Henna
	HENNA_SLOTS_AVAILABLE("hennaSlots");

	public static final Stats[] VALUES = values();
	public static final int NUM_STATS = VALUES.length;

	private final String _value;
	private double _min;
	private double _max;
	private double _init;

	public String getValue()
	{
		return _value;
	}

	public double getInit()
	{
		return _init;
	}

	private Stats(String s)
	{
		this(s, 0., Double.POSITIVE_INFINITY, 0.);
	}

	private Stats(String s, double min, double max)
	{
		this(s, min, max, 0.);
	}

	private Stats(String s, double min, double max, double init)
	{
		_value = s.toUpperCase();
		_min = min;
		_max = max;
		_init = init;
	}

	public double validate(double val)
	{
		if (val < _min)
			return _min;
		if (val > _max)
			return _max;
		return val;
	}

	public static Stats valueOfXml(String name)
	{
		String upperCaseName = name.toUpperCase();
		for (Stats s : VALUES)
		{
			if (s.getValue().equals(upperCaseName))
				return s;
		}
		throw new NoSuchElementException("Unknown name '" + name + "' for enum Stats");
	}

	@Override
	public String toString()
	{
		return _value;
	}
}