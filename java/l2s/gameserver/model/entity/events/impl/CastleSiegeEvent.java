package l2s.gameserver.model.entity.events.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.dao.JdbcEntityState;
import l2s.gameserver.Config;
import l2s.gameserver.dao.CastleDamageZoneDAO;
import l2s.gameserver.dao.CastleDoorUpgradeDAO;
import l2s.gameserver.dao.CastleHiredGuardDAO;
import l2s.gameserver.dao.SiegeClanDAO;
import l2s.gameserver.dao.SiegePlayerDAO;
import l2s.gameserver.data.xml.holder.EventHolder;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.data.xml.holder.TeleportListHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawner;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.model.base.RestartType;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.model.entity.HeroDiary;
import l2s.gameserver.model.entity.events.EventAction;
import l2s.gameserver.model.entity.events.actions.StartStopAction;
import l2s.gameserver.model.entity.events.objects.CastleSiegeClanObject;
import l2s.gameserver.model.entity.events.objects.CastleSiegeMercenaryObject;
import l2s.gameserver.model.entity.events.objects.DoorObject;
import l2s.gameserver.model.entity.events.objects.SiegeClanObject;
import l2s.gameserver.model.entity.events.objects.SiegeToggleNpcObject;
import l2s.gameserver.model.entity.events.objects.SpawnExObject;
import l2s.gameserver.model.entity.events.objects.SpawnSimpleObject;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.entity.residence.ResidenceSide;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.instances.residences.SiegeToggleNpcInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.pledge.Alliance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.pledge.UnitMember;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExMercenaryCastlewarCastleSiegeHudInfo;
import l2s.gameserver.network.l2.s2c.ExPledgeMercenaryMemberJoin;
import l2s.gameserver.network.l2.s2c.PlaySoundPacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.tables.ClanTable;
import l2s.gameserver.templates.item.support.MerchantGuard;
import l2s.gameserver.utils.SiegeUtils;

/**
 * @author VISTALL
 * @date 15:12/14.02.2011
 */
public class CastleSiegeEvent extends SiegeEvent<Castle, CastleSiegeClanObject>
{
	private class GlobalEventListeners implements OnPlayerEnterListener
	{
		@Override
		public void onPlayerEnter(Player player)
		{
			if(isInPrepare() || isInProgress())
			{
				player.sendPacket(new ExMercenaryCastlewarCastleSiegeHudInfo(CastleSiegeEvent.this));
				CastleSiegeMercenaryObject mercenaryObject = getMercenary(player.getObjectId());
				if(mercenaryObject != null)
					player.setMercenaryId(mercenaryObject.getMercenaryId());
			}
		}
	}

	public static final int MAX_SIEGE_CLANS = Config.MAX_SIEGE_CLANS;

	public static final String DEFENDERS_WAITING = "defenders_waiting";
	public static final String DEFENDERS_REFUSED = "defenders_refused";

	public static final String CONTROL_TOWERS = "control_towers";
	public static final String FLAME_TOWERS = "flame_towers";
	public static final String BOUGHT_ZONES = "bought_zones";
	public static final String GUARDS = "guards";
	public static final String HIRED_GUARDS = "hired_guards";
	public static final String ARTEFACT = "artefact";
	public static final String RELIC = "relic";

	private static final String LIGHT_SIDE = "light_side";
	private static final String DARK_SIDE = "dark_side";

	private static final String PREPARE = "prepare";

	public static final int MERCENARY_MIN_LEVEL = 65;
	public static final int MERCENARIES_CLAN_LIMIT = 100;

	public static final int PREPARE_STATE = (1 << 2) | REGISTRATION_STATE;

	private final GlobalEventListeners globalEventListeners = new GlobalEventListeners();

	private final AtomicInteger lastMercenaryId = new AtomicInteger(0);

	private boolean _firstStep = false;

	private final IntSet _visitedParticipants = new HashIntSet(); // Участники, которые посетили осаду.

	private int siegeDuration = 3600;

	public CastleSiegeEvent(MultiValueSet<String> set)
	{
		super(set);
	}

	// ========================================================================================================================================================================
	// Главные методы осады
	// ========================================================================================================================================================================
	@Override
	public void initEvent()
	{
		super.initEvent();

		List<DoorObject> doorObjects = getObjects(DOORS);

		addObjects(BOUGHT_ZONES, CastleDamageZoneDAO.getInstance().load(getResidence()));

		for(DoorObject doorObject : doorObjects)
		{
			doorObject.setUpgradeValue(this, CastleDoorUpgradeDAO.getInstance().load(doorObject.getId()));
			doorObject.getDoor().addListener(_doorDeathListener);
		}
	}

	public void takeCastle(Clan newOwnerClan, ResidenceSide side)
	{
		getResidence().setResidenceSide(side, false);
		getResidence().broadcastResidenceState();

		processStep(newOwnerClan);
	}

