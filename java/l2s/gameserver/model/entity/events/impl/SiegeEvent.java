package l2s.gameserver.model.entity.events.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CHashIntObjectMap;
import org.napile.primitive.pair.IntObjectPair;

import l2s.commons.collections.LazyArrayList;
import l2s.commons.collections.MultiValueSet;
import l2s.commons.dao.JdbcEntityState;
import l2s.commons.lang.reference.HardReference;
import l2s.commons.lang.reference.HardReferences;
import l2s.commons.time.cron.SchedulingPattern;
import l2s.commons.util.Rnd;
import l2s.gameserver.dao.SiegeClanDAO;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.data.xml.holder.TeleportListHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.listener.actor.OnDeathListener;
import l2s.gameserver.listener.actor.OnKillListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.base.RestartType;
import l2s.gameserver.model.entity.events.Event;
import l2s.gameserver.model.entity.events.EventType;
import l2s.gameserver.model.entity.events.objects.SiegeClanObject;
import l2s.gameserver.model.entity.events.objects.ZoneObject;
import l2s.gameserver.model.entity.residence.Residence;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.SummonInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.RelationChangedPacket;
import l2s.gameserver.network.l2.s2c.UserInfo;
import l2s.gameserver.tables.ClanTable;
import l2s.gameserver.templates.DoorTemplate;
import l2s.gameserver.templates.TeleportTemplate;
import l2s.gameserver.utils.TeleportUtils;

/**
 * @author VISTALL
 * @date 15:11/14.02.2011
 */
public abstract class SiegeEvent<R extends Residence, S extends SiegeClanObject> extends Event
{
	protected class SiegeSummonInfo
	{
		private int _skillId;
		private int _ownerObjectId;

		private HardReference<SummonInstance> _summonRef = HardReferences.emptyRef();

		SiegeSummonInfo(SummonInstance summonInstance)
		{
			_skillId = summonInstance.getSkillId();
			_ownerObjectId = summonInstance.getPlayer().getObjectId();
			_summonRef = summonInstance.getRef();
		}

		public int getSkillId()
		{
			return _skillId;
		}

		public int getOwnerObjectId()
		{
			return _ownerObjectId;
		}
	}

