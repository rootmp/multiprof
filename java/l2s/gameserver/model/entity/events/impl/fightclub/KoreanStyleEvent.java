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
package l2s.gameserver.model.entity.events.impl.fightclub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubPlayer;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubTeam;
import l2s.gameserver.model.entity.events.fightclubmanager.enums.EventState;
import l2s.gameserver.model.entity.events.fightclubmanager.enums.MessageType;
import l2s.gameserver.model.entity.events.impl.AbstractFightClub;
import l2s.gameserver.network.l2.s2c.SkillCoolTimePacket;
import l2s.gameserver.skills.enums.AbnormalEffect;

/**
 * @author Hl4p3x for L2Scripts Essence
 */
public class KoreanStyleEvent extends AbstractFightClub
{
	private static final long MAX_FIGHT_TIME = 90000L;
	private final FightClubPlayer[] _fightingPlayers;
	private final int[] _lastTeamChosenSpawn;
	private long _lastKill;
	private ScheduledFuture<?> _fightTask;

	public KoreanStyleEvent(MultiValueSet<String> set)
	{
		super(set);
		_lastKill = 0L;
		_fightingPlayers = new FightClubPlayer[2];
		_lastTeamChosenSpawn = new int[]
		{
			0,
			0
		};
	}

	public void onKilled(Creature actor, Creature victim)
	{
		if ((actor != null) && (actor.isPlayable()))
		{
			FightClubPlayer realActor = getFightClubPlayer(actor.getPlayer());
			if ((victim.isPlayer()) && (realActor != null))
			{
				realActor.increaseKills(true);
				updatePlayerScore(realActor);
				updateScreenScores();
				sendMessageToPlayer(realActor, MessageType.GM, "You have killed " + victim.getName());
			}
			actor.getPlayer().sendUserInfo();
		}

		if (victim.isPlayer())
		{
			FightClubPlayer realVictim = getFightClubPlayer(victim);
			realVictim.increaseDeaths();
			if (actor != null)
				sendMessageToPlayer(realVictim, MessageType.GM, "You have been killed by " + actor.getName());
			victim.broadcastCharInfo();

			_lastKill = System.currentTimeMillis();
		}
		checkFightingPlayers();
		super.onKilled(actor, victim);
	}

	public void loggedOut(Player player)
	{
		super.loggedOut(player);
		for (FightClubPlayer fPlayer : _fightingPlayers)
		{
			if ((fPlayer != null) && (fPlayer.getPlayer() != null) && (fPlayer.getPlayer().equals(player)))
			{
				checkFightingPlayers();
			}
		}
	}

	@Override
	public void stopEvent(boolean force)
	{
		for (int i = 0; i < _fightingPlayers.length; i++)
			_fightingPlayers[i] = null;
		super.stopEvent(force);
	}

	public boolean leaveEvent(Player player, boolean teleportTown)
	{
		super.leaveEvent(player, teleportTown);
		try
		{
			if (player.isImmobilized())
			{
				player.stopRooted();
			}
		}
		catch (IllegalStateException e)
		{
		}

		player.stopAbnormalEffect(AbnormalEffect.ROOT);
		if (getState() != EventState.STARTED)
			return true;
		for (FightClubPlayer fPlayer : _fightingPlayers)
		{
			if ((fPlayer != null) && (fPlayer.getPlayer() != null) && (fPlayer.getPlayer().equals(player)))
				checkFightingPlayers();
		}
		return true;
	}

	public void startEvent()
	{
		super.startEvent();
		for (final FightClubPlayer fPlayer : getPlayers(FIGHTING_PLAYERS, REGISTERED_PLAYERS))
		{
			final Player player = fPlayer.getPlayer();
			if (player.isDead())
			{
				player.doRevive();
			}
			if (player.isFakeDeath())
			{
				player.setFakeDeath(false);
			}
			player.sitDown(null);
			player.resetReuse();
			player.sendSkillList();
			if (player.getSummon() != null)
			{
				player.getSummon().startAbnormalEffect(AbnormalEffect.ROOT);
			}
		}

		_lastKill = System.currentTimeMillis();
	}