	@Override
	public void processStep(Clan newOwnerClan)
	{
		Clan oldOwnerClan = getResidence().getOwner();

		getResidence().changeOwner(newOwnerClan);

		// если есть овнер в резиденции, делаем его аттакером
		if(oldOwnerClan != null)
		{
			SiegeClanObject ownerSiegeClan = getSiegeClan(DEFENDERS, oldOwnerClan);
			if(ownerSiegeClan != null)
			{
				removeObject(DEFENDERS, ownerSiegeClan);

				ownerSiegeClan.setType(ATTACKERS);
				addObject(ATTACKERS, ownerSiegeClan);
			}
		}
		else
		{
			// Если атакуется замок, принадлежащий NPC, и только 1 атакующий - закончить
			// осаду
			if(getObjects(ATTACKERS).size() == 1)
			{
				stopEvent(false);
				return;
			}

			// Если атакуется замок, принадлежащий NPC, и все атакующие в одном альянсе -
			// закончить осаду
			int allianceObjectId = newOwnerClan.getAllyId();
			if(allianceObjectId > 0)
			{
				List<SiegeClanObject> attackers = getObjects(ATTACKERS);
				boolean sameAlliance = true;
				for(SiegeClanObject sc : attackers)
				{
					if(sc != null && sc.getClan().getAllyId() != allianceObjectId)
						sameAlliance = false;
				}

				if(sameAlliance)
				{
					stopEvent(false);
					return;
				}
			}
		}

		// ставим нового овнера защитником
		SiegeClanObject newOwnerSiegeClan = getSiegeClan(ATTACKERS, newOwnerClan);
		newOwnerSiegeClan.deleteFlag();
		newOwnerSiegeClan.setType(DEFENDERS);

		removeObject(ATTACKERS, newOwnerSiegeClan);

		// у нас защитник ток овнер
		List<SiegeClanObject> defenders = removeObjects(DEFENDERS);
		for(SiegeClanObject siegeClan : defenders)
			siegeClan.setType(ATTACKERS);

		// новый овнер это защитник
		addObject(DEFENDERS, newOwnerSiegeClan);

		// все дефендеры, стают аттакующими
		addObjects(ATTACKERS, defenders);

		// При захвате замка, удаляем из списка участвующих на других осадах замков.
		for(CastleSiegeEvent castleSiege : EventHolder.getInstance().getEvents(CastleSiegeEvent.class))
		{
			if(castleSiege == this)
				continue;

			SiegeClanObject siegeClan = castleSiege.getSiegeClan(ATTACKERS, newOwnerClan);
			if(siegeClan != null)
			{
				siegeClan.deleteFlag();
				castleSiege.removeObject(ATTACKERS, siegeClan);

				for(Player player : newOwnerClan.getOnlineMembers())
				{
					player.removeEvent(castleSiege);
					player.broadcastCharInfo();
				}
			}

			siegeClan = castleSiege.getSiegeClan(DEFENDERS, newOwnerClan);
			if(siegeClan != null)
			{
				siegeClan.deleteFlag();
				castleSiege.removeObject(DEFENDERS, siegeClan);

				for(Player player : newOwnerClan.getOnlineMembers())
				{
					player.removeEvent(castleSiege);
					player.broadcastCharInfo();
				}
			}
		}

		updateParticles(true, ATTACKERS, DEFENDERS);

		teleportPlayers(FROM_RESIDENCE_TO_TOWN);

		// ток при первом захвате обнуляем мерчант гвардов и убираем апгрейд дверей
		if(!_firstStep)
		{
			_firstStep = true;

			broadcastTo(SystemMsg.THE_TEMPORARY_ALLIANCE_OF_THE_CASTLE_ATTACKER_TEAM_HAS_BEEN_DISSOLVED, ATTACKERS, DEFENDERS);

			if(_oldOwner != null)
			{
				if(containsObjects(HIRED_GUARDS))
				{
					spawnAction(HIRED_GUARDS, false);
					removeObjects(HIRED_GUARDS);
				}
				damageZoneAction(false);
				removeObjects(BOUGHT_ZONES);

				CastleDamageZoneDAO.getInstance().delete(getResidence());
			}
			else
				spawnAction(GUARDS, false);

			List<DoorObject> doorObjects = getObjects(DOORS);
			for(DoorObject doorObject : doorObjects)
			{
				doorObject.setWeak(true);
				doorObject.setUpgradeValue(this, 0);

				CastleDoorUpgradeDAO.getInstance().delete(doorObject.getId());
			}
		}

		spawnAction(DOORS, true);
		if(getId() == 3 || getId() == 7)
		{ // Giran, Goddard
			spawnAction(ARTEFACT, false);
			spawnAction(RELIC, true);
		}
		despawnSiegeSummons();
	}

	private void startPrepare()
	{
		if(isInProgress())
			return;

		if(isInPrepare())
			return;

		if(!canStartSiege())
		{ return; }
		addState(PREPARE_STATE);
		broadcastToWorld(new ExMercenaryCastlewarCastleSiegeHudInfo(this));
		CharListenerList.addGlobal(globalEventListeners);

		Clan owner = getResidence().getOwner();
		if(owner != null && getSiegeClan(DEFENDERS, owner) == null)
		{
			addObject(DEFENDERS, newSiegeClan(DEFENDERS, owner.getClanId(), 0, System.currentTimeMillis()));
		}
	}

	private void stopPrepare()
	{
		if(!isInPrepare())
			return;

		removeState(PREPARE_STATE);
		broadcastToWorld(new ExMercenaryCastlewarCastleSiegeHudInfo(this));
		CharListenerList.removeGlobal(globalEventListeners);
	}

