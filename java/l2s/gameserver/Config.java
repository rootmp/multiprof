/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package l2s.gameserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import l2s.commons.configuration.ExProperties;
import l2s.commons.net.nio.impl.SelectorConfig;
import l2s.commons.string.StringArrayUtils;
import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.base.AcquireType;
import l2s.gameserver.model.base.PlayerAccess;
import l2s.gameserver.network.authcomm.ServerType;
import l2s.gameserver.skills.enums.AbnormalEffect;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.Language;

public class Config
{
	private static final Logger _log = LoggerFactory.getLogger(Config.class);

	public static final int NCPUS = Runtime.getRuntime().availableProcessors();

	/** Configuration files */
	// Default Folder
	public static final String CHAT_ANTIFLOOD_CONFIG_FILE = "config/chat_antiflood.properties";
	public static final String OTHER_CONFIG_FILE = "config/other.properties";
	public static final String RESIDENCE_CONFIG_FILE = "config/residence.properties";
	public static final String SPOIL_CONFIG_FILE = "config/spoil.properties";
	public static final String ALT_SETTINGS_FILE = "config/altsettings.properties";
	public static final String FORMULAS_CONFIGURATION_FILE = "config/formulas.properties";
	public static final String PVP_CONFIG_FILE = "config/pvp.properties";
	public static final String CONFIGURATION_FILE = "config/server.properties";
	public static final String AI_CONFIG_FILE = "config/ai.properties";
	public static final String GEODATA_CONFIG_FILE = "config/geodata.properties";
	public static final String SERVICES_FILE = "config/services.properties";
	public static final String OLYMPIAD = "config/olympiad.properties";
	public static final String DEVELOP_FILE = "config/develop.properties";
	public static final String EXT_FILE = "config/ext.properties";
	public static final String FAKE_PLAYERS_LIST = "config/fake_players.list";
	public static final String ESSENCE_FEATURES_FILE = "config/essence.properties";
	public static final String RATES_FILE = "config/rates.properties";
	public static final String ANUSEWORDS_CONFIG_FILE = "config/abusewords.txt";

	// Custom Folder
	public static final String BUFF_STORE_CONFIG_FILE = "config/custom/offline_buffer.properties";
	public static final String PVP_MANAGER_FILE = "config/custom/pvp_manager.properties";
	public static final String EVENTS_CONFIG_FILE = "config/custom/events.properties";
	public static final String BOT_FILE = "config/custom/anti_bot_system.properties";
	public static final String SCHEME_BUFFER_FILE = "config/custom/npcbuffer.properties";
	public static final String WEDDING_FILE = "config/custom/wedding.properties";
	public static final String FIGHT_CLUB_FILE = "config/custom/fightclub_events.properties";
	public static final String L2STUDIO = "config/custom/l2studio.properties";

	// Gm Folder
	public static final String GM_PERSONAL_ACCESS_FILE = "config/gm/master.xml";
	public static final String GM_ACCESS_FILES_DIR = "config/gm/";

	// Community Board Folder
	public static final String BBS_FILE_GENERAL = "config/community/bbs.properties";

	public static final String PHANTOM_PLAYERS_AKK = "rebellion";

	public static Map<Integer, Integer> CLAN_POINTS_FOR_KILL_RB;
	static
	{
		CLAN_POINTS_FOR_KILL_RB = new HashMap<>();

		// 1 очко за убийство РБ
		CLAN_POINTS_FOR_KILL_RB.put(25742, 1); // Жрец Ядра Дакар
		CLAN_POINTS_FOR_KILL_RB.put(25738, 1); // Трутень-Жрец Королевы Муравьев
		CLAN_POINTS_FOR_KILL_RB.put(25743, 1); // Жрец Орфен Лорд Ипос
		CLAN_POINTS_FOR_KILL_RB.put(25739, 1); // Ангел-Жрец Баюма

		// 2 очка за убийство РБ
		CLAN_POINTS_FOR_KILL_RB.put(29001, 2); // Королева Муравьев
		CLAN_POINTS_FOR_KILL_RB.put(29014, 2); // Орфен
		CLAN_POINTS_FOR_KILL_RB.put(29006, 2); // Ядро
		CLAN_POINTS_FOR_KILL_RB.put(29022, 2); // Закен
		CLAN_POINTS_FOR_KILL_RB.put(29020, 2); // Баюм
		CLAN_POINTS_FOR_KILL_RB.put(25286, 2); // Анаким
		CLAN_POINTS_FOR_KILL_RB.put(25283, 2); // Лилит

		// 3 очка за убийство РБ
		CLAN_POINTS_FOR_KILL_RB.put(29176, 3); // Бессмертный Баюм
		CLAN_POINTS_FOR_KILL_RB.put(29151, 3); // Антарас (1)
		CLAN_POINTS_FOR_KILL_RB.put(29152, 3); // Антарас (2)
		CLAN_POINTS_FOR_KILL_RB.put(29153, 3); // Антарас (3)
		CLAN_POINTS_FOR_KILL_RB.put(25912, 3); // Чудовище
		CLAN_POINTS_FOR_KILL_RB.put(25935, 3); // Совершенное Чудовище
		CLAN_POINTS_FOR_KILL_RB.put(29172, 3); // Хаотическая Королева Муравьёв
		CLAN_POINTS_FOR_KILL_RB.put(29171, 3); // Хаотическая Орфен
		CLAN_POINTS_FOR_KILL_RB.put(29173, 3); // Хаотический Закен
		CLAN_POINTS_FOR_KILL_RB.put(29170, 3); // Хаотическое Ядро
	}

	// Services Engine
	public static boolean ENABLE_BUFF_PARTY_SERVICES;
	public static int BUFF_ID1;
	public static int SKILL_LEVEL1;

	public static int BUFF_ID2;
	public static int SKILL_LEVEL2;

	public static int BUFF_ID3;
	public static int SKILL_LEVEL3;

	// Fight Club
	public static boolean FIGHT_CLUB_ENABLED;
	public static int MINIMUM_LEVEL_TO_PARRICIPATION;
	public static int MAXIMUM_LEVEL_TO_PARRICIPATION;
	public static int MAXIMUM_LEVEL_DIFFERENCE;
	public static String[] ALLOWED_RATE_ITEMS;
	public static int PLAYERS_PER_PAGE;
	public static int ARENA_TELEPORT_DELAY;
	public static boolean CANCEL_BUFF_BEFORE_FIGHT;
	public static boolean UNSUMMON_PETS;
	public static boolean UNSUMMON_SUMMONS;
	public static boolean REMOVE_CLAN_SKILLS;
	public static boolean REMOVE_HERO_SKILLS;
	public static int TIME_TO_PREPARATION;
	public static int FIGHT_TIME;
	public static boolean ALLOW_DRAW;
	public static int TIME_TELEPORT_BACK;
	public static boolean FIGHT_CLUB_ANNOUNCE_RATE;
	public static boolean FIGHT_CLUB_ANNOUNCE_RATE_TO_SCREEN;
	public static boolean FIGHT_CLUB_ANNOUNCE_START_TO_SCREEN;

	// anti bot stuff
	public static boolean ENABLE_ANTI_BOT_SYSTEM;
	public static int MINIMUM_TIME_QUESTION_ASK;
	public static int MAXIMUM_TIME_QUESTION_ASK;
	public static int MINIMUM_BOT_POINTS_TO_STOP_ASKING;
	public static int MAXIMUM_BOT_POINTS_TO_STOP_ASKING;
	public static int MAX_BOT_POINTS;
	public static int MINIMAL_BOT_RATING_TO_BAN;
	public static int AUTO_BOT_BAN_JAIL_TIME;
	public static boolean ANNOUNCE_AUTO_BOT_BAN;
	public static boolean ON_WRONG_QUESTION_KICK;

	public static int HTM_CACHE_MODE;
	public static int SHUTDOWN_ANN_TYPE;

	public static String DATABASE_DRIVER;
	public static int DATABASE_MAX_CONNECTIONS;
	public static int DATABASE_MAX_IDLE_TIMEOUT;
	public static int DATABASE_IDLE_TEST_PERIOD;
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;
	public static boolean DATABASE_AUTOUPDATE;

	// Database additional options
	public static boolean AUTOSAVE;

	public static long USER_INFO_INTERVAL;
	public static boolean BROADCAST_STATS_INTERVAL;
	public static long BROADCAST_CHAR_INFO_INTERVAL;

	public static int MIN_HIT_TIME;

	public static int SUB_START_LEVEL;
	public static int START_CLAN_LEVEL;
	public static boolean NEW_CHAR_IS_NOBLE;
	public static boolean ENABLE_L2_TOP_OVERONLINE;
	public static int L2TOP_MAX_ONLINE;
	public static int MIN_ONLINE_0_5_AM;
	public static int MAX_ONLINE_0_5_AM;
	public static int MIN_ONLINE_6_11_AM;
	public static int MAX_ONLINE_6_11_AM;
	public static int MIN_ONLINE_12_6_PM;
	public static int MAX_ONLINE_12_6_PM;
	public static int MIN_ONLINE_7_11_PM;
	public static int MAX_ONLINE_7_11_PM;
	public static int ADD_ONLINE_ON_SIMPLE_DAY;
	public static int ADD_ONLINE_ON_WEEKEND;
	public static int L2TOP_MIN_TRADERS;
	public static int L2TOP_MAX_TRADERS;
	public static int ALT_OLY_BY_SAME_BOX_NUMBER;

	public static boolean OLYMPIAD_ENABLE_ENCHANT_LIMIT;
	public static int OLYMPIAD_WEAPON_ENCHANT_LIMIT;
	public static int OLYMPIAD_ARMOR_ENCHANT_LIMIT;
	public static int OLYMPIAD_JEWEL_ENCHANT_LIMIT;

	public static boolean REFLECT_DAMAGE_CAPPED_BY_PDEF;

	public static int EFFECT_TASK_MANAGER_COUNT;

	public static int SKILLS_CAST_TIME_MIN_PHYSICAL;
	public static int SKILLS_CAST_TIME_MIN_MAGICAL;
	public static boolean ENABLE_CRIT_HEIGHT_BONUS;

	public static int MAXIMUM_ONLINE_USERS;

	public static int CLAN_WAR_LIMIT;
	public static int CLAN_WAR_REPUTATION_SCORE_PER_KILL;
	public static int CLAN_WAR_CANCEL_REPUTATION_PENALTY;

	public static boolean DONTLOADSPAWN;
	public static boolean DONTLOADQUEST;
	public static int MAX_REFLECTIONS_COUNT;

	public static int SHIFT_BY;
	public static int SHIFT_BY_Z;
	public static int MAP_MIN_Z;
	public static int MAP_MAX_Z;

	/** ChatBan */
	public static int CHAT_MESSAGE_MAX_LEN;
	public static boolean ABUSEWORD_BANCHAT;
	public static int[] BAN_CHANNEL_LIST = new int[18];
	public static boolean ABUSEWORD_REPLACE;
	public static String ABUSEWORD_REPLACE_STRING;
	public static int ABUSEWORD_BANTIME;
	public static Pattern ABUSEWORD_PATTERN = null;
	public static boolean BANCHAT_ANNOUNCE;
	public static boolean BANCHAT_ANNOUNCE_FOR_ALL_WORLD;
	public static boolean BANCHAT_ANNOUNCE_NICK;

	public static boolean ALLOW_REFFERAL_SYSTEM;

	public static int REF_SAVE_INTERVAL;

	public static int MAX_REFFERALS_PER_CHAR;

	public static int MIN_ONLINE_TIME;

	public static int MIN_REFF_LEVEL;

	public static double REF_PERCENT_GIVE;

	public static boolean PREMIUM_ACCOUNT_ENABLED;
	public static boolean PREMIUM_ACCOUNT_BASED_ON_GAMESERVER;
	public static int FREE_PA_TYPE;
	public static int FREE_PA_DELAY;
	public static boolean ENABLE_FREE_PA_NOTIFICATION;

	// catalyst chances
	public static int CATALYST_POWER_W_D;
	public static int CATALYST_POWER_W_C;
	public static int CATALYST_POWER_W_B;
	public static int CATALYST_POWER_W_A;
	public static int CATALYST_POWER_W_S;
	public static int CATALYST_POWER_A_D;
	public static int CATALYST_POWER_A_C;
	public static int CATALYST_POWER_A_B;
	public static int CATALYST_POWER_A_A;
	public static int CATALYST_POWER_A_S;

	public static boolean ALT_SELL_ITEM_ONE_ADENA;
	public static boolean SKILL_ABSORB;
	public static boolean BOW_ABSORB;
	public static boolean PHYS_ABSORB;

	public static boolean SKILL_ABSORBMP;
	public static boolean BOW_ABSORBMP;
	public static boolean PHYS_ABSORBMP;

	public static int MAX_SIEGE_CLANS;

	public static List<Integer> ITEM_LIST = new ArrayList<Integer>();

	public static List<Integer> DROP_ONLY_THIS = new ArrayList<Integer>();
	public static boolean INCLUDE_RAID_DROP;

	public static boolean SAVING_SPS;
	public static boolean CAN_SELF_DISPEL_SONG;
	public static boolean MANAHEAL_SPS_BONUS;

	public static int ALT_ADD_RECIPES;
	public static int ALT_MAX_ALLY_SIZE;

	public static int ALT_PARTY_DISTRIBUTION_RANGE;
	public static double[] ALT_PARTY_BONUS;
	public static double[] ALT_PARTY_CLAN_BONUS;
	public static int[] ALT_PARTY_LVL_DIFF_PENALTY;
	public static boolean ALT_ALL_PHYS_SKILLS_OVERHIT;

	public static double ALT_POLE_DAMAGE_MODIFIER;
	public static double LONG_RANGE_AUTO_ATTACK_P_ATK_MOD;
	public static double SHORT_RANGE_AUTO_ATTACK_P_ATK_MOD;

	public static double ALT_M_SIMPLE_DAMAGE_MOD;
	public static double ALT_P_DAMAGE_MOD;
	public static double ALT_M_CRIT_DAMAGE_MOD;
	public static double ALT_P_CRIT_DAMAGE_MOD;
	public static double ALT_P_CRIT_CHANCE_MOD;
	public static double ALT_M_CRIT_CHANCE_MOD;

	public static double SERVITOR_P_ATK_MODIFIER;
	public static double SERVITOR_M_ATK_MODIFIER;
	public static double SERVITOR_P_DEF_MODIFIER;
	public static double SERVITOR_M_DEF_MODIFIER;
	public static double SERVITOR_P_SKILL_POWER_MODIFIER;
	public static double SERVITOR_M_SKILL_POWER_MODIFIER;

	public static double ALT_BLOW_DAMAGE_MOD;
	public static double ALT_BLOW_CRIT_RATE_MODIFIER;
	public static double ALT_VAMPIRIC_CHANCE_MOD;

	public static boolean ALT_REMOVE_SKILLS_ON_DELEVEL;

	public static double ALT_SAYHAS_GRACE_RATE;
	public static double ALT_SAYHAS_GRACE_PA_RATE;
	public static double ALT_SAYHAS_GRACE_CONSUME_RATE;
	public static double RATE_LIMITED_SAYHA_GRACE_EXP_MULTIPLIER;

	public static boolean ALT_PCBANG_POINTS_ENABLED;
	public static boolean PC_BANG_POINTS_BY_ACCOUNT;
	public static boolean ALT_PCBANG_POINTS_ONLY_PREMIUM;
	public static double ALT_PCBANG_POINTS_BONUS_DOUBLE_CHANCE;
	public static int ALT_PCBANG_POINTS_BONUS;
	public static int ALT_PCBANG_POINTS_DELAY;
	public static int ALT_PCBANG_POINTS_MIN_LVL;
	public static TIntSet ALT_ALLOWED_MULTISELLS_IN_PCBANG = new TIntHashSet();

	public static int ALT_PCBANG_POINTS_MAX_CODE_ENTER_ATTEMPTS;
	public static int ALT_PCBANG_POINTS_BAN_TIME;

	public static boolean ALT_DEBUG_ENABLED;
	public static boolean ALT_DEBUG_PVP_ENABLED;
	public static boolean ALT_DEBUG_PVP_DUEL_ONLY;
	public static boolean ALT_DEBUG_PVE_ENABLED;
	public static boolean ALT_LOG_NOTDONE_SKILLS;
	public static boolean ALT_LOG_NOTDONE_ONLY_FOR_LEARN_SKILLS;

	/** Thread pools size */
	public static int SCHEDULED_THREAD_POOL_SIZE;
	public static int EXECUTOR_THREAD_POOL_SIZE;

	/** Network settings */
	public static SelectorConfig SELECTOR_CONFIG = new SelectorConfig();

	public static boolean AUTO_LOOT;
	public static boolean AUTO_LOOT_HERBS;
	public static boolean AUTO_LOOT_ONLY_ADENA;
	public static boolean AUTO_LOOT_INDIVIDUAL;
	public static boolean AUTO_LOOT_FROM_RAIDS;
	public static List<Integer> AUTO_LOOT_ITEM_ID_LIST = new ArrayList<Integer>();

	/** Auto-loot for/from players with karma also? */
	public static boolean AUTO_LOOT_PK;

	/** Character name template */
	public static String CNAME_TEMPLATE;

	public static int MAX_CHARACTERS_NUMBER_PER_ACCOUNT;

	public static int CNAME_MAXLEN = 32;

	/** Clan name template */
	public static String CLAN_NAME_TEMPLATE;
	public static String APASSWD_TEMPLATE;

	/** Clan title template */
	public static String CLAN_TITLE_TEMPLATE;

	/** Ally name template */
	public static String ALLY_NAME_TEMPLATE;

	/** Global chat state */
	public static boolean GLOBAL_SHOUT;
	public static boolean GLOBAL_TRADE_CHAT;
	public static int CHAT_RANGE;
	public static int SHOUT_SQUARE_OFFSET;

	public static boolean ALLOW_WORLD_CHAT;
	public static int WORLD_CHAT_POINTS_PER_DAY;
	public static int WORLD_CHAT_POINTS_PER_DAY_PA;

	public static boolean BAN_FOR_CFG_USAGE;

	public static boolean ALLOW_TOTAL_ONLINE;
	public static boolean ALLOW_ONLINE_PARSE;
	public static int FIRST_UPDATE;
	public static int DELAY_UPDATE;

	public static int EXCELLENT_SHIELD_BLOCK_CHANCE;
	public static int EXCELLENT_SHIELD_BLOCK_RECEIVED_DAMAGE;

	/** For test servers - evrybody has admin rights */
	public static boolean EVERYBODY_HAS_ADMIN_RIGHTS;

	public static double ALT_RAID_RESPAWN_MULTIPLIER;

	public static int DEFAULT_RAID_MINIONS_RESPAWN_DELAY;

	public static boolean ALLOW_AUGMENTATION;
	public static boolean ALT_ALLOW_DROP_AUGMENTED;

	public static boolean ITEM_BROKER_ITEM_SEARCH;

	public static boolean ALT_GAME_UNREGISTER_RECIPE;

	/** Delay for announce SS period (in minutes) */
	public static int SS_ANNOUNCE_PERIOD;

	/** Petition manager */
	public static boolean PETITIONING_ALLOWED;
	public static int MAX_PETITIONS_PER_PLAYER;
	public static int MAX_PETITIONS_PENDING;

	/** Show mob stats/droplist to players? */
	public static boolean ALT_GAME_SHOW_DROPLIST;
	public static boolean ALLOW_NPC_SHIFTCLICK;
	public static boolean SHOW_TARGET_PLAYER_INVENTORY_ON_SHIFT_CLICK;
	public static boolean ALLOW_VOICED_COMMANDS;
	public static boolean ALLOW_AUTOHEAL_COMMANDS;

	public static int[] ALT_DISABLED_MULTISELL;
	public static int[] ALT_SHOP_PRICE_LIMITS;
	public static int[] ALT_SHOP_UNALLOWED_ITEMS;

	public static int[] ALT_ALLOWED_PET_POTIONS;

	public static double MIN_ABNORMAL_SUCCESS_RATE;
	public static double MAX_ABNORMAL_SUCCESS_RATE;
	public static boolean ALT_SAVE_UNSAVEABLE;
	public static int ALT_SAVE_EFFECTS_REMAINING_TIME;
	public static boolean ALT_SHOW_REUSE_MSG;
	public static boolean ALT_DELETE_SA_BUFFS;
	public static int SKILLS_CAST_TIME_MIN;

	/** Титул при создании чара */
	public static boolean CHAR_TITLE;
	public static String ADD_CHAR_TITLE;

	/** Таймаут на использование social action */
	public static boolean ALT_SOCIAL_ACTION_REUSE;

	/** Отключение книг для изучения скилов */
	public static Set<AcquireType> DISABLED_SPELLBOOKS_FOR_ACQUIRE_TYPES;

	/** Alternative gameing - loss of XP on death */
	public static boolean ALT_GAME_DELEVEL;
	public static boolean ALLOW_DELEVEL_COMMAND;

	/** Разрешать ли на арене бои за опыт */
	public static boolean ALT_ARENA_EXP;

	public static boolean ENABLE_SUB_CLASSES;
	public static int ALT_GAME_LEVEL_TO_GET_SUBCLASS;
	public static int ALT_MAX_LEVEL;
	public static int ALT_MAX_SUB_LEVEL;
	public static boolean ALT_NO_LASTHIT;
	public static boolean ALT_KAMALOKA_NIGHTMARES_PREMIUM_ONLY;
	public static boolean ALT_PET_HEAL_BATTLE_ONLY;

	public static int ALT_BUFF_LIMIT;

	public static int MULTISELL_SIZE;

	public static boolean SERVICES_CHANGE_NICK_ENABLED;
	public static int SERVICES_CHANGE_NICK_PRICE;
	public static int SERVICES_CHANGE_NICK_ITEM;
	public static boolean ALLOW_CHANGE_PASSWORD_COMMAND;
	public static boolean ALLOW_CHANGE_PHONE_NUMBER_COMMAND;
	public static boolean FORCIBLY_SPECIFY_PHONE_NUMBER;

	public static boolean SERVICES_CHANGE_CLAN_NAME_ENABLED;
	public static int SERVICES_CHANGE_CLAN_NAME_PRICE;
	public static int SERVICES_CHANGE_CLAN_NAME_ITEM;

	public static boolean SERVICES_CHANGE_PET_NAME_ENABLED;
	public static int SERVICES_CHANGE_PET_NAME_PRICE;
	public static int SERVICES_CHANGE_PET_NAME_ITEM;

	public static boolean SERVICES_EXCHANGE_BABY_PET_ENABLED;
	public static int SERVICES_EXCHANGE_BABY_PET_PRICE;
	public static int SERVICES_EXCHANGE_BABY_PET_ITEM;

	public static boolean SERVICES_CHANGE_SEX_ENABLED;
	public static int SERVICES_CHANGE_SEX_PRICE;
	public static int SERVICES_CHANGE_SEX_ITEM;

	public static boolean SERVICES_CHANGE_BASE_ENABLED;
	public static int SERVICES_CHANGE_BASE_PRICE;
	public static int SERVICES_CHANGE_BASE_ITEM;

	public static boolean SERVICES_SEPARATE_SUB_ENABLED;
	public static int SERVICES_SEPARATE_SUB_PRICE;
	public static int SERVICES_SEPARATE_SUB_ITEM;

	public static boolean SERVICES_CHANGE_NICK_COLOR_ENABLED;
	public static int SERVICES_CHANGE_NICK_COLOR_PRICE;
	public static int SERVICES_CHANGE_NICK_COLOR_ITEM;
	public static String[] SERVICES_CHANGE_NICK_COLOR_LIST;

	public static boolean SERVICES_BASH_ENABLED;
	public static boolean SERVICES_BASH_SKIP_DOWNLOAD;
	public static int SERVICES_BASH_RELOAD_TIME;

	public static boolean SERVICES_NOBLESS_SELL_ENABLED;
	public static int SERVICES_NOBLESS_SELL_PRICE;
	public static int SERVICES_NOBLESS_SELL_ITEM;

	public static boolean SERVICES_EXPAND_INVENTORY_ENABLED;
	public static int SERVICES_EXPAND_INVENTORY_PRICE;
	public static int SERVICES_EXPAND_INVENTORY_ITEM;
	public static int SERVICES_EXPAND_INVENTORY_MAX;

	public static boolean SERVICES_EXPAND_WAREHOUSE_ENABLED;
	public static int SERVICES_EXPAND_WAREHOUSE_PRICE;
	public static int SERVICES_EXPAND_WAREHOUSE_ITEM;

	public static boolean SERVICES_EXPAND_CWH_ENABLED;
	public static int SERVICES_EXPAND_CWH_PRICE;
	public static int SERVICES_EXPAND_CWH_ITEM;

	public static boolean SERVICES_OFFLINE_TRADE_ALLOW;
	public static int SERVICES_OFFLINE_TRADE_ALLOW_ZONE;
	public static int SERVICES_OFFLINE_TRADE_MIN_LEVEL;
	public static int SERVICES_OFFLINE_TRADE_NAME_COLOR;
	public static AbnormalEffect SERVICES_OFFLINE_TRADE_ABNORMAL_EFFECT;
	public static int SERVICES_OFFLINE_TRADE_PRICE;
	public static int SERVICES_OFFLINE_TRADE_PRICE_ITEM;
	public static int SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK;
	public static boolean SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART;

	public static boolean SERVICES_GIRAN_HARBOR_ENABLED;

	public static boolean SERVICES_PARNASSUS_ENABLED;
	public static boolean SERVICES_PARNASSUS_NOTAX;

	public static long SERVICES_PARNASSUS_PRICE;

	public static boolean SERVICES_RIDE_HIRE_ENABLED;

	public static boolean SERVICES_ALLOW_LOTTERY;
	public static int SERVICES_LOTTERY_PRIZE;
	public static int SERVICES_ALT_LOTTERY_PRICE;
	public static int SERVICES_LOTTERY_TICKET_PRICE;
	public static double SERVICES_LOTTERY_5_NUMBER_RATE;
	public static double SERVICES_LOTTERY_4_NUMBER_RATE;
	public static double SERVICES_LOTTERY_3_NUMBER_RATE;
	public static int SERVICES_LOTTERY_2_AND_1_NUMBER_PRIZE;

	public static boolean SERVICES_ALLOW_ROULETTE;
	public static long SERVICES_ROULETTE_MIN_BET;
	public static long SERVICES_ROULETTE_MAX_BET;

	public static boolean ALT_ALLOW_OTHERS_WITHDRAW_FROM_CLAN_WAREHOUSE;
	public static boolean ALT_ALLOW_CLAN_COMMAND_ONLY_FOR_CLAN_LEADER;

