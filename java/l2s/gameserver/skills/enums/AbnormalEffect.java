package l2s.gameserver.skills.enums;

/**
 * @author Bonux,nexvill
 * @rework 311-362 MORSE
 **/
public enum AbnormalEffect
{
	// Normal Abnormal Effects
	/* 0 */NONE(0), //
	/* 1 */DOT_BLEEDING(1),
	/* 2 */DOT_POISON(2),
	/* 3 */DOT_FIRE(3), // Мигает красный круг вокруг живота персонажа.
	/* 4 */DOT_WATER(4), // Висит и мигает кусок для на животе персонажа.
	/* 5 */DOT_WIND(5), //
	/* 6 */DOT_SOIL(6), // Из персонажа появляется куча бардового дыма и зеленькие кружочки.
	/* 7 */STUN(7), //
	/* 8 */SLEEP(8), //
	/* 9 */SILENCE(9), //
	/* 10 */ROOT(10), //
	/* 11 */PARALYZE(11), //
	/* 12 */FLESH_STONE(12), //
	/* 13 */DOT_MP(13), // unk
	/* 14 */BIG_HEAD(14), //
	/* 15 */DOT_FIRE_AREA(15), // Пламя огня начиная с ног персонажа.
	/* 16 */CHANGE_TEXTURE(16), //
	/* 17 */BIG_BODY(17), //
	/* 18 */FLOATING_ROOT(18), //
	/* 19 */DANCE_ROOT(19), //
	/* 20 */GHOST_STUN(20), // Звездочки как у стана и на ногах красный круг.
	/* 21 */STEALTH(21), //
	/* 22 */SEIZURE1(22), // Вокруг живота синий туман с электричеством.
	/* 23 */SEIZURE2(23), // Вокруг живота синий туман с электричеством.
	/* 24 */MAGIC_SQUARE(24), //
	/* 25 */FREEZING(25), // Висит и мигает кусок для на животе персонажа.
	/* 26 */SHAKE(26), // Землетрясение.
	/* 27 */UNK_27(27), // unk
	/* 28 */ULTIMATE_DEFENCE(28), //
	/* 29 */VP_UP(29), //
	/* 30 */REAL_TARGET(30), //
	/* 31 */DEATH_MARK(31), //
	/* 32 */TURN_FLEE(32), // Синяя морда черепа над головой.
	/* 33 */INVINCIBILITY(33), //
	/* 34 */AIR_BATTLE_SLOW(34), // Мигает красный туманный шар вокруг живота персонажа.
	/* 35 */AIR_BATTLE_ROOT(35), // Мигает красный туманный шар вокруг живота персонажа.
	/* 36 */CHANGE_WP(36), // Багет вместо оружия.
	/* 37 */CHANGE_HAIR_G(37), // Золотая афро-прическа.
	/* 38 */CHANGE_HAIR_P(38), // Розовая афро-прическа.
	/* 39 */CHANGE_HAIR_B(39), // Черная афро-прическа.
	/* 40 */UNK_40(40), // unk
	/* 41 */STIGMA_OF_SILEN(41), //
	/* 42 */SPEED_DOWN(42), // Зеленые и белые линии запутуются в ногах.
	/* 43 */FROZEN_PILLAR(43), //
	/* 44 */CHANGE_VES_S(44), // Переодевает персонажа в золотой веспер.
	/* 45 */CHANGE_VES_C(45), // Переодевает персонажа в золотой веспер.
	/* 46 */CHANGE_VES_D(46), // Переодевает персонажа в белый веспер.
	/* 47 */TIME_BOMB(47), // Зеленый круговой дождь в тумане над головой персонажа.
	/* 48 */MP_SHIELD(48), // Критует клиент хD
	/* 49 */AIRBIND(49), // Поднимает в верх и держит в красном шаре персонажа.
	/* 50 */CHANGEBODY(50), // Переодевает персонажа в Д грейд.
	/* 51 */KNOCKDOWN(51), //
	/* 52 */NAVIT_ADVENT(52), // unk
	/* 53 */KNOCKBACK(53), //
	/* 54 */CHANGE_7ANNIVERSARY(54), //
	/* 55 */ON_SPOT_MOVEMENT(55), // unk
	/* 56 */DEPORT(56), // Закрывает персонажа в черной туманной клетке.
	/* 57 */AURA_BUFF(57), // С персонажа идет зеленый дым и сверкает живот.
	/* 58 */AURA_BUFF_SELF(58), // С персонажа идет бело-желтый дым, вокруг живота идут вспышки кольцами
	// градиентного цвета и вокруг живота летают белые кружочки. Также персонаж
	// стоит на красном круге.
	/* 59 */AURA_DEBUFF(59), // С персонажа мигает красный дым и сверкает живот красными полосами.
	/* 60 */AURA_DEBUFF_SELF(60), // Персонажа окутывают красные спиральные полосы и мигает не сильно красный дым.
	// (При активации происходит красная вспышка)
	/* 61 */HURRICANE(61), // (При активации происходит бело-желтая вспышка)
	/* 62 */HURRICANE_SELF(62), // Вокруг персонажа песчаная буря.
	/* 63 */BLACK_MARK(63), // Над головой персонажа морда красного черепа.
	/* 64 */BR_SOUL_AVATAR(64), //
	/* 65 */CHANGE_GRADE_B(65), // Переодевает персонажа в Б грейд.
	/* 66 */BR_BEAM_SWORD_ONEHAND(66), //
	/* 67 */BR_BEAM_SWORD_DUAL(67), //
	/* 68 */D_NOCHAT(68), //
	/* 69 */D_HERB_POWER(69), //
	/* 70 */D_HERB_MAGIC(70), //
	/* 71 */D_TALI_DECO_P(71), //
	/* 72 */D_TALI_DECO_B(72), //
	/* 73 */D_TALI_DECO_C(73), // Рука светится желтым.
	/* 74 */D_TALI_DECO_D(74), // Рука светится красным.
	/* 75 */D_TALI_DECO_E(75), // Рука светится синим.
	/* 76 */D_TALI_DECO_F(76), // Рука светится Фиолетовым.
	/* 77 */D_TALI_DECO_G(77), // Переодевает в Топ S80, одевает плащ и диадему.
	/* 78 */D_CHANGESHAPE_TRANSFORM_1(78), // Переодевает в NG.
	/* 79 */D_CHANGESHAPE_TRANSFORM_2(79), // Переодевает в D.
	/* 80 */D_CHANGESHAPE_TRANSFORM_3(80), // Переодевает в C.
	/* 81 */D_CHANGESHAPE_TRANSFORM_4(81), // Переодевает в B.
	/* 82 */D_CHANGESHAPE_TRANSFORM_5(82), // Переодевает в A.
	/* 83 */SWEET_ICE_FLAKES(83), // У артеас изчезает туловище, на остальных не проверялось.
	/* 84 */FANTASY_ICE_FLAKES(84), // У артеас изчезает туловище, на остальных не проверялось.
	/* 85 */SANTA_SUIT(85), // Переодевает в костюм деда мороза.
	/* 86 */CARD_PC_DECO(86), // Возле персонажа летает игральная карта.
	/* 87 */CHANGE_DINOS(87), // Переодевает в бейсбольную форму.
	/* 88 */CHANGE_VALENTINE(88), // Оружие персонажа превращается в свадебный букет.
	/* 89 */CHOCOLATE(89), // Возле персонажа летает леденец.
	/* 90 */CANDY(90), // Возле персонажа летает конфетка.
	/* 91 */COOKIE(91), // Возле персонажа летает печенька.
	/* 92 */EMPTY_STARS(92), // Над персонажом засвечивается 5 пустых звездочек.
	/* 93 */ONE_STAR(93), // Над персонажом засвечивается 1я звездочка.
	/* 94 */TWO_STARS(94), // Над персонажом засвечивается 2я звездочка.
	/* 95 */THREE_STARS(95), // Над персонажом засвечивается 3я звездочка.
	/* 96 */FOUR_STARS(96), // Над персонажом засвечивается 4я звездочка.
	/* 97 */FIVE_STARS(97), // Над персонажом засвечивается 5я звездочка.
	/* 98 */FACEOFF(98), // Песронаж стоит в бардовом круге и над головой мигают скрещенные 2 меча.
	/* 99 */FREEZING2(99), // Под персонажем земля пытается замерзнуть и выростает перед ногами небольшая
	// льдинка.
	/* 100 */CHANGE_YOGI(100), // Переодевает персонажа в бронь робокопа.
	/* 101 */YOGI(101), // Возле персонажа летает голова посоха Мастера Йоды.
	/* 102 */MUSICAL_NOTE_YELLOW(102), // Возле персонажа летает желтая нота.
	/* 103 */MUSICAL_NOTE_BLUE(103), // Возле персонажа летает синяя нота.
	/* 104 */MUSICAL_NOTE_GREEN(104), // Возле персонажа летает зеленая нота.
	/* 105 */TENTH_ANNIVERSARY(105), // Возле персонажа летает лого Lineage II.
	/* 106 */STOCKING_FAIRY(106), // Возле персонажа летает картинка носка.
	/* 107 */TREE_FAIRY(107), // Возле персонажа летает картинка елки.
	/* 108 */SNOWMAN_FAIRY(108), // Возле персонажа летает картинка снеговика.
	/* 109 */OTHELL_ROGUE_BLUFF(109), // Над головой персонажа крутятся бардовые спиральки.
	/* 110 */HE_PROTECT(110), // Вокруг персонажа желтый круг и персонаж стоит в красном круге.
	/* 111 */SU_SUMCROSS(111), // Вокруг персонажа образовывается бардовая стена с синими бликами.
	/* 112 */WIND_STUN(112), // Вокруг персонажа крутятся зеленые круги в сверичном виде.
	/* 113 */STORM_SIGN2(113), // Над головой светится красное око.
	/* 114 */STIGMA_STORM(114), // Над головой светится зеленое око.
	/* 115 */GREEN_SPEED_UP(115), // Персонаж светиться зеленым и при беге подпрыгивает.
	/* 116 */RED_SPEED_UP(116), // Персонаж светиться красным и при беге подпрыгивает.
	/* 117 */WIND_PROTECTION(117), // Персонаж светиться в зеленый и стоит с транной агрессивной стойке.
	/* 118 */LOVE(118), // Начинает летать сердце над головой.
	/* 119 */PERFECT_STORM(119), // На уровне пояса синенький маленький круговорот воздуха.
	/* 120 */WIND_ILLUSION(120), // Светиться синим и вокруг небольшой смерчь с листьями.
	/* 121 */SAYHA_FURY(121), // Темнеет персонаж с красным бликом.
	/* 122 */UNK_122(122), //
	/* 123 */GREAT_GRAVITY(123), // Темнеет персонаж с красной димной сверой в районе живота.
	/* 124 */STEEL_MIND(124), // Каждая нога окутывается зелеными полосами с звездочками и при беге
	// подпрыгивает персонаж.
	/* 124 */u_er_wa_pmental_hand_trail(124),
	/* 124 */u_er_wa_pmental_foot_trail(124),
	/* 125 */HOLD_LIGHTING(125), // Вокруг
	// персонажа
	// ниже
	// пояса
	// летает
	// черная
	// и
	// голубая
	// цепь.
	// Происходят
	// синие
	// вспышки.
	/* 126 */OBLATE(126), // Персонаж становится 2D. Сплюснутый))
	/* 127 */SPALLATION(127), // Персонаж оказывается в средине большого зеленого бликующего круга.
	/* 128 */U_HE_ASPECT_AVE(128), // С персонажа идет черный дым.
	/* 129 */RUNWAY_ARMOR1(129), // У артеас изчезает туловище, на остальных не проверялось.
	/* 130 */RUNWAY_ARMOR2(130), // У артеас изчезает туловище, на остальных не проверялось.
	/* 131 */RUNWAY_ARMOR3(131), // У артеас изчезает туловище, на остальных не проверялось.
	/* 132 */RUNWAY_ARMOR4(132), // Переодевает персонажа в бронь робокопа.
	/* 133 */RUNWAY_ARMOR5(133), // У артеас пропадает все тело, кроме головы, на остальных не проверялось.
	/* 134 */RUNWAY_ARMOR6(134), // У артеас пропадает все тело, кроме головы, на остальных не проверялось.
	/* 135 */RUNWAY_WEAPON1(135), // Оружие меняет на лазерное.
	/* 136 */RUNWAY_WEAPON2(136), // Оружие меняет на японское.
	/* 137 */UNK_137(137), //
	/* 138 */UNK_138(138), //
	/* 139 */UNK_139(139), //
	/* 140 */UNK_140(140), //
	/* 141 */U_AVE_PALADIN_DEF(141), // Вокруг персонажа крутятся светащиеся оранжевые щиты.
	/* 142 */U_AVE_GUARDIAN_DEF(142), // Вокруг персонажа крутятся светащиеся синие щиты.
	/* 143 */U_REALTAR2_AVE(143), // Над головой мигает красно-оранжевый оберег в виде тризуба.
	/* 144 */U_AVE_DIVINITY(144), // Персонажа переливается как эффект заточенной брони.
	/* 145 */U_AVE_SHILPROTECTION(145), // Вокруг персонажа образовывается красная сфера состоящая из шестиугольников.
	/* 146 */U_EVENT_STAR_CA(146), // Над персонажем мигают 5 звезд.
	/* 147 */U_EVENT_STAR1_TA(147), // Над персонажем появляется 1 звезда.
	/* 148 */U_EVENT_STAR2_TA(148), // Над персонажем появляется 2 звезда.
	/* 149 */U_EVENT_STAR3_TA(149), // Над персонажем появляется 3 звезда.
	/* 150 */U_EVENT_STAR4_TA(150), // Над персонажем появляется 4 звезда.
	/* 151 */U_EVENT_STAR5_TA(151), // Над персонажем появляется 5 звезда.
	/* 152 */U_AVE_ABSORB_SHIELD(152), // Вокруг персонажа образовывается белая сфера состоящая из шестиугольников.
	/* 153 */U_KN_PHOENIX_AURA(153), // Перед персонажем образовывается сферичный крест белый и исчезает.
	/* 153 */ave_aura_buff(153), // Перед персонажем образовывается сферичный крест белый и исчезает.
	/* 154 */U_KN_REVENGE_AURA(154), // Над персоажем происходит фиолетовая дымовая вспышка и исчезает.
	/* 155 */U_KN_EVAS_AURA(155), // Из тела персонажа идет синий дымок, но в начале крутятся вокруг него синий
	// цилиндр из шестиугольников.
	/* 156 */U_KN_REMPLA_AURA(156), // Из тела персонажа идет синий дымок, но в начале над ним появляются мечи.
	/* 157 */U_AVE_LONGBOW(157), // На оружии происходит белая вспышка и исчезает.
	/* 158 */U_AVE_WIDESWORD(158), // Оружие цилиндрично светиться радужными цветами.
	/* 159 */U_AVE_BIGFIST(159), // Кастеты светятся почти как геройские.
	/* 160 */U_AVE_SHADOWSTEP(160), //
	/* 161 */U_TORNADO_AVE(161), // поднимает вверх персонажа воздушным торнадо
	/* 162 */U_AVE_SNOW_SLOW(162), //
	/* 163 */U_AVE_SNOW_HOLD(163), //
	/* 164 */UNK_164(164), //
	/* 165 */U_AVE_TORNADO_SLOW(165), //
	/* 166 */U_AVE_ASTATINE_WATER(166), // поднимает вверх персонажа водным торнадо
	/* 167 */U_BIGBD_CAT_NPC(167), //
	/* 168 */U_BIGBD_UNICORN_NPC(168), //
	/* 169 */U_BIGBD_DEMON_NPC(169), //
	/* 170 */U_BIGBD_CAT_PC(170), //
	/* 171 */U_BIGBD_UNICORN_PC(171), //
	/* 172 */U_BIGBD_DEMON_PC(172), //
	/* 173 */BIG_BODY_2(173), //
	/* 174 */BIG_BODY_3(174), // персонаж уменьшаеться
	/* 175 */PIRATE_SUIT(175), // пират
	/* 176 */DARK_ASSASSIN_SUIT(176), // ассасин
	/* 177 */WHITE_ASSASSIN_SUIT(177), // белый ассасин
	/* 178 */UNK_178(178), // мушкетер
	/* 179 */RED_WIZARD_SUIT(179), // чародей в бардовом какой-то
	/* 180 */MYSTIC_SUIT(180), // джентельмен в цилиндре
	/* 181 */AVE_DRAGON_ULTIMATE(181), //
	/* 182 */HALLOWEEN_SUIT(182), // Трансформация в Фиолетовый Наряд Хеллоуина
	/* 183 */INFINITE_SHIELD1_AVE(183), // В очень тусклом красном шаре напоминающий футбольный
	/* 184 */INFINITE_SHIELD2_AVE(184), // В тусклом красном шаре напоминающий футбольный
	/* 185 */INFINITE_SHIELD3_AVE(185), // В красном шаре напоминающий футбольный
	/* 186 */INFINITE_SHIELD4_AVE(186), // В красном шаре напоминающий футбольный
	/* 187 */AVE_ABSORB2_SHIELD(187), // В синем шаре напоминающий футбольный
	/* 188 */UNK_188(188), // Раздевает туловище
	/* 189 */UNK_189(189), // Раздевает туловище
	/* 190 */TALISMAN_BAIUM(190), // Светится правая рука золотым
	TALI_DECO_BAIUM(190), // Светится правая рука золотым
	BLUE_DYNASTY(191), // Трансформация в Белую Династию
	RED_ZUBEI(192), // Трансформация в Золотой Зубей
	/* 193 */CHANGESHAPE_TRANSFORM(193), // Трансформация в Мушкитера
	/* 194 */ANGRY_GOLEM_AVE(194), // Вспыхивает солнце и начинает чар гореть
	/* 195 */WA_UNBREAKABLE_SONIC_AVE(195), // Свечение в виде солнца
	/* 196 */HEROIC_HOLY_AVE(196), // Трансформация в Темного Рыцаря
	/* 197 */HEROIC_SILENCE_AVE(197), // Над головой сало и впышки фиолетового дыма в районе живота
	/* 198 */HEROIC_FEAR_AVE_1(198), // Над головой крутится синее и впышки фиолетового дыма в районе живота
	/* 199 */HEROIC_FEAR_AVE_2(199), // Над головой крутится синее и впышки фиолетового дыма в районе живота
	/* 200 */AVE_BROOCH(200), // Временно светятся сиськи белым
	/* 201 */AVE_BROOCH_B(201), // Временно светятся сиськи голубым
	/* 202 */UNK_202(202), //
	/* 203 */UNK_203(203), //
	/* 204 */UNK_204(204), //
	/* 205 */UNK_205(205), //
	/* 206 */INFINITE_SHIELD4_AVE_2(206), // В красном шаре напоминающий футбольный
	/* 207 */CHANGESHAPE_TRANSFORM_1(207), // Раздевает и временное свечение
	/* 208 */CHANGESHAPE_TRANSFORM_2(208), // Раздевает и временное свечение
	/* 209 */CHANGESHAPE_TRANSFORM_3(209), // Раздевает и временное свечение
	/* 210 */CHANGESHAPE_TRANSFORM_4(210), // Раздевает и временное свечение
	/* 211 */UNK_211(211), // Раздевает
	/* 212 */UNK_212(212), // Раздевает
	/* 213 */UNK_213(213), // Раздевает туловище
	/* 214 */UNK_214(214), // Раздевает туловище
	/* 215 */RO_COUNTER_TRASPIE(215), // Дымится черным
	/* 215 */y_ro_counter_traspie_ave_smok(215), //
	/* 215 */y_ro_counter_traspie_ave_round(215), //
	/* 216 */UNK_216(216), // Раздевает
	/* 217 */RO_GHOST_REFLECT(217), // Дымится фиолетовым
	/* 218 */CHANGESHAPE_TRANSFORM_5(218), // Переодевает в розовое платье с салатовой кофточкой
	/* 219 */ICE_ELEMENTALDESTROY(219), // Пронзает льдинами
	/* 220 */DORMANT_USER(220), //
	/* 221 */NUWBIE_USER(221), //
	/* 222 */THIRTEENTH_BUFF(222), //
	/* 223 */UNK_223(223), //
	/* 224 */ARENA_UNSEAL_A(224), //
	/* 225 */ARENA_UNSEAL_B(225), //
	/* 226 */ARENA_UNSEAL_C(226), //
	/* 227 */ARENA_UNSEAL_D(227), //
	/* 228 */ARENA_UNSEAL_E(228), //
	/* 229 */IN_BATTLE_RHAPSODY(229), //
	/* 230 */IN_A_DECAL(230), //
	/* 231 */IN_B_DECAL(231), //
	/* 232 */CHANGESHAPE_TRANSFORM_6(232), //
	/* 233 */UNK_233(233), //
	/* 234 */CHANGESHAPE_TRANSFORM_7(234), //
	SPIRIT_KING_WIND_AVE_B(235),
	EARTH_KING_BARRIER1_AVE(236),
	EARTH_KING_BARRIER2_AVE(237),
	/* 238 */UNK_238(238), //
	/* 239 */UNK_239(239), //
	/* 240 */UNK_240(240), //
	/* 241 */UNK_241(241), //
	/* 242 */UNK_242(242), //
	/* 243 */UNK_243(243), //
	/* 244 */UNK_244(244), //
	/* 245 */UNK_245(245), //
	/* 246 */UNK_246(246), //
	/* 247 */FOCUS_SHIELD(247), //
	/* 248 */RAISE_SHIELD(248), //
	/* 249 */TRUE_VANGUARD(249), //
	/* 250 */SHIELD_WALL(250), //
	/* 251 */FOOT_TRAIL(251), // Летает Дракон желтый
	/* 252 */LEGEND_DECO_HERO(252), // Летает Дракон желтый
	/* 253 */WHITE_CAT_SUIT(253), // Переодевает в костюм кошечки
	SPIRIT_KING_WIND_AVE(254),
	U098_BUFF_TA_DECO(255),
	U098_RIGHT_DECO(255),
	U098_LEFT_DECO(255),
	ORFEN_ENERGY1_AVE(256),
	ORFEN_ENERGY2_AVE(257),
	ORFEN_ENERGY3_AVE(258),
	ORFEN_ENERGY4_AVE(259),
	ORFEN_ENERGY5_AVE(260),
	/* 261 */RED_CAT_SUIT(261), // Переодевает в костюм рыжего кота
	/* 262 */PANDA_SUIT(262), // Переодевает в костюм панды
	UNHOLY_BARRIER_AVE(263),
	/* 264 */UNK_264(264), //
	/* 265 */UNK_265(265), //
	RUDOLF_A_AVE(266),
	RUDOLF_B_AVE(267),
	RUDOLF_C_AVE(268),
	OLYMPIAD_MEDAL_AVE(269),
	OLYMPIAD_SPORT_A_AVE(270),
	OLYMPIAD_SPORT_B_AVE(271),
	OLYMPIAD_SPORT_C_AVE(272),
	/* 273 */DRAGON_SUIT(273), // Дракон Берсерк
	/* 274 */NINJA_SUIT(274), // Нинзя
	/* 275 */BLUE_MUSKETEER_SUIT(275), // Синий Мушкетер
	/* 276 */VALKYRIE_SUIT(276), // Валькирия
	/* 277 */WOLF_BARBARIAN_SUIT(277), // Волчий Варвар
	/* 278 */PIRATE_2_SUIT(278), // Пират
	/* 279 */RED_COWBOY_SUIT(279), // Красный Ковбой
	/* 280 */SUPREME_SUIT(280), // Верховный
	/* 281 */RED_ROYAL_SUIT(281), // Красная Королева
	/* 282 */WHITE_ROYAL_SUIT(282), // Белая Королева
	HDDOWN_AVE(283),
	HDMDOWN_AVE(284),
	/* 285 */UNK_285(265),
	KISSNHEART_AVE(286),
	EARTH_BARRIER_AVE(287),
	LILITH_DARK_BARRIER_AVE(288),
	EARTH_BARRIER2_AVE(289),
	CROFFIN_QUEEN_INVINCIBILITY_AVE(290),
	MPDOWN_AVE(291),
	WORLDCUP_RED_AVE(292),
	WORLDCUP_BLUE_AVE(293),
	/* 294 */UNK_294(294), //
	/* 295 */UNK_295(295), //
	SURGEWAVE_AVE(296),
	BLESS_AVE(297),
	ANTHARAS_RAGE_AVE(298),
	/* 299 */UNK_299(299), //
	/* 300 */UNK_300(300), //
	/* 301 */UNK_301(301), //
	G_BARRIER_AVE(302),
	/* 303 */UNK_303(303),
	FIREWORKS_001T(304),
	FIREWORKS_002T(305),
	FIREWORKS_003T(306),
	FIREWORKS_004T(307),
	FIREWORKS_005T(308),
	FIREWORKS_006T(309),
	FIREWORKS_007T(310),
	FIREWORKS_008T(311),
	FIREWORKS_009T(312),
	FIREWORKS_010T(313),
	FIREWORKS_011T(314),
	FIREWORKS_012T(315),
	FIREWORKS_013T(316),
	FIREWORKS_014T(317),
	FIREWORKS_015T(318),
	P_CAKE_AVE(319),
	/*
	 * 320
	 */UNK_320(320), //
	/* 321 */UNK_321(321), //
	/* 322 */BLUE_BIKINI_SUIT(322), // Синий Полосатый Купальник
	/* 323 */ZARICHE_PRISON(323), //
	/* 324 */RUDOLPH(324),
	XMAS_HEART_AVE(325),
	XMAS_HAND_AVE(326),
	LUCKYBAG_AVE(327),
	/* 328 */HEROIC_MIRACLE(328), //
	POCKETPIG_AVE(329),
	BLACK_STANCE_AVE(330),
	BLACK_TRANS_DECO_AVE(330),
	/* 331 */DARK_VEIL(331), //
	WHITE_STANCE_AVE(332),
	/* 333 */LIGHT_VEIL(333), //
	LONG_RAPIER_WHITE_AVE(334),
	LONG_RAPIER_BLACK_AVE(335),
	/* 336 */ZARICHE_PRISON_B(336), //
	/* 337 */BLUE_HEART(337), //
	/* 338 */ATTACK_BUFF(338), //
	/* 339 */SHIELD_BUFF(339), //
	/* 340 */BERSERKER_BUFF(340), //
	SEED_TALISMAN8(341),
	/* 342 */GOLD_STAR_1(342), //
	/* 343 */GOLD_STAR_2(343), //
	/* 344 */GOLD_STAR_3(344), //
	/* 345 */GOLD_STAR_4(345), //
	/* 346 */GOLD_STAR_5(346), //
	AVE_RAID_AREA(347),
	H_HEART_ADEN_AVE(348),
	H_ADENBAG_COIN_AVE(349),
	/* 350 */DARK_VEIL_1(350), //
	/* 351 */LIGHT_VEIL_1(351), //
	/* 352 */DEATH_EFFECT(352), //
	/* 353 */WHITE_KNIGHT(353), // костюм белого танка
	U_ER_WI_WINDHIDE_AVE(354),
	/* 355 */UNK_355(355), //
	/* 356 */UNK_356(356), //
	/* 357 */UNK_357(357), //
	/* 358 */UNK_358(358), //
	/* 359 */UNK_359(359), //
	/* 360 */UNK_360(360), //
	/* 361 */UNK_361(361), //
	/* 362 */UNK_362(362), //
	/* 363 */UNK_363(363), //
	/* 364 */UNK_364(364), //
	/* 365 */UNK_365(365), //
	/* 366 */UNK_366(366), //
	/* 367 */UNK_367(367), //
	/* 368 */UNK_368(368), //
	/* 369 */UNK_369(369), //
	/* 370 */UNK_370(370), //
	/* 371 */UNK_371(371), //
	/* 372 */UNK_372(372), //
	/* 373 */UNK_373(373), //
	/* 374 */UNK_374(374), //
	/* 375 */UNK_375(375), //
	/* 376 */UNK_376(376), //
	/* 377 */UNK_377(377), //
	/* 378 */UNK_378(378), //
	/* 379 */UNK_379(379), //
	/* 380 */UNK_380(380), //
	/* 381 */UNK_381(381), //
	/* 382 */UNK_382(382), //
	/* 383 */UNK_383(383), //
	/* 384 */UNK_384(384), //
	/* 385 */DEMON_SUIT, // demoneese suit
	/* 385 */RANKER_HUMAN(385), // human ranker transform
	/* 386 */RANKER_KAMAEL(386), // kamael ranker transform (has 2 dark wings)
	/* 387 */DEATH_KNIGHT_FLAME(387), //
	/* 388 */BONE_PRISON(388), //
	/* 389 */DEATH_KNIGHT_ARMOR_CHANGE(389), //
	/* 390 */IGNITION_HUMAN(390), //
	/* 391 */IGNITION_ELF(391), //
	/* 392 */IGNITION_DARKELF(392), //
	/* 393 */ACCELERATION(393), //
	/* 394 */BURN(394), //
	/* 395 */FREEZING_AREA(395), //
	/* 396 */SHOCK(396), //
	/* 397 */PERFECT_SHIELD(397), //
	/* 398 */FROSTBITE(398), //
	/* 399 */BONE_PRISON_SQUELA(399), //
	/* 400 */H_B_HASTE_B_AVE(400),
	/* 401 */STIGMA(401), //
	/* 402 */UNK_402(402), //
	/* 403 */FORT_FLAG(403), //
	/* 404 */HEAD_BALL(404), //
	/* 405 */UNK_405(405), //
	/* 406 */UNK_406(406), //
	/* 407 */UNK_407(407), //
	/* 408 */UNK_408(408), //
	/* 409 */RED_SPHERE(409), //
	/* 410 */BURNING_FLAME(410), //
	/* 411 */DARK_FLAME(411), //
	/* 412 */ORC_HEAD(412), //
	/* 413 */UNK_413(413), //
	/* 414 */UNK_414(414), //
	/* 415 */UNK_415(415), //
	/* 416 */UNK_416(416), //
	/* 417 */UNK_417(417), //
	H_EVENT_PUMPKIN_AVE(418),
	H_EVENT_PUMPKIN_B_AVE(419), // тыква хэлуина рядом с головой
	AVE_POISON_GROUND_G(420),
	AVE_POISON_GROUND_B(421),
	AVE_POISON_GROUND_P(422),
	AVE_POISON_GROUND_R(423),
	/* 424 */RANKER_ORC(424), // orc ranker transform
	H_DEBUFF_SELF_B_AVE(425),
	H_AURA_DEBUFF_B_AVE(426),
	H_ULTIMATE_DEFENCE_B_AVE(427),
	/* 428 */RANKER_ELF(428), // elf
	// ranker
	// transform
	/* 429 */RANKER_DARK_ELF(429), // dark elf ranker transform
	/* 430 */RANKER_DWARF(430), // dwarf ranker transform
	H_Y_MAGNETIC_AVE(431),
	/* 432 */UNK_432(432), //
	H_R_NATURAL_BEAST_AVE(433),
	/* 434 */UNK_434(434), //
	H_BERSERKER_B_BUFF_AVE(435),
	H_BERSERKER_C_BUFF_AVE(436),
	AVE_DIVINITY(437),
	Y_RO_GHOST_REFLECT_AVE(438),
	S_EVENT_KITE_DECO(439),
	/* 440 */UNK_440(440), //
	/* 441 */H_B_SYMPHONY_SWORD_AVE(441), //
	H_B_SYMPHONY_SWORD_DEFENCE_AVE(442),
	H_B_SYMPHONY_SWORD_BUFF_A_AVE(443),
	H_B_SYMPHONY_SWORD_BUFF_B_AVE(444),
	H_G_POISON_DANCE_AVE(445),
	H_G_POISON_DANCE_DEBUFF_A_AVE(446),
	H_G_POISON_DANCE_DEBUFF_B_AVE(447),
	H_R_POISON_DANCE_BUFF_B_AVE(448),
	/* 449 */COUPLE_DANCE(449), //
	H_B_CHOCOLATE_AVE(450),
	H_P_CHAIN_BLOCK_AVE(451),
	H_EVENT_MASK_AVE(452),
	/* 453 */UNK_453(453), //
	H_R_ORC_TITAN_AVE(454),
	H_R_GIGANTIC_WEAPON_AVE(455),
	/* 456 */UNK_456(456), //
	H_B_TOTEM_PUMA_AVE(457),
	H_Y_TOTEM_RABBIT_AVE(458),
	H_G_TOTEM_OGRE_AVE(459),
	H_Y_ORC_HP_AVE(460),
	H_B_ORC_HP_AVE(461),
	V_ORC_IMMOLATION_BODY_AVE(462),
	H_R_ORC_WAR_ROAR_AVE(463),
	/* 464 */UNK_464(464), //
	V_ORC_TOUCH_AVE(465),
	H_B_PARTY_UNITEA_AVE(466),
	/* 467 */LAUGH(467), //
	V_ORC_DEBUFF_MASTER_AVE(468),
	V_ORC_FLAME_BLAST_A_AVE(469),
	V_ORC_FLAME_BLAST_B_AVE(470),
	H_B_PARTY_UNITEB_AVE(471),
	H_R_BLOOD_LINKA_AVE(472),
	H_R_BLOOD_LINKB_AVE(473),
	V_ORC_COLD_FLAME_A_AVE(474),
	V_ORC_COLD_FLAME_B_AVE(475), // синяя
	// бомба\огонь
	// от
	// жиовта
	H_B_SPIRITWIND_AVE(477), // синеи пламя от живота
	H_W_SEACREATURE_AVE(478), // два воздушных элемента вокруг ног
	V_EVENT_2020_SUMMER_AVE(479), // арбуз рядом с головой
	/* 480 */UNK_480(480), //
	/* 481 */BOARD_RANKER(481), //
	/* 482 */BOARD_D(482), //
	/* 483 */BOARD_C(483), //
	/* 484 */BOARD_B(484), //
	/* 485 */BOARD_A(485), //
	H_G_AFFINITYA_AVE(486), // Голубое свечение от живота без сердечек
	H_G_AFFINITYB_AVE(487), // Голубое свечение от живота с сердечками
	V_SY_ELE_GUARD_AVE(488),
	H_SY_ELEMENTAL_STORM_AVE(489), // вихрь, похоже для бафа на скорость
	V_SY_ELE_TRANSITION_AVE(490), // красное пламя сверху
	H_SY_ELEMENTAL_RECOVERY_AVE(491), // синее пламя с ветром вокруг тела
	H_SY_FREEZING_AVE(492), // синее пламя с льдом вокруг тела
	V_SY_BURST_TIME_AVE(493), // синее пламя вокруг тела
	/* 494 */UNK_494(494), //
	/* 495 */UNK_495(495), //
	S_MASS_SALVATION_BUFF_AVE(496), // желтый крест со столбом над головой
	H_B_EXPEL_AVE(497), // синий крест со столбом над головой
	S_BANISH_DEBUFF_AVE(498), // красный крест со столбом над головой
	H_B_PROTECTION_SHIELD_AVE(499),
	H_R_PROTECTION_SHIELD_AVE(500),
	H_B_PROTECTION_MANA_AVE(501),
	H_Y_ROLLING_DICEA_AVE(502),
	H_Y_ROLLING_DICEB_AVE(503),
	H_Y_ROLLING_DICEC_AVE(504),
	H_R_METEOR_AVE(505),
	/* 506 */UNK_506(506), //
	H_SY_ELEMENTAL_ROAR_AVE(507),
	WH_HEAL_AVE(508),
	H_SY_ELEMENTAL_DUST_AVE(509),
	S_MENTOR_AVE(510),
	/* 511 */RANKER_SYLPH(511), // sylph ranker transformation
	/* 512 */UNK_512(512), //
	H_SY_ELEMENTAL_MARK_AVE(513),
	/* 514 */ICE_SCREEN(514),
	/* 515 */ICE_ONEHAND(515),
	/* 516 */ICE_TWOHAND(516),
	H_B_ICE_TWOHAND(516),
	/* 517 */ICE_LONG(517),
	V_ICE_LONG_02(517),
	V_FORTUNE_TIME(518),
	V_FORTUNE_TIME_DEBUFF(519),
	V_GOLD_STONE(520),
	V_GOLD_STONE_DEBUFF(521),
	V_VITAL_GAIN_LV1(522),
	V_VITAL_GAIN_LV2(523),
	V_VITAL_GAIN_LV3(524),
	V_VITAL_GAIN_LV4(525),
	V_VITAL_GAIN_LV5(526),
	H_B_OVERDRIVE(527),
	/*
	 * 528
	 */UNK_528(528), //
	H_LUCKYBAG2(529),
	/* 530 */UNK_530(530), //
	V_DISRON_VICTORY(531),
	H_P_AEGIS_ARMOR_DECO(532),
	/* 533 */UNK_533(533), //
	/* 534 */UNK_534(534), //
	/* 535 */UNK_535(535), //
	/* 536 */UNK_536(536), //
	/* 537 */UNK_537(537), //
	/* 538 */UNK_538(538), //
	/* 539 */UNK_539(539), //
	V_WORLDCASTLEWAR_HERO_WEAP(540),
	V_EARASED(541),
	H_R_IMPRISON(542),
	H_R_NEMESIS(543),
	H_Y_PROTECTION_SHIELD(544),
	H_P_PROTECTION_SHIELD(545),
	H_W_PROTECTION_SHIELD(546),
	UNK_548(548), // костюм похож на нпцешный