	@Override
	public void startEvent()
	{
		if(isInProgress())
			return;

		removeState(PREPARE_STATE);

		if(!canStartSiege())
			return;

		List<SiegeClanObject> attackers = getObjects(ATTACKERS);
		if(attackers.isEmpty())
		{
			if(getResidence().getOwner() == null)
				broadcastToWorld(new SystemMessagePacket(SystemMsg.THE_SIEGE_OF_S1_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_INTEREST).addResidenceName(getResidence()));
			else
			{
				broadcastToWorld(new SystemMessagePacket(SystemMsg.S1S_SIEGE_WAS_CANCELED_BECAUSE_THERE_WERE_NO_CLANS_THAT_PARTICIPATED).addResidenceName(getResidence()));

				getResidence().getOwner().setCastleDefendCount(getResidence().getOwner().getCastleDefendCount() + 1);
				getResidence().getOwner().updateClanInDB();
			}

			getResidence().getOwnDate().setTimeInMillis(getResidence().getOwner() == null ? 0 : System.currentTimeMillis());
			reCalcNextTime(false);

			broadcastToWorld(new ExMercenaryCastlewarCastleSiegeHudInfo(this));
			return;
		}

		CharListenerList.addGlobal(globalEventListeners);

		_oldOwner = getResidence().getOwner();
		if(_oldOwner != null)
		{
			if(getResidence().getSpawnMerchantTickets().size() > 0)
			{
				for(ItemInstance item : getResidence().getSpawnMerchantTickets())
				{
					MerchantGuard guard = getResidence().getMerchantGuard(item.getItemId());

					addObject(HIRED_GUARDS, new SpawnSimpleObject(guard.getNpcId(), item.getLoc()));

					item.deleteMe();
				}

				CastleHiredGuardDAO.getInstance().delete(getResidence());

				if(containsObjects(HIRED_GUARDS))
					spawnAction(HIRED_GUARDS, true);
			}
		}

		SiegeClanDAO.getInstance().delete(getResidence());
		SiegePlayerDAO.getInstance().delete(getResidence());

		updateParticles(true, ATTACKERS, DEFENDERS);

		broadcastTo(SystemMsg.THE_TEMPORARY_ALLIANCE_OF_THE_CASTLE_ATTACKER_TEAM_IS_IN_EFFECT, ATTACKERS);
		broadcastTo(new SystemMessagePacket(SystemMsg.YOU_ARE_PARTICIPATING_IN_THE_SIEGE_OF_S1_THIS_SIEGE_IS_SCHEDULED_FOR_2_HOURS).addResidenceName(getResidence()), ATTACKERS, DEFENDERS);

		super.startEvent();

		if(_oldOwner == null)
			initControlTowers();
		else
			damageZoneAction(true);

		broadcastToWorld(new ExMercenaryCastlewarCastleSiegeHudInfo(this));
		broadcastToWorld(new SystemMessagePacket(SystemMsg.THE_S1_SIEGE_HAS_STARTED).addResidenceName(getResidence()));

		GameObjectsStorage.getPlayers(false, false).stream().filter(p -> !isParticipant(p)).forEach(p -> p.sendPacket(SystemMsg.YOU_CAN_WATCH_THE_SIEGE_TURNING_ON_OBSERVATION_MODE));
	}

