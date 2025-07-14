package l2s.gameserver.model.actor.instances.player;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.ExAutoplayDoMacro;
import l2s.gameserver.network.l2.s2c.ExAutoplaySetting;
import l2s.gameserver.network.l2.s2c.ExServerPrimitivePacket;
import l2s.gameserver.utils.ItemFunctions;

public class AutoFarm
{
	private final Lock _onUseLock = new ReentrantLock();

	private class DistanceComparator implements Comparator<GameObject>
	{
		@Override
		public int compare(GameObject o1, GameObject o2)
		{
			final int dist1 = _owner.getDistance(o1);
			final int dist2 = _owner.getDistance(o2);
			return Integer.compare(dist1, dist2);
		}
	}

	public enum TargetType
	{
		ANY,
		MONSTER,
		PLAYER,
		NPC,
		PAYBACK_ATTACK
	}

	private final Player _owner;

	private final DistanceComparator _distanceComparator = new DistanceComparator();

	private TargetType _targetType = TargetType.MONSTER;

	private boolean _farmActivate = false;
	private boolean _autoPickUpItems = false;
	private boolean _meleeAttackMode = false;
	private int _petHealPercent = 0;
	private boolean _politeFarm = false;
	private int _meleeFarmDistance = 600;
	private int _longRangeFarmDistance = 1500;
	private boolean _showAutoFarmRange = false;
	private boolean _fixedFarmRange = false;
	private boolean _targetRaidBoss = false;
	private Location _fixedFarmRangePoint = null;
	private static final String MELEE_FARM_DISTANCE_VAR = "@melee_farm_distance";
	private static final String LONG_RANGE_FARM_DISTANCE_VAR = "@long_range_farm_distance";
	private static final String SHOW_AUTO_FARM_RANGE_VAR = "@show_auto_farm_range";
	private static final String FIXED_FARM_RANGE_VAR = "@fixed_farm_range";
	private static final String TARGET_RAID_BOSS_VAR = "@target_raid";

	private ScheduledFuture<?> _farmTask = null;

	public AutoFarm(Player owner)
	{
		_owner = owner;
	}

	public Player getOwner()
	{
		return _owner;
	}

	public TargetType getTargetType()
	{
		return _targetType;
	}

	public void setTargetType(TargetType targetType)
	{
		_targetType = targetType;
	}

	public boolean isFarmActivate()
	{
		return _farmActivate;
	}

	public void setFarmActivate(boolean farmActivate)
	{
		_farmActivate = farmActivate;
	}

	public boolean isAutoPickUpItems()
	{
		return _autoPickUpItems;
	}

	public void setAutoPickUpItems(boolean autoPickUpItems)
	{
		_autoPickUpItems = autoPickUpItems;
	}

	public boolean isMeleeAttackMode()
	{
		return _meleeAttackMode;
	}

	public void setMeleeAttackMode(boolean meleeAttackMode)
	{
		_meleeAttackMode = meleeAttackMode;
	}

	public int getHealPercent()
	{
		return _owner.getVarInt(PlayerVariables.AUTO_HP_VAR, 80);
	}

	public void setHealPercent(int healPercent)
	{
		_owner.setVar(PlayerVariables.AUTO_HP_VAR, healPercent);
	}

	public int getPetHealPercent()
	{
		return _petHealPercent;
	}

	public void setPetHealPercent(int healPercent)
	{
		_petHealPercent = healPercent;
	}

	public boolean isPoliteFarm()
	{
		return _politeFarm;
	}

	public void setPoliteFarm(boolean politeFarm)
	{
		_politeFarm = politeFarm;
	}

	public int getMeleeFarmDistance()
	{
		return _meleeFarmDistance;
	}

	public boolean isShowAutoFarmRange()
	{
		return _showAutoFarmRange;
	}

	public void setShowAutoFarmRange(boolean showAutoFarmRange)
	{
		if(_showAutoFarmRange == showAutoFarmRange)
			return;
		_showAutoFarmRange = showAutoFarmRange;
		_owner.setVar(SHOW_AUTO_FARM_RANGE_VAR, showAutoFarmRange);
		if(isFarmActivate())
		{
			if(showAutoFarmRange)
			{
				if(!isFixedFarmRange())
				{}
				else if(_fixedFarmRangePoint != null)
				{}
			}
			else if(!showAutoFarmRange)
			{}
		}
	}