	// TODO: MORSE: Новое в 338 Essence
	V_KNIGHT_PHOENIX_AVE(549),
	V_KNIGHT_PHOENIX_02_AVE(549),
	V_KNIGHT_HELL_HAND_AVE(550),
	V_KNIGHT_HELL_HAND_AVE1(550),
	V_KNIGHT_SHELTER_02_AVE(551), // skill id 47310 Shelter
	V_KNIGHT_SHELTER_AVE(552), // skill id 47311 Shelter Friends
	V_KNIGHT_CONDEMN_AVE(553),
	H_Y_ULTIMATE_DEFENCE_AVE(554),
	DK2_EQUAL_AURA_AVE(555),
	DK2_RAGE_AURA_AVE(556),
	H_DK2_SARDONIC_FORTITUDEA_AVE(557),
	H_DK2_SARDONIC_FORTITUDEB_AVE(558),
	S_DRAGON_SLAYER_AVE(559),
	V_DARION_REFLECTION_AVE(560),
	V_DARION_ROAR_AVE(561),
	V_DARION_CUBIC_AVE(562),
	V_BERES_FOCUSING_AVE(563),
	V_BERES_CURE_AVE(564),
	V_ROGUE_PHANTOM_01_AVE(566),
	V_ROGUE_PHANTOM_02_AVE(567),
	V_ROGUE_PHANTOM_03_AVE(568),
	V_ROGUE_FURY_BLADE_AVE(569),
	V_ROGUE_SYNCHRO_AVE(570),
	H_P_REVERSE_PULLING_AVE(571),
	H_P_REVERSE_PULLINGB_AVE(571),
	H_P_FAST_ASSAULT_AVE(574),
	H_R_BLUFF_AVE(575),
	V_ROGUE_CRITICAL_WOUND_AVE(576),
	H_P_PROTECTION_OF_DARK_AVE(578),
	H_P_PROTECTION_OF_EVA_AVE(579),
	V_DK2_ULTIMATE_DEFENCE_AVE(580),
	V_DK2_ROAR_AVE(581),
	H_AURA_DEBUFFB_AVE(582),
	AVE_AURA_DEBUFF(583),