	public void startRound()
	{
		super.startRound();
		checkFightingPlayers();
		if (_fightTask != null)
		{
			_fightTask.cancel(false);
			_fightTask = null;
		}
		_fightTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new CheckFightersInactive(this), 1000, 5000L);
	}

	public void endRound()
	{
		super.endRound();
		super.unrootPlayers();
		if (_fightTask != null)
		{
			_fightTask.cancel(false);
			_fightTask = null;
		}
	}

	private void checkFightingPlayers()
	{
		if ((getState() == EventState.OVER) || (getState() == EventState.NOT_ACTIVE))
		{
			return;
		}
		boolean changed = false;
		for (int i = 0; i < _fightingPlayers.length; i++)
		{
			final FightClubPlayer oldPlayer = _fightingPlayers[i];
			if ((oldPlayer == null) || (!isPlayerActive(oldPlayer.getPlayer())) || (getFightClubPlayer(oldPlayer.getPlayer()) == null))
			{
				if ((oldPlayer != null) && (!oldPlayer.getPlayer().isDead()))
				{
					oldPlayer.getPlayer().doDie(null);
					return;
				}
				FightClubPlayer newPlayer = chooseNewPlayer(i + 1);
				if (newPlayer == null)
				{
					for (FightClubTeam team : getTeams())
					{
						if (team.getIndex() != i + 1)
						{
							team.incScore(1);
						}
					}
					endRound();
					return;
				}
				newPlayer.getPlayer().isntAfk();
				_fightingPlayers[i] = newPlayer;
				changed = true;
			}
		}

		if (changed)
		{
			StringBuilder msg = new StringBuilder();
			for (int i = 0; i < _fightingPlayers.length; i++)
			{
				if (i > 0)
				{
					msg.append(" VS ");
				}
				msg.append(_fightingPlayers[i].getPlayer().getName());
			}
			sendMessageToFighting(MessageType.SCREEN_BIG, msg.toString(), false);
			preparePlayers();
		}
	}

	private FightClubPlayer chooseNewPlayer(int teamIndex)
	{
		final List<FightClubPlayer> alivePlayersFromTeam = new java.util.ArrayList<FightClubPlayer>();
		for (final FightClubPlayer fPlayer : getPlayers(FIGHTING_PLAYERS))
		{
			if ((fPlayer.getPlayer().isSitting()) && (fPlayer.getTeam().getIndex() == teamIndex))
			{
				alivePlayersFromTeam.add(fPlayer);
			}
		}

		if (alivePlayersFromTeam.isEmpty())
		{
			return null;
		}
		if (alivePlayersFromTeam.size() == 1)
		{
			return alivePlayersFromTeam.get(0);
		}
		return Rnd.get(alivePlayersFromTeam);
	}

	private void preparePlayers()
	{
		for (int i = 0; i < _fightingPlayers.length; i++)
		{
			final FightClubPlayer fPlayer = _fightingPlayers[i];
			final Player player = fPlayer.getPlayer();

			if (player.isBlocked())
			{
				player.unblock();
			}
			if (player.isImmobilized())
			{
				player.stopRooted();
			}
			player.stopAbnormalEffect(AbnormalEffect.ROOT);
			player.standUp();
			player.isntAfk();
			player.resetReuse();
			player.sendPacket(new SkillCoolTimePacket(player));
			healFull(player);
			if ((player.getAnyServitor() != null) && (!player.getAnyServitor().isDead()))
			{
				healFull(player.getAnyServitor());
			}
			Location loc = getMap().getKeyLocations()[i];
			player.teleToLocation(loc, getReflection());
		}
	}

	private static void healFull(Playable playable)
	{
		if (!playable.isDead())
		{
			cleanse(playable);
			playable.setCurrentHp(playable.getMaxHp(), false);
			playable.setCurrentMp(playable.getMaxMp());
			playable.setCurrentCp(playable.getMaxCp());
		}
		else if (playable.isPlayer())
		{
			ressurectPlayer(playable.getPlayer());
		}
	}

	private static void cleanse(Playable playable)
	{
		try
		{
			List<Abnormal> buffList = new ArrayList<>();
			for (Abnormal value : playable.getAbnormalList().values())
			{
				if (value.isOffensive() && (value.isCancelable()))
				{
					buffList.add(value);
				}
			}
			for (Abnormal effects : buffList)
			{
				effects.exit();
			}
		}
		catch (IllegalStateException illegalStateException)
		{
		}
	}

	public boolean canAttack(Creature target, Creature attacker, Skill skill, boolean force)
	{
		if (_preparing)
		{
			attacker.setTarget(attacker);
			return false;
		}
		if (getState() != EventState.STARTED)
			return false;
		if ((target == null) || (!target.isPlayable()) || (attacker == null) || (!attacker.isPlayable()))
			return false;
		if ((isFighting(target)) && (isFighting(attacker)))
			return true;
		return false;
	}

	private boolean isFighting(Creature actor)
	{
		for (FightClubPlayer fPlayer : _fightingPlayers)
		{
			if ((fPlayer != null) && (fPlayer.getPlayer() != null) && (fPlayer.getPlayer().equals(actor.getPlayer())))
				return true;
		}
		return false;
	}

	protected static class CheckFightersInactive implements Runnable
	{
		private final KoreanStyleEvent _fightClub;

		public CheckFightersInactive(KoreanStyleEvent fightClub)
		{
			_fightClub = fightClub;
		}

		public void run()
		{
			if (_fightClub.getState() != EventState.STARTED)
			{
				return;
			}
			if (_fightClub._lastKill + MAX_FIGHT_TIME < System.currentTimeMillis())
			{
				double playerToKillHp = Double.MAX_VALUE;
				Player playerToKill = null;
				for (FightClubPlayer fPlayer : _fightClub._fightingPlayers)
				{
					if ((fPlayer != null) && (fPlayer.getPlayer() != null))
					{
						if (!fPlayer.getPlayer().getNetConnection().isConnected())
						{
							playerToKill = fPlayer.getPlayer();
							playerToKillHp = -100.0D;
						}
						else if (System.currentTimeMillis() - fPlayer.getPlayer().getLastNotAfkTime() > 8000L)
						{
							playerToKill = fPlayer.getPlayer();
							playerToKillHp = -1.0D;
						}
						else if (fPlayer.getPlayer().getCurrentHpPercents() < playerToKillHp)
						{
							playerToKill = fPlayer.getPlayer();
							playerToKillHp = fPlayer.getPlayer().getCurrentHpPercents();
						}
					}
				}

				if (playerToKill != null)
				{
					playerToKill.doDie(null);
				}
			}
			ThreadPoolManager.getInstance().schedule(this, 5000L);
		}
	}

	protected Location getSinglePlayerSpawnLocation(FightClubPlayer fPlayer)
	{
		Location[] spawnLocations = getMap().getTeamSpawns().get(fPlayer.getTeam().getIndex());
		int ordinalTeamIndex = fPlayer.getTeam().getIndex() - 1;
		int lastSpawnIndex = _lastTeamChosenSpawn[ordinalTeamIndex];
		lastSpawnIndex++;
		if (lastSpawnIndex >= spawnLocations.length)
			lastSpawnIndex = 0;
		_lastTeamChosenSpawn[ordinalTeamIndex] = lastSpawnIndex;
		return spawnLocations[lastSpawnIndex];
	}

	protected int getRewardForWinningTeam(FightClubPlayer fPlayer, boolean atLeast1Kill)
	{
		return super.getRewardForWinningTeam(fPlayer, false);
	}

	protected void handleAfk(FightClubPlayer fPlayer, boolean setAsAfk)
	{
	}

	protected void unrootPlayers()
	{
	}

	protected boolean inScreenShowBeScoreNotKills()
	{
		return false;
	}

	protected boolean inScreenShowBeTeamNotInvidual()
	{
		return false;
	}

	protected boolean isAfkTimerStopped(Player player)
	{
		return (player.isSitting()) || (super.isAfkTimerStopped(player));
	}

	public boolean canStandUp(Player player)
	{
		for (FightClubPlayer fPlayer : _fightingPlayers)
		{
			if ((fPlayer != null) && (fPlayer.getPlayer().equals(player)))
				return true;
		}
		return false;
	}

	protected List<List<Player>> spreadTeamInPartys(FightClubTeam team)
	{
		return java.util.Collections.emptyList();
	}

	protected void createParty(List<Player> listOfPlayers)
	{
		//
	}
}
