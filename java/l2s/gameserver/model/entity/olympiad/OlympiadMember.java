package l2s.gameserver.model.entity.olympiad;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.actor.instances.player.Cubic;
import l2s.gameserver.model.base.TeamType;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.entity.events.impl.DuelEvent;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExOlympiadInfo;
import l2s.gameserver.network.l2.s2c.ExOlympiadMatchEndPacket;
import l2s.gameserver.network.l2.s2c.ExOlympiadModePacket;
import l2s.gameserver.network.l2.s2c.ExOlympiadRecord;
import l2s.gameserver.network.l2.s2c.RevivePacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.skills.TimeStamp;
import l2s.gameserver.templates.InstantZone;

public class OlympiadMember
{
	private final int index;
	private final OlympiadParticipiantData participiantData;
	private final OlympiadGame game;
	private final TeamType team;

	private final int clanId;
	private final String clanName;

	private double dealedDamage;

	private boolean dead = false;

	private Location _returnLoc = null;

	public OlympiadMember(int index, OlympiadParticipiantData participiantData, OlympiadGame game, TeamType team)
	{
		this.index = index;
		this.participiantData = participiantData;
		this.game = game;
		this.team = team;

		Player player = getPlayer();
		if(player != null)
		{
			Clan clan = player.getClan();
			this.clanId = clan == null ? 0 : clan.getClanId();
			this.clanName = clan == null ? StringUtils.EMPTY : clan.getName();
			player.setOlympiadSide(team.ordinal());
			player.setOlympiadGame(game);
		}
		else
		{
			this.clanId = 0;
			this.clanName = StringUtils.EMPTY;
		}
	}

	public OlympiadParticipiantData getStat()
	{
		return participiantData;
	}

	public Player getPlayer()
	{
		return GameObjectsStorage.getPlayer(getObjectId());
	}

	public TeamType getTeam()
	{
		return team;
	}