	// TODO: MORSE: Новое в 362 Essence
	H_Y_SAGITTARIUSA_AVE(584),
	H_Y_SAGITTARIUSB_AVE(584),
	H_B_MOONLIGHTA_AVE(585),
	H_B_MOONLIGHTB_AVE(585),
	H_P_GHOSTA_AVE(586),
	H_P_GHOSTB_AVE(586),
	H_R_TRICKSTERA_AVE(587),
	H_R_TRICKSTERB_AVE(587),
	V_ARCHER_TRUE_TARGET_AVE(588),
	V_ARCHER_SHAPE_SHOOTER_AVE(589),
	H_R_SPIKE_SHOT_AVE(590),
	V_ARCHER_CHAIN_ARREST_AVE(591),
	V_ARCHER_ARROW_SHOWER_AVE(592),
	V_WIZARD_MYSTIC_SHIELD_AVE(594),
	V_WIZARD_SOUL_GARD_AVE(595),
	V_WIZARD_SOUL_GARD_GROUND_AVE(595),
	V_WIZARD_DEEP_SLEEP_AVE(596),
	S_EVENT_MOON_AVE(597),
	// V_DARION_REFLECTION_AVE(598),
	V_OR_BURNING_BEAST_FIRE_AVE(599),
	V_OR_BURNING_BEAST_FIRE_AVE_1(599),
	V_OR_BURNING_BEAST_FIRE_AVE_2(599),
	V_OR_BURNING_BEAST_GROUND_AVE(599),
	VANGUARD_CHANGE_ARMOR(600),
	VANGUARD_RANKER(601),
	H_Y_BLESSING_OF_EMPEROR_AVE(602),
	H_B_BLESSING_OF_GUARDIAN_AVE(603),
	H_P_BLESSING_OF_LORD_AVE(604),
	H_B_LIFELINK_AVE(605),
	H_P_SERVITOR_ULTIMATE_AVE(606),
	AVE_STAR_X01_EV(607),
	AVE_STAR_X02_EV(608),
	AVE_STAR_X03_EV(609),
	AVE_STAR_X04_EV(610),
	AVE_STAR_X05_EV(611),
	AVE_STAR_X06_EV(612),
	AVE_STAR_X07_EV(613),
	AVE_STAR_X08_EV(614),
	AVE_STAR_X09_EV(615),
	AVE_STAR_X10_EV(616),
	AVE_STAR_X11_EV(617),
	AVE_STAR_X12_EV(618),
	AVE_STAR_X13_EV(619),
	AVE_STAR_X14_EV(620),
	AVE_STAR_X15_EV(621),
	AVE_STAR_X16_EV(622),
	AVE_STAR_X17_EV(623),
	AVE_STAR_X18_EV(624),