	public static boolean ALLOW_IP_LOCK;
	public static boolean AUTO_LOCK_IP_ON_LOGIN;
	public static boolean ALLOW_HWID_LOCK;
	public static boolean AUTO_LOCK_HWID_ON_LOGIN;
	public static int HWID_LOCK_MASK;
	/** Olympiad Compitition Starting time */
	public static int ALT_OLY_START_TIME;
	/** Olympiad Compition Min */
	public static int ALT_OLY_MIN;
	/** Olympaid Comptetition Period */
	public static long ALT_OLY_CPERIOD;
	/** Olympaid Weekly Period */
	public static long ALT_OLY_WPERIOD;
	/** Olympaid Validation Period */
	public static long ALT_OLY_VPERIOD;
	public static boolean CLASSED_GAMES_ENABLED;
	public static long OLYMPIAD_REGISTRATION_DELAY;

	public static boolean ENABLE_OLYMPIAD;
	public static boolean ENABLE_OLYMPIAD_SPECTATING;
	public static SchedulingPattern OLYMIAD_END_PERIOD_TIME;
	public static SchedulingPattern OLYMPIAD_START_TIME_1x1;
	public static SchedulingPattern OLYMPIAD_START_TIME_3x3;

	public static int OLYMPIAD_MIN_LEVEL;

	public static int CLASS_GAME_MIN;
	public static int NONCLASS_GAME_MIN;

	public static int OLYMPIAD_GAME_MAX_LIMIT;

	public static int ALT_OLY_REG_DISPLAY;
	public static int ALT_OLY_BATTLE_REWARD_ITEM;
	public static int ALT_OLY_COMP_RITEM;
	public static int ALT_OLY_GP_PER_POINT;
	public static int ALT_OLY_HERO_POINTS;
	public static int ALT_OLY_RANK1_POINTS;
	public static int ALT_OLY_RANK2_POINTS;
	public static int ALT_OLY_RANK3_POINTS;
	public static int ALT_OLY_RANK4_POINTS;
	public static int ALT_OLY_RANK5_POINTS;
	public static int OLYMPIAD_ALL_LOOSE_POINTS_BONUS;
	public static int OLYMPIAD_1_OR_MORE_WIN_POINTS_BONUS;
	public static int OLYMPIAD_STADIAS_COUNT;
	public static int OLYMPIAD_BATTLES_FOR_REWARD;
	public static int OLYMPIAD_POINTS_DEFAULT;
	public static int OLYMPIAD_POINTS_WEEKLY;
	public static boolean OLYMPIAD_OLDSTYLE_STAT;
	public static boolean OLYMPIAD_CANATTACK_BUFFER;
	public static int OLYMPIAD_BEGINIG_DELAY;

	public static long NONOWNER_ITEM_PICKUP_DELAY;

	/** Logging Chat Window */
	public static boolean LOG_CHAT;
	public static boolean TURN_LOG_SYSTEM;

	public static Map<Integer, PlayerAccess> gmlist = new HashMap<Integer, PlayerAccess>();

	/** Rate control */
	public static double[] RATE_XP_BY_LVL;
	public static double[] RATE_SP_BY_LVL;
	public static double RATE_ELEMENTAL_XP;
	public static double RATE_MAGIC_LAMP_XP;
	public static int MAX_DROP_ITEMS_FROM_ONE_GROUP;
	public static double[] RATE_DROP_ADENA_BY_LVL;
	public static double[] RATE_DROP_ITEMS_BY_LVL;
	public static double DROP_CHANCE_MODIFIER;
	public static double DROP_COUNT_MODIFIER;
	public static double[] RATE_DROP_SPOIL_BY_LVL;
	public static double SPOIL_CHANCE_MODIFIER;
	public static double SPOIL_COUNT_MODIFIER;
	public static double RATE_QUESTS_REWARD;
	public static boolean RATE_QUEST_REWARD_EXP_SP_ADENA_ONLY;
	public static double QUESTS_REWARD_LIMIT_MODIFIER;

	public static boolean EX_USE_QUEST_REWARD_PENALTY_PER;
	public static int EX_F2P_QUEST_REWARD_PENALTY_PER;
	public static TIntSet EX_F2P_QUEST_REWARD_PENALTY_QUESTS;

	public static double RATE_QUESTS_DROP;
	public static double RATE_CLAN_REP_SCORE;
	public static int RATE_CLAN_REP_SCORE_MAX_AFFECTED;
	public static double RATE_DROP_COMMON_ITEMS;
	public static double RATE_XP_RAIDBOSS_MODIFIER;
	public static double RATE_SP_RAIDBOSS_MODIFIER;
	public static double RATE_DROP_ITEMS_RAIDBOSS;
	public static double DROP_CHANCE_MODIFIER_RAIDBOSS;
	public static double DROP_COUNT_MODIFIER_RAIDBOSS;
	public static double RATE_DROP_ITEMS_BOSS;
	public static double DROP_CHANCE_MODIFIER_BOSS;
	public static double DROP_COUNT_MODIFIER_BOSS;
	public static TIntSet DISABLE_DROP_EXCEPT_ITEM_IDS;
	public static int[] NO_RATE_ITEMS;
	public static boolean NO_RATE_EQUIPMENT;
	public static boolean NO_RATE_KEY_MATERIAL;
	public static boolean NO_RATE_RECIPES;
	public static double RATE_DROP_SIEGE_GUARD;
	public static double RATE_MANOR;
	public static int RATE_FISH_DROP_COUNT;
	public static int PA_RATE_IN_PARTY_MODE;

	public static double RATE_MOB_SPAWN;
	public static int RATE_MOB_SPAWN_MIN_LEVEL;
	public static int RATE_MOB_SPAWN_MAX_LEVEL;

	/** Player Drop Rate control */
	public static boolean KARMA_DROP_GM;
	public static boolean KARMA_NEEDED_TO_DROP;

	public static int RATE_KARMA_LOST_STATIC;

	public static int KARMA_RANDOM_DROP_LOCATION_LIMIT;

	public static double KARMA_DROPCHANCE_1;
	public static double KARMA_DROPCHANCE_2;
	public static double KARMA_DROPCHANCE_3;
	public static int KARMA_ITEMSDROP_1;
	public static int KARMA_ITEMSDROP_2;
	public static int KARMA_ITEMSDROP_3;
	public static double KARMA_DROPCHANCE_MOD;
	public static double NORMAL_DROPCHANCE_BASE;
	public static int DROPCHANCE_EQUIPMENT;
	public static int DROPCHANCE_EQUIPPED_WEAPON;
	public static int DROPCHANCE_ITEM;

	public static int AUTODESTROY_ITEM_AFTER;
	public static int AUTODESTROY_PLAYER_ITEM_AFTER;

	public static int CHARACTER_DELETE_AFTER_HOURS;

	public static int PURGE_BYPASS_TASK_FREQUENCY;

	/** Datapack root directory */
	public static File DATAPACK_ROOT;
	public static File GEODATA_ROOT;

	public static double BUFFTIME_MODIFIER;
	public static int[] BUFFTIME_MODIFIER_SKILLS;
	public static double CLANHALL_BUFFTIME_MODIFIER;
	public static double SONGDANCETIME_MODIFIER;

	public static double MAXLOAD_MODIFIER;
	public static double GATEKEEPER_MODIFIER;

	public static int GATEKEEPER_FREE;

	public static double ALT_CHAMPION_CHANCE1;
	public static double ALT_CHAMPION_CHANCE2;
	public static boolean ALT_CHAMPION_CAN_BE_AGGRO;
	public static boolean ALT_CHAMPION_CAN_BE_SOCIAL;
	public static int ALT_CHAMPION_MIN_LEVEL;
	public static int ALT_CHAMPION_TOP_LEVEL;

	public static boolean ALLOW_DISCARDITEM;
	public static boolean ALLOW_MAIL;
	public static boolean ALLOW_WAREHOUSE;
	public static boolean ALLOW_WATER;
	public static boolean ALLOW_NOBLE_TP_TO_ALL;
	public static boolean ALLOW_ITEMS_REFUND;

	/** Pets */
	public static int SWIMING_SPEED;

	/** protocol revision */
	public static List<Integer> AVAILABLE_PROTOCOL_REVISIONS;

	/** random animation interval */
	public static int MIN_NPC_ANIMATION;
	public static int MAX_NPC_ANIMATION;

	public static boolean USE_CLIENT_LANG;
	public static boolean CAN_SELECT_LANGUAGE;
	public static Language DEFAULT_LANG;

	/** Время запланированного на определенное время суток рестарта */
	public static String RESTART_AT_TIME;

	public static boolean RETAIL_MULTISELL_ENCHANT_TRANSFER;

	public static int REQUEST_ID;
	public static String EXTERNAL_HOSTNAME;
	public static int PORT_GAME;

	// Security
	public static boolean EX_SECOND_AUTH_ENABLED;
	public static int EX_SECOND_AUTH_MAX_ATTEMPTS;
	public static int EX_SECOND_AUTH_BAN_TIME;

	public static boolean EX_USE_PREMIUM_HENNA_SLOT;
	public static boolean EX_USE_AUTO_SOUL_SHOT;
	public static boolean EX_USE_TO_DO_LIST;
	public static boolean EX_USE_PLEDGE_BONUS;
	public static boolean EX_USE_PRIME_SHOP;

	public static boolean ALT_EASY_RECIPES;

	public static boolean ALT_USE_TRANSFORM_IN_EPIC_ZONE;

	public static boolean ALT_ANNONCE_RAID_BOSSES_REVIVAL;

	public static boolean SPAWN_VITAMIN_MANAGER;

	public static boolean ALLOW_EVENT_GATEKEEPER;

	/** Inventory slots limits */
	public static int INVENTORY_MAXIMUM_NO_DWARF;
	public static int INVENTORY_MAXIMUM_DWARF;
	public static int INVENTORY_MAXIMUM_GM;
	public static int QUEST_INVENTORY_MAXIMUM;

	/** Warehouse slots limits */
	public static int WAREHOUSE_SLOTS_NO_DWARF;
	public static int WAREHOUSE_SLOTS_DWARF;
	public static int WAREHOUSE_SLOTS_CLAN;

	public static int FREIGHT_SLOTS;

	/** Spoil Rates */
	public static double BASE_SPOIL_RATE;
	public static double MINIMUM_SPOIL_RATE;
	public static boolean SHOW_HTML_WELCOME;

	/** Manor Config */
	public static double MANOR_SOWING_BASIC_SUCCESS;
	public static double MANOR_SOWING_ALT_BASIC_SUCCESS;
	public static double MANOR_HARVESTING_BASIC_SUCCESS;
	public static int MANOR_DIFF_PLAYER_TARGET;
	public static double MANOR_DIFF_PLAYER_TARGET_PENALTY;
	public static int MANOR_DIFF_SEED_TARGET;
	public static double MANOR_DIFF_SEED_TARGET_PENALTY;

	/** Karma System Variables */
	public static int KARMA_MIN_KARMA;
	public static int KARMA_RATE_KARMA_LOST;
	public static int KARMA_LOST_BASE;
	public static int KARMA_PENALTY_START_KARMA;
	public static int KARMA_PENALTY_DURATION_DEFAULT;
	public static double KARMA_PENALTY_DURATION_INCREASE;
	public static int KARMA_DOWN_TIME_MULTIPLE;
	public static int KARMA_CRIMINAL_DURATION_MULTIPLE;

	public static int MIN_PK_TO_ITEMS_DROP;
	public static boolean DROP_ITEMS_ON_DIE;
	public static boolean DROP_ITEMS_AUGMENTED;

	public static List<Integer> KARMA_LIST_NONDROPPABLE_ITEMS = new ArrayList<Integer>();
	public static List<RaidGlobalDrop> RAID_GLOBAL_DROP = new ArrayList<RaidGlobalDrop>();

	public static List<Integer> LIST_OF_SELLABLE_ITEMS = new ArrayList<Integer>();
	public static List<Integer> LIST_OF_TRABLE_ITEMS = new ArrayList<Integer>();

	public static int PVP_TIME;

	/** Karma Punishment */
	public static boolean ALT_GAME_KARMA_PLAYER_CAN_SHOP;

	public static boolean REGEN_SIT_WAIT;

	public static double RATE_RAID_REGEN;
	public static double RATE_RAID_DEFENSE;
	public static double RATE_RAID_ATTACK;
	public static double RATE_EPIC_DEFENSE;
	public static double RATE_EPIC_ATTACK;
	public static int RAID_MAX_LEVEL_DIFF;
	public static boolean PARALIZE_ON_RAID_DIFF;

	public static int STARTING_LVL;
	public static long STARTING_SP;

	/** Deep Blue Mobs' Drop Rules Enabled */
	public static boolean DEEPBLUE_DROP_RULES;
	public static int DEEPBLUE_DROP_MAXDIFF;
	public static int DEEPBLUE_DROP_RAID_MAXDIFF;
	public static boolean UNSTUCK_SKILL;

	/** Percent CP is restore on respawn */
	public static double RESPAWN_RESTORE_CP;
	/** Percent HP is restore on respawn */
	public static double RESPAWN_RESTORE_HP;
	/** Percent MP is restore on respawn */
	public static double RESPAWN_RESTORE_MP;

	/** Maximum number of available slots for pvt stores (sell/buy) - Dwarves */
	public static int MAX_PVTSTORE_SLOTS_DWARF;
	/** Maximum number of available slots for pvt stores (sell/buy) - Others */
	public static int MAX_PVTSTORE_SLOTS_OTHER;
	public static int MAX_PVTCRAFT_SLOTS;

	public static boolean SENDSTATUS_TRADE_JUST_OFFLINE;
	public static double SENDSTATUS_TRADE_MOD;

	public static boolean ALLOW_CH_DOOR_OPEN_ON_CLICK;
	public static boolean ALT_CH_SIMPLE_DIALOG;
	public static boolean ALT_CH_UNLIM_MP;
	public static boolean ALT_NO_FAME_FOR_DEAD;

	public static int CH_BID_GRADE1_MINCLANLEVEL;
	public static int CH_BID_GRADE1_MINCLANMEMBERS;
	public static int CH_BID_GRADE1_MINCLANMEMBERSLEVEL;
	public static int CH_BID_GRADE2_MINCLANLEVEL;
	public static int CH_BID_GRADE2_MINCLANMEMBERS;
	public static int CH_BID_GRADE2_MINCLANMEMBERSLEVEL;
	public static int CH_BID_GRADE3_MINCLANLEVEL;
	public static int CH_BID_GRADE3_MINCLANMEMBERS;
	public static int CH_BID_GRADE3_MINCLANMEMBERSLEVEL;
	public static double RESIDENCE_LEASE_FUNC_MULTIPLIER;
	public static double RESIDENCE_LEASE_MULTIPLIER;

	public static boolean ANNOUNCE_MAMMON_SPAWN;

	public static int GM_NAME_COLOUR;
	public static boolean GM_HERO_AURA;
	public static int NORMAL_NAME_COLOUR;
	public static int CLANLEADER_NAME_COLOUR;

	/** AI */
	public static int AI_TASK_MANAGER_COUNT;
	public static long AI_TASK_ATTACK_DELAY;
	public static long AI_TASK_ACTIVE_DELAY;
	public static boolean BLOCK_ACTIVE_TASKS;
	public static boolean ALWAYS_TELEPORT_HOME;
	public static boolean ALWAYS_TELEPORT_HOME_RB;
	public static boolean RND_WALK;
	public static int RND_WALK_RATE;
	public static int RND_ANIMATION_RATE;

	public static int AGGRO_CHECK_INTERVAL;
	public static long NONAGGRO_TIME_ONTELEPORT;
	public static long NONPVP_TIME_ONTELEPORT;

	/** Maximum range mobs can randomly go from spawn point */
	public static int MAX_DRIFT_RANGE;

	/** Maximum range mobs can pursue agressor from spawn point */
	public static int MAX_PURSUE_RANGE;
	public static int MAX_PURSUE_UNDERGROUND_RANGE;
	public static int MAX_PURSUE_RANGE_RAID;

	public static boolean ALLOW_DEATH_PENALTY;
	public static int ALT_DEATH_PENALTY_CHANCE;
	public static int ALT_DEATH_PENALTY_EXPERIENCE_PENALTY;
	public static int ALT_DEATH_PENALTY_KARMA_PENALTY;

	public static boolean HIDE_GM_STATUS;
	public static boolean SHOW_GM_LOGIN;
	public static boolean SAVE_GM_EFFECTS; // Silence, gmspeed, etc...

	public static boolean AUTO_LEARN_SKILLS;

	public static int MOVE_PACKET_DELAY;
	public static int ATTACK_PACKET_DELAY;

	public static boolean DAMAGE_FROM_FALLING;

	/** Community Board */
	public static boolean BBS_ENABLED;
	public static String BBS_DEFAULT_PAGE;
	public static String BBS_COPYRIGHT;
	public static boolean BBS_WAREHOUSE_ENABLED;
	public static boolean BBS_SELL_ITEMS_ENABLED;
	public static boolean BBS_AUGMENTATION_ENABLED;

	/** Wedding Options */
	public static boolean ALLOW_WEDDING;
	public static int WEDDING_PRICE;
	public static boolean WEDDING_PUNISH_INFIDELITY;
	public static boolean WEDDING_TELEPORT;
	public static int WEDDING_TELEPORT_PRICE;
	public static int WEDDING_TELEPORT_INTERVAL;
	public static boolean WEDDING_SAMESEX;
	public static boolean WEDDING_FORMALWEAR;
	public static int WEDDING_DIVORCE_COSTS;

	public static int FOLLOW_RANGE;

	public static boolean ALT_ITEM_AUCTION_ENABLED;
	public static boolean ALT_CUSTOM_ITEM_AUCTION_ENABLED;
	public static boolean ALT_ITEM_AUCTION_CAN_REBID;
	public static boolean ALT_ITEM_AUCTION_START_ANNOUNCE;
	public static long ALT_ITEM_AUCTION_MAX_BID;
	public static int ALT_ITEM_AUCTION_MAX_CANCEL_TIME_IN_MILLIS;

	public static boolean ALT_ENABLE_BLOCK_CHECKER_EVENT;
	public static int ALT_MIN_BLOCK_CHECKER_TEAM_MEMBERS;
	public static double ALT_RATE_COINS_REWARD_BLOCK_CHECKER;
	public static boolean ALT_HBCE_FAIR_PLAY;
	public static int ALT_PET_INVENTORY_LIMIT;

	/** limits of stats **/
	public static int LIM_PATK;
	public static int LIM_MATK;
	public static int LIM_PDEF;
	public static int LIM_MDEF;
	public static int LIM_MATK_SPD;
	public static int LIM_PATK_SPD;
	public static int LIM_CRIT_DAM;
	public static int LIM_CRIT;
	public static int LIM_MCRIT;
	public static int LIM_ACCURACY;
	public static int LIM_EVASION;
	public static int LIM_MOVE;
	public static int LIM_FAME;
	public static int LIM_RAID_POINTS;
	public static int HP_LIMIT;
	public static int MP_LIMIT;
	public static int CP_LIMIT;
	public static int LIM_CRAFT_POINTS;

	public static double PLAYER_P_ATK_MODIFIER;
	public static double PLAYER_M_ATK_MODIFIER;

	public static double ALT_NPC_PATK_MODIFIER;
	public static double ALT_NPC_MATK_MODIFIER;
	public static double ALT_NPC_MAXHP_MODIFIER;
	public static double ALT_NPC_MAXMP_MODIFIER;

	public static int FESTIVAL_MIN_PARTY_SIZE;
	public static double FESTIVAL_RATE_PRICE;

	/** Dimensional Rift Config **/
	public static int RIFT_MIN_PARTY_SIZE;
	public static int RIFT_SPAWN_DELAY; // Time in ms the party has to wait until the mobs spawn
	public static int RIFT_MAX_JUMPS;
	public static int RIFT_AUTO_JUMPS_TIME;
	public static int RIFT_AUTO_JUMPS_TIME_RAND;
	public static int RIFT_ENTER_COST_RECRUIT;
	public static int RIFT_ENTER_COST_SOLDIER;
	public static int RIFT_ENTER_COST_OFFICER;
	public static int RIFT_ENTER_COST_CAPTAIN;
	public static int RIFT_ENTER_COST_COMMANDER;
	public static int RIFT_ENTER_COST_HERO;

	public static boolean ALLOW_TALK_WHILE_SITTING;

	public static int MAXIMUM_MEMBERS_IN_PARTY;
	public static boolean PARTY_LEADER_ONLY_CAN_INVITE;

	/** Разрешены ли клановые скилы? **/
	public static boolean ALLOW_CLANSKILLS;

	/**
	 * Разрешено ли изучение скилов трансформации и саб классов без наличия
	 * выполненного квеста
	 */
	public static boolean ALLOW_LEARN_TRANS_SKILLS_WO_QUEST;

	/** Allow Manor system */
	public static boolean ALLOW_MANOR;

	/** Manor Refresh Starting time */
	public static int MANOR_REFRESH_TIME;

	/** Manor Refresh Min */
	public static int MANOR_REFRESH_MIN;

	/** Manor Next Period Approve Starting time */
	public static int MANOR_APPROVE_TIME;

	/** Manor Next Period Approve Min */
	public static int MANOR_APPROVE_MIN;

	/** Manor Maintenance Time */
	public static int MANOR_MAINTENANCE_PERIOD;

	/** Master Yogi event enchant config */
	public static int ENCHANT_CHANCE_MASTER_YOGI_STAFF;
	public static int ENCHANT_MAX_MASTER_YOGI_STAFF;
	public static int SAFE_ENCHANT_MASTER_YOGI_STAFF;

	// reflect configs
	public static int REFLECT_MIN_RANGE;
	public static double REFLECT_AND_BLOCK_DAMAGE_CHANCE_CAP;
	public static double REFLECT_AND_BLOCK_PSKILL_DAMAGE_CHANCE_CAP;
	public static double REFLECT_AND_BLOCK_MSKILL_DAMAGE_CHANCE_CAP;
	public static double REFLECT_DAMAGE_PERCENT_CAP;
	public static double REFLECT_BOW_DAMAGE_PERCENT_CAP;
	public static double REFLECT_PSKILL_DAMAGE_PERCENT_CAP;
	public static double REFLECT_MSKILL_DAMAGE_PERCENT_CAP;

	public static boolean SERVICES_NO_TRADE_ONLY_OFFLINE;
	public static double SERVICES_TRADE_TAX;
	public static double SERVICES_OFFSHORE_TRADE_TAX;
	public static boolean SERVICES_OFFSHORE_NO_CASTLE_TAX;
	public static boolean SERVICES_TRADE_TAX_ONLY_OFFLINE;
	public static boolean SERVICES_TRADE_ONLY_FAR;
	public static int SERVICES_TRADE_RADIUS;
	public static int SERVICES_TRADE_MIN_LEVEL;

	public static boolean SERVICES_ENABLE_NO_CARRIER;
	public static int SERVICES_NO_CARRIER_DEFAULT_TIME;
	public static int SERVICES_NO_CARRIER_MAX_TIME;
	public static int SERVICES_NO_CARRIER_MIN_TIME;

	public static boolean ALT_SHOW_SERVER_TIME;

	/** Geodata config */
	public static int GEO_X_FIRST, GEO_Y_FIRST, GEO_X_LAST, GEO_Y_LAST;
	public static boolean ALLOW_GEODATA;
	public static boolean ALLOW_FALL_FROM_WALLS;
	public static boolean ALLOW_KEYBOARD_MOVE;
	public static boolean COMPACT_GEO;
	public static int MAX_Z_DIFF;
	public static int MIN_LAYER_HEIGHT;
	public static int REGION_EDGE_MAX_Z_DIFF;

	/** Geodata (Pathfind) config */
	public static int PATHFIND_BOOST;
	public static int PATHFIND_MAP_MUL;
	public static boolean PATHFIND_DIAGONAL;
	public static boolean PATH_CLEAN;
	public static int PATHFIND_MAX_Z_DIFF;
	public static long PATHFIND_MAX_TIME;
	public static String PATHFIND_BUFFERS;
	public static int NPC_PATH_FIND_MAX_HEIGHT;
	public static int PLAYABLE_PATH_FIND_MAX_HEIGHT;

	public static boolean DEBUG;

	public static int WEAR_DELAY;
	public static boolean ALLOW_FAKE_PLAYERS;
	public static int FAKE_PLAYERS_PERCENT;

	public static boolean DISABLE_CRYSTALIZATION_ITEMS;

	public static int[] SERVICES_ENCHANT_VALUE;
	public static int[] SERVICES_ENCHANT_COAST;
	public static int[] SERVICES_ENCHANT_RAID_VALUE;
	public static int[] SERVICES_ENCHANT_RAID_COAST;

	public static boolean GOODS_INVENTORY_ENABLED = false;
	public static boolean EX_NEW_PETITION_SYSTEM;
	public static boolean EX_JAPAN_MINIGAME;
	public static boolean EX_LECTURE_MARK;

	public static boolean AUTH_SERVER_GM_ONLY;
	public static boolean AUTH_SERVER_BRACKETS;
	public static boolean AUTH_SERVER_IS_PVP;
	public static int AUTH_SERVER_AGE_LIMIT;
	public static int AUTH_SERVER_SERVER_TYPE;

	public static boolean ALLOW_MONSTER_RACE;
	public static boolean ONLY_ONE_SIEGE_PER_CLAN;
	public static double SPECIAL_CLASS_BOW_CROSS_BOW_PENALTY;
	public static boolean NEED_QUEST_FOR_PROOF;

	public static boolean ALLOW_USE_DOORMANS_IN_SIEGE_BY_OWNERS;

	public static boolean DISABLE_VAMPIRIC_VS_MOB_ON_PVP;

	public static boolean NPC_RANDOM_ENCHANT;

	public static boolean ENABLE_PARTY_SEARCH;
	public static boolean MENTOR_ONLY_PA;

	// pvp manager
	public static boolean ALLOW_PVP_REWARD;
	public static boolean PVP_REWARD_SEND_SUCC_NOTIF;
	public static int[] PVP_REWARD_REWARD_IDS;
	public static long[] PVP_REWARD_COUNTS;
	public static boolean PVP_REWARD_RANDOM_ONE;
	public static int PVP_REWARD_DELAY_ONE_KILL;
	public static int PVP_REWARD_MIN_PL_PROFF;
	public static int PVP_REWARD_MIN_PL_UPTIME_MINUTE;
	public static int PVP_REWARD_MIN_PL_LEVEL;
	public static boolean PVP_REWARD_PK_GIVE;
	public static boolean PVP_REWARD_ON_EVENT_GIVE;
	public static boolean PVP_REWARD_ONLY_BATTLE_ZONE;
	public static boolean PVP_REWARD_ONLY_NOBLE_GIVE;
	public static boolean PVP_REWARD_SAME_PARTY_GIVE;
	public static boolean PVP_REWARD_SAME_CLAN_GIVE;
	public static boolean PVP_REWARD_SAME_ALLY_GIVE;
	public static boolean PVP_REWARD_SAME_HWID_GIVE;
	public static boolean PVP_REWARD_SAME_IP_GIVE;
	public static boolean PVP_REWARD_SPECIAL_ANTI_TWINK_TIMER;
	public static int PVP_REWARD_HR_NEW_CHAR_BEFORE_GET_ITEM;
	public static boolean PVP_REWARD_CHECK_EQUIP;
	public static int PVP_REWARD_WEAPON_GRADE_TO_CHECK;
	public static boolean PVP_REWARD_LOG_KILLS;
	public static boolean DISALLOW_MSG_TO_PL;

