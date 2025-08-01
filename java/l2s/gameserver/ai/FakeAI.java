package l2s.gameserver.ai;

import static l2s.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;
import static l2s.gameserver.ai.CtrlIntention.AI_INTENTION_ATTACK;
import static l2s.gameserver.ai.CtrlIntention.AI_INTENTION_PICK_UP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import org.napile.primitive.lists.IntList;
import org.napile.primitive.sets.IntSet;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.dao.CharacterDAO;
import l2s.gameserver.data.xml.holder.FakePlayersHolder;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.listener.actor.OnDeathListener;
import l2s.gameserver.listener.actor.player.OnLevelChangeListener;
import l2s.gameserver.listener.actor.player.OnPlayerChatMessageReceive;
import l2s.gameserver.listener.actor.player.OnTeleportListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.base.RestartType;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.EffectUseType;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.skills.enums.SkillType;
import l2s.gameserver.templates.fakeplayer.FakePlayerAITemplate;
import l2s.gameserver.templates.fakeplayer.FarmZoneTemplate;
import l2s.gameserver.templates.fakeplayer.TownZoneTemplate;
import l2s.gameserver.templates.fakeplayer.actions.AbstractAction;
import l2s.gameserver.templates.fakeplayer.actions.GoToTownActions;
import l2s.gameserver.templates.fakeplayer.actions.OrdinaryActions;
import l2s.gameserver.templates.fakeplayer.actions.ReviveAction;
import l2s.gameserver.templates.fakeplayer.actions.StopFarmAction;
import l2s.gameserver.templates.fakeplayer.actions.TeleportToClosestTownAction;
import l2s.gameserver.templates.fakeplayer.actions.UseCommunityAction;
import l2s.gameserver.templates.fakeplayer.actions.WaitAction;
import l2s.gameserver.utils.FakePlayerUtils;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.PositionUtils;
import l2s.gameserver.utils.TeleportUtils;

/**
 * @author Bonux
 **/
public class FakeAI extends PlayerAI implements OnDeathListener, OnLevelChangeListener, OnTeleportListener, OnPlayerChatMessageReceive
{
	private class DistanceComparator implements Comparator<GameObject>
	{
		@Override
		public int compare(GameObject o1, GameObject o2)
		{
			Player player = getActor();
			if(player != null)
				return Integer.compare(o1.getDistance(player), o2.getDistance(player));
			return 0;
		}
	}

	private static final int MAX_ACTION_TRY_COUNT = 500;
	private static final int BUFF_DELAY = 600000; // 10 минут.
	private static final int CHECK_INVENTORY_DELAY = 10000; // 10 сек.
	private static final int SEARCH_PVP_PK_DELAY = 60000; // 1 минута.
	private static final int ATTACK_WAIT_DELAY = 180000; // 3 минуты.
	private static final int SHOUT_CHAT_MIN_DELAY = 60000; // 1 минута.
	private static final int SHOUT_CHAT_MAX_DELAY = 600000; // 10 минут.

	private final FakePlayerAITemplate _aiTemplate;

	private final DistanceComparator _distanceComparator = new DistanceComparator();

	private ScheduledFuture<?> _actionTask;

	private final List<AbstractAction> _plannedActions = new ArrayList<AbstractAction>();
	private int _lastActionTryCount = 0;

	private FarmZoneTemplate _currentFarmZone = null;

	private long _waitEndTime = 0L;
	private long _goToTownTime = -1L;
	private long _lastBuffTime = 0L;
	private long _lastCheckInventoryTime = 0L;
	private long _lastSearchPvPPKTime = 0L;
	private long _lastAttackTime = 0L;
	private long _nextShoutChatTime = 0L;

	private IntList todoEquip = null;

	private final AtomicBoolean deleted = new AtomicBoolean();

	public FakeAI(Player player, FakePlayerAITemplate aiTemplate)
	{
		super(player);
		_aiTemplate = aiTemplate;
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();

		Player actor = getActor();

		actor.addListener(this);

		if(actor.entering)
		{
			if(actor.getOnlineTime() == 0)
				planActions(_aiTemplate.getOnCreateAction());
		}

		actor.setActive();

		startActionTask();

		FakePlayerUtils.checkAutoShots(this);
	}