	DEXTEROUS_BODY(0), // TODO: Подобрать ID.

	BR_POWER_OF_EVA(0), // TODO: Подобрать ID.
	VP_KEEP(29), // TODO: Подобрать ID.
	BLUE_TALL_LIGHT(0), // TODO: Подобрать ID

	U_AVE_DRAGON_ULTIMATE(700),

	CHANGE_HALLOWEEN(1000),
	BR_Y_1_ACCESSORY_R_RING(10001),
	BR_Y_1_ACCESSORY_EARRING(10002),
	BR_Y_1_ACCESSORY_NECKRACE(10003),
	BR_Y_2_ACCESSORY_R_RING(10004),
	BR_Y_2_ACCESSORY_EARRING(10005),
	BR_Y_2_ACCESSORY_NECKRACE(10006),
	BR_Y_3_ACCESSORY_R_RING(10007),
	BR_Y_3_ACCESSORY_EARRING(10008),
	BR_Y_3_ACCESSORY_NECKRACE(10009),
	BR_Y_3_TALI_DECO_WING(10019),

	BASEBALL_COSTUME(10020), // baseball costume
	SANTA_COSTUME(10021), // santa costume
	SANTA_1_COSTUME(10022), // black aura kinda light
	SANTA_2_COSTUME(10023), // black aura heavier
	SCHOOOL_UNIFORM_COSTUME(10024), // black suit looks something japanese
	SCHOOOL_UNIFORM_1_COSTUME(10025), // black aura heavier
	SCHOOOL_UNIFORM_2_COSTUME(10026), // black aura even heavier
	BEACH_COSTUME(10027), // beach costume
	BEACH_1_COSTUME(10028), // black aura with ignition effect
	BEACH_2_COSTUME(10029), // black aura heavier with ignition
	BEAR_COSTUME(10030), // bear costume
	BEAR_1_COSTUME(10031), // bear costume with black aura with ignition
	BEAR_2_COSTUME(10032), // bear costume with black aura with ignition heavier
	CAT_COSTUME(10033), // cat costume with sparks
	CAT_1_COSTUME(10034), // cat costume with black aura
	CAT_2_COSTUME(10035), // cat costume with black aura heavier
	PANDA_COSTUME(10036), // panda costume
	PANDA_1_COSTUME(10037), // panda costume with black aura
	PANDA_2_COSTUME(10038), // panda costume with black aura heavier
	BASEBALL_COSTUME2(10039), // baseball costume with sparks
	BASEBALL_1_COSTUME2(10040), // baseball costume with black aura
	BASEBALL_2_COSTUME2(10041), // baseball costume with black aura heavier
	BEACH_COSTUME2(10042), // beach costume
	BEACH_1_COSTUME2(10043), // beach costume with black aura
	BEACH_2_COSTUME2(10044), // beach costume with black aura heavier
	SCHOOOL_UNIFORM_COSTUME2(10045), // school uniform costume
	SCHOOOL_UNIFORM_1_COSTUME2(10046), // school uniform costume with black aura
	SCHOOOL_UNIFORM_2_COSTUME2(10047), // school uniform costume with black aura heavier
	SAMURAI_COSTUME(10048), // samurai costume
	SAMURAI_1_COSTUME(10049), // samurai costume with black aura
	SAMURAI_2_COSTUME(10050), // samurai costume with black aura heavier
	ELITE_COSTUME(10051), // looks like very elite armor costume
	ELITE_1_COSTUME(10052), // elite armor costume with black aura
	ELITE_2_COSTUME(10053), // elite armor costume with black aura heavier
	SAMURAI_BATTLE_COSTUME(10054), // samurai battle costume
	SAMURAI_BATTLE_1_COSTUME(10055), // samurai battle costume with black aura
	SAMURAI_BATTLE_2_COSTUME(10056), // samurai battle costume with black aura heavier
	ROBIN_HOOD_COSTUME(10057), // robin hood costume
	ROBIN_HOOD_1_COSTUME(10058), // robin hood costume with black aura
	ROBIN_HOOD_2_COSTUME(10059), // robin hood costume with black aura heavier
	FORMAL_WEAR_COSTUME(10060), // formal wear costume
	FORMAL_WEAR_1_COSTUME(10061), // formal wear costume with black aura
	FORMAL_WEAR_2_COSTUME(10062), // formal wear costume with black aura heavier
	CAT_COSTUME2(10063), // cat armor costume
	CAT_1_COSTUME2(10064), // cat armor costume with black aura
	CAT_2_COSTUME2(10065), // cat armor costume with black aura heavier
	MAGICIAN_COSTUME(10066), // magician costume (holloween)
	MAGICIAN_1_COSTUME(10067), // magician costume with black aura
	MAGICIAN_2_COSTUME(10068), // magician costume with black aura heavier
	BEACH_COSTUME3(10069), // beach costume
	BEACH_1_COSTUME3(10070), // beach costume with black aura
	BEACH_2_COSTUME3(10071), // beach costume with black aura heavier
	LITTLE_DEMON_COSTUME(10072), // little demon (BDSM) costume
	LITTLE_DEMON_1_COSTUME(10073), // little demon costume with black aura
	LITTLE_DEMON_2_COSTUME(10074), // little demon costume with black aura heavier
	HEAVY_BLUE_ARMOR_COSTUME(10075), // heavy blue armor costume
	HEAVY_BLUE_ARMOR_1_COSTUME(10076), // heavy blue armor costume with black aura
	HEAVY_BLUE_ARMOR_2_COSTUME(10077), // heavy blue armor costume with black aura heavier
	ROYAL_KNIGHT_COSTUME(10078), // royal knight costume
	ROYAL_KNIGHT_1_COSTUME(10079), // royal knight costume with black aura
	ROYAL_KNIGHT_2_COSTUME(10080), // royal knight costume with black aura heavier
	RED_ROBIN_HOOD_COSTUME(10081), // red robin hood costume
	RED_ROBIN_HOOD_1_COSTUME(10082), // red robin hood costume with black aura
	RED_ROBIN_HOOD_2_COSTUME(10083), // red robin hood costume with black aura heavier
	FRANCE_OFFICER_COSTUME(10084), // france officer costume
	FRANCE_OFFICER_1_COSTUME(10085), // france officer costume with black aura
	FRANCE_OFFICER_2_COSTUME(10086), // france officer costume with black aura heavier
	BRONZE_WITCH_COSTUME(10087), // bronze witch costume
	BRONZE_WITCH_1_COSTUME(10088), // bronze witch costume with black aura
	BRONZE_WITCH_2_COSTUME(10089), // bronze witch costume with black aura heavier
	FAT_MAGICIAN_COSTUME(10090), // fat magician costume
	FAT_MAGICIAN_1_COSTUME(10091), // fat magician costume with black aura
	FAT_MAGICIAN_2_COSTUME(10092), // fat magician costume with black aura heavier
	PIRATE_COSTUME(10093), // pirate costume
	PIRATE_1_COSTUME(10094), // pirate costume with black aura
	PIRATE_2_COSTUME(10095), // pirate costume with black aura heavier
	KING_COSTUME(10096), // king costume
	KING_1_COSTUME(10097), // king costume with black aura
	KING_2_COSTUME(10098), // king costume with black aura heavier
	WOLF_LEATHER_COSTUME(10099), // wolf leather costume
	WOLF_LEATHER_1_COSTUME(10100), // wolf leather costume with black aura
	WOLF_LEATHER_2_COSTUME(10101), // wolf leather costume with black aura heavier
	VIKING_COSTUME(10102), // viking costume
	VIKING_1_COSTUME(10103), // viking costume with black aura
	VIKING_2_COSTUME(10104), // viking costume with black aura heavier
	KELBIM_COSTUME(10105), // black light with wings costume
	KELBIM_1_COSTUME(10106), // black light costume with black aura
	KELBIM_2_COSTUME(10107), // black light costume with black aura heavier
	CAREBIANS_PIRATE_COSTUME(10108), // pirate aka carebians costume
	CAREBIANS_PIRATE_1_COSTUME(10109), // pirate costume with black aura
	CAREBIANS_PIRATE_2_COSTUME(10110), // pirate costume with black aura heavier
	ELF_PRINCE_COSTUME(10111), // elf prince costume
	ELF_PRINCE_1_COSTUME(10112), // elf prince costume with black aura
	ELF_PRINCE_2_COSTUME(10113), // elf prince costume with black aura heavier
	BRIGHT_KING_COSTUME(10114), // bright king costume
	BRIGHT_KING_1_COSTUME(10115), // bright king costume with black aura
	BRIGHT_KING_2_COSTUME(10116), // bright king costume with black aura heavier
	WILD_WEST_SHERIFF_COSTUME(10117), // wild west sheriff costume
	WILD_WEST_SHERIFF_1_COSTUME(10118), // wild west sheriff costume with black aura
	WILD_WEST_SHERIFF_2_COSTUME(10119), // wild west sheriff costume with black aura heavier
	MUSHKETEER_COSTUME(10120), // mushketeer costume
	MUSHKETEER_1_COSTUME(10121), // mushketeer costume with black aura
	MUSHKETEER_2_COSTUME(10122), // mushketeer costume with black aura heavier
	ZAKEN_COSTUME(10123), // zaken costume
	ZAKEN_1_COSTUME(10124), // zaken costume with black aura
	ZAKEN_2_COSTUME(10125), // zaken costume with black aura heavier
	RED_MOB_COSTUME(10126), // red mob with horns costume
	RED_MOB_1_COSTUME(10127), // red mob costume with black aura
	RED_MOB_2_COSTUME(10128), // red mob costume with black aura heavier
	STRANGE_SILVER_COSTUME(10129), // strange silver costume
	STRANGE_SILVER_1_COSTUME(10130), // strange silver costume with black aura
	STRANGE_SILVER_2_COSTUME(10131), // strange silver costume with black aura heavier
	DARK_KING_COSTUME(10132), // dark king costume
	DARK_KING_1_COSTUME(10133), // dark king costume with black aura
	DARK_KING_2_COSTUME(10134), // dark king costume with black aura heavier
	WEALTHY_BLUE_TUNIC_COSTUME(10135), // wealthy blue tunic costume
	WEALTHY_BLUE_TUNIC_1_COSTUME(10136), // wealthy blue tunic costume with black aura
	WEALTHY_BLUE_TUNIC_2_COSTUME(10137), // wealthy blue tunic costume with black aura heavier
	S_TRANS_DECO_40(10138),
	BR_TRANS_LV2_DECO_40(10139),
	BR_TRANS_LV3_DECO_40(10140),
	S_TRANS_DECO_41(10141),
	BR_TRANS_LV2_DECO_41(10142),
	BR_TRANS_LV3_DECO_41(10143),
	S_TRANS_DECO_42(10144),
	BR_TRANS_LV2_DECO_42(10145),
	BR_TRANS_LV3_DECO_42(10146),
	S_TRANS_DECO_43(10147),
	BR_TRANS_LV2_DECO_43(10148),
	BR_TRANS_LV3_DECO_43(10149),
	S_TRANS_DECO_44(10150),
	BR_TRANS_LV2_DECO_44(10151),
	BR_TRANS_LV3_DECO_44(10152),
	S_TRANS_DECO_45(10153),
	BR_TRANS_LV2_DECO_45(10154),
	BR_TRANS_LV3_DECO_45(10155),
	S_TRANS_DECO_46(10156),
	BR_TRANS_LV2_DECO_46(10157),
	BR_TRANS_LV3_DECO_46(10158),
	S_TRANS_DECO_47(10159),
	BR_TRANS_LV2_DECO_47(10160),
	BR_TRANS_LV3_DECO_47(10161),
	JDK_BODY_FIRE_2(10162),
	UNK_10163(10163),
	UNK_10164(10164),
	UNK_10165(10165),
	UNK_10166(10166),
	UNK_10167(10167),
	S_TRANS_DECO(10168),
	BR_TRANS_LV2_DECO(10169),
	BR_TRANS_LV3_DECO(10170);

	public static final AbnormalEffect[] VALUES = values();

	public static AbnormalEffect valueOf(int abnormalId)
	{
		for(AbnormalEffect abnormalEffect : VALUES)
		{
			if(abnormalEffect.getId() == abnormalId)
				return abnormalEffect;
		}
		return null;
	}

	private final int _id;

	AbnormalEffect()
	{
		_id = ordinal();
	}

	AbnormalEffect(int id)
	{
		_id = id;
	}

	public final int getId()
	{
		return _id;
	}

	public final String getName()
	{
		return toString();
	}
}
