package l2s.gameserver.model.entity.events.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.data.xml.holder.InstantZoneHolder;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.listener.actor.player.OnPlayerExitListener;
import l2s.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.SimpleSpawner;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.base.RestartType;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.entity.events.Event;
import l2s.gameserver.model.entity.events.EventType;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubEventManager;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubGameRoom;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubLastStatsManager;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubLastStatsManager.FightClubStatType;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubMap;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubPlayer;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubTeam;
import l2s.gameserver.model.entity.events.fightclubmanager.enums.EventState;
import l2s.gameserver.model.entity.events.fightclubmanager.enums.MessageType;
import l2s.gameserver.model.entity.events.fightclubmanager.enums.PlayerClass;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.EarthQuakePacket;
import l2s.gameserver.network.l2.s2c.ExPVPMatchCCRecord;
import l2s.gameserver.network.l2.s2c.ExPVPMatchCCRetire;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.network.l2.s2c.RelationChangedPacket;
import l2s.gameserver.network.l2.s2c.RevivePacket;
import l2s.gameserver.network.l2.s2c.SayPacket2;
import l2s.gameserver.network.l2.s2c.ShowTutorialMarkPacket;
import l2s.gameserver.network.l2.s2c.SkillCoolTimePacket;
import l2s.gameserver.skills.enums.AbnormalEffect;
import l2s.gameserver.templates.DoorTemplate;
import l2s.gameserver.templates.InstantZone;
import l2s.gameserver.templates.ZoneTemplate;
import l2s.gameserver.utils.TimeUtils;
import l2s.gameserver.utils.Util;

public abstract class AbstractFightClub extends Event
{
	protected static final Logger _log = LoggerFactory.getLogger(AbstractFightClub.class);

	public static final String REGISTERED_PLAYERS = "registered_players";
	public static final String LOGGED_OFF_PLAYERS = "logged_off_players";
	public static final String FIGHTING_PLAYERS = "fighting_players";

	public static final int INSTANT_ZONE_ID = 400;
	private static int LAST_OBJECT_ID = 1;

	// TODO Make it configurable inside Config files.
	private static final int BADGES_FOR_MINUTE_OF_AFK = -1;
	private static final int CLOSE_LOCATIONS_VALUE = 80;

	private static final int TIME_FIRST_TELEPORT = 10;
	private static final int TIME_PLAYER_TELEPORTING = 5;
	private static final int TIME_PREPARATION_BEFORE_FIRST_ROUND = 30;
	private static final int TIME_PREPARATION_BETWEEN_NEXT_ROUNDS = 30;
	private static final int TIME_AFTER_ROUND_END_TO_RETURN_SPAWN = 15;
	private static final int TIME_TELEPORT_BACK_TOWN = 30;
	private static final int TIME_MAX_SECONDS_OUTSIDE_ZONE = 10;
	private static final int TIME_TO_BE_AFK = 30;

	private static final String[] ROUND_NUMBER_IN_STRING =
	{
		"",
		"1st",
		"2nd",
		"3rd",
		"4th",
		"5th",
		"6th",
		"7th",
		"8th",
		"9th",
		"10th"
	};

	private final int _objId;
	private final String _desc;
	private final String _icon;
	private final int _roundRunTime;
	private final boolean _isAutoTimed;
	private final int[][] _autoStartTimes;
	private final boolean _teamed;
	private final boolean _buffer;
	private final int[][] _fighterBuffs;
	private final int[][] _mageBuffs;
	private final boolean _rootBetweenRounds;
	private final PlayerClass[] _excludedClasses;
	private final int[] _excludedSkills;
	private final boolean _roundEvent;
	private final int _rounds;
	private final int _respawnTime;
	private final boolean _ressAllowed;
	private final boolean _instanced;
	private final boolean _showPersonality;
	private final double _badgesKillPlayer;
	private final int _badgesId;
	private final double _badgesKillPet;
	protected boolean _preparing = false;
	private final double _badgesDie;
	protected final double _badgeWin;
	private final int topKillerReward;
	private EventState _state = EventState.NOT_ACTIVE;
	private ExitListener _exitListener = new ExitListener();
	private ZoneListener _zoneListener = new ZoneListener();
	private FightClubMap _map;
	private Reflection _reflection;
	private List<FightClubTeam> _teams = new ArrayList<FightClubTeam>();
	private Map<FightClubPlayer, Zone> _leftZone = new ConcurrentHashMap<FightClubPlayer, Zone>();
	private int _currentRound = 0;
	private boolean _dontLetAnyoneIn = false;
	private FightClubGameRoom _room;
	private MultiValueSet<String> _set;
	private Map<String, Integer> _scores = new ConcurrentHashMap<String, Integer>();
	private Map<String, Integer> _bestScores = new ConcurrentHashMap<String, Integer>();
	private boolean _scoredUpdated = true;
	private ScheduledFuture<?> _timer;

	public AbstractFightClub(MultiValueSet<String> set)
	{
		super(set);
		_objId = (LAST_OBJECT_ID++);
		_desc = set.getString("desc");
		_icon = set.getString("icon");
		_roundRunTime = set.getInteger("roundRunTime", -1);
		_teamed = set.getBool("teamed");
		_buffer = set.getBool("buffer");
		_fighterBuffs = parseBuffs(set.getString("fighterBuffs", null));
		_mageBuffs = parseBuffs(set.getString("mageBuffs", null));
		_rootBetweenRounds = set.getBool("rootBetweenRounds");
		_excludedClasses = parseExcludedClasses(set.getString("excludedClasses", ""));
		_excludedSkills = parseExcludedSkills(set.getString("excludedSkills", null));
		_isAutoTimed = set.getBool("isAutoTimed", false);
		_autoStartTimes = parseAutoStartTimes(set.getString("autoTimes", ""));
		_roundEvent = set.getBool("roundEvent");
		_rounds = set.getInteger("rounds", -1);
		_respawnTime = set.getInteger("respawnTime");
		_ressAllowed = set.getBool("ressAllowed");
		_instanced = set.getBool("instanced", true);
		_showPersonality = set.getBool("showPersonality", true);

		_badgesKillPlayer = set.getDouble("badgesKillPlayer", 0.0D);
		_badgesId = set.getInteger("badgeID", 57);
		_badgesKillPet = set.getDouble("badgesKillPet", 0.0D);
		_badgesDie = set.getDouble("badgesDie", 0.0D);
		_badgeWin = set.getDouble("badgesWin", 0.0D);
		topKillerReward = set.getInteger("topKillerReward", 0);

		_set = set;
	}

	public void prepareEvent(FightClubGameRoom room)
	{
		_map = room.getMap();
		_room = room;

		for (Player player : room.getAllPlayers())
		{
			addObject(REGISTERED_PLAYERS, new FightClubPlayer(player));
			player.addEvent(this);
		}

		startTeleportTimer(room);
	}