	@Override
	public void onEvtDeSpawn()
	{
		getActor().removeListener(this);
		stopActionTask();
		super.onEvtDeSpawn();
	}

	@Override
	public boolean isFake()
	{
		return true;
	}

	public void startWait(int minDelay, int maxDelay)
	{
		_waitEndTime = System.currentTimeMillis() + Rnd.get(minDelay, maxDelay);
	}

	public void stopWait()
	{
		_waitEndTime = 0;
	}

	private boolean isWait()
	{
		return _waitEndTime > System.currentTimeMillis() || getIntention() == AI_INTENTION_PICK_UP;
	}

	private boolean planActions(OrdinaryActions action)
	{
		clearPlannedActions();

		if(action == null)
			return false;

		List<AbstractAction> actions = makeActionsList(action.makeActionsList());
		if(actions.isEmpty())
			return false;

		// TODO: Проверку на количество действий
		_plannedActions.addAll(actions);
		return true;
	}

	private List<AbstractAction> makeActionsList(List<AbstractAction> actions)
	{
		if(actions.isEmpty())
			return Collections.emptyList();

		List<AbstractAction> actionsList = new ArrayList<AbstractAction>();
		for(AbstractAction action : actions)
		{
			double chance = action.getChance();
			if(chance <= 0 || chance >= 100 || Rnd.chance(chance))
			{
				List<AbstractAction> tempList = action.makeActionsList();
				if(tempList == null)
					actionsList.add(action);
				else
					actionsList.addAll(makeActionsList(tempList));
			}
		}
		return actionsList;
	}

	public void clearPlannedActions()
	{
		_plannedActions.clear();
		_lastActionTryCount = 0;
	}

	public boolean clearCurrentFarmZone()
	{
		boolean result = _currentFarmZone != null;
		getActor().setTarget(null);
		_currentFarmZone = null;
		_goToTownTime = -1L;
		return result;
	}