	@Override
	public void stopEvent(boolean force)
	{
		if(!isInProgress())
		{
			stopPrepare();
			return;
		}

		CharListenerList.removeGlobal(globalEventListeners);

		if(!isInProgress())
		{
			removeMercenaries(false);
			removeObjects(ATTACKERS);
			removeObjects(DEFENDERS);
			removeObjects(DEFENDERS_WAITING);
			removeObjects(DEFENDERS_REFUSED);
		}
		else
		{
			List<DoorObject> doorObjects = getObjects(DOORS);
			for(DoorObject doorObject : doorObjects)
				doorObject.setWeak(false);

			for(int objectId : _visitedParticipants.toArray())
			{
				Player player = GameObjectsStorage.getPlayer(objectId);
				if(player != null)
					player.getListeners().onParticipateInCastleSiege(this);
			}

			damageZoneAction(false);

			updateParticles(false, ATTACKERS, DEFENDERS);

			removeMercenaries(true);

			List<SiegeClanObject> attackers = removeObjects(ATTACKERS);
			for(SiegeClanObject siegeClan : attackers)
				siegeClan.deleteFlag();

			broadcastToWorld(new SystemMessagePacket(SystemMsg.THE_SIEGE_OF_S1_IS_FINISHED).addResidenceName(getResidence()));

			removeObjects(DEFENDERS);
			removeObjects(DEFENDERS_WAITING);
			removeObjects(DEFENDERS_REFUSED);

			Clan ownerClan = getResidence().getOwner();
			if(ownerClan != null)
			{
				if(_oldOwner == ownerClan)
				{
					getResidence().getOwner().setCastleDefendCount(getResidence().getOwner().getCastleDefendCount() + 1);
					getResidence().getOwner().updateClanInDB();

					ownerClan.broadcastToOnlineMembers(new SystemMessagePacket(SystemMsg.SINCE_YOUR_CLAN_EMERGED_VICTORIOUS_FROM_THE_SIEGE_S1_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLANS_REPUTATION_SCORE).addInteger(ownerClan.incReputation(1500, false, toString())));
				}
				else
				{
					broadcastToWorld(new SystemMessagePacket(SystemMsg.CLAN_S1_IS_VICTORIOUS_OVER_S2S_CASTLE_SIEGE).addString(ownerClan.getName()).addResidenceName(getResidence()));

					ownerClan.broadcastToOnlineMembers(new SystemMessagePacket(SystemMsg.SINCE_YOUR_CLAN_EMERGED_VICTORIOUS_FROM_THE_SIEGE_S1_POINTS_HAVE_BEEN_ADDED_TO_YOUR_CLANS_REPUTATION_SCORE).addInteger(ownerClan.incReputation(3000, false, toString())));

					if(_oldOwner != null)
						_oldOwner.broadcastToOnlineMembers(new SystemMessagePacket(SystemMsg.YOUR_CLAN_HAS_FAILED_TO_DEFEND_THE_CASTLE_S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_YOU_CLAN_REPUTATION_SCORE_AND_ADDED_TO_YOUR_OPPONENTS).addInteger(-_oldOwner.incReputation(-3000, false, toString())));

					for(UnitMember member : ownerClan)
					{
						Player player = member.getPlayer();
						if(player != null)
						{
							player.sendPacket(PlaySoundPacket.SIEGE_VICTORY);
							if(player.isOnline() && player.isHero())
								Hero.getInstance().addHeroDiary(player.getObjectId(), HeroDiary.ACTION_CASTLE_TAKEN, getResidence().getId());
						}
					}
				}

				for(Castle castle : ResidenceHolder.getInstance().getResidenceList(Castle.class))
				{
					if(castle == getResidence())
						continue;

					SiegeEvent<?, ?> siegeEvent = castle.getSiegeEvent();
					if(siegeEvent == null)
						continue;

					SiegeClanObject siegeClan = siegeEvent.getSiegeClan(ATTACKERS, ownerClan);
					if(siegeClan == null)
						siegeClan = siegeEvent.getSiegeClan(DEFENDERS, ownerClan);
					if(siegeClan == null)
						siegeClan = siegeEvent.getSiegeClan(DEFENDERS_WAITING, ownerClan);

					if(siegeClan != null)
					{
						siegeEvent.getObjects(siegeClan.getType()).remove(siegeClan);

						SiegeClanDAO.getInstance().delete(castle, siegeClan);
						SiegePlayerDAO.getInstance().delete(castle, siegeClan.getClan().getClanId());
					}
				}

				getResidence().getOwnDate().setTimeInMillis(System.currentTimeMillis());
				getResidence().getLastSiegeDate().setTimeInMillis(getResidence().getSiegeDate().getTimeInMillis());

				/*
				 * // CLAN CRP int id = getResidence().getId(); if (id == 3 || id == 5 || id ==
				 * 8) { //ownerClan.incReputation(20000);
				 * ownerClan.incReputation(Config.SIEGE_WINNER_REPUTATION_REWARD, false,
				 * "SiegeWinnerCustomReward"); Player leader =
				 * ownerClan.getLeader().getPlayer(); if (leader != null && leader.isOnline()) {
				 * leader.getInventory().addItem(24003, 1, "SiegeEvent"); } String msg =
				 * "20.000 Clan Reputation Points has been added to " + ownerClan.getName() +
				 * " clan for capturing " + getResidence().getName() + " of castle!"; SayPacket2
				 * packet = new SayPacket2(0, ChatType.CRITICAL_ANNOUNCE,
				 * getResidence().getName() + " Castle", msg); for(Player player :
				 * GameObjectsStorage.getPlayers()) { player.sendPacket(packet);
				 * player.sendPacket(new ExShowScreenMessage(msg, 3000,
				 * ScreenMessageAlign.TOP_CENTER, false)); } }
				 */
			}
			else
			{
				broadcastToWorld(new SystemMessagePacket(SystemMsg.THE_SIEGE_OF_S1_HAS_ENDED_IN_A_DRAW).addResidenceName(getResidence()));

				getResidence().getOwnDate().setTimeInMillis(0);
				getResidence().getLastSiegeDate().setTimeInMillis(getResidence().getSiegeDate().getTimeInMillis());
				getResidence().setResidenceSide(ResidenceSide.NEUTRAL, false);
				getResidence().broadcastResidenceState();
			}

			despawnSiegeSummons();

			if(_oldOwner != null)
			{
				if(containsObjects(HIRED_GUARDS))
				{
					spawnAction(HIRED_GUARDS, false);
					removeObjects(HIRED_GUARDS);
				}
			}
		}

		super.stopEvent(force);

		broadcastToWorld(new ExMercenaryCastlewarCastleSiegeHudInfo(this));
	}