	public class DoorDeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature actor, Creature killer)
		{
			if(!isInProgress())
				return;

			DoorInstance door = (DoorInstance) actor;
			if(door.getDoorType() == DoorTemplate.DoorType.WALL)
				return;

			broadcastTo(SystemMsg.THE_CASTLE_GATE_HAS_BEEN_DESTROYED, SiegeEvent.ATTACKERS, SiegeEvent.DEFENDERS);
		}
	}

	public static final String HAVE_OWNER = "have_owner";
	public static final String HAVE_OLD_OWNER = "have_old_owner";

	public static final String ATTACKERS = "attackers";
	public static final String DEFENDERS = "defenders";
	public static final String SPECTATORS = "spectators";

	public static final String FROM_RESIDENCE_TO_TOWN = "from_residence_to_town";

	public static final String SIEGE_ZONES = "siege_zones";
	public static final String FLAG_ZONES = "flag_zones";

	public static final String DAY_OF_WEEK = "day_of_week";
	public static final String HOUR_OF_DAY = "hour_of_day";
	public static final String SIEGE_INTERVAL_IN_WEEKS = "siege_interval_in_weeks";

	public static final String REGISTRATION = "registration";

	public static final String DOORS = "doors";

	// states
	public static final int PROGRESS_STATE = 1 << 0;
	public static final int REGISTRATION_STATE = 1 << 1;

	public static final long DAY_IN_MILISECONDS = 86400000L;

	protected R _residence;

	private int _state;

	protected Clan _oldOwner;

	protected OnKillListener _killListener;
	protected OnDeathListener _doorDeathListener = new DoorDeathListener();
	protected IntObjectMap<SiegeSummonInfo> _siegeSummons = new CHashIntObjectMap<SiegeSummonInfo>();

	protected final SchedulingPattern _startTimePattern;
	protected final Calendar _validationDate;

	public SiegeEvent(MultiValueSet<String> set)
	{
		super(set);

		String startTime = set.getString("start_time", null);
		if(!StringUtils.isEmpty(startTime))
			_startTimePattern = new SchedulingPattern(startTime);
		else
			_startTimePattern = null;

		int[] validationTimeArray = set.getIntegerArray("validation_date", new int[] {
				2,
				4,
				2003
		});
		_validationDate = Calendar.getInstance();
		_validationDate.set(Calendar.DAY_OF_MONTH, validationTimeArray[0]);
		_validationDate.set(Calendar.MONTH, validationTimeArray[1] - 1);
		_validationDate.set(Calendar.YEAR, validationTimeArray[2]);
		_validationDate.set(Calendar.HOUR_OF_DAY, 0);
		_validationDate.set(Calendar.MINUTE, 0);
		_validationDate.set(Calendar.SECOND, 0);
		_validationDate.set(Calendar.MILLISECOND, 0);
	}

	public long generateSiegeDateTime(SchedulingPattern pattern)
	{
		if(pattern == null)
			return 0;

		final long currentTime = System.currentTimeMillis();
		long time = pattern.next(_validationDate.getTimeInMillis());
		while(time < currentTime)
		{
			time = pattern.next(time);
		}
		return time;
	}

	// ========================================================================================================================================================================
	// Start / Stop Siege
	// ========================================================================================================================================================================

	@Override
	public void startEvent()
	{
		addState(PROGRESS_STATE);

		super.startEvent();
	}

	@Override
	public void stopEvent(boolean force)
	{
		_state = 0;

		despawnSiegeSummons();
		reCalcNextTime(false);

		super.stopEvent(force);
	}

	public void processStep(Clan clan)
	{
		//
	}

	@Override
	public void reCalcNextTime(boolean onInit)
	{
		clearActions();

		final Calendar startSiegeDate = getResidence().getSiegeDate();
		if(onInit)
		{
			// дата ниже текущей
			if(startSiegeDate.getTimeInMillis() <= System.currentTimeMillis())
			{
				startSiegeDate.setTimeInMillis(generateSiegeDateTime(_startTimePattern));
				getResidence().setJdbcState(JdbcEntityState.UPDATED);
			}
		}
		else
		{
			startSiegeDate.setTimeInMillis(generateSiegeDateTime(_startTimePattern));
			getResidence().setJdbcState(JdbcEntityState.UPDATED);
		}

		registerActions();

		getResidence().update();
	}

	@Override
	protected long startTimeMillis()
	{
		return getSiegeDate().getTimeInMillis();
	}

	public Calendar getSiegeDate()
	{
		return getResidence().getSiegeDate();
	}

	// ========================================================================================================================================================================
	// Zones
	// ========================================================================================================================================================================

	@Override
	public void teleportPlayers(String t)
	{
		List<Player> players = new ArrayList<Player>();
		Clan ownerClan = getResidence().getOwner();
		if(t.equalsIgnoreCase(HAVE_OWNER))
		{
			if(ownerClan != null)
				for(Player player : getPlayersInZone())
					if(player.getClan() == ownerClan)
						players.add(player);
		}
		else if(t.equalsIgnoreCase(ATTACKERS))
		{
			for(Player player : getPlayersInZone())
			{
				if(isParticipant(ATTACKERS, player))
					players.add(player);
			}
		}
		else if(t.equalsIgnoreCase(DEFENDERS))
		{
			for(Player player : getPlayersInZone())
			{
				if(ownerClan != null && ownerClan.getClanId() == getSiegeClanId(player))
					continue;

				if(isParticipant(DEFENDERS, player))
					players.add(player);
			}
		}
		else if(t.equalsIgnoreCase(SPECTATORS))
		{
			for(Player player : getPlayersInZone())
			{
				if(ownerClan != null && ownerClan.getClanId() == getSiegeClanId(player))
					continue;

				if(!isParticipant(ATTACKERS, player) && !isParticipant(DEFENDERS, player))
					players.add(player);
			}
		}
		// выносих всех с резиденции в город
		else if(t.equalsIgnoreCase(FROM_RESIDENCE_TO_TOWN))
		{
			for(Player player : getResidence().getZone().getInsidePlayers())
			{
				if(ownerClan != null && ownerClan.getClanId() == getSiegeClanId(player))
					continue;

				players.add(player);
			}
		}
		else
			players = getPlayersInZone();

		for(Player player : players)
		{
			Location loc;
			if(t.equalsIgnoreCase(HAVE_OWNER) || t.equalsIgnoreCase(DEFENDERS))
				loc = getResidence().getOwnerRestartPoint();
			else if(t.equalsIgnoreCase(FROM_RESIDENCE_TO_TOWN))
				loc = TeleportUtils.getRestartPoint(player, RestartType.TO_VILLAGE).getLoc();
			else
				loc = getResidence().getNotOwnerRestartPoint(player);

			player.teleToLocation(loc, ReflectionManager.MAIN);
		}
	}

	public List<Player> getPlayersInZone()
	{
		List<ZoneObject> zones = getObjects(SIEGE_ZONES);
		List<Player> result = new LazyArrayList<Player>();
		for(ZoneObject zone : zones)
			result.addAll(zone.getInsidePlayers());
		return result;
	}

	public void broadcastInZone(IBroadcastPacket... packet)
	{
		for(Player player : getPlayersInZone())
			player.sendPacket(packet);
	}

	public boolean checkIfInZone(Creature character)
	{
		List<ZoneObject> zones = getObjects(SIEGE_ZONES);
		for(ZoneObject zone : zones)
			if(zone.checkIfInZone(character))
				return true;
		return false;
	}

	public void broadcastInZone2(IBroadcastPacket... packet)
	{
		for(Player player : getResidence().getZone().getInsidePlayers())
			player.sendPacket(packet);
	}

	// ========================================================================================================================================================================
	// Siege Clans
	// ========================================================================================================================================================================
	public void loadSiegeClans()
	{
		addObjects(ATTACKERS, SiegeClanDAO.getInstance().load(getResidence(), ATTACKERS));
		addObjects(DEFENDERS, SiegeClanDAO.getInstance().load(getResidence(), DEFENDERS));
	}

	@SuppressWarnings("unchecked")
	public S newSiegeClan(String type, int clanId, long param, long date)
	{
		Clan clan = ClanTable.getInstance().getClan(clanId);
		return clan == null ? null : (S) new SiegeClanObject(this, type, clan, param, date);
	}

	public void updateParticles(boolean start, String... arg)
	{
		for(String a : arg)
		{
			List<SiegeClanObject> siegeClans = getObjects(a);
			for(SiegeClanObject s : siegeClans)
				s.setEvent(start, this);
		}
	}

	public S getSiegeClan(String name, Player player)
	{
		return getSiegeClan(name, getSiegeClanId(player));
	}

	public S getSiegeClan(String name, Clan clan)
	{
		if(clan == null)
			return null;
		return getSiegeClan(name, clan.getClanId());
	}

	@SuppressWarnings("unchecked")
	public S getSiegeClan(String name, int objectId)
	{
		if(objectId <= 0)
			return null;
		List<SiegeClanObject> siegeClanList = getObjects(name);
		if(siegeClanList.isEmpty())
			return null;
		for(int i = 0; i < siegeClanList.size(); i++)
		{
			SiegeClanObject siegeClan = siegeClanList.get(i);
			if(siegeClan.getObjectId() == objectId)
				return (S) siegeClan;
		}
		return null;
	}

	public void broadcastTo(IBroadcastPacket packet, String... types)
	{
		for(String type : types)
		{
			List<SiegeClanObject> siegeClans = getObjects(type);
			for(SiegeClanObject siegeClan : siegeClans)
				siegeClan.broadcast(packet);
		}
	}

	// ========================================================================================================================================================================
	// Override Event
	// ========================================================================================================================================================================

	@Override
	@SuppressWarnings("unchecked")
	public void initEvent()
	{
		_residence = (R) ResidenceHolder.getInstance().getResidence(getId());

		loadSiegeClans();

		clearActions();

		super.initEvent();
	}

	@Override
	public boolean ifVar(String name)
	{
		if(name.equals(HAVE_OWNER))
			return getResidence().getOwner() != null;
		if(name.equals(HAVE_OLD_OWNER))
			return _oldOwner != null;

		return false;
	}

	@Override
	public void findEvent(Player player)
	{
		if(!isInProgress())
			return;

		if(isParticipant(player))
		{
			player.addEvent(this);
		}
	}

	public boolean isParticipant(String type, Player player)
	{
		Clan clan = player.getClan();
		if(clan == null)
			return false;
		SiegeClanObject siegeClanObject = getSiegeClan(type, player);
		return siegeClanObject != null && siegeClanObject.isParticle(player);
	}

	public boolean isParticipant(Player player)
	{
		return isParticipant(ATTACKERS, player) || isParticipant(DEFENDERS, player);
	}

	public int getSiegeClanId(Player player)
	{
		return player.getClanId();
	}

	@Override
	public void checkRestartLocs(Player player, Map<RestartType, Boolean> r)
	{
		if(getObjects(FLAG_ZONES).isEmpty())
			return;

		S clan = getSiegeClan(ATTACKERS, player);
		if(clan != null && clan.getFlag() != null)
		{
			r.put(RestartType.TO_FLAG, Boolean.TRUE);
		}
	}

	@Override
	public Location getRestartLoc(Player player, RestartType type)
	{
		if(!player.getReflection().isMain())
			return null;

		if(type == RestartType.TO_FLAG)
		{
			final S attackerClan = getSiegeClan(ATTACKERS, player);
			if(!getObjects(FLAG_ZONES).isEmpty() && attackerClan != null && attackerClan.getFlag() != null)
				return Location.findPointToStay(attackerClan.getFlag(), 50, 75);
			else
			{
				if(!isParticipant(SiegeEvent.DEFENDERS, player))
				{
					TeleportTemplate teleportInfo = null;
					if(getId() == 3)
						teleportInfo = TeleportListHolder.getInstance().getTeleportInfo(422);
					else if(getId() == 7)
						teleportInfo = TeleportListHolder.getInstance().getTeleportInfo(4227);

					if(teleportInfo != null)
						return Rnd.get(teleportInfo.getLocations());
				}
			}
			player.sendPacket(SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE);
		}

		return null;
	}

	@Override
	public long getRelation(Player thisPlayer, Player targetPlayer, long result)
	{
		if(thisPlayer.containsEvent(this))
		{
			result |= RelationChangedPacket.RelationChangedType.SIEGE_PARTICIPANT.getRelationState();

			SiegeClanObject targetSiegeClan = getSiegeClan(SiegeEvent.ATTACKERS, thisPlayer.getClan());
			if(targetSiegeClan != null)
				result |= RelationChangedPacket.RelationChangedType.SIEGE_ATTACKER.getRelationState();

			if(targetPlayer.containsEvent(this))
			{
				SiegeClanObject playerSiegeClan = getSiegeClan(SiegeEvent.ATTACKERS, targetPlayer.getClan());
				if(playerSiegeClan == targetSiegeClan || playerSiegeClan != null && targetSiegeClan != null && isAttackersInAlly())
				{
					result |= RelationChangedPacket.RelationChangedType.SIEGE_ALLY.getRelationState();
					if(playerSiegeClan == targetSiegeClan)
						result |= RelationChangedPacket.RelationChangedType.SAME_PLEDGE.getRelationState();
				}
				else
				{
					result |= RelationChangedPacket.RelationChangedType.SIEGE_ENEMY.getRelationState();
				}
			}
		}
		else if(checkForAttackNewCond(targetPlayer, thisPlayer, false) != null)
			result |= RelationChangedPacket.RelationChangedType.SIEGE_ALLY.getRelationState();
		return result;
	}

	@Override
	public int getUserRelation(Player thisPlayer, int oldRelation)
	{
		oldRelation |= UserInfo.USER_RELATION_IN_SIEGE;

		SiegeClanObject siegeClan = getSiegeClan(SiegeEvent.ATTACKERS, thisPlayer);
		if(siegeClan != null)
			oldRelation |= UserInfo.USER_RELATION_ATTACKER;

		return oldRelation;
	}

	@Override
	public SystemMsg checkForAttack(Creature target, Creature attacker, Skill skill, boolean force)
	{
		if(!checkIfInZone(target) || !checkIfInZone(attacker))
			return null;

		// или вообще не учасник, или учасники разных осад
		if(!target.containsEvent(this))
			return checkForAttackNewCond(target.getPlayer(), attacker.getPlayer(), force);

		Player player = target.getPlayer();
		if(player == null)
			return null;

		SiegeClanObject siegeClan1 = getSiegeClan(SiegeEvent.ATTACKERS, player);
		if(siegeClan1 == null && attacker.isSiegeGuard())
			return SystemMsg.INVALID_TARGET;

		Player playerAttacker = attacker.getPlayer();
		if(playerAttacker == null)
			return SystemMsg.INVALID_TARGET;

		SiegeClanObject siegeClan2 = getSiegeClan(SiegeEvent.ATTACKERS, playerAttacker);
		if(this instanceof CastleSiegeEvent)
		{
			if(siegeClan1 == siegeClan2)
				return SystemMsg.YOU_CANNOT_USE_FORCED_ATTACK_ON_THE_MEMBERS_AND_MERCENARIES_OF_YOUR_OWN_CLAN; // Нельзя
			// атаковать
			// членов
			// своего
			// клана
			// и
			// наемников
			// при
			// осаде.
			if(!force && siegeClan1 != null && siegeClan2 != null && !isAttackersInAlly())
				return SystemMsg.IT_IS_POSSIBLE_TO_FORCIBLY_ATTACK_OTHER_CHARACTERS_AND_MERCENARIES_FROM_OTHER_CLAN_EVEN_IF_ITS_AN_ALLY_CLAN; // Можно
			// принудительно
			// атаковать
			// персонажей
			// и
			// наемников
			// из
			// другого
			// клана,
			// даже
			// если
			// этот
			// клан
			// союзнический.
		}

		// если оба аттакеры, и в осаде, аттакеры в Алли, невозможно бить
		if(siegeClan1 != null && siegeClan2 != null && isAttackersInAlly())
			return SystemMsg.FORCE_ATTACK_IS_IMPOSSIBLE_AGAINST_A_TEMPORARY_ALLIED_MEMBER_DURING_A_SIEGE; // Нельзя
		// атаковать
		// временных
		// союзников
		// при
		// осаде.
		// если нету как Аттакры, это дефендеры, то невозможно бить
		if(siegeClan1 == null && siegeClan2 == null)
			return SystemMsg.INVALID_TARGET;

		return null;
	}

	private SystemMsg checkForAttackNewCond(Player target, Player attacker, boolean force)
	{
		if(target == null)
			return null;
		if(attacker == null)
			return null;

		if(attacker.isInSameParty(target))
			return SystemMsg.INVALID_TARGET;
		if(attacker.isInSameChannel(target))
			return SystemMsg.INVALID_TARGET;
		if(attacker.isInSameAlly(target) && !force)
			return SystemMsg.INVALID_TARGET;
		return null;
	}

	@Override
	public boolean isInProgress()
	{
		return hasState(PROGRESS_STATE);
	}

	@Override
	public void action(String name, boolean start)
	{
		if(name.equalsIgnoreCase(REGISTRATION))
		{
			if(start)
				addState(REGISTRATION_STATE);
			else
				removeState(REGISTRATION_STATE);
		}
		else
			super.action(name, start);
	}

	public boolean isAttackersInAlly()
	{
		return false;
	}

	@Override
	public void onAddEvent(GameObject object)
	{
		if(_killListener == null)
			return;

		if(object.isPlayer())
			((Player) object).addListener(_killListener);
	}

	@Override
	public void onRemoveEvent(GameObject object)
	{
		if(_killListener == null)
			return;

		if(object.isPlayer())
			((Player) object).removeListener(_killListener);
	}

	@Override
	public List<Player> broadcastPlayers(int range)
	{
		return itemObtainPlayers();
	}

	@Override
	public EventType getType()
	{
		return EventType.SIEGE_EVENT;
	}

	@Override
	public List<Player> itemObtainPlayers()
	{
		List<Player> playersInZone = getPlayersInZone();

		List<Player> list = new LazyArrayList<Player>(playersInZone.size());
		for(Player player : getPlayersInZone())
		{
			if(player.containsEvent(this))
				list.add(player);
		}
		return list;
	}

	@Override
	public void giveItem(Player player, int itemId, long count)
	{
		super.giveItem(player, itemId, count);
	}

	public Location getEnterLoc(Player player, Zone zone) // DS: в момент вызова игрок еще не вошел в игру и с него
	// нельзя получить список
	// зон, поэтому просто передаем найденную по локации
	{
		S siegeClan = getSiegeClan(ATTACKERS, player);
		if(siegeClan != null)
		{
			if(siegeClan.getFlag() != null)
				return Location.findAroundPosition(siegeClan.getFlag(), 50, 75);
			else
				return getResidence().getNotOwnerRestartPoint(player);
		}
		else
			return getResidence().getOwnerRestartPoint();
	}

	/**
	 * Вызывается для эвента киллера и показывает может ли киллер стать ПК
	 */
	public boolean canPK(Player target, Player killer)
	{
		if(!isInProgress())
			return true; // осада еще не началась

		if(!target.containsEvent(this))
			return true; // либо вообще не участник осад, либо разные осады

		final S targetClan = getSiegeClan(SiegeEvent.ATTACKERS, target);
		final S killerClan = getSiegeClan(SiegeEvent.ATTACKERS, killer);
		if(targetClan != null && killerClan != null && isAttackersInAlly()) // оба атакующие и в альянсе
			return true;
		if(targetClan == null && killerClan == null) // оба защитники
			return true;

		return false;
	}

	// ========================================================================================================================================================================
	// Getters & Setters
	// ========================================================================================================================================================================
	public R getResidence()
	{
		return _residence;
	}

	public void addState(int b)
	{
		_state |= b;
	}

	public void removeState(int b)
	{
		_state &= ~b;
	}

	public boolean hasState(int val)
	{
		return (_state & val) == val;
	}

	public boolean isRegistrationOver()
	{
		return !hasState(REGISTRATION_STATE);
	}

	// ========================================================================================================================================================================
	public void addSiegeSummon(Player player, SummonInstance summon)
	{
		_siegeSummons.put(player.getObjectId(), new SiegeSummonInfo(summon));
	}

	public boolean containsSiegeSummon(Servitor cha)
	{
		SiegeSummonInfo siegeSummonInfo = _siegeSummons.get(cha.getPlayer().getObjectId());
		if(siegeSummonInfo == null)
			return false;
		return siegeSummonInfo._summonRef.get() == cha;
	}

	public void removeSiegeSummon(Player player, Servitor cha)
	{
		_siegeSummons.remove(player.getObjectId());
	}

	public void updateSiegeSummon(Player player, SummonInstance summon)
	{
		SiegeSummonInfo siegeSummonInfo = _siegeSummons.get(player.getObjectId());
		if(siegeSummonInfo == null)
			return;

		if(siegeSummonInfo.getSkillId() == summon.getSkillId())
		{
			summon.setSiegeSummon(true);
			siegeSummonInfo._summonRef = summon.getRef();
		}
	}

	public void despawnSiegeSummons()
	{
		for(IntObjectPair<SiegeSummonInfo> entry : _siegeSummons.entrySet())
		{
			SiegeSummonInfo summonInfo = entry.getValue();

			SummonInstance summon = summonInfo._summonRef.get();
			if(summon != null)
				summon.unSummon(false);
			else
			{
				/*
				 * TODO: CharacterServitorDAO.getInstance().delete(entry.getKey(),
				 * summonInfo._skillId, Servitor.SUMMON_TYPE);
				 * SummonDAO.getInstance().delete(entry.getKey(), summonInfo._skillId);
				 * SummonEffectDAO.getInstance().delete(entry.getKey(), summonInfo._skillId);
				 */
			}
		}

		_siegeSummons.clear();
	}
}
