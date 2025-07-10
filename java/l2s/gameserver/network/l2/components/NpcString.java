package l2s.gameserver.network.l2.components;

import java.util.NoSuchElementException;

/**
 * @author VISTALL
 * @date 15:13/28.12.2010
 */
public enum NpcString
{
	// Text: Flag Sentry Greg has appeared!
	FLAG_SENTRY_GREG_HAS_APPEARED(1804011),
	// Text: You shouldn’t say it! All forces, attack!
	YOU_SHOULDNT_SAY_IT_ALL_FORCES_ATTACK(1804010),
	// Text: Giran Holy Artifact has appeared.
	GIRAN_HOLY_ARTIFACT_HAS_APPEARED(1803791),
	// Text: Goddard Holy Artifact has appeared.
	GODDARD_HOLY_ARTIFACT_HAS_APPEARED(1803974),
	NONE(-1),
	NONE2(2042),
	// Text: The radio signal detector is responding. # A suspicious pile of stones
	// catches your eye.
	THE_RADIO_SIGNAL_DETECTOR_IS_RESPONDING_A_SUSPICIOUS_PILE_OF_STONES_CATCHES_YOUR_EYE(11453),
	// Text: Att... attack... $s1. Ro... rogue... $s2..
	ATT__ATTACK__S1__RO__ROGUE__S2(46350),
	// Text: Stage $s1
	STAGE_S1(9),
	// Text: A non-permitted target has been discovered.
	A_NONPERMITTED_TARGET_HAS_BEEN_DISCOVERED(1000001),
	// Text: Intruder removal system initiated.
	INTRUDER_REMOVAL_SYSTEM_INITIATED(1000002),
	// Text: Removing intruders.
	REMOVING_INTRUDERS(1000003),
	// Text: A fatal error has occurred.
	A_FATAL_ERROR_HAS_OCCURRED(1000004),
	// Text: System is being shut down...
	SYSTEM_IS_BEING_SHUT_DOWN(1000005),
	// Text: ......
	_DOT_DOT_DOT_DOT_DOT_DOT_(1000006),
	// Text: Listen, you villagers. Our liege, who will soon become a lord, has
	// defeated the Headless Knight. You can now rest easy!
	LISTEN_YOU_VILLAGERS_OUR_LIEGE_WHO_WILL_SOON_BECAME_A_LORD_HAS_DEFEATED_THE_HEADLESS_KNIGHT(70854),
	// $s1 has become lord of the Town of Gludio. Long may he reign!
	S1_HAS_BECOME_THE_LORD_OF_THE_TOWN_OF_GLUDIO(70859),
	// Text: $s1 has become lord of the Town of Dion. Long may he reign!
	S1_HAS_BECOME_THE_LORD_OF_THE_TOWN_OF_DION(70959),
	// Text: $s1 has become the lord of the Town of Giran. May there be glory in the
	// territory of Giran!
	S1_HAS_BECOME_THE_LORD_OF_THE_TOWN_OF_GIRAN(71059),
	// Text: $s1 has become the lord of the Town of Oren. May there be glory in the
	// territory of Oren!
	S1_HAS_BECOME_THE_LORD_OF_THE_TOWN_OF_OREN(71259),
	// Text: $s1 has become the lord of the Town of Aden. May there be glory in the
	// territory of Aden!
	S1_HAS_BECOME_THE_LORD_OF_THE_TOWN_OF_ADEN(71351),
	// Text: $s1 has become the lord of the Town of Schuttgart. May there be glory
	// in the territory of Schuttgart!
	S1_HAS_BECOME_THE_LORD_OF_THE_TOWN_OF_SCHUTTGART(71459),
	// Text: $s1 has become the lord of the Town of Innadril. May there be glory in
	// the territory of Innadril!
	S1_HAS_BECOME_THE_LORD_OF_THE_TOWN_OF_INNADRIL(71159),
	// Text: $s1 has become the lord of the Town of Rune. May there be glory in the
	// territory of Rune!
	S1_HAS_BECOME_THE_LORD_OF_THE_TOWN_OF_RUNE(71659),
	// Text: Protect the catapult of Gludio!
	PROTECT_THE_CATAPULT_OF_GLUDIO(72951),
	// Text: Protect the catapult of Dion!
	PROTECT_THE_CATAPULT_OF_DION(72952),
	// Text: Protect the catapult of Giran!
	PROTECT_THE_CATAPULT_OF_GIRAN(72953),
	// Text: Protect the catapult of Oren!
	PROTECT_THE_CATAPULT_OF_OREN(72954),
	// Text: Protect the catapult of Aden!
	PROTECT_THE_CATAPULT_OF_ADEN(72955),
	// Text: Protect the catapult of Innadril!
	PROTECT_THE_CATAPULT_OF_INNADRIL(72956),
	// Text: Protect the catapult of Goddard!
	PROTECT_THE_CATAPULT_OF_GODDARD(72957),
	// Text: Protect the catapult of Rune!
	PROTECT_THE_CATAPULT_OF_RUNE(72958),
	// Text: Protect the catapult of Schuttgart!
	PROTECT_THE_CATAPULT_OF_SCHUTTGART(72959),
	// Text: The catapult of Gludio has been destroyed!
	THE_CATAPULT_OF_GLUDIO_HAS_BEEN_DESTROYED(72961),
	// Text: The catapult of Dion has been destroyed!
	THE_CATAPULT_OF_DION_HAS_BEEN_DESTROYED(72962),
	// Text: The catapult of Giran has been destroyed!
	THE_CATAPULT_OF_GIRAN_HAS_BEEN_DESTROYED(72963),
	// Text: The catapult of Oren has been destroyed!
	THE_CATAPULT_OF_OREN_HAS_BEEN_DESTROYED(72964),
	// Text: The catapult of Aden has been destroyed!
	THE_CATAPULT_OF_ADEN_HAS_BEEN_DESTROYED(72965),
	// Text: The catapult of Innadril has been destroyed!
	THE_CATAPULT_OF_INNADRIL_HAS_BEEN_DESTROYED(72966),
	// Text: The catapult of Goddard has been destroyed!
	THE_CATAPULT_OF_GODDARD_HAS_BEEN_DESTROYED(72967),
	// Text: The catapult of Rune has been destroyed!
	THE_CATAPULT_OF_RUNE_HAS_BEEN_DESTROYED(72968),
	// Text: The catapult of Schuttgart has been destroyed!
	THE_CATAPULT_OF_SCHUTTGART_HAS_BEEN_DESTROYED(72969),
	// Text: Protect the supplies safe of Gludio!
	PROTECT_THE_SUPPLIES_SAFE_OF_GLUDIO(73051),
	// Text: Protect the supplies safe of Dion!
	PROTECT_THE_SUPPLIES_SAFE_OF_DION(73052),
	// Text: Protect the supplies safe of Giran!
	PROTECT_THE_SUPPLIES_SAFE_OF_GIRAN(73053),
	// Text: Protect the supplies safe of Oren!
	PROTECT_THE_SUPPLIES_SAFE_OF_OREN(73054),
	// Text: Protect the supplies safe of Aden!
	PROTECT_THE_SUPPLIES_SAFE_OF_ADEN(73055),
	// Text: Protect the supplies safe of Innadril!
	PROTECT_THE_SUPPLIES_SAFE_OF_INNADRIL(73056),
	// Text: Protect the supplies safe of Goddard!
	PROTECT_THE_SUPPLIES_SAFE_OF_GODDARD(73057),
	// Text: Protect the supplies safe of Rune!
	PROTECT_THE_SUPPLIES_SAFE_OF_RUNE(73058),
	// Text: Protect the supplies safe of Schuttgart!
	PROTECT_THE_SUPPLIES_SAFE_OF_SCHUTTGART(73059),
	// Text: The supplies safe of Gludio has been destroyed!
	THE_SUPPLIES_SAFE_OF_GLUDIO_HAS_BEEN_DESTROYED(73061),
	// Text: The supplies safe of Dion has been destroyed!
	THE_SUPPLIES_SAFE_OF_DION_HAS_BEEN_DESTROYED(73062),
	// Text: The supplies safe of Giran has been destroyed!
	THE_SUPPLIES_SAFE_OF_GIRAN_HAS_BEEN_DESTROYED(73063),
	// Text: The supplies safe of Oren has been destroyed!
	THE_SUPPLIES_SAFE_OF_OREN_HAS_BEEN_DESTROYED(73064),
	// Text: The supplies safe of Aden has been destroyed!
	THE_SUPPLIES_SAFE_OF_ADEN_HAS_BEEN_DESTROYED(73065),
	// Text: The supplies safe of Innadril has been destroyed!
	THE_SUPPLIES_SAFE_OF_INNADRIL_HAS_BEEN_DESTROYED(73066),
	// Text: The supplies safe of Goddard has been destroyed!
	THE_SUPPLIES_SAFE_OF_GODDARD_HAS_BEEN_DESTROYED(73067),
	// Text: The supplies safe of Rune has been destroyed!
	THE_SUPPLIES_SAFE_OF_RUNE_HAS_BEEN_DESTROYED(73068),
	// Text: The supplies safe of Schuttgart has been destroyed!
	THE_SUPPLIES_SAFE_OF_SCHUTTGART_HAS_BEEN_DESTROYED(73069),
	// Text: Protect the Military Association Leader of Gludio!
	PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_GLUDIO(73151),
	// Text: Protect the Military Association Leader of Dion!
	PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_DION(73152),
	// Text: Protect the Military Association Leader of Giran!
	PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_GIRAN(73153),
	// Text: Protect the Military Association Leader of Oren!
	PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_OREN(73154),
	// Text: Protect the Military Association Leader of Aden!
	PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_ADEN(73155),
	// Text: Protect the Military Association Leader of Innadril!
	PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_INNADRIL(73156),
	// Text: Protect the Military Association Leader of Goddard!
	PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_GODDARD(73157),
	// Text: Protect the Military Association Leader of Rune!
	PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_RUNE(73158),
	// Text: Protect the Military Association Leader of Schuttgart!
	PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_SCHUTTGART(73159),
	// Text: The Military Association Leader of Gludio is dead!
	THE_MILITARY_ASSOCIATION_LEADER_OF_GLUDIO_IS_DEAD(73161),
	// Text: The Military Association Leader of Dion is dead!
	THE_MILITARY_ASSOCIATION_LEADER_OF_DION_IS_DEAD(73162),
	// Text: The Military Association Leader of Giran is dead!
	THE_MILITARY_ASSOCIATION_LEADER_OF_GIRAN_IS_DEAD(73163),
	// Text: The Military Association Leader of Oren is dead!
	THE_MILITARY_ASSOCIATION_LEADER_OF_OREN_IS_DEAD(73164),
	// Text: The Military Association Leader of Aden is dead!
	THE_MILITARY_ASSOCIATION_LEADER_OF_ADEN_IS_DEAD(73165),
	// Text: The Military Association Leader of Innadril is dead!
	THE_MILITARY_ASSOCIATION_LEADER_OF_INNADRIL_IS_DEAD(73166),
	// Text: The Military Association Leader of Goddard is dead!
	THE_MILITARY_ASSOCIATION_LEADER_OF_GODDARD_IS_DEAD(73167),
	// Text: The Military Association Leader of Rune is dead!
	THE_MILITARY_ASSOCIATION_LEADER_OF_RUNE_IS_DEAD(73168),
	// Text: The Military Association Leader of Schuttgart is dead!
	THE_MILITARY_ASSOCIATION_LEADER_OF_SCHUTTGART_IS_DEAD(73169),
	// Text: Protect the Religious Association Leader of Gludio!
	PROTECT_THE_RELIGIOUS_ASSOCIATION_LEADER_OF_GLUDIO(73251),
	// Text: Protect the Religious Association Leader of Dion!
	PROTECT_THE_RELIGIOUS_ASSOCIATION_LEADER_OF_DION(73252),
	// Text: Protect the Religious Association Leader of Giran!
	PROTECT_THE_RELIGIOUS_ASSOCIATION_LEADER_OF_GIRAN(73253),
	// Text: Protect the Religious Association Leader of Oren!
	PROTECT_THE_RELIGIOUS_ASSOCIATION_LEADER_OF_OREN(73254),
	// Text: Protect the Religious Association Leader of Aden!
	PROTECT_THE_RELIGIOUS_ASSOCIATION_LEADER_OF_ADEN(73255),
	// Text: Protect the Religious Association Leader of Innadril!
	PROTECT_THE_RELIGIOUS_ASSOCIATION_LEADER_OF_INNADRIL(73256),
	// Text: Protect the Religious Association Leader of Goddard!
	PROTECT_THE_RELIGIOUS_ASSOCIATION_LEADER_OF_GODDARD(73257),
	// Text: Protect the Religious Association Leader of Rune!
	PROTECT_THE_RELIGIOUS_ASSOCIATION_LEADER_OF_RUNE(73258),
	// Text: Protect the Religious Association Leader of Schuttgart!
	PROTECT_THE_RELIGIOUS_ASSOCIATION_LEADER_OF_SCHUTTGART(73259),
	// Text: The Religious Association Leader of Gludio is dead!
	THE_RELIGIOUS_ASSOCIATION_LEADER_OF_GLUDIO_IS_DEAD(73261),
	// Text: The Religious Association Leader of Dion is dead!
	THE_RELIGIOUS_ASSOCIATION_LEADER_OF_DION_IS_DEAD(73262),
	// Text: The Religious Association Leader of Giran is dead!
	THE_RELIGIOUS_ASSOCIATION_LEADER_OF_GIRAN_IS_DEAD(73263),
	// Text: The Religious Association Leader of Oren is dead!
	THE_RELIGIOUS_ASSOCIATION_LEADER_OF_OREN_IS_DEAD(73264),
	// Text: The Religious Association Leader of Aden is dead!
	THE_RELIGIOUS_ASSOCIATION_LEADER_OF_ADEN_IS_DEAD(73265),
	// Text: The Religious Association Leader of Innadril is dead!
	THE_RELIGIOUS_ASSOCIATION_LEADER_OF_INNADRIL_IS_DEAD(73266),
	// Text: The Religious Association Leader of Goddard is dead!
	THE_RELIGIOUS_ASSOCIATION_LEADER_OF_GODDARD_IS_DEAD(73267),
	// Text: The Religious Association Leader of Rune is dead!
	THE_RELIGIOUS_ASSOCIATION_LEADER_OF_RUNE_IS_DEAD(73268),
	// Text: The Religious Association Leader of Schuttgart is dead!
	THE_RELIGIOUS_ASSOCIATION_LEADER_OF_SCHUTTGART_IS_DEAD(73269),
	// Text: Protect the Economic Association Leader of Gludio!
	PROTECT_THE_ECONOMIC_ASSOCIATION_LEADER_OF_GLUDIO(73351),
	// Text: Protect the Economic Association Leader of Dion!
	PROTECT_THE_ECONOMIC_ASSOCIATION_LEADER_OF_DION(73352),
	// Text: Protect the Economic Association Leader of Giran!
	PROTECT_THE_ECONOMIC_ASSOCIATION_LEADER_OF_GIRAN(73353),
	// Text: Protect the Economic Association Leader of Oren!
	PROTECT_THE_ECONOMIC_ASSOCIATION_LEADER_OF_OREN(73354),
	// Text: Protect the Economic Association Leader of Aden!
	PROTECT_THE_ECONOMIC_ASSOCIATION_LEADER_OF_ADEN(73355),
	// Text: Protect the Economic Association Leader of Innadril!
	PROTECT_THE_ECONOMIC_ASSOCIATION_LEADER_OF_INNADRIL(73356),
	// Text: Protect the Economic Association Leader of Goddard!
	PROTECT_THE_ECONOMIC_ASSOCIATION_LEADER_OF_GODDARD(73357),
	// Text: Protect the Economic Association Leader of Rune!
	PROTECT_THE_ECONOMIC_ASSOCIATION_LEADER_OF_RUNE(73358),
	// Text: Protect the Economic Association Leader of Schuttgart!
	PROTECT_THE_ECONOMIC_ASSOCIATION_LEADER_OF_SCHUTTGART(73359),
	// Text: The Economic Association Leader of Gludio is dead!
	THE_ECONOMIC_ASSOCIATION_LEADER_OF_GLUDIO_IS_DEAD(73361),
	// Text: The Economic Association Leader of Dion is dead!
	THE_ECONOMIC_ASSOCIATION_LEADER_OF_DION_IS_DEAD(73362),
	// Text: The Economic Association Leader of Giran is dead!
	THE_ECONOMIC_ASSOCIATION_LEADER_OF_GIRAN_IS_DEAD(73363),
	// Text: The Economic Association Leader of Oren is dead!
	THE_ECONOMIC_ASSOCIATION_LEADER_OF_OREN_IS_DEAD(73364),
	// Text: The Economic Association Leader of Aden is dead!
	THE_ECONOMIC_ASSOCIATION_LEADER_OF_ADEN_IS_DEAD(73365),
	// Text: The Economic Association Leader of Innadril is dead!
	THE_ECONOMIC_ASSOCIATION_LEADER_OF_INNADRIL_IS_DEAD(73366),
	// Text: The Economic Association Leader of Goddard is dead!
	THE_ECONOMIC_ASSOCIATION_LEADER_OF_GODDARD_IS_DEAD(73367),
	// Text: The Economic Association Leader of Rune is dead!
	THE_ECONOMIC_ASSOCIATION_LEADER_OF_RUNE_IS_DEAD(73368),
	// Text: The Economic Association Leader of Schuttgart is dead!
	THE_ECONOMIC_ASSOCIATION_LEADER_OF_SCHUTTGART_IS_DEAD(73369),
	// Text: Defeat $s1 enemy knights!
	DEFEAT_S1_ENEMY_KNIGHTS(73451),
	// Text: You have defeated $s2 of $s1 knights.
	YOU_HAVE_DEFEATED_S2_OF_S1_KNIGHTS(73461),
	// Text: You weakened the enemy's defense!
	YOU_WEAKENED_THE_ENEMYS_DEFENSE(73462),
	// Text: Defeat $s1 warriors and rogues!
	DEFEAT_S1_WARRIORS_AND_ROGUES(73551),
	// Text: You have defeated $s2 of $s1 warriors and rogues.
	YOU_HAVE_DEFEATED_S2_OF_S1_WARRIORS_AND_ROGUES(73561),
	// Text: You weakened the enemy's attack!
	YOU_WEAKENED_THE_ENEMYS_ATTACK(73562),
	// Text: Defeat $s1 wizards and summoners!
	DEFEAT_S1_WIZARDS_AND_SUMMONERS(73651),
	// Text: You have defeated $s2 of $s1 enemies.
	YOU_HAVE_DEFEATED_S2_OF_S1_ENEMIES(73661),
	// Text: You weakened the enemy's magic!
	YOU_WEAKENED_THE_ENEMYS_MAGIC(73662),
	// Text: Defeat $s1 healers and buffers.
	DEFEAT_S1_HEALERS_AND_BUFFERS(73751),
	// Text: You have defeated $s2 of $s1 healers and buffers.
	YOU_HAVE_DEFEATED_S2_OF_S1_HEALERS_AND_BUFFERS(73761),
	// Text: You have weakened the enemy's support!
	YOU_HAVE_WEAKENED_THE_ENEMYS_SUPPORT(73762),
	// Text: Defeat $s1 warsmiths and overlords.
	DEFEAT_S1_WARSMITHS_AND_OVERLORDS(73851),
	// Text: You have defeated $s2 of $s1 warsmiths and overlords.
	YOU_HAVE_DEFEATED_S2_OF_S1_WARSMITHS_AND_OVERLORDS(73861),
	// Text: You destroyed the enemy's professionals!
	YOU_DESTROYED_THE_ENEMYS_PROFESSIONALS(73862),
	// Text: Return
	RETURN(1000170),
	// Text: Event Number
	EVENT_NUMBER(1000172),
	// Text: First Prize
	FIRST_PRIZE(1000173),
	// Text: Second Prize
	SECOND_PRIZE(1000174),
	// Text: Third Prize
	THIRD_PRIZE(1000175),
	// Text: Fourth Prize
	FOURTH_PRIZE(1000176),
	// Text: There has been no winning lottery ticket.
	THERE_HAS_BEEN_NO_WINNING_LOTTERY_TICKET(1000177),
	// Text: Your lucky numbers have been selected above.
	YOUR_LUCKY_NUMBERS_HAVE_BEEN_SELECTED_ABOVE(1000179),
	// Text: Prepare to die, foreign invaders! I am Gustav, the eternal ruler of
	// this fortress and I have taken up my sword to repel thee!
	PREPARE_TO_DIE_FOREIGN_INVADERS_I_AM_GUSTAV_THE_ETERNAL_RULER_OF_THIS_FORTRESS_AND_I_HAVE_TAKEN_UP_MY_SWORD_TO_REPEL_THEE(
																																1000275),
	// Text: Glory to Aden, the Kingdom of the Lion! Glory to Sir Gustav, our
	// immortal lord!
	GLORY_TO_ADEN_THE_KINGDOM_OF_THE_LION_GLORY_TO_SIR_GUSTAV_OUR_IMMORTAL_LORD(1000276),
	// Text: Soldiers of Gustav, go forth and destroy the invaders!
	SOLDIERS_OF_GUSTAV_GO_FORTH_AND_DESTROY_THE_INVADERS(1000277),
	// Text: This is unbelievable! Have I really been defeated? I shall return and
	// take your head!
	THIS_IS_UNBELIEVABLE_HAVE_I_REALLY_BEEN_DEFEATED_I_SHALL_RETURN_AND_TAKE_YOUR_HEAD(1000278),
	// Text: Could it be that I have reached my end? I cannot die without honor,
	// without the permission of Sir Gustav!
	COULD_IT_BE_THAT_I_HAVE_REACHED_MY_END_I_CANNOT_DIE_WITHOUT_HONOR_WITHOUT_THE_PERMISSION_OF_SIR_GUSTAV(1000279),
	// Text: Ah, the bitter taste of defeat... I fear my torments are not over...
	AH_THE_BITTER_TASTE_OF_DEFEAT_I_FEAR_MY_TORMENTS_ARE_NOT_OVER(1000280),
	// This world will soon be annihilated!
	THIS_WORLD_WILL_SOON_BE_ANNIHILATED(1000303),
	// Text: Now the fun starts!
	NOW_THE_FUN_STARTS(1000406),
	// Text: Enough fooling around. Get ready to die!
	ENOUGH_FOOLING_AROUND_GET_READY_TO_DIE(1000407),
	// Text: You idiot! I've just been toying with you!
	YOU_IDIOT_IVE_JUST_BEEN_TOYING_WITH_YOU(1000408),
	// Text: Witness my true power!
	WITNESS_MY_TRUE_POWER(1000409),
	// Text: Now the battle begins!
	NOW_THE_BATTLE_BEGINS(1000410),
	// Text: I must admit, no one makes my blood boil quite like you do!
	I_MUST_ADMIT_NO_ONE_MAKES_MY_BLOOD_BOIL_QUITE_LIKE_YOU_DO(1000411),
	// Text: It's stronger than expected!
	ITS_STRONGER_THAN_EXPECTED_(1000412),
	// Text: I'll double my strength!
	ILL_DOUBLE_MY_STRENGTH(1000413),
	// Text: Prepare to die!
	PREPARE_TO_DIE(1000414),
	// Text: All is lost! Prepare to meet the goddess of death!
	ALL_IS_LOST_PREPARE_TO_MEET_THE_GODDESS_OF_DEATH(1000415),
	// Text: All is lost! The prophecy of destruction has been fulfilled!
	ALL_IS_LOST_THE_PROPHECY_OF_DESTRUCTION_HAS_BEEN_FULFILLED(1000416),
	// Text: The end of time has come! The prophecy of destruction has been
	// fulfilled!
	THE_END_OF_TIME_HAS_COME_THE_PROPHECY_OF_DESTRUCTION_HAS_BEEN_FULFILLED(1000417),
	// Text: $s1! You bring an ill wind!
	S1_YOU_BRING_AN_ILL_WIND(1000418),
	// Text: $s1! You might as well give up!
	S1_YOU_MIGHT_AS_WELL_GIVE_UP(1000419),
	// Text: You don't have any hope! Your end has come!
	YOU_DONT_HAVE_ANY_HOPE_YOUR_END_HAS_COME(1000420),
	// Text: The prophecy of darkness has been fulfilled!
	THE_PROPHECY_OF_DARKNESS_HAS_BEEN_FULFILLED(1000421),
	// Text: As foretold in the prophecy of darkness, the era of chaos has begun!
	AS_FORETOLD_IN_THE_PROPHECY_OF_DARKNESS_THE_ERA_OF_CHAOS_HAS_BEGUN(1000422),
	// Text: The prophecy of darkness has come to pass!
	THE_PROPHECY_OF_DARKNESS_HAS_COME_TO_PASS(1000423),
	// Text: $s1! I give you the Blessing of Revelations!
	S1_I_GIVE_YOU_THE_BLESSING_OF_REVELATIONS(1000424),
	// Text: $s1! I bestow upon you the authority of the abyss!
	S1_I_BESTOW_UPON_YOU_THE_AUTHORITY_OF_THE_ABYSS(1000425),
	// Text: Herald of the new era, open your eyes!
	HERALD_OF_THE_NEW_ERA_OPEN_YOUR_EYES(1000426),
	// The day of judgment is near!
	THE_DAY_OF_JUDGMENT_IS_NEAR(1000305),
	// I bestow upon you a blessing!
	I_BESTOW_UPON_YOU_A_BLESSING(1000306),
	// A curse upon you!
	A_CURSE_UPON_YOU(1000304),
	// Text: The defenders of $s1 castle will be teleported to the inner castle.
	THE_DEFENDERS_OF_S1_CASTLE_WILL_BE_TELEPORTED_TO_THE_INNER_CASTLE(1000443),
	// Text: Competition
	COMPETITION(1000507),
	// Text: Seal Validation
	SEAL_VALIDATION(1000508),
	// Text: Preparation
	PREPARATION(1000509),
	// Text: Dusk
	DUSK(1000510),
	// Text: Dawn
	DAWN(1000511),
	// Text: No Owner
	NO_OWNER(1000512),
	// Text: **unregistered**
	__UNREGISTERED__(1000495),
	// Arrogant fool! You dare to challenge me, the Ruler of Flames? Here is your
	// reward!
	VALAKAS_ARROGAANT_FOOL_YOU_DARE_TO_CHALLENGE_ME(1000519),
	// Valakas finds your attacks to be annoying and disruptive to his
	// concentration. Keep it up!
	VALAKAS_FINDS_YOU_ATTACKS_ANNOYING_SILENCE(1801071),
	// Valakas' P.Def. is momentarily decreased because a warrior sliced a great
	// gash in his side!
	VALAKAS_PDEF_ISM_DECREACED_SLICED_DASH(1801072),
	// Your attacks have overwhelmed Valakas, momentarily distracting him from his
	// rage! Now's the time to attack!
	VALAKAS_OVERWHELMED_BY_ATTACK_NOW_TIME_ATTACK(1801073),
	// Your ranged attacks are provoking Valakas. If this continues, you might find
	// yourself in a dangerous situation.
	VALAKAS_RANGED_ATTACKS_PROVOKED(1801074),
	// Your sneaky counterattacks have heightened Valakas' rage, increasing his
	// attack power.
	VALAKAS_HEIGHTENED_BY_COUNTERATTACKS(1801075),
	// Your ranged attacks have enraged Valakas, causing him to attack his target
	// relentlessly.
	VALAKAS_RANGED_ATTACKS_ENRAGED_TARGET_FREE(1801076),
	// The evil Fire Dragon Valakas has been defeated!
	VALAKAS_THE_EVIL_FIRE_DRAGON_VALAKAS_DEFEATED(1900151),
	// You cannot hope to defeat me with your meager strength.
	ANTHARAS_YOU_CANNOT_HOPE_TO_DEFEAT_ME(1000520),
	// The evil Land Dragon Antharas has been defeated!
	ANTHARAS_THE_EVIL_LAND_DRAGON_ANTHARAS_DEFEATED(1900150),
	// Earth energy is gathering near Antharas's legs.
	ANTHARAS_EARTH_ENERGY_GATHERING_LEGS(1900155),
	// Antharas starts to absorb the earth energy.
	ANTHARAS_STARTS_ABSORB_EARTH_ENERGY(1900156),
	// Antharas raises its thick tail.
	ANTHARAS_RAISES_ITS_THICK_TAIL(1900157),
	// You are overcome by the strength of Antharas.
	ANTHARAS_YOU_ARE_OVERCOME_(1900158),
	// Antharas's eyes are filled with rage.
	ANTHARAS_EYES_FILLED_WITH_RAGE(1900159),
	// Text: Requiem of Hatred
	REQUIEM_OF_HATRED(1000522),
	// Text: Fugue of Jubilation
	FUGUE_OF_JUBILATION(1000523),
	// Text: Frenetic Toccata
	FRENETIC_TOCCATA(1000524),
	// Text: Hypnotic Mazurka
	HYPNOTIC_MAZURKA(1000525),
	// Text: Mournful Chorale Prelude
	MOURNFUL_CHORALE_PRELUDE(1000526),
	// Text: Rondo of Solitude
	RONDO_OF_SOLITUDE(1000527),
	// Text: Gludio
	GLUDIO(1001001),
	// Text: Dion
	DION(1001002),
	// Text: Giran
	GIRAN(1001003),
	// Text: Oren
	OREN(1001004),
	// Text: Aden
	ADEN(1001005),
	// Text: Innadril
	INNADRIL(1001006),
	// Text: The Kingdom of Elmore
	THE_KINGDOM_OF_ELMORE(1001100),
	// Text: Goddard
	GODDARD(1001007),
	// Text: Rune
	RUNE(1001008),
	// Text: Schuttgart
	SCHUTTGART(1001009),
	// Text: A delivery for Mr. Lector? Very good!
	A_DELIVERY_FOR_MR(1010201),
	// Text: I need a break!
	I_NEED_A_BREAK(1010202),
	// Text: Hello, Mr. Lector! Long time no see, Mr. Jackson!
	HELLO_MR(1010203),
	// Text: Lulu!
	LULU(1010204),
	// Text: Where has he gone?
	WHERE_HAS_HE_GONE(1010205),
	// Text: Have you seen Windawood?
	HAVE_YOU_SEEN_WINDAWOOD(1010206),
	// Text: Where did he go?
	WHERE_DID_HE_GO(1010207),
	// Text: A black moon... Now do you understand that he has opened his eyes?
	A_BLACK_MOON_NOW_DO_YOU_UNDERSTAND_THAT_HE_HAS_OPENED_HIS_EYES(1010221),
	// Text: Clouds of blood are gathering. Soon, it will start to rain. The rain of
	// crimson blood...
	CLOUDS_OF_BLOOD_ARE_GATHERING_SOON_IT_WILL_START_TO_RAIN_THE_RAIN_OF_CRIMSON_BLOOD(1010222),
	// Text: While the foolish light was asleep, the darkness will awaken first. Uh
	// huh huh...
	WHILE_THE_FOOLISH_LIGHT_WAS_ASLEEP_THE_DARKNESS_WILL_AWAKEN_FIRST_UH(1010223),
	// Text: It is the deepest darkness. With its arrival, the world will soon die.
	IT_IS_THE_DEEPEST_DARKNESS_WITH_ITS_ARRIVAL_THE_WORLD_WILL_SOON_DIE(1010224),
	// Text: Death is just a new beginning. Huhu... Fear not.
	DEATH_IS_JUST_A_NEW_BEGINNING_HUHU_FEAR_NOT(1010225),
	// Text: Ahh! Beautiful goddess of death! Cover over the filth of this world
	// with your darkness!
	AHH_BEAUTIFUL_GODDES_OF_DEATH_COVER_OVER_THE_FILTH_OF_THOS_WORLD_YOUR_DARKNESS(1010226),
	// Text: The goddess's resurrection has already begun. Huhu... Insignificant
	// creatures like you can do nothing!
	THE_GODDESS_RESURRECTION_HAS_ALREADY_BEGUN_HUHU_INSIGNIFICANT_CREATURES_LIKE_YOU_CAN_DO_NOTHING(1010227),
	// Text: Who dares to covet the throne of our castle! Leave immediately or you
	// will pay the price of your audacity with your very own blood!
	WHO_DARES_TO_COVET_THE_THRONE_OF_OUR_CASTLE__LEAVE_IMMEDIATELY_OR_YOU_WILL_PAY_THE_PRICE_OF_YOUR_AUDACITY_WITH_YOUR_VERY_OWN_BLOOD(
																																		1010623),
	// Text: Hmm, those who are not of the bloodline are coming this way to take
	// over the castle?! Humph! The bitter grudges of the dead. You must not make
	// light of their power!
	HMM_THOSE_WHO_ARE_NOT_OF_THE_BLOODLINE_ARE_COMING_THIS_WAY_TO_TAKE_OVER_THE_CASTLE__HUMPH__THE_BITTER_GRUDGES_OF_THE_DEAD(
																																1010624),
	// Text: Aargh...! If I die, then the magic force field of blood will...!
	AARGH_IF_I_DIE_THEN_THE_MAGIC_FORCE_FIELD_OF_BLOOD_WILL(1010625),
	// Text: It's not over yet... It won't be... over... like this... Never...
	ITS_NOT_OVER_YET__IT_WONT_BE__OVER__LIKE_THIS__NEVER(1010626),
	// Text: Oooh! Who poured nectar on my head while I was sleeping?
	OOOH_WHO_POURED_NECTAR_ON_MY_HEAD_WHILE_I_WAS_SLEEPING(1010627),
	// Text: Undecided
	UNDECIDED(1010635),
	// Text: Heh Heh... I see that the feast has begun! Be wary! The curse of the
	// Hellmann family has poisoned this land!
	HEH_HEH_I_SEE_THAT_THE_FEAST_HAS_BEGAN_BE_WARY_THE_CURSE_OF_THE_HELLMANN_FAMILY_HAS_POISONED_THIS_LAND(1010636),
	// Text: Arise, my faithful servants! You, my people who have inherited the
	// blood. It is the calling of my daughter. The feast of blood will now begin!
	ARISE_MY_FAITHFUL_SERVANTS_YOU_MY_PEOPLE_WHO_HAVE_INHERITED_THE_BLOOD(1010637),
	// Text: Grarr! For the next 2 minutes or so, the game arena are will be
	// cleaned. Throw any items you don't need to the floor now.
	GRARR_FOR_THE_NEXT_2_MINUTES_OR_SO_THE_GAME_ARENA_ARE_WILL_BE_CLEANED(1010639),
	// Text: Weapons have been added to your Inventory.
	WEAPONS_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY(1032201),
	// Text: You can see the Statues of Heroes inside the Museum.
	YOU_CAN_SEE_THE_STATUES_OF_HEROES_INSIDE_THE_MUSEUM(1032333),
	// Text: One day a statue of you could be made.
	ONE_DAY_A_STATUE_OF_YOU_COULD_BE_MADE(1032334),
	// Text: You can view the Museum Statistics from the System Menu.
	YOU_CAN_VIEW_THE_MUSEUM_STATISTICS_FROM_THE_SYSTEM_MENU(1032335),
	// Text: When you go to the Museum, speak to Pantheon.
	WHEN_YOU_GO_TO_THE_MUSEUM_SPEAK_TO_PANTHEON(1032336),
	// Text: Some folks don't know what they are doing.
	SOME_FOLKS_DONT_KNOW_WHAT_THEY_ARE_DOING(1032337),
	// Text: Don't know what to do? Look at the map.
	DONT_KNOW_WHAT_TO_DO_LOOK_AT_THE_MAP(1032338),
	// Text: Do you see a scroll icon? Go that location.
	DO_YOU_SEE_A_SCROLL_ICON_GO_THAT_LOCATION(1032339),
	// Text: Match begins in $s1 minute(s). Please gather around the administrator.
	MATCH_BEGINS_IN_S1_MINUTES(1800080),
	// Text: The match is automatically canceled because you are too far from the
	// admission manager.
	THE_MATCH_IS_AUTOMATICALLY_CANCELED_BECAUSE_YOU_ARE_TOO_FAR_FROM_THE_ADMISSION_MANAGER(1800081),
	// Text: Match cancelled. Opponent did not meet the stadium admission
	// requirements.
	MATCH_CANCELLED(1800123),
	// Text: Begin stage 1
	BEGIN_STAGE_1_FREYA(1801086),
	// Text: Begin stage 2
	BEGIN_STAGE_2_FREYA(1801087),
	// Text: Begin stage 3
	BEGIN_STAGE_3_FREYA(1801088),
	// Text: Begin stage 4
	BEGIN_STAGE_4_FREYA(1801089),
	// Text: Time remaining until next battle
	TIME_REMAINING_UNTIL_NEXT_BATTLE(1801090),
	// Text: Freya has started to move.
	FREYA_HAS_STARTED_TO_MOVE(1801097),
	// Text: $s1 of Balance
	S1_OF_BALANCE(1801100),
	// Text: Swift $s1
	SWIFT_S1(1801101),
	// Text: $s1 of Blessing
	S1_OF_BLESSING(1801102),
	// Text: Sharp $s1
	SHARP_S1(1801103),
	// Text: Useful $s1
	USEFUL_S1(1801104),
	// Text: Reckless $s1
	RECKLESS_S1(1801105),
	// Text: Alpen Kookaburra
	ALPEN_KOOKABURRA(1801106),
	// Text: Alpen Cougar
	ALPEN_COUGAR(1801107),
	// Text: Alpen Buffalo
	ALPEN_BUFFALO(1801108),
	// Text: Alpen Grendel
	ALPEN_GRENDEL(1801109),
	// Text: We have broken through the gate! Destroy the encampment and move to the
	// Command Post!
	WE_HAVE_BROKEN_THROUGH_THE_GATE_DESTROY_THE_ENCAMPMENT_AND_MOVE_TO_THE_COMMAND_POST(1300001),
	// Text: The command gate has opened! Capture the flag quickly and raise it high
	// to proclaim our victory!
	THE_COMMAND_GATE_HAS_OPENED_CAPTURE_THE_FLAG_QUICKLY_AND_RAISE_IT_HIGH_TO_PROCLAIM_OUR_VICTORY(1300002),
	// Text: The gods have forsaken us... Retreat!!
	THE_GODS_HAVE_FORSAKEN_US__RETREAT(1300003),
	// Text: You may have broken our arrows, but you will never break our will!
	// Archers, retreat!
	YOU_MAY_HAVE_BROKEN_OUR_ARROWS_BUT_YOU_WILL_NEVER_BREAK_OUR_WILL_ARCHERS_RETREAT(1300004),
	// Text: At last! The Magic Field that protects the fortress has weakened!
	// Volunteers, stand back!
	AT_LAST_THE_MAGIC_FIELD_THAT_PROTECTS_THE_FORTRESS_HAS_WEAKENED_VOLUNTEERS_STAND_BACK(1300005),
	// Text: Aiieeee! Command Center! This is guard unit! We need backup right away!
	AIIEEEE_COMMAND_CENTER_THIS_IS_GUARD_UNIT_WE_NEED_BACKUP_RIGHT_AWAY(1300006),
	// Text: Fortress power disabled.
	FORTRESS_POWER_DISABLED(1300007),
	// Text: Machine No. 1 - Power Off!
	MACHINE_NO_1_POWER_OFF(1300009),
	// Text: Machine No. 2 - Power Off!
	MACHINE_NO_2_POWER_OFF(1300010),
	// Text: Machine No. 3 - Power Off!
	MACHINE_NO_3_POWER_OFF(1300011),
	// Text: Spirit of Fire, unleash your power! Burn the enemy!!
	SPIRIT_OF_FIRE_UNLEASH_YOUR_POWER_BURN_THE_ENEMY(1300014),
	// Text: Do you need my power? You seem to be struggling.
	DO_YOU_NEED_MY_POWER_YOU_SEEM_TO_BE_STRUGGLING(1300016),
	// Text: Don't think that it's gonna end like this. Your ambition will soon be
	// destroyed as well.
	DONT_THINK_THAT_ITS_GONNA_END_LIKE_THIS(1300018),
	// Text: I feel so much grief that I can't even take care of myself. There isn't
	// any reason for me to stay here any longer.
	I_FEEL_SO_MUCH_GRIEF_THAT_I_CANT_EVEN_TAKE_CARE_OF_MYSELF(1300020),
	// Text: Independent State
	INDEPENDENT_STATE(1300122),
	// Text: Nonpartisan
	NONPARTISAN(1300123),
	// Text: Contract State
	CONTRACT_STATE(1300124),
	// Text: First password has been entered.
	FIRST_PASSWORD_HAS_BEEN_ENTERED(1300125),
	// Text: Second password has been entered.
	SECOND_PASSWORD_HAS_BEEN_ENTERED(1300126),
	// Text: Password has not been entered.
	PASSWORD_HAS_NOT_BEEN_ENTERED(1300127),
	// Text: Attempt $s1 / 3 is in progress. => This is the third attempt on $s1.
	ATTEMPT_S1__3_IS_IN_PROGRESS(1300128),
	// Text: The 1st Mark is correct.
	THE_1ST_MARK_IS_CORRECT(1300129),
	// Text: The 2nd Mark is correct.
	THE_2ND_MARK_IS_CORRECT(1300130),
	// Text: The Marks have not been assembled.
	THE_MARKS_HAVE_NOT_BEEN_ASSEMBLED(1300131),
	// Text: Olympiad class-free team match is going to begin in Arena $s1 in a
	// moment.
	OLYMPIAD_CLASSFREE_TEAM_MATCH_IS_GOING_TO_BEGIN_IN_ARENA_S1_IN_A_MOMENT(1300132),
	// Text: Domain Fortress
	DOMAIN_FORTRESS(1300133),
	// Text: Boundary Fortress
	BOUNDARY_FORTRESS(1300134),
	// Text: $s1hour $s2minute
	S1HOUR_S2MINUTE(1300135),
	// Text: Begin stage 1!
	BEGIN_STAGE_1(1300150),
	// Text: Begin stage 2!
	BEGIN_STAGE_2(1300151),
	// Text: Begin stage 3!
	BEGIN_STAGE_3(1300152),
	// Text: What a predicament... my attempts were unsuccessful.
	WHAT_A_PREDICAMENT_MY_ATTEMPTS_WERE_UNSUCCESSUFUL(1300162),
	// Text: Courage! Ambition! Passion! Mercenaries who want to realize their dream
	// of fighting in the territory war, come to me! Fortune and glory are waiting
	// for you!
	COURAGE_AMBITION_PASSION_MERCENARIES_WHO_WANT_TO_REALIZE_THEIR_DREAM_OF_FIGHTING_IN_THE_TERRITORY_WAR_COME_TO_ME_FORTUNE_AND_GLORY_ARE_WAITING_FOR_YOU(
																																							1300163),
	// Text: Do you wish to fight? Are you afraid? No matter how hard you try, you
	// have nowhere to run. But if you face it head on, our mercenary troop will
	// help you out!
	DO_YOU_WISH_TO_FIGHT_ARE_YOU_AFRAID_NO_MATTER_HOW_HARD_YOU_TRY_YOU_HAVE_NOWHERE_TO_RUN(1300164),
	// Text: Charge! Charge! Charge!
	CHARGE_CHARGE_CHARGE(1300165),
	// Text: Olympiad class-free individual match is going to begin in Arena $s1 in
	// a moment.
	OLYMPIAD_CLASSFREE_INDIVIDUAL_MATCH_IS_GOING_TO_BEGIN_IN_ARENA_S1_IN_A_MOMENT(1300166),
	// Text: Olympiad class individual match is going to begin in Arena $s1 in a
	// moment.
	OLYMPIAD_CLASS_INDIVIDUAL_MATCH_IS_GOING_TO_BEGIN_IN_ARENA_S1_IN_A_MOMENT(1300167),
	// Text: Begin Tutorial Quests
	BEGIN_TUTORIAL_QUESTS(1600026),
	// Text: Newbie Helper has casted buffs on $s1.
	NEWBIE_HELPER_HAS_CASTED_BUFFS_ON_S1(1600027),
	// Text: The airship has been summoned. It will automatically depart in 5
	// minutes.
	AIRSHIP_IS_SUMMONED_IS_DEPART_IN_5_MINUTES(1800219),
	// Text: The regularly scheduled airship has arrived. It will depart for the
	// Aden continent in 1 minute.
	AIRSHIP_IS_ARRIVED_IT_WILL_DEPART_TO_ADEN_IN_1_MINUTE(1800220),
	// Text: The regularly scheduled airship that flies to the Aden continent has
	// departed.
	AIRSHIP_IS_DEPARTED_TO_ADEN(1800221),
	// Text: The regularly scheduled airship has arrived. It will depart for the
	// Gracia continent in 1 minute.
	AIRSHIP_IS_ARRIVED_IT_WILL_DEPART_TO_GRACIA_IN_1_MINUTE(1800222),
	// Text: The regularly scheduled airship that flies to the Gracia continent has
	// departed.
	AIRSHIP_IS_DEPARTED_TO_GRACIA(1800223),
	// Text: Another airship has been summoned to the wharf. Please try again later.
	IN_AIR_HARBOR_ALREADY_AIRSHIP_DOCKED_PLEASE_WAIT_AND_TRY_AGAIN(1800224),
	// Text: Attack
	ATTACK(1800243),
	// Text: Defend
	DEFEND(1800244),
	// Text: Maguen appearance!!!
	MAGUEN_APPEARANCE(1801149),
	// Text: There are 5 minutes remaining to register for Kratei's cube match.
	THERE_ARE_5_MINUTES_REMAINING_TO_REGISTER_FOR_KRATEIS_CUBE_MATCH(1800203),
	// Text: There are 3 minutes remaining to register for Kratei's cube match.
	THERE_ARE_3_MINUTES_REMAINING_TO_REGISTER_FOR_KRATEIS_CUBE_MATCH(1800204),
	// Text: There are 1 minutes remaining to register for Kratei's cube match.
	THERE_ARE_1_MINUTES_REMAINING_TO_REGISTER_FOR_KRATEIS_CUBE_MATCH(1800205),
	// Text: The match will begin in $s1 minute(s).
	THE_MATCH_WILL_BEGIN_IN_S1_MINUTES(1800206),
	// Text: The match will begin shortly.
	THE_MATCH_WILL_BEGIN_SHORTLY(1800207),
	// Text: Registration for the next match will end at %s minutes after the hour.
	REGISTRATION_FOR_THE_NEXT_MATCH_WILL_END_AT_S1_MINUTES_AFTER_HOUR(1800208),
	// Text: Even though you bring something called a gift among your humans, it
	// would just be problematic for me...
	EVEN_THOUGH_YOU_BRING_SOMETHING_CALLED_A_GIFT_AMONG_YOUR_HUMANS_IT_WOULD_JUST_BE_PROBLEMATIC_FOR_ME(1801190),
	// Text: I just don't know what expression I should have it appeared on me. Are
	// human's emotions like this feeling?
	I_JUST_DONT_KNOW_WHAT_EXPRESSION_I_SHOULD_HAVE_IT_APPEARED_ON_ME(1801191),
	// Text: The feeling of thanks is just too much distant memory for me...
	THE_FEELING_OF_THANKS_IS_JUST_TOO_MUCH_DISTANT_MEMORY_FOR_ME(1801192),
	// Text: But I kind of miss it... Like I had felt this feeling before...
	BUT_I_KIND_OF_MISS_IT(1801193),
	// Text: I am Ice Queen Freya... This feeling and emotion are nothing but a part
	// of Melissa'a memories.
	I_AM_ICE_QUEEN_FREYA(1801194),
	// Text: Dear $s1... Think of this as my appreciation for the gift. Take this
	// with you. There's nothing strange about it. It's just a bit of my
	// capriciousness...
	DEAR_S1(1801195),
	// Text: Rulers of the seal! I bring you wondrous gifts!
	RULERS_OF_THE_SEAL_I_BRING_YOU_WONDROUS_GIFTS(1000431),
	// Text: Rulers of the seal! I have some excellent weapons to show you!
	RULERS_OF_THE_SEAL_I_HAVE_SOME_EXCELLENT_WEAPONS_TO_SHOW_YOU(1000432),
	// Text: I've been so busy lately, in addition to planning my trip!
	IVE_BEEN_SO_BUSY_LATELY_IN_ADDITION_TO_PLANNING_MY_TRIP(1000433),
	// Text: If you have items, please give them to me.
	IF_YOU_HAVE_ITEMS_PLEASE_GIVE_THEM_TO_ME(1800279),
	// Text: My stomach is empty.
	MY_STOMACH_IS_EMPTY(1800280),
	// Text: I'm hungry, I'm hungry!
	IM_HUNGRY_IM_HUNGRY(1800281),
	// Text: I'm still not full...
	IM_STILL_NOT_FULL(1800282),
	// Text: I'm still hungry~
	IM_STILL_HUNGRY(1800283),
	// Text: I feel a little woozy...
	I_FEEL_A_LITTLE_WOOZY(1800284),
	// Text: Give me something to eat.
	GIVE_ME_SOMETHING_TO_EAT(1800285),
	// Text: Now it's time to eat~
	NOW_ITS_TIME_TO_EAT(1800286),
	// Text: I also need a dessert.
	I_ALSO_NEED_A_DESSERT(1800287),
	// Text: I'm still hungry.
	IM_STILL_HUNGRY_(1800288),
	// Text: I'm full now, I don't want to eat anymore.
	IM_FULL_NOW_I_DONT_WANT_TO_EAT_ANYMORE(1800289),
	// Text: Elapsed Time :
	ELAPSED_TIME(1911119),
	// Text: Time Remaining :
	TIME_REMAINING(1911120),
	// Text: Strong magic power can be felt from somewhere!!
	I_FEEL_STRONG_MAGIC_FLOW(1801111),
	// Text: I haven't eaten anything, I'm so weak~
	I_HAVENT_EATEN_ANYTHING_IM_SO_WEAK(1800290),
	// Text: We must search high and low in every room for the reading desk that
	// contains the book we seek.
	WE_MUST_SEARCH_HIGH_AND_LOW_IN_EVERY_ROOM_FOR_THE_READING_DESK_THAT_CONTAINS_THE_BOOK_WE_SEEK(1029450),
	// Text: Remember the content of the books that you found. You can't take them
	// out with you.
	REMEMBER_THE_CONTENT_OF_THE_BOOKS_THAT_YOU_FOUND(1029451),
	// Text: It seems that you cannot remember to the room of the watcher who found
	// the book.
	IT_SEEMS_THAT_YOU_CANNOT_REMEMBER_TO_THE_ROOM_OF_THE_WATCHER_WHO_FOUND_THE_BOOK(1029452),
	// Text: Your work here is done, so return to the central guardian.
	YOUR_WORK_HERE_IS_DONE_SO_RETURN_TO_THE_CENTRAL_GUARDIAN(1029453),
	// Text: The guardian of the seal doesn't seem to get injured at all until the
	// barrier is destroyed.
	THE_GUARDIAN_OF_THE_SEAL_DOESNT_SEEM_TO_GET_INJURED_AT_ALL_UNTIL_THE_BARRIER_IS_DESTROYED(1029550),
	// Text: The device located in the room in front of the guardian of the seal is
	// definitely the barrier that controls the guardian's power.
	THE_DEVICE_LOCATED_IN_THE_ROOM_IN_FRONT_OF_THE_GUARDIAN_OF_THE_SEAL_IS_DEFINITELY_THE_BARRIER_THAT_CONTROLS_THE_GUARDIANS_POWER(
																																	1029551),
	// Text: To remove the barrier, you must find the relics that fit the barrier
	// and activate the device.
	TO_REMOVE_THE_BARRIER_YOU_MUST_FIND_THE_RELICS_THAT_FIT_THE_BARRIER_AND_ACTIVATE_THE_DEVICE(1029552),
	// Text: All the guardians were defeated, and the seal was removed. Teleport to
	// the center.
	ALL_THE_GUARDIANS_WERE_DEFEATED_AND_THE_SEAL_WAS_REMOVED(1029553),
	// Text: What took so long? I waited for ever.
	WHAT_TOOK_SO_LONG_I_WAITED_FOR_EVER(1029350),
	// Text: I must ask Librarian Sophia about the book.
	I_MUST_ASK_LIBRARIAN_SOPHIA_ABOUT_THE_BOOK(1029351),
	// Text: This library... It's huge but there aren't many useful books, right?
	THIS_LIBRARY(1029352),
	// Text: An underground library... I hate damp and smelly places...
	AN_UNDERGROUND_LIBRARY(1029353),
	// Text: The book that we seek is certainly here. Search inch by inch.
	THE_BOOK_THAT_WE_SEEK_IS_CERTAINLY_HERE(1029354),
	// Text: You foolish invaders who disturb the rest of Solina, be gone from this
	// place.
	YOU_FOOLISH_INVADERS_WHO_DISTURB_THE_REST_OF_SOLINA_BE_GONE_FROM_THIS_PLACE(1029460),
	// Text: I know not what you seek, but this truth cannot be handled by mere
	// humans.
	I_KNOW_NOT_WHAT_YOU_SEEK_BUT_THIS_TRUTH_CANNOT_BE_HANDLED_BY_MERE_HUMANS(1029461),
	// Text: I will not stand by and watch your foolish actions. I warn you, leave
	// this place at once.
	I_WILL_NOT_STAND_BY_AND_WATCH_YOUR_FOOLISH_ACTIONS(1029462),
	// Text: View our wide variety of accessories.
	VIEW_OUR_WIDE_VARIETY_OF_ACCESSORIES(1032319),
	// Text: The best weapon doesn't make you the best.
	THE_BEST_WEAPON_DOESNT_MAKE_YOU_THE_BEST(1032320),
	// Text: We buy and sell. Come take a look.
	WE_BUY_AND_SELL_COME_TAKE_A_LOOK(1032321),
	// Text: Hey! Did you speak with Pantheon?
	HEY_DID_YOU_SPEAK_WITH_PANTHEON(1032346),
	// Text: Everyone needs to meet Pantheon first before hunting.
	EVERYONE_NEEDS_TO_MEET_PANTHEON_FIRST_BEFORE_HUNTING(1032347),
	// Text: Come and eat.
	COME_AND_EAT(1801117),
	// Text: Looks delicious.
	LOOKS_DELICIOUS(1801118),
	// Text: Let's go eat.
	LETS_GO_EAT(1801119),
	// Text: Hall of Suffering
	HALL_OF_SUFFERING(1800240),
	// Text: Hall of Erosion
	HALL_OF_EROSION(1800241),
	// Text: Heart of Immortality
	HEART_OF_IMMORTALITY(1800242),
	// Text: You can hear the undead of Ekimus rushing toward you. $s1 $s2, it has
	// now begun!
	YOU_CAN_HEAR_THE_UNDEAD_OF_EKIMUS_RUSHING_TOWARD_YOU(1800263),
	// Text: The tumor inside $s1 has been destroyed! \nIn order to draw out the
	// cowardly Cohemenes, you must destroy all the tumors!
	THE_TUMOR_INSIDE_S1_HAS_BEEN_DESTROYED_NIN_ORDER_TO_DRAW_OUT_THE_COWARDLY_COHEMENES_YOU_MUST_DESTROY_ALL_THE_TUMORS(
																														1800274),
	// Text: The tumor inside $s1 has completely revived. \nThe restrengthened
	// Cohemenes has fled deeper inside the seed...
	THE_TUMOR_INSIDE_S1_HAS_COMPLETELY_REVIVED(1800275),
	// Text: All the tumors inside $s1 have been destroyed! Driven into a corner,
	// Cohemenes appears close by!
	ALL_THE_TUMORS_INSIDE_S1_HAVE_BEEN_DESTROYED_DRIVEN_INTO_A_CORNER_COHEMENES_APPEARS_CLOSE_BY(1800299),
	// Text: $s1's party has moved to a different location through the crack in the
	// tumor!
	S1S_PARTY_HAS_MOVED_TO_A_DIFFERENT_LOCATION_THROUGH_THE_CRACK_IN_THE_TUMOR(1800247),
	// Text: $s1's party has entered the Chamber of Ekimus through the crack in the
	// tumor!
	S1S_PARTY_HAS_ENTERED_THE_CHAMBER_OF_EKIMUS_THROUGH_THE_CRACK_IN_THE_TUMOR(1800248),
	// Text: Ekimus has sensed abnormal activity. \nThe advancing party is
	// forcefully expelled!
	EKIMUS_HAS_SENSED_ABNORMAL_ACTIVITY(1800249),
	// Text: C'mon, c'mon! Show your face, you little rats! Let me see what the
	// doomed weaklings are scheming!
	CMON_CMON_SHOW_YOUR_FACE_YOU_LITTLE_RATS_LET_ME_SEE_WHAT_THE_DOOMED_WEAKLINGS_ARE_SCHEMING(1800233),
	// Text: Impressive.... Hahaha it's so much fun, but I need to chill a little
	// while. Argekunte, clear the way!
	IMPRESSIVE(1800234),
	// Text: Kyahaha! Since the tumor has been resurrected, I no longer need to
	// waste my time on you!
	KYAHAHA_SINCE_THE_TUMOR_HAS_BEEN_RESURRECTED_I_NO_LONGER_NEED_TO_WASTE_MY_TIME_ON_YOU(1800235),
	// Text: Keu... I will leave for now... But don't think this is over... The Seed
	// of Infinity can never die...
	KEU(1800236),
	// Text: $s1 minute(s) are remaining.
	S1_MINUTES_ARE_REMAINING(1010643),
	// Text: Congratulations! You have succeeded at $s1 $s2! The instance will
	// shortly expire.
	CONGRATULATIONS_YOU_HAVE_SUCCEEDED_AT_S1_S2_THE_INSTANCE_WILL_SHORTLY_EXPIRE(1800245),
	// Text: You have failed at $s1 $s2... The instance will shortly expire.
	YOU_HAVE_FAILED_AT_S1_S2(1800246),
	// Text: You will participate in $s1 $s2 shortly. Be prepared for anything.
	YOU_WILL_PARTICIPATE_IN_S1_S2_SHORTLY(1800262),
	// Text: I shall accept your challenge, $s1! Come and die in the arms of
	// immortality!
	I_SHALL_ACCEPT_YOUR_CHALLENGE_S1_COME_AND_DIE_IN_THE_ARMS_OF_IMMORTALITY(1800261),
	// Text: The tumor inside $s1 that has provided energy \n to Ekimus is
	// destroyed!
	THE_TUMOR_INSIDE_S1_THAT_HAS_PROVIDED_ENERGY_N_TO_EKIMUS_IS_DESTROYED(1800302),
	// Text: The tumor inside $s1 has been completely resurrected \n and started to
	// energize Ekimus again...
	THE_TUMOR_INSIDE_S1_HAS_BEEN_COMPLETELY_RESURRECTED_N_AND_STARTED_TO_ENERGIZE_EKIMUS_AGAIN(1800303),
	// Text: With all connections to the tumor severed, Ekimus has lost its power to
	// control the Feral Hound!
	WITH_ALL_CONNECTIONS_TO_THE_TUMOR_SEVERED_EKIMUS_HAS_LOST_ITS_POWER_TO_CONTROL_THE_FERAL_HOUND(1800269),
	// Text: With the connection to the tumor restored, Ekimus has regained control
	// over the Feral Hound...
	WITH_THE_CONNECTION_TO_THE_TUMOR_RESTORED_EKIMUS_HAS_REGAINED_CONTROL_OVER_THE_FERAL_HOUND(1800270),
	// Text: There is no party currently challenging Ekimus. \n If no party enters
	// within $s1 seconds, the attack on the Heart of Immortality will fail...
	THERE_IS_NO_PARTY_CURRENTLY_CHALLENGING_EKIMUS(1800229),
	// Text: You can feel the surging energy of death from the tumor.
	YOU_CAN_FEEL_THE_SURGING_ENERGY_OF_DEATH_FROM_THE_TUMOR(1800264),
	// Text: The area near the tumor is full of ominous energy.
	THE_AREA_NEAR_THE_TUMOR_IS_FULL_OF_OMINOUS_ENERGY(1800265),
	// Text: The tumor inside $s1 has been destroyed! \nThe nearby Undead that were
	// attacking Seed of Life start losing their energy and run away!
	THE_TUMOR_INSIDE_S1_HAS_BEEN_DESTROYED_NTHE_NEARBY_UNDEAD_THAT_WERE_ATTACKING_SEED_OF_LIFE_START_LOSING_THEIR_ENERGY_AND_RUN_AWAY(
																																		1800300),
	// Text: The tumor inside $s1 has completely revived. \nRecovered nearby Undead
	// are swarming toward Seed of Life...
	THE_TUMOR_INSIDE_S1_HAS_COMPLETELY_REVIVED_(1800301),
	// Text: The tumor inside $s1 has been destroyed! \nThe speed that Ekimus calls
	// out his prey has slowed down!
	THE_TUMOR_INSIDE_S1_HAS_BEEN_DESTROYED_NTHE_SPEED_THAT_EKIMUS_CALLS_OUT_HIS_PREY_HAS_SLOWED_DOWN(1800304),
	// Text: The tumor inside $s1 has completely revived. \nEkimus started to regain
	// his energy and is desperately looking for his prey...
	THE_TUMOR_INSIDE_S1_HAS_COMPLETELY_REVIVED__(1800305),
	// Text: Bring more, more souls...!
	BRING_MORE_MORE_SOULS(1800306),
	// Text: Hurry hurry
	HURRY_HURRY(1800882),
	// Text: I am not that type of person who stays in one place for a long time
	I_AM_NOT_THAT_TYPE_OF_PERSON_WHO_STAYS_IN_ONE_PLACE_FOR_A_LONG_TIME(1800883),
	// Text: It's hard for me to keep standing like this
	ITS_HARD_FOR_ME_TO_KEEP_STANDING_LIKE_THIS(1800884),
	// Text: Why don't I go that way this time
	WHY_DONT_I_GO_THAT_WAY_THIS_TIME(1800885),
	// Text: Welcome!
	WELCOME(1800886),
	// Text: Ha, ha, ha!...
	HA_HA_HA(7164),
	// Text: The Soul Coffin has awakened Ekimus. If $s1 more Soul Coffin(s) are
	// created, the defense of the Heart of Immortality will fail...
	THE_SOUL_COFFIN_HAS_AWAKENED_EKIMUS(1800232),
	// Text: Yum-yum, yum-yum
	YUMYUM_YUMYUM(1800291),
	// Text: Hahaha... You dare to disrupt me... I will be your nightmare from which
	// you can never awaken!
	HAHAHA_YOU_DARE_TO_DISRUPT_ME_I_WILL_BE_YOUR_NIGHTMARE_FROM_WHICH_YOU_CAN_NEVER_AWAKEN(1801235),
	// Text: You dare attack me? I will fill your nightmares with blood!
	YOU_DARE_ATTACK_ME_I_WILL_FILL_YOUR_NIGHTMARES_WITH_BLOOD(1801236),
	// Text: I cannot let you stop the wraith of Shilen!
	I_CANNOT_LET_YOU_STOP_THE_WRAITH_OF_SHILEN(1801237),
	// Text: Ah…
	AH(1801238),
	// Text: Halt! Your nightmares will fill you with dread!
	HALT_YOUR_NIGHTMARES_WILL_FILL_YOU_WITH_DREAD(1801239),
	// Text: You won't get away!
	YOU_WONT_GET_AWAY(1801240),
	// Text: How... All that power... Removed...
	HOW_ALL_THAT_POWER_REMOVED(1801241),
	// Text: Shilen... I have failed...
	SHILEN_I_HAVE_FAILED(1801242),
	// Text: To think that I could fail… impossible.
	TO_THINK_THAT_I_COULD_FAIL_IMPOSSIBLE(1801243),
	// Text: Behind you! The enemy is ambushing you!
	BEHIND_YOU_THE_ENEMY_IS_AMBUSHING_YOU(1811194),
	// Text: Kill the guy messing with the Electric Device!
	KILL_THE_GUY_MESSING_WITH_THE_ELECTRIC_DEVICE(1811195),
	// Text: Focus on attacking the guy in the room!
	FOCUS_ON_ATTACKING_THE_GUY_IN_THE_ROOM(1811196),
	// Text: If Terain dies, the mission will fail.
	IF_TERAIN_DIES_THE_MISSION_WILL_FAIL(1811197),
	// Text: Mark of Belis can be acquired from enemies.\\nUse them in the Belis
	// Verification System
	MARK_OF_BELIS_CAN_BE_ACQUIRED_FROM_ENEMIESNUSE_THEM_IN_THE_BELIS_VERIFICATION_SYSTEM(1811199),
	// Text: Electronic device has been destroyed.
	ELECTRONIC_DEVICE_HAS_BEEN_DESTROYED(1811200),
	// Text: When the combat is difficult, I will help.
	WHEN_THE_COMBAT_IS_DIFFICULT_I_WILL_HELP(1811201),
	// Text: Free the Giant from his imprisonment and awaken your true power.
	FREE_THE_GIANT_FROM_HIS_IMPRISONMENT_AND_AWAKEN_YOUR_TRUE_POWER(1811216),
	// Text: Don't come back here!!
	DONT_COME_BACK_HERE(1811217),
	// Text: Talking Island Village is really beautiful.
	TALKING_ISLAND_VILLAGE_IS_REALLY_BEAUTIFUL(1811243),
	// Text: I haven't felt this good in ages.
	I_HAVENT_FELT_THIS_GOOD_IN_AGES(1811244),
	// Text: Alchemy is a science and an art.
	ALCHEMY_IS_A_SCIENCE_AND_AN_ART(1811245),
	// Text: Every race built a piece of this Village.
	EVERY_RACE_BUILT_A_PIECE_OF_THIS_VILLAGE(1811252),
	// Text: Weee!
	WEEE(1811253),
	// Text: Boys are so annoying.
	BOYS_ARE_SO_ANNOYING(1811254),
	// Text: Is it better to end destiny or start destiny…
	IS_IT_BETTER_TO_END_DESTINY_OR_START_DESTINY(1811291),
	// Text: And now your journey begins.
	AND_NOW_YOUR_JOURNEY_BEGINS(1811294),
	// Text: Speak with me about traveling around Aden.
	SPEAK_WITH_ME_ABOUT_TRAVELING_AROUND_ADEN(1811308),
	// Text: You cannot teleport while you are dead.
	YOU_CANNOT_TELEPORT_WHILE_YOU_ARE_DEAD(1811318),
	// Text: Remaining Time
	REMAINING_TIME(1811302),
	// Text: No! The Seal Controls have been exposed. Guards protect the Seal
	// Controls!
	NO_THE_SEAL_CONTROLS_HAVE_BEEN_EXPOSED_GUARDS_PROTECT_THE_SEAL_CONTROLS(1811223),
	// Text: Disable device will go out of control in 1 minute
	DISABLE_DEVICE_WILL_GO_OUT_OF_CONTROL_IN_1_MINUTE(1811226),
	// Text: 50 seconds are remaining.
	_50_SECONDS_ARE_REMAINING(1811227),
	// Text: 40 seconds are remaining.
	_40_SECONDS_ARE_REMAINING(1811228),
	// Text: 30 seconds are remaining.
	_30_SECONDS_ARE_REMAINING(1811229),
	// Text: 20 seconds are remaining.
	_20_SECONDS_ARE_REMAINING(1811230),
	// Text: 10 seconds are remaining.
	_10_SECONDS_ARE_REMAINING(1811231),
	// Text: 5 Seconds
	_5_SECONDS(1811232),
	// Text: 4 Seconds
	_4_SECONDS(1811233),
	// Text: 3 Seconds
	_3_SECONDS(1811234),
	// Text: 2 Seconds
	_2_SECONDS(1811235),
	// Text: 1 Second
	_1_SECOND(1811236),
	// Text: An intruder… interesting.
	AN_INTRUDER_INTERESTING(10338011),
	// Text: Prove your worth…
	PROVE_YOUR_WORTH(10338012),
	// Text: Only those strong enough shall proceed.
	ONLY_THOSE_STRONG_ENOUGH_SHALL_PROCEED(10338013),
	// Text: Are you against the will of light?
	ARE_YOU_AGAINST_THE_WILL_OF_LIGHT_(10338014),
	// Text: Come! Attack me if you dare!
	COME_ATTACK_ME_IF_YOU_DARE(10338015),
	// Text: Are you planning to betray the gods and follow a Giant?
	ARE_YOU_PLANNING_TO_BETRAY_THE_GODS_AND_FOLLOW_A_GIANT(10338016),
	// Text: It's the end for you traitor!
	ITS_THE_END_FOR_YOU_TRAITOR(10338017),
	// Text: Haha…
	HAHA(10338018),
	// Text: I want to hear you cry.
	I_WANT_TO_HEAR_YOU_CRY(10338019),
	// Text: Mortal!
	MORTAL(10338020),
	// Text: Lets see what you are made of!
	LETS_SEE_WHAT_YOU_ARE_MADE_OF(10338021),
	// Text: You will not free Hermuncus. 
	YOU_WILL_NOT_FREE_HERMUNCUS(10338022),
	// Text: You'll have to kill us first!
	YOULL_HAVE_TO_KILL_US_FIRST(10338023),
	// Text: Trying to free Hermuncus…
	TRYING_TO_FREE_HERMUNCUS(10338024),
	// Text: Repent and your death will be quick!
	REPENT_AND_YOUR_DEATH_WILL_BE_QUICK(10338025),
	// Text: You will never break the seal! 
	YOU_WILL_NEVER_BREAK_THE_SEAL(10338026),
	// Text: Die traitor!
	DIE_TRAITOR(10338027),
	// Text: Only the light may pass. 
	ONLY_THE_LIGHT_MAY_PASS(10338028),
	// Text: You are not light. You may not pass.
	YOU_ARE_NOT_LIGHT_YOU_MAY_NOT_PASS(10338029),
	// Text: Receive this power form the ancient Giant. 
	RECEIVE_THIS_POWER_FORM_THE_ANCIENT_GIANT(10338036),
	// Text: Mm.. I see
	MM_I_SEE(1620051),
	// Text: Thank you for the report Rogin.
	THANK_YOU_FOR_THE_REPORT_ROGIN(1620052),
	// Text: Soldiers, we're fighting a battle that can't be won.
	SOLDIERS_WERE_FIGHTING_A_BATTLE_THAT_CANT_BE_WON(1620053),
	// Text: But we have to defend our village, so we're fighting.
	BUT_WE_HAVE_TO_DEFEND_OUR_VILLAGE_SO_WERE_FIGHTING(1620054),
	// Text: For the fine wines and treasures of Aden!
	FOR_THE_FINE_WINES_AND_TREASURES_OF_ADEN(1620055),
	// Text: I'm proud of every one of---
	IM_PROUD_OF_EVERY_ONE_OF(1620056),
	// Text: Ugh!! If I see you in the spirit world… first round is on me.
	UGH_IF_I_SEE_YOU_IN_THE_SPIRIT_WORLD_FIRST_ROUND_IS_ON_ME(1620057),
	// Text: Use this new power when the time is right.
	USE_THIS_NEW_POWER_WHEN_THE_TIME_IS_RIGHT(10338037),
	// Text: Gah...Shilen... Why must you make us suffer...
	GAHSHILEN_WHY_MUST_YOU_MAKE_US_SUFFER(50853),
	// Text: Shilen... abandoned us. It is our time... to die.
	SHILEN_ABANDONED_US_IT_IS_OUR_TIME_TO_DIE(50854),
	// Text: With our sacrifice will we fulfill the prophecy?
	WITH_OUR_SACRIFICE_WILL_WE_FULFILL_THE_PROPHECY(50855),
	// Text: Bloody rain, plague, death... she is near.
	BLOODY_RAIN_PLAGUE_DEATH_SHE_IS_NEAR(50856),
	// Text: Arhhhh...
	ARHHHH(50857),
	// Text: We offer our blood as a sacrifice! Shilen see us!
	WE_OFFER_OUR_BLOOD_AS_A_SACRIFICE_SHILEN_SEE_US(50858),
	// Text: Will Dark Elves be forgotten after what we have done?
	WILL_DARK_ELVES_BE_FORGOTTEN_AFTER_WHAT_WE_HAVE_DONE(50859),
	// Text: Unbelievers run... death will follow you.
	UNBELIEVERS_RUN_DEATH_WILL_FOLLOW_YOU(50860),
	// Text: I curse our blood.. I despise what we are.. Shilen…
	I_CURSE_OUR_BLOOD_I_DESPISE_WHAT_WE_ARE_SHILEN(50861),
	// Text: $s1! That man in front is Holden.
	S1_THAT_MAN_IN_FRONT_IS_HOLDEN(1032306),
	// Text: I Hermuncus, give my power to those who fight for me.
	I_HERMUNCUS_GIVE_MY_POWER_TO_THOSE_WHO_FIGHT_FOR_ME(1811213),
	// Text: Though small, this power will help you greatly.
	THOUGH_SMALL_THIS_POWER_WILL_HELP_YOU_GREATLY(1811214),
	// Text: Soulshot have been added to your Inventory.
	SOULSHOT_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY(1032349),
	// Text: Automate Soulshot as shown in the Tutorial.
	AUTOMATE_SOULSHOT_AS_SHOWN_IN_THE_TUTORIAL(1032350),
	// Text: Spiritshot have been added to your Inventory.
	SPIRITSHOT_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY(1032351),
	// Text: Automate Spiritshot as shown in the Tutorial.
	AUTOMATE_SPIRITSHOT_AS_SHOWN_IN_THE_TUTORIAL(1032352),
	// Text: Free me from this binding of light!
	FREE_ME_FROM_THIS_BINDING_OF_LIGHT(10338006),
	// Text: Destroy the Ghost of Harnak… this corrupted creature.
	DESTROY_THE_GHOST_OF_HARNAK_THIS_CORRUPTED_CREATURE(10338007),
	// Text: Elapsed Time :
	ELAPSED_TIME__(1911119),
	// Text: Received Regeneration Energy!
	RECEIVED_REGENERATION_ENERGY(1811179),
	// Text: Soldier Tie received $s1 pieces of bio-energy residue.
	SOLDER_TIE_RECEIVED_S1_PRIECES_OF_BIO_ENERGY_RESIDUE(1811146),
	// Text: There is still lots of time left. Do not stop here.
	THERE_IS_STILL_LOTS_OF_TIME_LEFT_DO_NOT_STOP_HERE(1811142),
	// Text: Free me... And I promise you the power of Giants!
	FREE_ME_AND_I_PROMISE_YOU_THE_POWER_OF_GIANTS(10338008),
	// Text: Catch up to King, he's waiting.
	CATCH_UP_TO_KING_HES_WAITING(17178339),
	// Text: Ruff!! Ruff! Rrrrrr!!!
	RUFF_RUFF_RRRRRR(17178340),
	// Text: It's here!!!
	ITS_HERE(1811262),
	// Text: You may use Scroll of Afterlife from Hermuncus to Awaken.
	YOU_MAY_USE_SCROLL_OF_AFTERLIFE_FROM_HERMUNCUS_TO_AWAKEN(10338010),
	// Text: Armor has been added to your Inventory.
	ARMOR_HAS_BEEN_ADDED_TO_YOUR_INVENTORY(11022202),
	// Text: $s1! Come follow me!
	S1_COME_FOLLOW_ME(1032302),
	// Text: Chief!!!!
	CHIEF(1620077),
	// Text: Bronk!!!
	BRONK(1620059),
	// Text: Chief!!!!
	CHIEF_(1620060),
	// Text: Bronk!!!
	BRONK_(1620061),
	// Text: No way!!!
	NO_WAY(1620062),
	// Text: For Bronk!!!
	FOR_BRONK(1620068),
	// Text: Dwarves forever!!!
	DWARVES_FOREVER(1620069),
	// Text: Save the Dwarven Village!!
	SAVE_THE_DWARVEN_VILLAGE(1620070),
	// Text: Whoaaaaaa!!!!
	WHOAAAAAA(1620071),
	// Text: Fight!!
	FIGHT(1620072),
	// Text: You've shown your condolences to one corpse.
	YOUVE_SHOWN_YOUR_CONDOLENCES_TO_ONE_CORPSE(1036301),
	// Text: You've shown your condolences to a third corpse.
	YOUVE_SHOWN_YOUR_CONDOLENCES_TO_A_THIRD_CORPSE(1036303),
	// Text: You've shown your condolences to a fourth corpse.
	YOUVE_SHOWN_YOUR_CONDOLENCES_TO_A_FOURTH_CORPSE(1036304),
	// Text: You've shown your condolences to a fifth corpse.
	YOUVE_SHOWN_YOUR_CONDOLENCES_TO_A_FIFTH_CORPSE(1036305),
	// Text: You are too far from the corpse to show your condolences.
	YOU_ARE_TOO_FAR_FROM_THE_CORPSE_TO_SHOW_YOUR_CONDOLENCES(10307000),
	// Text: Grudge of Ye Sagira victims have been relieved with your tears.
	GRUDGE_OF_YE_SAGIRA_VICTIMS_HAVE_BEEN_RELIEVED_WITH_YOUR_TEARS(1810364),
	// Text: Chief, reporting in.
	CHIEF_REPORTING_IN(1620073),
	// Text: Don't toy with the dead!
	DONT_TOY_WITH_THE_DEAD(1036344),
	// Text: You are too far from the corpse to do that.
	YOU_ARE_TOO_FAR_FROM_THE_CORPSE_TO_DO_THAT(1810363),
	// Text: Enemies are approaching form the South!
	ENEMIES_ARE_APPROACHING_FORM_THE_SOUTH(1620074),
	// Text: The Elders haven't been moved to safety.
	THE_ELDERS_HAVENT_BEEN_MOVED_TO_SAFETY(1620075),
	// Text: Many residents still haven't left their homes.
	MANY_RESIDENTS_STILL_HAVENT_LEFT_THEIR_HOMES(1620076),
	// Text: Rogin! I'm here!
	ROGIN_IM_HERE(1620058),
	// Text: I hit things… they fall dead.
	I_HIT_THINGS_THEY_FALL_DEAD(1034101),
	// Text: My summons are not afraid of Shilen's monsters.
	MY_SUMMONS_ARE_NOT_AFRAID_OF_SHILENS_MONSTERS(1034103),
	// Text: I can heal you during combat.
	I_CAN_HEAL_YOU_DURING_COMBAT(1034102),
	// Text: What do I feel when I kill Shilen's monsters? Recoil.
	WHAT_DO_I_FEEL_WHEN_I_KILL_SHILENS_MONSTERS_RECOIL(1034104),
	// Text: $s1! That man in front is Ibane.
	S1_THAT_MAN_IN_FRONT_IS_IBANE(1032303),
	// Text: Get behind me! Get behind me!
	GET_BEHIND_ME_GET_BEHIND_ME(1034118),
	// Text: Lets get this over with
	LETS_GET_THIS_OVER_WITH(1811181),
	// Text: I must go help some more.
	I_MUST_GO_HELP_SOME_MORE(1811222),
	// Text: Are you strong or weak... Of the Light or darkness…
	ARE_YOU_STRONG_OR_WEAK_OF_THE_LIGHT_OR_DARKNESS(10338031),
	// Text: Only those of light may pass. Others must prove their strength.
	ONLY_THOSE_OF_LIGHT_MAY_PASS_OTHERS_MUST_PROVE_THEIR_STRENGTH(10338032),
	// Text: Watch the Dwarven Village Last Stand
	WATCH_THE_DWARVEN_VILLAGE_LAST_STAND(1620096),
	// Text: $s1! Come with me! I will lead you to Ibane.
	S1_COME_WITH_ME_I_WILL_LEAD_YOU_TO_IBANE(1032301),
	// Text: $s1! Come with me! I will lead you to Holden.
	S1_COME_WITH_ME_I_WILL_LEAD_YOU_TO_HOLDEN(1032304),
	// Text: The only good Shilen Creature, is a dead one.
	THE_ONLY_GOOD_SHILEN_CREATURE_IS_A_DEAD_ONE(1034119),
	// Text: Did someone cry medic? Here, be healed!
	DID_SOMEONE_CRY_MEDIC_HERE_BE_HEALED(1034120),
	// Text: I'm on fire! No wait, that would be you...
	IM_ON_FIRE_NO_WAIT_THAT_WOULD_BE_YOU(1034121),
	// Text: Boom! Headshot!
	BOOM_HEADSHOT(1034122),
	// Text: Enough of this… come at me!
	ENOUGH_OF_THIS_COME_AT_ME(1032328),
	// Text: Watch out! You are being attacked!
	WATCH_OUT_YOU_ARE_BEING_ATTACKED(1032322),
	// Text: Your normal attacks aren't working!
	YOUR_NORMAL_ATTACKS_ARENT_WORKING(1032325),
	// Text: Use your skill attacks against them!
	USE_YOUR_SKILL_ATTACKS_AGAINST_THEM(1032327),
	// Text: Finally... I thought I was going to die waiting.
	FINALLY_I_THOUGHT_I_WAS_GOING_TO_DIE_WAITING(1032324),
	// Text: Accessories have been added to your Inventory
	ACCESSORIES_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY(11022201),
	// Text: Looks like only skill based attacks damage them!
	LOOKS_LIKE_ONLY_SKILL_BASED_ATTACKS_DAMAGE_THEM(1032326),
	// Text: Talk to that apprentice and get on Kookaru.
	TALK_TO_THAT_APPRENTICE_AND_GET_ON_KOOKARU(1811268),
	// Text: Start charging mana ballista!
	START_CHARGING_MANA_BALLISTA(1811172),
	// Text: No... How could this be... I can't go back to Nihil like this...
	HOW_ITS_IMPOSSIBLE_RETURNING_TO_ABYSS_AGAIN(1801327),
	// Text: You can no longer live here. Have a taste of the dimensional poison.
	INTRUDERS_CANNOT_LEAVE_ALIVE(1801323),
	// Text: Replenish ballista magic power
	REPLENISH_BALLISTA_MAGIC_POWER(1811347),
	// Text: В Not bad for a bunch of humans. I'm leaving!
	YOU_VERY_STRONG_FOR_MORTAL_I_RETREAT(1801324),
	// Text: After $s1 seconds, the charging magic Ballistas starts.
	AFTER_S1_SECONDS_THE_CHARGING_MAGIC_BALLISTA_STARTS(1811155),
	// Text: Hey kid! Hurry up and follow me.
	HEY_KID_HURRY_UP_AND_FOLLOW_ME(1811265),
	// Text: Creatures Resurrected - Defend Yourself
	CREATURES_RESURRECTED__DEFEND_YOURSELF(1034105),
	// Text: I, Death Wound... Champion of Shilen, shall end your world.
	I_DEATH_WOUND_CHAMPION_OF_SHILEN_SHALL_END_YOUR_WORLD(1034116),
	// Text: You've shown your condolences to a second corpse.
	YOUVE_SHOWN_YOUR_CONDOLENCES_TO_A_SECOND_CORPSE(1036302),
	// Text: You must move to Exploration Area 5 in order to continue.
	YOU_MUST_MOVE_TO_EXPLORATION_AREA_5_IN_ORDER_TO_CONTINUE(17178341),
	// Text: King has returned to Def. Return to Def and start again.
	KING_HAS_RETURNED_TO_DEF_RETURN_TO_DEF_AND_START_AGAIN(17178342),
	// Text: Agh... humans... ha... it does not matter, your world will end anyways.
	AGH_HUMANS_HA_IT_DOES_NOT_MATTER_YOUR_WORLD_WILL_END_ANYWAYS(1034117),
	// Text: Creatures have stopped their attack. Rest and then speak with Adolph.
	CREATURES_HAVE_STOPPED_THEIR_ATTACK_REST_AND_THEN_SPEAK_WITH_ADOLPH(1034113),
	// Text: Congratulations! You will now graduate from the Clan Academy and leave
	// your current clan. As a graduate of the academy, you can immediately join a
	// clan as a regular member without being subject to any penalties.
	CONGRATULATIONS_YOU_WILL_NOW_GRADUATE_FROM_THE_CLAN_ACADEMY_AND_LEAVE_YOUR_CURRENT_CLAN_AS_A_GRADUATE_OF_THE_ACADEMY_YOU_CAN_IMMEDIATELY_JOIN_A_CLAN_AS_A_REGULAR_MEMBER_WITHOUT_BEING_SUBJECT_TO_ANY_PENALTIES(
																																																					17171749),
	// Text: The Cry of Fate pendant will be helpful to you. Please equip it and
	// bring out the power of the pendant to prepare for the next fight.
	THE_CRY_OF_FATE_PENDANT_WILL_BE_HELPFUL_TO_YOU_PLEASE_EQUIP_IT_AND_BRING_OUT_THE_POWER_OF_THE_PENDANT_TO_PREPARE_FOR_THE_NEXT_FIGHT(
																																		1034124),
	// Text: Try Kukura ...
	TRY_KUKURA(17178345),
	// Text: Drink the sacrifice of blood that we have
	DRINK_THE_SACRIFICE_OF_BLOOD_THAT_WE_HAVE(8888103),
	// Text: and bring down the hammer of justice!
	AND_BRING_DOWN_THE_HAMMER_OF_JUSTICE(8888104),
	// Text: For the destruction and resurrection!
	FOR_THE_DESTRUCTION_AND_RESURRECTION(8888105),
	// Text: Dear the goddess of destruction... The light and their creatures fear
	// you...
	DEAR_THE_GODDESS_OF_DESTRUCTION_THE_LIGHT_AND_THEIR_CREATURES_FEAR_YOU(2010111),
	// Text: Lovely Plagueworms, contaminate the swamp even more!
	LOVELY_PLAGUEWORMS_CONTAMINATE_THE_SWAMP_EVEN_MORE(1811221),
	// Text: Who dares to bother us?
	WHO_DARES_TO_BOTHER_US(1801244),
	// Text: How foolish. The price of attacking me is death!
	HOW_FOOLISH_THE_PRICE_OF_ATTACKING_ME_IS_DEATH(1801245),
	// Text: My sword will take your life!
	MY_SWORD_WILL_TAKE_YOUR_LIFE(1801246),
	// Text: Yaaah...
	YAAAH(1801247),
	// Text: Prepare! I shall grant you death!
	PREPARE_I_SHALL_GRANT_YOU_DEATH(1801248),
	// Text: Oh, Shilen... Give me strength...
	OH_SHILEN_GIVE_ME_STRENGTH(1801249),
	// Text: I would defeated...
	I_WOULD_DEFEATED(1801250),
	// Text: Don't think this is the end!
	DONT_THINK_THIS_IS_THE_END(1801251),
	// Text: No... I lost all the gathered power of light to this... this...
	NO_I_LOST_ALL_THE_GATHERED_POWER_OF_LIGHT_TO_THIS_THIS(1801252),
	// Text: Are you the one to shatter the peace?
	ARE_YOU_THE_ONE_TO_SHATTER_THE_PEACE(1801253),
	// Text: Our mission is to resurrect the goddess. Do not interfere.
	OUR_MISSION_IS_TO_RESURRECT_THE_GODDESS_DO_NOT_INTERFERE(1801254),
	// Text: I will let you sleep in darkness. Forever...
	I_WILL_LET_YOU_SLEEP_IN_DARKNESS_FOREVER(1801255),
	// Text: Hyaaaaaah....
	HYAAAAAAH(1801256),
	// Text: Feel the true terror of darkness!
	FEEL_THE_TRUE_TERROR_OF_DARKNESS(1801257),
	// Text: Oh, creatures of the goddess! Lend me your strength...
	OH_CREATURES_OF_THE_GODDESS_LEND_ME_YOUR_STRENGTH(1801258),
	// Text: No! N... no! No!
	NO_N_NO_NO(1801259),
	// Text: I will always watch you from the darkness...
	I_WILL_ALWAYS_WATCH_YOU_FROM_THE_DARKNESS(1801260),
	// Text: I mustn't lose the strength...
	I_MUSTNT_LOSE_THE_STRENGTH(1801261),
	// Text: Hehehe, I'm glad you came. I was bored!
	HEHEHE_IM_GLAD_YOU_CAME_I_WAS_BORED(1801262),
	// Text: Hehehe, I'm glad you came. I was hungry!
	HEHEHE_IM_GLAD_YOU_CAME_I_WAS_HUNGRY(1801263),
	// Text: Hehehe, shall we play?
	HEHEHE_SHALL_WE_PLAY(1801264),
	// Text: Kyaaah...
	KYAAAH(1801265),
	// Text: Small fry! I will show you true madness!вЂЋ Hahaha!!
	SMALL_FRY_I_WILL_SHOW_YOU_TRUE_MADNESS_HAHAHA(1801266),
	// Text: Hehehe! Prepare! My madness will swallow you up!
	HEHEHE_PREPARE_MY_MADNESS_WILL_SWALLOW_YOU_UP(1801267),
	// Text: Huh? What happened...? I... I lost?
	HUH_WHAT_HAPPENED_I_I_LOST(1801268),
	// Text: Huhuhu... Huhuhu... Huhahaha!
	HUHUHU_HUHUHU_HUHAHAHA(1801269),
	// Text: Ack! No! My body... It's disappearing...
	ACK_NO_MY_BODY_ITS_DISAPPEARING(1801270),
	// Text: Die...
	DIE(1801271),
	// Text: Do not interfere...
	DO_NOT_INTERFERE(1801272),
	// Text: For the goddess...
	FOR_THE_GODDESS(1801273),
	// Text: Ooooh...
	OOOOH(1801274),
	// Text: You will die.
	YOU_WILL_DIE(1801275),
	// Text: You will be destroyed.
	YOU_WILL_BE_DESTROYED(1801276),
	// Text: Is this the end...?
	IS_THIS_THE_END(1801277),
	// Text: Oh, goddess...
	OH_GODDESS(1801278),
	// Text: No! I didn't stay silent all this time, just to disappear now like
	// this!
	NO_I_DIDNT_STAY_SILENT_ALL_THIS_TIME_JUST_TO_DISAPPEAR_NOW_LIKE_THIS(1801279),
	// Text: No! I don't want to die!
	NO_I_DONT_WANT_TO_DIE(1801280),
	// Text: Attack!
	ATTACK_2(1811168),
	// Text: Follow me!
	FOLLOW_ME(1811169),
	// Text: Installation charge.
	INSTALLATION_CHARGE(2621101),
	// Text: Location Portal changed.
	LOCATION_PORTAL_CHANGED(1811150),
	// Text: The door opened. Someone has to stay and watch for a time bomb
	THE_DOOR_OPENED_SOMEONE_HAS_TO_STAY_AND_WATCH_FOR_A_TIME_BOMB(1811152),
	// Text: The door opened.
	THE_DOOR_OPENED(1811154),
	// Text: Rescued changes state only after exposure to light it
	RESCUED_CHANGES_STATE_ONLY_AFTER_EXPOSURE_TO_LIGHT_IT(1811153),
	// Text: Cannon is loading
	CANNON_IS_LOADING(98704),
	// Text: Target recognition achieved. Attack sequence commencing.
	TARGET_RECOGNITION_ACHIEVED_ATTACK_SEQUENCE_COMMENCING(1800852),
	// Text: Istina's soul stone starts powerfully illuminating in red.
	ISTINAS_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_RED(1811138),
	// Text: Istina's soul stone starts powerfully illuminating in blue.
	ISTINAS_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_BLUE(1811139),
	// Text: Istina's soul stone starts powerfully illuminating in green.
	ISTINAS_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_GREEN(1811140),
	// Text: Istina gets furious and recklessly crazy.
	ISTINA_GETS_FURIOUS_AND_RECKLESSLY_CRAZY(1811141),
	// Text: Berserker of Istina has been disabled.
	BERSERKER_OF_ISTINA_HAS_BEEN_DISABLED(1811175),
	// Text: Powerful acidic energy is erupting from Istina's body.
	POWERFUL_ACIDIC_ENERGY_IS_ERUPTING_FROM_ISTINAS_BODY(1811156),
	// Text: Istina spreads the reflecting protective sheet.
	ISTINA_SPREADS_THE_REFLECTING_PROTECTIVE_SHEET(1811148),
	// Text: Ah uh ah uh ah...
	AH_UH_AH_UH_AH(44440001),
	// Text: If you double-click the empty bottle, it will become full of water.
	IF_YOU_DOUBLECLICK_THE_EMPTY_BOTTLE_IT_WILL_BECOME_FULL_OF_WATER(574906),
	// Text: Click the flame flower, then double click the trowel.
	CLICK_THE_FLAME_FLOWER_THEN_DOUBLE_CLICK_THE_TROWEL(574907),
	// Text: When the world plunges into chaos, we will need your help. At that
	// time, please join in with us. I hope that you will become stronger.
	WHEN_THE_WORLD_PLUNGES_INTO_CHAOS_WE_WILL_NEED_YOUR_HELP_AT_THAT_TIME_PLEASE_JOIN_IN_WITH_US_I_HOPE_THAT_YOU_WILL_BECOME_STRONGER(
																																		1300172),
	// Text: We will execute our plan on Sunday night. If you join us, I will give
	// you a substantial reward.
	WE_WILL_EXECUTE_OUR_PLAN_ON_SUNDAY_NIGHT_IF_YOU_JOIN_US_I_WILL_GIVE_YOU_A_SUBSTANTIAL_REWARD(1300171),
	// Message: Everyone die!
	EVERYONE_DIE(1801599),
	// Message: For Tauti!
	FOR_TAUTI(1801600),
	// Message: Even rats struggle when you step on them!
	EVEN_RATS_STRUNGGLE_WHEN_YOU_STEP_ON_THEM(1801610),
	// Message: You rat-like creatures!
	YOU_RAT_LIKE_CREATURES(1801603),
	// Message: Today, my weapon will feast on your Petras.
	TODAY_MY_WEAPON_WILL_FEAST_ON_YOUR_PETRAS(1801604),
	// Message: Hahahaha hahahaha puny insects!
	HAHAHAHA_HAHAHAHA_PUNY_INSECTS(1801605),
	// Message: I will punish you in the name of Tauti! The crime is stealing. The
	// punishment is death!
	I_WILL_PUNISH_YOU_IN_THE_NAME_TAUTI_THE_CRIME_IS_STEALING_THE_PUNISHMENT_IS_DEATH(1801608),
	// Message: Fight for the sake of our future!
	FIGHT_FOR_THE_SAKE_OF_OUR_FUTURE(1801609),
	// Message: For our friends and family!
	FOR_OUR_FRIENDS_AND_FAMILY(1801611),
	// Message: You kundanomus! My weapon isn't great, but I will still cut off your
	// heads with it!
	YOU_KUNDANOMUS_MY_WEAPON_ISNT_GREAT_BUT_I_WILL_STILL_CUT_OFF_YOUR_HEADS_WITH_IT(1801612),
	// Message: Give me freedom or give me death!
	GIVE_ME_FREEDOM_OR_GIVE_ME_DEATH(1801613),
	// Message: Us! Today! Here! We shall write new history by defeating Tauti! For
	// freedom and happiness!
	US_TODAY_HERE_WE_SHALL_WRITE_HISTORY_BY_DEFEATING_TAUTI_FOR_FREEDOM_AND_HAPPINESS(1801614),
	// Message: We are not your pets or cattle!
	WE_ARE_NOT_YOUR_PETS_OR_CATTLE(1801615),
	// Message: You will die! And I will live.
	YOU_WILL_DIE_AND_I_WILL_LIVE(1801616),
	// Message: We cannot forgive Tauti for feeding on us anymore!
	WE_CANNOT_FORGIVE_TAUTI_FOR_FEEDING_ON_US_ANYMORE(1801617),
	// Message: If we all fall here, our plan will certainly fail. Please protect my
	// friends.
	IF_WE_ALL_FALL_HERE_OUR_PLAN_WILL_CERTAINLY_FAIL_PLEASE_PROTECT_MY_FRIENDS(1801619),
	// Message: Jahak is infusing its Petra to Tauti
	JAHAK_IS_INFUSING_ITS_PETRA_TO_TAUTI(1801649),
	// Message: Lord Tauti, receive my Petra and be strengthened. Then, defeat these
	// feeble wretches!
	LORD_TAUTI_REVEIVE_MY_PETRA_AND_BE_STRENGTHENED_THEN_DEFEAT_THESE_FEEBLE_WRETCHES(1801650),
	// Message: It left nothing behind..
	IT_LEFT_NOTHING_BEHIND(1900192),
	// Message: I'm in a pickle. We can't go back. Let's look further.
	IM_IN_A_PICKLE_WE_CANT_GO_BACK_LETS_LOOK_FURTHER(1900194),
	// Message: We'll begin internal purification process.
	WELL_BEGIN_INTERNAL_PURIFICATION_PROCESS(1900195),
	// Message: Nothing comes out neither from inside or outside.
	NOTHING_COMES_OUT_NEITHER_FROM_INSIDE_OR_OUSIDE(1900190),
	// Message: As it didn't exist
	AS_IT_DIDNT_EXIST(1900191),
	// Message: Should we report it to the kingdom?
	SHOULD_VE_REPORT_IT_TO_THE_KINGDOM(1900193),
	// Message: Chaos Shield breakthrough!!
	CHAOS_SHIELD_BREAKTHROUGH(1801773),
	// Text: Who dare to interrupt our rest...
	WHO_DARE_TO_INTERRUPT_OUR_REST(8888002),
	// Text: Stage 1
	STAGE_1(8888017),
	// Text: Stage 2
	STAGE_2(8888018),
	// Text: Stage 3
	STAGE_3(8888019),
	// Text: Stage 4
	STAGE_4(8888020),
	// Text: Stage 5
	STAGE_5(8888021),
	// Text: Stage 6
	STAGE_6(8888022),
	// Text: Stage 7
	STAGE_7(8888023),
	// Text: Stage 8
	STAGE_8(8888024),
	// Text: Final Stage
	FINAL_STAGE(8888025),
	// Text: Bonus Stage
	BONUS_STAGE(8888026),
	// Text: Weeping Yui appears
	WEEPING_YUI_APPEARS(8888027),
	// Text: Mukshu the Coward and Blind Hornafi appear
	MUKSHU_THE_COWARD_AND_BLIND_HORNAFI_APPEAR(8888028),
	// Text: Enraged Master Kinen appears
	ENRAGED_MASTER_KINEN_APPEARS(8888029),
	// Text: Sir Lesyinda of the Black Shadow appears
	SIR_LESYINDA_OF_THE_BLACK_SHADOW_APPEARS(8888030),
	// Text: Magical Warrior Konyar appears
	MAGICAL_WARRIOR_KONYAR_APPEARS(8888031),
	// Text: Yoentumak the Waiter appears
	YOENTUMAK_THE_WAITER_APPEARS(8888032),
	// Text: Trone appears
	TRONE_APPEARS(8888033),
	// Text: Those who came here looking for cursed ones... welcome.
	THOSE_WHO_CAME_HERE_LOOKING_FOR_CURSED_ONES_WELCOME(8888003),
	// Text: Bloodsucking creatures! Absorb the light and fill it into darkness.
	BLOODSUCKING_CREATURES_ABSORB_THE_LIGHT_AND_FILL_IT_INTO_DARKNESS(8888004),
	// Text: Let's see how much you can endure...
	LETS_SEE_HOW_MUCH_YOU_CAN_ENDURE(8888005),
	// Text: Don't you fear death?
	DONT_YOU_FEAR_DEATH(8888006),
	// Text: This is only the start.
	THIS_IS_ONLY_THE_START(8888007),
	// Text: Darkness! Swallow everything away.
	DARKNESS_SWALLOW_EVERYTHING_AWAY(8888008),
	// Text: Amazing... but this is the end. Full force advance!
	AMAZING_BUT_THIS_IS_THE_END_FULL_FORCE_ADVANCE(8888009),
	// Text: I now have to go and handle it.
	I_NOW_HAVE_TO_GO_AND_HANDLE_IT(8888010),
	// Text: For the eternal rest of the forgotten heroes!
	FOR_THE_ETERNAL_REST_OF_THE_FORGOTTEN_HEROES(8888011),
	// Text: Their possession can be broken by breaking the sphere of light.
	THEIR_POSSESSION_CAN_BE_BROKEN_BY_BREAKING_THE_SPHERE_OF_LIGHT(8888012),
	// Text: We need a little more...
	WE_NEED_A_LITTLE_MORE(8888013),
	// Text: I will summon more spheres of light with the last of my strength.
	I_WILL_SUMMON_MORE_SPHERES_OF_LIGHT_WITH_THE_LAST_OF_MY_STRENGTH(8888014),
	// Text: A little more... Please try a little more.
	A_LITTLE_MORE_PLEASE_TRY_A_LITTLE_MORE(8888015),
	// Text: Bloodsucking creatures! Wake the soldiers now!
	BLOODSUCKING_CREATURES_WAKE_THE_SOLDIERS_NOW(8888016),
	// Text: You must defeat Shilen's Messenger.
	YOU_MUST_DEFEAT_SHILENS_MESSENGER(18564),
	// Text: Speak with Roxxy.
	SPEAK_WITH_ROXXY(1802018),
	// Text: Go outside the temple.
	GO_OUTSIDE_THE_TEMPLE(1802019),
	// Text: Who dares summon the merchant of mammonвЂ¦?
	WHO_DARES_SUMMON_THE_MERCHANT_OF_MAMMON(19624),
	// Message: Please.
	PLEASE_33381(1900196),
	// Text: Successful Destruction of Stronghold $s1
	SUCCESSFUL_DESTRUCTION_OF_STRONGHOLD_S1(1801198),
	// Text: Successful Destruction of Stronghold: Entry Accessed
	SUCCESSFUL_DESTRUCTION_OF_STRONGHOLD_ENTRY_ACCESSED(1801199),
	// Text: The Dimensional Door opened near you!
	THE_DIMENSIONAL_DOOR_OPENED_NEAR_YOU(1630001),
	// Text: $s1! You have become a Hero of Sigel Knights. Congratulations!
	S1__YOU_HAVE_BECOME_A_HERO_OF_SIGEL_KNIGHTS_CONGRATULATIONS(1000464),
	// Text: $s1! You have become a Hero of Tyrr Warriors. Congratulations!
	S1__YOU_HAVE_BECOME_A_HERO_OF_TYRR_WARRIORS_CONGRATULATIONS(1000465),
	// Text: $s1! You have become a Hero of Othell Rogue. Congratulations!
	S1__YOU_HAVE_BECOME_A_HERO_OF_OTHELL_ROGUE_CONGRATULATIONS(1000466),
	// Text: $s1! You have become a Hero of Yul Archer. Congratulations!
	S1__YOU_HAVE_BECOME_A_HERO_OF_YUL_ARCHER_CONGRATULATIONS(1000467),
	// Text: $s1! You have become a Hero of Feoh Wizard. Congratulations!
	S1__YOU_HAVE_BECOME_A_HERO_OF_FEOH_WIZARD_CONGRATULATIONS(1000468),
	// Text: $s1! You have become a Hero of Iss Enchanter. Congratulations!
	S1__YOU_HAVE_BECOME_A_HERO_OF_ISS_ENCHANTER_CONGRATULATIONS(1000469),
	// Text: $s1! You have become a Hero of Wynn Summoner. Congratulations!
	S1__YOU_HAVE_BECOME_A_HERO_OF_WYNN_SUMMONER_CONGRATULATIONS(1000470),
	// Text: $s1! You have become a Hero of Aeore Healer. Congratulations!
	S1_YOU_HAVE_BECOME_A_HERO_OF_AEORE_HEALER_CONGRATULATIONS(1000471),
	// Text: Dark power seeps out from the middle of the town.
	DARK_POWER_SEEPS_OUT_FROM_THE_MIDDLE_OF_THE_TOWN(8888108),
	// Hein:Location Field Of Selence And Whispers
	// Message: Ah ah... From the Magic Force, no more... I will be freed
	AH_AH_FROM_THE_MAGIC_FORCE_NO_MORE_I_WILL_BE_FREED(1800874),
	// Message: Even the Magic Force binds you, you will never be forgiven...
	EVEN_THE_MAGIC_FORCE_BINDS_YOU_YOU_WILL_NEVER_BE_FORGIVEN(1800860),
	// Message: Protect the Braziers of Purity at all costs!!
	PROTECT_THE_BRAZIERS_OF_PURITY_AT_ALL_COSTS(1800855),
	// Message: The purification field is being attacked. Guardian Spirits!
	// Protect the Magic Force!!
	THE_PURIFICATION_FIELD_IS_BEING_ATTACKED_GUARDIAN_SPIRITS_PROTECT_THE_MAGIC_FORCE(1800854),
	// Message: Naia waganagel peutagun!
	NAIA_WAGANAGEL_PEUTAGUN(1800858),
	// Message: Target. Threat. Level. Launching. Strongest. Countermeasure.
	TARGET_THREAT_LEVEL_LAUNCHING_STRONGEST_COUNTERMEASURE(1800853),
	// Message: Drive device entire destruction moving suspension
	DRIVE_DEVICE_ENTIRE_DESTRUCTION_MOVING_SUSPENSION(1800873),
	// Message: Drive device partial destruction impulse result
	DRIVE_DEVICE_PARTIAL_DESTRUCTION_IMPULSE_RESULT(1800859),
	// Message: Defend our domain even at risk of your own life!
	DEFEND_OUR_DOMAIN_EVEN_AT_RISK_OF_YOUR_OWN_LIFE(1800856),
	// Message: Peunglui muglanep Naia waganagel peutagun!
	PEUNGLUI_MUGLANEP_NAIA_WAGANAGEL_PEUTAGUN(1800872),
	// Message: Peunglui muglanep!
	PEUNGLUI_MUGLANEP(1800857),
	// Text: You dare interfere with Embryo? Surely you wish for death!
	YOU_DARE_INTERFERE_WITH_EMBRYO_SURELY_YOU_WISH_FOR_DEATH(1802372),
	// Message: Alert! Alert! Damage detection recognized. Countermeasures
	// enabled.
	ALERT_ALERT_DAMAGE_DETECTION_RECOGNIZED_COUNTERMEASURES_ENABLED(1800851),
	// Text: $s1 has become the Hero of the Sigel Phoenix Knight class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_SIGEL_PHOENIX_KNIGHT_CLASS_CONGRATULATIONS(11505),
	// Text: $s1 has become the Hero of the Sigel Hell Knight class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_SIGEL_HELL_KNIGHT_CLASS_CONGRATULATIONS(11506),
	// Text: $s1 has become the Hero of the Sigel Eva's Templar class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_SIGEL_EVAS_TEMPLAR_CLASS_CONGRATULATIONS(11507),
	// Text: $s1 has become the Hero of the Sigel Shillien Templar class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_SIGEL_SHILLIEN_TEMPLAR_CLASS_CONGRATULATIONS(11508),
	// Text: $s1 has become the Hero of the Tyrr Duelist class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_TYRR_DUELIST_CLASS_CONGRATULATIONS(11509),
	// Text: $s1 has become the Hero of the Tyrr Dreadnought class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_TYRR_DREADNOUGHT_CLASS_CONGRATULATIONS(11510),
	// Text: $s1 has become the Hero of the Tyrr Titan class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_TYRR_TITAN_CLASS_CONGRATULATIONS(11511),
	// Text: $s1 has become the Hero of the Tyrr Grand Khavatari class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_TYRR_GRAND_KHAVATARI_CLASS_CONGRATULATIONS(11512),
	// Text: $s1 has become the Hero of the Tyrr Maestro class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_TYRR_MAESTRO_CLASS_CONGRATULATIONS(11513),
	// Text: $s1 has become the Hero of the Tyrr DoomBringer class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_TYRR_DOOMBRINGER_CLASS_CONGRATULATIONS(11514),
	// Text: $s1 has become the Hero of the Othell Adventurer class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_OTHELL_ADVENTURER_CLASS_CONGRATULATIONS(11515),
	// Text: $s1 has become the Hero of the Othell Wind Rider class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_OTHELL_WIND_RIDER_CLASS_CONGRATULATIONS(11516),
	// Text: $s1 has become the Hero of the Othell Ghost Hunter class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_OTHELL_GHOST_HUNTER_CLASS_CONGRATULATIONS(11517),
	// Text: $s1 has become the Hero of the Othell Fortune Seeker class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_OTHELL_FORTUNE_SEEKER_CLASS_CONGRATULATIONS(11518),
	// Text: $s1 has become the Hero of the Yul Sagittarius class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_YUL_SAGITTARIUS_CLASS_CONGRATULATIONS(11519),
	// Text: $s1 has become the Hero of the Yul Moonlight Sentinel class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_YUL_MOONLIGHT_SENTINEL_CLASS_CONGRATULATIONS(11520),
	// Text: $s1 has become the Hero of the Yul Ghost Sentinel class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_YUL_GHOST_SENTINEL_CLASS_CONGRATULATIONS(11521),
	// Text: $s1 has become the Hero of the Yul Trickster class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_YUL_TRICKSTER_CLASS_CONGRATULATIONS(11522),
	// Text: $s1 has become the Hero of the Feoh Archmage class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_FEOH_ARCHMAGE_CLASS_CONGRATULATIONS(11523),
	// Text: $s1 has become the Hero of the Feoh Soultaker class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_FEOH_SOULTAKER_CLASS_CONGRATULATIONS(11524),
	// Text: $s1 has become the Hero of the Feoh Mystic Muse class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_FEOH_MYSTIC_MUSE_CLASS_CONGRATULATIONS(11525),
	// Text: $s1 has become the Hero of the Feoh Storm Screamer class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_FEOH_STORM_SCREAMER_CLASS_CONGRATULATIONS(11526),
	// Text: $s1 has become the Hero of the Feoh Soul Hound class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_FEOH_SOUL_HOUND_CLASS_CONGRATULATIONS(11527),
	// Text: $s1 has become the Hero of the Iss Hierophant class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_ISS_HIEROPHANT_CLASS_CONGRATULATIONS(11528),
	// Text: $s1 has become the Hero of the Iss Sword Muse class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_ISS_SWORD_MUSE_CLASS_CONGRATULATIONS(11529),
	// Text: $s1 has become the Hero of the Iss Spectral Dancer class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_ISS_SPECTRAL_DANCER_CLASS_CONGRATULATIONS(11530),
	// Text: $s1 has become the Hero of the Iss Dominator class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_ISS_DOMINATOR_CLASS_CONGRATULATIONS(11531),
	// Text: $s1 has become the Hero of the Iss DoomCryer class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_ISS_DOOMCRYER_CLASS_CONGRATULATIONS(11532),
	// Text: $s1 has become the Hero of the Wynn Arcana Lord class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_WYNN_ARCANA_LORD_CLASS_CONGRATULATIONS(11533),
	// Text: $s1 has become the Hero of the Wynn Elemental Master class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_WYNN_ELEMENTAL_MASTER_CLASS_CONGRATULATIONS(11534),
	// Text: $s1 has become the Hero of the Wynn Spectral Master class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_WYNN_SPECTRAL_MASTER_CLASS_CONGRATULATIONS(11535),
	// Text: $s1 has become the Hero of the Aeore Cardinal class. Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_AEORE_CARDINAL_CLASS_CONGRATULATIONS(11536),
	// Text: $s1 has become the Hero of the Aeore Eva's Saint class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_AEORE_EVAS_SAINT_CLASS_CONGRATULATIONS(11537),
	// Text: $s1 has become the Hero of the Aeore Shillien Saint class.
	// Congratulations!
	S1_HAS_BECOME_THE_HERO_OF_THE_AEORE_SHILLIEN_SAINT_CLASS_CONGRATULATIONS(11538),
	// Text: Gainak in War
	GAINAK_IN_WAR(1600063),
	// Text: Gainak in Peace
	GAINAK_IN_PEACE(1600066),
	// Text: Rumors have it that Lindvior is headed this way.
	RUMORS_HAVE_IT_THAT_LINDVIOR_IS_HEADED_THIS_WAY(1802355),
	// Text: Do you think he can be stopped?
	DO_YOU_THINK_HE_CAN_BE_STOPPED(1802356),
	// Text: I've never seen so many scholars and wizards in my life.
	IVE_NEVER_SEEN_SO_MANY_SCHOLARS_AND_WIZARDS_IN_MY_LIFE(1802348),
	// Text: It just goes to show how important and difficult it is to activate the
	// Seal Device!
	IT_JUST_GOES_TO_SHOW_HOW_IMPORTANT_AND_DIFFICULT_IT_IS_TO_ACTIVATE_THE_SEAL_DEVICE(1802350),
	// Text: It sure seems sturdy, but would this really be able to stop the
	// sacrifices? Hm.
	IT_SURE_SEEMS_STURDY_BUT_WOULD_THIS_REALLY_BE_ABLE_TO_STOP_THE_SACRIFICES_HM(1802353),
	// Text: For now, we have no choice but to rely on these cannons placed around
	// the Generators.
	FOR_NOW_WE_HAVE_NO_CHOICE_BUT_TO_RELY_ON_THESE_CANNONS_PLACED_AROUND_THE_GENERATORS(1802357),
	// Text: May the gods watch over us.
	MAY_THE_GODS_WATCH_OVER_US(1802358),
	// Text: It's not everyday you get to see such a sight, huh?
	ITS_NOT_EVERYDAY_YOU_GET_TO_SEE_SUCH_A_SIGHT_HUH(1802349),
	// Text: This has been too taxing on us all.
	THIS_HAS_BEEN_TOO_TAXING_ON_US_ALL(1802351),
	// Text: We need a new soul that can maintain the seal.
	WE_NEED_A_NEW_SOUL_THAT_CAN_MAINTAIN_THE_SEAL(1802352),
	// Text: We did make this Generator at Lady Jenna's suggestion, but…I'm still
	// nervous.
	WE_DID_MAKE_THIS_GENERATOR_AT_LADY_JENNAS_SUGGESTION_BUTIM_STILL_NERVOUS(1802354),
	// Text: You need to use a skill just right on the Generator to obtain a scale.
	// Talk to Jenna about it.
	YOU_NEED_TO_USE_A_SKILL_JUST_RIGHT_ON_THE_GENERATOR_TO_OBTAIN_A_SCALE_TALK_TO_JENNA_ABOUT_IT(1802379),
	// Text: HP is fully restored.
	HP_IS_FULLY_RESTORED(1802306),
	// Text: There is only death for intruders!
	THERE_IS_ONLY_DEATH_FOR_INTRUDERS(540651),
	// Text: You dig your own grave, coming here!
	YOU_DIG_YOUR_OWN_GRAVE_COMING_HERE(540652),
	// Text: Die!
	DIE_2(540653),
	// Text: Do not touch that flower!
	DO_NOT_TOUCH_THAT_FLOWER(540654),
	// Text: Hah! You believe that is enough to stand in the path of darkness?
	HAH_YOU_BELIEVE_THAT_IS_ENOUGH_TO_STAND_IN_THE_PATH_OF_DARKNESS(540655),
	// Text: You…with the power of the gods…cease your masquerading as our masters.
	// Or else…
	YOUWITH_THE_POWER_OF_THE_GODSCEASE_YOUR_MASQUERADING_AS_OUR_MASTERS_OR_ELSE(540352),
//Lindvior raid:
	// Text: We will hold off Lindvior's minions!
	WE_WILL_HOLD_OFF_LINDVIORS_MINIONS(1802364),
	// Text: Activate the Generator! Hurry!
	ACTIVATE_THE_GENERATOR_HURRY(1802365),
	// Text: You must activate the 4 Generators.
	YOU_MUST_ACTIVATE_THE_4_GENERATORS(14211701),
	// Text: Protect the Generator!
	PROTECT_THE_GENERATOR(14211702),
	// Text: The Generator has been destroyed!
	THE_GENERATOR_HAS_BEEN_DESTROYED(14211711),
	// Text: All 4 Generators must be activated.
	ALL_4_GENERATORS_MUST_BE_ACTIVATED(1802366),
	// Text: $s1 has charged the cannon!
	S1_HAS_CHARGED_THE_CANNON(14211703),
	A_FEARSOME_POWER_EMANATES_FROM_LINDVIOR(14211705),
	// Text: A gigantic whirlwind has appeared!
	A_GIGANTIC_WHIRLWIND_HAS_APPEARED(14211706),
	// Text: $s1 minutes left until Lindvior gathers his full energy!
	S1_MINUTES_LEFT_UNTIL_LINDVIOR_GATHERS_HIS_FULL_ENERGY_(14211707),
	// Text: Lindvior has fallen from the sky!
	LINDVIOR_HAS_FALLEN_FROM_THE_SKY(14211708),
	// Text: Lindvior has landed!
	LINDVIOR_HAS_LANDED(14211709),
	// Text: Messenger, inform the patrons of the Keucereus Alliance Base! We're
	// gathering brave adventurers to attack Tiat's Mounted Troop that's rooted in
	// the Seed of Destruction.
	MESSENGER_INFORM_THE_PATRONS_OF_THE_KEUCEREUS_ALLIANCE_BASE_WERE_GATHERING_BRAVE_ADVENTURERS_TO_ATTACK_TIATS_MOUNTED_TROOP_THATS_ROOTED_IN_THE_SEED_OF_DESTRUCTION(
																																										1800695),
	// Text: Messenger, inform the patrons of the Keucereus Alliance Base! The Seed
	// of Destruction is currently secured under the flag of the Keucereus Alliance!
	MESSENGER_INFORM_THE_PATRONS_OF_THE_KEUCEREUS_ALLIANCE_BASE_THE_SEED_OF_DESTRUCTION_IS_CURRENTLY_SECURED_UNDER_THE_FLAG_OF_THE_KEUCEREUS_ALLIANCE(
																																						1800696),
	// Text: Messenger, inform the patrons of the Keucereus Alliance Base! Tiat's
	// Mounted Troop is currently trying to retake Seed of Destruction! Commit all
	// the available reinforcements into Seed of Destruction!
	MESSENGER_INFORM_THE_PATRONS_OF_THE_KEUCEREUS_ALLIANCE_BASE_TIATS_MOUNTED_TROOP_IS_CURRENTLY_TRYING_TO_RETAKE_SEED_OF_DESTRUCTION_COMMIT_ALL_THE_AVAILABLE_REINFORCEMENTS_INTO_SEED_OF_DESTRUCTION(
																																																		1800697),
	// Text: Messenger, inform the brothers in Kucereus' clan outpost! Brave
	// adventurers who have challenged the Seed of Infinity are currently
	// infiltrating the Hall of Erosion through the defensively weak Hall of
	// Suffering!
	MESSENGER_INFORM_THE_BROTHERS_IN_KUCEREUS_CLAN_OUTPOST_BRAVE_ADVENTURERS_WHO_HAVE_CHALLENGED_THE_SEED_OF_INFINITY_ARE_CURRENTLY_INFILTRATING_THE_HALL_OF_EROSION_THROUGH_THE_DEFENSIVELY_WEAK_HALL_OF_SUFFERING(
																																																					1800698),
	// Text: Messenger, inform the brothers in Kucereus' clan outpost! Sweeping the
	// Seed of Infinity is currently complete to the Heart of the Seed. Ekimus is
	// being directly attacked, and the Undead remaining in the Hall of Suffering
	// are being eradicated!
	MESSENGER_INFORM_THE_BROTHERS_IN_KUCEREUS_CLAN_OUTPOST_SWEEPING_THE_SEED_OF_INFINITY_IS_CURRENTLY_COMPLETE_TO_THE_HEART_OF_THE_SEED_EKIMUS_IS_BEING_DIRECTLY_ATTACKED_AND_THE_UNDEAD_REMAINING_IN_THE_HALL_OF_SUFFERING_ARE_BEING_ERADICATED(
																																																													1800699),
	// Text: Messenger, inform the patrons of the Keucereus Alliance Base! The Seed
	// of Infinity is currently secured under the flag of the Keucereus Alliance!
	MESSENGER_INFORM_THE_PATRONS_OF_THE_KEUCEREUS_ALLIANCE_BASE_THE_SEED_OF_INFINITY_IS_CURRENTLY_SECURED_UNDER_THE_FLAG_OF_THE_KEUCEREUS_ALLIANCE(
																																					1800700),
	// Text: Messenger, inform the patrons of the Keucereus Alliance Base! The
	// resurrected Undead in the Seed of Infinity are pouring into the Hall of
	// Suffering and the Hall of Erosion!
	MESSENGER_INFORM_THE_PATRONS_OF_THE_KEUCEREUS_ALLIANCE_BASE_THE_RESURRECTED_UNDEAD_IN_THE_SEED_OF_INFINITY_ARE_POURING_INTO_THE_HALL_OF_SUFFERING_AND_THE_HALL_OF_EROSION(
																																												1800702),
	// Text: Messenger, inform the brothers in Kucereus' clan outpost! Ekimus is
	// about to be revived by the resurrected Undead in Seed of Infinity. Send all
	// reinforcements to the Heart and the Hall of Suffering!
	MESSENGER_INFORM_THE_BROTHERS_IN_KUCEREUS_CLAN_OUTPOST_EKIMUS_IS_ABOUT_TO_BE_REVIVED_BY_THE_RESURRECTED_UNDEAD_IN_SEED_OF_INFINITY_SEND_ALL_REINFORCEMENTS_TO_THE_HEART_AND_THE_HALL_OF_SUFFERING(
																																																		1800703),
	// Text: Stabbing three times!
	STABBING_THREE_TIMES(1800704),
	// Text: Honorable warriors have driven off Lindvior, the evil wind dragon!
	HONORABLE_WARRIORS_HAVE_DRIVEN_OFF_LINDVIOR_THE_EVIL_WIND_DRAGON(14211715),
	// End lindvior raid
	// Text: HP is halfway restored.
	HP_IS_HALFWAY_RESTORED(1802313),
	// Text: Queen Serenity is causing you.
	QUEEN_SERENITY_IS_CAUSING_YOU(1802616),
	// Text: You must activate the Warp Gate behind me in order to teleport to
	// Hellbound.
	YOU_MUST_ACTIVATE_THE_WARP_GATE_BEHIND_ME_IN_ORDER_TO_TELEPORT_TO_HELLBOUND(545521),
	// Text: Have you made preparations for the mission? There isn't much time.
	HAVE_YOU_MADE_PREPARATIONS_FOR_THE_MISSION_THERE_ISNT_MUCH_TIME(545522),
	// Text: You have acquired SP x 2.
	YOU_HAVE_ACQUIRED_SP_X_2(1802452),
	// Text: You have acquired SP x 4.
	YOU_HAVE_ACQUIRED_SP_X_4(1802453),
	// Text: You have acquired SP x 8.
	YOU_HAVE_ACQUIRED_SP_X_8(1802454),
	// Text: You have acquired SP x 16.
	YOU_HAVE_ACQUIRED_SP_X_16(1802455),
	// Text: $s1 acquired 32 times the skill points as a reward.
	S1_ACQUIRED_32_TIMES_THE_SKILL_POINTS_AS_A_REWARD(1802456),
	// Text: Check on Telesha.
	CHECK_ON_TELESHA(1802506),
	// Text: Talk to the Mysterious Wizard.
	TALK_TO_THE_MYSTERIOUS_WIZARD(1802507),
	// Text: Return to Raymond of the Town of Gludio.
	RETURN_TO_RAYMOND_OF_THE_TOWN_OF_GLUDIO(1802623),
	// Text: Check your equipment in your inventory.
	CHECK_YOUR_EQUIPMENT_IN_YOUR_INVENTORY(1802620),
	// Text: Attack the monster!
	ATTACK_THE_MONSTER(1802514),
	// Text: Fight using Skills!
	FIGHT_USING_SKILLS(1802516),
	// Text: Talk to Katalin to leave the training grounds.
	TALK_TO_KATALIN_TO_LEAVE_THE_TRAINING_GROUNDS(1802618),
	// Text: Attack the Training Dummy.
	ATTACK_THE_TRAINING_DUMMY(1802511),
	// Text: Talk to the Apprentice Adventurer's Guide.
	TALK_TO_THE_APPRENTICE_ADVENTURERS_GUIDE(1802513),
	// Text: Talk to Ayanthe to leave the training grounds.
	TALK_TO_AYANTHE_TO_LEAVE_THE_TRAINING_GROUNDS(1802619),
	// Text: Follow Ricky!
	FOLLOW_RICKY(1802643),
	// Text: Take Ricky to Leira in under 2 minutes.
	TAKE_RICKY_TO_LEIRA_IN_UNDER_2_MINUTES(574207),
	// Text: Ricky is not here.\\nTry searching another Kiku's Cave.
	RICKY_IS_NOT_HERENTRY_SEARCHING_ANOTHER_KIKUS_CAVE(1802617),
	// Text: Ricky has found Leira.
	RICKY_HAS_FOUND_LEIRA(1802644),
	// Text: Talk to Dolkin and leave the Karaphon Habitat.
	TALK_TO_DOLKIN_AND_LEAVE_THE_KARAPHON_HABITAT(1802645),
	// Text: Queen Navari has sent a letter.\\nClick the question-mark icon to read.
	QUEEN_NAVARI_HAS_SENT_A_LETTERNCLICK_THE_QUESTIONMARK_ICON_TO_READ(1802512),
	// Text: Master Katalin has sent a letter.\\nClick the question-mark icon to
	// read.
	MASTER_KATALIN_HAS_SENT_A_LETTERNCLICK_THE_QUESTIONMARK_ICON_TO_READ(1802610),
	// Text: Magister Ayanthe has sent a letter.\\nClick the question-mark icon to
	// read.
	MAGISTER_AYANTHE_HAS_SENT_A_LETTERNCLICK_THE_QUESTIONMARK_ICON_TO_READ(1802611),
	// Text: Talk to the Ghost of von Hellmann.
	TALK_TO_THE_GHOST_OF_VON_HELLMANN(1802589),
	// Text: Time to move onto the next place.
	TIME_TO_MOVE_ONTO_THE_NEXT_PLACE(1802509),
	// Text: Conversation with Newbie Helper
	_SPEAK_WITH_THE_NEWBIE_HELPER(1802764),
	// Text: This choice cannot be reversed.
	THIS_CHOICE_CANNOT_BE_REVERSED(1802668),
	// Text: Such monsters in a place like this…! Unbelievable!
	SUCH_MONSTERS_IN_A_PLACE_LIKE_THIS_UNBELIEVABLE(1802612),
	// Text: Leave this place to Kain.\\nGo to the next room.
	LEAVE_THIS_PLACE_TO_KAINNGO_TO_THE_NEXT_ROOM(1802614),
	// Text: You can't die here! I didn't learn Resurrect yet!
	YOU_CANT_DIE_HERE_I_DIDNT_LEARN_RESURRECT_YET(1802634),
	// Text: Do you think I'll grow taller if I eat lots and lots?
	DO_YOU_THINK_ILL_GROW_TALLER_IF_I_EAT_LOTS_AND_LOTS(1802635),
	// Text: That's a bratty kid if I've ever seen one. You two close?
	THATS_A_BRATTY_KID_IF_IVE_EVER_SEEN_ONE_YOU_TWO_CLOSE(1802639),
	// Text: Leave this to me. Go!
	LEAVE_THIS_TO_ME_GO(1802646),
	// Text: Go now! Kain can handle this.
	GO_NOW_KAIN_CAN_HANDLE_THIS(1802647),
	// Text: Immense Windima or Giant Windima
	IMMENSE_WINDIMA_OR_GIANT_WINDIMA(575711),
	// Text: Try using the teleport scroll Levian gave you.
	TRY_USING_THE_TELEPORT_SCROLL_LEVIAN_GAVE_YOU(1802587),
	// Text: I am loyal to yo master of the winds, and loyal I shall remain, if my
	// very soul betrays me!
	I_AM_LOYAL_TO_YO_MASTER_OF_THE_WINDS_AND_LOYAL_I_SHALL_REMAIN_IF_MY_VERY_SOUL_BETRAYS_ME(1802621),
	// Text: That's the monster that attacked Faeron. You're outmatched here. Go
	// ahead; I'll catch up.
	THATS_THE_MONSTER_THAT_ATTACKED_FAERON_YOURE_OUTMATCHED_HERE_GO_AHEAD_ILL_CATCH_UP(1802642),
	// Text: A powerful monster has come to face you!
	A_POWERFUL_MONSTER_HAS_COME_TO_FACE_YOU(1802608),
	// Text: Queen Navari has sent a letter.\\nClick the question-mark icon to read.
	QUEEN_NAVARI_HAS_SENT_A_LETTERNCLICK_THE_QUESTIONMARK_ICON_TO_READ2(575500),
	// Text: Try talking to Vorbos by the well.\\nYou can receive Queen Navari's
	// next letter at Lv. 40!
	TRY_TALKING_TO_VORBOS_BY_THE_WELLNYOU_CAN_RECEIVE_QUEEN_NAVARIS_NEXT_LETTER_AT_LV_40(1802615),
	// Text: Try using the teleport scroll Sylvain gave you to go to Cruma Tower.
	TRY_USING_THE_TELEPORT_SCROLL_SYLVAIN_GAVE_YOU_TO_GO_TO_CRUMA_TOWER(1802592),
	// Text: Grow stronger here until you receive the next letter from Queen Navari
	// at Lv. 46!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_QUEEN_NAVARI_AT_LV_46(1802671),
	// Text: Kill them! Don't let them get away with the fragment!
	KILL_THEM_DONT_LET_THEM_GET_AWAY_WITH_THE_FRAGMENT(1802540),
	// Text: Lada has sent a letter.\\nClick the question-mark icon to read.
	LADA_HAS_SENT_A_LETTERNCLICK_THE_QUESTIONMARK_ICON_TO_READ(1802669),
	// Text: Grow stronger here until you receive the next letter from Queen Navari
	// at Lv. 30!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_QUEEN_NAVARI_AT_LV_30(1802670),
	// Text: The Crusher is activated!
	THE_CRUSHER_IS_ACTIVATED(1802536),
	// Text: $s1 object(s) destroyed!
	S1_OBJECTS_DESTROYED(1802537),
	// Text: The device ran out of magic.
	THE_DEVICE_RAN_OUT_OF_MAGIC(1802538),
	// Text: The device ran out of magic. Try looking for another!
	THE_DEVICE_RAN_OUT_OF_MAGIC_TRY_LOOKING_FOR_ANOTHER(1802539),
	// Text: To Queen Navari of Faeron!
	TO_QUEEN_NAVARI_OF_FAERON(1802541),
	// Text: Grow stronger here until you receive the next letter from Queen Navari
	// at Lv. 52!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_QUEEN_NAVARI_AT_LV_52(1802672),
	// Text: Cursed Ertheia! I will kill you all!
	CURSED_ERTHEIA_I_WILL_KILL_YOU_ALL(1802542),
	// Text: Grow stronger here until you receive the next letter from Queen Navari
	// at Lv. 58!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_QUEEN_NAVARI_AT_LV_58(1802673),
	// Text: Grow stronger here until you receive the next letter from Queen Navari
	// at Lv. 61!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_QUEEN_NAVARI_AT_LV_61(1802674),
	// Text: I will give you death!
	I_WILL_GIVE_YOU_DEATH(578351),
	// Text: Back for more, huh?
	BACK_FOR_MORE_HUH(578352),
	// Text: You little punk! Take that!
	YOU_LITTLE_PUNK_TAKE_THAT(578353),
	// Text: Grow stronger here until you receive the next letter from Queen Navari
	// at Lv. 65!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_QUEEN_NAVARI_AT_LV_65(1802675),
	// Text: Grow stronger here until you receive the next letter from Queen Navari
	// at Lv. 70!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_QUEEN_NAVARI_AT_LV_70(1802676),
	// Text: Grow stronger here until you receive the next letter from Queen Navari
	// at Lv. 76!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_QUEEN_NAVARI_AT_LV_76(1802677),
	// Text: Try using the teleport scroll Levian gave you to go to Orc Barracks.
	TRY_USING_THE_TELEPORT_SCROLL_LEVIAN_GAVE_YOU_TO_GO_TO_ORC_BARRACKS(1802590),
	// Text: You have finished all of Queen Navari's Letters! Grow stronger here
	// until you receive letters from a minstrel at Lv. 85.
	YOU_HAVE_FINISHED_ALL_OF_QUEEN_NAVARIS_LETTERS_GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_LETTERS_FROM_A_MINSTREL_AT_LV_85(
																															1802678),
	// Text: You can gather more Monster Blood.
	YOU_CAN_GATHER_MORE_MONSTER_BLOOD(1802544),
	// Text: You can gather more Powerful Dark Malice.
	YOU_CAN_GATHER_MORE_POWERFUL_DARK_MALICE(1802545),
	// Text: You have finished all of Kekropus' Letters! Grow stronger here until
	// you receive letters from a minstrel at Lv. 85.
	YOU_HAVE_FINISHED_ALL_OF_KEKROPUS_LETTERS_GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_LETTERS_FROM_A_MINSTREL_AT_LV_85(
																													1802574),
	// Text: Grow stronger here until you receive the next letter from Kekropus at
	// Lv. 46!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_KEKROPUS_AT_LV_46(1802566),
	// Text: Grow stronger here until you receive the next letter from Kekropus at
	// Lv. 52!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_KEKROPUS_AT_LV_52(1802567),
	// Text: Grow stronger here until you receive the next letter from Kekropus at
	// Lv. 58!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_KEKROPUS_AT_LV_58(1802568),
	// Text: Grow stronger here until you receive the next letter from Kekropus at
	// Lv. 61!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_KEKROPUS_AT_LV_61(1802569),
	// Text: Grow stronger here until you receive the next letter from Kekropus at
	// Lv. 65!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_KEKROPUS_AT_LV_65(1802570),
	// Text: Grow stronger here until you receive the next letter from Kekropus at
	// Lv. 70!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_KEKROPUS_AT_LV_70(1802571),
	// Text: Grow stronger here until you receive the next letter from Kekropus at
	// Lv. 76!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_KEKROPUS_AT_LV_76(1802572),
	// Text: Grow stronger here until you receive the next letter from Kekropus at
	// Lv. 81!
	GROW_STRONGER_HERE_UNTIL_YOU_RECEIVE_THE_NEXT_LETTER_FROM_KEKROPUS_AT_LV_81(1802573),
	// Text: Kekropus' Letter has arrived.\\nClick the question-mark icon to read.
	KEKROPUS_LETTER_HAS_ARRIVEDNCLICK_THE_QUESTIONMARK_ICON_TO_READ(539051),
	// Text: A minstrel has sent an invitation.\\nClick the question-mark icon to
	// read.
	A_MINSTREL_HAS_SENT_AN_INVITATIONNCLICK_THE_QUESTIONMARK_ICON_TO_READ(571200),
	// Text: The war is not yet over.
	THE_WAR_IS_NOT_YET_OVER(1802666),
	// Text: Thank you. Deliver this Mark of Gratitude to Leo.
	THANK_YOU_DELIVER_THIS_MARK_OF_GRATITUDE_TO_LEO(1802659),
	// Text: Lavi's Boss
	LAVIS_BOSS(1802444),
	// Text: Lavisys's Boss
	LAVISYSS_BOSS(1802445),
	// Text: I look west.
	I_LOOK_WEST(2000155),
	// Text: Eve will bring you great fortune.
	EVE_WILL_BRING_YOU_GREAT_FORTUNE(2000156),
	// Text: You will one day ask me for guidance in your path.
	YOU_WILL_ONE_DAY_ASK_ME_FOR_GUIDANCE_IN_YOUR_PATH(2000157),
	// Text: Spezion's status will only change when exposed to light.
	SPEZIONS_STATUS_WILL_ONLY_CHANGE_WHEN_EXPOSED_TO_LIGHT(1811153),
	// Text: $s1 has summoned Elite Soldiers through the Clone Generator.
	S1_HAS_SUMMONED_ELITE_SOLDIERS_THROUGH_THE_CLONE_GENERATOR(1802277),
	// Text: Istina calls her creatures with tremendous anger.
	ISTINA_CALLS_HER_CREATURES_WITH_TREMENDOUS_ANGER(1811144),
	// Text: Istina's Mark shines above the head.
	ISTINAS_MARK_SHINES_ABOVE_THE_HEAD(1811187),
	// Text: Istina shoots powerful acid into the air.
	ISTINA_SHOOTS_POWERFUL_ACID_INTO_THE_AIR(1811178),
	// Text: $s1 second(s) remaining.
	S1_SECONDS_REMAINING(1800079),
	// Text: How dare you attack my recruits!!
	HOW_DARE_YOU_ATTACK_MY_RECRUITS(1801112),
	// Text: Who is disrupting the order?!
	WHO_IS_DISRUPTING_THE_ORDER(1801113),
	// Text: The drillmaster is dead!
	THE_DRILLMASTER_IS_DEAD(1801114),
	// Text: Line up the ranks!!
	LINE_UP_THE_RANKS(1801115),
	// Text: Charging
	CHARGING(16211701),
	// Text: We shall see about that!
	WE_SHALL_SEE_ABOUT_THAT(1000007),
	// Text: I will definitely repay this humiliation!
	I_WILL_DEFINITELY_REPAY_THIS_HUMILIATION(1000008),
	// Text: Retreat!
	RETREAT(1000009),
	// Text: Tactical retreat!
	TACTICAL_RETREAT(1000010),
	// Text: Mass fleeing!
	MASS_FLEEING(1000011),
	// Text: It's stronger than expected!
	ITS_STRONGER_THAN_EXPECTED(1000012),
	// Text: I'll kill you next time!
	ILL_KILL_YOU_NEXT_TIME(1000013),
	// Text: I'll definitely kill you next time!
	ILL_DEFINITELY_KILL_YOU_NEXT_TIME(1000014),
	// Text: Oh! How strong!
	OH_HOW_STRONG(1000015),
	// Text: Invader!
	INVADER(1000016),
	// Text: There is no reason for you to kill me! I have nothing you need!
	THERE_IS_NO_REASON_FOR_YOU_TO_KILL_ME_I_HAVE_NOTHING_YOU_NEED(1000017),
	// Text: Someday you will pay!
	SOMEDAY_YOU_WILL_PAY(1000018),
	// Text: I won't just stand still while you hit me.
	I_WONT_JUST_STAND_STILL_WHILE_YOU_HIT_ME(1000019),
	// Text: Stop hitting!
	STOP_HITTING(1000020),
	// Text: It hurts to the bone!
	IT_HURTS_TO_THE_BONE(1000021),
	// Text: Am I the neighborhood drum for beating!
	AM_I_THE_NEIGHBORHOOD_DRUM_FOR_BEATING(1000022),
	// Text: Follow me if you want!
	FOLLOW_ME_IF_YOU_WANT(1000023),
	// Text: Surrender!
	SURRENDER(1000024),
	// Text: Oh, I'm dead!
	OH_IM_DEAD(1000025),
	// Text: I'll be back!
	ILL_BE_BACK(1000026),
	// Text: I'll give you ten million arena if you let me live!
	ILL_GIVE_YOU_TEN_MILLION_ARENA_IF_YOU_LET_ME_LIVE(1000027),
	// Text: Sacrifice left: $s1
	SACRIFICE_LEFT_S1(2518003),
	// Text: You must stop the altar before everything is sacrificed.
	YOU_MUST_STOP_THE_ALTAR_BEFORE_EVERYTHING_IS_SACRIFICED(2518004),
	// Text: Altar has stopped.
	ALTAR_HAS_STOPPED(2518005),
	// Text: Come out, warriors. Protect Seed of Destruction.
	COME_OUT_WARRIORS_PROTECT_SEED_OF_DESTRUCTION(1800297),
	// Text: Obelisk has collapsed. Don't let the enemies jump around wildly
	// anymore!!!!
	OBELISK_HAS_COLLAPSED_DONT_LET_THE_ENEMIES_JUMP_AROUND_WILDLY_ANYMORE(1800295),
	// Text: Burning Blood's effect is felt.
	BURNING_BLOODS_EFFECT_IS_FELT(1802106),
	// Text: The Life Plunderer has disappeared.
	THE_LIFE_PLUNDERER_HAS_DISAPPEARED(1802107),
	// Text: The Life Plunderer's true form is revealed.
	THE_LIFE_PLUNDERERS_TRUE_FORM_IS_REVEALED(1802104),
	// Text: Sacrifice has been killed! (Sacrifice left: $s1)
	SACRIFICE_HAS_BEEN_KILLED_SACRIFICE_LEFT_S1(2518001),
	// Text: All have been sacrificed. You have failed.
	ALL_HAVE_BEEN_SACRIFICED_YOU_HAVE_FAILED(2518002),
	// Text: Altar of Shilen is starting! Must focus fire the altar!
	ALTAR_OF_SHILEN_IS_STARTING_MUST_FOCUS_FIRE_THE_ALTAR(2518006),
	// Text: Focus fire the altar to stop blessing of Shilen!
	FOCUS_FIRE_THE_ALTAR_TO_STOP_BLESSING_OF_SHILEN(2518007),
	// Text: Start the altar
	START_THE_ALTAR(2518008),
	// Text: The portal to the next room is now open.
	THE_PORTAL_TO_THE_NEXT_ROOM_IS_NOW_OPEN(1801200),
	// Text: Open the ability screen in the character information screen.\\nPress
	// "Adjust Points" to adjust the acquired SP and Ability points.
	OPEN_THE_ABILITY_SCREEN_IN_THE_CHARACTER_INFORMATION_SCREENNPRESS_ADJUST_POINTS_TO_ADJUST_THE_ACQUIRED_SP_AND_ABILITY_POINTS(
																																	546123),
	// Text: The letter from Lionel Hunter has arrived.\\nClick the question mark
	// icon to read the letter's contents.
	THE_LETTER_FROM_LIONEL_HUNTER_HAS_ARRIVEDNCLICK_THE_QUESTION_MARK_ICON_TO_READ_THE_LETTERS_CONTENTS(546151),
	// Text: Golem entered the required zone.
	GOLEM_ENTERED_THE_REQUIRED_ZONE__(1801201),
	// Text: Given to $s1
	GIVEN_TO_S1_(10307005),
	// Text: Traitor Crystalline Golem
	TRAITOR_CRYSTALLINE_GOLEM(10307006),
	// Text: Crystalline Golem
	CRYSTALLINE_GOLEM(10307007),
	// Text: Resistance underlings!
	RESISTANCE_UNDERLINGS(2010092),
	// Text: Phantom image!
	PHANTOM_IMAGE(2010097),
	// Text: I will come back alive with rotting aura!
	I_WILL_COME_BACK_ALIVE_WITH_ROTTING_AURA(10307002),
	// Text: Darkness that engulfed me one day made me this way!
	DARKNESS_THAT_ENGULFED_ME_ONE_DAY_MADE_ME_THIS_WAY(10307003),
	// Text: All for nothing,
	ALL_FOR_NOTHING(1000203),
	// Text: I won't let you get away any more!
	I_WONT_LET_YOU_GET_AWAY_ANY_MORE(2010099),
	// Text: Go Advance!
	GO_ADVANCE(2010031),
	// Text: All Attack!
	ALL_ATTACK(2010032),
	// Text: Forward!
	FORWARD(2010033),
	// Text: For the Freedom!
	FOR_THE_FREEDOM(2010034),
	// Text: Umm. You're still alive?
	UMM_YOURE_STILL_ALIVE(2010098),
	// Text: How ridiculous! You think you can find me?
	HOW_RIDICULOUS_YOU_THINK_YOU_CAN_FIND_ME(2010064),
	// Text: Then try. Ha ha ha.
	THEN_TRY_HA_HA_HA(2010065),
	// Text: Ha ha ha ha...
	HA_HA_HA_HA(2010067),
	// Text: Thank you for fighting well until the end. I will request that you be
	// additionally rewarded.
	THANK_YOU_FOR_FIGHTING_WELL_UNTIL_THE_END_I_WILL_REQUEST_THAT_YOU_BE_ADDITIONALLY_REWARDED(2010116),
	// Text: Unfortunately, they ran away.
	UNFORTUNATELY_THEY_RAN_AWAY(2010070),
	// Text: Foolish, insignificant creatures! How dare you challenge me!
	FOOLISH_INSIGNIFICANT_CREATURES_HOW_DARE_YOU_CHALLENGE_ME_(1010581),
	// Text: How dare you came to me! This is where you'll be buried.
	HOW_DARE_YOU_CAME_TO_ME_THIS_IS_WHERE_YOULL_BE_BURIED(2010100),
	// Text: But you came this far, I should at least play with you a little.
	BUT_YOU_CAME_THIS_FAR_I_SHOULD_AT_LEAST_PLAY_WITH_YOU_A_LITTLE(2010101),
	// Text: You did it! Thank you!
	YOU_DID_IT_THANK_YOU(2010069),
	// Text: You did a lot of work! Let's go back to the village and have a
	// congratulatory drink!
	YOU_DID_A_LOT_OF_WORK_LETS_GO_BACK_TO_THE_VILLAGE_AND_HAVE_A_CONGRATULATORY_DRINK(2010090),
	// Text: Beleth's Appearance
	BELETHS_APPEARANCE(14211716),
	// Text: Darion's Appearance
	DARIONS_APPEARANCE(14211717),
	// Text: Don't expect to get out alive..
	DONT_EXPECT_TO_GET_OUT_ALIVE(1800066),
	// Text: Demon King Beleth, give me the power! Aaahh!!!
	DEMON_KING_BELETH_GIVE_ME_THE_POWER_AAAHH(1800067),
	// Text: I don't feel any evil from them. I will take of them so please go and
	// attack Beleth's incarnation!
	I_DONT_FEEL_ANY_EVIL_FROM_THEM_I_WILL_TAKE_OF_THEM_SO_PLEASE_GO_AND_ATTACK_BELETHS_INCARNATION(1802428),
	// Text: Leona Blackbird gave Beleth's Ring as a gift to $s1.
	LEONA_BLACKBIRD_GAVE_BELETHS_RING_AS_A_GIFT_TO_S1(1802451),
	// Text: You have defeated the forces of evil while I was gathering
	// reinforcements. I wish to give you a reward, so please come here.
	YOU_HAVE_DEFEATED_THE_FORCES_OF_EVIL_WHILE_I_WAS_GATHERING_REINFORCEMENTS_I_WISH_TO_GIVE_YOU_A_REWARD_SO_PLEASE_COME_HERE(
																																1802430),
	// Text: Is this Antharas...
	IS_THIS_ANTHARAS(17178316),
	// Text: We have no more chance. We must go back!
	WE_HAVE_NO_MORE_CHANCE_WE_MUST_GO_BACK(17178317),
	// Text: I think we hurt him good. We can defeat him!
	I_THINK_WE_HURT_HIM_GOOD_WE_CAN_DEFEAT_HIM(17178318),
	// Text: How stubborn... Squirming 'til the last minute
	HOW_STUBBORN_SQUIRMING_TIL_THE_LAST_MINUTE(17178327),
	// Text: You want more losses?!
	YOU_WANT_MORE_LOSSES(17178319),
	// Text: Watch your words!
	WATCH_YOUR_WORDS(17178320),
	// Text: This is their limit!
	THIS_IS_THEIR_LIMIT(17178322),
	// Text: Do your best for those who died for us!!
	DO_YOUR_BEST_FOR_THOSE_WHO_DIED_FOR_US(17178323),
	// Text: Charge!!!!
	CHARGE(17178324),
	// Text: Damn it! Will we end up dead here...
	DAMN_IT_WILL_WE_END_UP_DEAD_HERE(17178325),
	// Text: Whoaaaaaa!!!!
	WHOAAAAAA_2(17178326),
	// Text: I can't die like this! I will get backup from the Kingdom!
	I_CANT_DIE_LIKE_THIS_I_WILL_GET_BACKUP_FROM_THE_KINGDOM(17178328),
	// Text: Mercenaries! I think they're here to support us!
	MERCENARIES_I_THINK_THEYRE_HERE_TO_SUPPORT_US(17178329),
	// Text: Mercenaries!! Everyone charge! We can't let them win it for us!
	MERCENARIES_EVERYONE_CHARGE_WE_CANT_LET_THEM_WIN_IT_FOR_US(17178330),
	// Text: Everyone listen!
	EVERYONE_LISTEN(17178321),
	// Text: Your sacrifices will become a new rescue...
	YOUR_SACRIFICES_WILL_BECOME_A_NEW_RESCUE(17178308),
	// Text: Heavens will know no greater wrath!
	HEAVENS_WILL_KNOW_NO_GREATER_WRATH(1811198),
	// Text: It's stronger than expected! I didn't think I'd be hurt this much...
	ITS_STRONGER_THAN_EXPECTED_I_DIDNT_THINK_ID_BE_HURT_THIS_MUCH(17178306),
	// Text: Children. With noble your sacrifice, give them pain!
	CHILDREN_WITH_NOBLE_YOUR_SACRIFICE_GIVE_THEM_PAIN(17178305),
	// Text: Not enough... I will have to go myself.
	NOT_ENOUGH_I_WILL_HAVE_TO_GO_MYSELF(17178304),
	// Text: Be happy that I'm backing off today.
	BE_HAPPY_THAT_IM_BACKING_OFF_TODAY(17178331),
	// Text: Imbeciles...you'll disappear on the day of destruction...
	IMBECILESYOULL_DISAPPEAR_ON_THE_DAY_OF_DESTRUCTION(17178332),
	// Text: Shoot fire at the imbecile!
	SHOOT_FIRE_AT_THE_IMBECILE(17178300),
	// Text: Wrath of the ground will fall from the sky on $s1!
	WRATH_OF_THE_GROUND_WILL_FALL_FROM_THE_SKY_ON_S1(17178302),
	// Text: Children. Heal me with your noble sacrifice.
	CHILDREN_HEAL_ME_WITH_YOUR_NOBLE_SACRIFICE(17178307),
	// Text: Ah.. Did the backup get wiped out... Looks like we're late.
	AH_DID_THE_BACKUP_GET_WIPED_OUT_LOOKS_LIKE_WERE_LATE(17178333),
	// Text: You guys are the mercenaries.
	YOU_GUYS_ARE_THE_MERCENARIES(17178334),
	// Text: He's quiet again. Thanks.
	HES_QUIET_AGAIN_THANKS(17178335),
	// Text: This.. We brought this to support the backup, but we could give these
	// to you.
	THIS_WE_BROUGHT_THIS_TO_SUPPORT_THE_BACKUP_BUT_WE_COULD_GIVE_THESE_TO_YOU(17178336),
	// Text: Courageous ones who supported Antharas force, come and take the
	// Kingdom's reward.
	COURAGEOUS_ONES_WHO_SUPPORTED_ANTHARAS_FORCE_COME_AND_TAKE_THE_KINGDOMS_REWARD(17178337),
	// Text: Are there those who didn't receive the rewards yet? Come and get it
	// from me.
	ARE_THERE_THOSE_WHO_DIDNT_RECEIVE_THE_REWARDS_YET_COME_AND_GET_IT_FROM_ME(17178338),
	// Text: The Repository is attacked! Fight! Fight!
	THE_REPOSITORY_IS_ATTACKED_FIGHT_FIGHT(1802496),
	// Text: Argh! Who is…hiding there…?
	ARGH_WHO_ISHIDING_THERE(1802497),
	// Text: A smart Giant, huh? Well, hand it over! The Kartia's Seed is ours!
	A_SMART_GIANT_HUH_WELL_HAND_IT_OVER_THE_KARTIAS_SEED_IS_OURS(1802498),
	// Text: Kartia's Seed! Got it!
	KARTIAS_SEED_GOT_IT(1802499),
	// Text: Arghh!!
	ARGHH(1802504),
	// Text: You worthless Giant…curse you for eternity!
	YOU_WORTHLESS_GIANTCURSE_YOU_FOR_ETERNITY(1802500),
	// Text: Dimensional Warp Lv. $s1
	DIMENSIONAL_WARP_LV_S1(1802575),
	// Text: $s1 seconds have been added to the instanced zone duration.
	S1_SECONDS_HAVE_BEEN_ADDED_TO_THE_INSTANCED_ZONE_DURATION(1802609),
	// Text: The surrounding energy has dissipated.
	THE_SURROUNDING_ENERGY_HAS_DISSIPATED(1802593),
	// Text: Dimensional Imp!
	DIMENSIONAL_IMP(1802624),
	// Text: Unworldly Imp!
	UNWORLDLY_IMP(1802625),
	// Text: Abyssal Imp!
	ABYSSAL_IMP(1802626),
	// Text: The instanced zone will close soon!
	THE_INSTANCED_ZONE_WILL_CLOSE_SOON(1802704),
	// Text: You'll earn tons of items using the Training Grounds.
	YOULL_EARN_TONS_OF_ITEMS_USING_THE_TRAINING_GROUNDS(1811256),
	// Text: What's this? Why am I being disturbed?
	WHATS_THIS_WHY_AM_I_BEING_DISTURBED(99700),
	// Text: Ta-da! Here I am!
	TADA_HERE_I_AM(99701),
	// Text: What are you looking at?
	WHAT_ARE_YOU_LOOKING_AT(99702),
	// Text: If you give me nectar, this little Wintermelon will grow up quickly!
	IF_YOU_GIVE_ME_NECTAR_THIS_LITTLE_WINTERMELON_WILL_GROW_UP_QUICKLY(99703),
	// Text: Are you my mommy?
	ARE_YOU_MY_MOMMY(99704),
	// Text: Fancy meeting you here!
	FANCY_MEETING_YOU_HERE(99705),
	// Text: Are you afraid of the big-bad Wintermlon?
	ARE_YOU_AFRAID_OF_THE_BIGBAD_WINTERMLON(99706),
	// Text: Impressive, aren't I?
	IMPRESSIVE_ARENT_I(99707),
	// Text: Obey me!!
	OBEY_ME(99708),
	// Text: Raise me well and you'll be rewarded. Neglect me and suffer the
	// consequences!
	RAISE_ME_WELL_AND_YOULL_BE_REWARDED_NEGLECT_ME_AND_SUFFER_THE_CONSEQUENCES(99709),
	// Text: Transform!
	TRANSFORM(99710),
	// Text: I feel different?
	I_FEEL_DIFFERENT(99711),
	// Text: I'm bigger now! Bring it on!
	IM_BIGGER_NOW_BRING_IT_ON(99712),
	// Text: I'm not a kid anymore!
	IM_NOT_A_KID_ANYMORE(99713),
	// Text: Big time!
	BIG_TIME(99714),
	// Text: Good luck!
	GOOD_LUCK(99715),
	// Text: I'm all grown up now!
	IM_ALL_GROWN_UP_NOW(99716),
	// Text: If you let me go, I'll be your best friend.
	IF_YOU_LET_ME_GO_ILL_BE_YOUR_BEST_FRIEND(99717),
	// Text: I'm chuck full of goodness!
	IM_CHUCK_FULL_OF_GOODNESS(99718),
	// Text: Good job! Now what are you going to do?
	GOOD_JOB_NOW_WHAT_ARE_YOU_GOING_TO_DO(99719),
	// Text: Keep it coming!
	KEEP_IT_COMING(99720),
	// Text: That's what I'm talking about!
	THATS_WHAT_IM_TALKING_ABOUT(99721),
	// Text: May I have some more?
	MAY_I_HAVE_SOME_MORE(99722),
	// Text: That hit the spot!
	THAT_HIT_THE_SPOT(99723),
	// Text: I feel special!
	I_FEEL_SPECIAL(99724),
	// Text: I think it's working!
	I_THINK_ITS_WORKING(99725),
	// Text: You DO understand!
	YOU_DO_UNDERSTAND(99726),
	// Text: Yuck! What is this? Ha Ha just kidding!
	YUCK_WHAT_IS_THIS_HA_HA_JUST_KIDDING(99727),
	// Text: A total of five and I'll be twice as alive!
	A_TOTAL_OF_FIVE_AND_ILL_BE_TWICE_AS_ALIVE(99728),
	// Text: Nectar is sublime!
	NECTAR_IS_SUBLIME(99729),
	// Text: You call that a hit?
	YOU_CALL_THAT_A_HIT(99730),
	// Text: Why are you hitting me? Ouch, stop it! Give me nectar!
	WHY_ARE_YOU_HITTING_ME_OUCH_STOP_IT_GIVE_ME_NECTAR(99731),
	// Text: Stop or I'll wilt!
	STOP_OR_ILL_WILT(99732),
	// Text: I'm not fully grown yet! Oh well, do what you will. I'll fade away
	// without nectar anyway!
	IM_NOT_FULLY_GROWN_YET_OH_WELL_DO_WHAT_YOU_WILL_ILL_FADE_AWAY_WITHOUT_NECTAR_ANYWAY(99733),
	// Text: Go ahead and hit me again. It won't do you any good!
	GO_AHEAD_AND_HIT_ME_AGAIN_IT_WONT_DO_YOU_ANY_GOOD(99734),
	// Text: Woe is me! I'm wilting!
	WOE_IS_ME_IM_WILTING(99735),
	// Text: I'm not fully grown yet! How about some nectar to ease my pain?
	IM_NOT_FULLY_GROWN_YET_HOW_ABOUT_SOME_NECTAR_TO_EASE_MY_PAIN(99736),
	// Text: The end is near!
	THE_END_IS_NEAR(99737),
	// Text: Pretty please... with sugar on top, give me some nectar!
	PRETTY_PLEASE_WITH_SUGAR_ON_TOP_GIVE_ME_SOME_NECTAR(99738),
	// Text: If I die without nectar, you'll get nothing.
	IF_I_DIE_WITHOUT_NECTAR_YOULL_GET_NOTHING(99739),
	// Text: I'm feeling better! Another thirty seconds and I'll be out of here!
	IM_FEELING_BETTER_ANOTHER_THIRTY_SECONDS_AND_ILL_BE_OUT_OF_HERE(99740),
	// Text: Twenty seconds and it's ciao, baby!
	TWENTY_SECONDS_AND_ITS_CIAO_BABY(99741),
	// Text: Woohoo, just ten seconds left! nine... eight... seven!
	WOOHOO_JUST_TEN_SECONDS_LEFT_NINE_EIGHT_SEVEN(99742),
	// Text: Give me nectar or I'll be gone in two minutes!
	GIVE_ME_NECTAR_OR_ILL_BE_GONE_IN_TWO_MINUTES(99743),
	// Text: Give me nectar or I'll be gone in one minute!
	GIVE_ME_NECTAR_OR_ILL_BE_GONE_IN_ONE_MINUTE(99744),
	// Text: So long, losers!
	SO_LONG_LOSERS(99745),
	// Text: I'm out of here!
	IM_OUT_OF_HERE(99746),
	// Text: I must be going! Have fun everybody!
	I_MUST_BE_GOING_HAVE_FUN_EVERYBODY(99747),
	// Text: Time is up! Put your weapons down!
	TIME_IS_UP_PUT_YOUR_WEAPONS_DOWN(99748),
	// Text: Good for me, bad for you!
	GOOD_FOR_ME_BAD_FOR_YOU(99749),
	// Text: Soundtastic!
	SOUNDTASTIC(99750),
	// Text: I can sing along if you like?
	I_CAN_SING_ALONG_IF_YOU_LIKE(99751),
	// Text: I think you need some backup!
	I_THINK_YOU_NEED_SOME_BACKUP(99752),
	// Text: Keep up that rhythm and you'll be a star!
	KEEP_UP_THAT_RHYTHM_AND_YOULL_BE_A_STAR(99753),
	// Text: My heart yearns for more music.
	MY_HEART_YEARNS_FOR_MORE_MUSIC(99754),
	// Text: You're out of tune again!
	YOURE_OUT_OF_TUNE_AGAIN(99755),
	// Text: This is awful!
	THIS_IS_AWFUL(99756),
	// Text: I think I broke something!
	I_THINK_I_BROKE_SOMETHING(99757),
	// Text: What a lovely melody! Play it again!
	WHAT_A_LOVELY_MELODY_PLAY_IT_AGAIN(99758),
	// Text: Music to my, uh... ears!
	MUSIC_TO_MY_UH_EARS(99759),
	// Text: You need music lessons!
	YOU_NEED_MUSIC_LESSONS(99760),
	// Text: I can't hear you!
	I_CANT_HEAR_YOU(99761),
	// Text: You can't hurt me like that!
	YOU_CANT_HURT_ME_LIKE_THAT(99762),
	// Text: I'm stronger than you are!
	IM_STRONGER_THAN_YOU_ARE(99763),
	// Text: No music? I'm out of here!
	NO_MUSIC_IM_OUT_OF_HERE(99764),
	// Text: That racket is getting on my nerves! Tone it down a bit!
	THAT_RACKET_IS_GETTING_ON_MY_NERVES_TONE_IT_DOWN_A_BIT(99765),
	// Text: You can only hurt me through music!
	YOU_CAN_ONLY_HURT_ME_THROUGH_MUSIC(99766),
	// Text: Only musical instruments can hurt me! Nothing else!
	ONLY_MUSICAL_INSTRUMENTS_CAN_HURT_ME_NOTHING_ELSE(99767),
	// Text: Your skills are impressive, but sadly, useless...
	YOUR_SKILLS_ARE_IMPRESSIVE_BUT_SADLY_USELESS(99768),
	// Text: Catch a Chrono for me please.
	CATCH_A_CHRONO_FOR_ME_PLEASE(99769),
	// Text: You got me!
	YOU_GOT_ME(99770),
	// Text: Now look at what you've done!
	NOW_LOOK_AT_WHAT_YOUVE_DONE(99771),
	// Text: You win!
	YOU_WIN(99772),
	// Text: Fingers crossed!
	FINGERS_CROSSED(99773),
	// Text: Don't tell anyone!
	DONT_TELL_ANYONE(99774),
	// Text: Gross! My guts are coming out!
	GROSS_MY_GUTS_ARE_COMING_OUT(99775),
	// Text: Take it and go.
	TAKE_IT_AND_GO(99776),
	// Text: I should've left when I could!
	I_SHOULDVE_LEFT_WHEN_I_COULD(99777),
	// Text: Now look what you have done!
	NOW_LOOK_WHAT_YOU_HAVE_DONE(99778),
	// Text: I feel dirty!
	I_FEEL_DIRTY(99779),
	// Text: Better luck next time!
	BETTER_LUCK_NEXT_TIME(99780),
	// Text: Nice shot!
	NICE_SHOT(99781),
	// Text: I'm not afraid of you!
	IM_NOT_AFRAID_OF_YOU(99782),
	// Text: If I knew this was going to happen, I would have stayed home!
	IF_I_KNEW_THIS_WAS_GOING_TO_HAPPEN_I_WOULD_HAVE_STAYED_HOME(99783),
	// Text: Try harder or I'm out of here!
	TRY_HARDER_OR_IM_OUT_OF_HERE(99784),
	// Text: I'm tougher than I look!
	IM_TOUGHER_THAN_I_LOOK(99785),
	// Text: Good strike!
	GOOD_STRIKE(99786),
	// Text: Oh my gourd!
	OH_MY_GOURD(99787),
	// Text: That's all you've got?
	THATS_ALL_YOUVE_GOT(99788),
	// Text: Why me?
	WHY_ME(99789),
	// Text: Bring me nectar!
	BRING_ME_NECTAR(99790),
	// Text: I must have nectar to grow!
	I_MUST_HAVE_NECTAR_TO_GROW(99791),
	// Text: Give me some nectar quickly or you'll get nothing!
	GIVE_ME_SOME_NECTAR_QUICKLY_OR_YOULL_GET_NOTHING(99792),
	// Text: Please give me some nectar! I'm hungry!
	PLEASE_GIVE_ME_SOME_NECTAR_IM_HUNGRY(99793),
	// Text: Nectar please!
	NECTAR_PLEASE(99794),
	// Text: Nectar will make me grow quickly!
	NECTAR_WILL_MAKE_ME_GROW_QUICKLY(99795),
	// Text: Don't you want a bigger wintermelon? Give me some Nectar and I'll grow
	// much larger!
	DONT_YOU_WANT_A_BIGGER_WINTERMELON_GIVE_ME_SOME_NECTAR_AND_ILL_GROW_MUCH_LARGER(99796),
	// Text: If you raise me well, you'll get prizes! Or not...
	IF_YOU_RAISE_ME_WELL_YOULL_GET_PRIZES_OR_NOT(99797),
	// Text: You are here for the stuff, eh? Well it's mine, all mine!
	YOU_ARE_HERE_FOR_THE_STUFF_EH_WELL_ITS_MINE_ALL_MINE(99798),
	// Text: Trust me, give me some Nectar and I'll become a giant Wintermelon!
	TRUST_ME_GIVE_ME_SOME_NECTAR_AND_ILL_BECOME_A_GIANT_WINTERMELON(99799),
	// Text: Tra la la... Today, I'm going to make another fun-filled trip. I wonder
	// what I should look for this time...
	TRA_LA_LA_TODAY_IM_GOING_TO_MAKE_ANOTHER_FUNFILLED_TRIP__I_WONDER_WHAT_I_SHOULD_LOOK_FOR_THIS_TIME(99601),
	// Text: Oh~! Where I be~? Who call me~?
	OH_WHERE_I_BE_WHO_CALL_ME(1800903),
	// Text: Tada~! It's a watermelon~!
	TADA_ITS_A_WATERMELON(1800904),
	// Text: > <... Did ya call me...?
	__DID_YA_CALL_ME(1800905),
	// Text: Enter the watermelon~! It's gonna grow and grow from now on!
	ENTER_THE_WATERMELON_ITS_GONNA_GROW_AND_GROW_FROM_NOW_ON(1800906),
	// Text: Oh, ouch~! Did I see you before~?
	OH_OUCH_DID_I_SEE_YOU_BEFORE(1800907),
	// Text: A new season! Summer is all about the watermelon~!
	A_NEW_SEASON_SUMMER_IS_ALL_ABOUT_THE_WATERMELON(1800908),
	// Text: Did ya call~? Ho! Thought you'd get something~?^^
	DID_YA_CALL_HO_THOUGHT_YOUD_GET_SOMETHING(1800909),
	// Text: Do you want to see my beautiful self~?
	DO_YOU_WANT_TO_SEE_MY_BEAUTIFUL_SELF(1800910),
	// Text: Hohoho! Let's do it together~!
	HOHOHO_LETS_DO_IT_TOGETHER(1800911),
	// Text: It's a giant watermelon if you raise it right~ And a watermelon slice
	// if you mess up~!
	ITS_A_GIANT_WATERMELON_IF_YOU_RAISE_IT_RIGHT_AND_A_WATERMELON_SLICE_IF_YOU_MESS_UP(1800912),
	// Text: Tada~ Transformation~ complete~!
	TADA_TRANSFORMATION_COMPLETE(1800913),
	// Text: Am I a rain watermelon? Or a defective watermelon?
	AM_I_A_RAIN_WATERMELON_OR_A_DEFECTIVE_WATERMELON(1800914),
	// Text: Now! I've gotten big~! Everyone, come at me!
	NOW_IVE_GOTTEN_BIG_EVERYONE_COME_AT_ME(1800915),
	// Text: Get bigger~ Get stronger~! Tell me your wish~~!
	GET_BIGGER_GET_STRONGER_TELL_ME_YOUR_WISH(1800916),
	// Text: A watermelon slice's wish! But I'm bigger already?
	A_WATERMELON_SLICES_WISH_BUT_IM_BIGGER_ALREADY(1800917),
	// Text: A large watermelon's wish! Well, try to break me!
	A_LARGE_WATERMELONS_WISH_WELL_TRY_TO_BREAK_ME(1800918),
	// Text: I'm done growing~! I'm running away now~^^
	IM_DONE_GROWING_IM_RUNNING_AWAY_NOW(1800919),
	// Text: If you let me go, I'll give you ten million Adena!
	IF_YOU_LET_ME_GO_ILL_GIVE_YOU_TEN_MILLION_ADENA(1800920),
	// Text: Freedom~! What do you think I have inside?
	FREEDOM_WHAT_DO_YOU_THINK_I_HAVE_INSIDE(1800921),
	// Text: OK~ OK~ Good job. You know what to do next, right?
	OK_OK_GOOD_JOB_YOU_KNOW_WHAT_TO_DO_NEXT_RIGHT(1800922),
	// Text: Look here! Do it right! You spilled! This precious...
	LOOK_HERE_DO_IT_RIGHT_YOU_SPILLED_THIS_PRECIOUS(1800923),
	// Text: Ah~ Refreshing! Spray a little more~!
	AH_REFRESHING_SPRAY_A_LITTLE_MORE(1800924),
	// Text: Gulp gulp~ Great! But isn't there more?
	GULP_GULP_GREAT_BUT_ISNT_THERE_MORE(1800925),
	// Text: Can't you even aim right? Have you even been to the army?
	CANT_YOU_EVEN_AIM_RIGHT_HAVE_YOU_EVEN_BEEN_TO_THE_ARMY(1800926),
	// Text: Did you mix this with water~? Why's it taste like this~!
	DID_YOU_MIX_THIS_WITH_WATER_WHYS_IT_TASTE_LIKE_THIS(1800927),
	// Text: Oh! Good! Do a little more. Yeah~
	OH_GOOD_DO_A_LITTLE_MORE_YEAH(1800928),
	// Text: Hoho! It's not there! Over here~! Am I so small that you can even spray
	// me right?
	HOHO_ITS_NOT_THERE_OVER_HERE_AM_I_SO_SMALL_THAT_YOU_CAN_EVEN_SPRAY_ME_RIGHT(1800929),
	// Text: Yuck! What is this~! Are you sure this is nectar~?
	YUCK_WHAT_IS_THIS_ARE_YOU_SURE_THIS_IS_NECTAR(1800930),
	// Text: Do your best~! I become a big watermelon after just five bottles~!
	DO_YOUR_BEST_I_BECOME_A_BIG_WATERMELON_AFTER_JUST_FIVE_BOTTLES(1800931),
	// Text: Of course~! Watermelon is the best nectar! Hahaha~!
	OF_COURSE_WATERMELON_IS_THE_BEST_NECTAR_HAHAHA(1800932),
	// Text: Ahh! Good~ > < Slap slap me~
	AHH_GOOD___SLAP_SLAP_ME(1800933),
	// Text: Oww! You're just beating me now~ Give me nectar~
	OWW_YOURE_JUST_BEATING_ME_NOW_GIVE_ME_NECTAR(1800934),
	// Text: Look~!! It's gonna break~!
	LOOK_ITS_GONNA_BREAK(1800935),
	// Text: Now! Are you trying to eat without doing the work? Fine~ Do what you
	// want~ I'll hate you if you don't give me any nectar!
	NOW_ARE_YOU_TRYING_TO_EAT_WITHOUT_DOING_THE_WORK_FINE_DO_WHAT_YOU_WANT_ILL_HATE_YOU_IF_YOU_DONT_GIVE_ME_ANY_NECTAR(
																														1800936),
	// Text: Hit me more! Hit me more!
	HIT_ME_MORE_HIT_ME_MORE(1800937),
	// Text: I'm gonna wither like this~ Damn it~!
	IM_GONNA_WITHER_LIKE_THIS_DAMN_IT(1800938),
	// Text: Hey you~ If I die like this, there'll be no item either~ Are you really
	// so stingy with the nectar?
	HEY_YOU_IF_I_DIE_LIKE_THIS_THERELL_BE_NO_ITEM_EITHER_ARE_YOU_REALLY_SO_STINGY_WITH_THE_NECTAR(1800939),
	// Text: It's just a little more!! Good luck~!
	ITS_JUST_A_LITTLE_MORE_GOOD_LUCK(1800940),
	// Text: Save me~ I'm about to die without tasting nectar even once~
	SAVE_ME_IM_ABOUT_TO_DIE_WITHOUT_TASTING_NECTAR_EVEN_ONCE(1800941),
	// Text: If I die like this, I'll just be a watermelon slice~!
	IF_I_DIE_LIKE_THIS_ILL_JUST_BE_A_WATERMELON_SLICE(1800942),
	// Text: I'm getting stronger~? I think I'll be able to run away in 30 seconds~
	// Hoho~
	IM_GETTING_STRONGER_I_THINK_ILL_BE_ABLE_TO_RUN_AWAY_IN_30_SECONDS_HOHO(1800943),
	// Text: It's goodbye after 20 seconds~!
	ITS_GOODBYE_AFTER_20_SECONDS(1800944),
	// Text: Yeah, 10 seconds left~! 9... 8... 7...!
	YEAH_10_SECONDS_LEFT_9_8_7(1800945),
	// Text: I'm leaving in 2 minutes if you don't give me any nectar~!
	IM_LEAVING_IN_2_MINUTES_IF_YOU_DONT_GIVE_ME_ANY_NECTAR(1800946),
	// Text: I'm leaving in 1 minutes if you don't give me any nectar~!
	IM_LEAVING_IN_1_MINUTES_IF_YOU_DONT_GIVE_ME_ANY_NECTAR(1800947),
	// Text: I'm leaving now~! Then, goodbye~!
	IM_LEAVING_NOW_THEN_GOODBYE(1800948),
	// Text: Sorry, but this large watermelon is disappearing here~!
	SORRY_BUT_THIS_LARGE_WATERMELON_IS_DISAPPEARING_HERE(1800949),
	// Text: Too late~! Have a good time~!
	TOO_LATE_HAVE_A_GOOD_TIME(1800950),
	// Text: Ding ding~ That's the bell. Put away your weapons and try for next
	// time~!
	DING_DING_THATS_THE_BELL_PUT_AWAY_YOUR_WEAPONS_AND_TRY_FOR_NEXT_TIME(1800951),
	// Text: Too bad~! You raised it up, too! - -
	TOO_BAD_YOU_RAISED_IT_UP_TOO__(1800952),
	// Text: Oh~ What a nice sound~
	OH_WHAT_A_NICE_SOUND(1800953),
	// Text: The instrument is nice, but there's no song. Shall I sing for you?
	THE_INSTRUMENT_IS_NICE_BUT_THERES_NO_SONG_SHALL_I_SING_FOR_YOU(1800954),
	// Text: What beautiful music!
	WHAT_BEAUTIFUL_MUSIC(1800955),
	// Text: I feel good~ Play some more!
	I_FEEL_GOOD_PLAY_SOME_MORE(1800956),
	// Text: My heart is being captured by the sound of Crono!
	MY_HEART_IS_BEING_CAPTURED_BY_THE_SOUND_OF_CRONO(1800957),
	// Text: Get the notes right~! Hey old man~ That was wrong!
	GET_THE_NOTES_RIGHT_HEY_OLD_MAN_THAT_WAS_WRONG(1800958),
	// Text: I like it~!
	I_LIKE_IT(1800959),
	// Text: Ooh~~ My body wants to open!
	OOH_MY_BODY_WANTS_TO_OPEN(1800960),
	// Text: Oh~! This chord! My heart is being torn! Play a little more!
	OH_THIS_CHORD_MY_HEART_IS_BEING_TORN_PLAY_A_LITTLE_MORE(1800961),
	// Text: It's this~ This! I wanted this sound! Why don't you try becoming a
	// singer?
	ITS_THIS_THIS_I_WANTED_THIS_SOUND_WHY_DONT_YOU_TRY_BECOMING_A_SINGER(1800962),
	// Text: You can try a hundred times on this~ You won't get anything good~!
	YOU_CAN_TRY_A_HUNDRED_TIMES_ON_THIS_YOU_WONT_GET_ANYTHING_GOOD(1800963),
	// Text: It hurts~! Play just the instrument~!
	IT_HURTS_PLAY_JUST_THE_INSTRUMENT(1800964),
	// Text: Only good music can open my body!
	ONLY_GOOD_MUSIC_CAN_OPEN_MY_BODY(1800965),
	// Text: Not this, but you know, that~ What you got as a Chronicle souvenir.
	// Play with that!
	NOT_THIS_BUT_YOU_KNOW_THAT_WHAT_YOU_GOT_AS_A_CHRONICLE_SOUVENIR_PLAY_WITH_THAT(1800966),
	// Text: Why~ You have no music? Boring... I'm leaving now~
	WHY_YOU_HAVE_NO_MUSIC_BORING_IM_LEAVING_NOW(1800967),
	// Text: Not those sharp things~! Use the ones that make nice sounds!
	NOT_THOSE_SHARP_THINGS_USE_THE_ONES_THAT_MAKE_NICE_SOUNDS(1800968),
	// Text: Large watermelons only open with music~ Just striking with a weapon
	// won't work~!
	LARGE_WATERMELONS_ONLY_OPEN_WITH_MUSIC_JUST_STRIKING_WITH_A_WEAPON_WONT_WORK(1800969),
	// Text: Strike with music~! Not with something like this. You need music~!
	STRIKE_WITH_MUSIC_NOT_WITH_SOMETHING_LIKE_THIS_YOU_NEED_MUSIC(1800970),
	// Text: You're pretty amazing~! But it's all for nothing~~!
	YOURE_PRETTY_AMAZING_BUT_ITS_ALL_FOR_NOTHING(1800971),
	// Text: Use that on monsters, OK? I want Crono~!
	USE_THAT_ON_MONSTERS_OK_I_WANT_CRONO(1800972),
	// Text: Everyone~ The watermelon is breaking!!!
	EVERYONE_THE_WATERMELON_IS_BREAKING(1800973),
	// Text: It's like a watermelon slice~!
	ITS_LIKE_A_WATERMELON_SLICE(1800974),
	// Text: A goblin is coming out(?)!
	A_GOBLIN_IS_COMING_OUT(1800975),
	// Text: Large watermelon~! Make a wish!!
	LARGE_WATERMELON_MAKE_A_WISH(1800976),
	// Text: Don't tell anyone about my death~
	DONT_TELL_ANYONE_ABOUT_MY_DEATH(1800977),
	// Text: Ugh~ The red juice is flowing out!
	UGH_THE_RED_JUICE_IS_FLOWING_OUT(1800978),
	// Text: This is all~
	THIS_IS_ALL(1800979),
	// Text: Kyaahh!!! I'm mad...!
	KYAAHH_IM_MAD(1800980),
	// Text: Everyone~ This watermelon broke open!! The item is falling out!
	EVERYONE_THIS_WATERMELON_BROKE_OPEN_THE_ITEM_IS_FALLING_OUT(1800981),
	// Text: Oh! It burst! The contents are spilling out~
	OH_IT_BURST_THE_CONTENTS_ARE_SPILLING_OUT(1800982),
	// Text: Hohoho~ Play better!
	HOHOHO_PLAY_BETTER(1800983),
	// Text: Oh~!! You're very talented, huh~?
	OH_YOURE_VERY_TALENTED_HUH(1800984),
	// Text: Play some more! More! More! More!
	PLAY_SOME_MORE_MORE__MORE__MORE(1800985),
	// Text: I eat hits and grow!!
	I_EAT_HITS_AND_GROW(1800986),
	// Text: Buck up~ There isn't much time~
	BUCK_UP_THERE_ISNT_MUCH_TIME(1800987),
	// Text: Do you think I'll burst with just that~?
	DO_YOU_THINK_ILL_BURST_WITH_JUST_THAT(1800988),
	// Text: What a nice attack~ You might be able to kill a passing fly~
	WHAT_A_NICE_ATTACK_YOU_MIGHT_BE_ABLE_TO_KILL_A_PASSING_FLY(1800989),
	// Text: Right there! A little to the right~! Ah~ Refreshing.
	RIGHT_THERE_A_LITTLE_TO_THE_RIGHT_AH_REFRESHING(1800990),
	// Text: You call that hitting? Bring some more talented friends!
	YOU_CALL_THAT_HITTING_BRING_SOME_MORE_TALENTED_FRIENDS(1800991),
	// Text: Don't think! Just hit! We're hitting!
	DONT_THINK_JUST_HIT_WERE_HITTING(1800992),
	// Text: I need nectar~ Gourd nectar!
	I_NEED_NECTAR_GOURD_NECTAR(1800993),
	// Text: I can only grow by drinking nectar~
	I_CAN_ONLY_GROW_BY_DRINKING_NECTAR(1800994),
	// Text: Grow me quick! If you're good, it's a large watermelon. If you're bad,
	// it a watermelon slice~!
	GROW_ME_QUICK_IF_YOURE_GOOD_ITS_A_LARGE_WATERMELON_IF_YOURE_BAD_IT_A_WATERMELON_SLICE(1800995),
	// Text: Gimme nectar~ I'm hungry~
	GIMME_NECTAR_IM_HUNGRY(1800996),
	// Text: Hurry and bring me nectar... Not a necktie~... (sorry)
	HURRY_AND_BRING_ME_NECTAR_NOT_A_NECKTIE_SORRY(1800997),
	// Text: Bring me nectar. Then, I'll drink and grow!
	BRING_ME_NECTAR_THEN_ILL_DRINK_AND_GROW(1800998),
	// Text: You wanna eat a tiny watermelon like me? Try giving me some nectar.
	// I'll get huge~!
	YOU_WANNA_EAT_A_TINY_WATERMELON_LIKE_ME_TRY_GIVING_ME_SOME_NECTAR_ILL_GET_HUGE(1800999),
	// Text: Hehehe... Grow me well and you'll get a reward. Grow me bad and who
	// knows what'll happen~
	HEHEHE_GROW_ME_WELL_AND_YOULL_GET_A_REWARD_GROW_ME_BAD_AND_WHO_KNOWS_WHATLL_HAPPEN(1801000),
	// Text: You want a large watermelon? I'd like to be a watermelon slice~
	YOU_WANT_A_LARGE_WATERMELON_ID_LIKE_TO_BE_A_WATERMELON_SLICE(1801001),
	// Text: Trust me and bring me some nectar!! I'll become a large watermelon for
	// you~!
	TRUST_ME_AND_BRING_ME_SOME_NECTAR_ILL_BECOME_A_LARGE_WATERMELON_FOR_YOU(1801002),
	// Text: Heyheyhey... Don't hit me, but love me~
	HEYHEYHEY_DONT_HIT_ME_BUT_LOVE_ME(1620078),
	// Text: Hello. Master~!
	HELLO_MASTER(1620079),
	// Text: You're always so nice ^^
	YOURE_ALWAYS_SO_NICE_(1620080),
	// Text: Nice to see you too~
	NICE_TO_SEE_YOU_TOO(1620081),
	// Text: Hello~ You don't have to be polite with me^^
	HELLO_YOU_DONT_HAVE_TO_BE_POLITE_WITH_ME(1620082),
	// Text: May the one strand of cool wind be with you master...
	MAY_THE_ONE_STRAND_OF_COOL_WIND_BE_WITH_YOU_MASTER(1620083),
	// Text: The world revolves around you master~
	THE_WORLD_REVOLVES_AROUND_YOU_MASTER(1620084),
	// Text: Master did something sad happen?
	MASTER_DID_SOMETHING_SAD_HAPPEN(1620085),
	// Text: The sky becomes clear after a rain~. Let us both cheer up^^
	THE_SKY_BECOMES_CLEAR_AFTER_A_RAIN_LET_US_BOTH_CHEER_UP(1620086),
	// Text: I'm beside you~
	IM_BESIDE_YOU(1620087),
	// Text: If you become the king of Aden... I will think about it..
	IF_YOU_BECOME_THE_KING_OF_ADEN_I_WILL_THINK_ABOUT_IT(1620088),
	// Text: No you cannot. Master~!
	NO_YOU_CANNOT_MASTER(1620089),
	// Text: Master~! - -
	MASTER__(1620090),
	// Text: ......
	DOT_DOT_DOT_DOT_DOT_DOT(1620091),
	// Text: This is a hideout made with 'Shine Stone' used in Giant's Dimensional
	// technique. Supposedly the capital of the Giants that have not been found is
	// still in the dimensional gap somewhere...
	THIS_IS_A_HIDEOUT_MADE_WITH_SHINE_STONE_USED_IN_GIANTS_DIMENSIONAL_TECHNIQUE__SUPPOSEDLY_THE_CAPITAL_OF_THE_GIANTS_THAT_HAVE_NOT_BEEN_FOUND_IS_STILL_IN_THE_DIMENSIONAL_GAP_SOMEWHERE(
																																															1620092),
	// Text: The evil Land Dragon Antharas has been defeated by brave heroes!!!
	THE_EVIL_LAND_DRAGON_ANTHARAS_HAS_BEEN_DEFEATED_BY_BRAVE_HEROES_(901900150),
	// Text: The evil Fire Dragon Valakas has been defeated!
	THE_EVIL_FIRE_DRAGON_VALAKAS_HAS_BEEN_DEFEATED_(901900151),
	// Text: An intense cold is coming. Look around.
	AN_INTENSE_COLD_IS_COMING_LOOK_AROUND(1803260),
	// Text: You've successfully sealed the Altar of Earth!
	YOUVE_SUCCESSFULLY_SEALED_THE_ALTAR_OF_EARTH(1803046),
	// Text: You've successfully sealed the Altar of Wind!
	YOUVE_SUCCESSFULLY_SEALED_THE_ALTAR_OF_WIND(1803058),
	// Text: The door to the Dungeon has been opened!
	THE_DOOR_TO_THE_DUNGEON_HAS_BEEN_OPENED(1803059),
	// Text: The door to Kelbim's throne has opened!
	THE_DOOR_TO_KELBIMS_THRONE_HAS_OPENED(1803074),
	// Text: Are you ready to hear the story?
	ARE_YOU_READY_TO_HEAR_THE_STORY(1803193),
	// Text: I'll start once everyone is seated.
	ILL_START_ONCE_EVERYONE_IS_SEATED(1803194),
	// Text: Heh! What should I talk about next? Hmm..
	HEH_WHAT_SHOULD_I_TALK_ABOUT_NEXT_HMM(1803195),
	// Text: Well, which story do you want to hear?
	WELL_WHICH_STORY_DO_YOU_WANT_TO_HEAR(1803196),
	// Text: I wonder what kind of stories are popular with the customers..
	I_WONDER_WHAT_KIND_OF_STORIES_ARE_POPULAR_WITH_THE_CUSTOMERS(1803197),
	// Text: Sit down first. I can't start otherwise.
	SIT_DOWN_FIRST_I_CANT_START_OTHERWISE(1803198),
	// Text: Should I start? Let's see if we're ready..
	SHOULD_I_START_LETS_SEE_IF_WERE_READY(1803199),
	// Text: I'll be starting now, so take a seat.
	ILL_BE_STARTING_NOW_SO_TAKE_A_SEAT_(1803200),
	// Text: Which story do you want to hear?
	WHICH_STORY_DO_YOU_WANT_TO_HEAR(1803201),
	// Text: I have many stories to tell.
	I_HAVE_MANY_STORIES_TO_TELL(1803202),
	// Text: Please sit down so that I can start.
	PLEASE_SIT_DOWN_SO_THAT_I_CAN_START(1803203),
	// Text: Well! Whose story should I tell you today?
	WELL_WHOSE_STORY_SHOULD_I_TELL_YOU_TODAY(1803204),
	// Text: Hurry! Sit down!
	HURRY_SIT_DOWN(1803205),
	// Text: Whose story do you want to hear?
	WHOSE_STORY_DO_YOU_WANT_TO_HEAR(1803206),
	// Text: You have to be ready.
	YOU_HAVE_TO_BE_READY(1803207),
	// Text: The winner of the Ceremony of Chaos has been born.
	THE_WINNER_OF_THE_CEREMONY_OF_CHAOS_HAS_BEEN_BORN(1717806),
	// Text: You are the clan member who produced $s1, this week's winner. You have
	// tremendous abilities and luck. I give you my master's blessing...!
	YOU_ARE_THE_CLAN_MEMBER_WHO_PRODUCED_S1_THIS_WEEKS_WINNER_YOU_HAVE_TREMENDOUS_ABILITIES_AND_LUCK_I_GIVE_YOU_MY_MASTERS_BLESSING(
																																	1801644),
	// Text: The clan that seized the most honor this week is $s1.
	THE_CLAN_THAT_SEIZED_THE_MOST_HONOR_THIS_WEEK_IS_S1(1801645),
	// Text: Is there no clan that can raise a true ultimate warrior...?
	IS_THERE_NO_CLAN_THAT_CAN_RAISE_A_TRUE_ULTIMATE_WARRIOR(1801646),
	// Text: Your clan has acquired $s1 point(s) to its Clan Reputation.
	YOUR_CLAN_HAS_ACQUIRED_S1_POINTS_TO_ITS_CLAN_REPUTATION(1600043),
	// Text: The teleport gate to the 2nd floor has been activated.
	THE_TELEPORT_GATE_TO_THE_2ND_FLOOR_HAS_BEEN_ACTIVATED(1803018),
	// Text: It looks like Captain Adolph wants to talk to the Party Leader. Go talk
	// to Adolph.
	IT_LOOKS_LIKE_CAPTAIN_ADOLPH_WANTS_TO_TALK_TO_THE_PARTY_LEADER_GO_TALK_TO_ADOLPH(1803019),
	// Text: The teleport gate to the 3rd floor has been activated.
	THE_TELEPORT_GATE_TO_THE_3RD_FLOOR_HAS_BEEN_ACTIVATED(1803020),
	// Text: You've successfully attacked the Command Post and defeated Commander
	// Burnstein.
	YOUVE_SUCCESSFULLY_ATTACKED_THE_COMMAND_POST_AND_DEFEATED_COMMANDER_BURNSTEIN(1803021),
	// Text: Stop there.
	STOP_THERE(1801304),
	// Text: Waaaaaaaahhhhhh
	WAAAAAAAAHHHHHH(1801305),
	// Text: Die!
	DIE_(1801306),
	// Text: Turn to ashes.
	TURN_TO_ASHES(1801307),
	// Text: Multiple goals, shooting complete
	MULTIPLE_GOALS_SHOOTING_COMPLETE(1801308),
	// Text: Goal discovered nearby. Begin shooting.
	GOAL_DISCOVERED_NEARBY_BEGIN_SHOOTING(1801309),
	// Text: Aim for goal. Shooting complete.
	AIM_FOR_GOAL_SHOOTING_COMPLETE(1801310),
	// Text: Stop the infiltrators! Strengthen all defenses!
	STOP_THE_INFILTRATORS_STRENGTHEN_ALL_DEFENSES(1802929),
	// Text: Master Kelbim won't let you just be!
	MASTER_KELBIM_WONT_LET_YOU_JUST_BE(1802930),
	// Text: Be ready. They might come out any time.
	BE_READY_THEY_MIGHT_COME_OUT_ANY_TIME(1802931),
	// Text: Did you think you could escape? Take back the stronghold and the
	// captives.
	DID_YOU_THINK_YOU_COULD_ESCAPE_TAKE_BACK_THE_STRONGHOLD_AND_THE_CAPTIVES(1802932),
	// Text: Liona and the lowly soldiers will be buried here.
	LIONA_AND_THE_LOWLY_SOLDIERS_WILL_BE_BURIED_HERE(1802954),
	// Text: Show them the power of Kelbim.
	SHOW_THEM_THE_POWER_OF_KELBIM(1802955),
	// Text: Unit 1! We need reinforcements!
	UNIT_1_WE_NEED_REINFORCEMENTS(1802999),
	// Text: All the recruits are to train and follow the sergeants!
	ALL_THE_RECRUITS_ARE_TO_TRAIN_AND_FOLLOW_THE_SERGEANTS(1803000),
	// Text: Soldiers, we have guests. Welcome them.
	SOLDIERS_WE_HAVE_GUESTS_WELCOME_THEM_(1803001),
	// Text: Don't let a single one leave this place alive!
	DONT_LET_A_SINGLE_ONE_LEAVE_THIS_PLACE_ALIVE(1803005),
	// Text: Useless bunch… I'll deal with you.
	USELESS_BUNCH_ILL_DEAL_WITH_YOU(1803010),
	// Text: Commander Burnstein… I wasn't able to complete my mission…
	COMMANDER_BURNSTEIN_I_WASNT_ABLE_TO_COMPLETE_MY_MISSION(1803011),
	// Text: $s1. Stop kidding yourself about your own powerlessness!
	S1_STOP_KIDDING_YOURSELF_ABOUT_YOUR_OWN_POWERLESSNESS(1000028),
	// Text: $s1. I'll make you feel what true fear is!
	S1_ILL_MAKE_YOU_FEEL_WHAT_TRUE_FEAR_IS(1000029),
	// Text: You're really stupid to have challenged me. $s1! Get ready!
	YOURE_REALLY_STUPID_TO_HAVE_CHALLENGED_ME_S1_GET_READY(1000030),
	// Text: $s1. Do you think that's going to work?!
	S1_DO_YOU_THINK_THATS_GOING_TO_WORK(1000031),
	// Text: $s1! Watch your back!
	S1_WATCH_YOUR_BACK(1000381),
	// Text: Your rear is practically unguarded!
	YOUR_REAR_IS_PRACTICALLY_UNGUARDED(1000382),
	// Divine Angels are nowhere to be seen! I want to talk to the party leader!
	DIVINE_ANGELS_ARE_NOWHERE(1803286),
	// Destroy weakened Divine Angels\0
	DESTROY_WEAKED_DIVINE_ANGELS(1803287),
	// Set off bombs and get treasures
	SET_OFF_BOMBS_AND_GET_TREASURE(1803288),
	// Protect the Central Tower from Divine Angels
	PROTECT_THE_CENTRAL_TOWER_FROM_DIVINE_ANGELS(1803289),
	// Text: The lord of Gludio is dead. The seal is available now.
	THE_LORD_OF_GLUDIO_IS_DEAD_THE_SEAL_IS_AVAILABLE_NOW(1803353),
	// Text: Ye not be finding me below the drink!
	YE_NOT_BE_FINDING_ME_BELOW_THE_DRINK(1800868),
	// Text: Even the Magic Force binds yo you will never be forgiven...
	EVEN_THE_MAGIC_FORCE__BINDS_YO_YOU__WILL_NEVER_BE_FORGIVEN(1800869),
	// Text: Ye not be finding me in the Crows Nest.
	YE_NOT_BE_FINDING_ME_IN_THE_CROWS_NEST(1800870),
	// Text: The day has come... While I'm restoring my powers, my servants must
	// protect the Devil's Isle!
	THE_DAY_HAS_COME_WHILE_IM_RESTORING_MY_POWERS_MY_SERVANTS_MUST_PROTECT_THE_DEVILS_ISLE(1803625),
	// Text: My time has come. Oh my servants, reveal the image I have granted you!
	MY_TIME_HAS_COME_OH_MY_SERVANTS_REVEAL_THE_IMAGE_I_HAVE_GRANTED_YOU(1803626),
	// Text: Zaken's treasure map! No!
	ZAKENS_TREASURE_MAP_NO(1803627),
	// Text: Losers! You fall under my sword!
	LOSERS_YOU_FALL_UNDER_MY_SWORD(1803628),
	// Text: Your blood will be my flesh!
	YOUR_BLOOD_WILL_BE_MY_FLESH(1803629),
	// Text: Ha, ha, ha! Try and find me!
	HA_HA_HA_TRY_AND_FIND_ME(1803630),
	// Text: You're the weakest! Shoo!
	YOURE_THE_WEAKEST_SHOO(1803631),
	// Text: Not bad. Let's go to the deck, it's time to finish with it.
	NOT_BAD_LETS_GO_TO_THE_DECK_ITS_TIME_TO_FINISH_WITH_IT(1803632),
	// Text: $s1 has extended the duration for $s2 sec.
	S1_HAS_EXTENDED_THE_DURATION_FOR_S2_SEC(1640001),
	// Text: The countdown timer is paused for 1 min. Every character may receive
	// supplies once per stage.
	THE_COUNTDOWN_TIMER_IS_PAUSED_FOR_1_MIN_EVERY_CHARACTER_MAY_RECEIVE_SUPPLIES_ONCE_PER_STAGE(1803639),
	// Text: The countdown is on! The raid begins!
	THE_COUNTDOWN_IS_ON_THE_RAID_BEGINS(1803640),
	// Text: $s1 has received a Valor Box!
	S1_HAS_RECEIVED_A_VALOR_BOX(1640004),
	// Text: To extend the duration click the Extend duration button.
	TO_EXTEND_THE_DURATION_CLICK_THE_EXTEND_DURATION_BUTTON(1803641),
	// Text: Time extension error!
	TIME_EXTENSION_ERROR(1803642),
	// Text: 5 minutes until Raid Boss goes berserk.
	_5_MINUTES_UNTIL_RAID_BOSS_GOES_BERSERK(1803273),
	// Text: Raid Boss went berserk!
	RAID_BOSS_WENT_BERSERK(1803274),
	// Text: Raid Boss went back to normal.
	RAID_BOSS_WENT_BACK_TO_NORMAL(1803275),
	// Text: Be careful! You are about to face the last Ol Mahum!
	BE_CAREFUL_YOU_ARE_ABOUT_TO_FACE_THE_LAST_OL_MAHUM(1803643),
	// Text: Come what may - it is not your fault.
	COME_WHAT_MAY__IT_IS_NOT_YOUR_FAULT(1803644),
	// Text: Bring over and surrender your precious gold treasure to me!
	BRING_OVER_AND_SURRENDER_YOUR_PRECIOUS_GOLD_TREASURE_TO_ME(1803645),
	// Text: I'm covered in Kraven's blood… You will be punished!
	IM_COVERED_IN_KRAVENS_BLOOD_YOU_WILL_BE_PUNISHED(1803646),
	// Text: Who is real? It is not easy to tell!
	WHO_IS_REAL_IT_IS_NOT_EASY_TO_TELL(1803647),
	// Text: I will be fighting until Kandra finally accepts me!
	I_WILL_BE_FIGHTING_UNTIL_KANDRA_FINALLY_ACCEPTS_ME(1803648),
	// Text: This is the power of Orcs! Hahaha!
	THIS_IS_THE_POWER_OF_ORCS_HAHAHA(1803649),
	// Text: I'm gonna make you feel my rage!
	IM_GONNA_MAKE_YOU_FEEL_MY_RAGE(1803650),
	// Text: Where is Leo? Where have you hidden him?
	WHERE_IS_LEO_WHERE_HAVE_YOU_HIDDEN_HIM(1803651),
	// Text: It is just the beginning… You will fail!
	IT_IS_JUST_THE_BEGINNING_YOU_WILL_FAIL(1803652),
	// Text: We are nothing like these Varka weaklings!
	WE_ARE_NOTHING_LIKE_THESE_VARKA_WEAKLINGS(1803653),
	// Text: I'll kill you! This is Hallate's will! Die!
	ILL_KILL_YOU_THIS_IS_HALLATES_WILL_DIE(1803654),
	// Text: Fine, let's see what you can do.
	FINE_LETS_SEE_WHAT_YOU_CAN_DO(1803655),
	// Text: I will show no mercy! I will kill you!
	I_WILL_SHOW_NO_MERCY_I_WILL_KILL_YOU(1803656),
	// Text: I'll make you suffer! You will burn forever!
	ILL_MAKE_YOU_SUFFER_YOU_WILL_BURN_FOREVER(1803657),
	// Text: I'll make everyone feel this pain!
	ILL_MAKE_EVERYONE_FEEL_THIS_PAIN(1803658),
	// Text: You worthless scum… I will break you!
	YOU_WORTHLESS_SCUM_I_WILL_BREAK_YOU(1803659),
	// Text: There is nothing you can do!
	THERE_IS_NOTHING_YOU_CAN_DO(1803660),
	// Text: You think you can take me?
	YOU_THINK_YOU_CAN_TAKE_ME(1803661),
	// Text: Run, run!
	RUN_RUN(1803662),
	// Text: You are stronger than you look. But this was just a test.
	YOU_ARE_STRONGER_THAN_YOU_LOOK_BUT_THIS_WAS_JUST_A_TEST(1803663),
	// Text: This was a mistake! You will never defeat me!
	THIS_WAS_A_MISTAKE_YOU_WILL_NEVER_DEFEAT_ME(1803664),
	// Text: Ha! Not bad.
	HA_NOT_BAD(1803665),
	// Text: Welcome to the Arena! Test your clan's strength!
	WELCOME_TO_THE_ARENA_TEST_YOUR_CLANS_STRENGTH(1803666),
	// Text: The countdown is on! The raid begins! To extend the duration you can
	// click the Extend duration button.
	THE_COUNTDOWN_IS_ON_THE_RAID_BEGINS_TO_EXTEND_THE_DURATION_YOU_CAN_CLICK_THE_EXTEND_DURATION_BUTTON(1803667),
	// Text: All levels are completed.
	ALL_LEVELS_ARE_COMPLETED(1640005),
	// Text: What a lovely day! The treasure is mine!
	WHAT_A_LOVELY_DAY_THE_TREASURE_IS_MINE(1803668),
	// Text: Wow! This was a pure success!
	WOW_THIS_WAS_A_PURE_SUCCESS(1803669),
	// Text: The treasure chest is over there! Come, quickly!
	THE_TREASURE_CHEST_IS_OVER_THERE_COME_QUICKLY(1803670),
	// Text: There, that's the treasure chest! Let's see…
	THERE_THATS_THE_TREASURE_CHEST_LETS_SEE(1803671),
	// Text: The treasure is ours! We will rule the world!
	THE_TREASURE_IS_OURS_WE_WILL_RULE_THE_WORLD(1803672),
	// Text: I see the treasure chest! Come on, let's go!
	I_SEE_THE_TREASURE_CHEST_COME_ON_LETS_GO(1803673),
	// Text: $s1, that's the treasure… It's our lucky day!
	S1_THATS_THE_TREASURE_ITS_OUR_LUCKY_DAY(1803674),
	// Text: Those guys with $s1 got lucky!
	THOSE_GUYS_WITH_S1_GOT_LUCKY(1803675),
	// Text: If only you knew how much effort it took us to find this treasure…
	// Thank you.
	IF_ONLY_YOU_KNEW_HOW_MUCH_EFFORT_IT_TOOK_US_TO_FIND_THIS_TREASURE_THANK_YOU(1803676),
	// Text: The countdown is on! The last raid has begun!
	THE_COUNTDOWN_IS_ON_THE_LAST_RAID_HAS_BEGUN(1803677),
	// Text: All raids are finished for now. We will prepare new arena raids for
	// you. Please come back soon!
	ALL_RAIDS_ARE_FINISHED_FOR_NOW_WE_WILL_PREPARE_NEW_ARENA_RAIDS_FOR_YOU_PLEASE_COME_BACK_SOON(1803678),
	// Text: The chest is empty!
	THE_CHEST_IS_EMPTY(1803679),
	// Text: Don't think that treasure hunting is going to be easy…
	DONT_THINK_THAT_TREASURE_HUNTING_IS_GOING_TO_BE_EASY(1803680),
	// Text: Oh! This is a treasure chest!
	OH_THIS_IS_A_TREASURE_CHEST(1803681),
	// Text: I see a chest full of treasures!
	I_SEE_A_CHEST_FULL_OF_TREASURES(1803682),
	// Text: Antharas is trying to escape.
	ANTHARAS_IS_TRYING_TO_ESCAPE(1803683),
	// Text: Balthus Knights are looking for mercenaries!
	BALTHUS_KNIGHTS_ARE_LOOKING_FOR_MERCENARIES(1803684),
	// Text: Let's join our forces and face this together!
	LETS_JOIN_OUR_FORCES_AND_FACE_THIS_TOGETHER(1803685),
	// Text: minute(s) have passed.
	MINUTES_HAVE_PASSED(1000456),
	// Text: Game over. The teleport will appear momentarily.
	GAME_OVER(1000457),
	// Text: You may now enter the Sepulcher.
	YOU_MAY_NOW_ENTER_THE_SEPULCHER(1000500),
	// Text: If you place your hand on the stone statue in front of each sepulcher,
	// you will be able to enter.
	IF_YOU_PLACE_YOUR_HAND_ON_THE_STONE_STATUE_IN_FRONT_OF_EACH_SEPULCHER_YOU_WILL_BE_ABLE_TO_ENTER(1000501),
	// Text: Thank you for saving me.
	THANK_YOU_FOR_SAVING_ME(1000503),
	// Text: Help me!!
	HELP_ME(1010484),
	// Text: Drive Out Monsters around the Village
	DRIVE_OUT_MONSTERS_AROUND_THE_VILLAGE(103511),
	// Text: Talk to Grocer Vollodos
	TALK_TO_GROCER_VOLLODOS(103512),
	// Text: Hunt Wolves and Keltirs
	HUNT_WOLVES_AND_KELTIRS(103911),
	// Text: Talk to Priest of the Earth Gerald
	TALK_TO_PRIEST_OF_THE_EARTH_GERALD(103912),
	// Text: Clear Hills of Hope
	CLEAR_HILLS_OF_HOPE(596110),
	// Text: Talk to Leahen
	TALK_TO_LEAHEN(596111),
	// Text: Expel Grave Robber
	EXPEL_GRAVE_ROBBER(103711),
	// Text: Talk to Accessory Merchant Uska
	TALK_TO_ACCESSORY_MERCHANT_USKA(103712),
	// Text: Hunt Orcs and Goblin Goblins
	HUNT_ORCS_AND_GOBLIN_GOBLINS(103311),
	// Text: Talk to Grocer Herbiel
	TALK_TO_GROCER_HERBIEL(103312),
	// Text: Hunt Wolves and Keltirs
	HUNT_WOLVES_AND_KELTIRS_2(103111),
	// Text: Talk to Armor Merchant Jackson
	TALK_TO_ARMOR_MERCHANT_JACKSON(103112),
	// Text: Stop the Maraku Werewolf's Plan
	STOP_THE_MARAKU_WEREWOLFS_PLAN(103811),
	// Text: Hunt Giant Spider
	HUNT_GIANT_SPIDER(103211),
	// Text: Talk to Captain Bathis
	TALK_TO_CAPTAIN_BATHIS(103212),
	// Text: Clear Hills of Gold
	CLEAR_HILLS_OF_GOLD(596210),
	// Text: Talk to Captain Bathis
	TALK_TO_CAPTAIN_BATHIS_2(596211),
	// Text: Hunt Zombie and Dark Horror
	HUNT_ZOMBIE_AND_DARK_HORROR(103611),
	// Text: Kill monsters in the Ruins of Agony
	KILL_MONSTERS_IN_THE_RUINS_OF_AGONY(529006),
	// Text: Clear Abandoned Camp
	CLEAR_ABANDONED_CAMP(529105),
	// Text: Clear Gorgon Flower Garden
	CLEAR_GORGON_FLOWER_GARDEN(529205),
	// Text: Defeat monsters in Ant Nest
	DEFEAT_MONSTERS_IN_ANT_NEST(596305),
	// Text: Clear Death Pass
	CLEAR_DEATH_PASS(529305),
	// Text: Orven's Request
	ORVENS_REQUEST(104505),
	// Text: Clear Training Grounds I
	CLEAR_TRAINING_GROUNDS_I(595805),
	// Text: Talk to Mattehoren
	TALK_TO_MATTEHOREN(595810),
	// Text: Clear Training Grounds II
	CLEAR_TRAINING_GROUNDS_II(595905),
	// Text: Only characters of Lv. 85 and above can move to Hellbound.
	ONLY_CHARACTERS_OF_LV_85_AND_ABOVE_CAN_MOVE_TO_HELLBOUND(1804091),
	// Text: The gates to Hellbound are open.\nThe teleport to Hellbound is
	// available from the Ivory Tower.
	THE_GATES_TO_HELLBOUND_ARE_OPEN_THE_TELEPORT_TO_HELLBOUND_IS_AVAILABLE_FROM_THE_IVORY_TOWER(1804094),
	// Text: We are looking for Lv. 85 and higher warriors for the Hellbound
	// expedition.
	WE_ARE_LOOKING_FOR_LV_85_AND_HIGHER_WARRIORS_FOR_THE_HELLBOUND_EXPEDITION(1804097),
	// Text: Kill monsters in the Quiet Plain
	KILL_MONSTERS_IN_THE_QUIET_PLAIN(595505),
	// Text: The training in over.\nUse a Scroll of Escape in your inventory to go
	// back to Master Kerkir.
	THE_TRAINING_IN_OVER_USE_A_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_GO_BACK_TO_MASTER_KERKIR(595511),
	// Text: Kill monsters in the Whispering Woods
	KILL_MONSTERS_IN_THE_WHISPERING_WOODS(595605),
	// Text: The training in over.\nUse a Scroll of Escape in your inventory to go
	// back to Grocer Evia.
	THE_TRAINING_IS_OVER_USE_A_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_GO_BACK_TO_GROCER_EVIA(595611),
	// Text: Check your inventory and equip your weapon.
	CHECK_YOUR_INVENTORY_AND_EQUIP_YOUR_WEAPON(1803968),
	// Text: Before you go for a battle, check the skill window (Alt+K). New skills
	// will help you to get stronger.
	BEFORE_YOU_GO_FOR_A_BATTLE_CHECK_THE_SKILL_WINDOW_NEW_SKILLS_WILL_HELP_YOU_TO_GET_STRONGER(1803969),
	// Text: You've made the first steps on the adventurer's path.\nReturn to Bathis
	// to get your reward.
	YOUVE_MADE_THE_FIRST_STEPS_ON_THE_ADVENTURERS_PATH_RETURN_TO_BATHIS_TO_GET_YOUR_REWARD(1803755),
	// Text: You've killed all the monsters.\nUse the Scroll of Escape in your
	// inventory to return to Trader Reahen.
	YOUVE_KILLED_ALL_THE_MONSTERS_USE_THE_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_RETURN_TO_TRADER_REAHEN(1803778),
	// Text: You've killed all the monsters.\nUse the Scroll of Escape in your
	// inventory to return to Captain Bathis in Gludio.
	YOUVE_KILLED_ALL_THE_MONSTERS_USE_THE_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_RETURN_TO_CAPTAIN_BATHIS_IN_GLUDIO(
																													1803735),
	// Text: You've killed all the monsters.\nUse the Scroll of Escape in your
	// inventory to return to Armor Merchant Jackson.
	YOUVE_KILLED_ALL_THE_MONSTERS_USE_THE_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_RETURN_TO_ARMOR_MERCHANT_JACKSON(
																													1803730),
	// Text: You've killed all the monsters.\nUse the Scroll of Escape in your
	// inventory to return to Grocer Herbiel.
	YOUVE_KILLED_ALL_THE_MONSTERS_USE_THE_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_RETURN_TO_GROCER_HERBIEL(1803731),
	// Text: You've killed all the monsters.\nUse the Scroll of Escape in your
	// inventory to return to Grocer Vollodos.
	YOUVE_KILLED_ALL_THE_MONSTERS_USE_THE_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_RETURN_TO_GROCER_VOLLODOS(1803732),
	// Text: You've killed all the monsters.\nUse the Scroll of Escape in your
	// inventory to return to Accessory Merchant Uska.
	YOUVE_KILLED_ALL_THE_MONSTERS_USE_THE_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_RETURN_TO_ACCESSORY_MERCHANT_USKA(
																													1803733),
	// Text: You've killed all the monsters.\nUse the Scroll of Escape in your
	// inventory to return to Head Priest of the Earth Gerald.
	YOUVE_KILLED_ALL_THE_MONSTERS_USE_THE_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_RETURN_TO_HEAD_PRIEST_OF_THE_EARTH_GERALD(
																															1803734),
	// Text: Speak to Head Trainer Kilremange
	SPEAK_TO_HEAD_TRAINER_KILREMANGE(1803994),
	// Text: The training in over.\nUse a Scroll of Escape in your inventory to go
	// back to Quartermaster Mathorn.
	THE_TRAINING_IN_OVER_USE_A_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_GO_BACK_TO_QUARTERMASTER_MATHORN(595811),
	// Text: You've finished the tutorial.\nTake your first class transfer and
	// complete your training with Mathorn to become stronger.
	YOUVE_FINISHED_THE_TUTORIAL_TAKE_YOUR_FIRST_CLASS_TRANSFER_AND_COMPLETE_YOUR_TRAINING_WITH_MATHORN_TO_BECOME_STRONGER(
																															595911),
	// Text: You've got Adventurer's Bracelet and Adventurer's Talisman.\nComplete
	// the tutorial and try to use the talisman.
	YOUVE_GOT_ADVENTURERS_BRACELET_AND_ADVENTURERS_TALISMAN_COMPLETE_THE_TUTORIAL_AND_TRY_TO_USE_THE_TALISMAN(1803785),
	// Text: You've got Adventurer's Brooch and Adventurer's Rough Jewel.\nComplete
	// the tutorial and try to enchase the jewel.
	YOUVE_GOT_ADVENTURERS_BROOCH_AND_ADVENTURERS_ROUGH_JEWEL_COMPLETE_THE_TUTORIAL_AND_TRY_TO_USE_THE_JEWEL(1803786),
	// Text: Monsters of the Gorgon Flower Garden are killed.\nUse the teleport to
	// get to High Priest Raymond in Gludio.
	MONSTERS_OF_THE_GORGON_FLOWER_GARDEN_ARE_KILLED_USE_THE_TELEPORT_TO_GET_TO_HIGH_PRIEST_RAYMOND_IN_GLUDIO(596411),
	// Text: You've got Adventurer's Agathion Bracelet and Adventurer's Agathion
	// Griffin.\nComplete the tutorial and try to use the agathion.
	YOUVE_GOT_ADVENTURERS_AGATHION_BRACELET_AND_ADVENTURERS_AGATHION_GRIFFIN_COMPLETE_THE_TUTORIAL_AND_TRY_TO_USE_THE_AGATHION(
																																1803787),
	// Text: Monsters of the Death Pass are killed.\nUse the teleport or the Scroll
	// of Escape to get to High Priest Maximilian in Giran.
	MONSTERS_OF_THE_DEATH_PASS_ARE_KILLED_USE_THE_TELEPORT_OR_THE_SCROLL_OF_ESCAPE_TO_GET_TO_HIGH_PRIEST_MAXIMILIAN_IN_GIRAN(
																																596511),
	// Text: The element is active. You can open the character info window to
	// navigate your spirit.
	THE_ELEMENT_IS_ACTIVE_YOU_CAN_OPEN_THE_CHARACTER_INFO_WINDOW_TO_NAVIGATE_YOUR_SPIRIT(1803794),
	// Text: Kill monsters in the Sea of Spores
	KILL_MONSTERS_IN_THE_SEA_OF_SPORES(529405),
	// Text: All missions are completed.\nUse Scroll of Escape: High Priest Orven to
	// get to High Priest Orven in Aden.
	ALL_MISSIONS_ARE_COMPLETED_USE_SCROLL_OF_ESCAPE_HIGH_PRIEST_ORVEN_TO_GET_TO_HIGH_PRIEST_ORVEN_IN_ADEN(529411),
	// Text: Kill monsters in a Transcendent Instance Zone
	KILL_MONSTERS_IN_A_TRANSCENDENT_INSTANCE_ZONE(529905),
	// Text: Find High Priest Orven after you come out a Transcendent Instance Zone.
	FIND_HIGH_PRIEST_ORVEN_AFTER_YOU_COME_OUT_A_TRANSCENDENT_INSTANCE_ZONE(529911),
	// Text: Kill monsters in the Cemetery
	KILL_MONSTERS_IN_THE_CEMETERY(529505),
	// Text: From now try to get as much quests as you can. I'll tell you what to do
	// next.
	FROM_NOW_TRY_TO_GET_AS_MUCH_QUESTS_AS_YOU_CAN_ILL_TELL_YOU_WHAT_TO_DO_NEXT(1803970),
	// Text : Kill monsters in the Fields of Massacre
	KILL_MONSTERS_IN_THE_FIELDS_OF_MASSACRE(529605),
	// Text: Kill monsters on the Plains of Glory
	KILL_MONSTERS_ON_THE_PLAINS_OF_GLORY(529705),
	// Text: Kill monsters on the War-Torn Plains
	KILL_MONSTERS_ON_THE_WAR_TORN_PLAINS(529805),
	// Text: Defeat monsters in the Cruma Tower
	DEFEAT_MONSTERS_IN_THE_CRUMA_TOWER(530010),
	// Text: Defeat monsters in the Silent Valley
	DEFEAT_MONSTERS_IN_THE_SILENT_VALLEY(530110),
	// Text: Eradicate monsters in the Plains of the Lizardmen
	ERADICATE_MONSTERS_IN_THE_PLAINS_OF_THE_LIZARDMEN(530210),
	// Text: Defeat monsters in the Tower of Insolence
	DEFEAT_MONSTERS_IN_THE_TOWER_OF_INSOLENCE(530310),
	// Text: Defeat monsters in the Dragon Valley (west)
	DEFEAT_MONSTERS_IN_THE_DRAGON_VALLEY_WEST(530410),
	// Text: Defeat monsters in the Dragon Valley (east)
	DEFEAT_MONSTERS_IN_THE_DRAGON_VALLEY_EAST(530510),
	// Text: Defeat monsters in Sel Mahum Base
	DEFEAT_MONSTERS_IN_SEL_MAHUM_BASE(530610),
	// Text: Defeat monsters in the Orc Barracks
	DEFEAT_MONSTERS_IN_THE_ORC_BARRACKS(530710),
	// Text: Gladiator: Transcendent Skills |Double/ Triple Sonic Slash|
	GLADIATOR_TRANSCENDENT_SKILLS_DOUBLE_TRIPLE_SONIC_SLASH(1803808),
	// Text: Warlord: Transcendent Skill |Fatal Strike|
	WARLORD_TRANSCENDENT_SKILL_FATAL_STRIKE(1803809),
	// Text: Paladin: Transcendent Skill |Shield Strike|
	PALADIN_TRANSCENDENT_SKILL_SHIELD_STRIKE(1803810),
	// Text: Dark Avenger: Transcendent Skill |Shield Strike|
	DARK_AVENGER_TRANSCENDENT_SKILL_SHIELD_STRIKE(1803811),
	// Text: Treasure Hunter: Transcendent Skill |Deadly Blow|
	TREASURE_HUNTER_TRANSCENDENT_SKILL_DEADLY_BLOW(1803812),
	// Text: Archer: Transcendent Skill |Double Shot|
	HAWKEYE_TRANSCENDENT_SKILL_DOUBLE_SHOT(1803813),
	// Text: Sorcerer: Transcendent Skill |Prominence|
	SORCERER_TRANSCENDENT_SKILL_PROMINENCE(1803814),
	// Text: Necromancer: Transcendent Skill |Death Spike|
	NECROMANCER_TRANSCENDENT_SKILL_DEATH_SPIKE(1803815),
	// Text: Warlock: Transcendent Skill |Blaze|
	WARLOCK_TRANSCENDENT_SKILL_BLAZE(1803816),
	// Text: Bishop: Transcendent Skill |Might of Heaven|
	BISHOP_TRANSCENDENT_SKILL_MIGHT_OF_HEAVEN(1803817),
	// Text: Prophet: Transcendent Skill |Might of Heaven|
	PROPHET_TRANSCENDENT_SKILL_MIGHT_OF_HEAVEN(1803818),
	// Text: Temple Knight: Transcendent Skill |Tribunal|
	TEMPLE_KNIGHT_TRANSCENDENT_SKILL_TRIBUNAL(1803819),
	// Text: Swordsinger: Transcendent Skill |Guard Crush|
	SWORDSINGER_TRANSCENDENT_SKILL_GUARD_CRUSH(1803820),
	// Text: Plains Walker: Transcendent Skill |Deadly Blow|
	PLAINS_WALKER_TRANSCENDENT_SKILL_DEADLY_BLOW(1803821),
	// Text: Silver Ranger: Transcendent Skill |Double Shot|
	SILVER_RANGER_TRANSCENDENT_SKILL_DOUBLE_SHOT(1803822),
	// Text: Spellsinger: Transcendent Skill |Hydro Blast|
	SPELLSINGER_TRANSCENDENT_SKILL_HYDRO_BLAST(1803823),
	// Text: Elemental Summoner: Transcendent Skill |Aqua Swirl|
	ELEMENTAL_SUMMONER_TRANSCENDENT_SKILL_AQUA_SWIRL(1803824),
	// Text: Elven Elder: Transcendent Skill |Might of Heaven|
	ELVEN_ELDER_TRANSCENDENT_SKILL_MIGHT_OF_HEAVEN(1803825),
	// Text: Shillien Knight: Transcendent Skill |Judgment|
	SHILLIEN_KNIGHT_TRANSCENDENT_SKILL_JUDGMENT(1803826),
	// Text: Bladedancer: Transcendent Skill |Guard Crush|
	BLADEDANCER_TRANSCENDENT_SKILL_GUARD_CRUSH(1803827),
	// Text: Abyss Walker: Transcendent Skill |Deadly Blow|
	ABYSS_WALKER_TRANSCENDENT_SKILL_DEADLY_BLOW(1803828),
	// Text: Phantom Ranger: Transcendent Skill |Double Shot|
	PHANTOM_RANGER_TRANSCENDENT_SKILL_DOUBLE_SHOT(1803829),
	// Text: Spellhowler: Transcendent Skill |Hurricane|
	SPELLHOWLER_TRANSCENDENT_SKILL_HURRICANE(1803830),
	// Text: Phantom Summoner: Transcendent Skill |Twister|
	PHANTOM_SUMMONER_TRANSCENDENT_SKILL_TWISTER(1803831),
	// Text: Shillien Elder: Transcendent Skill |Might of Heaven|
	SHILLIEN_ELDER_TRANSCENDENT_SKILL_MIGHT_OF_HEAVEN(1803832),
	// Text: Destroyer: Transcendent Skill |Fatal Strike|
	DESTROYER_TRANSCENDENT_SKILL_FATAL_STRIKE(1803833),
	// Text: Hermit: Transcendent Skill |Hurricane Assault|
	TYRANT_TRANSCENDENT_SKILL_HURRICANE_ASSAULT(1803834),
	// Text: Destroyer: Transcendent Skill |Steal Essence|
	OVERLORD_TRANSCENDENT_SKILL_STEAL_ESSENCE(1803835),
	// Text: Warcryer: Transcendent Skill |Steal Essence|
	WARCRYER_TRANSCENDENT_SKILL_STEAL_ESSENCE(1803836),
	// Text: Bounty Hunter: Transcendent Skill |Fatal Strike|
	BOUNTY_HUNTER_TRANSCENDENT_SKILL_FATAL_STRIKE(1803837),
	// Text: Blacksmith: Transcendent Skill |Fatal Strike|
	WARSMITH_TRANSCENDENT_SKILL_FATAL_STRIKE(1803838),
	// Text: Berserker: Transcendent Skill |Soul Impulse|
	BERSERKER_TRANSCENDENT_SKILL_SOUL_IMPULSE(1803839),
	// Text: Soul Ranger: Transcendent Skill |Twin Shot|
	SOUL_RANGER_TRANSCENDENT_SKILL_TWIN_SHOT(1803840),
	// Text: Soul Breaker: Transcendent Skill |Soul Piercing|
	SOUL_BREAKER_TRANSCENDENT_SKILL_SOUL_PIERCING(1803841),
	// Text: Duelist: Transcendent Skills |Double/ Triple Sonic Slash|
	DUELIST_TRANSCENDENT_SKILLS_DOUBLE_TRIPLE_SONIC_SLASH(1803842),
	// Text: Dreadnought: Transcendent Skill |Spinning Slasher|
	DREADNOUGHT_TRANSCENDENT_SKILL_SPINNING_SLASHER(1803843),
	// Text: Phoenix Knight: Transcendent Skill |Shield Strike|
	PHOENIX_KNIGHT_TRANSCENDENT_SKILL_SHIELD_STRIKE(1803844),
	// Text: Hell Knight: Transcendent Skill |Shield Strike|
	HELL_KNIGHT_TRANSCENDENT_SKILL_SHIELD_STRIKE(1803845),
	// Text: Adventurer: Transcendent Skill |Lethal Blow|
	ADVENTURER_TRANSCENDENT_SKILL_LETHAL_BLOW(1803846),
	// Text: Sagittarius: Transcendent Skill |Lethal Shot|
	SAGITTARIUS_TRANSCENDENT_SKILL_LETHAL_SHOT(1803847),
	// Text: Archmage: Transcendent Skill |Prominence|
	ARCHMAGE_TRANSCENDENT_SKILL_PROMINENCE(1803848),
	// Text: Soultaker: Transcendent Skill |Death Spike|
	SOULTAKER_TRANSCENDENT_SKILL_DEATH_SPIKE(1803849),
	// Text: Arcana Lord: Transcendent Skill |Blaze|
	ARCANA_LORD_TRANSCENDENT_SKILL_BLAZE(1803850),
	// Text: Cardinal: Transcendent Skill |Divine Beam|
	CARDINAL_TRANSCENDENT_SKILL_DIVINE_BEAM(1803851),
	// Text: Apostle: Transcendent Skill |Divine Beam|
	HIEROPHANT_TRANSCENDENT_SKILL_DIVINE_BEAM(1803852),
	// Text: Eva's Templar: Transcendent Skill |Tribunal|
	EVAS_TEMPLAR_TRANSCENDENT_SKILL_TRIBUNAL(1803853),
	// Text: Bladedancer: Transcendent Skill |Deadly Strike|
	SWORD_MUSE_TRANSCENDENT_SKILL_DEADLY_STRIKE(1803854),
	// Text: Wind Rider: Transcendent Skill |Lethal Blow|
	WIND_RIDER_TRANSCENDENT_SKILL_LETHAL_BLOW(1803855),
	// Text: Moonlight Sentinel: Transcendent Skill |Lethal Shot|
	MOONLIGHT_SENTINEL_TRANSCENDENT_SKILL_LETHAL_SHOT(1803856),
	// Text: Mystic Muse: Transcendent Skill |Hydro Blast|
	MYSTIC_MUSE_TRANSCENDENT_SKILL_HYDRO_BLAST(1803857),
	// Text: Elemental Master: Transcendent Skill |Aqua Swirl|
	ELEMENTAL_MASTER_TRANSCENDENT_SKILL_AQUA_SWIRL(1803858),
	// Text: Eva's Saint: Transcendent Skill |Divine Beam|
	EVAS_SAINT_TRANSCENDENT_SKILL_DIVINE_BEAM(1803859),
	// Text: Shillien Templar: Transcendent Skill |Judgment|
	SHILLIEN_TEMPLAR_TRANSCENDENT_SKILL_JUDGMENT(1803860),
	// Text: Spectral Dancer: Transcendent Skill |Deadly Strike|
	SPECTRAL_DANCER_TRANSCENDENT_SKILL_DEADLY_STRIKE(1803861),
	// Text: Ghost Hunter: Transcendent Skill |Lethal Blow|
	GHOST_HUNTER_TRANSCENDENT_SKILL_LETHAL_BLOW(1803862),
	// Text: Ghost Sentinel: Transcendent Skill |Lethal Shot|
	GHOST_SENTINEL_TRANSCENDENT_SKILL_LETHAL_SHOT(1803863),
	// Text: Storm Screamer: Transcendent Skill |Hurricane|
	STORM_SCREAMER_TRANSCENDENT_SKILL_HURRICANE(1803864),
	// Text: Spectral Master: Transcendent Skill |Twister|
	SPECTRAL_MASTER_TRANSCENDENT_SKILL_TWISTER(1803865),
	// Text: Shillien's Saint: Transcendent Skill |Divine Beam|
	SHILLIENS_SAINT_TRANSCENDENT_SKILL_DIVINE_BEAM(1803866),
	// Text: Titan: Transcendent Skill |Demolition Impact|
	TITAN_TRANSCENDENT_SKILL_DEMOLITION_IMPACT(1803867),
	// Text: Grand Khavatari: Transcendent Skill |Hurricane Assault|
	GRAND_KHAVATARI_TRANSCENDENT_SKILL_HURRICANE_ASSAULT(1803868),
	// Text: Dominator: Transcendent Skill |Steal Essence|
	DOMINATOR_TRANSCENDENT_SKILL_STEAL_ESSENCE(1803869),
	// Text: Doomcryer: Transcendent Skill |Steal Essence|
	DOOMCRYER_TRANSCENDENT_SKILL_STEAL_ESSENCE(1803870),
	// Text: Fortune Seeker: Transcendent Skill |Spoil Crush|
	FORTUNE_SEEKER_TRANSCENDENT_SKILL_SPOIL_CRUSH(1803871),
	// Text: Master: Transcendent Skill |Earth Tremor|
	MAESTRO_TRANSCENDENT_SKILL_EARTH_TREMOR(1803872),
	// Text: Doombringer: Transcendent Skill |Soul Impulse|
	DOOMBRINGER_TRANSCENDENT_SKILL_SOUL_IMPULSE(1803873),
	// Text: Trickster: Transcendent Skill |Twin Shot|
	TRICKSTER_TRANSCENDENT_SKILL_TWIN_SHOT(1803874),
	// Text: Soul Hound: Transcendent Skill |Soul Piercing|
	SOUL_HOUND_TRANSCENDENT_SKILL_SOUL_PIERCING(1803875),
	// Text: Death Messenger: Transcendent Skill |Wipeout|
	DEATH_MESSENGER_TRANSCENDENT_SKILL_WIPEOUT(1804014),
	// Text: Death Knight: Transcendent Skill |Wipeout|
	DEATH_KNIGHT_TRANSCENDENT_SKILL_WIPEOUT(1804015),
	// Text: Wind Sniper: Transcendent Skill |Freezing Wound|
	WIND_SNIPER_TRANSCENDENT_SKILL_FREEZING_WOUND(1804147),
	// Text: Storm Blaster: Transcendent Skill |Freezing Wound|
	STORM_BLASTER_TRANSCENDENT_SKILL_FREEZING_WOUND(1804148),
	// Text: Dragoon: Transcendent Skills |Piercing| |Wild Scratch|
	DRAGOON_TRANSCENDENT_SKILLS_PIERCING_WILD_SCRATCH(1804507),
	// Text: Vanguard Rider: Transcendent Skills |Amazing Piercing| |Shadow Scratch|
	VANGUARD_RIDER_TRANSCENDENT_SKILLS_AMAZING_PIERCING_SHADOW_SCRATCH(1804508),
	// Text: Sea of Spores Guard appears
	SEA_OF_SPORES_GUARD_APPEARS(1804187),
	// Text: Graveyard Guard appears
	GRAVEYARD_GUARD_APPEARS(1804188),
	// Text: Plains of Glory Guard appears
	PLAINS_OF_GLORY_GUARD_APPEARS(1804189),
	// Text: War-Torn Plains Guard appears
	WAR_TORN_PLAINS_GUARD_APPEARS(1804190),
	// Text: Dragon Valley Guard appears
	DRAGON_VALLEY_GUARD_APPEARS(1804191),
	// Text: All Cruma Tower exploration support missions are finished.\nTalk to
	// Ivory Tower Wizard Carsus near the Cruma Tower entrance or to High Priest
	// Orven in Aden town.
	ALL_CRUMA_TOWER_EXPLORATION_SUPPORT_MISSIONS_ARE_FINISHED_TALK_TO_IVORY_TOWER_WIZARD_CARSUS_NEAR_THE_CRUMA_TOWER_ENTRANCE_OR_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN(
																																									530011),
	// Text: Monsters in the Silent Valley have been defeated.\nTalk to High Priest
	// Orven in Aden town.
	MONSTERS_IN_THE_SILENT_VALLEY_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN(530111),
	// Text: Monsters in the Plains of the Lizardmen have been defeated.\nTalk to
	// High Priest Orven in Aden town.
	MONSTERS_IN_THE_PLAINS_OF_THE_LIZARDMEN_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN(530211),
	// Text: Monsters in the Tower of Insolence have been defeated.\nTalk to High
	// Priest Orven in Aden town.
	MONSTERS_IN_THE_TOWER_OF_INSOLENCE_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN(530311),
	// Text: Monsters in the Dragon Valley (west) have been defeated.\nTalk to High
	// Priest Orven in Aden town.
	MONSTERS_IN_THE_DRAGON_VALLEY_WEST_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN(530411),
	// Text: Monsters in the Dragon Valley (east) have been defeated.\nTalk to High
	// Priest Orven in Aden town.
	MONSTERS_IN_THE_DRAGON_VALLEY_EAST_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN(530511),
	// Text: Monsters in the Sel Mahum Base have been defeated.\nTalk to High Priest
	// Orven in Aden town.
	MONSTERS_IN_THE_SEL_MAHUM_BASE_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN(530611),
	// Text: Monsters in the Orc Barracks have been defeated.\nTalk to High Priest
	// Orven in Aden town.
	MONSTERS_IN_THE_ORC_BARRACKS_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN(530711),
	// Text: Talk to Flame Guardian Vulkus.
	TALK_TO_FLAME_GUARDIAN_VULKUS(595012),
	// Text: Talk to First Vanguard Rider Sabitus.
	TALK_TO_FIRST_VANGUARD_RIDER_SABITUS(595112),
	// Text: Subjugation in the Valley of Heroes
	SUBJUGATION_IN_THE_VALLEY_OF_HEROES(595205),
	// Text: The training in over.\nUse a Scroll of Escape in your inventory to go
	// back to Tanai.
	THE_TRAINING_IN_OVER_USE_A_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_GO_BACK_TO_TANAI(595211),
	// Text: Subjugation in the Northern Area of the Immortal Plateau
	SUBJUGATION_IN_THE_NORTHERN_AREA_OF_THE_IMMORTAL_PLATEAU(595305),
	// Text: The training in over.\nUse a Scroll of Escape in your inventory to go
	// back to Gantaki Zu Urutu.
	THE_TRAINING_IN_OVER_USE_A_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_GO_BACK_TO_GANTAKI_ZU_URUTU(595311),
	// Text: You've finished the tutorial.\nTake your 1st class transfer and
	// complete your training with Bathis to become stronger.
	YOUVE_FINISHED_THE_TUTORIAL_TAKE_YOUR_1ST_CLASS_TRANSFER_AND_COMPLETE_YOUR_TRAINING_WITH_BATHIS_TO_BECOME_STRONGER(
																														595312);

	private final int _id;
	private final int _size;

	NpcString(int id)
	{
		_id = id;

		if (name().contains("S4"))
			_size = 4;
		else if (name().contains("S3"))
			_size = 3;
		else if (name().contains("S2"))
			_size = 2;
		else if (name().contains("S1"))
			_size = 1;
		else
			_size = 0;
	}

	public int getId()
	{
		return _id;
	}

	public int getSize()
	{
		return _size;
	}

	public static NpcString valueOf(int id)
	{
		for (NpcString m : values())
			if (m.getId() == id)
				return m;

		throw new NoSuchElementException("Not find NpcString by id: " + id);
	}
}