	private void removeMercenaries(boolean giveRewards)
	{

		lastMercenaryId.set(0);

		List<CastleSiegeClanObject> clanObjects = new ArrayList<>();
		clanObjects.addAll(getObjects(ATTACKERS));
		clanObjects.addAll(getObjects(DEFENDERS));

		for(CastleSiegeClanObject clanObject : clanObjects)
		{
			int type = clanObject.getType().equals(ATTACKERS) ? 1 : (clanObject.getType().equals(DEFENDERS) ? 2 : 0);
			for(CastleSiegeMercenaryObject mercenaryObject : clanObject.getMercenaries())
			{
				Player player = mercenaryObject.getPlayer();
				if(player != null)
				{
					if(giveRewards)
					{
						/*
						 * TODO: Реализовать награду наемникам. msg_begin id=9001 UNK_0=1 message=[Я
						 * хотел бы выразить свою благодарность за огромный вклад, который Вы, $s1,
						 * внесли в победу в битве за Замок Гиран в качестве наемника, Вы заняли 1-е
						 * место. Также направляю Вам награду за Вашу службу. Надеюсь, что Вы
						 * присоединитесь к нам и в будущих сражениях. Благодарю!\n-Лорд Замка Гиран-]
						 * group=0 color=B09B79FF sound=[None] voice=[None] win=0 font=0 lftime=0 bkg=0
						 * anim=0 scrnmsg=[] scrnparam=[] gfxscrnmsg=[] gfxscrnparam=[] type=[none]
						 * msg_end msg_begin id=9002 UNK_0=1 message=[Я хотел бы выразить свою
						 * благодарность за вклад, который наемник $s1 внес в победу в битве за Замок
						 * Гиран, Вы заняли 2-е место. Также направляю Вам награду за Вашу службу.
						 * Надеюсь, что Вы присоединитесь к нам и в будущих сражениях. Благодарю!\n-Лорд
						 * Замка Гиран-] group=0 color=B09B79FF sound=[None] voice=[None] win=0 font=0
						 * lftime=0 bkg=0 anim=0 scrnmsg=[] scrnparam=[] gfxscrnmsg=[] gfxscrnparam=[]
						 * type=[none] msg_end msg_begin id=9003 UNK_0=1 message=[Я хотел бы выразить
						 * свою благодарность за вклад, который Вы, $s1, внесли в победу в битве за
						 * Замок Гиран в качестве наемника, Вы заняли 3-е место. Также направляю Вам
						 * награду за Вашу службу. Надеюсь, что Вы присоединитесь к нам и в будущих
						 * сражениях. Благодарю!\n-Лорд Замка Гиран-] group=0 color=B09B79FF
						 * sound=[None] voice=[None] win=0 font=0 lftime=0 bkg=0 anim=0 scrnmsg=[]
						 * scrnparam=[] gfxscrnmsg=[] gfxscrnparam=[] type=[none] msg_end msg_begin
						 * id=9004 UNK_0=1 message=[Я хотел бы выразить свою благодарность за огромный
						 * вклад, который Вы, $s1, внесли в победу в битве за Замок Гиран в качестве
						 * наемника. Надеюсь, что Вы присоединитесь к нам и в будущих сражениях.
						 * Благодарю!\n-Лорд Замка Гиран-] group=0 color=B09B79FF sound=[None]
						 * voice=[None] win=0 font=0 lftime=0 bkg=0 anim=0 scrnmsg=[] scrnparam=[]
						 * gfxscrnmsg=[] gfxscrnparam=[] type=[none] msg_end msg_begin id=9005 UNK_0=1
						 * message=[Я хотел бы выразить свою благодарность за вклад, который Вы, $s1,
						 * внесли в победу в битве за Замок Гиран в качестве наемника. Надеюсь, что Вы
						 * присоединитесь к нам и в будущих сражениях. Благодарю!\n-Лорд Замка Гиран-]
						 * group=0 color=B09B79FF sound=[None] voice=[None] win=0 font=0 lftime=0 bkg=0
						 * anim=0 scrnmsg=[] scrnparam=[] gfxscrnmsg=[] gfxscrnparam=[] type=[none]
						 * msg_end msg_begin id=9006 UNK_0=1 message=[Я хотел бы выразить свою
						 * благодарность за вклад, который Вы, $s1, внесли в победу в битве за Замок
						 * Гиран в качестве наемника. Надеюсь, что Вы присоединитесь к нам и в будущих
						 * сражениях. Благодарю!\n-Лорд Замка Гиран-] group=0 color=B09B79FF
						 * sound=[None] voice=[None] win=0 font=0 lftime=0 bkg=0 anim=0 scrnmsg=[]
						 * scrnparam=[] gfxscrnmsg=[] gfxscrnparam=[] type=[none] msg_end msg_begin
						 * id=9007 UNK_0=1 message=[Благодарю Вас, $s1, за участие в битве за Замок
						 * Гиран в качестве наемника, прилагаю Вашу награду. Благодарю Вас за
						 * участие.\n-Лорд Замка Гиран-] group=0 color=B09B79FF sound=[None]
						 * voice=[None] win=0 font=0 lftime=0 bkg=0 anim=0 scrnmsg=[] scrnparam=[]
						 * gfxscrnmsg=[] gfxscrnparam=[] type=[none] msg_end msg_begin id=9008 UNK_0=1
						 * message=[$s1, увы, но мы не можем вознаградить Вас за службу в качестве
						 * наемника. Надеюсь, в будущих сражениях Вы будете более удачливы.\n-Лорд Замка
						 * Гиран-] group=0 color=B09B79FF sound=[None] voice=[None] win=0 font=0
						 * lftime=0 bkg=0 anim=0 scrnmsg=[] scrnparam=[] gfxscrnmsg=[] gfxscrnparam=[]
						 * type=[none] msg_end
						 */
						player.sendPacket(SystemMsg.S1_TO_MY_REGRET_WE_CANNOT_REWARD_YOU_FOR_YOUR_SERVICES_AS_A_MERCENARY_I_HOPE_YOU_WILL_HAVE_MORE_LUCK_IN_THE_BATTLES_TO_COMEN_GIRAN_CASTLE_LORD);
					}
					player.setMercenaryId(-1);
					player.broadcastPacket(new ExPledgeMercenaryMemberJoin(type, false, player.getObjectId(), clanObject.getObjectId()));
					player.sendUserInfo(true);
					player.broadcastCharInfoImpl();
				}
			}
			clanObject.clearMercenaries();
		}
	}