	public boolean isFixedFarmRange()
	{
		return _fixedFarmRange;
	}

	public boolean isTargetRaidBoss()
	{
		return _targetRaidBoss;
	}

	public void setTargetRaidBoss(boolean target)
	{
		if(_targetRaidBoss == target)
			return;
		_targetRaidBoss = target;
		_owner.setVar(TARGET_RAID_BOSS_VAR, target);
	}

	public void setFixedFarmRange(boolean fixedFarmRange)
	{
		if(_fixedFarmRange == fixedFarmRange)
			return;
		_fixedFarmRange = fixedFarmRange;
		_owner.setVar(FIXED_FARM_RANGE_VAR, fixedFarmRange);
		updateFixedFarmRangePoint(_owner.getLoc());
	}

	public void onMove(Location loc, boolean manual)
	{
		if(!isFarmActivate())
			return;
		if(isFixedFarmRange())
		{
			if(manual)
			{
				updateFixedFarmRangePoint(loc);
			}
		}
	}

	private void debugAutofarmRange(Location loc)
	{
		final ExServerPrimitivePacket packet = new ExServerPrimitivePacket("AutoFarmRange", loc.getX(), loc.getY(), 65535 + loc.getZ());
		if(isFarmActivate() && isShowAutoFarmRange())
		{
			final int circleRadius = getFarmDistance();
			for(int step = 0; step < 3; step++)
			{
				final int z = loc.getZ() + 10 + (step * 30);
				for(int degrees = 0; degrees < 360; degrees += 6)
				{
					final int x1 = (int) (loc.getX() - circleRadius * Math.sin(Math.toRadians(degrees)));
					final int y1 = (int) (loc.getY() + circleRadius * Math.cos(Math.toRadians(degrees)));
					final int x2 = (int) (loc.getX() - circleRadius * Math.sin(Math.toRadians(degrees + 6)));
					final int y2 = (int) (loc.getY() + circleRadius * Math.cos(Math.toRadians(degrees + 6)));
					packet.addLine(Color.GREEN, x1, y1, z, x2, y2, z);
				}
			}
		}
		else
		{
			packet.addPoint(Color.GREEN, loc.getX(), loc.getY(), Short.MIN_VALUE);
		}
		_owner.sendPacket(packet);
	}

	private void updateFixedFarmRangePoint(Location loc)
	{
		if(isShowAutoFarmRange())
		{
			debugAutofarmRange(loc);
		}
		if(isFixedFarmRange())
		{
			_fixedFarmRangePoint = loc;
			if(isFarmActivate() && isShowAutoFarmRange())
			{
				debugAutofarmRange(loc);
			}
		}
		else
		{
			_fixedFarmRangePoint = null;
		}
	}

	private Location getFarmRangePoint()
	{
		if(isFixedFarmRange())
		{
			if(_fixedFarmRangePoint == null)
			{
				updateFixedFarmRangePoint(_owner.getLoc());
			}
			return _fixedFarmRangePoint;
		}
		return _owner.getLoc();
	}

	private int getFarmDistance()
	{
		return isMeleeAttackMode() ? getMeleeFarmDistance() : getLongRangeFarmDistance();
	}

	public void setMeleeFarmDistance(int meleeFarmDistance)
	{
		if(_meleeFarmDistance == meleeFarmDistance)
			return;
		_meleeFarmDistance = meleeFarmDistance;
		_owner.setVar(MELEE_FARM_DISTANCE_VAR, meleeFarmDistance);
		if(isFarmActivate() && isShowAutoFarmRange())
		{
			if(!isFixedFarmRange())
			{
				debugAutofarmRange(_owner.getLoc());
			}
			else if(isFixedFarmRange())
			{
				debugAutofarmRange(_owner.getLoc());
			}
		}
	}

	public int getLongRangeFarmDistance()
	{
		return _longRangeFarmDistance;
	}

