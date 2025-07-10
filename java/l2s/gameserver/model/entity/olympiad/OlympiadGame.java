package l2s.gameserver.model.entity.olympiad;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.util.Rnd;
import l2s.gameserver.data.xml.holder.InstantZoneHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.OlympiadHistoryManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.ObservableArena;
import l2s.gameserver.model.ObservePoint;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.base.RestartType;
import l2s.gameserver.model.base.TeamType;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.entity.events.EventType;
import l2s.gameserver.model.entity.events.impl.SingleMatchEvent;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.AbnormalStatusUpdatePacket;
import l2s.gameserver.network.l2.s2c.ExOlympiadMatchEndPacket;
import l2s.gameserver.network.l2.s2c.ExOlympiadModePacket;
import l2s.gameserver.network.l2.s2c.ExOlympiadSpelledInfoPacket;
import l2s.gameserver.network.l2.s2c.ExOlympiadUserInfoPacket;
import l2s.gameserver.network.l2.s2c.ExReceiveOlympiadPacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.templates.InstantZone;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.utils.Functions;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.Log;
import l2s.gameserver.utils.PlayerUtils;

public class OlympiadGame extends ObservableArena
{
	private class OlympiadGameEvent extends SingleMatchEvent
	{
		public OlympiadGameEvent(MultiValueSet<String> set)
		{
			super(set);
		}

		@Override
		public void reCalcNextTime(boolean onInit)
		{
			//
		}

		@Override
		public EventType getType()
		{
			return EventType.PVP_EVENT;
		}

		@Override
		public boolean isInProgress()
		{
			return super.isInProgress();
		}

		@Override
		public Reflection getReflection()
		{
			return OlympiadGame.this.getReflection();
		}

		@Override
		protected long startTimeMillis()
		{
			return startTime;
		}

		@Override
		public void checkRestartLocs(Player player, Map<RestartType, Boolean> r)
		{
			r.clear();
		}

		@Override
		public boolean canResurrect(Creature active, Creature target, boolean force, boolean quiet)
		{
			return false;
		}

		@Override
		public Boolean isInvisible(Creature creature, GameObject observer)
		{
			if (OlympiadGame.this.getType() == CompType.TEAM)
			{
				Player player = creature.getPlayer();
				if (player != null)
				{
					return isDead(player);
				}
			}
			return null;
		}

		@Override
		public SystemMsg checkForAttack(Creature target, Creature attacker, Skill skill, boolean force)
		{
			if (!isEnemy(target, attacker))
				return SystemMsg.INVALID_TARGET;
			return null;
		}

		@Override
		public boolean canAttack(Creature target, Creature attacker, Skill skill, boolean force, boolean nextAttackCheck)
		{
			return isEnemy(target, attacker);
		}

		@Override
		public SystemMsg canUseItem(Player player, ItemInstance item)
		{
			if (!isRegistered(player) || isDead(player))
			{
				return SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS;
			}
			return super.canUseItem(player, item);
		}