	private synchronized boolean performNextAction(boolean force)
	{
		if(deleted.get())
			return false;

		if(_nextShoutChatTime < System.currentTimeMillis())
		{
			FakePlayerUtils.writeToRandomChat(this);
			_nextShoutChatTime = System.currentTimeMillis() + Rnd.get(SHOUT_CHAT_MIN_DELAY, SHOUT_CHAT_MAX_DELAY);
		}

		if(isWait())
			return false;

		Player player = getActor();

		if(!player.isAlikeDead())
		{
			IntList todoEquip = this.todoEquip;
			if(todoEquip != null && !todoEquip.isEmpty())
			{
				int itemId = todoEquip.removeByIndex(0);
				if(FakePlayerUtils.addEquip(this, itemId))
				{
					startWait(100, 1000);
					return true;
				}
			}

			if((_lastCheckInventoryTime + CHECK_INVENTORY_DELAY) < System.currentTimeMillis())
			{
				FakePlayerUtils.checkInventory(this);
				_lastCheckInventoryTime = System.currentTimeMillis();
			}

			int dropCount = 0;
			ItemInstance dropItem = null;
			if(Rnd.chance(player.isInPeaceZone() ? 3 : 97))
			{
				for(GameObject object : World.getAroundObjects(player, 2000, 1000))
				{
					if(object instanceof ItemInstance)
					{
						ItemInstance item = (ItemInstance) object;
						if(item.getItemId() != 8190 && item.getItemId() != 8689) // Не подымаем проклятое оружие.
						{
							if(player.getDistance(item) > 10000
									|| !GeoEngine.canMoveToCoord(player.getX(), player.getY(), player.getZ(), item.getX(), item.getY(), item.getZ(), player.getGeoIndex()))
								continue;

							if(ItemFunctions.checkIfCanPickup(player, item))
							{
								if(dropItem == null || player.getDistance(item) < player.getDistance(dropItem))
									dropItem = item;
								dropCount++;
							}
						}
					}
				}
			}

			GameObject target = player.getTarget();
			if((target instanceof Creature) && Rnd.chance(95))
			{
				Creature creatureTarget = (Creature) target;
				if(!player.isInPeaceZone() || creatureTarget.isMonster())
				{
					boolean attackable = creatureTarget.isAutoAttackable(player);

					Player targetPlayer = creatureTarget.getPlayer();
					if(targetPlayer != null)
						attackable = targetPlayer.isCtrlAttackable(player, (targetPlayer.isPK() || targetPlayer.getPvpFlag() > 0), false);

					if(!attackable || creatureTarget.isAlikeDead() || !creatureTarget.isVisible() || creatureTarget.isInvisible(player)
							|| player.getDistance(creatureTarget) > 10000
							|| !GeoEngine.canMoveToCoord(player.getX(), player.getY(), player.getZ(), creatureTarget.getX(), creatureTarget.getY(), creatureTarget.getZ(), player.getGeoIndex()))
					{
						player.setTarget(null);
						player.abortAttack(true, false);
						player.abortCast(true, false);
						startWait(100, dropCount == 0 ? 2000 : 700);
						return true;
					}
					else if(player.getAI().getAttackTarget() != creatureTarget && player.getAI().getCastTarget() != creatureTarget
							|| getIntention() != AI_INTENTION_ATTACK || Rnd.chance(5))
					{
						attack(creatureTarget);
						return true;
					}
				}
			}

			if(_plannedActions.isEmpty() && getIntention() == AI_INTENTION_ACTIVE || Rnd.chance(5))
			{
				if(dropItem != null && !player.getMovement().isMoving() && !player.isMovementDisabled())
				{
					dropItem.onAction(player, false);
					startWait(500, dropCount == 1 ? 3000 : 1000);
					return true;
				}

				if(!player.isInPeaceZone())
				{
					if((_lastSearchPvPPKTime + SEARCH_PVP_PK_DELAY) < System.currentTimeMillis())
					{
						for(Creature pk : player.getAroundCharacters(1000, 250))
						{
							if(pk.isPlayer())
							{
								Player targetPlayer = pk.getPlayer();
								if(!targetPlayer.isAlikeDead() && targetPlayer.isVisible() && !targetPlayer.isInvisible(player)
										&& player.getDistance(targetPlayer) <= 10000
										&& GeoEngine.canMoveToCoord(player.getX(), player.getY(), player.getZ(), pk.getX(), pk.getY(), pk.getZ(), player.getGeoIndex()))
								{
									double attackChance = 0;

									boolean targeted = targetPlayer.getAI().getAttackTarget() == player && targetPlayer.getAI().getCastTarget() == player;
									boolean attackable = targetPlayer.isCtrlAttackable(player, false, false);
									if(attackable)
									{
										attackChance = 20;
										if(!targeted)
											attackChance /= 5;
									}
									else
									{
										attackable = targetPlayer.isCtrlAttackable(player, true, false);
										if(attackable)
										{
											attackChance = 5;
											if(!targeted)
												attackChance /= 5;
										}
									}

									if(Rnd.chance(attackChance))
									{
										player.setTarget(targetPlayer);
										startWait(1000, 3000);
										return true;
									}
								}
							}
						}
						_lastSearchPvPPKTime = System.currentTimeMillis();
					}
				}
			}

			if((_lastBuffTime + BUFF_DELAY) < System.currentTimeMillis())
			{
				// TODO: Переделать.
				if(player.getClassId().isOfRace(Race.ORC) || !player.isMageClass())
					_plannedActions.add(new UseCommunityAction("_cbbsbuffer get 0 1_0 1", 100));
				else
					_plannedActions.add(new UseCommunityAction("_cbbsbuffer get 0 1_0 2", 100));

				_lastBuffTime = System.currentTimeMillis();
			}
		}

		if(!_plannedActions.isEmpty())
		{
			AbstractAction action = _plannedActions.get(0);

			_lastActionTryCount++;
			if(_lastActionTryCount <= MAX_ACTION_TRY_COUNT) // Заглушка, чтобы боты не зависали.
			{
				if(!action.checkCondition(this, force))
					return false;

				if(!action.performAction(this))
					return false;
			}
			else
				_lastActionTryCount = 0;

			_plannedActions.remove(action);
		}
		else if(player.isDead())
		{
			doRevive();
		}
		else if(_currentFarmZone != null)
		{
			farm();
		}
		else
		{
			Location closestTownLoc = TeleportUtils.getRestartPoint(player, RestartType.TO_VILLAGE).getLoc();

			TownZoneTemplate townZone = null;
			loop1:
			for(TownZoneTemplate t : FakePlayersHolder.getInstance().getTownZones())
			{
				for(Zone zone : t.getZones())
				{
					if(zone.checkIfInZone(closestTownLoc.x, closestTownLoc.y, closestTownLoc.z))
					{
						townZone = t;
						break loop1;
					}
				}
			}

			if(townZone != null)
			{
				for(Zone zone : townZone.getZones())
				{
					if(zone.checkIfInZone(player))
					{
						planActions(townZone.getActions());
						return true;
					}
				}
			}

			FarmZoneTemplate farmZone = null;
			loop2:
			for(FarmZoneTemplate f : _aiTemplate.getFarmZones())
			{
				for(Zone zone : f.getZones())
				{
					if(zone.checkIfInZone(player))
					{
						if(farmZone == null || (player.getLevel() - farmZone.getMaxLevel()) > (player.getLevel() - f.getMaxLevel()))
						{
							farmZone = f;

							if(farmZone.checkCondition(player))
								break loop2;
						}
					}
				}
			}

			if(farmZone != null)
			{
				if(!farmZone.checkCondition(player))
				{
					if(player.getLevel() >= farmZone.getMaxLevel())
						planActions(farmZone.getOnObtainMaxLevelAction());
					else
					{
						// TODO: Сделать список глобальных действий и вынести в датапак.
						_plannedActions.add(new TeleportToClosestTownAction(100));
					}
				}
				else
				{
					_currentFarmZone = farmZone;
					return performFarm();
				}
			}
			else
			{
				if(townZone == null)
					return performFarm();

				// TODO: Сделать список глобальных действий и вынести в датапак.
				_plannedActions.add(new TeleportToClosestTownAction(100));
			}
		}
		return true;
	}

