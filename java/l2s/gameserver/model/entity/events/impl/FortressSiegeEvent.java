package l2s.gameserver.model.entity.events.impl;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.dao.JdbcEntityState;
import l2s.gameserver.data.xml.holder.EventHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2s.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.model.entity.events.objects.FortressCombatFlagObject;
import l2s.gameserver.model.entity.events.objects.SiegeClanObject;
import l2s.gameserver.model.entity.events.objects.StaticObjectObject;
import l2s.gameserver.model.entity.events.objects.ZoneObject;
import l2s.gameserver.model.entity.residence.Fortress;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.mail.Mail;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExAdenFortressSiegeHUDInfo;
import l2s.gameserver.network.l2.s2c.ExNoticePostArrived;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.network.l2.s2c.ExUnReadMailCount;
import l2s.gameserver.network.l2.s2c.PlaySoundPacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ItemFunctions;

public class FortressSiegeEvent extends SiegeEvent<Fortress, SiegeClanObject>
{
	public static final String FLAG_POLE = "flag_pole";
	public static final int PREPARE_STATE = (1 << 2) | REGISTRATION_STATE;
	private static final String COMBAT_FLAGS = "combat_flags";
	private static final String PREPARE = "prepare";
	private final GlobalEventListeners globalEventListeners = new GlobalEventListeners();
	private final SiegeZoneListener siegeZoneListener = new SiegeZoneListener();
	private final int siegeDuration;
	private final long flagDisplayReward;
	private Player flagDisplayer = null;

	public FortressSiegeEvent(MultiValueSet<String> set)
	{
		super(set);
		siegeDuration = set.getInteger("duration");
		flagDisplayReward = set.getLong("flag_display_reward", 0);
	}

	public int getSiegeDuration()
	{
		return siegeDuration;
	}

	public boolean isInPrepare()
	{
		return hasState(PREPARE_STATE);
	}

	public synchronized void displayFlag(Player player)
	{
		if(flagDisplayer != null)
		{ return; }

		Clan clan = player.getClan();
		if((clan == null) || (clan.getCastle() != 0))
		{ return; }

		flagDisplayer = player;

		broadcastInZone(new SystemMessagePacket(SystemMsg.S1_DISPLAYED_THE_FLAG_SUCCESSFULLY).addName(player));
		processStep(clan);
	}

	@Override
	public void processStep(Clan newOwnerClan)
	{
		if(newOwnerClan.getCastle() != 0)
		{ return; }
		getResidence().changeOwner(newOwnerClan);
		stopEvent(true);
	}

	@Override
	public void initEvent()
	{
		super.initEvent();
		flagPoleUpdate(false);
	}

	private void startPrepare()
	{
		if(!canStartSiege())
		{ return; }
		addState(PREPARE_STATE);
		broadcastToWorld(new ExAdenFortressSiegeHUDInfo(this));
		CharListenerList.addGlobal(globalEventListeners);
		actActions("start_prepare");
	}

	@Override
	public void startEvent()
	{
		removeState(PREPARE_STATE);

		if(!canStartSiege())
		{ return; }

		CharListenerList.addGlobal(globalEventListeners);

		_oldOwner = getResidence().getOwner();

		getResidence().changeOwner(null);

		for(ZoneObject zoneObject : getObjects(SIEGE_ZONES, ZoneObject.class))
		{
			zoneObject.getZone().addListener(siegeZoneListener);
		}

		super.startEvent();

		flagPoleUpdate(true);

		broadcastToWorld(new ExAdenFortressSiegeHUDInfo(this));
		broadcastInZone(new SystemMessagePacket(SystemMsg.THE_FORTRESS_BATTLE_HAS_BEGUN));
		broadcastInZone(new ExShowScreenMessage(NpcString.YOU_SHOULDNT_SAY_IT_ALL_FORCES_ATTACK, 10000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, true, false));
	}

	@Override
	public void stopEvent(boolean force)
	{
		CharListenerList.removeGlobal(globalEventListeners);

		broadcastInZone(new SystemMessagePacket(SystemMsg.THE_FORTRESS_BATTLE_IS_OVER));

		Clan ownerClan = getResidence().getOwner();
		if(ownerClan != null)
		{
			ownerClan.broadcastToOnlineMembers(PlaySoundPacket.SIEGE_VICTORY);
			broadcastInZone(new SystemMessagePacket(SystemMsg.S1_IS_VICTORIOUS_IN_THE_FORTRESS_BATTLE_OF_S2).addString(ownerClan.getName()).addResidenceName(getResidence()));
			getResidence().getOwnDate().setTimeInMillis(System.currentTimeMillis());
			getResidence().startCycleTask();
		}
		else
		{
			getResidence().getOwnDate().setTimeInMillis(0);
		}

		getResidence().getLastSiegeDate().setTimeInMillis(System.currentTimeMillis());

		spawnAction(COMBAT_FLAGS, false);

		flagPoleUpdate(false);

		if(flagDisplayer != null)
		{
			sendRewardMail(flagDisplayer);
			flagDisplayer = null;
		}

		super.stopEvent(force);

		for(ZoneObject zoneObject : getObjects(SIEGE_ZONES, ZoneObject.class))
		{
			zoneObject.getZone().removeListener(siegeZoneListener);
		}

		broadcastToWorld(new ExAdenFortressSiegeHUDInfo(this));
	}