	public static int ALL_CHAT_USE_MIN_LEVEL;
	public static int ALL_CHAT_USE_MIN_LEVEL_WITHOUT_PA;
	public static int ALL_CHAT_USE_DELAY;
	public static int SHOUT_CHAT_USE_MIN_LEVEL;
	public static int SHOUT_CHAT_USE_MIN_LEVEL_WITHOUT_PA;
	public static int SHOUT_CHAT_USE_DELAY;
	public static int WORLD_CHAT_USE_MIN_LEVEL;
	public static int WORLD_CHAT_USE_MIN_LEVEL_WITHOUT_PA;
	public static int WORLD_CHAT_USE_DELAY;
	public static int TRADE_CHAT_USE_MIN_LEVEL;
	public static int TRADE_CHAT_USE_MIN_LEVEL_WITHOUT_PA;
	public static int TRADE_CHAT_USE_DELAY;
	public static int HERO_CHAT_USE_MIN_LEVEL;
	public static int HERO_CHAT_USE_MIN_LEVEL_WITHOUT_PA;
	public static int HERO_CHAT_USE_DELAY;
	public static int PRIVATE_CHAT_USE_MIN_LEVEL;
	public static int PRIVATE_CHAT_USE_MIN_LEVEL_WITHOUT_PA;
	public static int PRIVATE_CHAT_USE_DELAY;
	public static int MAIL_USE_MIN_LEVEL;
	public static int MAIL_USE_MIN_LEVEL_WITHOUT_PA;
	public static int MAIL_USE_DELAY;

	public static int IM_PAYMENT_ITEM_ID;

	public static boolean ALT_SHOW_MONSTERS_LVL;
	public static boolean ALT_SHOW_MONSTERS_AGRESSION;

	public static int BEAUTY_SHOP_COIN_ITEM_ID;

	public static boolean ALT_TELEPORT_TO_TOWN_DURING_SIEGE;

	public static int ALT_CLAN_LEAVE_PENALTY_TIME;
	public static int ALT_CLAN_CREATE_PENALTY_TIME;

	public static int ALT_EXPELLED_MEMBER_PENALTY_TIME;
	public static int ALT_LEAVED_ALLY_PENALTY_TIME;
	public static int ALT_DISSOLVED_ALLY_PENALTY_TIME;

	public static boolean RAID_DROP_GLOBAL_ITEMS;
	public static int MIN_RAID_LEVEL_TO_DROP;

	public static int NPC_DIALOG_PLAYER_DELAY;

	public static double PHYSICAL_MIN_CHANCE_TO_HIT;
	public static double PHYSICAL_MAX_CHANCE_TO_HIT;

	public static double MAGIC_MIN_CHANCE_TO_HIT;
	public static double MAGIC_MAX_CHANCE_TO_HIT;

	public static boolean ENABLE_CRIT_DMG_REDUCTION_ON_MAGIC;

	public static double MAX_BLOW_RATE_ON_BEHIND;
	public static double MAX_BLOW_RATE_ON_FRONT_AND_SIDE;

	public static double BLOW_SKILL_CHANCE_MOD_ON_BEHIND;
	public static double BLOW_SKILL_CHANCE_MOD_ON_FRONT;

	public static double BLOW_SKILL_DEX_CHANCE_MOD;
	public static double NORMAL_SKILL_DEX_CHANCE_MOD;

	public static double CRIT_STUN_BREAK_CHANCE;

	public static boolean ENABLE_STUN_BREAK_ON_ATTACK;
	public static double CRIT_STUN_BREAK_CHANCE_ON_MAGICAL_SKILL;
	public static double NORMAL_STUN_BREAK_CHANCE_ON_MAGICAL_SKILL;
	public static double CRIT_STUN_BREAK_CHANCE_ON_PHYSICAL_SKILL;
	public static double NORMAL_STUN_BREAK_CHANCE_ON_PHYSICAL_SKILL;
	public static double CRIT_STUN_BREAK_CHANCE_ON_REGULAR_HIT;
	public static double NORMAL_STUN_BREAK_CHANCE_ON_REGULAR_HIT;

	public static double NORMAL_STUN_BREAK_CHANCE;

	public static String CLAN_DELETE_TIME;
	public static String CLAN_CHANGE_LEADER_TIME;
	public static int CLAN_MAX_LEVEL;
	public static int CLAN_KILLED_MOBS_TO_POINT;

	public static int CLAN_ATTENDANCE_REWARD_1;
	public static int CLAN_ATTENDANCE_REWARD_2;
	public static int CLAN_ATTENDANCE_REWARD_3;
	public static int CLAN_ATTENDANCE_REWARD_4;

	public static int CLAN_HUNTING_REWARD_1;
	public static int CLAN_HUNTING_REWARD_2;
	public static int CLAN_HUNTING_REWARD_3;
	public static int CLAN_HUNTING_REWARD_4;

	public static double CLAN_HUNTING_PROGRESS_RATE;

	public static int ALT_MUSIC_LIMIT;
	public static int ALT_DEBUFF_LIMIT;
	public static int ALT_TRIGGER_LIMIT;

	// Buffer Scheme NPC
	public static boolean NpcBuffer_VIP;
	public static int NpcBuffer_VIP_ALV;
	public static boolean NpcBuffer_EnableBuff;
	public static boolean NpcBuffer_EnableScheme;
	public static boolean NpcBuffer_EnableHeal;
	public static boolean NpcBuffer_EnableBuffs;
	public static boolean NpcBuffer_EnableResist;
	public static boolean NpcBuffer_EnableSong;
	public static boolean NpcBuffer_EnableDance;
	public static boolean NpcBuffer_EnableChant;
	public static boolean NpcBuffer_EnableOther;
	public static boolean NpcBuffer_EnableSpecial;
	public static boolean NpcBuffer_EnableCubic;
	public static boolean NpcBuffer_EnableCancel;
	public static boolean NpcBuffer_EnableBuffSet;
	public static boolean NpcBuffer_EnableBuffPK;
	public static boolean NpcBuffer_EnableFreeBuffs;
	public static boolean NpcBuffer_EnableTimeOut;
	public static int NpcBuffer_TimeOutTime;
	public static int NpcBuffer_MinLevel;
	public static int NpcBuffer_PriceCancel;
	public static int NpcBuffer_PriceHeal;
	public static int NpcBuffer_PriceBuffs;
	public static int NpcBuffer_PriceResist;
	public static int NpcBuffer_PriceSong;
	public static int NpcBuffer_PriceDance;
	public static int NpcBuffer_PriceChant;
	public static int NpcBuffer_PriceOther;
	public static int NpcBuffer_PriceSpecial;
	public static int NpcBuffer_PriceCubic;
	public static int NpcBuffer_PriceSet;
	public static int NpcBuffer_PriceScheme;
	public static int NpcBuffer_MaxScheme;
	public static boolean SCHEME_ALLOW_FLAG;
	public static boolean IS_DISABLED_IN_REFLECTION;

	public static int SPECIAL_ITEM_ID;
	public static long SPECIAL_ITEM_COUNT;
	public static double SPECIAL_ITEM_DROP_CHANCE;

	public static int ALT_DELEVEL_ON_DEATH_PENALTY_MIN_LEVEL;

	public static boolean ALT_PETS_NOT_STARVING;

	public static Set<Language> AVAILABLE_LANGUAGES;

	public static int MAX_ACTIVE_ACCOUNTS_ON_ONE_IP;
	public static String[] MAX_ACTIVE_ACCOUNTS_IGNORED_IP;
	public static int MAX_ACTIVE_ACCOUNTS_ON_ONE_HWID;

	public static int[] MONSTER_LEVEL_DIFF_EXP_PENALTY;

	public static boolean SHOW_TARGET_PLAYER_BUFF_EFFECTS;
	public static boolean SHOW_TARGET_PLAYER_DEBUFF_EFFECTS;
	public static boolean SHOW_TARGET_NPC_BUFF_EFFECTS;
	public static boolean SHOW_TARGET_NPC_DEBUFF_EFFECTS;

	public static int CANCEL_SKILLS_HIGH_CHANCE_CAP;
	public static int CANCEL_SKILLS_LOW_CHANCE_CAP;

	public static long SP_LIMIT;

	public static int ELEMENT_ATTACK_LIMIT;

	public static boolean ALLOW_AWAY_STATUS;
	public static boolean AWAY_ONLY_FOR_PREMIUM;
	public static int AWAY_TIMER;
	public static int BACK_TIMER;
	public static int AWAY_TITLE_COLOR;
	public static boolean AWAY_PLAYER_TAKE_AGGRO;
	public static boolean AWAY_PEACE_ZONE;

	public static double[] PERCENT_LOST_ON_DEATH;
	public static double PERCENT_LOST_ON_DEATH_MOD_IN_PEACE_ZONE;
	public static double PERCENT_LOST_ON_DEATH_MOD_IN_PVP;
	public static double PERCENT_LOST_ON_DEATH_MOD_IN_WAR;
	public static double PERCENT_LOST_ON_DEATH_MOD_FOR_PK;

	public static boolean ALLOW_LUCKY_GAME_EVENT;
	public static int LUCKY_GAME_UNIQUE_REWARD_GAMES_COUNT;
	public static int LUCKY_GAME_ADDITIONAL_REWARD_GAMES_COUNT;

	public static boolean FISHING_ONLY_PREMIUM_ACCOUNTS;
	public static int FISHING_MINIMUM_LEVEL;

	public static int FAKE_PLAYERS_COUNT;
	public static int FAKE_PLAYERS_SPAWN_TASK_DELAY;

	public static boolean BOTREPORT_ENABLED;
	public static int BOTREPORT_REPORT_DELAY;
	public static String BOTREPORT_REPORTS_RESET_TIME;
	public static boolean BOTREPORT_ALLOW_REPORTS_FROM_SAME_CLAN_MEMBERS;

	public static boolean VIP_ATTENDANCE_REWARDS_ENABLED;
	public static boolean VIP_ATTENDANCE_REWARDS_REWARD_BY_ACCOUNT = true;

	public static LocalDateTime VIP_ATTENDANCE_REWARDS_START_DATE;
	public static LocalDateTime VIP_ATTENDANCE_REWARDS_END_DATE;

	public static boolean ALT_SAVE_PRIVATE_STORE;

	public static boolean APPEARANCE_STONE_CHECK_ARMOR_TYPE = true;

	public static boolean MULTICLASS_SYSTEM_ENABLED;
	public static boolean MULTICLASS_SYSTEM_SHOW_LEARN_LIST_ON_OPEN_SKILL_LIST;
	public static double MULTICLASS_SYSTEM_NON_CLASS_SP_MODIFIER;
	public static double MULTICLASS_SYSTEM_1ST_CLASS_SP_MODIFIER;
	public static double MULTICLASS_SYSTEM_2ND_CLASS_SP_MODIFIER;
	public static double MULTICLASS_SYSTEM_3RD_CLASS_SP_MODIFIER;
	public static int MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_ID_BASED_ON_SP;
	public static int MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_ID_BASED_ON_SP;
	public static int MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_ID_BASED_ON_SP;
	public static int MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_ID_BASED_ON_SP;
	public static double MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP;
	public static double MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP;
	public static double MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP;
	public static double MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP;
	public static int MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_ID;
	public static int MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_ID;
	public static int MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_ID;
	public static int MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_ID;
	public static long MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_COUNT;
	public static long MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_COUNT;
	public static long MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_COUNT;
	public static long MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_COUNT;

	public static int BATTLE_ZONE_AROUND_RAID_BOSSES_RANGE;

	public static boolean BUFF_STORE_ENABLED;
	public static boolean BUFF_STORE_MP_ENABLED;
	public static double BUFF_STORE_MP_CONSUME_MULTIPLIER;
	public static boolean BUFF_STORE_ITEM_CONSUME_ENABLED;
	public static int BUFF_STORE_NAME_COLOR;
	public static int BUFF_STORE_TITLE_COLOR;
	public static int BUFF_STORE_OFFLINE_NAME_COLOR;
	public static List<Integer> BUFF_STORE_ALLOWED_CLASS_LIST;
	public static TIntSet BUFF_STORE_ALLOWED_SKILL_LIST = new TIntHashSet();
	public static long BUFF_STORE_MIN_PRICE;
	public static long BUFF_STORE_MAX_PRICE;

	public static boolean TRAINING_CAMP_ENABLE;
	public static boolean TRAINING_CAMP_PREMIUM_ONLY;
	public static int TRAINING_CAMP_MAX_DURATION;
	public static int TRAINING_CAMP_MIN_LEVEL;
	public static int TRAINING_CAMP_MAX_LEVEL;

	public static String[] ALLOWED_TRADE_ZONES;

	public static double ATTACK_TRAIT_POISON_MOD;
	public static double DEFENCE_TRAIT_POISON_MOD;
	public static double ATTACK_TRAIT_HOLD_MOD;
	public static double DEFENCE_TRAIT_HOLD_MOD;
	public static double ATTACK_TRAIT_BLEED_MOD;
	public static double DEFENCE_TRAIT_BLEED_MOD;
	public static double ATTACK_TRAIT_SLEEP_MOD;
	public static double DEFENCE_TRAIT_SLEEP_MOD;
	public static double ATTACK_TRAIT_SHOCK_MOD;
	public static double DEFENCE_TRAIT_SHOCK_MOD;
	public static double ATTACK_TRAIT_DERANGEMENT_MOD;
	public static double DEFENCE_TRAIT_DERANGEMENT_MOD;
	public static double ATTACK_TRAIT_PARALYZE_MOD;
	public static double DEFENCE_TRAIT_PARALYZE_MOD;
	public static double ATTACK_TRAIT_BOSS_MOD;
	public static double DEFENCE_TRAIT_BOSS_MOD;
	public static double ATTACK_TRAIT_DEATH_MOD;
	public static double DEFENCE_TRAIT_DEATH_MOD;
	public static double ATTACK_TRAIT_ROOT_PHYSICALLY_MOD;
	public static double DEFENCE_TRAIT_ROOT_PHYSICALLY_MOD;
	public static double ATTACK_TRAIT_TURN_STONE_MOD;
	public static double DEFENCE_TRAIT_TURN_STONE_MOD;
	public static double ATTACK_TRAIT_GUST_MOD;
	public static double DEFENCE_TRAIT_GUST_MOD;
	public static double ATTACK_TRAIT_PHYSICAL_BLOCKADE_MOD;
	public static double DEFENCE_TRAIT_PHYSICAL_BLOCKADE_MOD;
	public static double ATTACK_TRAIT_TARGET_MOD;
	public static double DEFENCE_TRAIT_TARGET_MOD;
	public static double ATTACK_TRAIT_PHYSICAL_WEAKNESS_MOD;
	public static double DEFENCE_TRAIT_PHYSICAL_WEAKNESS_MOD;
	public static double ATTACK_TRAIT_MAGICAL_WEAKNESS_MOD;
	public static double DEFENCE_TRAIT_MAGICAL_WEAKNESS_MOD;
	public static double ATTACK_TRAIT_KNOCKBACK_MOD;
	public static double DEFENCE_TRAIT_KNOCKBACK_MOD;
	public static double ATTACK_TRAIT_KNOCKDOWN_MOD;
	public static double DEFENCE_TRAIT_KNOCKDOWN_MOD;
	public static double ATTACK_TRAIT_PULL_MOD;
	public static double DEFENCE_TRAIT_PULL_MOD;
	public static double ATTACK_TRAIT_HATE_MOD;
	public static double DEFENCE_TRAIT_HATE_MOD;
	public static double ATTACK_TRAIT_AGGRESSION_MOD;
	public static double DEFENCE_TRAIT_AGGRESSION_MOD;
	public static double ATTACK_TRAIT_AIRBIND_MOD;
	public static double DEFENCE_TRAIT_AIRBIND_MOD;
	public static double ATTACK_TRAIT_DISARM_MOD;
	public static double DEFENCE_TRAIT_DISARM_MOD;
	public static double ATTACK_TRAIT_DEPORT_MOD;
	public static double DEFENCE_TRAIT_DEPORT_MOD;
	public static double ATTACK_TRAIT_CHANGEBODY_MOD;
	public static double DEFENCE_TRAIT_CHANGEBODY_MOD;

	public static int CHECK_BANS_INTERVAL;

	public static boolean ELEMENTAL_SYSTEM_ENABLED = true;
	public static int ELEMENTAL_RESET_POINTS_ITEM_ID = ItemTemplate.ITEM_ID_ADENA;
	public static long ELEMENTAL_RESET_POINTS_ITEM_COUNT = 50_000;

	public static boolean RANDOM_CRAFT_SYSTEM_ENABLED;
	public static int RANDOM_CRAFT_TRY_COMMISSION;

	public static int VITALITY_MAX_ITEMS_ALLOWED;

	public static boolean MAGIC_LAMP_ENABLED;
	public static int BLESSING_ITEM_CHANCE;
	public static int MAGIC_LAMP_CONSUME_COUNT;
	public static int MAGIC_LAMP_GREATER_CONSUME_COUNT;
	public static int MAGIC_LAMP_MAX_LEVEL_EXP;

	public static int SHARE_POSITION_COST;
	public static int SHARED_TELEPORTS_PER_DAY;
	public static int SHARED_TELEPORT_TO_LOCATION;

	public static boolean STEADY_BOX_ENABLED;
	public static int STEADY_BOX_KILL_MOBS;
	public static int STEADY_BOX_KILL_PLAYERS;

	public static boolean HELLBOUND_ENABLED_ALL_TIME;
	public static boolean AUTOFARM_IN_PEACE_ZONE;

	// Balthus Event
	public static boolean BALTHUS_EVENT_ENABLE;
	public static String BALTHUS_EVENT_TIME_START;
	public static String BALTHUS_EVENT_TIME_END;
	public static int BALTHUS_EVENT_PARTICIPATE_BUFF_ID;
	public static int BALTHUS_EVENT_BASIC_REWARD_ID;
	public static int BALTHUS_EVENT_BASIC_REWARD_COUNT;
	public static int BALTHUS_EVENT_JACKPOT_CHANCE;

	// BM Festival event
	// BM Festival
	public static boolean BM_FESTIVAL_ENABLE;
	public static String BM_FESTIVAL_TIME_START;
	public static String BM_FESTIVAL_TIME_END;
	public static int BM_FESTIVAL_ITEM_TO_PLAY;
	public static int BM_FESTIVAL_PLAY_LIMIT;
	public static int BM_FESTIVAL_ITEM_TO_PLAY_COUNT;

	// Fortress
	public static int FORTRESS_REWARD_ID;
	public static int FORTRESS_REWARD_COUNT;

	public static boolean SUBJUGATION_ENABLED;

	public static void loadEssenceConfig()
	{
		ExProperties essenceSettings = load(ESSENCE_FEATURES_FILE);

		ALLOW_WORLD_CHAT = essenceSettings.getProperty("ALLOW_WORLD_CHAT", true);
		WORLD_CHAT_POINTS_PER_DAY = essenceSettings.getProperty("WORLD_CHAT_POINTS_PER_DAY", 10);
		WORLD_CHAT_POINTS_PER_DAY_PA = essenceSettings.getProperty("WORLD_CHAT_POINTS_PER_DAY_PA", 20);

		RANDOM_CRAFT_SYSTEM_ENABLED = essenceSettings.getProperty("EnableRandomCraftSystem", true);
		RANDOM_CRAFT_TRY_COMMISSION = essenceSettings.getProperty("RandomCraftTryCommission", 300000);

		VITALITY_MAX_ITEMS_ALLOWED = essenceSettings.getProperty("VitalityMaxItemsAllowed", 999);
		if(VITALITY_MAX_ITEMS_ALLOWED == 0)
			VITALITY_MAX_ITEMS_ALLOWED = Integer.MAX_VALUE;

		MAGIC_LAMP_ENABLED = essenceSettings.getProperty("EnableMagicLampGame", true);
		BLESSING_ITEM_CHANCE = essenceSettings.getProperty("BlessingItemChance", 5);
		MAGIC_LAMP_CONSUME_COUNT = essenceSettings.getProperty("MagicLampConsumeCount", 1);
		MAGIC_LAMP_GREATER_CONSUME_COUNT = essenceSettings.getProperty("MagicLampGreaterConsumeCount", 10);
		MAGIC_LAMP_MAX_LEVEL_EXP = essenceSettings.getProperty("MagicLampMaxLevelExp", 10000000);

		SHARE_POSITION_COST = essenceSettings.getProperty("SharePositionCost", 50);
		SHARED_TELEPORTS_PER_DAY = essenceSettings.getProperty("SharedTeleportsPerDay", 5);
		SHARED_TELEPORT_TO_LOCATION = essenceSettings.getProperty("SharedTeleportToLocationCost", 400);

		SUBJUGATION_ENABLED = essenceSettings.getProperty("EnablePurgeSystem", true);

		STEADY_BOX_ENABLED = essenceSettings.getProperty("EnableAchievementBox", true);
		STEADY_BOX_KILL_MOBS = essenceSettings.getProperty("AchievementBoxKillMobs", 1000);
		STEADY_BOX_KILL_PLAYERS = essenceSettings.getProperty("AchievementBoxKillPlayers", 5);

		HELLBOUND_ENABLED_ALL_TIME = essenceSettings.getProperty("HellboundEnabledAllTime", false);

		TRAINING_CAMP_ENABLE = essenceSettings.getProperty("ENABLE", false);
		TRAINING_CAMP_PREMIUM_ONLY = essenceSettings.getProperty("PREMIUM_ONLY", true);
		TRAINING_CAMP_MAX_DURATION = essenceSettings.getProperty("MAX_DURATION", 18000);
		TRAINING_CAMP_MIN_LEVEL = essenceSettings.getProperty("MIN_LEVEL", 18);
		TRAINING_CAMP_MAX_LEVEL = essenceSettings.getProperty("MAX_LEVEL", 127);
	}

	public static void loadWeddingConfig()
	{
		ExProperties weddingSettings = load(WEDDING_FILE);

		ALLOW_WEDDING = weddingSettings.getProperty("AllowWedding", false);
		WEDDING_PRICE = weddingSettings.getProperty("WeddingPrice", 500000);
		WEDDING_PUNISH_INFIDELITY = weddingSettings.getProperty("WeddingPunishInfidelity", true);
		WEDDING_TELEPORT = weddingSettings.getProperty("WeddingTeleport", true);
		WEDDING_TELEPORT_PRICE = weddingSettings.getProperty("WeddingTeleportPrice", 500000);
		WEDDING_TELEPORT_INTERVAL = weddingSettings.getProperty("WeddingTeleportInterval", 120);
		WEDDING_SAMESEX = weddingSettings.getProperty("WeddingAllowSameSex", true);
		WEDDING_FORMALWEAR = weddingSettings.getProperty("WeddingFormalWear", true);
		WEDDING_DIVORCE_COSTS = weddingSettings.getProperty("WeddingDivorceCosts", 20);
	}