	// ========================================================================================================================================================================

	@Override
	public void reCalcNextTime(boolean onInit)
	{
		clearActions();

		if(onInit)
		{
			final long currentTimeMillis = System.currentTimeMillis();
			final Calendar startSiegeDate = getResidence().getSiegeDate();
			if(startSiegeDate.getTimeInMillis() > currentTimeMillis)
				registerActions();
			else if(startSiegeDate.getTimeInMillis() == 0 || startSiegeDate.getTimeInMillis() <= currentTimeMillis)
				setNextSiegeTime();
		}
		else
		{
			if(getResidence().getOwner() != null)
			{
				getResidence().getSiegeDate().setTimeInMillis(0);
				getResidence().setJdbcState(JdbcEntityState.UPDATED);
				getResidence().update();
			}
			setNextSiegeTime();
		}
	}

	@Override
	public void loadSiegeClans()
	{
		super.loadSiegeClans();

		addObjects(DEFENDERS_WAITING, SiegeClanDAO.getInstance().load(getResidence(), DEFENDERS_WAITING));
		addObjects(DEFENDERS_REFUSED, SiegeClanDAO.getInstance().load(getResidence(), DEFENDERS_REFUSED));

		for(String type : new String[] {
				ATTACKERS,
				DEFENDERS
		})
		{
			List<CastleSiegeClanObject> siegeClanObjects = getObjects(type);
			siegeClanObjects.forEach(CastleSiegeClanObject::select);
		}
	}

	@Override
	public void announce(int id, String value, int time)
	{
		if(id == 1)
		{
			SystemMessagePacket msg;
			int seconds = Integer.parseInt(value);
			int min = seconds / 60;
			int hour = min / 60;

			if(hour > 0)
				msg = new SystemMessagePacket(SystemMsg.S1_HOURS_UNTIL_CASTLE_SIEGE_CONCLUSION).addInteger(hour);
			else if(min > 0)
				msg = new SystemMessagePacket(SystemMsg.S1_MINUTES_UNTIL_CASTLE_SIEGE_CONCLUSION).addInteger(min);
			else
				msg = new SystemMessagePacket(SystemMsg.THIS_CASTLE_SIEGE_WILL_END_IN_S1_SECONDS).addInteger(seconds);

			broadcastTo(msg, ATTACKERS, DEFENDERS);
		}
	}

	// ========================================================================================================================================================================
	// Control Tower Support
	// ========================================================================================================================================================================
	private void initControlTowers()
	{
		List<SpawnExObject> objects = getObjects(GUARDS);
		List<Spawner> spawns = new ArrayList<Spawner>();
		for(SpawnExObject o : objects)
			spawns.addAll(o.getSpawns());

		List<SiegeToggleNpcObject> ct = getObjects(CONTROL_TOWERS);

		SiegeToggleNpcInstance closestCt;
		double distance, distanceClosest;

		for(Spawner spawn : spawns)
		{
			Location spawnLoc = spawn.getRandomSpawnRange().getRandomLoc(ReflectionManager.MAIN.getGeoIndex(), false);

			closestCt = null;
			distanceClosest = 0;

			for(SiegeToggleNpcObject c : ct)
			{
				SiegeToggleNpcInstance npcTower = c.getToggleNpc();
				distance = npcTower.getDistance(spawnLoc);

				if(closestCt == null || distance < distanceClosest)
				{
					closestCt = npcTower;
					distanceClosest = distance;
				}

				closestCt.register(spawn);
			}
		}
	}

	// ========================================================================================================================================================================
	// Damage Zone Actions
	// ========================================================================================================================================================================
	private void damageZoneAction(boolean active)
	{
		if(containsObjects(BOUGHT_ZONES))
			zoneAction(BOUGHT_ZONES, active);
	}

	// ========================================================================================================================================================================
	// Суппорт Методы для установки времени осады
	// ========================================================================================================================================================================
	/**
	 * Автоматически генерит и устанавливает дату осады
	 */
	private void setNextSiegeTime()
	{
		long startTime = generateSiegeDateTime(_startTimePattern);
		if(startTime == 0)
			return;

		broadcastToWorld(new SystemMessagePacket(SystemMsg.S1_HAS_ANNOUNCED_THE_NEXT_CASTLE_SIEGE_TIME).addResidenceName(getResidence()));

		clearActions();

		getResidence().getSiegeDate().setTimeInMillis(startTime);
		getResidence().setJdbcState(JdbcEntityState.UPDATED);
		getResidence().update();

		registerActions();
	}

	@Override
	public boolean isAttackersInAlly()
	{
		return !_firstStep;
	}