	@Override
	public void announce(int id, String value, int time)
	{
		if(id == 1)
		{
			broadcastInZone(new SystemMessagePacket(SystemMsg.THE_FORTRESS_BATTLE_WILL_START_IN_S1_MIN).addInteger(Integer.parseInt(value)));
		}
		else if(id == 2)
		{
			broadcastInZone(new SystemMessagePacket(SystemMsg.THE_FORTRESS_BATTLE_WILL_BE_OVER_IN_S1_MIN).addInteger(Integer.parseInt(value)));
		}
		else if(id == 3)
		{
			broadcastInZone(new ExShowScreenMessage(NpcString.FLAG_SENTRY_GREG_HAS_APPEARED, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, true));
		}
	}

	public void flagPoleUpdate(boolean dis)
	{
		StaticObjectObject object = getFirstObject(FLAG_POLE);
		if(object != null)
		{
			object.setMeshIndex(dis ? 0 : (getResidence().getOwner() != null ? 1 : 0));
		}
	}

	@Override
	public boolean canResurrect(Creature active, Creature target, boolean force, boolean quiet)
	{
		return !target.isInZoneBattle();
	}

	@Override
	public long getRelation(Player thisPlayer, Player targetPlayer, long result)
	{
		return 0;
	}

	@Override
	public int getUserRelation(Player thisPlayer, int oldRelation)
	{
		return 0;
	}

	@Override
	public SystemMsg checkForAttack(Creature target, Creature attacker, Skill skill, boolean force)
	{
		if(target.isPlayable() && attacker.isPlayable())
		{
			if(!target.isInZoneBattle() || !attacker.isInZoneBattle())
			{ return SystemMsg.INVALID_TARGET; }
			return null;
		}
		return super.checkForAttack(target, attacker, skill, force);
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
				warn("Cannot stop prepare in fortress siege event ID: " + getId());
			}
		}
		else
		{
			super.action(name, start);
		}
	}

	@Override
	public boolean isParticipant(String type, Player player)
	{
		return type.equals(ATTACKERS) && player.containsEvent(this);
	}

	private boolean canStartSiege()
	{
		for(FortressSiegeEvent siegeEvent : EventHolder.getInstance().getEvents(getClass()))
		{
			if((siegeEvent != this) && (siegeEvent.isInPrepare() || siegeEvent.isInProgress()))
			{
				warn("Cannot start two or more fortress sieges in same time! Please change siege time for fortress siege ID: " + getId());
				return false;
			}
		}
		return true;
	}

	public synchronized void dropFlag(NpcInstance npc)
	{
		List<FortressCombatFlagObject> combatFlags = getObjects(COMBAT_FLAGS, FortressCombatFlagObject.class);
		for(FortressCombatFlagObject flagObject : combatFlags)
		{
			if(flagObject.dropFlag(this, npc, Location.findPointToStay(npc, 100, 200), getReflection()))
			{
				break;
			}
		}
	}

	private void sendRewardMail(Player receiver)
	{
		if(flagDisplayReward <= 0)
		{ return; }

		Mail mail = new Mail();
		mail.setSenderId(receiver.getObjectId());
		mail.setSenderName(receiver.getName());
		mail.setReceiverId(receiver.getObjectId());
		mail.setReceiverName(receiver.getName());
		mail.setTopic(Mail.FORTRESS_REWARD_TOPIC);
		mail.setBody(null);
		mail.setPrice(0L);
		mail.setUnread(true);
		mail.setType(Mail.SenderType.NEWS_INFORMER); // TODO: Подобрать правильный тип.
		mail.setExpireTime((int) (TimeUnit.DAYS.toSeconds(365) + (System.currentTimeMillis() / 1000L))); // Ставим год
		// (На оффе
		// 15 дней,
		// но через
		// 15 дней
		// письмо не
		// пропадает).
		mail.setSystemTopic(SystemMsg.A_LETTER_FROM_THE_FORTRESS_MANAGER_ARRIVED);
		mail.setSystemBody(SystemMsg.THANK_YOU_FOR_RECAPTURING_THE_FORTRESS_INVADED_BY_ORCS_HERE_IS_YOUR_REWARD_FOR_DISPLAYING_A_FLAG_FORTRESS_MANAGER);

		ItemInstance adenaItem = ItemFunctions.createItem(ItemTemplate.ITEM_ID_ADENA);
		Objects.requireNonNull(adenaItem);
		adenaItem.setOwnerId(receiver.getObjectId());
		adenaItem.setCount(flagDisplayReward);
		adenaItem.setLocation(ItemLocation.MAIL);
		adenaItem.setJdbcState(JdbcEntityState.UPDATED);
		adenaItem.update();

		mail.addAttachment(adenaItem);
		mail.save();

		receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
		receiver.sendPacket(new ExUnReadMailCount(receiver));
		receiver.sendPacket(SystemMsg.THE_MAIL_HAS_ARRIVED);
	}

	private class GlobalEventListeners implements OnPlayerEnterListener
	{
		@Override
		public void onPlayerEnter(Player player)
		{
			if(isInPrepare() || isInProgress())
			{
				player.sendPacket(new ExAdenFortressSiegeHUDInfo(FortressSiegeEvent.this));
			}
		}
	}

	private class SiegeZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature actor)
		{
			if(actor.isPlayer())
			{
				actor.addEvent(FortressSiegeEvent.this);
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature actor)
		{
			if(actor.isPlayer())
			{
				if(checkIfInZone(actor))
				{ return; }
				actor.removeEvent(FortressSiegeEvent.this);
			}
		}
	}
}