	public boolean performFarm()
	{
		Player player = getActor();

		if(_currentFarmZone == null)
		{
			List<FarmZoneTemplate> availableFarmZones = new ArrayList<FarmZoneTemplate>();
			loop:
			for(FarmZoneTemplate f : _aiTemplate.getFarmZones()) // Выбираем подходящие зоны, в которых уже
			// находимся.
			{
				if(!f.checkCondition(player))
					continue;

				for(Zone zone : f.getZones())
				{
					if(!zone.checkIfInZone(player))
						continue;

					availableFarmZones.add(f);
					break loop;
				}
			}

			if(availableFarmZones.isEmpty())
			{
				for(FarmZoneTemplate f : _aiTemplate.getFarmZones())
				{
					if(f.checkCondition(player))
						availableFarmZones.add(f);
				}
			}

			FarmZoneTemplate farmZone = availableFarmZones.isEmpty() ? null : Rnd.get(availableFarmZones);
			if(farmZone == null)
			{
				deleteFake(player);
				// _log.warn("Cannot find farm zone from player RACE[" + player.getRace() + "],
				// TYPE[" + player.getClassId().getType() + "], CLASS[" + player.getClassId() +
				// "], LEVEL[" + player.getLevel() + "]!");
				return false;
			}

			_currentFarmZone = farmZone;
			_lastAttackTime = System.currentTimeMillis();
		}

		clearPlannedActions();
		return true;
	}