	public static void loadRateServerConfig()
	{
		ExProperties rateServerSettings = load(RATES_FILE);

		double RATE_XP = rateServerSettings.getProperty("RateXp", 1.);
		RATE_XP_BY_LVL = new double[Byte.MAX_VALUE];
		double prevRateXp = RATE_XP;
		for(int i = 1; i < RATE_XP_BY_LVL.length; i++)
		{
			double rate = rateServerSettings.getProperty("RateXpByLevel" + i, prevRateXp);
			RATE_XP_BY_LVL[i] = rate;
			if(rate != prevRateXp)
				prevRateXp = rate;
		}

		double RATE_SP = rateServerSettings.getProperty("RateSp", 1.);
		RATE_SP_BY_LVL = new double[Byte.MAX_VALUE];
		double prevRateSp = RATE_SP;
		for(int i = 1; i < RATE_SP_BY_LVL.length; i++)
		{
			double rate = rateServerSettings.getProperty("RateSpByLevel" + i, prevRateSp);
			RATE_SP_BY_LVL[i] = rate;
			if(rate != prevRateSp)
				prevRateSp = rate;
		}

		RATE_ELEMENTAL_XP = rateServerSettings.getProperty("RATE_ELEMENTAL_XP", 1.);

		RATE_MAGIC_LAMP_XP = rateServerSettings.getProperty("RateMagicLampXp", 1.);

		MAX_DROP_ITEMS_FROM_ONE_GROUP = rateServerSettings.getProperty("MAX_DROP_ITEMS_FROM_ONE_GROUP", 1);

		double RATE_DROP_ADENA = rateServerSettings.getProperty("RateDropAdena", 1.);
		RATE_DROP_ADENA_BY_LVL = new double[Byte.MAX_VALUE];
		double prevRateAdena = RATE_DROP_ADENA;
		for(int i = 1; i < RATE_DROP_ADENA_BY_LVL.length; i++)
		{
			double rate = rateServerSettings.getProperty("RateDropAdenaByLevel" + i, prevRateAdena);
			RATE_DROP_ADENA_BY_LVL[i] = rate;
			if(rate != prevRateAdena)
				prevRateAdena = rate;
		}

		double RATE_DROP_ITEMS = rateServerSettings.getProperty("RateDropItems", 1.);
		RATE_DROP_ITEMS_BY_LVL = new double[Byte.MAX_VALUE];
		double prevRateItems = RATE_DROP_ITEMS;
		for(int i = 1; i < RATE_DROP_ITEMS_BY_LVL.length; i++)
		{
			double rate = rateServerSettings.getProperty("RateDropItemsByLevel" + i, prevRateItems);
			RATE_DROP_ITEMS_BY_LVL[i] = rate;
			if(rate != prevRateItems)
				prevRateItems = rate;
		}

		DROP_CHANCE_MODIFIER = rateServerSettings.getProperty("DROP_CHANCE_MODIFIER", 1.);
		DROP_COUNT_MODIFIER = rateServerSettings.getProperty("DROP_COUNT_MODIFIER", 1.);

		double RATE_DROP_SPOIL = rateServerSettings.getProperty("RateDropSpoil", 1.);
		RATE_DROP_SPOIL_BY_LVL = new double[Byte.MAX_VALUE];
		double prevRateSpoil = RATE_DROP_SPOIL;
		for(int i = 1; i < RATE_DROP_SPOIL_BY_LVL.length; i++)
		{
			double rate = rateServerSettings.getProperty("RateDropSpoilByLevel" + i, prevRateSpoil);
			RATE_DROP_SPOIL_BY_LVL[i] = rate;
			if(rate != prevRateSpoil)
				prevRateSpoil = rate;
		}

		SPOIL_CHANCE_MODIFIER = rateServerSettings.getProperty("SPOIL_CHANCE_MODIFIER", 1.);
		SPOIL_COUNT_MODIFIER = rateServerSettings.getProperty("SPOIL_COUNT_MODIFIER", 1.);

		RATE_QUESTS_REWARD = rateServerSettings.getProperty("RateQuestsReward", 1.);
		RATE_QUEST_REWARD_EXP_SP_ADENA_ONLY = rateServerSettings.getProperty("RATE_QUEST_REWARD_EXP_SP_ADENA_ONLY", true);
		QUESTS_REWARD_LIMIT_MODIFIER = rateServerSettings.getProperty("QUESTS_REWARD_LIMIT_MODIFIER", RATE_QUESTS_REWARD);

		RATE_QUESTS_DROP = rateServerSettings.getProperty("RateQuestsDrop", 1.);
		RATE_CLAN_REP_SCORE = rateServerSettings.getProperty("RateClanRepScore", 1.);
		RATE_CLAN_REP_SCORE_MAX_AFFECTED = rateServerSettings.getProperty("RateClanRepScoreMaxAffected", 2);
		RATE_XP_RAIDBOSS_MODIFIER = rateServerSettings.getProperty("RATE_XP_RAIDBOSS_MODIFIER", 1.);
		RATE_SP_RAIDBOSS_MODIFIER = rateServerSettings.getProperty("RATE_SP_RAIDBOSS_MODIFIER", 1.);
		RATE_DROP_ITEMS_RAIDBOSS = rateServerSettings.getProperty("RATE_DROP_ITEMS_RAIDBOSS", 1.);
		DROP_CHANCE_MODIFIER_RAIDBOSS = rateServerSettings.getProperty("DROP_CHANCE_MODIFIER_RAIDBOSS", 1.);
		DROP_COUNT_MODIFIER_RAIDBOSS = rateServerSettings.getProperty("DROP_COUNT_MODIFIER_RAIDBOSS", 1.);
		RATE_DROP_ITEMS_BOSS = rateServerSettings.getProperty("RATE_DROP_ITEMS_BOSS", 1.);
		DROP_CHANCE_MODIFIER_BOSS = rateServerSettings.getProperty("DROP_CHANCE_MODIFIER_BOSS", 1.);
		DROP_COUNT_MODIFIER_BOSS = rateServerSettings.getProperty("DROP_COUNT_MODIFIER_BOSS", 1.);
		DISABLE_DROP_EXCEPT_ITEM_IDS = new TIntHashSet();
		DISABLE_DROP_EXCEPT_ITEM_IDS.addAll(rateServerSettings.getProperty("DISABLE_DROP_EXCEPT_ITEM_IDS", new int[0]));
		NO_RATE_ITEMS = rateServerSettings.getProperty("NoRateItemIds", new int[] {
				6660,
				6662,
				6661,
				6659,
				6656,
				6658,
				8191,
				6657,
				10170,
				10314,
				16025,
				16026
		});
		NO_RATE_EQUIPMENT = rateServerSettings.getProperty("NoRateEquipment", false);
		NO_RATE_KEY_MATERIAL = rateServerSettings.getProperty("NoRateKeyMaterial", false);
		NO_RATE_RECIPES = rateServerSettings.getProperty("NoRateRecipes", false);
		RATE_DROP_SIEGE_GUARD = rateServerSettings.getProperty("RateSiegeGuard", 1.);
		PA_RATE_IN_PARTY_MODE = rateServerSettings.getProperty("PA_RATE_IN_PARTY_MODE", 0);

		String[] ignoreAllDropButThis = rateServerSettings.getProperty("IgnoreAllDropButThis", "-1").split(";");
		for(String dropId : ignoreAllDropButThis)
		{
			if(dropId == null || dropId.isEmpty())
				continue;

			try
			{
				int itemId = Integer.parseInt(dropId);
				if(itemId > 0)
					DROP_ONLY_THIS.add(itemId);
			}
			catch(NumberFormatException e)
			{
				_log.error("", e);
			}
		}
		INCLUDE_RAID_DROP = rateServerSettings.getProperty("RemainRaidDropWithNoChanges", false);

		RATE_MOB_SPAWN = rateServerSettings.getProperty("RateMobSpawn", 1.);
		RATE_MOB_SPAWN_MIN_LEVEL = rateServerSettings.getProperty("RateMobMinLevel", 1);
		RATE_MOB_SPAWN_MAX_LEVEL = rateServerSettings.getProperty("RateMobMaxLevel", 100);

		RATE_RAID_REGEN = rateServerSettings.getProperty("RateRaidRegen", 1.);
		RATE_RAID_DEFENSE = rateServerSettings.getProperty("RateRaidDefense", 1.);
		RATE_RAID_ATTACK = rateServerSettings.getProperty("RateRaidAttack", 1.);
		RATE_EPIC_DEFENSE = rateServerSettings.getProperty("RateEpicDefense", RATE_RAID_DEFENSE);
		RATE_EPIC_ATTACK = rateServerSettings.getProperty("RateEpicAttack", RATE_RAID_ATTACK);
		RAID_MAX_LEVEL_DIFF = rateServerSettings.getProperty("RaidMaxLevelDiff", 8);
		PARALIZE_ON_RAID_DIFF = rateServerSettings.getProperty("ParalizeOnRaidLevelDiff", true);
	}