	@Override
	public boolean canResurrect(Creature active, Creature target, boolean force, boolean quiet)
	{
		boolean targetInZone = checkIfInZone(target);
		// если оба вне зоны - рес разрешен
		// если таргет вне осадный зоны - рес разрешен
		if(!targetInZone)
			return true;

		Player resurectPlayer = active.getPlayer();
		Player targetPlayer = target.getPlayer();

		// если оба незареганы - невозможно ресать
		// если таргет незареган - невозможно ресать
		if(!resurectPlayer.containsEvent(this) || !targetPlayer.containsEvent(this))
		{
			if(!quiet)
			{
				if(force)
					targetPlayer.sendPacket(SystemMsg.IT_IS_NOT_POSSIBLE_TO_RESURRECT_IN_BATTLEFIELDS_WHERE_A_SIEGE_WAR_IS_TAKING_PLACE);
				active.sendPacket(force ? SystemMsg.IT_IS_NOT_POSSIBLE_TO_RESURRECT_IN_BATTLEFIELDS_WHERE_A_SIEGE_WAR_IS_TAKING_PLACE : SystemMsg.INVALID_TARGET);
			}
			return false;
		}

		SiegeClanObject targetSiegeClan = getSiegeClan(ATTACKERS, targetPlayer);
		if(targetSiegeClan == null)
			targetSiegeClan = getSiegeClan(DEFENDERS, targetPlayer);

		if(targetSiegeClan == null || targetSiegeClan.getType() == ATTACKERS)
		{
			// если нету флага - рес запрещен
			if(targetSiegeClan == null || targetSiegeClan.getFlag() == null)
			{
				if(!quiet)
				{
					if(force)
						targetPlayer.sendPacket(SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE);
					active.sendPacket(force ? SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE : SystemMsg.INVALID_TARGET);
				}
				return false;
			}
		}
		else
		{
			List<SiegeToggleNpcObject> towers = getObjects(CONTROL_TOWERS);

			boolean canRes = false;
			for(SiegeToggleNpcObject t : towers)
			{
				if(t.isAlive())
				{
					canRes = true;
					break;
				}
			}

			if(!canRes)
			{
				targetPlayer.sendPacket(SystemMsg.THE_GUARDIAN_TOWER_HAS_BEEN_DESTROYED_AND_RESURRECTION_IS_NOT_POSSIBLE);
				active.sendPacket(SystemMsg.THE_GUARDIAN_TOWER_HAS_BEEN_DESTROYED_AND_RESURRECTION_IS_NOT_POSSIBLE);
				return false;
			}
		}

		// if(force)
		return true;
		/*
		 * else { if(!quiet) active.sendPacket(SystemMsg.INVALID_TARGET); return false;
		 * }
		 */
	}

	@Override
	public boolean ifVar(String name)
	{
		if(name.equals(LIGHT_SIDE))
			return getResidence().getResidenceSide() == ResidenceSide.LIGHT;
		if(name.equals(DARK_SIDE))
			return getResidence().getResidenceSide() == ResidenceSide.DARK;

		return super.ifVar(name);
	}

	public void addVisitedParticipant(Player player)
	{
		_visitedParticipants.add(player.getObjectId());
	}

	public boolean canRegisterOnSiege(Player player, Clan clan, boolean attacker)
	{
		if(attacker)
		{
			if(getResidence().getOwnerId() == clan.getClanId())
			{
				player.sendPacket(SystemMsg.CASTLE_OWNING_CLANS_ARE_AUTOMATICALLY_REGISTERED_ON_THE_DEFENDING_SIDE);
				return false;
			}

			Alliance alliance = clan.getAlliance();
			if(alliance != null)
			{
				for(Clan c : alliance.getMembers())
				{
					if(c.getCastle() == getResidence().getId())
					{
						player.sendPacket(SystemMsg.YOU_CANNOT_REGISTER_AS_AN_ATTACKER_BECAUSE_YOU_ARE_IN_AN_ALLIANCE_WITH_THE_CASTLE_OWNING_CLAN);
						return false;
					}
				}
			}
			if(clan.getCastle() != 0)
			{
				player.sendPacket(SystemMsg.A_CLAN_THAT_OWNS_A_CASTLE_CANNOT_PARTICIPATE_IN_ANOTHER_SIEGE);
				return false;
			}

			if(getSiegeClan(ATTACKERS, clan) != null)
			{
				player.sendPacket(SystemMsg.YOU_HAVE_ALREADY_REQUESTED_A_CASTLE_SIEGE);
				return false;
			}

			if(getSiegeClan(DEFENDERS, clan) != null || getSiegeClan(DEFENDERS_WAITING, clan) != null || getSiegeClan(DEFENDERS_REFUSED, clan) != null)
			{
				player.sendPacket(SystemMsg.YOU_HAVE_ALREADY_REGISTERED_TO_THE_DEFENDER_SIDE_AND_MUST_CANCEL_YOUR_REGISTRATION_BEFORE_SUBMITTING_YOUR_REQUEST);
				return false;
			}
		}
		else
		{
			if(getResidence().getOwnerId() == 0)
			{
				player.sendPacket(SystemMsg.NO_MORE_REGISTRATIONS_MAY_BE_ACCEPTED_FOR_THE_DEFENDER_SIDE);
				return false;
			}

			if(getResidence().getOwnerId() == clan.getClanId())
			{
				player.sendPacket(SystemMsg.CASTLE_OWNING_CLANS_ARE_AUTOMATICALLY_REGISTERED_ON_THE_DEFENDING_SIDE);
				return false;
			}

			if(clan.getCastle() != 0)
			{
				player.sendPacket(SystemMsg.A_CLAN_THAT_OWNS_A_CASTLE_CANNOT_PARTICIPATE_IN_ANOTHER_SIEGE);
				return false;
			}

			if(getSiegeClan(DEFENDERS, clan) != null || getSiegeClan(DEFENDERS_WAITING, clan) != null || getSiegeClan(DEFENDERS_REFUSED, clan) != null)
			{
				player.sendPacket(SystemMsg.YOU_HAVE_ALREADY_REQUESTED_A_CASTLE_SIEGE);
				return false;
			}

			if(getSiegeClan(ATTACKERS, clan) != null)
			{
				player.sendPacket(SystemMsg.YOU_ARE_ALREADY_REGISTERED_TO_THE_ATTACKER_SIDE_AND_MUST_CANCEL_YOUR_REGISTRATION_BEFORE_SUBMITTING_YOUR_REQUEST);
				return false;
			}
		}

		if(isMercenary(player.getObjectId()))
		{
			player.sendPacket(SystemMsg.YOU_CANT_REGISTER_FOR_ATTACKERS_OR_DEFENDERS_IN_THE_MERCENARY_MODE_);
			return false;
		}

		IBroadcastPacket msg = checkSiegeClanLevel(clan);
		if(msg != null)
		{
			player.sendPacket(msg);
			return false;
		}

		return true;
	}