	private void farm()
	{
		if(isWait())
			return;

		Player player = getActor();

		if(player.getMovement().isMoving() || player.isMovementDisabled())
			return;

		if(!_currentFarmZone.checkCondition(player))
		{
			FarmZoneTemplate currentFarmZone = _currentFarmZone;
			clearCurrentFarmZone();
			if(player.getLevel() >= currentFarmZone.getMaxLevel())
				planActions(currentFarmZone.getOnObtainMaxLevelAction());
			else
				clearPlannedActions();
			return;
		}

		GoToTownActions goToTownActions = _currentFarmZone.getGoToTownActions();
		if(goToTownActions != null)
		{
			if(_goToTownTime == -1L)
			{
				_goToTownTime = System.currentTimeMillis() + (Rnd.get(goToTownActions.getMinFarmTime(), goToTownActions.getMaxFarmTime()) * 1000L);
			}
			else if(_goToTownTime < System.currentTimeMillis())
			{
				_goToTownTime = Long.MAX_VALUE;
				planActions(goToTownActions);
				_plannedActions.add(new StopFarmAction(100.));
				return;
			}
		}

		List<NpcInstance> npcs = new ArrayList<NpcInstance>();

		for(Zone zone : _currentFarmZone.getZones())
		{
			npcs.addAll(getNpcsForAttack(zone.getInsideNpcs()));
		}

		Collections.sort(npcs, _distanceComparator);

		for(NpcInstance npc : npcs)
		{
			if(prepareAttack(npc))
				return;
		}

		List<NpcInstance> arroundNpcs = getNpcsForAttack(player.getAroundNpc(2000, 1000));
		Collections.sort(arroundNpcs, _distanceComparator);

		for(NpcInstance npc : arroundNpcs)
		{
			if(prepareAttack(npc))
				return;
		}

		NpcInstance npc = npcs.isEmpty() ? null : npcs.get(0);

		if((_lastAttackTime + ATTACK_WAIT_DELAY) < System.currentTimeMillis())
		{
			_lastAttackTime = System.currentTimeMillis();

			Location loc = npc != null ? npc.getLoc() : getRandomLoc(_currentFarmZone.getZones(), player.getGeoIndex(), player.isFlying());
			player.teleToLocation(loc, 0, 0);
			return;
		}

		if(npc != null || !isInside(_currentFarmZone.getZones(), player.getX(), player.getY(), player.getZ()))
		{
			Location loc = npc != null ? npc.getLoc() : getRandomLoc(_currentFarmZone.getZones(), player.getGeoIndex(), player.isFlying());
			if(player.getDistance(loc) > 10000
					|| !player.getMovement().moveToLocation(Location.findAroundPosition(loc, 0, player.getGeoIndex()), 0, true, 50))
			{
				Location restartLoc = Rnd.get(_currentFarmZone.getSpawnPoints());
				if(!isInside(_currentFarmZone.getZones(), restartLoc.x, restartLoc.y, restartLoc.z))
				{
					// TODO: Придумать алгоритм поиска ближайшей точки зоны к заданой точке и
					// измерять дистанцию от нее.
					if(PositionUtils.calculateDistance(restartLoc.x, restartLoc.y, loc.x, loc.y) > 10000)
					{
						// _log.warn("PK restart point for farm zone \"" + zoneTemplate.getName() + "\"
						// is long away!");
						restartLoc = null;
					}
				}
				if(restartLoc == null)
					restartLoc = loc;
				if(player.isInRange(restartLoc, 50))
					restartLoc = loc;
				if(!player.isInRange(restartLoc, 50))
				{
					if(player.getDistance(restartLoc) > 10000
							|| !player.getMovement().moveToLocation(Location.findAroundPosition(restartLoc, 50, 150, player.getGeoIndex()), 0, true, 50))
						player.teleToLocation(restartLoc, 0, 0);
					return;
				}
			}
			else
				return;
		}

		Location loc = Location.coordsRandomize(player.getLoc(), 100, 300);
		if(isInside(_currentFarmZone.getZones(), loc.x, loc.y, loc.z)
				&& player.getMovement().moveToLocation(Location.findAroundPosition(loc, 0, player.getGeoIndex()), 0, true, 50))
			startWait(1000, 10000);
	}