	public void setLongRangeFarmDistance(int longRangeFarmDistance)
	{
		if(_longRangeFarmDistance == longRangeFarmDistance)
			return;
		_longRangeFarmDistance = longRangeFarmDistance;
		_owner.setVar(LONG_RANGE_FARM_DISTANCE_VAR, longRangeFarmDistance);
		if(isFarmActivate() && isShowAutoFarmRange())
		{
			if(!isFixedFarmRange())
			{
				debugAutofarmRange(_owner.getLoc());
			}
			else if(isFixedFarmRange())
			{
				debugAutofarmRange(_owner.getLoc());
			}
		}
	}

	public void onAttacked(Creature attacker)
	{
		_onUseLock.lock();
		try
		{
			if(getTargetType() == TargetType.PAYBACK_ATTACK)
			{
				final Player player = attacker.getPlayer();
				if(player != null)
				{
					final GameObject target = _owner.getTarget();
					if(target == null || !target.isPlayer() && target != player)
					{
						final Abnormal abnormal = player.getAbnormalList().getAbnormal(60002);
						if(abnormal != null && abnormal.getLevel() >= 3)
						{
							if(checkTargetCondition(player, TargetType.PAYBACK_ATTACK))
							{
								_owner.setTarget(player);
							}
						}
					}
				}
			}
		}
		finally
		{
			_onUseLock.unlock();
		}
	}

	public synchronized void doAutoFarm()
	{
		_onUseLock.lock();
		try
		{
			if(_owner.isTeleporting() || _owner.isDead())
			{
				_farmActivate = false;
				// _farmTask.cancel(false);
				_owner.sendPacket(new ExAutoplaySetting(_owner));

			}
			if(_owner.isMounted() || _owner.isTransformed())
			{
				_farmActivate = false;
				_owner.sendPacket(new ExAutoplaySetting(_owner));
				return;
			}
			if(!isFarmActivate())
			{
				if(_farmTask != null)
				{
					_farmTask.cancel(false);
					_farmTask = null;
				}
				return;
			}

			if(_farmTask == null)
			{
				_farmTask = ThreadPoolManager.getInstance().scheduleAtFixedDelay(this::doAutoFarm, 500L, 500L);
			}

			if(_owner.getAI().getIntention() == CtrlIntention.AI_INTENTION_PICK_UP)
				return;

			GameObject target = _owner.getTarget();
			if(!checkTargetCondition(target, getTargetType()))
			{
				_owner.setTarget(null);
				target = findAutoFarmTarget();
				if(target != null)
				{
					if(target.isItem())
					{
						_owner.getAI().setIntention(CtrlIntention.AI_INTENTION_PICK_UP, target, null);
						return;
					}
					_owner.setTarget(target);
				}
			}

			if((target == null) || (_owner.isInPeaceZone() && !Config.AUTOFARM_IN_PEACE_ZONE))
				return;

			if(_owner.isInPeaceZone() && (_owner.getTarget() != null) && (!_owner.getTarget().isMonster()))
				return;

			if(!_owner.getAutoShortCuts().autoSkillsActive())
			{
				if(!_owner.isMageClass() || _owner.getAutoShortCuts().autoAttackActive())
				{
					_owner.sendPacket(new ExAutoplayDoMacro());
				}
			}
		}
		finally
		{
			_onUseLock.unlock();
		}
	}