	@Override
	public void startEvent()
	{
		super.startEvent();

		_state = EventState.PREPARATION;

		IntObjectMap<DoorTemplate> doors = new HashIntObjectMap<DoorTemplate>(0);
		Map<String, ZoneTemplate> zones = new HashMap<String, ZoneTemplate>();
		for (Entry<Integer, Map<String, ZoneTemplate>> entry : getMap().getTerritories().entrySet())
		{
			for (Entry<String, ZoneTemplate> team : entry.getValue().entrySet())
			{
				zones.put(team.getKey(), team.getValue());
			}
		}

		if (isInstanced())
		{
			createReflection(doors, zones);
		}

		final List<FightClubPlayer> playersToRemove = new ArrayList<FightClubPlayer>();
		for (final FightClubPlayer iFPlayer : getPlayers(REGISTERED_PLAYERS))
		{
			stopInvisibility(iFPlayer.getPlayer());
			if (!checkIfRegisteredPlayerMeetCriteria(iFPlayer))
			{
				playersToRemove.add(iFPlayer);
				continue;
			}

			if (isHidePersonality())
			{
				iFPlayer.getPlayer().setPolyId(FightClubGameRoom.getPlayerClassGroup(iFPlayer.getPlayer()).getTransformId());
			}
		}

		for (FightClubPlayer playerToRemove : playersToRemove)
		{
			unregister(playerToRemove.getPlayer());
		}

		if (isTeamed())
		{
			spreadIntoTeamsAndPartys();
		}

		teleportRegisteredPlayers();

		updateEveryScore();

		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS, REGISTERED_PLAYERS))
		{
			iFPlayer.getPlayer().isntAfk();
			iFPlayer.getPlayer().setFightClubGameRoom(null);
		}

		startNewTimer(true, TIME_PLAYER_TELEPORTING * 1000, "startRoundTimer", TIME_PREPARATION_BEFORE_FIRST_ROUND);

		ThreadPoolManager.getInstance().schedule(new LeftZoneThread(), 5000L);
	}

	public void startRound()
	{
		_state = EventState.STARTED;

		_currentRound++;

		if (isRoundEvent())
		{
			if (_currentRound == _rounds)
			{
				sendMessageToFighting(MessageType.SCREEN_BIG, "Last Round STARTED!", true); // TODO: Вынести в ДП.
			}
			else
			{
				sendMessageToFighting(MessageType.SCREEN_BIG, new StringBuilder().append("Round ").append(_currentRound).append(" STARTED!").toString(), true);
			}
		}
		else
		{
			sendMessageToFighting(MessageType.SCREEN_BIG, "Fight!", true); // TODO: Вынести в ДП.
		}

		unrootPlayers();

		if (getRoundRuntime() > 0)
		{
			startNewTimer(true, (int) ((double) getRoundRuntime() / 2 * 60000), "endRoundTimer", (int) ((double) getRoundRuntime() / 2 * 60));
		}

		if (_currentRound == 1)
		{
			ThreadPoolManager.getInstance().schedule(new TimeSpentOnEventThread(), 10000L);
			ThreadPoolManager.getInstance().schedule(new CheckAfkThread(), 1000L);
		}

		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			hideScores(iFPlayer.getPlayer());
			iFPlayer.getPlayer().broadcastUserInfo(true);
		}
	}

	public void endRound()
	{
		_state = EventState.OVER;

		if (!isLastRound())
		{
			sendMessageToFighting(MessageType.SCREEN_BIG, new StringBuilder().append("Round ").append(_currentRound).append(" is over!").toString(), false);
		}
		else
		{
			sendMessageToFighting(MessageType.SCREEN_BIG, "Event is now Over!", false); // TODO: Вынести в ДП.
		}

		ressAndHealPlayers();

		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			showScores(iFPlayer.getPlayer());
			handleAfk(iFPlayer, false);
		}

		if (!isLastRound())
		{
			if (isTeamed())
			{
				for (FightClubTeam team : getTeams())
				{
					team.setSpawnLoc(null);
				}
			}

			ThreadPoolManager.getInstance().schedule(() ->
			{
				for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
				{
					teleportSinglePlayer(iFPlayer, false, true);
				}

				startNewTimer(true, 0, "startRoundTimer", TIME_PREPARATION_BETWEEN_NEXT_ROUNDS);

			}, TIME_AFTER_ROUND_END_TO_RETURN_SPAWN * 1000);
		}
		else
		{
			ThreadPoolManager.getInstance().schedule(() -> stopEvent(false), 5 * 1000);

			if (isTeamed())
			{
				announceWinnerTeam(true, null);
			}
			else
			{
				announceWinnerPlayer(true, null);
			}
		}

		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			iFPlayer.getPlayer().broadcastUserInfo(true);
		}
	}

	private void announceTopKillers(FightClubPlayer[] topKillers)
	{
		if (topKillers == null)
		{
			return;
		}
		for (FightClubPlayer fPlayer : topKillers)
		{
			if (fPlayer != null)
			{
				String message = fPlayer.getPlayer().getName() + " had most kills" + " on " + getName() + " Event!";
				FightClubEventManager.getInstance().sendToAllMsg(this, message);
			}
		}
	}

	@Override
	public void stopEvent(boolean force)
	{
		_state = EventState.NOT_ACTIVE;
		super.stopEvent(force);
		reCalcNextTime(false);
		_room = null;

		showLastAFkMessage();
		FightClubPlayer[] topKillers = getTopKillers();
		announceTopKillers(topKillers);
		giveRewards(topKillers);

		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			iFPlayer.getPlayer().broadcastCharInfo();
			if (iFPlayer.getPlayer().getSummon() != null)
			{
				iFPlayer.getPlayer().getSummon().broadcastCharInfo();
			}
		}

		for (Player player : getAllFightingPlayers())
		{
			showScores(player);
		}

		FightClubEventManager.clearBoxes();
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				for (Player player : getAllFightingPlayers())
				{
					leaveEvent(player, true);
					if (player.isImmobilized())
					{
						player.getFlags().getImmobilized().stop();
						player.stopAbnormalEffect(AbnormalEffect.ROOT);
						player.resetReuse();
						player.sendPacket(new SkillCoolTimePacket(player));
					}
					player.sendPacket(new ExShowScreenMessage("Teleporting to town!", 10, ExShowScreenMessage.ScreenMessageAlign.TOP_LEFT, false));
				}
				ThreadPoolManager.getInstance().schedule(() ->
				{
					destroyMe();
				}, (15 + TIME_TELEPORT_BACK_TOWN) * 1000);
			}
		}, 10000L);
	}

	public void destroyMe()
	{
		if (getReflection() != null)
		{
			for (Zone zone : getReflection().getZones())
				zone.removeListener(_zoneListener);
			getReflection().collapse();
		}

		if (_timer != null)
		{
			_timer.cancel(false);
		}

		_timer = null;
		_bestScores.clear();
		_scores.clear();
		_leftZone.clear();
		getObjects().clear();
		_set = null;
		_room = null;
		_zoneListener = null;

		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.removeListener(_exitListener);
		}

		_exitListener = null;
	}

	public void onKilled(Creature actor, Creature victim)
	{
		if (victim.isPlayer() && getRespawnTime() > 0)
		{
			showScores(victim);
		}
		if (actor != null && actor.isPlayer() && getFightClubPlayer(actor) != null)
		{
			FightClubLastStatsManager.getInstance().updateStat(actor.getPlayer(), FightClubLastStatsManager.FightClubStatType.KILL_PLAYER, getFightClubPlayer(actor).getKills(true));
		}
		if (victim.isPlayer() && getRespawnTime() > 0 && !_ressAllowed && getFightClubPlayer(victim.getPlayer()) != null)
		{
			startNewTimer(false, 0, "ressurectionTimer", getRespawnTime(), getFightClubPlayer(victim));
		}
	}

	public void requestRespawn(Player activeChar, RestartType restartType)
	{
		if (getRespawnTime() > 0)
		{
			startNewTimer(false, 0, "ressurectionTimer", getRespawnTime(), getFightClubPlayer(activeChar));
		}
	}

	public boolean canAttack(Creature target, Creature attacker, Skill skill, boolean force)
	{
		if (_state != EventState.STARTED)
		{
			return false;
		}

		if (_preparing)
		{
			attacker.setTarget((GameObject) attacker);
		}

		Player player = attacker.getPlayer();
		if (player == null || target == null)
		{
			return false;
		}
		if (player == target || player == target.getPlayer())
		{
			return false;
		}

		final FightClubPlayer targetFPlayer = getFightClubPlayer(target);
		final FightClubPlayer attackerFPlayer = getFightClubPlayer(attacker);
		if (targetFPlayer == null && attackerFPlayer == null)
		{
			return false;
		}
		if (isTeamed() && (targetFPlayer == null || attackerFPlayer == null || targetFPlayer.getTeam().equals(attackerFPlayer.getTeam())))
		{
			return false;
		}

		return true;
	}

	public boolean canUseSkill(Creature caster, Creature target, Skill skill)
	{
		if (_preparing)
		{
			caster.setTarget((GameObject) caster);
		}
		if (_excludedSkills != null)
		{
			for (int id : _excludedSkills)
			{
				if (skill.getId() == id)
				{
					return false;
				}
			}
		}
		return true;
	}

	public SystemMsg checkForAttack(Creature target, Creature attacker, Skill skill, boolean force)
	{
		if (_preparing)
		{
			attacker.setTarget((GameObject) attacker);
		}
		if (!canAttack(target, attacker, skill, force))
		{
			return SystemMsg.INVALID_TARGET;
		}
		return null;
	}

	public boolean canRessurect(Player player, Creature creature, boolean force)
	{
		return _ressAllowed;
	}

	public int getMySpeed(Player player)
	{
		return -1;
	}

	public int getPAtkSpd(Player player)
	{
		return -1;
	}

	public void checkRestartLocs(Player player, Map<RestartType, Boolean> r)
	{
		r.clear();
		if (isTeamed() && getRespawnTime() > 0 && getFightClubPlayer(player) != null && _ressAllowed)
		{
			r.put(RestartType.TO_FLAG, Boolean.valueOf(true));
		}
	}

	public boolean canUseBuffer(Player player, boolean heal)
	{
		FightClubPlayer fPlayer = getFightClubPlayer(player);
		if (!getBuffer())
			return false;
		if (player.isInCombat())
			return false;
		if (heal)
		{
			if (player.isDead())
				return false;
			if (_state != EventState.STARTED)
				return true;

			return fPlayer.isInvisible();
		}

		return true;
	}

	public boolean canUsePositiveMagic(Creature user, Creature target)
	{
		Player player = user.getPlayer();
		if (player == null)
			return true;
		if (!isFriend(user, target))
			return false;

		return !isInvisible(player, player);
	}

	public int getRelation(Player thisPlayer, Player target, int oldRelation)
	{
		if (_state == EventState.STARTED)
			return isFriend(thisPlayer, target) ? getFriendRelation() : getWarRelation();
		return oldRelation;
	}

	public boolean canJoinParty(Player sender, Player receiver)
	{
		return isFriend(sender, receiver);
	}

	public boolean canReceiveInvitations(Player sender, Player receiver)
	{
		return true;
	}

	public boolean canOpenStore(Player player)
	{
		return false;
	}

	public boolean loseBuffsOnDeath(Player player)
	{
		return false;
	}

	protected boolean inScreenShowBeScoreNotKills()
	{
		return true;
	}

	protected boolean inScreenShowBeTeamNotInvidual()
	{
		return isTeamed();
	}

	public boolean isFriend(Creature c1, Creature c2)
	{
		if (c1.equals(c2))
			return true;
		if (!c1.isPlayable() || !c2.isPlayable())
			return true;

		if (c1.isSummon() && c2.isPlayer() && c2.getPlayer().getAnyServitor() != null && c2.getPlayer().getAnyServitor().equals(c1))
			return true;

		if (c2.isSummon() && c1.isPlayer() && c1.getPlayer().getAnyServitor() != null && c1.getPlayer().getAnyServitor().equals(c2))
			return true;

		FightClubPlayer fPlayer1 = getFightClubPlayer(c1.getPlayer());
		FightClubPlayer fPlayer2 = getFightClubPlayer(c2.getPlayer());

		if (isTeamed())
			return fPlayer1 != null && fPlayer2 != null && fPlayer1.getTeam() == fPlayer2.getTeam();

		return false;
	}

	public boolean isInvisible(Player actor, Player watcher)
	{
		return actor.getFlags().getInvisible().get();
	}

	public String getVisibleName(Player player, String currentName, boolean toMe)
	{
		if (isHidePersonality() && !toMe)
			return "Player";
		return currentName;
	}

	public String getVisibleTitle(Player player, String currentTitle, boolean toMe)
	{
		return currentTitle;
	}

	public int getVisibleTitleColor(Player player, int currentTitleColor, boolean toMe)
	{
		return currentTitleColor;
	}

	public int getVisibleNameColor(Player player, int currentNameColor, boolean toMe)
	{
		if (isTeamed())
		{
			FightClubPlayer fPlayer = getFightClubPlayer(player);
			return fPlayer.getTeam().getNickColor();
		}
		return currentNameColor;
	}

	protected int getBadgesEarned(FightClubPlayer fPlayer, int currentValue, boolean topKiller)
	{
		if (fPlayer == null)
			return 0;
		currentValue += addMultipleBadgeToPlayer(fPlayer.getKills(true), _badgesKillPlayer);

		currentValue += getRewardForWinningTeam(fPlayer, true);

		int minutesAFK = (int) Math.round(fPlayer.getTotalAfkSeconds() / 60.0D);
		currentValue += minutesAFK * BADGES_FOR_MINUTE_OF_AFK;

		if (topKiller)
		{
			currentValue += topKillerReward;
		}

		return currentValue;
	}

	protected int addMultipleBadgeToPlayer(int score, double badgePerScore)
	{
		return (int) Math.floor(score * badgePerScore);
	}

	protected int addMultipleBadgeToPlayer(FightClubPlayer fPlayer, FightClubStatType whatFor, int score, double badgePerScore, int secondsSpent)
	{
		int badgesEarned = (int) Math.floor(score * badgePerScore);
		return badgesEarned;
	}

	private int getEndEventBadges(FightClubPlayer fPlayer)
	{
		return 0;
	}

	public void startTeleportTimer(FightClubGameRoom room)
	{
		setState(EventState.COUNT_DOWN);

		startNewTimer(true, 0, "teleportWholeRoomTimer", TIME_FIRST_TELEPORT);
	}

	protected void teleportRegisteredPlayers()
	{
		for (final FightClubPlayer player : getPlayers(REGISTERED_PLAYERS))
		{
			teleportSinglePlayer(player, true, true);
		}
	}

	protected void teleportSinglePlayer(FightClubPlayer fPlayer, boolean firstTime, boolean healAndRess)
	{
		Player player = fPlayer.getPlayer();

		if (healAndRess)
		{
			ressurectPlayer(player);
		}

		Location[] spawns = null;
		Location loc = null;

		if (!isTeamed())
			spawns = getMap().getPlayerSpawns();
		else
			loc = getTeamSpawn(fPlayer, true);

		if (!isTeamed())
			loc = getSafeLocation(spawns);

		loc = Location.findPointToStay(loc, 0, CLOSE_LOCATIONS_VALUE / 2, fPlayer.getPlayer().getGeoIndex());

		if (isInstanced())
			player.teleToLocation(loc, getReflection());
		else
			player.teleToLocation(loc);

		if (_state == EventState.PREPARATION || _state == EventState.OVER)
			rootPlayer(player);

		cancelNegativeEffects(player);
		if (player.getPet() != null)
			cancelNegativeEffects(player.getPet());

		if (firstTime)
		{
			removeObject(REGISTERED_PLAYERS, fPlayer);
			addObject(FIGHTING_PLAYERS, fPlayer);

			player.getAbnormalList().stopAll();
			if (player.getAnyServitor() != null)
				player.getAnyServitor().getAbnormalList().stopAll();

			player.store(true);
			player.sendPacket(new ShowTutorialMarkPacket(false, 100));

			player.sendPacket(new SayPacket2(0, ChatType.ALL, 0, getName(), "Normal Chat is visible for every player in event."));
			if (isTeamed())
			{
				player.sendPacket(new SayPacket2(0, ChatType.ALL, 0, getName(), "Battlefield(^) Chat is visible only to your team!"));
				player.sendPacket(new SayPacket2(0, ChatType.BATTLEFIELD, 0, getName(), "Battlefield(^) Chat is visible only to your team!"));
			}
		}

		if (healAndRess)
			buffPlayer(fPlayer.getPlayer());
	}

	public void unregister(Player player)
	{
		final FightClubPlayer fPlayer = getFightClubPlayer(player, REGISTERED_PLAYERS);
		player.removeEvent(this);
		removeObject(REGISTERED_PLAYERS, fPlayer);
		player.sendMessage("You are no longer registered!"); // TODO: Вынести в ДП.
	}

	public boolean leaveEvent(Player player, boolean teleportTown)
	{
		FightClubPlayer fPlayer = getFightClubPlayer(player);
		if (fPlayer == null)
		{
			return true;
		}

		if (_state == EventState.NOT_ACTIVE)
		{
			if (fPlayer.isInvisible())
			{
				stopInvisibility(player);
			}
			removeObject(FIGHTING_PLAYERS, fPlayer);
			if (isTeamed())
			{
				fPlayer.getTeam().removePlayer(fPlayer);
			}
			player.removeEvent(this);

			if (teleportTown)
			{
				ThreadPoolManager.getInstance().schedule(() ->
				{
					ressurectPlayer(player);
					teleportBackToTown(player);
				}, 5000);
			}
			else
			{
				player.doRevive();
			}
		}
		else
		{
			rewardPlayer(fPlayer, false);
			if (teleportTown)
			{
				setInvisible(player, 30, false);
			}
			else
			{
				setInvisible(player, -1, false);
			}
			removeObject(FIGHTING_PLAYERS, fPlayer);

			player.doDie(null);
			player.removeEvent(this);

			if (teleportTown)
			{
				ThreadPoolManager.getInstance().schedule(() ->
				{
					ressurectPlayer(player);
					teleportBackToTown(player);
					startNewTimer(false, 0, "teleportBackSinglePlayerTimer", TIME_TELEPORT_BACK_TOWN, player);
				}, 5000);
			}
			else
			{
				player.doRevive();
			}
		}

		ThreadPoolManager.getInstance().schedule(() ->
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			player.setCurrentCp(player.getMaxCp());
		}, 5000L);

		hideScores(player);
		updateScreenScores();

		if (getPlayers(FIGHTING_PLAYERS, REGISTERED_PLAYERS).isEmpty())
		{
			ThreadPoolManager.getInstance().schedule(() ->
			{
				destroyMe();
			}, (15 + TIME_TELEPORT_BACK_TOWN) * 1000);
		}

		if (player.getParty() != null)
		{
			player.getParty().removePartyMember(player, true, true);
		}

		return true;
	}

	protected static void ressurectPlayer(Player player)
	{
		if (player.isDead())
		{
			player.doRevive(100.0D);
			ThreadPoolManager.getInstance().schedule(() ->
			{
				player.restoreExp();
				player.setCurrentCp(player.getMaxCp());
				player.setCurrentHp(player.getMaxHp(), true);
				player.setCurrentMp(player.getMaxMp());
				player.broadcastPacket(new RevivePacket(player));
				player.setCurrentCp(player.getMaxCp());
				player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			}, 5000L);
		}
	}

	public void loggedOut(Player player)
	{
		leaveEvent(player, true);
	}

	protected void teleportBackToTown(Player player)
	{
		player.setPolyId(0);
		Location loc = Location.findPointToStay(FightClubEventManager.RETURN_LOC, 0, 100, 0);
		player.teleToLocation(loc, true);
		ressurectPlayer(player);
		player.stopAbnormalEffect(AbnormalEffect.STEALTH);
		player.stopInvisible(this, true);
		player.broadcastUserInfo(true);
		player.broadcastCharInfo();
		player.resetReuse();
		player.sendPacket(new SkillCoolTimePacket(player));
	}

	protected void rewardPlayer(FightClubPlayer fPlayer, boolean isTopKiller)
	{
		int badgesToGive = getBadgesEarned(fPlayer, 0, isTopKiller);
		if (getState() == EventState.NOT_ACTIVE)
		{
			badgesToGive += getEndEventBadges(fPlayer);
		}

		badgesToGive = Math.max(0, badgesToGive);
		fPlayer.getPlayer().getInventory().addItem(_badgesId, badgesToGive);
		sendMessageToPlayer(fPlayer, MessageType.SCREEN_BIG, new StringBuilder().append("You have earned ").append(badgesToGive).append(" Festival Adena!").toString());
		sendMessageToPlayer(fPlayer, MessageType.NORMAL_MESSAGE, new StringBuilder().append("You have earned ").append(badgesToGive).append(" Festival Adena!").toString());
	}

	protected void announceWinnerTeam(boolean wholeEvent, FightClubTeam winnerOfTheRound)
	{
		int bestScore = -1;
		FightClubTeam bestTeam = null;
		boolean draw = false;
		if (wholeEvent)
		{
			for (FightClubTeam team : getTeams())
			{
				if (team.getScore() > bestScore)
				{
					draw = false;
					bestScore = team.getScore();
					bestTeam = team;
				}
				else if (team.getScore() == bestScore)
				{
					draw = true;
				}
			}
		}
		else
		{
			bestTeam = winnerOfTheRound;
		}

		SayPacket2 packet;
		if (!draw)
		{
			packet = new SayPacket2(0, ChatType.COMMANDCHANNEL_ALL, 0, new StringBuilder().append(bestTeam.getName()).append(" Team").toString(), new StringBuilder().append("We won ").append(wholeEvent ? getName() : " Round").append("!").toString());
			for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
			{
				iFPlayer.getPlayer().sendPacket(packet);
			}
		}
		updateScreenScores();
	}

	protected void announceWinnerPlayer(boolean wholeEvent, FightClubPlayer winnerOfTheRound)
	{
		int bestScore = -1;
		FightClubPlayer bestPlayer = null;
		boolean draw = false;
		if (wholeEvent)
		{
			for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
			{
				if (iFPlayer.getPlayer() != null && iFPlayer.getPlayer().isOnline())
				{
					if (iFPlayer.getScore() > bestScore)
					{
						bestScore = iFPlayer.getScore();
						bestPlayer = iFPlayer;
					}
					else if (iFPlayer.getScore() == bestScore)
					{
						draw = true;
					}
				}
			}
		}
		else
		{
			bestPlayer = winnerOfTheRound;
		}

		SayPacket2 packet;
		if (!draw && bestPlayer != null)
		{
			packet = new SayPacket2(0, ChatType.COMMANDCHANNEL_ALL, 0, bestPlayer.getPlayer().getName(), new StringBuilder().append("I Won ").append(wholeEvent ? getName() : "Round").append("!").toString());
			for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
			{
				iFPlayer.getPlayer().sendPacket(packet);
			}
		}
		updateScreenScores();
	}

	protected void updateScreenScores()
	{
		String msg = getScreenScores(inScreenShowBeScoreNotKills(), inScreenShowBeTeamNotInvidual());
		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			sendMessageToPlayer(iFPlayer, MessageType.SCREEN_SMALL, msg);
		}
	}

	protected void updateScreenScores(Player player)
	{
		if (getFightClubPlayer(player) != null)
		{
			sendMessageToPlayer(getFightClubPlayer(player), MessageType.SCREEN_SMALL, getScreenScores(inScreenShowBeScoreNotKills(), inScreenShowBeTeamNotInvidual()));
		}
	}

	protected String getScorePlayerName(FightClubPlayer fPlayer)
	{
		return new StringBuilder().append(fPlayer.getPlayer().getName()).append(isTeamed() ? new StringBuilder().append(" (").append(fPlayer.getTeam().getName()).append(" Team)").toString() : "").toString();
	}

	protected void updatePlayerScore(FightClubPlayer fPlayer)
	{
		_scores.put(getScorePlayerName(fPlayer), Integer.valueOf(fPlayer.getKills(true)));
		_scoredUpdated = true;

		if (!isTeamed())
			updateScreenScores();
	}

	protected void showScores(Creature c)
	{
		Map<String, Integer> scores = getBestScores();
		FightClubPlayer fPlayer = getFightClubPlayer(c);

		if (fPlayer != null)
		{
			fPlayer.setShowRank(true);
		}
		c.sendPacket(new ExPVPMatchCCRecord(scores));
	}

	protected void hideScores(Creature c)
	{
		c.sendPacket(ExPVPMatchCCRetire.STATIC);
	}

	private void handleAfk(FightClubPlayer fPlayer, boolean setAsAfk)
	{
		Player player = fPlayer.getPlayer();

		if (setAsAfk)
		{
			fPlayer.setAfk(true);
			fPlayer.setAfkStartTime(player.getLastNotAfkTime());
			sendMessageToPlayer(player, MessageType.CRITICAL, "You are considered as AFK Player!");
		}
		else if (fPlayer.isAfk())
		{
			int totalAfkTime = (int) ((System.currentTimeMillis() - fPlayer.getAfkStartTime()) / 1000L);
			totalAfkTime -= TIME_TO_BE_AFK;
			if (totalAfkTime > 5)
			{
				fPlayer.setAfk(false);
				fPlayer.addTotalAfkSeconds(totalAfkTime);
				sendMessageToPlayer(player, MessageType.CRITICAL, new StringBuilder().append("You were afk for ").append(totalAfkTime).append(" seconds!").toString());
			}
		}
	}

	protected void setInvisible(Player player, int seconds, boolean sendMessages)
	{
		FightClubPlayer fPlayer = getFightClubPlayer(player);
		fPlayer.setInvisible(true);
		player.startAbnormalEffect(AbnormalEffect.STEALTH);
		player.startInvisible(this, true);
		player.sendUserInfo(true);

		if (seconds > 0)
		{
			startNewTimer(false, 0, "setInvisible", seconds, fPlayer, sendMessages);
		}
	}

	protected void stopInvisibility(Player player)
	{
		FightClubPlayer fPlayer = getFightClubPlayer(player);
		if (fPlayer != null)
		{
			fPlayer.setInvisible(false);
		}

		player.stopAbnormalEffect(AbnormalEffect.STEALTH);
		player.stopInvisible(this, true);
	}

	protected void rootPlayer(Player player)
	{
		if (!isRootBetweenRounds())
		{
			return;
		}

		List<Playable> toRoot = new ArrayList<Playable>();
		toRoot.add(player);
		if (player.getAnyServitor() != null)
		{
			toRoot.add(player.getAnyServitor());
		}
		if (!player.isImmobilized())
		{
			player.startRooted();
		}
		player.getMovement().stopMove(true);
		player.startAbnormalEffect(AbnormalEffect.ROOT);
	}

	protected void unrootPlayers()
	{
		if (!isRootBetweenRounds())
		{
			return;
		}

		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			final Player player = iFPlayer.getPlayer();
			if (player != null)
			{
				if (player.isImmobilized())
				{
					player.stopRooted();
					player.stopAbnormalEffect(AbnormalEffect.ROOT);
				}
				if (!player.isDead())
				{
					player.setCurrentCp(player.getMaxCp());
					player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
				}
			}
		}
	}

	protected void ressAndHealPlayers()
	{
		for (final FightClubPlayer fPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			Player player = fPlayer.getPlayer();

			ressurectPlayer(player);
			cancelNegativeEffects(player);
			if (player.getAnyServitor() != null)
			{
				cancelNegativeEffects((Playable) player.getAnyServitor());
			}
			buffPlayer(player);
		}
	}

	protected int getWarRelation()
	{
		int result = 0;

		result = (int) (result | RelationChangedPacket.RelationChangedType.IN_PLEDGE.getRelationState());
		result = (int) (result | RelationChangedPacket.RelationChangedType.CLAN_WAR_ATTACKED.getRelationState());
		result = (int) (result | RelationChangedPacket.RelationChangedType.CLAN_WAR_ATTACKER.getRelationState());

		return result;
	}

	protected int getFriendRelation()
	{
		int result = 0;

		result = (int) (result | RelationChangedPacket.RelationChangedType.IN_PLEDGE.getRelationState());
		result = (int) (result | RelationChangedPacket.RelationChangedType.SAME_PLEDGE.getRelationState());

		return result;
	}

	protected NpcInstance chooseLocAndSpawnNpc(int id, Location[] locs, int respawnInSeconds)
	{
		return spawnNpc(id, getSafeLocation(locs), respawnInSeconds);
	}

	protected NpcInstance spawnNpc(int id, Location loc, int respawnInSeconds)
	{
		SimpleSpawner spawn = new SimpleSpawner(id);
		List<NpcInstance> npcs = spawn.initAndReturn();

		spawn.setSpawnRange((geoIndex, fly) -> new Location(loc));
		spawn.setAmount(1);
		spawn.setRespawnDelay(Math.max(0, respawnInSeconds));
		spawn.setReflection(getReflection());

		if (respawnInSeconds <= 0)
		{
			spawn.stopRespawn();
		}
		return npcs.get(0);
	}

	protected static String getFixedTime(int seconds)
	{
		int minutes = seconds / 60;
		String result = "";
		if (seconds >= 60)
		{
			result = new StringBuilder().append(minutes).append(" minute").append(minutes > 1 ? "s" : "").toString();
		}
		else
		{
			result = new StringBuilder().append(seconds).append(" second").append(seconds > 1 ? "s" : "").toString();
		}
		return result;
	}

	private void buffPlayer(Player player)
	{
		if (getBuffer())
		{
			giveBuffs(player, player.isMageClass() ? _mageBuffs : _fighterBuffs);
			if (player.getAnyServitor() != null)
			{
				giveBuffs(player.getAnyServitor(), _fighterBuffs);
			}
		}
	}

	private static void giveBuffs(final Playable playable, int[][] buffs)
	{
		for (int i = 0; i < buffs.length; i++)
		{
			Skill buff = SkillHolder.getInstance().getSkill(buffs[i][0], buffs[i][1]);
			if (buff == null)
			{
				continue;
			}
			buff.getEffects(playable, playable);
		}

		ThreadPoolManager.getInstance().schedule(() ->
		{
			playable.setCurrentHp(playable.getMaxHp(), true);
			playable.setCurrentMp(playable.getMaxMp());
			playable.setCurrentCp(playable.getMaxCp());
		}, 1000L);
	}

	protected void sendMessageToFightingAndRegistered(MessageType type, String msg)
	{
		sendMessageToFighting(type, msg, false);
		sendMessageToRegistered(type, msg);
	}

	protected void sendMessageToTeam(FightClubTeam team, MessageType type, String msg)
	{
		for (FightClubPlayer iFPlayer : team.getPlayers())
		{
			sendMessageToPlayer(iFPlayer, type, msg);
		}
	}

	protected void sendMessageToFighting(MessageType type, String msg, boolean skipJustTeleported)
	{
		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			if (!skipJustTeleported || !iFPlayer.isInvisible())
			{
				sendMessageToPlayer(iFPlayer, type, msg);
			}
		}
	}

	protected void sendMessageToRegistered(MessageType type, String msg)
	{
		for (final FightClubPlayer iFPlayer : getPlayers(REGISTERED_PLAYERS))
		{
			sendMessageToPlayer(iFPlayer, type, msg);
		}
	}

	public void sendMessageToPlayer(FightClubPlayer fPlayer, MessageType type, String msg)
	{
		sendMessageToPlayer(fPlayer.getPlayer(), type, msg);
	}

	protected void sendMessageToPlayer(Player player, MessageType type, String msg)
	{
		switch (type)
		{
			case GM:
				player.sendPacket(new SayPacket2(0, ChatType.CRITICAL_ANNOUNCE, 0, player.getName(), msg));
				updateScreenScores(player);
				break;
			case NORMAL_MESSAGE:
				player.sendMessage(msg);
				break;
			case SCREEN_BIG:
				player.sendPacket(new ExShowScreenMessage(msg, 3000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
				updateScreenScores(player);
				break;
			case SCREEN_SMALL:
				player.sendPacket(new ExShowScreenMessage(msg, 600000, ExShowScreenMessage.ScreenMessageAlign.TOP_LEFT, false));
				break;
			case CRITICAL:
				player.sendPacket(new SayPacket2(0, ChatType.COMMANDCHANNEL_ALL, 0, player.getName(), msg));
				updateScreenScores(player);
				break;
		}
	}

	public void setState(EventState state)
	{
		_state = state;
	}

	public EventState getState()
	{
		return _state;
	}

	public int getObjectId()
	{
		return _objId;
	}

	public int getEventId()
	{
		return getId();
	}

	public String getDescription()
	{
		return _desc;
	}

	public String getIcon()
	{
		return _icon;
	}

	public boolean isAutoTimed()
	{
		return _isAutoTimed;
	}

	public int[][] getAutoStartTimes()
	{
		return _autoStartTimes;
	}

	public FightClubMap getMap()
	{
		return _map;
	}

	public boolean isTeamed()
	{
		return _teamed;
	}

	protected boolean isInstanced()
	{
		return _instanced;
	}

	public Reflection getReflection()
	{
		return _reflection;
	}

	public int getRoundRuntime()
	{
		return _roundRunTime;
	}

	public int getRespawnTime()
	{
		return _respawnTime;
	}

	public boolean isRoundEvent()
	{
		return _roundEvent;
	}

	public int getTotalRounds()
	{
		return _rounds;
	}

	public int getCurrentRound()
	{
		return _currentRound;
	}

	public boolean getBuffer()
	{
		return _buffer;
	}

	protected boolean isRootBetweenRounds()
	{
		return _rootBetweenRounds;
	}

	public boolean isLastRound()
	{
		return (!isRoundEvent()) || (getCurrentRound() == getTotalRounds());
	}

	protected List<FightClubTeam> getTeams()
	{
		return _teams;
	}

	public MultiValueSet<String> getSet()
	{
		return _set;
	}

	public void clearSet()
	{
		_set = null;
	}

	public PlayerClass[] getExcludedClasses()
	{
		return _excludedClasses;
	}

	public boolean isHidePersonality()
	{
		return !_showPersonality;
	}

	protected int getTeamTotalKills(FightClubTeam team)
	{
		if (!isTeamed())
			return 0;
		int totalKills = 0;
		for (FightClubPlayer iFPlayer : team.getPlayers())
		{
			totalKills += iFPlayer.getKills(true);
		}
		return totalKills;
	}

	public int getPlayersCount(String[] groups)
	{
		return getPlayers(groups).size();
	}

	public List<FightClubPlayer> getPlayers(String... groups)
	{
		if (groups.length == 1)
		{
			final List<FightClubPlayer> fPlayers = getObjects(groups[0]);
			return fPlayers;
		}
		else
		{
			final List<FightClubPlayer> newList = new ArrayList<>();
			for (final String group : groups)
			{
				final List<FightClubPlayer> fPlayers = getObjects(group);
				newList.addAll(fPlayers);
			}
			return newList;
		}
	}

	public List<Player> getAllFightingPlayers()
	{
		final List<FightClubPlayer> fPlayers = getPlayers(FIGHTING_PLAYERS);
		final List<Player> players = new ArrayList<>(fPlayers.size());
		for (FightClubPlayer fPlayer : fPlayers)
		{
			players.add(fPlayer.getPlayer());
		}
		return players;
	}

	public List<Player> getMyTeamFightingPlayers(Player player)
	{
		final FightClubTeam fTeam = getFightClubPlayer(player).getTeam();
		final List<FightClubPlayer> fPlayers = getPlayers(FIGHTING_PLAYERS);
		final List<Player> players = new ArrayList<Player>(fPlayers.size());

		if (!isTeamed())
		{
			player.sendPacket(new SayPacket2(0, ChatType.BATTLEFIELD, 0, getName(), "(There are no teams, only you can see the message)"));
			players.add(player);
		}
		else
		{
			for (FightClubPlayer iFPlayer : fPlayers)
			{
				if (iFPlayer.getTeam().equals(fTeam))
				{
					players.add(iFPlayer.getPlayer());
				}
			}
		}
		return players;
	}

	public FightClubPlayer getFightClubPlayer(Creature creature)
	{
		return getFightClubPlayer(creature, FIGHTING_PLAYERS);
	}

	public FightClubPlayer getFightClubPlayer(Creature creature, String... groups)
	{
		if (creature == null || !creature.isPlayable())
		{
			return null;
		}

		final int lookedPlayerId = creature.getPlayer().getObjectId();

		for (FightClubPlayer iFPlayer : getPlayers(groups))
		{
			if (iFPlayer.getPlayer().getObjectId() == lookedPlayerId)
			{
				return iFPlayer;
			}
		}
		return null;
	}

	private void spreadIntoTeamsAndPartys()
	{
		for (int i = 0; i < _room.getTeamsCount(); i++)
		{
			_teams.add(new FightClubTeam(i + 1));
		}
		int index = 0;
		for (Player player : _room.getAllPlayers())
		{
			FightClubTeam team = _teams.get(index % _room.getTeamsCount());
			final FightClubPlayer fPlayer = getFightClubPlayer(player, REGISTERED_PLAYERS);

			if (fPlayer == null)
			{
				continue;
			}

			fPlayer.setTeam(team);
			team.addPlayer(fPlayer);
			index++;
		}

		for (FightClubTeam team : _teams)
		{
			List<List<Player>> partys = spreadTeamInPartys(team);
			for (List<Player> party : partys)
			{
				createParty(party);
			}
		}
	}

	private List<List<Player>> spreadTeamInPartys(FightClubTeam team)
	{
		Map<PlayerClass, List<Player>> classesMap = new HashMap<>();
		for (PlayerClass getPlayerClass : PlayerClass.values())
		{
			classesMap.put(getPlayerClass, new ArrayList<Player>());
		}

		for (FightClubPlayer iFPlayer : team.getPlayers())
		{
			Player player = iFPlayer.getPlayer();
			PlayerClass getClassGroup = FightClubGameRoom.getPlayerClassGroup(player);
			if (getClassGroup != null)
			{
				classesMap.get(getClassGroup).add(player);
			}
			else
			{
				_log.warn("AbstractFightEvent: Problem with add player - " + player.getName());
				_log.warn("AbstractFightEvent: Class - " + player.getClassId().name() + " null for event!");
			}
		}

		int partyCount = (int) Math.ceil(team.getPlayers().size() / Party.MAX_SIZE);

		List<List<Player>> partys = new ArrayList<List<Player>>();
		for (int i = 0; i < partyCount; i++)
		{
			partys.add(new ArrayList<Player>());
		}

		if (partyCount == 0)
		{
			return partys;
		}

		int finishedOnIndex = 0;
		for (Entry<PlayerClass, List<Player>> getClassEntry : classesMap.entrySet())
		{
			for (Player player : getClassEntry.getValue())
			{
				partys.get(finishedOnIndex).add(player);
				finishedOnIndex++;
				if (finishedOnIndex == partyCount)
				{
					finishedOnIndex = 0;
				}
			}
		}
		return partys;
	}

	private void createParty(List<Player> listOfPlayers)
	{
		if (listOfPlayers.size() <= 1)
		{
			return;
		}

		Party newParty = null;
		for (Player player : listOfPlayers)
		{
			if (player.getParty() != null)
			{
				player.getParty().removePartyMember(player, true, true);
			}

			if (newParty == null)
			{
				player.setParty(newParty = new Party(player, 4));
			}
			else
			{
				player.joinParty(newParty, true);
			}
		}
	}

	private synchronized void createReflection(IntObjectMap<DoorTemplate> doors, Map<String, ZoneTemplate> zones)
	{
		InstantZone iz = InstantZoneHolder.getInstance().getInstantZone(400);

		_reflection = new Reflection();
		_reflection.init(iz);
		_reflection.init(doors, zones);

		for (Zone zone : _reflection.getZones())
		{
			zone.addListener(_zoneListener);
		}
	}

	private Location getSafeLocation(Location[] locations)
	{
		Location safeLoc = null;
		int checkedCount = 0;
		boolean isOk = false;

		while (!isOk)
		{
			safeLoc = Rnd.get(locations);
			isOk = nobodyIsClose(safeLoc);
			checkedCount++;

			if (checkedCount > locations.length * 2)
			{
				isOk = true;
			}
		}
		return safeLoc;
	}

	protected Location getTeamSpawn(FightClubPlayer fPlayer, boolean randomNotClosestToPt)
	{
		FightClubTeam team = fPlayer.getTeam();
		Location[] spawnLocs = getMap().getTeamSpawns().get(team.getIndex());

		if (randomNotClosestToPt || _state != EventState.STARTED)
		{
			return Rnd.get(spawnLocs);
		}
		else
		{
			List<Player> playersToCheck = new ArrayList<Player>();
			if (fPlayer.getParty() != null)
			{
				playersToCheck = fPlayer.getParty().getPartyMembers();
			}
			else
			{
				for (FightClubPlayer iFPlayer : team.getPlayers())
				{
					playersToCheck.add(iFPlayer.getPlayer());
				}
			}

			final Map<Location, Integer> spawnLocations = new HashMap<>(spawnLocs.length);
			for (Location loc : spawnLocs)
			{
				spawnLocations.put(loc, 0);
			}

			for (Player player : playersToCheck)
			{
				if (player != null && player.isOnline() && !player.isDead())
				{
					Location winner = null;
					double winnerDist = -1;
					for (Location loc : spawnLocs)
					{
						if (winnerDist <= 0 || winnerDist < player.getDistance(loc))
						{
							winner = loc;
							winnerDist = player.getDistance(loc);
						}
					}

					if (winner != null)
					{
						spawnLocations.put(winner, spawnLocations.get(winner) + 1);
					}
				}
			}

			Location winner = null;
			double points = -1;
			for (Entry<Location, Integer> spawn : spawnLocations.entrySet())
			{
				if (points < spawn.getValue())
				{
					winner = spawn.getKey();
					points = spawn.getValue();
				}
			}

			if (points <= 0.0D)
			{
				return Rnd.get(spawnLocs);
			}
			return winner;
		}
	}

	private void giveRewards(FightClubPlayer[] topKillers)
	{
		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			if (iFPlayer != null)
			{
				rewardPlayer(iFPlayer, Util.arrayContains(topKillers, iFPlayer));
			}
		}
	}

	private void showLastAFkMessage()
	{
		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			int minutesAFK = (int) Math.round(iFPlayer.getTotalAfkSeconds() / 60.0D);
			int badgesDecreased = -minutesAFK * BADGES_FOR_MINUTE_OF_AFK;
			if (badgesDecreased > 0)
			{
				sendMessageToPlayer(iFPlayer, MessageType.NORMAL_MESSAGE, new StringBuilder().append("Reward decreased by ").append(badgesDecreased).append(" FA for AFK time!").toString());
			}
		}
	}

	private Map<String, Integer> getBestScores()
	{
		if (!_scoredUpdated)
		{
			return _bestScores;
		}

		List<Integer> points = new ArrayList<Integer>(_scores.values());
		Collections.sort(points);
		Collections.reverse(points);

		int cap;
		if (points.size() <= 26)
		{
			cap = points.size() - 1;
		}
		else
		{
			cap = 25;
		}
		Map<String, Integer> finalResult = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> i : _scores.entrySet())
		{
			if (i.getValue() > cap)
			{
				finalResult.put(i.getKey(), i.getValue());
			}
		}

		if (finalResult.size() < 25)
		{
			for (Map.Entry<String, Integer> i : _scores.entrySet())
			{
				if (i.getValue() == cap)
				{
					finalResult.put(i.getKey(), i.getValue());
					if (finalResult.size() == 25)
						break;
				}
			}
		}

		_bestScores = finalResult;
		_scoredUpdated = false;
		return finalResult;
	}

	private void updateEveryScore()
	{
		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			iFPlayer.getPlayer().sendUserInfo(true);
			iFPlayer.getPlayer().broadcastCharInfo();
			_scores.put(getScorePlayerName(iFPlayer), iFPlayer.getKills(true));
			_scoredUpdated = true;
		}
	}

	private String getScreenScores(boolean showScoreNotKills, boolean teamPointsNotInvidual)
	{
		String msg = "";
		if (isTeamed() && teamPointsNotInvidual)
		{
			List<FightClubTeam> teams = getTeams();
			Collections.sort(teams, new BestTeamComparator(showScoreNotKills));
			for (FightClubTeam team : teams)
			{
				msg = new StringBuilder().append(msg).append(team.getName()).append(" Team: ").append(showScoreNotKills ? team.getScore() : getTeamTotalKills(team)).append(" ").append(showScoreNotKills ? "Points" : "Kills").append("\n").toString();
			}
		}
		else
		{
			final List<FightClubPlayer> fPlayers = getPlayers(FIGHTING_PLAYERS);
			final List<FightClubPlayer> changedFPlayers = new ArrayList<FightClubPlayer>(fPlayers.size());
			changedFPlayers.addAll(fPlayers);

			Collections.sort(changedFPlayers, new BestPlayerComparator(showScoreNotKills));
			int max = Math.min(10, changedFPlayers.size());
			for (int i = 0; i < max; i++)
			{
				msg = new StringBuilder().append(msg).append(changedFPlayers.get(i).getPlayer().getName()).append(" ").append(showScoreNotKills ? "Score" : "Kills").append(": ").append(showScoreNotKills ? changedFPlayers.get(i).getScore() : changedFPlayers.get(i).getKills(true)).append("\n").toString();
			}
		}
		return msg;
	}

	protected int getRewardForWinningTeam(FightClubPlayer fPlayer, boolean atLeast1Kill)
	{
		if ((!_teamed) || ((_state != EventState.OVER) && (_state != EventState.NOT_ACTIVE)))
		{
			return 0;
		}
		if ((atLeast1Kill) && (fPlayer.getKills(true) <= 0) && (FightClubGameRoom.getPlayerClassGroup(fPlayer.getPlayer()) != PlayerClass.HEALERS))
		{
			return 0;
		}
		FightClubTeam winner = null;
		int winnerPoints = -1;
		boolean sameAmount = false;
		for (FightClubTeam team : getTeams())
		{
			if (team.getScore() > winnerPoints)
			{
				winner = team;
				winnerPoints = team.getScore();
				sameAmount = false;
			}
			else if (team.getScore() == winnerPoints)
			{
				sameAmount = true;
			}
		}

		if ((!sameAmount) && (fPlayer.getTeam().equals(winner)))
		{
			return (int) _badgeWin;
		}

		return 0;
	}

	private boolean nobodyIsClose(Location loc)
	{
		for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			final Location playerLoc = iFPlayer.getPlayer().getLoc();
			if (Math.abs(playerLoc.getX() - loc.getX()) <= CLOSE_LOCATIONS_VALUE)
			{
				return false;
			}
			if (Math.abs(playerLoc.getY() - loc.getY()) <= CLOSE_LOCATIONS_VALUE)
			{
				return false;
			}
		}
		return true;
	}

	private void checkIfRegisteredMeetCriteria()
	{
		for (final FightClubPlayer iFPlayer : getPlayers(REGISTERED_PLAYERS))
		{
			if (iFPlayer != null)
			{
				checkIfRegisteredPlayerMeetCriteria(iFPlayer);
			}
		}
	}

	private boolean checkIfRegisteredPlayerMeetCriteria(FightClubPlayer fPlayer)
	{
		return FightClubEventManager.getInstance().canPlayerParticipate(fPlayer.getPlayer(), true, false);
	}

	private void cancelNegativeEffects(Playable playable)
	{
		List<Abnormal> _buffList = new ArrayList<>();

		for (Abnormal e : playable.getAbnormalList().values())
		{
			if (e.isOffensive() && e.isCancelable())
			{
				_buffList.add(e);
			}
		}

		for (Abnormal e : _buffList)
		{
			e.exit();
		}
	}

	private PlayerClass[] parseExcludedClasses(String classes)
	{
		if (classes.isEmpty())
		{
			return new PlayerClass[0];
		}

		String[] classType = classes.split(";");
		PlayerClass[] realTypes = new PlayerClass[classType.length];

		for (int i = 0; i < classType.length; i++)
		{
			realTypes[i] = PlayerClass.valueOf(classType[i]);
		}

		return realTypes;
	}

	protected int[] parseExcludedSkills(String ids)
	{
		if (ids == null || ids.isEmpty())
		{
			return null;
		}

		StringTokenizer st = new StringTokenizer(ids, ";");
		int[] realIds = new int[st.countTokens()];
		int index = 0;
		while (st.hasMoreTokens())
		{
			realIds[index] = Integer.parseInt(st.nextToken());
			index++;
		}
		return realIds;
	}

	private int[][] parseAutoStartTimes(String times)
	{
		if (times == null || times.isEmpty())
			return (int[][]) null;

		StringTokenizer st = new StringTokenizer(times, ",");
		int[][] realTimes = new int[st.countTokens()][2];
		int index = 0;
		while (st.hasMoreTokens())
		{
			String[] hourMin = st.nextToken().split(":");
			int[] realHourMin =
			{
				Integer.parseInt(hourMin[0]),
				Integer.parseInt(hourMin[1])
			};
			realTimes[index] = realHourMin;
			index++;
		}
		return realTimes;
	}

	private int[][] parseBuffs(String buffs)
	{
		if (buffs == null || buffs.isEmpty())
			return (int[][]) null;

		StringTokenizer st = new StringTokenizer(buffs, ";");
		int[][] realBuffs = new int[st.countTokens()][2];
		int index = 0;
		while (st.hasMoreTokens())
		{
			String[] skillLevel = st.nextToken().split(",");
			int[] realHourMin =
			{
				Integer.parseInt(skillLevel[0]),
				Integer.parseInt(skillLevel[1])
			};
			realBuffs[index] = realHourMin;
			index++;
		}
		return realBuffs;
	}

	public void onDamage(Creature actor, Creature victim, double damage)
	{
	}

	public boolean canAttackDoor(DoorInstance door, Creature attacker)
	{
		Player player;
		FightClubPlayer fPlayer;
		switch (door.getDoorType())
		{
			case WALL:
				return false;
			case DOOR:
				player = attacker.getPlayer();
				if (player == null)
					return false;
				fPlayer = getFightClubPlayer((Creature) player);
				if (fPlayer == null)
					return false;
				break;
		}
		return !door.isInvulnerable();
	}

	public synchronized boolean onTalkNpc(Player activeChar, NpcInstance npc)
	{
		return false;
	}

	private int getTimeToWait(int totalLeftTimeInSeconds)
	{
		int toWait = 1;

		int[] stops =
		{
			5,
			15,
			30,
			60,
			300,
			600,
			900
		};

		for (int stop : stops)
		{
			if (totalLeftTimeInSeconds > stop)
			{
				toWait = stop;
			}
		}
		return toWait;
	}

	public static boolean teleportWholeRoomTimer(int eventObjId, int secondsLeft)
	{
		final AbstractFightClub event = FightClubEventManager.getInstance().getEventByObjId(eventObjId);
		if (secondsLeft == 0)
		{
			event._dontLetAnyoneIn = true;
			event.startEvent();
		}
		else
		{
			event.checkIfRegisteredMeetCriteria();
			event.sendMessageToRegistered(MessageType.SCREEN_BIG, new StringBuilder().append("You are going to be teleported in ").append(getFixedTime(secondsLeft)).append("!").toString());
		}
		return true;
	}

	public static boolean startRoundTimer(int eventObjId, int secondsLeft)
	{
		AbstractFightClub event = FightClubEventManager.getInstance().getEventByObjId(eventObjId);

		if (secondsLeft > 0)
		{
			String firstWord;
			if (event.isRoundEvent())
			{
				firstWord = new StringBuilder().append(event.getCurrentRound() + 1 == event.getTotalRounds() ? "Last" : ROUND_NUMBER_IN_STRING[(event.getCurrentRound() + 1)]).append(" Round").toString();
			}
			else
			{
				firstWord = "Match";
			}
			String message = new StringBuilder().append(firstWord).append(" is going to start in ").append(getFixedTime(secondsLeft)).append("!").toString();
			event.sendMessageToFighting(MessageType.SCREEN_BIG, message, true);
		}
		else
		{
			event.startRound();
		}
		return true;
	}

	public static boolean endRoundTimer(int eventObjId, int secondsLeft)
	{
		final AbstractFightClub event = FightClubEventManager.getInstance().getEventByObjId(eventObjId);
		if (secondsLeft > 0)
		{
			event.sendMessageToFighting(MessageType.SCREEN_BIG, new StringBuilder().append(!event.isLastRound() ? "Round" : "Match").append(" is going to be Over in ").append(getFixedTime(secondsLeft)).append("!").toString(), false);
		}
		else
		{
			event.endRound();
		}
		return true;
	}

	public static boolean shutDownTimer(int eventObjId, int secondsLeft)
	{
		final AbstractFightClub event = FightClubEventManager.getInstance().getEventByObjId(eventObjId);

		if (!FightClubEventManager.getInstance().serverShuttingDown())
		{
			event._dontLetAnyoneIn = false;
			return false;
		}

		if (secondsLeft < 180)
		{
			if (!event._dontLetAnyoneIn)
			{
				event.sendMessageToRegistered(MessageType.CRITICAL, "You are no longer registered because of Shutdown!");
				for (final FightClubPlayer player : event.getPlayers(REGISTERED_PLAYERS))
				{
					event.unregister(player.getPlayer());
				}
				event.getObjects(REGISTERED_PLAYERS).clear();
				event._dontLetAnyoneIn = true;
			}
		}

		if (secondsLeft < 60)
		{
			event._timer.cancel(false);
			event.sendMessageToFighting(MessageType.CRITICAL, "Event ended because of Shutdown!", false);
			event.setState(EventState.OVER);
			event.stopEvent(false);

			event._dontLetAnyoneIn = false;
			return false;
		}
		return true;
	}

	public static boolean teleportBackSinglePlayerTimer(int eventObjId, int secondsLeft, Player player)
	{
		final AbstractFightClub event = FightClubEventManager.getInstance().getEventByObjId(eventObjId);

		if (player == null || !player.isOnline())
		{
			return false;
		}

		if (secondsLeft > 0)
		{
			event.sendMessageToPlayer(player, MessageType.SCREEN_BIG, new StringBuilder().append("You are going to be teleported back in ").append(getFixedTime(secondsLeft)).append("!").toString());
		}
		else
		{
			event.teleportBackToTown(player);
		}
		return true;
	}

	public static boolean ressurectionTimer(int eventObjId, int secondsLeft, FightClubPlayer fPlayer)
	{
		final AbstractFightClub event = FightClubEventManager.getInstance().getEventByObjId(eventObjId);
		final Player player = fPlayer.getPlayer();

		if (player == null || !player.isOnline() || !player.isDead())
		{
			return false;
		}

		if (secondsLeft > 0)
		{
			player.sendMessage(new StringBuilder().append("Respawn in ").append(getFixedTime(secondsLeft)).append("!").toString());
		}
		else
		{
			event.hideScores(player);
			event.teleportSinglePlayer(fPlayer, false, true);
		}
		return true;
	}

	public static boolean setInvisible(int eventObjId, int secondsLeft, FightClubPlayer fPlayer, boolean sendMessages)
	{
		final AbstractFightClub event = FightClubEventManager.getInstance().getEventByObjId(eventObjId);
		if (fPlayer.getPlayer() == null || !fPlayer.getPlayer().isOnline())
		{
			return false;
		}

		if (secondsLeft > 0)
		{
			if (sendMessages)
			{
				event.sendMessageToPlayer(fPlayer, MessageType.SCREEN_BIG, new StringBuilder().append("Visible in ").append(getFixedTime(secondsLeft)).append("!").toString());
			}
		}
		else
		{
			if (sendMessages && event.getState() == EventState.STARTED)
			{
				event.sendMessageToPlayer(fPlayer, MessageType.SCREEN_BIG, "Fight!");
			}
			event.stopInvisibility(fPlayer.getPlayer());
		}
		return true;
	}

	public void startNewTimer(boolean saveAsMainTimer, int firstWaitingTimeInMilis, String methodName, Object... args)
	{
		final ScheduledFuture<?> timer = ThreadPoolManager.getInstance().schedule(new SmartTimer(methodName, saveAsMainTimer, args), firstWaitingTimeInMilis);
		if (saveAsMainTimer)
		{
			_timer = timer;
		}
	}

	@Override
	public void reCalcNextTime(boolean onInit)
	{
		clearActions();
		registerActions();
	}

	@Override
	public EventType getType()
	{
		return EventType.FIGHT_CLUB_EVENT;
	}

	@Override
	protected long startTimeMillis()
	{
		return 0L;
	}

	@Override
	public void onAddEvent(GameObject o)
	{
		if (o.isPlayer())
		{
			o.getPlayer().addListener(_exitListener);
		}
	}

	@Override
	public void onRemoveEvent(GameObject o)
	{
		if (o.isPlayer())
		{
			o.getPlayer().removeListener(_exitListener);
		}
	}

	@Override
	public void printInfo()
	{
		info(getName() + " inited");
	}

	public void printScheduledTime(long startTime)
	{
		info(getName() + " time - " + TimeUtils.toSimpleFormat(startTime));
	}

	@Override
	public boolean isInProgress()
	{
		return _state != EventState.NOT_ACTIVE;
	}

	private class SmartTimer implements Runnable
	{
		private final String _methodName;
		private final Object[] _args;
		private final boolean _saveAsMain;

		private SmartTimer(String methodName, boolean saveAsMainTimer, Object[] args)
		{
			_methodName = methodName;

			final Object[] changedArgs = new Object[args.length + 1];
			changedArgs[0] = Integer.valueOf(getObjectId());
			for (int i = 0; i < args.length; i++)
			{
				changedArgs[(i + 1)] = args[i];
			}
			_args = changedArgs;
			_saveAsMain = saveAsMainTimer;
		}

		@Override
		public void run()
		{
			final Class<?>[] parameterTypes = new Class<?>[_args.length];
			for (int i = 0; i < _args.length; i++)
			{
				parameterTypes[i] = _args[i] != null ? _args[i].getClass() : null;
			}

			int waitingTime = ((Integer) _args[1]).intValue();

			try
			{
				Object ret = MethodUtils.invokeMethod(AbstractFightClub.this, _methodName, _args, parameterTypes);
				if ((boolean) ret == false)
				{
					return;
				}
			}
			catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
			{
				e.printStackTrace();
			}

			if (waitingTime > 0)
			{
				int toWait = AbstractFightClub.this.getTimeToWait(waitingTime);
				waitingTime -= toWait;
				_args[1] = Integer.valueOf(waitingTime);
				ScheduledFuture<?> timer = ThreadPoolManager.getInstance().schedule(this, toWait * 1000);
				if (_saveAsMain)
				{
					_timer = timer;
				}
			}
			else
			{
				return;
			}
		}
	}

	private class BestPlayerComparator implements Comparator<FightClubPlayer>
	{
		private boolean _scoreNotKills;

		private BestPlayerComparator(boolean scoreNotKills)
		{
			_scoreNotKills = scoreNotKills;
		}

		public int compare(FightClubPlayer arg0, FightClubPlayer arg1)
		{
			if (_scoreNotKills)
			{
				return Integer.compare(arg1.getScore(), arg0.getScore());
			}
			return Integer.compare(arg1.getKills(true), arg0.getKills(true));
		}
	}

	private class BestTeamComparator implements Comparator<FightClubTeam>
	{
		private boolean _scoreNotKills;

		private BestTeamComparator(boolean scoreNotKills)
		{
			_scoreNotKills = scoreNotKills;
		}

		@Override
		public int compare(FightClubTeam arg0, FightClubTeam arg1)
		{
			if (_scoreNotKills)
			{
				return Integer.compare(arg1.getScore(), arg0.getScore());
			}
			return Integer.compare(getTeamTotalKills(arg1), getTeamTotalKills(arg0));
		}
	}

	private class CheckAfkThread implements Runnable
	{
		@Override
		public void run()
		{
			long currentTime = System.currentTimeMillis();
			for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
			{
				final Player player = iFPlayer.getPlayer();
				boolean isAfk = player.getLastNotAfkTime() + 30000L < currentTime;

				if (player.isDead() && !_ressAllowed && getRespawnTime() <= 0)
				{
					isAfk = false;
				}

				if (iFPlayer.isAfk())
				{
					if (!isAfk)
					{
						handleAfk(iFPlayer, false);
					}
					else if (_state != EventState.OVER)
					{
						sendMessageToPlayer(player, MessageType.CRITICAL, "You are in AFK mode!");
					}
				}
				else if (_state == EventState.NOT_ACTIVE)
				{
					handleAfk(iFPlayer, false);
				}
				else if (isAfk)
				{
					handleAfk(iFPlayer, true);
				}
			}

			if (getState() != EventState.NOT_ACTIVE)
			{
				ThreadPoolManager.getInstance().schedule(this, 1000L);
			}
			else
			{
				for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
				{
					if (iFPlayer.isAfk())
					{
						AbstractFightClub.this.handleAfk(iFPlayer, false);
					}
				}
			}
		}
	}

	private class LeftZoneThread implements Runnable
	{
		@Override
		public void run()
		{
			final List<FightClubPlayer> toDelete = new ArrayList<FightClubPlayer>();
			for (Entry<FightClubPlayer, Zone> entry : _leftZone.entrySet())
			{
				Player player = entry.getKey().getPlayer();
				if (player == null || !player.isOnline() || _state == EventState.NOT_ACTIVE || entry.getValue().checkIfInZone(player) || player.isDead() || player.isTeleporting())
				{
					toDelete.add(entry.getKey());
					continue;
				}

				int power = (int) Math.max(400, entry.getValue().findDistanceToZone(player, true) - 4000);

				player.sendPacket(new EarthQuakePacket(player.getLoc(), power, 5));
				player.sendPacket(new SayPacket2(0, ChatType.COMMANDCHANNEL_ALL, 0, "Error", "Go Back To Event Zone!"));
				entry.getKey().increaseSecondsOutsideZone();

				if (entry.getKey().getSecondsOutsideZone() >= TIME_MAX_SECONDS_OUTSIDE_ZONE)
				{
					player.doDie(null);
					toDelete.add(entry.getKey());
					entry.getKey().clearSecondsOutsideZone();
				}
			}

			for (FightClubPlayer playerToDelete : toDelete)
			{
				if (playerToDelete != null)
				{
					_leftZone.remove(playerToDelete);
					playerToDelete.clearSecondsOutsideZone();
				}
			}

			if (_state != EventState.NOT_ACTIVE)
			{
				ThreadPoolManager.getInstance().schedule(this, 1000L);
			}
		}
	}

	private class TimeSpentOnEventThread implements Runnable
	{
		@Override
		public void run()
		{
			if (_state == EventState.STARTED)
			{
				for (final FightClubPlayer iFPlayer : getPlayers(FIGHTING_PLAYERS))
				{
					if (iFPlayer.getPlayer() == null || !iFPlayer.getPlayer().isOnline() || iFPlayer.isAfk())
					{
						continue;
					}
					iFPlayer.incSecondsSpentOnEvent(10);
				}
			}

			if (_state != EventState.NOT_ACTIVE)
			{
				ThreadPoolManager.getInstance().schedule(new TimeSpentOnEventThread(), 10000L);
			}
		}
	}

	private class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature actor)
		{
			if (actor.isPlayer())
			{
				FightClubPlayer fPlayer = getFightClubPlayer(actor);
				if (fPlayer != null)
				{
					actor.sendPacket(new EarthQuakePacket(actor.getLoc(), 0, 1));
					_leftZone.remove(getFightClubPlayer(actor));
				}
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature actor)
		{
			if (actor.isPlayer() && _state != EventState.NOT_ACTIVE)
			{
				FightClubPlayer fPlayer = getFightClubPlayer(actor);
				if (fPlayer != null)
				{
					_leftZone.put(getFightClubPlayer(actor), zone);
				}
			}
		}
	}

	private class ExitListener implements OnPlayerExitListener
	{
		@Override
		public void onPlayerExit(Player player)
		{
			loggedOut(player);
		}
	}

	protected boolean isPlayerActive(Player player)
	{
		if (player == null)
		{
			return false;
		}
		if (player.isDead())
		{
			return false;
		}
		if (!player.getReflection().equals(getReflection()))
		{
			return false;
		}
		if (System.currentTimeMillis() - player.getLastNotAfkTime() > 120000L)
		{
			return false;
		}
		boolean insideZone = false;
		for (Zone zone : getReflection().getZones())
		{
			if (zone.checkIfInZone(player.getX(), player.getY(), player.getZ(), player.getReflection()))
			{
				insideZone = true;
			}
		}
		if (!insideZone)
		{
			return false;
		}
		return true;
	}

	private FightClubPlayer[] getTopKillers()
	{
		if ((!_teamed) || (topKillerReward == 0))
		{
			return null;
		}
		FightClubPlayer[] topKillers = new FightClubPlayer[_teams.size()];
		int[] topKillersKills = new int[_teams.size()];

		int teamIndex = 0;
		for (FightClubTeam team : _teams)
		{
			for (FightClubPlayer fPlayer : team.getPlayers())
			{
				if (fPlayer != null)
				{
					if (fPlayer.getKills(true) == topKillersKills[teamIndex])
					{
						topKillers[teamIndex] = null;
					}
					else if (fPlayer.getKills(true) > topKillersKills[teamIndex])
					{
						topKillers[teamIndex] = fPlayer;
						topKillersKills[teamIndex] = fPlayer.getKills(true);
					}
				}
			}
			teamIndex++;
		}
		return topKillers;
	}

	protected boolean isAfkTimerStopped(Player player)
	{
		return (player.isDead()) && (!_ressAllowed) && (_respawnTime <= 0);
	}

	public boolean canStandUp(Player player)
	{
		return true;
	}
}