	private List<NpcInstance> getNpcsForAttack(List<NpcInstance> avaialbleNpcs)
	{
		Player player = getActor();

		List<NpcInstance> npcs = new ArrayList<NpcInstance>();
		for(NpcInstance n : avaialbleNpcs)
		{
			if(n.isAlikeDead())
				continue;

			if(n.isInvulnerable())
				continue;

			if(!n.isVisible())
				continue;

			if(n.isInvisible(player))
				continue;

			if(_currentFarmZone.isIgnoredMonster(n.getNpcId()))
				continue;

			IntSet farmMonsters = _currentFarmZone.getFarmMonsters();
			if(farmMonsters.isEmpty())
			{
				if(Math.abs(player.getLevel() - n.getLevel()) > 10)
					continue;
			}
			else if(!farmMonsters.contains(n.getNpcId()))
				continue;

			if(!n.isMonster() || n.isRaid())
				continue;

			if(n.getAI().getAttackTarget() != null && n.getAI().getAttackTarget() != player
					|| n.getAI().getCastTarget() != null && n.getAI().getCastTarget() != player)
			{
				if(Rnd.chance(95))
					continue;
			}

			npcs.add(n);
		}
		return npcs;
	}

	private boolean prepareAttack(Creature target)
	{
		Player player = getActor();

		if(player.getDistance(target) <= 10000
				&& GeoEngine.canMoveToCoord(player.getX(), player.getY(), player.getZ(), target.getX(), target.getY(), target.getZ(), player.getGeoIndex()))
		{
			if(Rnd.chance(80))
			{
				int distanceToTarget = player.getDistance(target);
				for(Creature neighbor : target.getAroundCharacters(distanceToTarget + 5000, 250))
				{
					if(!neighbor.isFakePlayer())
						continue;

					if(neighbor.getTarget() != target && neighbor.getAI().getAttackTarget() != target && neighbor.getAI().getCastTarget() != target)
						continue;

					return false;
				}
			}

			if(Rnd.chance(20))
			{
				player.setCurrentHp(player.getMaxHp(), true, true);
				player.setCurrentMp(player.getMaxMp());
			}

			if(Rnd.chance(10))
			{
				if(player.getClassId().isOfRace(Race.ORC) || !player.isMageClass())
					player.getMovement().moveToLocation(Location.findAroundPosition(target, 80, 180), 0, true);
				else
					player.getMovement().moveToLocation(Location.findAroundPosition(target, 160, 360), 0, true);
				player.setTarget(target);
				startWait(500, 3000);
				return true;
			}

			_lastAttackTime = System.currentTimeMillis();

			player.setTarget(target);
			startWait(1000, 3000);
			return true;
		}
		return false;
	}