	public void incGameCount()
	{
		OlympiadParticipiantData data = getStat();
		switch(game.getType())
		{
			case TEAM:
				data.setTeamGamesCount(data.getTeamGamesCount() + 1);
				break;
			case CLASSED:
				data.setClassedGamesCount(data.getClassedGamesCount() + 1);
				break;
			case NON_CLASSED:
				data.setNonClassedGamesCount(data.getNonClassedGamesCount() + 1);
				break;
		}
		data.setDailyGameCount(data.getDailyGameCount() + 1);
		data.setLastGameTime((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
	}

	public boolean checkPlayer()
	{
		Player player = getPlayer();
		if(player == null || player.isLogoutStarted() || player.getOlympiadGame() == null || player.isInObserverMode())
			return false;
		return true;
	}

	public void portPlayerToArena()
	{
		Player player = getPlayer();
		if(!checkPlayer() || player.isTeleporting())
		{ return; }

		DuelEvent duel = player.getEvent(DuelEvent.class);
		if(duel != null)
			duel.abortDuel(player);

		_returnLoc = player.getStablePoint()
				== null ? player.getReflection().getReturnLoc() == null ? player.getLoc() : player.getReflection().getReturnLoc() : player.getStablePoint();

		if(player.isDead())
			player.setPendingRevive(true);
		if(player.isSitting())
			player.standUp();

		player.setTarget(null);
		player.setIsInOlympiadMode(true);
		player.addEvent(game.getEvent());

		player.getInventory().validateItems();

		player.leaveParty(false);

		if(game.getType() == CompType.TEAM)
			player.setTeam(getTeam());

		Reflection ref = game.getReflection();
		InstantZone instantZone = ref.getInstancedZone();

		Location tele = Location.findPointToStay(instantZone.getTeleportCoords().get(team.ordinal() - 1), 50, 50, ref.getGeoIndex());

		player.setVar("backCoords", _returnLoc.toXYZString(), -1);
		player.teleToLocation(tele, ref);

		player.sendPacket(new ExOlympiadModePacket(team.ordinal()));
	}

	public void portPlayerBack()
	{
		Player player = getPlayer();
		if(player == null)
			return;

		if(_returnLoc == null) // игрока не портнуло на стадион
			return;

		player.removeEvent(game.getEvent());
		player.setTeam(TeamType.NONE);
		player.setIsInOlympiadMode(false);
		player.setOlympiadSide(-1);
		player.setOlympiadGame(null);

		// Удаляем баффы и чужие кубики
		for(Abnormal abnormal : player.getAbnormalList())
		{
			if(!player.isSpecialAbnormal(abnormal.getSkill()))
				abnormal.exit();
		}

		for(Cubic cubic : player.getCubics())
		{
			if(player.getSkillLevel(cubic.getSkill().getId()) <= 0)
				cubic.delete();
		}

		for(Servitor servitor : player.getServitors())
			servitor.getAbnormalList().stopAll();

		player.setCurrentCp(player.getMaxCp());
		player.setCurrentMp(player.getMaxMp());

		if(player.isDead())
		{
			player.setCurrentHp(player.getMaxHp(), true);
			player.broadcastPacket(new RevivePacket(player));
			// player.broadcastStatusUpdate();
		}
		else
			player.setCurrentHp(player.getMaxHp(), false);

		// Возвращаем клановые скиллы если репутация положительная.
		if(player.getClan() != null)
			player.getClan().enableSkills(player);

		// Активируем геройские скиллы.
		player.activateHeroSkills(true);

		// Обновляем скилл лист, после добавления скилов
		player.sendSkillList();
		player.sendPacket(new ExOlympiadModePacket(0));
		player.sendPacket(new ExOlympiadMatchEndPacket());

		String back = player.getVar("backCoords");
		if(_returnLoc != null)
			player.teleToLocation(_returnLoc, ReflectionManager.MAIN);
		else if(back != null)
		{
			player.teleToLocation(Location.parseLoc(back), ReflectionManager.MAIN);
			player.unsetVar("backCoords");
		}
		else
			player.teleToLocation(Player.STABLE_LOCATION, ReflectionManager.MAIN);

		player.sendPacket(new ExOlympiadInfo(player));
		player.sendPacket(new ExOlympiadRecord(player));
	}

	public void preparePlayer1()
	{
		Player player = getPlayer();
		if(player == null)
			return;

		if(player.isInObserverMode())
			player.leaveObserverMode();

		// Un activate clan skills
		if(player.getClan() != null)
			player.getClan().disableSkills(player);

		// Деактивируем геройские скиллы.
		player.activateHeroSkills(false);

		// Abort casting if player casting
		if(player.isCastingNow())
			player.abortCast(true, true);

		// Abort attack if player attacking
		if(player.isAttackingNow())
			player.abortAttack(true, true);

		// Удаляем баффы и чужие кубики
		for(Abnormal abnormal : player.getAbnormalList())
		{
			if(!player.isSpecialAbnormal(abnormal.getSkill()))
				abnormal.exit();
		}

		for(Cubic cubic : player.getCubics())
		{
			if(player.getSkillLevel(cubic.getSkill().getId()) <= 0)
				cubic.delete();
		}

		// Remove Servitor's Buffs
		for(Servitor servitor : player.getServitors())
		{
			if(servitor.isPet())
				servitor.unSummon(false);
			else
			{
				servitor.getAbnormalList().stopAll();
				servitor.transferOwnerBuffs();
			}
		}

		// unsummon agathion
		if(player.getAgathionId() > 0)
			player.deleteAgathion();

		// Сброс кулдауна всех скилов, время отката которых меньше 15 минут
		for(TimeStamp sts : player.getSkillReuses())
		{
			if(sts == null)
				continue;
			Skill skill = SkillHolder.getInstance().getSkill(sts.getId(), sts.getLevel());
			if(skill == null)
				continue;
			if(skill.getReuseDelay() <= 900000L)
				player.enableSkill(skill);
		}

		// Обновляем скилл лист, после удаления скилов
		player.sendSkillList();

		// Проверяем одетые вещи на возможность ношения.
		player.getInventory().validateItems();

		// remove bsps/sps/ss automation
		player.removeAutoShots(true);

		player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
		player.setCurrentCp(player.getMaxCp());
		player.broadcastUserInfo(true);
	}

	public void preparePlayer2()
	{
		Player player = getPlayer();
		if(player == null)
			return;

		player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
		player.setCurrentCp(player.getMaxCp());
		player.broadcastUserInfo(true);
	}

	public void doDie()
	{
		dead = true;
		if(game.getType() == CompType.TEAM)
		{
			game.broadcastPacket(new SystemMessagePacket(SystemMsg.S1_WAS_KILLED).addString(getName()), true, true);
			Player player = getPlayer();
			if(player != null)
			{
				Reflection ref = game.getReflection();
				InstantZone instantZone = ref.getInstancedZone();
				Location tele = Location.findPointToStay(instantZone.getTeleportCoords().get(team.ordinal() - 1), 50, 50, ref.getGeoIndex());
				player.teleToLocation(tele, game.getReflection());
				// TODO: player.getFlags().getRegenBlock().start(this);
			}
		}
	}

	public boolean isDead()
	{
		return dead;
	}

	public void saveParticipantData()
	{
		OlympiadDatabase.saveParticipantData(getObjectId());
	}

	public void logout()
	{
		//
	}

	public String getName()
	{
		return getStat().getName();
	}

	public void addDealedDamage(double d)
	{
		dealedDamage += d;
	}

	public double getDealedDamage()
	{
		return dealedDamage;
	}

	public int getClanId()
	{
		return clanId;
	}

	public String getClanName()
	{
		return clanName;
	}

	public int getClassId()
	{
		return getStat().getClassId();
	}

	public int getObjectId()
	{
		return participiantData.getObjectId();
	}
}