	public IBroadcastPacket checkSiegeClanLevel(Clan clan)
	{
		if(clan.getLevel() < SiegeUtils.MIN_CLAN_SIEGE_LEVEL)
			return SystemMsg.ONLY_CLANS_OF_LEVEL_5_OR_HIGHER_MAY_REGISTER_FOR_A_CASTLE_SIEGE;
		return null;
	}

	public boolean canCastSeal(Player player)
	{
		return true;
	}

	public void onLordDie(NpcInstance npc)
	{
		//
	}

	@Override
	public void action(String name, boolean start)
	{
		if(name.equalsIgnoreCase(PREPARE))
		{
			if(start)
			{
				startPrepare();
			}
			else
			{
				stopPrepare();
			}
		}
		else
			super.action(name, start);
	}

	@Override
	public void addOnTimeAction(int time, EventAction action)
	{
		super.addOnTimeAction(time, action);

		if(action instanceof StartStopAction)
		{
			StartStopAction startStopAction = (StartStopAction) action;
			if(!startStopAction.isStart() && startStopAction.getName().equalsIgnoreCase(EVENT))
				siegeDuration = time;
		}
	}

	private boolean canStartSiege()
	{
		for(CastleSiegeEvent siegeEvent : EventHolder.getInstance().getEvents(getClass()))
		{
			if(siegeEvent != this && (siegeEvent.isInPrepare() || siegeEvent.isInProgress()))
			{
				if(TimeUnit.MILLISECONDS.toSeconds(siegeEvent.startTimeMillis()) != TimeUnit.MILLISECONDS.toSeconds(startTimeMillis()))
				{
					warn("Cannot start two or more castle sieges in same time! Please change siege time for castle siege ID: " + getId());
					return false;
				}
			}
		}
		return true;
	}

	public int getSiegeDuration()
	{
		return siegeDuration;
	}

	public boolean isInPrepare()
	{
		return hasState(PREPARE_STATE);
	}

	@Override
	public void checkRestartLocs(Player player, Map<RestartType, Boolean> r)
	{
		if(!isParticipant(DEFENDERS, player))
		{
			if(getId() == 3)
			{
				if(TeleportListHolder.getInstance().getTeleportInfo(422) != null)
					r.put(RestartType.TO_FLAG, Boolean.TRUE);
			}
			else if(getId() == 7)
			{
				if(TeleportListHolder.getInstance().getTeleportInfo(4227) != null)
					r.put(RestartType.TO_FLAG, Boolean.TRUE);
			}
		}
		super.checkRestartLocs(player, r);
	}

	public AtomicInteger getLastMercenaryId()
	{
		return lastMercenaryId;
	}

	public CastleSiegeMercenaryObject getMercenary(String type, int objectId)
	{
		List<CastleSiegeClanObject> siegeClanObjects = getObjects(type);
		for(CastleSiegeClanObject siegeClanObject : siegeClanObjects)
		{
			CastleSiegeMercenaryObject mercenaryObject = siegeClanObject.getMercenary(objectId);
			if(mercenaryObject != null)
				return mercenaryObject;
		}
		return null;
	}

	public CastleSiegeMercenaryObject getMercenary(int objectId)
	{
		CastleSiegeMercenaryObject mercenaryObject = getMercenary(ATTACKERS, objectId);
		if(mercenaryObject == null)
			mercenaryObject = getMercenary(DEFENDERS, objectId);
		return mercenaryObject;
	}

	public boolean isMercenary(String type, int objectId)
	{
		return getMercenary(type, objectId) != null;
	}

	public boolean isMercenary(int objectId)
	{
		return isMercenary(ATTACKERS, objectId) || isMercenary(DEFENDERS, objectId);
	}

	@Override
	public CastleSiegeClanObject newSiegeClan(String type, int clanId, long i, long date)
	{
		Clan clan = ClanTable.getInstance().getClan(clanId);
		return clan == null ? null : new CastleSiegeClanObject(this, type, clan, i, date);
	}

	@Override
	public int getSiegeClanId(Player player)
	{
		CastleSiegeMercenaryObject mercenaryObject = getMercenary(player.getObjectId());
		if(mercenaryObject != null)
			return mercenaryObject.getClanId();
		return super.getSiegeClanId(player);
	}

	@Override
	public Clan getVisibleClan(Player player, Player observer)
	{
		int clanId = getSiegeClanId(player);
		if(clanId > 0)
			return ClanTable.getInstance().getClan(clanId);
		return null;
	}
}