	public static void loadServerConfig()
	{
		ExProperties serverSettings = load(CONFIGURATION_FILE);

		AUTH_SERVER_AGE_LIMIT = serverSettings.getProperty("ServerAgeLimit", 0);
		AUTH_SERVER_GM_ONLY = serverSettings.getProperty("ServerGMOnly", false);
		AUTH_SERVER_BRACKETS = serverSettings.getProperty("ServerBrackets", false);
		AUTH_SERVER_IS_PVP = serverSettings.getProperty("PvPServer", false);
		for(String a : serverSettings.getProperty("ServerType", ArrayUtils.EMPTY_STRING_ARRAY))
		{
			if(a.trim().isEmpty())
			{
				continue;
			}

			ServerType t = ServerType.valueOf(a.toUpperCase());
			AUTH_SERVER_SERVER_TYPE |= t.getMask();
		}

		EVERYBODY_HAS_ADMIN_RIGHTS = serverSettings.getProperty("EverybodyHasAdminRights", false);

		HIDE_GM_STATUS = serverSettings.getProperty("HideGMStatus", false);
		SHOW_GM_LOGIN = serverSettings.getProperty("ShowGMLogin", true);
		SAVE_GM_EFFECTS = serverSettings.getProperty("SaveGMEffects", false);

		CNAME_TEMPLATE = serverSettings.getProperty("CnameTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f]{2,16}");
		CLAN_NAME_TEMPLATE = serverSettings.getProperty("ClanNameTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f]{3,16}");
		CLAN_TITLE_TEMPLATE = serverSettings.getProperty("ClanTitleTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f \\p{Punct}]{1,16}");
		ALLY_NAME_TEMPLATE = serverSettings.getProperty("AllyNameTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f]{3,16}");

		MAX_CHARACTERS_NUMBER_PER_ACCOUNT = serverSettings.getProperty("MAX_CHARACTERS_NUMBER_PER_ACCOUNT", 7);
		CHECK_BANS_INTERVAL = serverSettings.getProperty("CHECK_BANS_INTERVAL", 5);

		GLOBAL_SHOUT = serverSettings.getProperty("GlobalShout", false);
		GLOBAL_TRADE_CHAT = serverSettings.getProperty("GlobalTradeChat", false);
		CHAT_RANGE = serverSettings.getProperty("ChatRange", 1250);
		SHOUT_SQUARE_OFFSET = serverSettings.getProperty("ShoutOffset", 0);
		SHOUT_SQUARE_OFFSET = SHOUT_SQUARE_OFFSET * SHOUT_SQUARE_OFFSET;

		LOG_CHAT = serverSettings.getProperty("LogChat", false);
		TURN_LOG_SYSTEM = serverSettings.getProperty("GlobalLogging", true);

		AUTODESTROY_ITEM_AFTER = serverSettings.getProperty("AutoDestroyDroppedItemAfter", 0);
		AUTODESTROY_PLAYER_ITEM_AFTER = serverSettings.getProperty("AutoDestroyPlayerDroppedItemAfter", 0);
		CHARACTER_DELETE_AFTER_HOURS = serverSettings.getProperty("DeleteCharAfterHours", 168);
		PURGE_BYPASS_TASK_FREQUENCY = serverSettings.getProperty("PurgeTaskFrequency", 60);

		try
		{
			DATAPACK_ROOT = new File(serverSettings.getProperty("DatapackRoot", ".")).getCanonicalFile();
		}
		catch(IOException e)
		{
			_log.error("", e);
		}

		ALLOW_DISCARDITEM = serverSettings.getProperty("AllowDiscardItem", true);
		ALLOW_MAIL = serverSettings.getProperty("AllowMail", true);
		ALLOW_WAREHOUSE = serverSettings.getProperty("AllowWarehouse", true);
		ALLOW_WATER = serverSettings.getProperty("AllowWater", true);
		ALLOW_ITEMS_REFUND = serverSettings.getProperty("ALLOW_ITEMS_REFUND", true);

		final String[] protocols = serverSettings.getProperty("AvailableProtocolRevisions", "362").split(";");
		AVAILABLE_PROTOCOL_REVISIONS = new ArrayList<>(protocols.length);
		for(final String protocol : protocols)
		{
			try
			{
				AVAILABLE_PROTOCOL_REVISIONS.add(Integer.parseInt(protocol.trim()));
			}
			catch(final NumberFormatException e)
			{
				_log.info("Wrong config protocol version: " + protocol + ". Skipped.");
			}
		}

		MIN_NPC_ANIMATION = serverSettings.getProperty("MinNPCAnimation", 5);
		MAX_NPC_ANIMATION = serverSettings.getProperty("MaxNPCAnimation", 90);

		AUTOSAVE = serverSettings.getProperty("Autosave", true);

		MAXIMUM_ONLINE_USERS = serverSettings.getProperty("MaximumOnlineUsers", 3000);

		DATABASE_DRIVER = serverSettings.getProperty("DATABASE_DRIVER", "org.mariadb.jdbc.Driver");

		String databaseHost = serverSettings.getProperty("DATABASE_HOST", "localhost");
		int databasePort = serverSettings.getProperty("DATABASE_PORT", 3306);
		String databaseName = serverSettings.getProperty("DATABASE_NAME", "l2game");

		DATABASE_URL = serverSettings.getProperty("DATABASE_URL", "jdbc:mariadb://" + databaseHost + ":" + databasePort + "/" + databaseName
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
		DATABASE_LOGIN = serverSettings.getProperty("DATABASE_LOGIN", "root");
		DATABASE_PASSWORD = serverSettings.getProperty("DATABASE_PASSWORD", "root");

		DATABASE_AUTOUPDATE = serverSettings.getProperty("DATABASE_AUTOUPDATE", false);

		DATABASE_MAX_CONNECTIONS = serverSettings.getProperty("MaximumDbConnections", 10);
		DATABASE_MAX_IDLE_TIMEOUT = serverSettings.getProperty("MaxIdleConnectionTimeout", 600);
		DATABASE_IDLE_TEST_PERIOD = serverSettings.getProperty("IdleConnectionTestPeriod", 60);

		USER_INFO_INTERVAL = serverSettings.getProperty("UserInfoInterval", 100L);
		BROADCAST_STATS_INTERVAL = serverSettings.getProperty("BroadcastStatsInterval", true);
		BROADCAST_CHAR_INFO_INTERVAL = serverSettings.getProperty("BroadcastCharInfoInterval", 100L);

		EFFECT_TASK_MANAGER_COUNT = serverSettings.getProperty("EffectTaskManagers", 2);

		SCHEDULED_THREAD_POOL_SIZE = serverSettings.getProperty("ScheduledThreadPoolSize", NCPUS * 4);
		EXECUTOR_THREAD_POOL_SIZE = serverSettings.getProperty("ExecutorThreadPoolSize", NCPUS * 2);

		SELECTOR_CONFIG.SLEEP_TIME = serverSettings.getProperty("SelectorSleepTime", 10L);
		SELECTOR_CONFIG.INTEREST_DELAY = serverSettings.getProperty("InterestDelay", 30L);
		SELECTOR_CONFIG.MAX_SEND_PER_PASS = serverSettings.getProperty("MaxSendPerPass", 32);
		SELECTOR_CONFIG.READ_BUFFER_SIZE = serverSettings.getProperty("ReadBufferSize", 65536);
		SELECTOR_CONFIG.WRITE_BUFFER_SIZE = serverSettings.getProperty("WriteBufferSize", 131072);
		SELECTOR_CONFIG.HELPER_BUFFER_COUNT = serverSettings.getProperty("BufferPoolSize", 64);

		CHAT_MESSAGE_MAX_LEN = serverSettings.getProperty("ChatMessageLimit", 1000);
		ABUSEWORD_BANCHAT = serverSettings.getProperty("ABUSEWORD_BANCHAT", false);
		int counter = 0;
		for(int id : serverSettings.getProperty("ABUSEWORD_BAN_CHANNEL", new int[] {
				0
		}))
		{
			BAN_CHANNEL_LIST[counter] = id;
			counter++;
		}
		ABUSEWORD_REPLACE = serverSettings.getProperty("ABUSEWORD_REPLACE", false);
		ABUSEWORD_REPLACE_STRING = serverSettings.getProperty("ABUSEWORD_REPLACE_STRING", "_-_");
		BANCHAT_ANNOUNCE = serverSettings.getProperty("BANCHAT_ANNOUNCE", true);
		BANCHAT_ANNOUNCE_FOR_ALL_WORLD = serverSettings.getProperty("BANCHAT_ANNOUNCE_FOR_ALL_WORLD", true);
		BANCHAT_ANNOUNCE_NICK = serverSettings.getProperty("BANCHAT_ANNOUNCE_NICK", true);
		ABUSEWORD_BANTIME = serverSettings.getProperty("ABUSEWORD_UNBAN_TIMER", 30);

		USE_CLIENT_LANG = serverSettings.getProperty("UseClientLang", false);
		CAN_SELECT_LANGUAGE = serverSettings.getProperty("CAN_SELECT_LANGUAGE", !USE_CLIENT_LANG);
		DEFAULT_LANG = Language.valueOf(serverSettings.getProperty("DefaultLang", "ENGLISH").toUpperCase());
		RESTART_AT_TIME = serverSettings.getProperty("AutoRestartAt", "0 5 * * *");

		RETAIL_MULTISELL_ENCHANT_TRANSFER = serverSettings.getProperty("RetailMultisellItemExchange", true);

		SHIFT_BY = serverSettings.getProperty("HShift", 12);
		SHIFT_BY_Z = serverSettings.getProperty("VShift", 11);
		MAP_MIN_Z = serverSettings.getProperty("MapMinZ", Short.MIN_VALUE);
		MAP_MAX_Z = serverSettings.getProperty("MapMaxZ", Short.MAX_VALUE);

		MOVE_PACKET_DELAY = serverSettings.getProperty("MovePacketDelay", 200);
		ATTACK_PACKET_DELAY = serverSettings.getProperty("AttackPacketDelay", 500);

		DONTLOADSPAWN = serverSettings.getProperty("StartWithoutSpawn", false);
		DONTLOADQUEST = serverSettings.getProperty("StartWithoutQuest", false);

		MAX_REFLECTIONS_COUNT = serverSettings.getProperty("MaxReflectionsCount", 300);

		WEAR_DELAY = serverSettings.getProperty("WearDelay", 5);

		HTM_CACHE_MODE = serverSettings.getProperty("HtmCacheMode", HtmCache.LAZY);
		SHUTDOWN_ANN_TYPE = serverSettings.getProperty("ShutdownAnnounceType", Shutdown.OFFLIKE_ANNOUNCES);
		APASSWD_TEMPLATE = serverSettings.getProperty("PasswordTemplate", "[A-Za-z0-9]{4,16}");

		ALLOW_MONSTER_RACE = serverSettings.getProperty("AllowMonsterRace", false);
		// ALT_SAVE_ADMIN_SPAWN = serverSettings.getProperty("SaveAdminSpawn", false);

		AVAILABLE_LANGUAGES = new HashSet<Language>();
		AVAILABLE_LANGUAGES.add(Language.ENGLISH);
		AVAILABLE_LANGUAGES.add(Language.RUSSIAN);
		AVAILABLE_LANGUAGES.add(DEFAULT_LANG);

		if(USE_CLIENT_LANG || CAN_SELECT_LANGUAGE)
		{
			String[] availableLanguages = serverSettings.getProperty("AVAILABLE_LANGUAGES", new String[0], ";");
			for(String availableLanguage : availableLanguages)
			{
				Language lang = Language.valueOf(availableLanguage.toUpperCase());
				if(!lang.isCustom() || CAN_SELECT_LANGUAGE)
					AVAILABLE_LANGUAGES.add(lang);
			}
		}

		MAX_ACTIVE_ACCOUNTS_ON_ONE_IP = serverSettings.getProperty("MAX_ACTIVE_ACCOUNTS_ON_ONE_IP", -1);
		MAX_ACTIVE_ACCOUNTS_IGNORED_IP = serverSettings.getProperty("MAX_ACTIVE_ACCOUNTS_IGNORED_IP", new String[0], ";");
		MAX_ACTIVE_ACCOUNTS_ON_ONE_HWID = serverSettings.getProperty("MAX_ACTIVE_ACCOUNTS_ON_ONE_HWID", -1);

		FAKE_PLAYERS_COUNT = serverSettings.getProperty("FAKE_PLAYERS_COUNT", 0);
		FAKE_PLAYERS_SPAWN_TASK_DELAY = Math.max(1, serverSettings.getProperty("FAKE_PLAYERS_SPAWN_TASK_DELAY", 10));
	}

	public static void loadResidenceConfig()
	{
		ExProperties residenceSettings = load(RESIDENCE_CONFIG_FILE);

		CH_BID_GRADE1_MINCLANLEVEL = residenceSettings.getProperty("ClanHallBid_Grade1_MinClanLevel", 2);
		CH_BID_GRADE1_MINCLANMEMBERS = residenceSettings.getProperty("ClanHallBid_Grade1_MinClanMembers", 1);
		CH_BID_GRADE1_MINCLANMEMBERSLEVEL = residenceSettings.getProperty("ClanHallBid_Grade1_MinClanMembersAvgLevel", 1);
		CH_BID_GRADE2_MINCLANLEVEL = residenceSettings.getProperty("ClanHallBid_Grade2_MinClanLevel", 2);
		CH_BID_GRADE2_MINCLANMEMBERS = residenceSettings.getProperty("ClanHallBid_Grade2_MinClanMembers", 1);
		CH_BID_GRADE2_MINCLANMEMBERSLEVEL = residenceSettings.getProperty("ClanHallBid_Grade2_MinClanMembersAvgLevel", 1);
		CH_BID_GRADE3_MINCLANLEVEL = residenceSettings.getProperty("ClanHallBid_Grade3_MinClanLevel", 2);
		CH_BID_GRADE3_MINCLANMEMBERS = residenceSettings.getProperty("ClanHallBid_Grade3_MinClanMembers", 1);
		CH_BID_GRADE3_MINCLANMEMBERSLEVEL = residenceSettings.getProperty("ClanHallBid_Grade3_MinClanMembersAvgLevel", 1);
		RESIDENCE_LEASE_FUNC_MULTIPLIER = residenceSettings.getProperty("ResidenceLeaseFuncMultiplier", 1.);
		RESIDENCE_LEASE_MULTIPLIER = residenceSettings.getProperty("ResidenceLeaseMultiplier", 1.);
	}

	public static void loadChatAntiFloodConfig()
	{
		ExProperties properties = load(CHAT_ANTIFLOOD_CONFIG_FILE);

		ALL_CHAT_USE_MIN_LEVEL = properties.getProperty("ALL_CHAT_USE_MIN_LEVEL", 1);
		ALL_CHAT_USE_MIN_LEVEL_WITHOUT_PA = properties.getProperty("ALL_CHAT_USE_MIN_LEVEL_WITHOUT_PA", 1);
		ALL_CHAT_USE_DELAY = properties.getProperty("ALL_CHAT_USE_DELAY", 0);

		SHOUT_CHAT_USE_MIN_LEVEL = properties.getProperty("SHOUT_CHAT_USE_MIN_LEVEL", 1);
		SHOUT_CHAT_USE_MIN_LEVEL_WITHOUT_PA = properties.getProperty("SHOUT_CHAT_USE_MIN_LEVEL_WITHOUT_PA", 1);
		SHOUT_CHAT_USE_DELAY = properties.getProperty("SHOUT_CHAT_USE_DELAY", 0);

		WORLD_CHAT_USE_MIN_LEVEL = properties.getProperty("WORLD_CHAT_USE_MIN_LEVEL", 95);
		WORLD_CHAT_USE_MIN_LEVEL_WITHOUT_PA = properties.getProperty("WORLD_CHAT_USE_MIN_LEVEL_WITHOUT_PA", 85);
		WORLD_CHAT_USE_DELAY = properties.getProperty("WORLD_CHAT_USE_DELAY", 0);

		TRADE_CHAT_USE_MIN_LEVEL = properties.getProperty("TRADE_CHAT_USE_MIN_LEVEL", 1);
		TRADE_CHAT_USE_MIN_LEVEL_WITHOUT_PA = properties.getProperty("TRADE_CHAT_USE_MIN_LEVEL_WITHOUT_PA", 1);
		TRADE_CHAT_USE_DELAY = properties.getProperty("TRADE_CHAT_USE_DELAY", 0);

		HERO_CHAT_USE_MIN_LEVEL = properties.getProperty("HERO_CHAT_USE_MIN_LEVEL", 1);
		HERO_CHAT_USE_MIN_LEVEL_WITHOUT_PA = properties.getProperty("HERO_CHAT_USE_MIN_LEVEL_WITHOUT_PA", 1);
		HERO_CHAT_USE_DELAY = properties.getProperty("HERO_CHAT_USE_DELAY", 0);

		PRIVATE_CHAT_USE_MIN_LEVEL = properties.getProperty("PRIVATE_CHAT_USE_MIN_LEVEL", 1);
		PRIVATE_CHAT_USE_MIN_LEVEL_WITHOUT_PA = properties.getProperty("PRIVATE_CHAT_USE_MIN_LEVEL_WITHOUT_PA", 1);
		PRIVATE_CHAT_USE_DELAY = properties.getProperty("PRIVATE_CHAT_USE_DELAY", 0);

		MAIL_USE_MIN_LEVEL = properties.getProperty("MAIL_USE_MIN_LEVEL", 1);
		MAIL_USE_MIN_LEVEL_WITHOUT_PA = properties.getProperty("MAIL_USE_MIN_LEVEL_WITHOUT_PA", 1);
		MAIL_USE_DELAY = properties.getProperty("MAIL_USE_DELAY", 0);
	}

	public static void loadL2StudioSettings()
	{
		ExProperties l2StudioSettings = load(L2STUDIO);

		ENABLE_BUFF_PARTY_SERVICES = l2StudioSettings.getProperty("EnableBuffPartyServices", false);
		BUFF_ID1 = l2StudioSettings.getProperty("BuffMinPartySkillId2", 1);
		BUFF_ID2 = l2StudioSettings.getProperty("BuffMinPartySkillId3", 1);
		BUFF_ID3 = l2StudioSettings.getProperty("BuffMinPartySkillId5", 1);

		SKILL_LEVEL1 = l2StudioSettings.getProperty("BuffMinPartySkillLevel2", 1);
		SKILL_LEVEL2 = l2StudioSettings.getProperty("BuffMinPartySkillLevel3", 1);
		SKILL_LEVEL3 = l2StudioSettings.getProperty("BuffMinPartySkillLevel5", 1);
	}

	public static void loadFightClubSettings()
	{
		ExProperties eventFightClubSettings = load(FIGHT_CLUB_FILE);

		FIGHT_CLUB_ENABLED = eventFightClubSettings.getProperty("FightClubEnabled", false);
		MINIMUM_LEVEL_TO_PARRICIPATION = eventFightClubSettings.getProperty("MinimumLevel", 1);
		MAXIMUM_LEVEL_TO_PARRICIPATION = eventFightClubSettings.getProperty("MaximumLevel", 85);
		MAXIMUM_LEVEL_DIFFERENCE = eventFightClubSettings.getProperty("MaximumLevelDifference", 10);
		ALLOWED_RATE_ITEMS = eventFightClubSettings.getProperty("AllowedItems", "").trim().replaceAll(" ", "").split(",");
		PLAYERS_PER_PAGE = eventFightClubSettings.getProperty("RatesOnPage", 10);
		ARENA_TELEPORT_DELAY = eventFightClubSettings.getProperty("ArenaTeleportDelay", 5);
		CANCEL_BUFF_BEFORE_FIGHT = eventFightClubSettings.getProperty("CancelBuffs", true);
		UNSUMMON_PETS = eventFightClubSettings.getProperty("UnsummonPets", true);
		UNSUMMON_SUMMONS = eventFightClubSettings.getProperty("UnsummonSummons", true);
		REMOVE_CLAN_SKILLS = eventFightClubSettings.getProperty("RemoveClanSkills", false);
		REMOVE_HERO_SKILLS = eventFightClubSettings.getProperty("RemoveHeroSkills", false);
		TIME_TO_PREPARATION = eventFightClubSettings.getProperty("TimeToPreparation", 10);
		FIGHT_TIME = eventFightClubSettings.getProperty("TimeToDraw", 300);
		ALLOW_DRAW = eventFightClubSettings.getProperty("AllowDraw", true);
		TIME_TELEPORT_BACK = eventFightClubSettings.getProperty("TimeToBack", 10);
		FIGHT_CLUB_ANNOUNCE_RATE = eventFightClubSettings.getProperty("AnnounceRate", false);
		FIGHT_CLUB_ANNOUNCE_RATE_TO_SCREEN = eventFightClubSettings.getProperty("AnnounceRateToAllScreen", false);
		FIGHT_CLUB_ANNOUNCE_START_TO_SCREEN = eventFightClubSettings.getProperty("AnnounceStartBatleToAllScreen", false);
	}

	public static void loadOtherConfig()
	{
		ExProperties otherSettings = load(OTHER_CONFIG_FILE);

		DEEPBLUE_DROP_RULES = otherSettings.getProperty("UseDeepBlueDropRules", true);
		DEEPBLUE_DROP_MAXDIFF = otherSettings.getProperty("DeepBlueDropMaxDiff", 8);
		DEEPBLUE_DROP_RAID_MAXDIFF = otherSettings.getProperty("DeepBlueDropRaidMaxDiff", 2);

		SWIMING_SPEED = otherSettings.getProperty("SwimingSpeedTemplate", 50);

		/* Inventory slots limits */
		INVENTORY_MAXIMUM_NO_DWARF = otherSettings.getProperty("MaximumSlotsForNoDwarf", 80);
		INVENTORY_MAXIMUM_DWARF = otherSettings.getProperty("MaximumSlotsForDwarf", 100);
		INVENTORY_MAXIMUM_GM = otherSettings.getProperty("MaximumSlotsForGMPlayer", 250);
		QUEST_INVENTORY_MAXIMUM = otherSettings.getProperty("MaximumSlotsForQuests", 100);

		MULTISELL_SIZE = otherSettings.getProperty("MultisellPageSize", 40);

		/* Warehouse slots limits */
		WAREHOUSE_SLOTS_NO_DWARF = otherSettings.getProperty("BaseWarehouseSlotsForNoDwarf", 100);
		WAREHOUSE_SLOTS_DWARF = otherSettings.getProperty("BaseWarehouseSlotsForDwarf", 120);
		WAREHOUSE_SLOTS_CLAN = otherSettings.getProperty("MaximumWarehouseSlotsForClan", 200);
		FREIGHT_SLOTS = otherSettings.getProperty("MaximumFreightSlots", 10);

		REGEN_SIT_WAIT = otherSettings.getProperty("RegenSitWait", false);
		UNSTUCK_SKILL = otherSettings.getProperty("UnstuckSkill", true);

		/* Amount of HP, MP, and CP is restored */
		RESPAWN_RESTORE_CP = otherSettings.getProperty("RespawnRestoreCP", 0.) / 100;
		RESPAWN_RESTORE_HP = otherSettings.getProperty("RespawnRestoreHP", 65.) / 100;
		RESPAWN_RESTORE_MP = otherSettings.getProperty("RespawnRestoreMP", 0.) / 100;

		/* Maximum number of available slots for pvt stores */
		MAX_PVTSTORE_SLOTS_DWARF = otherSettings.getProperty("MaxPvtStoreSlotsDwarf", 5);
		MAX_PVTSTORE_SLOTS_OTHER = otherSettings.getProperty("MaxPvtStoreSlotsOther", 4);
		MAX_PVTCRAFT_SLOTS = otherSettings.getProperty("MaxPvtManufactureSlots", 20);

		SENDSTATUS_TRADE_JUST_OFFLINE = otherSettings.getProperty("SendStatusTradeJustOffline", false);
		SENDSTATUS_TRADE_MOD = otherSettings.getProperty("SendStatusTradeMod", 1.);

		ANNOUNCE_MAMMON_SPAWN = otherSettings.getProperty("AnnounceMammonSpawn", true);

		GM_NAME_COLOUR = Integer.decode("0x" + otherSettings.getProperty("GMNameColour", "FFFFFF"));
		GM_HERO_AURA = otherSettings.getProperty("GMHeroAura", false);
		NORMAL_NAME_COLOUR = Integer.decode("0x" + otherSettings.getProperty("NormalNameColour", "FFFFFF"));
		CLANLEADER_NAME_COLOUR = Integer.decode("0x" + otherSettings.getProperty("ClanleaderNameColour", "FFFFFF"));
		SHOW_HTML_WELCOME = otherSettings.getProperty("ShowHTMLWelcome", false);

		MONSTER_LEVEL_DIFF_EXP_PENALTY = otherSettings.getProperty("MONSTER_LEVEL_DIFF_EXP_PENALTY", new int[] {
				0,
				0,
				0,
				3,
				20,
				39,
				63,
				78,
				87,
				92,
				95,
				100
		});
	}

	public static void loadSpoilConfig()
	{
		ExProperties spoilSettings = load(SPOIL_CONFIG_FILE);

		BASE_SPOIL_RATE = spoilSettings.getProperty("BasePercentChanceOfSpoilSuccess", 78.);
		MINIMUM_SPOIL_RATE = spoilSettings.getProperty("MinimumPercentChanceOfSpoilSuccess", 1.);
		MANOR_SOWING_BASIC_SUCCESS = spoilSettings.getProperty("BasePercentChanceOfSowingSuccess", 100.);
		MANOR_SOWING_ALT_BASIC_SUCCESS = spoilSettings.getProperty("BasePercentChanceOfSowingAltSuccess", 10.);
		MANOR_HARVESTING_BASIC_SUCCESS = spoilSettings.getProperty("BasePercentChanceOfHarvestingSuccess", 90.);
		MANOR_DIFF_PLAYER_TARGET = spoilSettings.getProperty("MinDiffPlayerMob", 5);
		MANOR_DIFF_PLAYER_TARGET_PENALTY = spoilSettings.getProperty("DiffPlayerMobPenalty", 5.);
		MANOR_DIFF_SEED_TARGET = spoilSettings.getProperty("MinDiffSeedMob", 5);
		MANOR_DIFF_SEED_TARGET_PENALTY = spoilSettings.getProperty("DiffSeedMobPenalty", 5.);
		ALLOW_MANOR = spoilSettings.getProperty("AllowManor", true);
		MANOR_REFRESH_TIME = spoilSettings.getProperty("AltManorRefreshTime", 20);
		MANOR_REFRESH_MIN = spoilSettings.getProperty("AltManorRefreshMin", 00);
		MANOR_APPROVE_TIME = spoilSettings.getProperty("AltManorApproveTime", 6);
		MANOR_APPROVE_MIN = spoilSettings.getProperty("AltManorApproveMin", 00);
		MANOR_MAINTENANCE_PERIOD = spoilSettings.getProperty("AltManorMaintenancePeriod", 360000);
	}

	public static void loadFormulasConfig()
	{
		ExProperties formulasSettings = load(FORMULAS_CONFIGURATION_FILE);

		MIN_ABNORMAL_SUCCESS_RATE = formulasSettings.getProperty("MIN_ABNORMAL_SUCCESS_RATE", 10.);
		MAX_ABNORMAL_SUCCESS_RATE = formulasSettings.getProperty("MAX_ABNORMAL_SUCCESS_RATE", 90.);
		SKILLS_CAST_TIME_MIN = formulasSettings.getProperty("SkillsCastTimeMin", 333);

		LIM_PATK = formulasSettings.getProperty("LimitPatk", -1);
		LIM_MATK = formulasSettings.getProperty("LimitMAtk", -1);
		LIM_PDEF = formulasSettings.getProperty("LimitPDef", 15000);
		LIM_MDEF = formulasSettings.getProperty("LimitMDef", 15000);
		LIM_PATK_SPD = formulasSettings.getProperty("LimitPatkSpd", 1500);
		LIM_MATK_SPD = formulasSettings.getProperty("LimitMatkSpd", 1999);
		LIM_CRIT_DAM = formulasSettings.getProperty("LimitCriticalDamage", 500);
		LIM_CRIT = formulasSettings.getProperty("LimitCritical", 500);
		LIM_MCRIT = formulasSettings.getProperty("LimitMCritical", 20);
		LIM_ACCURACY = formulasSettings.getProperty("LimitAccuracy", 200);
		LIM_EVASION = formulasSettings.getProperty("LimitEvasion", 200);
		LIM_MOVE = formulasSettings.getProperty("LimitMove", 250);
		HP_LIMIT = formulasSettings.getProperty("HP_LIMIT", 150000);
		MP_LIMIT = formulasSettings.getProperty("MP_LIMIT", -1);
		CP_LIMIT = formulasSettings.getProperty("CP_LIMIT", -1);

		LIM_FAME = formulasSettings.getProperty("LimitFame", 50000);
		LIM_RAID_POINTS = formulasSettings.getProperty("LIM_RAID_POINTS", 50000);

		LIM_CRAFT_POINTS = formulasSettings.getProperty("LimitCraftPoints", 99);

		PLAYER_P_ATK_MODIFIER = formulasSettings.getProperty("PLAYER_P_ATK_MODIFIER", 1.0);
		PLAYER_M_ATK_MODIFIER = formulasSettings.getProperty("PLAYER_M_ATK_MODIFIER", 1.0);

		ALT_NPC_PATK_MODIFIER = formulasSettings.getProperty("NpcPAtkModifier", 1.0);
		ALT_NPC_MATK_MODIFIER = formulasSettings.getProperty("NpcMAtkModifier", 1.0);
		ALT_NPC_MAXHP_MODIFIER = formulasSettings.getProperty("NpcMaxHpModifier", 1.0);
		ALT_NPC_MAXMP_MODIFIER = formulasSettings.getProperty("NpcMapMpModifier", 1.0);

		ALT_POLE_DAMAGE_MODIFIER = formulasSettings.getProperty("PoleDamageModifier", 1.0);
		LONG_RANGE_AUTO_ATTACK_P_ATK_MOD = formulasSettings.getProperty("LONG_RANGE_AUTO_ATTACK_P_ATK_MOD", 1.0);
		SHORT_RANGE_AUTO_ATTACK_P_ATK_MOD = formulasSettings.getProperty("SHORT_RANGE_AUTO_ATTACK_P_ATK_MOD", 1.0);

		ALT_M_SIMPLE_DAMAGE_MOD = formulasSettings.getProperty("mDamSimpleModifier", 1.0);
		ALT_P_DAMAGE_MOD = formulasSettings.getProperty("pDamMod", 1.0);
		ALT_M_CRIT_DAMAGE_MOD = formulasSettings.getProperty("mCritModifier", 1.0);
		ALT_P_CRIT_DAMAGE_MOD = formulasSettings.getProperty("pCritModifier", 1.0);
		ALT_P_CRIT_CHANCE_MOD = formulasSettings.getProperty("pCritModifierChance", 1.0);
		ALT_M_CRIT_CHANCE_MOD = formulasSettings.getProperty("mCritModifierChance", 1.0);

		SERVITOR_P_ATK_MODIFIER = formulasSettings.getProperty("SERVITOR_P_ATK_MODIFIER", 1.0);
		SERVITOR_M_ATK_MODIFIER = formulasSettings.getProperty("SERVITOR_M_ATK_MODIFIER", 1.0);
		SERVITOR_P_DEF_MODIFIER = formulasSettings.getProperty("SERVITOR_P_DEF_MODIFIER", 1.0);
		SERVITOR_M_DEF_MODIFIER = formulasSettings.getProperty("SERVITOR_M_DEF_MODIFIER", 1.0);
		SERVITOR_P_SKILL_POWER_MODIFIER = formulasSettings.getProperty("SERVITOR_P_SKILL_POWER_MODIFIER", 1.0);
		SERVITOR_M_SKILL_POWER_MODIFIER = formulasSettings.getProperty("SERVITOR_M_SKILL_POWER_MODIFIER", 1.0);

		ALT_BLOW_DAMAGE_MOD = formulasSettings.getProperty("blowDamageModifier", 1.0);
		ALT_BLOW_CRIT_RATE_MODIFIER = formulasSettings.getProperty("blowCritRateModifier", 1.0);
		ALT_VAMPIRIC_CHANCE_MOD = formulasSettings.getProperty("vampiricChanceMod", 1.0);

		REFLECT_MIN_RANGE = formulasSettings.getProperty("ReflectMinimumRange", 600);
		REFLECT_AND_BLOCK_DAMAGE_CHANCE_CAP = formulasSettings.getProperty("reflectAndBlockDamCap", 60.);
		REFLECT_AND_BLOCK_PSKILL_DAMAGE_CHANCE_CAP = formulasSettings.getProperty("reflectAndBlockPSkillDamCap", 60.);
		REFLECT_AND_BLOCK_MSKILL_DAMAGE_CHANCE_CAP = formulasSettings.getProperty("reflectAndBlockMSkillDamCap", 60.);
		REFLECT_DAMAGE_PERCENT_CAP = formulasSettings.getProperty("reflectDamCap", 60.);
		REFLECT_BOW_DAMAGE_PERCENT_CAP = formulasSettings.getProperty("reflectBowDamCap", 60.);
		REFLECT_PSKILL_DAMAGE_PERCENT_CAP = formulasSettings.getProperty("reflectPSkillDamCap", 60.);
		REFLECT_MSKILL_DAMAGE_PERCENT_CAP = formulasSettings.getProperty("reflectMSkillDamCap", 60.);
		SPECIAL_CLASS_BOW_CROSS_BOW_PENALTY = formulasSettings.getProperty("specialClassesWeaponMagicSpeedPenalty", 1.);
		DISABLE_VAMPIRIC_VS_MOB_ON_PVP = formulasSettings.getProperty("disableVampiricAndDrainPvEInPvp", false);
		MIN_HIT_TIME = formulasSettings.getProperty("MinimumHitTime", -1);

		PHYSICAL_MIN_CHANCE_TO_HIT = formulasSettings.getProperty("PHYSICAL_MIN_CHANCE_TO_HIT", 27.5);
		PHYSICAL_MAX_CHANCE_TO_HIT = formulasSettings.getProperty("PHYSICAL_MAX_CHANCE_TO_HIT", 98.0);

		MAGIC_MIN_CHANCE_TO_HIT = formulasSettings.getProperty("MAGIC_MIN_CHANCE_TO_HIT", 72.5);
		MAGIC_MAX_CHANCE_TO_HIT = formulasSettings.getProperty("MAGIC_MAX_CHANCE_TO_HIT", 98.0);

		ENABLE_CRIT_DMG_REDUCTION_ON_MAGIC = formulasSettings.getProperty("ENABLE_CRIT_DMG_REDUCTION_ON_MAGIC", true);

		MAX_BLOW_RATE_ON_BEHIND = formulasSettings.getProperty("MAX_BLOW_RATE_ON_BEHIND", 100.);
		MAX_BLOW_RATE_ON_FRONT_AND_SIDE = formulasSettings.getProperty("MAX_BLOW_RATE_ON_FRONT_AND_SIDE", 80.);

		BLOW_SKILL_CHANCE_MOD_ON_BEHIND = formulasSettings.getProperty("BLOW_SKILL_CHANCE_MOD_ON_BEHIND", 5.);
		BLOW_SKILL_CHANCE_MOD_ON_FRONT = formulasSettings.getProperty("BLOW_SKILL_CHANCE_MOD_ON_FRONT", 4.);

		BLOW_SKILL_DEX_CHANCE_MOD = formulasSettings.getProperty("BLOW_SKILL_DEX_CHANCE_MOD", 1.);
		NORMAL_SKILL_DEX_CHANCE_MOD = formulasSettings.getProperty("NORMAL_SKILL_DEX_CHANCE_MOD", 1.);
		CRIT_STUN_BREAK_CHANCE = formulasSettings.getProperty("CriticalStunBreakChance", 75);
		NORMAL_STUN_BREAK_CHANCE = formulasSettings.getProperty("NormalStunBreakChance", 10);

		EXCELLENT_SHIELD_BLOCK_CHANCE = formulasSettings.getProperty("ExcellentShieldBlockChance", 5);
		EXCELLENT_SHIELD_BLOCK_RECEIVED_DAMAGE = formulasSettings.getProperty("ExcellentShieldBlockDamage", 1);

		SKILLS_CAST_TIME_MIN_PHYSICAL = formulasSettings.getProperty("MinCastTimePhysical", 396);
		SKILLS_CAST_TIME_MIN_MAGICAL = formulasSettings.getProperty("MinCastTimeMagical", 333);
		ENABLE_CRIT_HEIGHT_BONUS = formulasSettings.getProperty("EnableCritHeightBonus", true);

		ENABLE_STUN_BREAK_ON_ATTACK = formulasSettings.getProperty("EnableStunBreakOnAttack", true);
		CRIT_STUN_BREAK_CHANCE_ON_MAGICAL_SKILL = formulasSettings.getProperty("CritStunBreakChanceOnMagicSkill", 66.67);
		NORMAL_STUN_BREAK_CHANCE_ON_MAGICAL_SKILL = formulasSettings.getProperty("NormalStunBreakChanceOnMagicSkill", 33.33);
		CRIT_STUN_BREAK_CHANCE_ON_PHYSICAL_SKILL = formulasSettings.getProperty("CritStunBreakChanceOnPhysSkill", 66.67);
		NORMAL_STUN_BREAK_CHANCE_ON_PHYSICAL_SKILL = formulasSettings.getProperty("NormalStunBreakChanceOnPhysSkill", 33.33);
		CRIT_STUN_BREAK_CHANCE_ON_REGULAR_HIT = formulasSettings.getProperty("CritStunBreakOnRegularHit", 33.33);
		NORMAL_STUN_BREAK_CHANCE_ON_REGULAR_HIT = formulasSettings.getProperty("NormalStunBreakOnRegularHit", 16.67);

		CANCEL_SKILLS_HIGH_CHANCE_CAP = formulasSettings.getProperty("CANCEL_SKILLS_HIGH_CHANCE_CAP", 75);
		CANCEL_SKILLS_LOW_CHANCE_CAP = formulasSettings.getProperty("CANCEL_SKILLS_LOW_CHANCE_CAP", 25);

		SP_LIMIT = formulasSettings.getProperty("SP_LIMIT", 5000000000L);

		ELEMENT_ATTACK_LIMIT = formulasSettings.getProperty("ELEMENT_ATTACK_LIMIT", 999);

		ATTACK_TRAIT_POISON_MOD = formulasSettings.getProperty("ATTACK_TRAIT_POISON_MOD", 1.);
		DEFENCE_TRAIT_POISON_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_POISON_MOD", 1.);
		ATTACK_TRAIT_HOLD_MOD = formulasSettings.getProperty("ATTACK_TRAIT_HOLD_MOD", 1.);
		DEFENCE_TRAIT_HOLD_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_HOLD_MOD", 1.);
		ATTACK_TRAIT_BLEED_MOD = formulasSettings.getProperty("ATTACK_TRAIT_BLEED_MOD", 1.);
		DEFENCE_TRAIT_BLEED_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_BLEED_MOD", 1.);
		ATTACK_TRAIT_SLEEP_MOD = formulasSettings.getProperty("ATTACK_TRAIT_SLEEP_MOD", 1.);
		DEFENCE_TRAIT_SLEEP_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_SLEEP_MOD", 1.);
		ATTACK_TRAIT_SHOCK_MOD = formulasSettings.getProperty("ATTACK_TRAIT_SHOCK_MOD", 1.);
		DEFENCE_TRAIT_SHOCK_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_SHOCK_MOD", 1.);
		ATTACK_TRAIT_DERANGEMENT_MOD = formulasSettings.getProperty("ATTACK_TRAIT_DERANGEMENT_MOD", 1.);
		DEFENCE_TRAIT_DERANGEMENT_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_DERANGEMENT_MOD", 1.);
		ATTACK_TRAIT_PARALYZE_MOD = formulasSettings.getProperty("ATTACK_TRAIT_PARALYZE_MOD", 1.);
		DEFENCE_TRAIT_PARALYZE_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_PARALYZE_MOD", 1.);
		ATTACK_TRAIT_BOSS_MOD = formulasSettings.getProperty("ATTACK_TRAIT_BOSS_MOD", 1.);
		DEFENCE_TRAIT_BOSS_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_BOSS_MOD", 1.);
		ATTACK_TRAIT_DEATH_MOD = formulasSettings.getProperty("ATTACK_TRAIT_DEATH_MOD", 1.);
		DEFENCE_TRAIT_DEATH_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_DEATH_MOD", 1.);
		ATTACK_TRAIT_ROOT_PHYSICALLY_MOD = formulasSettings.getProperty("ATTACK_TRAIT_ROOT_PHYSICALLY_MOD", 1.);
		DEFENCE_TRAIT_ROOT_PHYSICALLY_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_ROOT_PHYSICALLY_MOD", 1.);
		ATTACK_TRAIT_TURN_STONE_MOD = formulasSettings.getProperty("ATTACK_TRAIT_TURN_STONE_MOD", 1.);
		DEFENCE_TRAIT_TURN_STONE_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_TURN_STONE_MOD", 1.);
		ATTACK_TRAIT_GUST_MOD = formulasSettings.getProperty("ATTACK_TRAIT_GUST_MOD", 1.);
		DEFENCE_TRAIT_GUST_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_GUST_MOD", 1.);
		ATTACK_TRAIT_PHYSICAL_BLOCKADE_MOD = formulasSettings.getProperty("ATTACK_TRAIT_PHYSICAL_BLOCKADE_MOD", 1.);
		DEFENCE_TRAIT_PHYSICAL_BLOCKADE_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_PHYSICAL_BLOCKADE_MOD", 1.);
		ATTACK_TRAIT_TARGET_MOD = formulasSettings.getProperty("ATTACK_TRAIT_TARGET_MOD", 1.);
		DEFENCE_TRAIT_TARGET_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_TARGET_MOD", 1.);
		ATTACK_TRAIT_PHYSICAL_WEAKNESS_MOD = formulasSettings.getProperty("ATTACK_TRAIT_PHYSICAL_WEAKNESS_MOD", 1.);
		DEFENCE_TRAIT_PHYSICAL_WEAKNESS_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_PHYSICAL_WEAKNESS_MOD", 1.);
		ATTACK_TRAIT_MAGICAL_WEAKNESS_MOD = formulasSettings.getProperty("ATTACK_TRAIT_MAGICAL_WEAKNESS_MOD", 1.);
		DEFENCE_TRAIT_MAGICAL_WEAKNESS_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_MAGICAL_WEAKNESS_MOD", 1.);
		ATTACK_TRAIT_KNOCKBACK_MOD = formulasSettings.getProperty("ATTACK_TRAIT_KNOCKBACK_MOD", 1.);
		DEFENCE_TRAIT_KNOCKBACK_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_KNOCKBACK_MOD", 1.);
		ATTACK_TRAIT_KNOCKDOWN_MOD = formulasSettings.getProperty("ATTACK_TRAIT_KNOCKDOWN_MOD", 1.);
		DEFENCE_TRAIT_KNOCKDOWN_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_KNOCKDOWN_MOD", 1.);
		ATTACK_TRAIT_PULL_MOD = formulasSettings.getProperty("ATTACK_TRAIT_PULL_MOD", 1.);
		DEFENCE_TRAIT_PULL_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_PULL_MOD", 1.);
		ATTACK_TRAIT_HATE_MOD = formulasSettings.getProperty("ATTACK_TRAIT_HATE_MOD", 1.);
		DEFENCE_TRAIT_HATE_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_HATE_MOD", 1.);
		ATTACK_TRAIT_AGGRESSION_MOD = formulasSettings.getProperty("ATTACK_TRAIT_AGGRESSION_MOD", 1.);
		DEFENCE_TRAIT_AGGRESSION_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_AGGRESSION_MOD", 1.);
		ATTACK_TRAIT_AIRBIND_MOD = formulasSettings.getProperty("ATTACK_TRAIT_AIRBIND_MOD", 1.);
		DEFENCE_TRAIT_AIRBIND_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_AIRBIND_MOD", 1.);
		ATTACK_TRAIT_DISARM_MOD = formulasSettings.getProperty("ATTACK_TRAIT_DISARM_MOD", 1.);
		DEFENCE_TRAIT_DISARM_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_DISARM_MOD", 1.);
		ATTACK_TRAIT_DEPORT_MOD = formulasSettings.getProperty("ATTACK_TRAIT_DEPORT_MOD", 1.);
		DEFENCE_TRAIT_DEPORT_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_DEPORT_MOD", 1.);
		ATTACK_TRAIT_CHANGEBODY_MOD = formulasSettings.getProperty("ATTACK_TRAIT_CHANGEBODY_MOD", 1.);
		DEFENCE_TRAIT_CHANGEBODY_MOD = formulasSettings.getProperty("DEFENCE_TRAIT_CHANGEBODY_MOD", 1.);
	}

	public static void loadDevelopSettings()
	{
		ExProperties p = load(DEVELOP_FILE);
	}

	public static void loadExtSettings()
	{
		ExProperties properties = load(EXT_FILE);

		EX_NEW_PETITION_SYSTEM = properties.getProperty("NewPetitionSystem", false);
		EX_JAPAN_MINIGAME = properties.getProperty("JapanMinigame", false);
		EX_LECTURE_MARK = properties.getProperty("LectureMark", false);

		EX_SECOND_AUTH_ENABLED = properties.getProperty("SecondAuthEnabled", false);
		EX_SECOND_AUTH_MAX_ATTEMPTS = properties.getProperty("SecondAuthMaxAttempts", 5);
		EX_SECOND_AUTH_BAN_TIME = properties.getProperty("SecondAuthBanTime", 480);

		EX_USE_QUEST_REWARD_PENALTY_PER = properties.getProperty("UseQuestRewardPenaltyPer", false);
		EX_F2P_QUEST_REWARD_PENALTY_PER = properties.getProperty("F2PQuestRewardPenaltyPer", 0);
		EX_F2P_QUEST_REWARD_PENALTY_QUESTS = new TIntHashSet();
		EX_F2P_QUEST_REWARD_PENALTY_QUESTS.addAll(properties.getProperty("F2PQuestRewardPenaltyQuests", new int[0]));

		EX_USE_PREMIUM_HENNA_SLOT = properties.getProperty("UsePremiumHennaSlot", false);

		VIP_ATTENDANCE_REWARDS_ENABLED = properties.getProperty("UseVIPAttendance", false);

		EX_USE_AUTO_SOUL_SHOT = properties.getProperty("UseAutoSoulShot", true);

		EX_USE_TO_DO_LIST = properties.getProperty("UseToDoList", true);

		EX_USE_PLEDGE_BONUS = properties.getProperty("UsePledgeBonus", true);

		EX_USE_PRIME_SHOP = properties.getProperty("UsePrimeShop", false);
	}

	public static void loadBBSSettings()
	{
		ExProperties properties = load(BBS_FILE_GENERAL);

		BBS_ENABLED = properties.getProperty("ENABLED", true);
		BBS_DEFAULT_PAGE = properties.getProperty("DEFAULT_PAGE", "_bbshome");
		BBS_COPYRIGHT = properties.getProperty("COPYRIGHT", "(c) L2Studio 2022");
		BBS_WAREHOUSE_ENABLED = properties.getProperty("WAREHOUSE_ENABLED", false);
		BBS_SELL_ITEMS_ENABLED = properties.getProperty("SELL_ITEMS_ENABLED", false);
		BBS_AUGMENTATION_ENABLED = properties.getProperty("AUGMENTATION_ENABLED", false);
	}

	public static void loadAltSettings()
	{
		ExProperties altSettings = load(ALT_SETTINGS_FILE);

		STARTING_LVL = altSettings.getProperty("StartingLvl", 1);
		STARTING_SP = altSettings.getProperty("StartingSP", 0L);
		ALT_ARENA_EXP = altSettings.getProperty("ArenaExp", true);
		ALT_GAME_DELEVEL = altSettings.getProperty("Delevel", true);
		ALLOW_DELEVEL_COMMAND = altSettings.getProperty("AllowDelevelCommand", false);
		ALT_SAVE_UNSAVEABLE = altSettings.getProperty("AltSaveUnsaveable", false);
		ALT_SAVE_EFFECTS_REMAINING_TIME = altSettings.getProperty("AltSaveEffectsRemainingTime", 5);
		ALT_SHOW_REUSE_MSG = altSettings.getProperty("AltShowSkillReuseMessage", true);
		ALT_DELETE_SA_BUFFS = altSettings.getProperty("AltDeleteSABuffs", false);
		AUTO_LOOT = altSettings.getProperty("AutoLoot", false);
		AUTO_LOOT_HERBS = altSettings.getProperty("AutoLootHerbs", false);
		AUTO_LOOT_ONLY_ADENA = altSettings.getProperty("AutoLootOnlyAdena", true);
		AUTO_LOOT_INDIVIDUAL = altSettings.getProperty("AutoLootIndividual", false);
		AUTO_LOOT_FROM_RAIDS = altSettings.getProperty("AutoLootFromRaids", false);

		String[] autoLootItemIdList = altSettings.getProperty("AutoLootItemIdList", "-1").split(";");
		for(String item : autoLootItemIdList)
		{
			if(item == null || item.isEmpty())
			{
				continue;
			}

			try
			{
				int itemId = Integer.parseInt(item);
				if(itemId > 0)
				{
					AUTO_LOOT_ITEM_ID_LIST.add(itemId);
				}
			}
			catch(NumberFormatException e)
			{
				_log.error("", e);
			}
		}

		AUTO_LOOT_PK = altSettings.getProperty("AutoLootPK", false);
		ALT_GAME_KARMA_PLAYER_CAN_SHOP = altSettings.getProperty("AltKarmaPlayerCanShop", false);
		SAVING_SPS = altSettings.getProperty("SavingSpS", false);
		CAN_SELF_DISPEL_SONG = altSettings.getProperty("Selfdispelsong", false);
		MANAHEAL_SPS_BONUS = altSettings.getProperty("ManahealSpSBonus", false);
		ALT_RAID_RESPAWN_MULTIPLIER = altSettings.getProperty("AltRaidRespawnMultiplier", 1.0);
		DEFAULT_RAID_MINIONS_RESPAWN_DELAY = altSettings.getProperty("DEFAULT_RAID_MINIONS_RESPAWN_DELAY", 120);
		ALLOW_AUGMENTATION = altSettings.getProperty("ALLOW_AUGMENTATION", true);
		ALT_ALLOW_DROP_AUGMENTED = altSettings.getProperty("AlowDropAugmented", true);
		ALT_GAME_UNREGISTER_RECIPE = altSettings.getProperty("AltUnregisterRecipe", true);
		ALT_GAME_SHOW_DROPLIST = altSettings.getProperty("AltShowDroplist", false);
		ALLOW_NPC_SHIFTCLICK = altSettings.getProperty("AllowShiftClick", false);
		SHOW_TARGET_PLAYER_INVENTORY_ON_SHIFT_CLICK = altSettings.getProperty("SHOW_TARGET_PLAYER_INVENTORY_ON_SHIFT_CLICK", false);
		ALLOW_VOICED_COMMANDS = altSettings.getProperty("AllowVoicedCommands", false);
		ALLOW_AUTOHEAL_COMMANDS = altSettings.getProperty("ALLOW_AUTOHEAL_COMMANDS", false);
		ENABLE_SUB_CLASSES = altSettings.getProperty("ENABLE_SUB_CLASSES", false);
		ALT_GAME_LEVEL_TO_GET_SUBCLASS = altSettings.getProperty("AltLevelToGetSubclass", 75);
		ALT_MAX_LEVEL = altSettings.getProperty("AltMaxLevel", 99);
		ALT_MAX_SUB_LEVEL = altSettings.getProperty("AltMaxSubLevel", 90);
		ALT_ALLOW_OTHERS_WITHDRAW_FROM_CLAN_WAREHOUSE = altSettings.getProperty("AltAllowOthersWithdrawFromClanWarehouse", false);
		ALT_ALLOW_CLAN_COMMAND_ONLY_FOR_CLAN_LEADER = altSettings.getProperty("AltAllowClanCommandOnlyForClanLeader", true);

		BAN_FOR_CFG_USAGE = altSettings.getProperty("BanForCfgUsageAgainsBots", false);

		ALT_ADD_RECIPES = altSettings.getProperty("AltAddRecipes", 0);
		SS_ANNOUNCE_PERIOD = altSettings.getProperty("SSAnnouncePeriod", 0);
		PETITIONING_ALLOWED = altSettings.getProperty("PetitioningAllowed", true);
		MAX_PETITIONS_PER_PLAYER = altSettings.getProperty("MaxPetitionsPerPlayer", 5);
		MAX_PETITIONS_PENDING = altSettings.getProperty("MaxPetitionsPending", 25);
		AUTO_LEARN_SKILLS = altSettings.getProperty("AutoLearnSkills", false);
		ALT_SOCIAL_ACTION_REUSE = altSettings.getProperty("AltSocialActionReuse", false);

		DISABLED_SPELLBOOKS_FOR_ACQUIRE_TYPES = new HashSet<AcquireType>();
		for(String t : altSettings.getProperty("DISABLED_SPELLBOOKS_FOR_ACQUIRE_TYPES", ArrayUtils.EMPTY_STRING_ARRAY))
		{
			if(t.trim().isEmpty())
			{
				continue;
			}
			DISABLED_SPELLBOOKS_FOR_ACQUIRE_TYPES.add(AcquireType.valueOf(t.toUpperCase()));
		}

		ALT_BUFF_LIMIT = altSettings.getProperty("BuffLimit", 20);
		ALT_MUSIC_LIMIT = altSettings.getProperty("ALT_MUSIC_LIMIT", 12);
		ALT_DEBUFF_LIMIT = altSettings.getProperty("ALT_DEBUFF_LIMIT", 12);
		ALT_TRIGGER_LIMIT = altSettings.getProperty("ALT_TRIGGER_LIMIT", 12);
		ALLOW_DEATH_PENALTY = altSettings.getProperty("EnableDeathPenalty", false);
		ALT_DEATH_PENALTY_CHANCE = altSettings.getProperty("DeathPenaltyChance", 10);
		ALT_DEATH_PENALTY_EXPERIENCE_PENALTY = altSettings.getProperty("DeathPenaltyRateExpPenalty", 1);
		ALT_DEATH_PENALTY_KARMA_PENALTY = altSettings.getProperty("DeathPenaltyC5RateKarma", 1);
		NONOWNER_ITEM_PICKUP_DELAY = altSettings.getProperty("NonOwnerItemPickupDelay", 15L) * 1000L;
		ALT_NO_LASTHIT = altSettings.getProperty("NoLasthitOnRaid", false);
		ALT_KAMALOKA_NIGHTMARES_PREMIUM_ONLY = altSettings.getProperty("KamalokaNightmaresPremiumOnly", false);
		ALT_PET_HEAL_BATTLE_ONLY = altSettings.getProperty("PetsHealOnlyInBattle", true);
		CHAR_TITLE = altSettings.getProperty("CharTitle", false);
		ADD_CHAR_TITLE = altSettings.getProperty("CharAddTitle", "");

		ALT_DISABLED_MULTISELL = altSettings.getProperty("DisabledMultisells", ArrayUtils.EMPTY_INT_ARRAY);
		ALT_SHOP_PRICE_LIMITS = altSettings.getProperty("ShopPriceLimits", ArrayUtils.EMPTY_INT_ARRAY);
		ALT_SHOP_UNALLOWED_ITEMS = altSettings.getProperty("ShopUnallowedItems", ArrayUtils.EMPTY_INT_ARRAY);

		ALT_ALLOWED_PET_POTIONS = altSettings.getProperty("AllowedPetPotions", new int[] {
				735,
				1060,
				1061,
				1062,
				1374,
				1375,
				1539,
				1540,
				6035,
				6036
		});

		FESTIVAL_MIN_PARTY_SIZE = altSettings.getProperty("FestivalMinPartySize", 5);
		FESTIVAL_RATE_PRICE = altSettings.getProperty("FestivalRatePrice", 1.0);

		RIFT_MIN_PARTY_SIZE = altSettings.getProperty("RiftMinPartySize", 5);
		RIFT_SPAWN_DELAY = altSettings.getProperty("RiftSpawnDelay", 10000);
		RIFT_MAX_JUMPS = altSettings.getProperty("MaxRiftJumps", 4);
		RIFT_AUTO_JUMPS_TIME = altSettings.getProperty("AutoJumpsDelay", 8);
		RIFT_AUTO_JUMPS_TIME_RAND = altSettings.getProperty("AutoJumpsDelayRandom", 120000);

		RIFT_ENTER_COST_RECRUIT = altSettings.getProperty("RecruitFC", 18);
		RIFT_ENTER_COST_SOLDIER = altSettings.getProperty("SoldierFC", 21);
		RIFT_ENTER_COST_OFFICER = altSettings.getProperty("OfficerFC", 24);
		RIFT_ENTER_COST_CAPTAIN = altSettings.getProperty("CaptainFC", 27);
		RIFT_ENTER_COST_COMMANDER = altSettings.getProperty("CommanderFC", 30);
		RIFT_ENTER_COST_HERO = altSettings.getProperty("HeroFC", 33);
		ALLOW_CLANSKILLS = altSettings.getProperty("AllowClanSkills", true);
		ALLOW_LEARN_TRANS_SKILLS_WO_QUEST = altSettings.getProperty("AllowLearnTransSkillsWOQuest", false);
		MAXIMUM_MEMBERS_IN_PARTY = altSettings.getProperty("MAXIMUM_MEMBERS_IN_PARTY", 9);
		PARTY_LEADER_ONLY_CAN_INVITE = altSettings.getProperty("PartyLeaderOnlyCanInvite", true);
		ALLOW_TALK_WHILE_SITTING = altSettings.getProperty("AllowTalkWhileSitting", true);
		ALLOW_NOBLE_TP_TO_ALL = altSettings.getProperty("AllowNobleTPToAll", false);

		BUFFTIME_MODIFIER = altSettings.getProperty("BuffTimeModifier", 1.0);
		BUFFTIME_MODIFIER_SKILLS = altSettings.getProperty("BuffTimeModifierSkills", new int[0]);
		CLANHALL_BUFFTIME_MODIFIER = altSettings.getProperty("ClanHallBuffTimeModifier", 1.0);
		SONGDANCETIME_MODIFIER = altSettings.getProperty("SongDanceTimeModifier", 1.0);
		MAXLOAD_MODIFIER = altSettings.getProperty("MaxLoadModifier", 1.0);
		GATEKEEPER_MODIFIER = altSettings.getProperty("GkCostMultiplier", 1.0);
		GATEKEEPER_FREE = altSettings.getProperty("GkFree", 40);

		ALT_CHAMPION_CHANCE1 = altSettings.getProperty("AltChampionChance1", 0.);
		ALT_CHAMPION_CHANCE2 = altSettings.getProperty("AltChampionChance2", 0.);
		ALT_CHAMPION_CAN_BE_AGGRO = altSettings.getProperty("AltChampionAggro", false);
		ALT_CHAMPION_CAN_BE_SOCIAL = altSettings.getProperty("AltChampionSocial", false);
		ALT_CHAMPION_MIN_LEVEL = altSettings.getProperty("AltChampionMinLevel", 40);
		ALT_CHAMPION_TOP_LEVEL = altSettings.getProperty("AltChampionTopLevel", 75);
		SPECIAL_ITEM_ID = altSettings.getProperty("ChampionSpecialItem", 0);
		SPECIAL_ITEM_COUNT = altSettings.getProperty("ChampionSpecialItemCount", 1);
		SPECIAL_ITEM_DROP_CHANCE = altSettings.getProperty("ChampionSpecialItemDropChance", 100.);

		ALT_SAYHAS_GRACE_RATE = altSettings.getProperty("AltSayhasGraceRate", 300.) / 100;
		ALT_SAYHAS_GRACE_PA_RATE = altSettings.getProperty("AltSayhasGracePArate", 400.) / 100;
		ALT_SAYHAS_GRACE_CONSUME_RATE = altSettings.getProperty("AltSayhasGraceConsumeRate", 1.);
		RATE_LIMITED_SAYHA_GRACE_EXP_MULTIPLIER = altSettings.getProperty("RateLimitedSayhaGraceExpMultiplier", 200.) / 100.;
		ALT_PCBANG_POINTS_ENABLED = altSettings.getProperty("AltPcBangPointsEnabled", false);
		PC_BANG_POINTS_BY_ACCOUNT = altSettings.getProperty("PC_BANG_POINTS_BY_ACCOUNT", false);
		ALT_PCBANG_POINTS_ONLY_PREMIUM = altSettings.getProperty("AltPcBangPointsOnlyPA", false);
		ALT_PCBANG_POINTS_BONUS_DOUBLE_CHANCE = altSettings.getProperty("AltPcBangPointsDoubleChance", 10.);
		ALT_PCBANG_POINTS_BONUS = altSettings.getProperty("AltPcBangPointsBonus", 0);
		ALT_PCBANG_POINTS_DELAY = altSettings.getProperty("AltPcBangPointsDelay", 20);
		ALT_PCBANG_POINTS_MIN_LVL = altSettings.getProperty("AltPcBangPointsMinLvl", 1);
		ALT_ALLOWED_MULTISELLS_IN_PCBANG.addAll(altSettings.getProperty("ALT_ALLOWED_MULTISELLS_IN_PCBANG", new int[0]));

		ALT_PCBANG_POINTS_MAX_CODE_ENTER_ATTEMPTS = altSettings.getProperty("ALT_PCBANG_POINTS_MAX_CODE_ENTER_ATTEMPTS", 5);
		ALT_PCBANG_POINTS_BAN_TIME = altSettings.getProperty("ALT_PCBANG_POINTS_BAN_TIME", 480);

		ALT_DEBUG_ENABLED = altSettings.getProperty("AltDebugEnabled", false);
		ALT_LOG_NOTDONE_SKILLS = altSettings.getProperty("AltLogNotDoneSkills", false);
		ALT_LOG_NOTDONE_ONLY_FOR_LEARN_SKILLS = altSettings.getProperty("AltLogNotDoneSkillsOnlyForLearn", false);

		ALT_MAX_ALLY_SIZE = altSettings.getProperty("AltMaxAllySize", 3);
		ALT_PARTY_DISTRIBUTION_RANGE = altSettings.getProperty("AltPartyDistributionRange", 1500);
		ALT_PARTY_BONUS = altSettings.getProperty("AltPartyBonus", new double[] {
				1.00,
				1.30,
				1.35,
				1.40,
				1.55,
				1.60,
				1.70,
				1.80,
				2.00
		});
		ALT_PARTY_CLAN_BONUS = altSettings.getProperty("ALT_PARTY_CLAN_BONUS", new double[] {
				1.00
		});
		ALT_PARTY_LVL_DIFF_PENALTY = altSettings.getProperty("ALT_PARTY_LVL_DIFF_PENALTY", new int[] {
				100,
				98,
				95,
				93,
				91,
				88,
				86,
				83,
				81,
				78,
				23,
				22,
				21,
				20,
				19,
				0
		});

		ALT_ALL_PHYS_SKILLS_OVERHIT = altSettings.getProperty("AltAllPhysSkillsOverhit", true);
		ALT_REMOVE_SKILLS_ON_DELEVEL = altSettings.getProperty("AltRemoveSkillsOnDelevel", true);
		ALLOW_CH_DOOR_OPEN_ON_CLICK = altSettings.getProperty("AllowChDoorOpenOnClick", true);
		ALT_CH_SIMPLE_DIALOG = altSettings.getProperty("AltChSimpleDialog", false);
		ALT_CH_UNLIM_MP = altSettings.getProperty("ALT_CH_UNLIM_MP", true);
		ALT_NO_FAME_FOR_DEAD = altSettings.getProperty("AltNoFameForDead", false);

		ALT_SHOW_SERVER_TIME = altSettings.getProperty("ShowServerTime", false);

		FOLLOW_RANGE = altSettings.getProperty("FollowRange", 100);

		ALT_ITEM_AUCTION_ENABLED = altSettings.getProperty("AltItemAuctionEnabled", true);
		ALT_CUSTOM_ITEM_AUCTION_ENABLED = ALT_ITEM_AUCTION_ENABLED && altSettings.getProperty("AltCustomItemAuctionEnabled", false);
		ALT_ITEM_AUCTION_CAN_REBID = altSettings.getProperty("AltItemAuctionCanRebid", false);
		ALT_ITEM_AUCTION_START_ANNOUNCE = altSettings.getProperty("AltItemAuctionAnnounce", true);
		ALT_ITEM_AUCTION_MAX_BID = altSettings.getProperty("AltItemAuctionMaxBid", 1000000L);
		ALT_ITEM_AUCTION_MAX_CANCEL_TIME_IN_MILLIS = altSettings.getProperty("AltItemAuctionMaxCancelTimeInMillis", 604800000);

		ALT_ENABLE_BLOCK_CHECKER_EVENT = altSettings.getProperty("EnableBlockCheckerEvent", true);
		ALT_MIN_BLOCK_CHECKER_TEAM_MEMBERS = Math.min(Math.max(altSettings.getProperty("BlockCheckerMinOlympiadMembers", 1), 1), 6);
		ALT_RATE_COINS_REWARD_BLOCK_CHECKER = altSettings.getProperty("BlockCheckerRateCoinReward", 1.);

		ALT_HBCE_FAIR_PLAY = altSettings.getProperty("HBCEFairPlay", false);

		ALT_PET_INVENTORY_LIMIT = altSettings.getProperty("AltPetInventoryLimit", 12);

		ALLOW_FAKE_PLAYERS = altSettings.getProperty("AllowFake", false);
		FAKE_PLAYERS_PERCENT = altSettings.getProperty("FakePercent", 0);

		DISABLE_CRYSTALIZATION_ITEMS = altSettings.getProperty("DisableCrystalizationItems", false);

		SUB_START_LEVEL = altSettings.getProperty("SubClassStartLevel", 40);
		START_CLAN_LEVEL = altSettings.getProperty("ClanStartLevel", 0);
		NEW_CHAR_IS_NOBLE = altSettings.getProperty("IsNewCharNoble", false);
		ENABLE_L2_TOP_OVERONLINE = altSettings.getProperty("EnableL2TOPFakeOnline", false);
		L2TOP_MAX_ONLINE = altSettings.getProperty("L2TOPMaxOnline", 3000);
		MIN_ONLINE_0_5_AM = altSettings.getProperty("MinOnlineFrom00to05", 500);
		MAX_ONLINE_0_5_AM = altSettings.getProperty("MaxOnlineFrom00to05", 700);
		MIN_ONLINE_6_11_AM = altSettings.getProperty("MinOnlineFrom06to11", 700);
		MAX_ONLINE_6_11_AM = altSettings.getProperty("MaxOnlineFrom06to11", 1000);
		MIN_ONLINE_12_6_PM = altSettings.getProperty("MinOnlineFrom12to18", 1000);
		MAX_ONLINE_12_6_PM = altSettings.getProperty("MaxOnlineFrom12to18", 1500);
		MIN_ONLINE_7_11_PM = altSettings.getProperty("MinOnlineFrom19to23", 1500);
		MAX_ONLINE_7_11_PM = altSettings.getProperty("MaxOnlineFrom19to23", 2500);
		ADD_ONLINE_ON_SIMPLE_DAY = altSettings.getProperty("AddOnlineIfSimpleDay", 50);
		ADD_ONLINE_ON_WEEKEND = altSettings.getProperty("AddOnlineIfWeekend", 300);
		L2TOP_MIN_TRADERS = altSettings.getProperty("L2TOPMinTraders", 80);
		L2TOP_MAX_TRADERS = altSettings.getProperty("L2TOPMaxTraders", 190);
		ALT_SELL_ITEM_ONE_ADENA = altSettings.getProperty("AltSellItemOneAdena", false);
		SKILL_ABSORB = altSettings.getProperty("AltSkillAbsorb", false);
		BOW_ABSORB = altSettings.getProperty("AltBowAbsorb", false);
		PHYS_ABSORB = altSettings.getProperty("AltPhysAbsorb", false);

		SKILL_ABSORBMP = altSettings.getProperty("AltSkillAbsorbMP", false);
		BOW_ABSORBMP = altSettings.getProperty("AltBowAbsorbMP", false);
		PHYS_ABSORBMP = altSettings.getProperty("AltPhysAbsorbMP", false);

		MAX_SIEGE_CLANS = altSettings.getProperty("MaxSiegeClans", 20);
		ONLY_ONE_SIEGE_PER_CLAN = altSettings.getProperty("OneClanCanRegisterOnOneSiege", false);

		CLAN_WAR_LIMIT = altSettings.getProperty("CLAN_WAR_LIMIT", 30);
		CLAN_WAR_REPUTATION_SCORE_PER_KILL = altSettings.getProperty("CLAN_WAR_REPUTATION_SCORE_PER_KILL", 1);
		CLAN_WAR_CANCEL_REPUTATION_PENALTY = altSettings.getProperty("CLAN_WAR_CANCEL_REPUTATION_PENALTY", 500);

		NEED_QUEST_FOR_PROOF = altSettings.getProperty("NeedQuestForProff", true);

		LIST_OF_SELLABLE_ITEMS = new ArrayList<Integer>();
		for(int id : altSettings.getProperty("ListOfAlwaysSellableItems", new int[] {
				57
		}))
			LIST_OF_SELLABLE_ITEMS.add(id);
		LIST_OF_TRABLE_ITEMS = new ArrayList<Integer>();
		for(int id : altSettings.getProperty("ListOfAlwaysTradableItems", new int[] {
				57
		}))
			LIST_OF_TRABLE_ITEMS.add(id);

		ALLOW_USE_DOORMANS_IN_SIEGE_BY_OWNERS = altSettings.getProperty("AllowUseDoormansInSiegeByOwners", true);

		NPC_RANDOM_ENCHANT = altSettings.getProperty("NpcRandomEnchant", false);
		ENABLE_PARTY_SEARCH = altSettings.getProperty("AllowPartySearch", false);
		MENTOR_ONLY_PA = altSettings.getProperty("MentorServiceOnlyForPremium", false);

		ALT_SHOW_MONSTERS_AGRESSION = altSettings.getProperty("AltShowMonstersAgression", false);
		ALT_SHOW_MONSTERS_LVL = altSettings.getProperty("AltShowMonstersLvL", false);

		ALT_TELEPORT_TO_TOWN_DURING_SIEGE = altSettings.getProperty("ALT_TELEPORT_TO_TOWN_DURING_SIEGE", true);

		ALT_CLAN_LEAVE_PENALTY_TIME = altSettings.getProperty("ALT_CLAN_LEAVE_PENALTY_TIME", 24);
		ALT_CLAN_CREATE_PENALTY_TIME = altSettings.getProperty("ALT_CLAN_CREATE_PENALTY_TIME", 240);

		ALT_EXPELLED_MEMBER_PENALTY_TIME = altSettings.getProperty("ALT_EXPELLED_MEMBER_PENALTY_TIME", 24);
		ALT_LEAVED_ALLY_PENALTY_TIME = altSettings.getProperty("ALT_LEAVED_ALLY_PENALTY_TIME", 24);
		ALT_DISSOLVED_ALLY_PENALTY_TIME = altSettings.getProperty("ALT_DISSOLVED_ALLY_PENALTY_TIME", 24);

		MIN_RAID_LEVEL_TO_DROP = altSettings.getProperty("MinRaidLevelToDropItem", 0);

		RAID_DROP_GLOBAL_ITEMS = altSettings.getProperty("AltEnableGlobalRaidDrop", false);
		String[] infos = altSettings.getProperty("RaidGlobalDrop", new String[0], ";");
		for(String info : infos)
		{
			if(info.isEmpty())
				continue;

			String[] data = info.split(",");
			int id = Integer.parseInt(data[0]);
			long count = Long.parseLong(data[1]);
			double chance = Double.parseDouble(data[2]);
			RAID_GLOBAL_DROP.add(new RaidGlobalDrop(id, count, chance));
		}

		NPC_DIALOG_PLAYER_DELAY = altSettings.getProperty("NpcDialogPlayerDelay", 0);

		CLAN_DELETE_TIME = altSettings.getProperty("CLAN_DELETE_TIME", "0 5 * * 2");
		CLAN_CHANGE_LEADER_TIME = altSettings.getProperty("CLAN_CHANGE_LEADER_TIME", "0 5 * * 2");

		CLAN_MAX_LEVEL = altSettings.getProperty("CLAN_MAX_LEVEL", 10);
		CLAN_KILLED_MOBS_TO_POINT = altSettings.getProperty("ClanKilledMobsToPoint", 500);

		CLAN_ATTENDANCE_REWARD_1 = altSettings.getProperty("CLAN_ATTENDANCE_REWARD_1", 55168);
		CLAN_ATTENDANCE_REWARD_2 = altSettings.getProperty("CLAN_ATTENDANCE_REWARD_2", 55169);
		CLAN_ATTENDANCE_REWARD_3 = altSettings.getProperty("CLAN_ATTENDANCE_REWARD_3", 55170);
		CLAN_ATTENDANCE_REWARD_4 = altSettings.getProperty("CLAN_ATTENDANCE_REWARD_4", 55171);

		CLAN_HUNTING_REWARD_1 = altSettings.getProperty("CLAN_HUNTING_REWARD_1", 70020);
		CLAN_HUNTING_REWARD_2 = altSettings.getProperty("CLAN_HUNTING_REWARD_2", 70021);
		CLAN_HUNTING_REWARD_3 = altSettings.getProperty("CLAN_HUNTING_REWARD_3", 70022);
		CLAN_HUNTING_REWARD_4 = altSettings.getProperty("CLAN_HUNTING_REWARD_4", 70023);

		CLAN_HUNTING_PROGRESS_RATE = altSettings.getProperty("CLAN_HUNTING_PROGRESS_RATE", 1.);

		REFLECT_DAMAGE_CAPPED_BY_PDEF = altSettings.getProperty("ReflectDamageCappedByPDef", false);

		ALT_DELEVEL_ON_DEATH_PENALTY_MIN_LEVEL = altSettings.getProperty("ALT_DELEVEL_ON_DEATH_PENALTY_MIN_LEVEL", 10);

		ALT_PETS_NOT_STARVING = altSettings.getProperty("ALT_PETS_NOT_STARVING", false);

		SHOW_TARGET_PLAYER_BUFF_EFFECTS = altSettings.getProperty("SHOW_TARGET_PLAYER_BUFF_EFFECTS", false);
		SHOW_TARGET_PLAYER_DEBUFF_EFFECTS = altSettings.getProperty("SHOW_TARGET_PLAYER_DEBUFF_EFFECTS", false);
		SHOW_TARGET_NPC_BUFF_EFFECTS = altSettings.getProperty("SHOW_TARGET_NPC_BUFF_EFFECTS", false);
		SHOW_TARGET_NPC_DEBUFF_EFFECTS = altSettings.getProperty("SHOW_TARGET_NPC_DEBUFF_EFFECTS", false);

		PERCENT_LOST_ON_DEATH = new double[Byte.MAX_VALUE];
		double prevPercentLost = 0.;
		for(int i = 1; i < PERCENT_LOST_ON_DEATH.length; i++)
		{
			double percent = altSettings.getProperty("PERCENT_LOST_ON_DEATH_LVL_" + i, prevPercentLost);
			PERCENT_LOST_ON_DEATH[i] = percent;
			if(percent != prevPercentLost)
				prevPercentLost = percent;
		}

		PERCENT_LOST_ON_DEATH_MOD_IN_PEACE_ZONE = altSettings.getProperty("PERCENT_LOST_ON_DEATH_MOD_IN_PEACE_ZONE", 0.);
		PERCENT_LOST_ON_DEATH_MOD_IN_PVP = altSettings.getProperty("PERCENT_LOST_ON_DEATH_MOD_IN_PVP", 1.);
		PERCENT_LOST_ON_DEATH_MOD_IN_WAR = altSettings.getProperty("PERCENT_LOST_ON_DEATH_MOD_IN_WAR", 0.25);
		PERCENT_LOST_ON_DEATH_MOD_FOR_PK = altSettings.getProperty("PERCENT_LOST_ON_DEATH_MOD_FOR_PK", 1.);

		ALT_EASY_RECIPES = altSettings.getProperty("EasyRecipiesExtraFeature", false);

		ALT_USE_TRANSFORM_IN_EPIC_ZONE = altSettings.getProperty("ALT_USE_TRANSFORM_IN_EPIC_ZONE", true);

		ALT_ANNONCE_RAID_BOSSES_REVIVAL = altSettings.getProperty("ALT_ANNONCE_RAID_BOSSES_REVIVAL", false);

		ALT_SAVE_PRIVATE_STORE = altSettings.getProperty("ALT_SAVE_PRIVATE_STORE", false);

		String allowedTradeZones = altSettings.getProperty("ALLOWED_TRADE_ZONES", "");
		if(StringUtils.isEmpty(allowedTradeZones))
		{
			ALLOWED_TRADE_ZONES = new String[0];
		}
		else
		{
			ALLOWED_TRADE_ZONES = allowedTradeZones.split(";");
		}
		MULTICLASS_SYSTEM_ENABLED = altSettings.getProperty("MULTICLASS_SYSTEM_ENABLED", false);
		MULTICLASS_SYSTEM_SHOW_LEARN_LIST_ON_OPEN_SKILL_LIST = altSettings.getProperty("MULTICLASS_SYSTEM_SHOW_LEARN_LIST_ON_OPEN_SKILL_LIST", false);
		MULTICLASS_SYSTEM_NON_CLASS_SP_MODIFIER = altSettings.getProperty("MULTICLASS_SYSTEM_NON_CLASS_SP_MODIFIER", 1.0);
		MULTICLASS_SYSTEM_1ST_CLASS_SP_MODIFIER = altSettings.getProperty("MULTICLASS_SYSTEM_1ST_CLASS_SP_MODIFIER", 1.0);
		MULTICLASS_SYSTEM_2ND_CLASS_SP_MODIFIER = altSettings.getProperty("MULTICLASS_SYSTEM_2ND_CLASS_SP_MODIFIER", 1.0);
		MULTICLASS_SYSTEM_3RD_CLASS_SP_MODIFIER = altSettings.getProperty("MULTICLASS_SYSTEM_3RD_CLASS_SP_MODIFIER", 1.0);
		MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_ID_BASED_ON_SP = altSettings.getProperty("MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_ID_BASED_ON_SP", 0);
		MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_ID_BASED_ON_SP = altSettings.getProperty("MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_ID_BASED_ON_SP", 0);
		MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_ID_BASED_ON_SP = altSettings.getProperty("MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_ID_BASED_ON_SP", 0);
		MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_ID_BASED_ON_SP = altSettings.getProperty("MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_ID_BASED_ON_SP", 0);
		MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP = altSettings.getProperty("MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP", 1.0);
		MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP = altSettings.getProperty("MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP", 1.0);
		MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP = altSettings.getProperty("MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP", 1.0);
		MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP = altSettings.getProperty("MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_COUNT_MODIFIER_BASED_ON_SP", 1.0);
		MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_ID = altSettings.getProperty("MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_ID", 0);
		MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_ID = altSettings.getProperty("MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_ID", 0);
		MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_ID = altSettings.getProperty("MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_ID", 0);
		MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_ID = altSettings.getProperty("MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_ID", 0);
		MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_COUNT = altSettings.getProperty("MULTICLASS_SYSTEM_NON_CLASS_COST_ITEM_COUNT", 0L);
		MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_COUNT = altSettings.getProperty("MULTICLASS_SYSTEM_1ST_CLASS_COST_ITEM_COUNT", 0L);
		MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_COUNT = altSettings.getProperty("MULTICLASS_SYSTEM_2ND_CLASS_COST_ITEM_COUNT", 0L);
		MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_COUNT = altSettings.getProperty("MULTICLASS_SYSTEM_3RD_CLASS_COST_ITEM_COUNT", 0L);

		BATTLE_ZONE_AROUND_RAID_BOSSES_RANGE = altSettings.getProperty("BATTLE_ZONE_AROUND_RAID_BOSSES_RANGE", 0);

		AUTOFARM_IN_PEACE_ZONE = altSettings.getProperty("AutoFarmInPeaceZone", false);

		FORTRESS_REWARD_ID = altSettings.getProperty("FortressRewardID", 57);
		FORTRESS_REWARD_COUNT = altSettings.getProperty("FortressRewardCount", 30000000);
	}

	public static void loadServicesSettings()
	{
		ExProperties servicesSettings = load(SERVICES_FILE);

		ITEM_BROKER_ITEM_SEARCH = servicesSettings.getProperty("ITEM_BROKER_ITEM_SEARCH", false);

		SPAWN_VITAMIN_MANAGER = servicesSettings.getProperty("SPAWN_VITAMIN_MANAGER", true);

		SERVICES_CHANGE_NICK_ENABLED = servicesSettings.getProperty("NickChangeEnabled", false);
		SERVICES_CHANGE_NICK_PRICE = servicesSettings.getProperty("NickChangePrice", 100);
		SERVICES_CHANGE_NICK_ITEM = servicesSettings.getProperty("NickChangeItem", 4037);

		ALLOW_CHANGE_PASSWORD_COMMAND = servicesSettings.getProperty("ALLOW_CHANGE_PASSWORD_COMMAND", false);
		ALLOW_CHANGE_PHONE_NUMBER_COMMAND = servicesSettings.getProperty("ALLOW_CHANGE_PHONE_NUMBER_COMMAND", false);
		FORCIBLY_SPECIFY_PHONE_NUMBER = servicesSettings.getProperty("FORCIBLY_SPECIFY_PHONE_NUMBER", false);

		SERVICES_CHANGE_CLAN_NAME_ENABLED = servicesSettings.getProperty("ClanNameChangeEnabled", false);
		SERVICES_CHANGE_CLAN_NAME_PRICE = servicesSettings.getProperty("ClanNameChangePrice", 100);
		SERVICES_CHANGE_CLAN_NAME_ITEM = servicesSettings.getProperty("ClanNameChangeItem", 4037);
		ALLOW_TOTAL_ONLINE = servicesSettings.getProperty("AllowVoiceCommandOnline", false);
		ALLOW_ONLINE_PARSE = servicesSettings.getProperty("AllowParsTotalOnline", false);
		FIRST_UPDATE = servicesSettings.getProperty("FirstOnlineUpdate", 1);
		DELAY_UPDATE = servicesSettings.getProperty("OnlineUpdate", 5);

		SERVICES_CHANGE_PET_NAME_ENABLED = servicesSettings.getProperty("PetNameChangeEnabled", false);
		SERVICES_CHANGE_PET_NAME_PRICE = servicesSettings.getProperty("PetNameChangePrice", 100);
		SERVICES_CHANGE_PET_NAME_ITEM = servicesSettings.getProperty("PetNameChangeItem", 4037);

		SERVICES_EXCHANGE_BABY_PET_ENABLED = servicesSettings.getProperty("BabyPetExchangeEnabled", false);
		SERVICES_EXCHANGE_BABY_PET_PRICE = servicesSettings.getProperty("BabyPetExchangePrice", 100);
		SERVICES_EXCHANGE_BABY_PET_ITEM = servicesSettings.getProperty("BabyPetExchangeItem", 4037);

		SERVICES_CHANGE_SEX_ENABLED = servicesSettings.getProperty("SexChangeEnabled", false);
		SERVICES_CHANGE_SEX_PRICE = servicesSettings.getProperty("SexChangePrice", 100);
		SERVICES_CHANGE_SEX_ITEM = servicesSettings.getProperty("SexChangeItem", 4037);

		SERVICES_CHANGE_BASE_ENABLED = servicesSettings.getProperty("BaseChangeEnabled", false);
		SERVICES_CHANGE_BASE_PRICE = servicesSettings.getProperty("BaseChangePrice", 100);
		SERVICES_CHANGE_BASE_ITEM = servicesSettings.getProperty("BaseChangeItem", 4037);

		SERVICES_SEPARATE_SUB_ENABLED = servicesSettings.getProperty("SeparateSubEnabled", false);
		SERVICES_SEPARATE_SUB_PRICE = servicesSettings.getProperty("SeparateSubPrice", 100);
		SERVICES_SEPARATE_SUB_ITEM = servicesSettings.getProperty("SeparateSubItem", 4037);

		SERVICES_CHANGE_NICK_COLOR_ENABLED = servicesSettings.getProperty("NickColorChangeEnabled", false);
		SERVICES_CHANGE_NICK_COLOR_PRICE = servicesSettings.getProperty("NickColorChangePrice", 100);
		SERVICES_CHANGE_NICK_COLOR_ITEM = servicesSettings.getProperty("NickColorChangeItem", 4037);
		SERVICES_CHANGE_NICK_COLOR_LIST = servicesSettings.getProperty("NickColorChangeList", new String[] {
				"00FF00"
		});

		SERVICES_BASH_ENABLED = servicesSettings.getProperty("BashEnabled", false);
		SERVICES_BASH_SKIP_DOWNLOAD = servicesSettings.getProperty("BashSkipDownload", false);
		SERVICES_BASH_RELOAD_TIME = servicesSettings.getProperty("BashReloadTime", 24);

		SERVICES_NOBLESS_SELL_ENABLED = servicesSettings.getProperty("NoblessSellEnabled", false);
		SERVICES_NOBLESS_SELL_PRICE = servicesSettings.getProperty("NoblessSellPrice", 1000);
		SERVICES_NOBLESS_SELL_ITEM = servicesSettings.getProperty("NoblessSellItem", 4037);

		SERVICES_EXPAND_INVENTORY_ENABLED = servicesSettings.getProperty("ExpandInventoryEnabled", false);
		SERVICES_EXPAND_INVENTORY_PRICE = servicesSettings.getProperty("ExpandInventoryPrice", 1000);
		SERVICES_EXPAND_INVENTORY_ITEM = servicesSettings.getProperty("ExpandInventoryItem", 4037);
		SERVICES_EXPAND_INVENTORY_MAX = servicesSettings.getProperty("ExpandInventoryMax", 250);

		SERVICES_EXPAND_WAREHOUSE_ENABLED = servicesSettings.getProperty("ExpandWarehouseEnabled", false);
		SERVICES_EXPAND_WAREHOUSE_PRICE = servicesSettings.getProperty("ExpandWarehousePrice", 1000);
		SERVICES_EXPAND_WAREHOUSE_ITEM = servicesSettings.getProperty("ExpandWarehouseItem", 4037);

		SERVICES_EXPAND_CWH_ENABLED = servicesSettings.getProperty("ExpandCWHEnabled", false);
		SERVICES_EXPAND_CWH_PRICE = servicesSettings.getProperty("ExpandCWHPrice", 1000);
		SERVICES_EXPAND_CWH_ITEM = servicesSettings.getProperty("ExpandCWHItem", 4037);

		SERVICES_OFFLINE_TRADE_ALLOW = servicesSettings.getProperty("AllowOfflineTrade", false);
		SERVICES_OFFLINE_TRADE_ALLOW_ZONE = servicesSettings.getProperty("AllowOfflineTradeZone", 0);
		SERVICES_OFFLINE_TRADE_MIN_LEVEL = servicesSettings.getProperty("OfflineMinLevel", 0);
		SERVICES_OFFLINE_TRADE_NAME_COLOR = Integer.decode("0x" + servicesSettings.getProperty("OfflineTradeNameColor", "B0FFFF"));
		SERVICES_OFFLINE_TRADE_ABNORMAL_EFFECT = AbnormalEffect.valueOf(servicesSettings.getProperty("OfflineTradeAbnormalEffect", "NONE").toUpperCase());
		SERVICES_OFFLINE_TRADE_PRICE_ITEM = servicesSettings.getProperty("OfflineTradePriceItem", 0);
		SERVICES_OFFLINE_TRADE_PRICE = servicesSettings.getProperty("OfflineTradePrice", 0);
		SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK = servicesSettings.getProperty("OfflineTradeDaysToKick", 14) * 86400;
		SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART = servicesSettings.getProperty("OfflineRestoreAfterRestart", true);

		SERVICES_NO_TRADE_ONLY_OFFLINE = servicesSettings.getProperty("NoTradeOnlyOffline", false);
		SERVICES_TRADE_TAX = servicesSettings.getProperty("TradeTax", 0.0);
		SERVICES_OFFSHORE_TRADE_TAX = servicesSettings.getProperty("OffshoreTradeTax", 0.0);
		SERVICES_TRADE_TAX_ONLY_OFFLINE = servicesSettings.getProperty("TradeTaxOnlyOffline", false);
		SERVICES_OFFSHORE_NO_CASTLE_TAX = servicesSettings.getProperty("NoCastleTaxInOffshore", false);
		SERVICES_TRADE_ONLY_FAR = servicesSettings.getProperty("TradeOnlyFar", false);
		SERVICES_TRADE_MIN_LEVEL = servicesSettings.getProperty("MinLevelForTrade", 0);
		SERVICES_TRADE_RADIUS = servicesSettings.getProperty("TradeRadius", 30);

		SERVICES_GIRAN_HARBOR_ENABLED = servicesSettings.getProperty("GiranHarborZone", false);
		SERVICES_PARNASSUS_ENABLED = servicesSettings.getProperty("ParnassusZone", false);
		SERVICES_PARNASSUS_NOTAX = servicesSettings.getProperty("ParnassusNoTax", false);
		SERVICES_PARNASSUS_PRICE = servicesSettings.getProperty("ParnassusPrice", 500000);

		SERVICES_ALLOW_LOTTERY = servicesSettings.getProperty("AllowLottery", false);
		SERVICES_LOTTERY_PRIZE = servicesSettings.getProperty("LotteryPrize", 50000);
		SERVICES_ALT_LOTTERY_PRICE = servicesSettings.getProperty("AltLotteryPrice", 2000);
		SERVICES_LOTTERY_TICKET_PRICE = servicesSettings.getProperty("LotteryTicketPrice", 2000);
		SERVICES_LOTTERY_5_NUMBER_RATE = servicesSettings.getProperty("Lottery5NumberRate", 0.6);
		SERVICES_LOTTERY_4_NUMBER_RATE = servicesSettings.getProperty("Lottery4NumberRate", 0.4);
		SERVICES_LOTTERY_3_NUMBER_RATE = servicesSettings.getProperty("Lottery3NumberRate", 0.2);
		SERVICES_LOTTERY_2_AND_1_NUMBER_PRIZE = servicesSettings.getProperty("Lottery2and1NumberPrize", 200);

		SERVICES_ALLOW_ROULETTE = servicesSettings.getProperty("AllowRoulette", false);
		SERVICES_ROULETTE_MIN_BET = servicesSettings.getProperty("RouletteMinBet", 1L);
		SERVICES_ROULETTE_MAX_BET = servicesSettings.getProperty("RouletteMaxBet", Long.MAX_VALUE);

		SERVICES_ENABLE_NO_CARRIER = servicesSettings.getProperty("EnableNoCarrier", false);
		SERVICES_NO_CARRIER_MIN_TIME = servicesSettings.getProperty("NoCarrierMinTime", 0);
		SERVICES_NO_CARRIER_MAX_TIME = servicesSettings.getProperty("NoCarrierMaxTime", 90);
		SERVICES_NO_CARRIER_DEFAULT_TIME = servicesSettings.getProperty("NoCarrierDefaultTime", 60);

		ALLOW_EVENT_GATEKEEPER = servicesSettings.getProperty("AllowEventGatekeeper", false);

		SERVICES_ENCHANT_VALUE = servicesSettings.getProperty("EnchantValue", new int[] {
				0
		});
		SERVICES_ENCHANT_COAST = servicesSettings.getProperty("EnchantCoast", new int[] {
				0
		});
		SERVICES_ENCHANT_RAID_VALUE = servicesSettings.getProperty("EnchantRaidValue", new int[] {
				0
		});
		SERVICES_ENCHANT_RAID_COAST = servicesSettings.getProperty("EnchantRaidCoast", new int[] {
				0
		});

		ALLOW_IP_LOCK = servicesSettings.getProperty("AllowLockIP", false);
		AUTO_LOCK_IP_ON_LOGIN = servicesSettings.getProperty("AUTO_LOCK_IP_ON_LOGIN", false);
		ALLOW_HWID_LOCK = servicesSettings.getProperty("AllowLockHwid", false);
		AUTO_LOCK_HWID_ON_LOGIN = servicesSettings.getProperty("AUTO_LOCK_HWID_ON_LOGIN", false);
		HWID_LOCK_MASK = servicesSettings.getProperty("HwidLockMask", 10);

		SERVICES_RIDE_HIRE_ENABLED = servicesSettings.getProperty("SERVICES_RIDE_HIRE_ENABLED", false);

		/** Away System **/
		ALLOW_AWAY_STATUS = servicesSettings.getProperty("AllowAwayStatus", false); // FIXME: скорее всего не корректно
		AWAY_ONLY_FOR_PREMIUM = servicesSettings.getProperty("AwayOnlyForPremium", true);
		AWAY_PLAYER_TAKE_AGGRO = servicesSettings.getProperty("AwayPlayerTakeAggro", false);
		AWAY_TITLE_COLOR = Integer.decode("0x" + servicesSettings.getProperty("AwayTitleColor", "0000FF"));
		AWAY_TIMER = servicesSettings.getProperty("AwayTimer", 30);
		BACK_TIMER = servicesSettings.getProperty("BackTimer", 30);
		AWAY_PEACE_ZONE = servicesSettings.getProperty("AwayOnlyInPeaceZone", false);
	}

	public static void loadPvPSettings()
	{
		ExProperties pvpSettings = load(PVP_CONFIG_FILE);

		/* KARMA SYSTEM */
		KARMA_MIN_KARMA = pvpSettings.getProperty("MinKarma", 720);
		KARMA_RATE_KARMA_LOST = pvpSettings.getProperty("RateKarmaLost", -1);
		KARMA_LOST_BASE = pvpSettings.getProperty("BaseKarmaLost", 1200);

		KARMA_DROP_GM = pvpSettings.getProperty("CanGMDropEquipment", false);
		KARMA_NEEDED_TO_DROP = pvpSettings.getProperty("KarmaNeededToDrop", true);
		DROP_ITEMS_ON_DIE = pvpSettings.getProperty("DropOnDie", true);
		DROP_ITEMS_AUGMENTED = pvpSettings.getProperty("DropAugmented", false);

		MIN_PK_TO_ITEMS_DROP = pvpSettings.getProperty("MinPKToDropItems", 0);

		KARMA_RANDOM_DROP_LOCATION_LIMIT = pvpSettings.getProperty("MaxDropThrowDistance", 70);

		KARMA_DROPCHANCE_1 = pvpSettings.getProperty("ChanceOfDrop1", 1.);
		KARMA_DROPCHANCE_2 = pvpSettings.getProperty("ChanceOfDrop2", 20.);
		KARMA_DROPCHANCE_3 = pvpSettings.getProperty("ChanceOfDrop3", 50.);
		KARMA_ITEMSDROP_1 = pvpSettings.getProperty("ItemsDropMax1", 1);
		KARMA_ITEMSDROP_2 = pvpSettings.getProperty("ItemsDropMax2", 2);
		KARMA_ITEMSDROP_3 = pvpSettings.getProperty("ItemsDropMax3", 3);
		KARMA_DROPCHANCE_MOD = pvpSettings.getProperty("ChanceOfPKsDropMod", 1.);

		KARMA_LIST_NONDROPPABLE_ITEMS = new ArrayList<Integer>();
		for(int id : pvpSettings.getProperty("ListOfNonDroppableItems", new int[] {
				57,
				1147,
				425,
				1146,
				461,
				10,
				2368,
				7,
				6,
				2370,
				2369,
				3500,
				3501,
				3502,
				4422,
				4423,
				4424,
				2375,
				6648,
				6649,
				6650,
				6842,
				6834,
				6835,
				6836,
				6837,
				6838,
				6839,
				6840,
				5575,
				7694,
				6841,
				8181
		}))
			KARMA_LIST_NONDROPPABLE_ITEMS.add(id);

		PVP_TIME = pvpSettings.getProperty("PvPTime", 40000);
		RATE_KARMA_LOST_STATIC = pvpSettings.getProperty("KarmaLostStaticValue", -1);
	}

	public static void loadAISettings()
	{
		ExProperties aiSettings = load(AI_CONFIG_FILE);

		AI_TASK_MANAGER_COUNT = aiSettings.getProperty("AiTaskManagers", 1);
		AI_TASK_ATTACK_DELAY = aiSettings.getProperty("AiTaskDelay", 1000);
		AI_TASK_ACTIVE_DELAY = aiSettings.getProperty("AiTaskActiveDelay", 1000);
		BLOCK_ACTIVE_TASKS = aiSettings.getProperty("BlockActiveTasks", false);
		ALWAYS_TELEPORT_HOME = aiSettings.getProperty("AlwaysTeleportHome", false);
		ALWAYS_TELEPORT_HOME_RB = aiSettings.getProperty("ALWAYS_TELEPORT_HOME_RB", true);

		RND_WALK = aiSettings.getProperty("RndWalk", true);
		RND_WALK_RATE = aiSettings.getProperty("RndWalkRate", 1);
		RND_ANIMATION_RATE = aiSettings.getProperty("RndAnimationRate", 2);

		AGGRO_CHECK_INTERVAL = aiSettings.getProperty("AggroCheckInterval", 250);
		NONAGGRO_TIME_ONTELEPORT = aiSettings.getProperty("NonAggroTimeOnTeleport", 15000);
		NONPVP_TIME_ONTELEPORT = aiSettings.getProperty("NonPvPTimeOnTeleport", 0);
		MAX_DRIFT_RANGE = aiSettings.getProperty("MaxDriftRange", 100);
		MAX_PURSUE_RANGE = aiSettings.getProperty("MaxPursueRange", 10000);
		MAX_PURSUE_UNDERGROUND_RANGE = aiSettings.getProperty("MaxPursueUndergoundRange", 5000);
		MAX_PURSUE_RANGE_RAID = aiSettings.getProperty("MaxPursueRangeRaid", 5000);
	}

	public static void loadGeodataSettings()
	{
		ExProperties geodataSettings = load(GEODATA_CONFIG_FILE);

		GEO_X_FIRST = geodataSettings.getProperty("GeoFirstX", 11);
		GEO_Y_FIRST = geodataSettings.getProperty("GeoFirstY", 10);
		GEO_X_LAST = geodataSettings.getProperty("GeoLastX", 26);
		GEO_Y_LAST = geodataSettings.getProperty("GeoLastY", 26);

		ALLOW_GEODATA = geodataSettings.getProperty("AllowGeodata", true);

		try
		{
			GEODATA_ROOT = new File(geodataSettings.getProperty("GeodataRoot", "./geodata/")).getCanonicalFile();
		}
		catch(IOException e)
		{
			_log.error("", e);
		}

		ALLOW_FALL_FROM_WALLS = geodataSettings.getProperty("AllowFallFromWalls", false);
		ALLOW_KEYBOARD_MOVE = geodataSettings.getProperty("AllowMoveWithKeyboard", true);
		COMPACT_GEO = geodataSettings.getProperty("CompactGeoData", false);
		PATHFIND_BOOST = geodataSettings.getProperty("PathFindBoost", 2);
		PATHFIND_DIAGONAL = geodataSettings.getProperty("PathFindDiagonal", true);
		PATHFIND_MAP_MUL = geodataSettings.getProperty("PathFindMapMul", 2);
		PATH_CLEAN = geodataSettings.getProperty("PathClean", true);
		PATHFIND_MAX_Z_DIFF = geodataSettings.getProperty("PathFindMaxZDiff", 32);
		MAX_Z_DIFF = geodataSettings.getProperty("MaxZDiff", 64);
		MIN_LAYER_HEIGHT = geodataSettings.getProperty("MinLayerHeight", 64);
		REGION_EDGE_MAX_Z_DIFF = geodataSettings.getProperty("RegionEdgeMaxZDiff", 128);
		PATHFIND_MAX_TIME = geodataSettings.getProperty("PathFindMaxTime", 10000000);
		PATHFIND_BUFFERS = geodataSettings.getProperty("PathFindBuffers", "8x96;8x128;8x160;8x192;4x224;4x256;4x288;2x320;2x384;2x352;1x512");
		NPC_PATH_FIND_MAX_HEIGHT = geodataSettings.getProperty("NPC_PATH_FIND_MAX_HEIGHT", 1024);
		PLAYABLE_PATH_FIND_MAX_HEIGHT = geodataSettings.getProperty("PLAYABLE_PATH_FIND_MAX_HEIGHT", 256);
		DAMAGE_FROM_FALLING = geodataSettings.getProperty("DamageFromFalling", true);
	}

	public static void pvpManagerSettings()
	{
		ExProperties pvp_manager = load(PVP_MANAGER_FILE);

		ALLOW_PVP_REWARD = pvp_manager.getProperty("AllowPvPManager", true);
		PVP_REWARD_SEND_SUCC_NOTIF = pvp_manager.getProperty("SendNotification", true);

		PVP_REWARD_REWARD_IDS = pvp_manager.getProperty("PvPRewardsIDs", new int[] {
				57,
				6673
		});
		PVP_REWARD_COUNTS = pvp_manager.getProperty("PvPRewardsCounts", new long[] {
				1,
				2
		});
		if(PVP_REWARD_REWARD_IDS.length != PVP_REWARD_COUNTS.length)
			_log.warn("pvp_manager.properties: PvPRewardsIDs array length != PvPRewardsCounts array length");

		PVP_REWARD_RANDOM_ONE = pvp_manager.getProperty("GiveJustOneRandom", true);
		PVP_REWARD_DELAY_ONE_KILL = pvp_manager.getProperty("DelayBetweenKillsOneCharSec", 60);
		PVP_REWARD_MIN_PL_PROFF = pvp_manager.getProperty("ToRewardMinProff", 0);
		PVP_REWARD_MIN_PL_UPTIME_MINUTE = pvp_manager.getProperty("ToRewardMinPlayerUptimeMinutes", 60);
		PVP_REWARD_MIN_PL_LEVEL = pvp_manager.getProperty("ToRewardMinPlayerLevel", 75);
		PVP_REWARD_PK_GIVE = pvp_manager.getProperty("RewardPK", false);
		PVP_REWARD_ON_EVENT_GIVE = pvp_manager.getProperty("ToRewardIfInEvent", false);
		PVP_REWARD_ONLY_BATTLE_ZONE = pvp_manager.getProperty("ToRewardOnlyIfInBattleZone", false);
		PVP_REWARD_ONLY_NOBLE_GIVE = pvp_manager.getProperty("ToRewardOnlyIfNoble", false);
		PVP_REWARD_SAME_PARTY_GIVE = pvp_manager.getProperty("ToRewardIfInSameParty", false);
		PVP_REWARD_SAME_CLAN_GIVE = pvp_manager.getProperty("ToRewardIfInSameClan", false);
		PVP_REWARD_SAME_ALLY_GIVE = pvp_manager.getProperty("ToRewardIfInSameAlly", false);
		PVP_REWARD_SAME_HWID_GIVE = pvp_manager.getProperty("ToRewardIfInSameHWID", false);
		PVP_REWARD_SAME_IP_GIVE = pvp_manager.getProperty("ToRewardIfInSameIP", false);
		PVP_REWARD_SPECIAL_ANTI_TWINK_TIMER = pvp_manager.getProperty("SpecialAntiTwinkCharCreateDelay", false);
		PVP_REWARD_HR_NEW_CHAR_BEFORE_GET_ITEM = pvp_manager.getProperty("SpecialAntiTwinkDelayInHours", 24);
		PVP_REWARD_CHECK_EQUIP = pvp_manager.getProperty("EquipCheck", false);
		PVP_REWARD_WEAPON_GRADE_TO_CHECK = pvp_manager.getProperty("MinimumGradeToCheck", 0);
		PVP_REWARD_LOG_KILLS = pvp_manager.getProperty("LogKillsToDB", false);
		DISALLOW_MSG_TO_PL = pvp_manager.getProperty("DoNotShowMessagesToPlayers", false);
	}

	public static void loadEventsSettings()
	{
		ExProperties eventSettings = load(EVENTS_CONFIG_FILE);

		BALTHUS_EVENT_ENABLE = eventSettings.getProperty("BalthusEventEnabled", false);
		BALTHUS_EVENT_TIME_START = eventSettings.getProperty("BalthusEventTimeStart", "2014/10/29 18:00:00");
		BALTHUS_EVENT_TIME_END = eventSettings.getProperty("BalthusEventTimeEnd", "2014/11/25 18:00:00");
		BALTHUS_EVENT_PARTICIPATE_BUFF_ID = eventSettings.getProperty("BalthusEventParticipateBuffId", 39171);
		BALTHUS_EVENT_BASIC_REWARD_ID = eventSettings.getProperty("BalthusEventBasicRewardId", 94783);
		BALTHUS_EVENT_BASIC_REWARD_COUNT = eventSettings.getProperty("BalthusEventBasicRewardCount", 10);
		BALTHUS_EVENT_JACKPOT_CHANCE = eventSettings.getProperty("BalthusEventJackpotChance", 15);

		BM_FESTIVAL_ENABLE = eventSettings.getProperty("BMFestivalEventEnabled", false);
		BM_FESTIVAL_TIME_START = eventSettings.getProperty("BMFestivalTimeStart", "2014/10/29 18:00:00");
		BM_FESTIVAL_TIME_END = eventSettings.getProperty("BMFestivalTimeEnd", "2024/11/25 18:00:00");
		BM_FESTIVAL_ITEM_TO_PLAY = eventSettings.getProperty("BMFestivalItemToPlay", 94441);
		BM_FESTIVAL_PLAY_LIMIT = eventSettings.getProperty("BMFestivalPlayLimit", -1);
		BM_FESTIVAL_ITEM_TO_PLAY_COUNT = eventSettings.getProperty("BMFestivalItemToPlayCount", 1);
	}

	public static void loadOlympiadSettings()
	{
		ExProperties olympSettings = load(OLYMPIAD);

		ENABLE_OLYMPIAD = olympSettings.getProperty("EnableOlympiad", true);
		ENABLE_OLYMPIAD_SPECTATING = olympSettings.getProperty("EnableOlympiadSpectating", true);
		OLYMIAD_END_PERIOD_TIME = new SchedulingPattern(olympSettings.getProperty("OLYMIAD_END_PERIOD_TIME", "00 00 01 * *"));
		OLYMPIAD_START_TIME_1x1 = new SchedulingPattern(olympSettings.getProperty("OLYMPIAD_START_TIME_1x1", "00 20 * * 6"));
		OLYMPIAD_START_TIME_3x3 = new SchedulingPattern(olympSettings.getProperty("OLYMPIAD_START_TIME_3x3", "00 20 * * 5,7"));
		ALT_OLY_CPERIOD = olympSettings.getProperty("AltOlyCPeriod", 14400000);
		ALT_OLY_WPERIOD = olympSettings.getProperty("AltOlyWPeriod", 604800000);
		ALT_OLY_VPERIOD = olympSettings.getProperty("AltOlyVPeriod", 43200000);
		CLASSED_GAMES_ENABLED = olympSettings.getProperty("CLASSED_GAMES_ENABLED", false);
		OLYMPIAD_REGISTRATION_DELAY = olympSettings.getProperty("OLYMPIAD_REGISTRATION_DELAY", 1200000);
		OLYMPIAD_MIN_LEVEL = Math.max(40, olympSettings.getProperty("OLYMPIAD_MIN_LEVEL", 55));
		CLASS_GAME_MIN = olympSettings.getProperty("ClassGameMin", 10);
		NONCLASS_GAME_MIN = olympSettings.getProperty("NonClassGameMin", 20);

		OLYMPIAD_GAME_MAX_LIMIT = olympSettings.getProperty("GameMaxLimit", 5);

		ALT_OLY_REG_DISPLAY = olympSettings.getProperty("AltOlyRegistrationDisplayNumber", 100);
		ALT_OLY_BATTLE_REWARD_ITEM = olympSettings.getProperty("AltOlyBattleRewItem", 45584);
		ALT_OLY_COMP_RITEM = olympSettings.getProperty("AltOlyCompRewItem", 45584);
		ALT_OLY_GP_PER_POINT = olympSettings.getProperty("AltOlyGPPerPoint", 20);
		ALT_OLY_HERO_POINTS = olympSettings.getProperty("AltOlyHeroPoints", 100);
		ALT_OLY_RANK1_POINTS = olympSettings.getProperty("AltOlyRank1Points", 200);
		ALT_OLY_RANK2_POINTS = olympSettings.getProperty("AltOlyRank2Points", 80);
		ALT_OLY_RANK3_POINTS = olympSettings.getProperty("AltOlyRank3Points", 50);
		ALT_OLY_RANK4_POINTS = olympSettings.getProperty("AltOlyRank4Points", 30);
		ALT_OLY_RANK5_POINTS = olympSettings.getProperty("AltOlyRank5Points", 15);
		OLYMPIAD_ALL_LOOSE_POINTS_BONUS = olympSettings.getProperty("OLYMPIAD_ALL_LOOSE_POINTS_BONUS", 0);
		OLYMPIAD_1_OR_MORE_WIN_POINTS_BONUS = olympSettings.getProperty("OLYMPIAD_1_OR_MORE_WIN_POINTS_BONUS", 10);
		OLYMPIAD_STADIAS_COUNT = olympSettings.getProperty("OlympiadStadiasCount", 160);
		OLYMPIAD_BATTLES_FOR_REWARD = olympSettings.getProperty("OlympiadBattlesForReward", 10);
		OLYMPIAD_POINTS_DEFAULT = olympSettings.getProperty("OlympiadPointsDefault", 10);
		OLYMPIAD_POINTS_WEEKLY = olympSettings.getProperty("OlympiadPointsWeekly", 10);
		OLYMPIAD_OLDSTYLE_STAT = olympSettings.getProperty("OlympiadOldStyleStat", false);
		OLYMPIAD_CANATTACK_BUFFER = olympSettings.getProperty("OlympiadCanAttackBuffer", true);

		OLYMPIAD_BEGINIG_DELAY = olympSettings.getProperty("OlympiadBeginingDelay", 120);

		ALT_OLY_BY_SAME_BOX_NUMBER = olympSettings.getProperty("OlympiadSameBoxesNumberLimitation", 0);

		OLYMPIAD_ENABLE_ENCHANT_LIMIT = olympSettings.getProperty("ENABLE_ENCHANT_LIMIT", false);
		OLYMPIAD_WEAPON_ENCHANT_LIMIT = olympSettings.getProperty("WEAPON_ENCHANT_LIMIT", 0);
		OLYMPIAD_ARMOR_ENCHANT_LIMIT = olympSettings.getProperty("ARMOR_ENCHANT_LIMIT", 0);
		OLYMPIAD_JEWEL_ENCHANT_LIMIT = olympSettings.getProperty("JEWEL_ENCHANT_LIMIT", 0);
	}

	public static void loadAntiBotSettings()
	{
		ExProperties botSettings = load(BOT_FILE);

		ENABLE_ANTI_BOT_SYSTEM = botSettings.getProperty("EnableAntiBotSystem", false);
		MINIMUM_TIME_QUESTION_ASK = botSettings.getProperty("MinimumTimeQuestionAsk", 60);
		MAXIMUM_TIME_QUESTION_ASK = botSettings.getProperty("MaximumTimeQuestionAsk", 120);
		MINIMUM_BOT_POINTS_TO_STOP_ASKING = botSettings.getProperty("MinimumBotPointsToStopAsking", 10);
		MAXIMUM_BOT_POINTS_TO_STOP_ASKING = botSettings.getProperty("MaximumBotPointsToStopAsking", 15);
		MAX_BOT_POINTS = botSettings.getProperty("MaxBotPoints", 15);
		MINIMAL_BOT_RATING_TO_BAN = botSettings.getProperty("MinimalBotPointsToBan", -5);
		AUTO_BOT_BAN_JAIL_TIME = botSettings.getProperty("AutoBanJailTime", 24);
		ANNOUNCE_AUTO_BOT_BAN = botSettings.getProperty("AnounceAutoBan", true);
		ON_WRONG_QUESTION_KICK = botSettings.getProperty("IfWrongKick", true);
	}

	public static void loadSchemeBuffer()
	{
		ExProperties npcbuffer = load(SCHEME_BUFFER_FILE);

		NpcBuffer_VIP = npcbuffer.getProperty("EnableVIP", false);
		NpcBuffer_VIP_ALV = npcbuffer.getProperty("VipAccesLevel", 1);
		NpcBuffer_EnableBuff = npcbuffer.getProperty("EnableBuffSection", true);
		NpcBuffer_EnableScheme = npcbuffer.getProperty("EnableScheme", true);
		NpcBuffer_EnableHeal = npcbuffer.getProperty("EnableHeal", true);
		NpcBuffer_EnableBuffs = npcbuffer.getProperty("EnableBuffs", true);
		NpcBuffer_EnableResist = npcbuffer.getProperty("EnableResist", true);
		NpcBuffer_EnableSong = npcbuffer.getProperty("EnableSongs", true);
		NpcBuffer_EnableDance = npcbuffer.getProperty("EnableDances", true);
		NpcBuffer_EnableChant = npcbuffer.getProperty("EnableChants", true);
		NpcBuffer_EnableOther = npcbuffer.getProperty("EnableOther", true);
		NpcBuffer_EnableSpecial = npcbuffer.getProperty("EnableSpecial", true);
		NpcBuffer_EnableCubic = npcbuffer.getProperty("EnableCubic", true);
		NpcBuffer_EnableCancel = npcbuffer.getProperty("EnableRemoveBuffs", true);
		NpcBuffer_EnableBuffSet = npcbuffer.getProperty("EnableBuffSet", true);
		NpcBuffer_EnableBuffPK = npcbuffer.getProperty("EnableBuffForPK", false);
		NpcBuffer_EnableFreeBuffs = npcbuffer.getProperty("EnableFreeBuffs", true);
		NpcBuffer_EnableTimeOut = npcbuffer.getProperty("EnableTimeOut", true);
		SCHEME_ALLOW_FLAG = npcbuffer.getProperty("EnableBuffforFlag", false);
		NpcBuffer_TimeOutTime = npcbuffer.getProperty("TimeoutTime", 10);
		NpcBuffer_MinLevel = npcbuffer.getProperty("MinimumLevel", 20);
		NpcBuffer_PriceCancel = npcbuffer.getProperty("RemoveBuffsPrice", 100000);
		NpcBuffer_PriceHeal = npcbuffer.getProperty("HealPrice", 100000);
		NpcBuffer_PriceBuffs = npcbuffer.getProperty("BuffsPrice", 100000);
		NpcBuffer_PriceResist = npcbuffer.getProperty("ResistPrice", 100000);
		NpcBuffer_PriceSong = npcbuffer.getProperty("SongPrice", 100000);
		NpcBuffer_PriceDance = npcbuffer.getProperty("DancePrice", 100000);
		NpcBuffer_PriceChant = npcbuffer.getProperty("ChantsPrice", 100000);
		NpcBuffer_PriceOther = npcbuffer.getProperty("OtherPrice", 100000);
		NpcBuffer_PriceSpecial = npcbuffer.getProperty("SpecialPrice", 100000);
		NpcBuffer_PriceCubic = npcbuffer.getProperty("CubicPrice", 100000);
		NpcBuffer_PriceSet = npcbuffer.getProperty("SetPrice", 100000);
		NpcBuffer_PriceScheme = npcbuffer.getProperty("SchemePrice", 100000);
		NpcBuffer_MaxScheme = npcbuffer.getProperty("MaxScheme", 4);
		IS_DISABLED_IN_REFLECTION = npcbuffer.getProperty("DisableBufferInReflection", true);
	}

	public static void loadBuffStoreConfig()
	{
		ExProperties buffStoreConfig = load(BUFF_STORE_CONFIG_FILE);

		// Buff Store
		BUFF_STORE_ENABLED = buffStoreConfig.getProperty("BuffStoreEnabled", false);
		BUFF_STORE_MP_ENABLED = buffStoreConfig.getProperty("BuffStoreMpEnabled", true);
		BUFF_STORE_MP_CONSUME_MULTIPLIER = buffStoreConfig.getProperty("BuffStoreMpConsumeMultiplier", 1.0f);
		BUFF_STORE_ITEM_CONSUME_ENABLED = buffStoreConfig.getProperty("BuffStoreItemConsumeEnabled", true);

		BUFF_STORE_NAME_COLOR = Integer.decode("0x" + buffStoreConfig.getProperty("BuffStoreNameColor", "808080"));
		BUFF_STORE_TITLE_COLOR = Integer.decode("0x" + buffStoreConfig.getProperty("BuffStoreTitleColor", "808080"));
		BUFF_STORE_OFFLINE_NAME_COLOR = Integer.decode("0x" + buffStoreConfig.getProperty("BuffStoreOfflineNameColor", "808080"));

		String[] classes = buffStoreConfig.getProperty("BuffStoreAllowedClassList", "").split(",");
		BUFF_STORE_ALLOWED_CLASS_LIST = new ArrayList<Integer>();
		if(classes.length > 0)
		{
			for(String classId : classes)
			{
				BUFF_STORE_ALLOWED_CLASS_LIST.add(Integer.valueOf(Integer.parseInt(classId)));
			}
		}
		BUFF_STORE_ALLOWED_SKILL_LIST.addAll(StringArrayUtils.stringToIntArray(buffStoreConfig.getProperty("BUFF_STORE_ALLOWED_SKILL_LIST", ""), ","));

		BUFF_STORE_MIN_PRICE = buffStoreConfig.getProperty("BuffStoreMinPrice", 1);
		BUFF_STORE_MAX_PRICE = buffStoreConfig.getProperty("BuffStoreMaxPrice", Long.MAX_VALUE);
	}

	public static int PVPBOOK_EXPIRATION_DELAY;
	public static int PVPBOOK_TELEPORT_HELP_PRICE;
	public static Map<Integer, Integer> PVPBOOK_ADENA_LOCATION_SHOW = new HashMap<>();
	public static Map<Integer, Integer> PVPBOOK_LCOIN_TELEPORT_COUNT = new HashMap<>();

	public static final String PVPBOOK_CONFIG_FILE = "config/Pvpbook.properties";

	public static void loadPvpbookSettings()
	{
		ExProperties Pvpbookconfig = load(PVPBOOK_CONFIG_FILE);
		PVPBOOK_EXPIRATION_DELAY = Pvpbookconfig.getProperty("PvpbookExpirationDelay", 24);
		PVPBOOK_TELEPORT_HELP_PRICE = Pvpbookconfig.getProperty("TeleportHelpPrice", 100);

		Pattern pattern = Pattern.compile("\\{(\\d+);(\\d+)\\}");
		Matcher matcher = pattern.matcher(Pvpbookconfig.getProperty("AdenaLocationShow", "{1;50000};{2;100000};{3;200000}"));

		while(matcher.find())
		{
			int views = Integer.parseInt(matcher.group(1));
			int price = Integer.parseInt(matcher.group(2));
			PVPBOOK_ADENA_LOCATION_SHOW.put(views, price);
		}

		matcher = pattern.matcher(Pvpbookconfig.getProperty("LcoinTeleportCount", "{0;1};{2;50};{2;100};{3;100};{4;200}"));

		while(matcher.find())
		{
			int views = Integer.parseInt(matcher.group(1));
			int price = Integer.parseInt(matcher.group(2));
			PVPBOOK_LCOIN_TELEPORT_COUNT.put(views, price);
		}

	}

	public static int[] ADENLAB_RESEARCH_DIARY;
	public static long ADENLAB_ADENA_PLAY;
	public static long ADENLAB_ADENA_FIX;

	public static final String HUNT_PASS_CONFIG_FILE = "config/HuntPass.properties";

	// HuntPass
	public static boolean ENABLE_HUNT_PASS;
	public static int HUNT_PASS_PERIOD;

	public static int HUNT_PASS_PREMIUM_ITEM_ID;
	public static int HUNT_PASS_PREMIUM_COST;
	public static int HUNT_PASS_POINTS_FOR_STEP;
	public static int HUNT_PASS_POINTS_FOR_MOB;
	public static int HUNT_PASS_POINTS_FOR_MOB_INSTANCE;

	public static void loadHuntPass()
	{
		ExProperties huntPassConfig = load(HUNT_PASS_CONFIG_FILE);

		ENABLE_HUNT_PASS = huntPassConfig.getProperty("EnabledHuntPass", true);
		HUNT_PASS_PREMIUM_ITEM_ID = huntPassConfig.getProperty("PremiumItemId", 91663);
		HUNT_PASS_PREMIUM_COST = huntPassConfig.getProperty("PremiumCost", 3600);
		HUNT_PASS_POINTS_FOR_STEP = huntPassConfig.getProperty("PointsForstep", 2400);
		HUNT_PASS_PERIOD = huntPassConfig.getProperty("DayOfMonth", 1);

		HUNT_PASS_POINTS_FOR_MOB = huntPassConfig.getProperty("PointsForMob", 1);
		HUNT_PASS_POINTS_FOR_MOB_INSTANCE = huntPassConfig.getProperty("PointsForMobInstance", 2);
	}

	// World Exchange
	public static boolean ENABLE_WORLD_EXCHANGE;
	public static Language WORLD_EXCHANGE_DEFAULT_LANG;
	public static long WORLD_EXCHANGE_SAVE_INTERVAL;
	public static int WORLD_EXCHANGE_TAX;
	public static int WORLD_EXCHANGE_MIN_ADENA_TAX;
	public static int WORLD_EXCHANGE_MIN_BALANS_TAX;

	public static int WORLD_EXCHANGE_ITEM_SELL_PERIOD;
	public static int WORLD_EXCHANGE_ITEM_BACK_PERIOD;
	public static int WORLD_EXCHANGE_PAYMENT_TAKE_PERIOD;
	public static boolean VIP_ATTENDANCE_REWARDS_REWARD_ONLY_PREMIUM = false;

	public static final String WORLD_EXCHANGE_CONFIG_FILE = "config/WorldExchange.properties";

	public static final boolean DISABLE_SHOPPING_IN_THE_STORE = false;

	public static final int CLAN_EXP_RATE = 1;

	public static boolean APPEARANCE_STONE_RETURT_STONE = false;

	public static void loadWorldExchangeSettings()
	{
		ExProperties worldExchangeconfig = load(WORLD_EXCHANGE_CONFIG_FILE);

		ENABLE_WORLD_EXCHANGE = worldExchangeconfig.getProperty("EnableWorldExchange", true);
		WORLD_EXCHANGE_DEFAULT_LANG = Language.valueOf(worldExchangeconfig.getProperty("WorldExchangeDefaultLanguage", "ENGLISH"));
		WORLD_EXCHANGE_SAVE_INTERVAL = worldExchangeconfig.getProperty("BidItemsIntervalStatusCheck", 30000);
		WORLD_EXCHANGE_TAX = worldExchangeconfig.getProperty("Tax", 5);
		WORLD_EXCHANGE_MIN_ADENA_TAX = worldExchangeconfig.getProperty("MinAdenaTax", 100);
		WORLD_EXCHANGE_MIN_BALANS_TAX = worldExchangeconfig.getProperty("MinBalansTax", 1);

		WORLD_EXCHANGE_ITEM_SELL_PERIOD = worldExchangeconfig.getProperty("ItemSellPeriod", 14);
		WORLD_EXCHANGE_ITEM_BACK_PERIOD = worldExchangeconfig.getProperty("ItemBackPeriod", 120);
		WORLD_EXCHANGE_PAYMENT_TAKE_PERIOD = worldExchangeconfig.getProperty("PaymentTakePeriod", 120);
	}

	public static void loadGMAccess()
	{
		gmlist.clear();
		loadGMAccess(new File(GM_PERSONAL_ACCESS_FILE));
		File dir = new File(GM_ACCESS_FILES_DIR);
		if(!dir.exists() || !dir.isDirectory())
		{
			_log.info("Dir " + dir.getAbsolutePath() + " not exists.");
			return;
		}
		for(File f : dir.listFiles())
		{
			if(!f.isDirectory() && f.getName().endsWith(".xml"))
			{
				loadGMAccess(f);
			}
		}
	}

	public static void loadGMAccess(File file)
	{
		try
		{
			Field fld;
			// File file = new File(filename);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document doc = factory.newDocumentBuilder().parse(file);

			for(Node z = doc.getFirstChild(); z != null; z = z.getNextSibling())
			{
				for(Node n = z.getFirstChild(); n != null; n = n.getNextSibling())
				{
					if(!n.getNodeName().equalsIgnoreCase("char"))
					{
						continue;
					}
					PlayerAccess pa = new PlayerAccess();
					for(Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						Class<?> cls = pa.getClass();
						String node = d.getNodeName();

						if(node.equalsIgnoreCase("#text"))
						{
							continue;
						}
						try
						{
							fld = cls.getField(node);
						}
						catch(NoSuchFieldException e)
						{
							_log.info("Not found desclarate ACCESS name: " + node + " in XML Player access Object");
							continue;
						}

						if(fld.getType().getName().equalsIgnoreCase("boolean"))
						{
							fld.setBoolean(pa, Boolean.parseBoolean(d.getAttributes().getNamedItem("set").getNodeValue()));
						}
						else if(fld.getType().getName().equalsIgnoreCase("int"))
						{
							fld.setInt(pa, Integer.valueOf(d.getAttributes().getNamedItem("set").getNodeValue()));
						}
					}
					gmlist.put(pa.PlayerID, pa);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static ExProperties load(String filename)
	{
		return load(new File(filename));
	}

	public static ExProperties load(File file)
	{
		ExProperties result = new ExProperties();
		try
		{
			result.load(file);
		}
		catch(IOException e)
		{
			_log.error("Error loading config : " + file.getName() + "!");
		}

		return result;
	}

	public static boolean containsAbuseWord(String s)
	{
		if(ABUSEWORD_PATTERN == null)
		{ return false; }

		return ABUSEWORD_PATTERN.matcher(s).matches();
	}

	public static String replaceAbuseWords(String text, String censore)
	{
		if(ABUSEWORD_PATTERN == null)
		{ return text; }

		Matcher m = ABUSEWORD_PATTERN.matcher(text);
		while(m.find())
		{
			text = text.replace(m.group(1), censore);
		}
		return text;
	}

	public static class RaidGlobalDrop
	{
		int _id;
		long _count;
		double _chance;

		public RaidGlobalDrop(int id, long count, double chance)
		{
			_id = id;
			_count = count;
			_chance = chance;
		}

		public int getId()
		{
			return _id;
		}

		public long getCount()
		{
			return _count;
		}

		public double getChance()
		{
			return _chance;
		}
	}

	private Config()
	{}

	public static void load()
	{
		loadServerConfig();
		loadRateServerConfig();
		loadResidenceConfig();
		loadChatAntiFloodConfig();
		loadOtherConfig();
		loadSpoilConfig();
		loadFormulasConfig();
		loadAltSettings();
		loadServicesSettings();
		loadPvPSettings();
		loadAISettings();
		loadGeodataSettings();
		loadOlympiadSettings();
		loadDevelopSettings();
		loadExtSettings();
		loadEventsSettings();
		loadBBSSettings();
		loadSchemeBuffer();
		loadBuffStoreConfig();
		loadPvpbookSettings();
		loadHuntPass();
		loadWorldExchangeSettings();

		abuseLoad();
		loadGMAccess();
		pvpManagerSettings();
		loadAntiBotSettings();
		loadEssenceConfig();
		loadWeddingConfig();
		loadFightClubSettings();
	}

	public static void abuseLoad()
	{
		try (LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(ANUSEWORDS_CONFIG_FILE), "UTF-8")))
		{
			StringBuilder abuses = new StringBuilder();
			String line;
			int count = 0;

			while((line = lnr.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line, "\n\r");
				if(st.hasMoreTokens())
				{
					abuses.append(st.nextToken());
					abuses.append("|");
					count++;
				}
			}

			if(count > 0)
			{
				String abusesGroup = abuses.toString();
				abusesGroup = abusesGroup.substring(0, abusesGroup.length() - 1);
				ABUSEWORD_PATTERN = Pattern.compile(".*(" + abusesGroup + ").*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			}
			_log.info("Abuse: Loaded " + count + " abuse words.");
		}
		catch(IOException e1)
		{
			_log.warn("Error reading abuse: " + e1);
		}
	}
}