	private GameObject findAutoFarmTarget()
	{
		_onUseLock.lock();
		try
		{
			final TargetType targetType = getTargetType();

			if(isAutoPickUpItems())
			{
				final List<ItemInstance> items = World.getAroundItems(_owner, 2000, 500);
				items.sort(_distanceComparator);
				for(final ItemInstance item : items)
				{
					if(!item.isVisible() || !ItemFunctions.checkIfCanPickup(_owner, item) || (item.getDropTimeOwner() <= System.currentTimeMillis())
							|| !GeoEngine.canMoveToCoord(_owner, item))
					{
						continue;
					}
					return item;
				}
			}

			List<GameObject> targets;
			switch(targetType)
			{
				case MONSTER:
				case PAYBACK_ATTACK:
					targets = World.getAroundObjects(_owner, 2000, 500, GameObject::isMonster);
					break;
				case NPC:
					targets = World.getAroundObjects(_owner, 2000, 500, c -> c.isNpc() && !c.isMonster());
					break;
				case PLAYER:
					targets = World.getAroundObjects(_owner, 2000, 500, GameObject::isPlayable);
					break;
				default:
					targets = World.getAroundObjects(_owner, 2000, 500, GameObject::isCreature);
			}
			targets.sort(_distanceComparator);

			if(_owner.hasServitor() || _owner.isInParty())
			{
				for(final GameObject target : targets)
				{
					if(checkTargetCondition(target, true, targetType))
						return target;
				}
			}

			for(final GameObject target : targets)
			{
				if(checkTargetCondition(target, false, targetType))
					return target;
			}
		}
		finally
		{
			_onUseLock.unlock();
		}
		return null;
	}

	private boolean checkTargetCondition(GameObject target, TargetType targetType)
	{
		if(target == null)
			return false;
		return checkTargetCondition(target, false, targetType);
	}

	private boolean checkTargetCondition(GameObject target, boolean help, TargetType targetType)
	{
		if(!checkTargetCondition0(target, help, targetType))
			return false;
		return GeoEngine.canSeeTarget(_owner, target) && GeoEngine.canMoveToCoord(_owner, target);
	}

	private boolean checkTargetCondition0(GameObject target, boolean help, TargetType targetType)
	{
		_onUseLock.lock();
		try
		{
			if(!target.isVisible() || !target.isCreature())
				return false;
			final Creature creature = (Creature) target;
			if(creature.isAlikeDead() || (creature.getReflectionId() != _owner.getReflectionId()))
				return false;

			if(targetType == TargetType.PAYBACK_ATTACK && creature.isPlayer())
			{
				final Player player = creature.getPlayer();
				if(player != null)
				{
					if(!player.isInRange(_owner, 5000) || (!player.isAutoAttackable(_owner) && player.getPvpFlag() <= 0) || player.isInvisible(_owner))
						return false;
					return true;
				}
			}

			if(creature.isMonster())
			{
				if(targetType != TargetType.ANY && targetType != TargetType.MONSTER && targetType != TargetType.PAYBACK_ATTACK)
					return false;
				final NpcInstance npc = (NpcInstance) creature;
				if(npc.isRaid())
					return false;
				final NpcInstance leader = npc.getLeader();
				if(leader != null && leader.isRaid())
					return false;
			}
			else if(creature.isNpc())
			{
				if(targetType != TargetType.ANY && targetType != TargetType.NPC)
					return false;
			}
			else if(creature.isPlayable())
			{
				if(targetType != TargetType.ANY && targetType != TargetType.PLAYER)
					return false;
			}

			if(creature.isInvulnerable())
				return false;

			if(!help)
			{
				final int attackRange = isMeleeAttackMode() ? 600 : 1200;
				if(!creature.isInRange(_owner, attackRange))
					return false;
			}
			if(!creature.isAutoAttackable(_owner) || creature.isInvisible(_owner))
				return false;

			final Creature[] npcTargets = new Creature[] {
					creature.getAI().getAttackTarget(),
					creature.getAI().getCastTarget()
			};
			if(creature.isInCombat())
			{
				for(final Creature attackTarget : npcTargets)
				{
					if(attackTarget != null)
					{
						if((!help && attackTarget == _owner) || _owner.isMyServitor(attackTarget.getObjectId()))
							return true;
						final Player attackTargetPlayer = attackTarget.getPlayer();
						if(attackTargetPlayer != null && _owner.isInSameParty(attackTargetPlayer))
							return true;
					}
				}
			}

			if(help)
				return false;

			if(isPoliteFarm() && creature.isInCombat())
			{ // Вежливая охота
				for(final Creature attackTarget : npcTargets)
				{
					if(attackTarget != null && attackTarget.isPlayable())
						return false;
				}
			}
		}
		finally
		{
			_onUseLock.unlock();
		}
		return true;
	}
}