	private void attack(Creature target)
	{
		Player player = getActor();
		/*
		 * if(Rnd.chance(1)) { player.setTarget(null); player.abortAttack(true, false);
		 * player.abortCast(true, false);
		 * player.getMovement().moveToLocation(Location.findAroundPosition(player.getLoc
		 * (), 500, 1000, player.getGeoIndex()), 250, true); return; }
		 */

		if(Rnd.chance(80))
		{
			if(tryRunOff(target))
				return;
		}

		if(Rnd.chance(5))
		{
			Skill skill = getRandomSkillSelf();
			if(skill != null)
			{
				Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skill), player);
				return;
			}
		}

		if(!GeoEngine.canSeeTarget(player, target))
		{
			if(!player.getMovement().isMoving())
			{
				if(!player.getMovement().moveToLocation(Location.findAroundPosition(target, 50, 150), 0, true, 50))
					player.setTarget(null);
				return;
			}
		}

		Skill skill = getRandomSkill(player, target);
		if(skill == null && player.isMageClass() && !player.getClassId().isOfRace(Race.ORC) && Rnd.chance(90))
			return;

		if(skill != null)
		{
			if(Rnd.chance(30))
			{
				Cast(SkillEntry.makeSkillEntry(SkillEntryType.NONE, skill), target, false, false);
				return;
			}
		}

		if(player.getClassId().isOfRace(Race.ORC) || !player.isMageClass())
			Attack(target, true, false);
	}

	private boolean tryRunOff(Creature target)
	{
		if(target.isAlikeDead())
			return false;

		Player player = getActor();
		if(player.getPhysicalAttackRange() > 100 || (!player.getClassId().isOfRace(Race.ORC) && player.isMageClass()))
		{
			if(player.getDistance(target) <= 200 && !player.getMovement().isMoving())
			{
				int posX = player.getX();
				int posY = player.getY();
				int posZ = player.getZ();

				int old_posX = posX;
				int old_posY = posY;
				int old_posZ = posZ;

				int signx = posX < target.getX() ? -1 : 1;
				int signy = posY < target.getY() ? -1 : 1;

				int range = Math.max((int) (player.getPhysicalAttackRange() * 0.9), 200);

				posX += signx * range;
				posY += signy * range;
				posZ = GeoEngine.getLowerHeight(posX, posY, posZ, player.getGeoIndex());

				if(GeoEngine.canMoveToCoord(old_posX, old_posY, old_posZ, posX, posY, posZ, player.getGeoIndex()))
				{
					player.abortAttack(true, false);
					if(player.getMovement().moveToLocation(Location.findAroundPosition(posX, posY, posZ, 0, 0, player.getGeoIndex()), 0, true))
						return true;
				}
			}
		}
		return false;
	}

	private Skill getRandomSkillSelf()
	{
		List<Skill> skills = new ArrayList<>();
		loop:
		for(SkillEntry skillEntry : getActor().getAllSkills())
		{
			Skill skill = skillEntry.getTemplate();
			if(!skill.isActive() && !skill.isToggle())
				continue;

			if(skill.hasEffect(EffectUseType.NORMAL, "Transformation"))
				continue;

			if(getActor().isSkillDisabled(skill))
				continue;

			if(skill.getSkillType() == SkillType.BUFF)
			{
				for(Abnormal e : getActor().getAbnormalList())
				{
					if(checkAbnormal(e, skill))
						continue loop;
				}

				switch(skill.getTargetType())
				{
					case TARGET_ONE:
					case TARGET_SELF:
						skills.add(skill);
						break;
				}
			}
		}
		return skills.isEmpty() ? null : skills.get(Rnd.get(skills.size()));
	}

	/**
	 * Возвращает true если эффект для скилла уже есть и заново накладывать не надо
	 */
	private boolean checkAbnormal(Abnormal abnormal, Skill skill)
	{
		return !abnormal.canReplaceAbnormal(skill, 10);
	}

	private Skill getRandomSkill(Player player, Creature target)
	{
		List<Skill> weakSkills = new ArrayList<>();
		List<Skill> skills = new ArrayList<>();
		for(SkillEntry skillEntry : player.getAllSkills())
		{
			Skill skill = skillEntry.getTemplate();
			if(!skill.isActive())
				continue;

			switch(skill.getId())
			{
				case 11030:
				case 30546:
				case 30547:
					continue;
			}

			if(player.isSkillDisabled(skill))
				continue;

			if(!skillEntry.checkCondition(player, target, false, false, true))
				continue;

			double chance = 0;

			switch(skill.getSkillType())
			{
				case DEBUFF:
				case PARALYZE:
				case ROOT:
				case STEAL_BUFF:
				case DOT:
				case AIEFFECTS:
				case CPDAM:
				case DELETE_HATE:
				case MDOT:
				case DECOY:
				case CHARGE:
				case POISON:
				case SLEEP:
				case DESTROY_SUMMON:
				case SHIFT_AGGRESSION:
				case DISCORD:
				case MANADAM:
				case MUTE:
					chance = 5.;
					break;
				case DRAIN:
					chance = (5. + ((100. - getActor().getCurrentCpPercents()) / 5.)) / (player.isMageClass() ? 1. : 3.);
					break;
				case MDAM:
					chance = !player.getClassId().isOfRace(Race.ORC) && player.isMageClass() ? 100 : 5;
					break;
				case PDAM:
				case STUN:
				case LETHAL_SHOT:
					chance = 15.;
					break;
			}

			switch(skill.getTargetType())
			{
				case TARGET_AURA:
				case TARGET_AREA:
				case TARGET_FAN:
				case TARGET_FAN_PB:
				case TARGET_SQUARE:
				case TARGET_SQUARE_PB:
				case TARGET_RING_RANGE:
					chance /= 10.;
					break;
			}

			if(Rnd.chance(chance))
			{
				if(skill.getMagicLevel() < (player.getLevel() - 10))
					weakSkills.add(skill);
				else
					skills.add(skill);
			}
		}

		if(skills.isEmpty())
			skills = weakSkills;

		return Rnd.get(skills);
	}

	@Override
	protected void onEvtAttacked(Creature attacker, Skill skill, int damage)
	{
		synchronized (this)
		{
			if(Rnd.chance(60))
			{
				if(tryRunOff(attacker))
					return;
			}

			if(damage > 0)
			{
				Player player = getActor();
				if(attacker.isNpc())
				{
					if(Rnd.chance(25))
					{
						player.setCurrentHp(player.getCurrentHp() + (player.getMaxHp() / 5), true, true);
						player.setCurrentMp(player.getCurrentMp() + (player.getMaxMp() / 5));
					}
				}

				double chance = 25;

				GameObject target = player.getTarget();
				if(target != null)
				{
					if(target == attacker)
						return;

					if(target instanceof Creature)
					{
						Creature creatureTarget = (Creature) target;
						if(creatureTarget.getAI().getAttackTarget() != player && creatureTarget.getAI().getCastTarget() != player)
							chance = 80;
						else
							chance = 5;
					}
					else
						return;
				}

				if(attacker.isPlayable())
					chance = 30;

				if((attacker.getLevel() - player.getLevel()) >= 10)
					chance /= 5;

				if(Rnd.chance(chance))
				{
					player.setTarget(attacker);
					stopWait();
				}
			}
		}
	}

	@Override
	public void onDeath(Creature actor, Creature killer)
	{
		doRevive();
	}

	private void doRevive()
	{
		clearCurrentFarmZone();
		clearPlannedActions();

		_plannedActions.add(new WaitAction(3000, 10000, 100.)); // Думаем перед воскрешением
		_plannedActions.add(new ReviveAction());
		_plannedActions.add(new WaitAction(2000, 6000, 100.)); // Тупим после воскрешения
	}

	@Override
	public void onLevelChange(Player player, int oldLvl, int newLvl)
	{
		if(player.isFakePlayer())
		{
			FakePlayerUtils.setProf(player);
			todoEquip = FakePlayerUtils.checkEquip(this);
		}

		if(player.getLevel() == Config.ALT_MAX_LEVEL) // TODO: Переделать.
		{
			deleteFake(player);
		}
	}

	private void deleteFake(Player player)
	{
		if(!player.isFakePlayer())
			return;

		if(!deleted.compareAndSet(false, true))
			return;

		int objectId = player.getObjectId();
		player.kick();
		CharacterDAO.getInstance().deleteCharByObjId(objectId);
	}

	@Override
	public void onTeleport(Player player, int x, int y, int z, Reflection reflection)
	{
		synchronized (this)
		{
			startWait(2000, 5000);

			TownZoneTemplate townZone = null;
			loop1:
			for(TownZoneTemplate t : FakePlayersHolder.getInstance().getTownZones())
			{
				for(Zone zone : t.getZones())
				{
					if(zone.checkIfInZone(x, y, z))
					{
						townZone = t;
						break loop1;
					}
				}
			}

			if(townZone != null)
			{
				if(clearCurrentFarmZone())
					clearPlannedActions();
			}
		}
	}

	@Override
	public void onChatMessageReceive(Player player, ChatType type, String charName, String text)
	{
		if(type == ChatType.TELL)
			FakePlayerUtils.writeInPrivateChat(this, charName);
	}

	@Override
	public void run()
	{
		Player actor = getActor();
		if(actor == null || deleted.get())
		{
			stopActionTask();
			return;
		}
		performNextAction(false);
	}

	private synchronized void startActionTask()
	{
		if(_actionTask == null)
			_actionTask = ThreadPoolManager.getInstance().scheduleAtFixedDelay(this, 500L, 500L);
	}

	private synchronized void stopActionTask()
	{
		if(_actionTask != null)
		{
			_actionTask.cancel(true);
			_actionTask = null;
		}
	}

	private static Location getRandomLoc(List<Zone> zones, int geoIndex, boolean fly)
	{
		Zone zone = Rnd.get(zones);
		if(zone != null)
			return zone.getTerritory().getRandomLoc(geoIndex, fly);
		return new Location();
	}

	private static boolean isInside(List<Zone> zones, int x, int y, int z)
	{
		for(Zone zone : zones)
		{
			if(zone.checkIfInZone(x, y, z))
				return true;
		}
		return false;
	}
}