		@Override
		public boolean canUseSkill(Creature caster, Creature target, Skill skill, boolean sendMsg)
		{
			Player player = caster.getPlayer();
			player.sendMessage("canUseSkill #1");
			if (!isRegistered(player) || isDead(player))
			{
				player.sendMessage("canUseSkill #2");
				if (sendMsg)
					caster.sendPacket(new SystemMessagePacket(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(skill));
				return false;
			}
			player.sendMessage("canUseSkill isDead(player)=" + isDead(player));
			return super.canUseSkill(caster, target, skill, sendMsg);
		}

		@Override
		public boolean canUseTeleport(Player player)
		{
			return false;
		}

		@Override
		public boolean canJoinParty(Player inviter, Player target)
		{
			return false;
		}

		@Override
		public boolean canLeaveParty(Player player)
		{
			return false;
		}

		private boolean isEnemy(Creature target, Creature attacker)
		{
			if (getState() != BATTLE_STATE)
				return false;
			if (OlympiadGame.this.getType() == CompType.TEAM)
			{
				if (target.getTeam() == TeamType.NONE || attacker.getTeam() == TeamType.NONE || target.getTeam() == attacker.getTeam())
					return false;
			}
			Player attackerPlayer = attacker.getPlayer();
			if (attackerPlayer == null)
				return false;
			Player targetPlayer = target.getPlayer();
			if (targetPlayer == null)
				return false;
			if (attackerPlayer.getOlympiadSide() == targetPlayer.getOlympiadSide())
				return false;
			return !isDead(attacker.getPlayer()) && !isDead(target.getPlayer());
		}

		@Override
		public void onEffectIconsUpdate(Player player, Abnormal[] effects)
		{
			if (getState() != BATTLE_STATE)
				return;
			ExOlympiadSpelledInfoPacket olympiadSpelledInfo = new ExOlympiadSpelledInfoPacket();
			for (Abnormal effect : effects)
			{
				if (effect != null)
					effect.addOlympiadSpelledIcon(player, olympiadSpelledInfo);
			}
			SkillEntry passiveSkillEntry = player.getKnownSkill(6040);
			if (passiveSkillEntry != null)
			{
				olympiadSpelledInfo.addSpellRecivedPlayer(player);
				olympiadSpelledInfo.addEffect(passiveSkillEntry.getDisplayId(), passiveSkillEntry.getDisplayLevel(), AbnormalStatusUpdatePacket.INFINITIVE_EFFECT, -1);
			}
			broadcastPacket(olympiadSpelledInfo, true, true);
		}

		@Override
		public void onStatusUpdate(Player player)
		{
			if (getState() != BATTLE_STATE)
				return;
			broadcastInfo(player, null, false);
		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(OlympiadGame.class);

	public static final int NONE_STATE = 0;
	public static final int PREPARE_STATE = 1;
	public static final int BATTLE_STATE = 2;
	public static final int FINISH_STATE = 3;

	public static final int MAX_POINTS_LOOSE = 10;

	private final AtomicBoolean validated = new AtomicBoolean(false);

	private TeamType winner = TeamType.NONE;
	private final AtomicInteger state = new AtomicInteger(NONE_STATE);

	private final int id;
	private final Reflection reflection;
	private final CompType _type;
	private final OlympiadGameEvent event;

	private final OlympiadTeam team1;
	private final OlympiadTeam team2;

	private long startTime;

	public static OlympiadGame makeGame(int id, CompType type, OlympiadParticipiantData[] participants1, OlympiadParticipiantData[] participants2)
	{
		int instantZoneId = type == CompType.TEAM ? Rnd.get(new int[]
		{
			151,
			152,
			153
		}) : Rnd.get(new int[]
		{
			147,
			/* 148, */149
				/* , 150 */ });
		InstantZone instantZone = InstantZoneHolder.getInstance().getInstantZone(instantZoneId);
		if (instantZone == null)
		{
			LOGGER.warn(String.format("Not found instance zone ID[%d] for olympiad game!", instantZoneId));
			return null;
		}
		return new OlympiadGame(id, type, instantZone, participants1, participants2);
	}

	private OlympiadGame(int id, CompType type, InstantZone instantZone, OlympiadParticipiantData[] participants1, OlympiadParticipiantData[] participants2)
	{
		_type = type;
		this.id = id;
		reflection = new Reflection();
		reflection.init(instantZone);

		StatsSet set = new StatsSet();
		set.set("id", getId());
		set.set("name", "OlympiadGame_" + getId());
		event = new OlympiadGameEvent(set);

		team1 = new OlympiadTeam(participants1, this, TeamType.BLUE);
		team2 = new OlympiadTeam(participants2, this, TeamType.RED);

		Log.add("Olympiad System: Game - " + id + ": " + team1.getName() + " vs " + team2.getName(), "olympiad");
	}

	public void addBuffers()
	{
		reflection.spawnByGroup("olympiad_" + reflection.getInstancedZoneId() + "_buffers");
	}

	public void deleteBuffers()
	{
		reflection.despawnByGroup("olympiad_" + reflection.getInstancedZoneId() + "_buffers");
	}

	public void managerShout()
	{
		for (NpcInstance npc : Olympiad.getNpcs())
		{
			NpcString npcString;
			switch (_type)
			{
				case CLASSED:
					npcString = NpcString.OLYMPIAD_CLASS_INDIVIDUAL_MATCH_IS_GOING_TO_BEGIN_IN_ARENA_S1_IN_A_MOMENT;
					break;
				case NON_CLASSED:
					npcString = NpcString.OLYMPIAD_CLASSFREE_INDIVIDUAL_MATCH_IS_GOING_TO_BEGIN_IN_ARENA_S1_IN_A_MOMENT;
					break;
				default:
					continue;
			}
			Functions.npcShout(npc, npcString, String.valueOf(id + 1));
		}
	}

	public void portPlayersToArena()
	{
		team1.portPlayersToArena();
		team2.portPlayersToArena();
	}

	public void preparePlayers1()
	{
		team1.preparePlayers1();
		team2.preparePlayers1();
	}

	public void preparePlayers2()
	{
		team1.preparePlayers2();
		team2.preparePlayers2();
	}

	public void portPlayersBack()
	{
		team1.portPlayersBack();
		team2.portPlayersBack();
	}

	public boolean validatePlayers()
	{
		for (OlympiadMember member : team1.getMembers())
		{
			boolean valid = true;
			Player player = member.getPlayer();
			for (Player p : getAllPlayers(false))
			{
				if (p != player)
				{
					if (!Olympiad.validPlayer(p, player, getType(), true))
						valid = false;
				}
			}
			if (!valid)
				return false;
		}
		for (OlympiadMember member : team2.getMembers())
		{
			boolean valid = true;
			Player player = member.getPlayer();
			for (Player p : getAllPlayers(false))
			{
				if (p != player)
				{
					if (!Olympiad.validPlayer(p, player, getType(), true))
						valid = false;
				}
			}
			if (!valid)
				return false;
		}
		return true;
	}

	public void collapse()
	{
		portPlayersBack();
		clearObservers();
		reflection.collapse();
	}

	public void validateWinner(boolean aborted)
	{
		int prevState = getState();

		state.set(FINISH_STATE);

		if (!validated.compareAndSet(false, true))
		{
			Log.add("Olympiad Result: (" + team1.getName() + ") vs (" + team2.getName() + ") ... double validate check!!!", "olympiad");
			return;
		}

		// Если игра закончилась до телепортации на стадион, то забираем очки у вышедших
		// из игры, не засчитывая никому победу
		if (prevState < 1 && aborted)
		{
			broadcastPacket(SystemMsg.YOUR_OPPONENT_MADE_HASTE_WITH_THEIR_TAIL_BETWEEN_THEIR_LEGS_THE_MATCH_HAS_BEEN_CANCELLED, true, false);
			return;
		}

		boolean member1Check = team1.checkPlayers();
		boolean member2Check = team2.checkPlayers();

		if (winner == TeamType.NONE)
		{
			if (!member1Check && !member2Check)
				winner = TeamType.NONE;
			else if (!member2Check)
				winner = TeamType.BLUE; // Выиграла первая команда
			else if (!member1Check)
				winner = TeamType.RED; // Выиграла вторая команда
			else if (team1.getDamage() < team2.getDamage()) // Вторая команда нанесла вреда меньше, чем первая
				winner = TeamType.BLUE; // Выиграла первая команда
			else if (team1.getDamage() > team2.getDamage()) // Вторая команда нанесла вреда больше, чем первая
				winner = TeamType.RED; // Выиграла вторая команда
		}

		winGame();

		team1.saveParticipantsData();
		team2.saveParticipantsData();

		broadcastRelation();
	}

	private void winGame()
	{
		OlympiadTeam winnerTeam;
		OlympiadTeam looseTeam;
		if (winner == TeamType.BLUE)
		{
			winnerTeam = team1;
			looseTeam = team2;
		}
		else if (winner == TeamType.RED)
		{
			winnerTeam = team2;
			looseTeam = team1;
		}
		else
		{
			tie();
			return;
		}

		OlympiadMember winnerTopDamagaer = winnerTeam.getTopDamager();
		ExReceiveOlympiadPacket.MatchResult packet = new ExReceiveOlympiadPacket.MatchResult(winner, getType(), winnerTopDamagaer.getName());

		winnerTeam.getMembers().forEach(OlympiadMember::incGameCount);
		looseTeam.getMembers().forEach(OlympiadMember::incGameCount);

		int winnerPoints = getType().getWinnerPoints();
		winnerTeam.getMembers().forEach(m ->
		{
			OlympiadParticipiantData data = m.getStat();
			data.setPoints(data.getPoints() + winnerPoints);
			packet.addPlayer(m, winnerPoints, getId());
		});

		int loosePoints = getType().getLoosePoints();
		looseTeam.getMembers().forEach(m ->
		{
			OlympiadParticipiantData data = m.getStat();
			data.setPoints(data.getPoints() + loosePoints);
			packet.addPlayer(m, loosePoints, getId());
		});

		int diff = (int) ((System.currentTimeMillis() - startTime) / 1000L);

		team1.getMembers().forEach(m ->
		{
			OlympiadMember topEnemy = team2.getTopDamager();
			OlympiadHistory h = new OlympiadHistory(m.getObjectId(), topEnemy.getObjectId(), m.getClassId(), topEnemy.getClassId(), m.getName(), topEnemy.getName(), startTime, diff, winner.ordinal(), getType().ordinal());
			OlympiadHistoryManager.getInstance().saveHistory(h);
		});

		team2.getMembers().forEach(m ->
		{
			OlympiadMember topEnemy = team1.getTopDamager();
			OlympiadHistory h = new OlympiadHistory(m.getObjectId(), topEnemy.getObjectId(), m.getClassId(), topEnemy.getClassId(), m.getName(), topEnemy.getName(), startTime, diff, winner.ordinal(), getType().ordinal());
			OlympiadHistoryManager.getInstance().saveHistory(h);
		});

		for (OlympiadMember member : winnerTeam.getMembers())
		{
			Player player = member.getPlayer();
			if (player != null)
			{
				if (getType().getRewardId() != 0 && getType().getWinnerReward() > 0)
				{
					if (getType() == CompType.TEAM)
					{
						player.sendPacket(SystemMsg.YOUVE_RECEIVED_THE_REWARD_FOR_WINNING_A_3_VS_3_OLYMPIAD_MATCH);
					}
					ItemFunctions.addItem(player, getType().getRewardId(), getType().getWinnerReward());
				}
				// TODO: OlympiadRankingManager.getInstance().updateRank(player);
			}
		}

		for (OlympiadMember member : looseTeam.getMembers())
		{
			Player player = member.getPlayer();
			if (player != null)
			{
				if (getType().getRewardId() != 0 && getType().getLooserReward() > 0)
				{
					if (getType() == CompType.TEAM)
					{
						player.sendPacket(SystemMsg.YOUVE_RECEIVED_THE_CONSOLATION_PRIZE_FOR_PARTICIPATING_IN_THE_3_VS_3_OLYMPIAD);
					}
					ItemFunctions.addItem(player, getType().getRewardId(), getType().getLooserReward());
				}
				// TODO: OlympiadRankingManager.getInstance().updateRank(player);
			}
		}

		for (OlympiadTeam team : getAllTeams())
		{
			for (OlympiadMember member : team.getMembers())
			{
				Player player = member.getPlayer();
				if (player != null)
				{
					player.getListeners().onOlympiadFinishBattle(winnerTeam == team);
					for (QuestState qs : player.getAllQuestsStates())
					{
						if (qs.isStarted())
							qs.getQuest().onOlympiadEnd(this, qs);
					}
				}
			}
		}

		broadcastPacket(packet, true, false);

		// FIXME [VISTALL] неверная мессага?
		broadcastPacket(new SystemMessagePacket(SystemMsg.CONGRATULATIONS_C1_YOU_WIN_THE_MATCH).addString(winnerTopDamagaer.getName()), false, true);

		// FIXME [VISTALL] нужно ли?
		// broadcastPacket(new
		// SystemMessagePacket(SystemMsg.C1_HAS_EARNED_S2_POINTS_IN_THE_GRAND_OLYMPIAD_GAMES).addString(winnerMember.getName()).addInteger(pointDiff),
		// true, false);
		// broadcastPacket(new
		// SystemMessagePacket(SystemMsg.C1_HAS_LOST_S2_POINTS_IN_THE_GRAND_OLYMPIAD_GAMES).addString(looseMember.getName()).addInteger(pointDiff),
		// true, false);

		Log.add("Olympiad Result: (" + winnerTeam.getName() + ") vs (" + looseTeam.getName() + ") ... (" + (int) winnerTeam.getDamage() + " vs " + (int) looseTeam.getDamage() + ") " + winnerTeam.getName() + " win " + winnerPoints + " points", "olympiad");
	}

	private void tie()
	{
		ExReceiveOlympiadPacket.MatchResult packet = new ExReceiveOlympiadPacket.MatchResult(TeamType.NONE, getType(), StringUtils.EMPTY);

		for (OlympiadMember member : team1.getMembers())
		{
			member.incGameCount();
			int points = getType().getTiePoints();
			packet.addPlayer(member, points, getId());
			OlympiadParticipiantData data = member.getStat();
			data.setPoints(data.getPoints() + points);
		}

		for (OlympiadMember member : team2.getMembers())
		{
			member.incGameCount();
			int points = getType().getTiePoints();
			packet.addPlayer(member, points, getId());
			OlympiadParticipiantData data = member.getStat();
			data.setPoints(data.getPoints() + points);
		}

		int diff = (int) ((System.currentTimeMillis() - startTime) / 1000L);

		team1.getMembers().forEach(m ->
		{
			OlympiadMember topEnemy = team2.getTopDamager();
			OlympiadHistory h = new OlympiadHistory(m.getObjectId(), topEnemy.getObjectId(), m.getClassId(), topEnemy.getClassId(), m.getName(), topEnemy.getName(), startTime, diff, 0, getType().ordinal());
			OlympiadHistoryManager.getInstance().saveHistory(h);
		});

		team2.getMembers().forEach(m ->
		{
			OlympiadMember topEnemy = team1.getTopDamager();
			OlympiadHistory h = new OlympiadHistory(m.getObjectId(), topEnemy.getObjectId(), m.getClassId(), topEnemy.getClassId(), m.getName(), topEnemy.getName(), startTime, diff, 0, getType().ordinal());
			OlympiadHistoryManager.getInstance().saveHistory(h);
		});

		for (Player player : getAllPlayers(false))
		{
			player.getListeners().onOlympiadFinishBattle(false);

			for (QuestState qs : player.getAllQuestsStates())
			{
				if (qs.isStarted())
					qs.getQuest().onOlympiadEnd(this, qs);
			}
		}

		broadcastPacket(SystemMsg.THERE_IS_NO_VICTOR_THE_MATCH_ENDS_IN_A_TIE, false, true);
		broadcastPacket(packet, true, false);

		Log.add("Olympiad Result: (" + team1.getName() + ") vs (" + team2.getName() + ") ... tie", "olympiad");
	}

	public void openDoors()
	{
		if (getType() != CompType.TEAM)
		{
			for (DoorInstance door : reflection.getDoors())
				door.openMe();
		}
	}

	public int getId()
	{
		return id;
	}

	@Override
	public Reflection getReflection()
	{
		return reflection;
	}

	@Override
	public Location getObserverEnterPoint(Player player)
	{
		List<Location> spawns = getReflection().getInstancedZone().getTeleportCoords();
		if (spawns.size() < 3)
		{
			Location c1 = spawns.get(0);
			Location c2 = spawns.get(1);
			return new Location((c1.x + c2.x) / 2, (c1.y + c2.y) / 2, (c1.z + c2.z) / 2);
		}
		else
			return spawns.get(2);
	}

	@Override
	public boolean showObservableArenasList(Player player)
	{
		if (!Olympiad.inCompPeriod() || Olympiad.isOlympiadEnd())
		{
			player.sendPacket(SystemMsg.THE_GRAND_OLYMPIAD_GAMES_ARE_NOT_CURRENTLY_IN_PROGRESS);
			return false;
		}
		player.sendPacket(new ExReceiveOlympiadPacket.MatchList());
		return true;
	}

	@Override
	public void onAppearObserver(ObservePoint observer)
	{
		broadcastInfo(null, observer.getPlayer(), true);
	}

	@Override
	public void onEnterObserverArena(Player player)
	{
		player.sendPacket(new ExOlympiadModePacket(3));
	}

	@Override
	public void onChangeObserverArena(Player player)
	{
		player.sendPacket(ExOlympiadMatchEndPacket.STATIC);
	}

	@Override
	public void onExitObserverArena(Player player)
	{
		player.sendPacket(new ExOlympiadModePacket(0));
		player.sendPacket(ExOlympiadMatchEndPacket.STATIC);
	}

	public boolean isRegistered(int objId)
	{
		return team1.isMember(objId) || team2.isMember(objId);
	}

	public boolean isRegistered(Player player)
	{
		if (player == null)
			return false;
		return isRegistered(player.getObjectId());
	}

	public void broadcastInfo(Player sender, Player receiver, boolean onlyToObservers)
	{
		// TODO заюзать пакеты:
		// ExEventMatchCreate
		// ExEventMatchFirecracker
		// ExEventMatchManage
		// ExEventMatchMessage
		// ExEventMatchObserver
		// ExEventMatchScore
		// ExEventMatchTeamInfo
		// ExEventMatchTeamUnlocked
		// ExEventMatchUserInfo

		if (sender != null)
			if (receiver != null)
				receiver.sendPacket(new ExOlympiadUserInfoPacket(sender, sender.getOlympiadSide()));
			else
				broadcastPacket(new ExOlympiadUserInfoPacket(sender, sender.getOlympiadSide()), !onlyToObservers, true);
		else
		{
			// Рассылаем информацию о первой команде
			for (OlympiadMember member : team1.getMembers())
			{
				Player player = member.getPlayer();
				if (player != null)
				{
					if (receiver != null)
						receiver.sendPacket(new ExOlympiadUserInfoPacket(player, player.getOlympiadSide()));
					else
					{
						broadcastPacket(new ExOlympiadUserInfoPacket(player, player.getOlympiadSide()), !onlyToObservers, true);
						PlayerUtils.updateAttackableFlags(player);
					}
				}
			}

			// Рассылаем информацию о второй команде
			for (OlympiadMember member : team2.getMembers())
			{
				Player player = member.getPlayer();
				if (player != null)
				{
					if (receiver != null)
						receiver.sendPacket(new ExOlympiadUserInfoPacket(player, player.getOlympiadSide()));
					else
					{
						broadcastPacket(new ExOlympiadUserInfoPacket(player, player.getOlympiadSide()), !onlyToObservers, true);
						PlayerUtils.updateAttackableFlags(player);
					}
				}
			}
		}
	}

	public void broadcastRelation()
	{
		for (Player player : getAllPlayers(false))
		{
			PlayerUtils.updateAttackableFlags(player);
		}
	}

	public void broadcastPacket(IBroadcastPacket packet, boolean toTeams, boolean toObservers)
	{
		if (toTeams)
		{
			for (Player player : getAllPlayers(false))
			{
				player.sendPacket(packet);
			}
		}

		if (toObservers)
		{
			for (ObservePoint observer : getObservers())
				observer.sendPacket(packet);
		}
	}

	public List<Player> getAllPlayers(boolean withObservers)
	{
		List<Player> result = new ArrayList<>();
		for (OlympiadMember member : team1.getMembers())
		{
			Player player = member.getPlayer();
			if (player != null)
				result.add(player);
		}
		for (OlympiadMember member : team2.getMembers())
		{
			Player player = member.getPlayer();
			if (player != null)
				result.add(player);
		}
		if (withObservers)
		{
			for (ObservePoint observer : getObservers())
			{
				Player player = observer.getPlayer();
				if (player != null)
					result.add(player);
			}
		}
		return result;
	}

	public void setWinner(int val)
	{
		winner = TeamType.values()[val];
	}

	public OlympiadTeam getWinnerTeam()
	{
		if (winner == TeamType.BLUE) // Выиграла первая команда
			return team1;
		else if (winner == TeamType.RED) // Выиграла вторая команда
			return team2;
		return null;
	}

	public OlympiadTeam[] getAllTeams()
	{
		return new OlympiadTeam[]
		{
			team1,
			team2
		};
	}

	public void setState(int value)
	{
		state.set(value);
		if (value == 1)
			startTime = System.currentTimeMillis();
	}

	public int getState()
	{
		return state.get();
	}

	public void addDealedDamage(Player player, double damage)
	{
		if (player.getOlympiadSide() == 1)
			team1.addDealedDamage(player, damage);
		else if (player.getOlympiadSide() == 2)
			team2.addDealedDamage(player, damage);
	}

	public boolean checkPlayersOnline()
	{
		return team1.checkPlayers() && team2.checkPlayers();
	}

	public void logoutPlayer(Player player)
	{
		boolean result = false;
		if (player != null)
		{
			if (player.getOlympiadSide() == 1)
				result = team1.logout(player);
			else if (player.getOlympiadSide() == 2)
				result = team2.logout(player);
		}
		if (result && !isValidated())
		{
			endGame(20, true);
		}
	}

	OlympiadGameTask _task;
	ScheduledFuture<?> _shedule;

	public void doDie(Player player)
	{
		boolean result = false;
		if (player != null)
		{
			if (player.getOlympiadSide() == 1)
				result = team1.doDie(player);
			else if (player.getOlympiadSide() == 2)
				result = team2.doDie(player);
		}
		if (result)
		{
			setWinner(player.getOlympiadSide() == 1 ? 2 : 1);
			endGame(20, false);
		}
	}

	public boolean isDead(Player player)
	{
		if (player == null)
			return false;
		if (player.getOlympiadSide() == 1)
		{
			return team1.isDead(player);
		}
		else if (player.getOlympiadSide() == 2)
		{
			return team2.isDead(player);
		}
		return false;
	}

	public synchronized void sheduleTask(OlympiadGameTask task)
	{
		if (_shedule != null)
			_shedule.cancel(false);
		_task = task;
		_shedule = task.shedule();
	}

	public OlympiadGameTask getTask()
	{
		return _task;
	}

	public BattleStatus getStatus()
	{
		if (_task != null)
			return _task.getStatus();
		return BattleStatus.Begining;
	}

	public void endGame(long time, boolean aborted)
	{
		try
		{
			validateWinner(aborted);
		}
		catch (Exception e)
		{
			LOGGER.error("", e);
		}

		sheduleTask(new OlympiadGameTask(this, BattleStatus.Ending, 0, time * 1000));
	}

	public CompType getType()
	{
		return _type;
	}

	public String getTeamName1()
	{
		return team1.getName();
	}

	public String getTeamName2()
	{
		return team2.getName();
	}

	public boolean isValidated()
	{
		return validated.get();
	}

	public OlympiadGameEvent getEvent()
	{
		return event;
	}
}