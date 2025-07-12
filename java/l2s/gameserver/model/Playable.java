package l2s.gameserver.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CHashIntObjectMap;
import org.napile.primitive.pair.IntObjectPair;

import l2s.commons.lang.reference.HardReference;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.AggroList.AggroInfo;
import l2s.gameserver.model.Zone.ZoneType;
import l2s.gameserver.model.actor.basestats.PlayableBaseStats;
import l2s.gameserver.model.actor.flags.PlayableFlags;
import l2s.gameserver.model.entity.boat.Boat;
import l2s.gameserver.model.entity.events.Event;
import l2s.gameserver.model.entity.events.impl.DuelEvent;
import l2s.gameserver.model.entity.events.impl.SingleMatchEvent;
import l2s.gameserver.model.instances.ChairInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.RevivePacket;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.TimeStamp;
import l2s.gameserver.skills.enums.AbnormalType;
import l2s.gameserver.skills.enums.SkillTargetType;
import l2s.gameserver.skills.enums.SkillType;
import l2s.gameserver.stats.Stats;
import l2s.gameserver.templates.CreatureTemplate;
import l2s.gameserver.templates.item.EtcItemTemplate;
import l2s.gameserver.templates.item.WeaponTemplate;
import l2s.gameserver.templates.item.WeaponTemplate.WeaponType;

public abstract class Playable extends Creature
{
	private boolean _isPendingRevive;
	protected final IntObjectMap<TimeStamp> _sharedGroupReuses = new CHashIntObjectMap<TimeStamp>();
	protected final AtomicBoolean _isUsingItem = new AtomicBoolean(false);

	public Playable(int objectId, CreatureTemplate template)
	{
		super(objectId, template);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HardReference<? extends Playable> getRef()
	{
		return (HardReference<? extends Playable>) super.getRef();
	}

	public abstract Inventory getInventory();

	public abstract long getWearedMask();

	private Boat _boat;
	private Location _inBoatPosition;

	/**
	 * Checks whether to set the PvP flag for the player.
	 */
	@Override
	public boolean checkPvP(final Creature target, SkillEntry skillEntry)
	{
		final Player player = getPlayer();

		if (isDead() || target == null || player == null || target == this || target == player || player.isMyServitor(target.getObjectId()) || player.isPK())
			return false;

		if (skillEntry != null)
		{
			if (skillEntry.isAltUse())
				return false;
			if (skillEntry.getTemplate().getTargetType() == SkillTargetType.TARGET_UNLOCKABLE)
				return false;
			if (skillEntry.getTemplate().getTargetType() == SkillTargetType.TARGET_CHEST)
				return false;
		}

		// Duel check... Members of the same duel are not flagged
		for (final SingleMatchEvent event : getEvents(SingleMatchEvent.class))
		{
			if (!event.checkPvPFlag(player, target))
				return false;
		}

		if ((isInPeaceZone() && target.isInPeaceZone()) || (isInZoneBattle() && target.isInZoneBattle()))
			return false;
		if (isInSiegeZone() && target.isInSiegeZone())
			return false;
		if (getPlayer().isInFightClub())
			return false;
		if (skillEntry == null || skillEntry.getTemplate().isDebuff())
		{
			if (target.isPK())
				return false;
			if (target.isPlayable())
				return true;
		}
		else if (target.getPvpFlag() > 0 || target.isPK() || (target.isMonster() && !skillEntry.getTemplate().isNoFlagNoForce()))
			return true;

		return false;
	}

	/**
	 * Проверяет, можно ли атаковать цель (для физ атак)
	 */
	public boolean checkTarget(Creature target)
	{
		final Player player = getPlayer();
		if (player == null)
			return false;

		if (target == null || target.isDead())
		{
			player.sendPacket(SystemMsg.INVALID_TARGET);
			return false;
		}

		if (!isInRange(target, 2000))
		{
			player.sendPacket(SystemMsg.YOUR_TARGET_IS_OUT_OF_RANGE);
			return false;
		}

		if (target.isInvisible(this) || getReflection() != target.getReflection())
		{
			player.sendPacket(SystemMsg.CANNOT_SEE_TARGET);
			return false;
		}

		if (player.isInZone(ZoneType.epic) != target.isInZone(ZoneType.epic))
		{
			player.sendPacket(SystemMsg.INVALID_TARGET);
			return false;
		}

		if (target.isPlayable())
		{
			if (!player.getPlayerAccess().PeaceAttack)
			{
				// You cannot attack someone who is in the arena if you yourself are not in the
				// arena
				if (isInZoneBattle() != target.isInZoneBattle())
				{
					player.sendPacket(SystemMsg.INVALID_TARGET);
					return false;
				}

				// If the target or the attacker is in a peaceful zone, you cannot attack
				if (isInPeaceZone() || target.isInPeaceZone())
				{
					player.sendPacket(SystemMsg.YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_A_PEACEFUL_ZONE);
					return false;
				}
			}
			if (player.isInOlympiadMode() && !player.isOlympiadCompStart())
				return false;
		}

		if (!target.isAttackable(this))
		{
			player.sendPacket(SystemMsg.INVALID_TARGET);
			return false;
		}

		if (target.paralizeOnAttack(this))
		{
			if (Config.PARALIZE_ON_RAID_DIFF)
			{
				paralizeMe(target);
				return false;
			}
		}

		return true;
	}

	@Override
	public void doAttack(Creature target)
	{
		final Player player = getPlayer();
		if (player == null)
			return;

		if (isAMuted() || isAttackingNow() || player.isInObserverMode())
		{
			player.sendActionFailed();
			return;
		}

		if (!checkTarget(target))
		{
			// On the offe, the summon still tries to attack an unattacked target.
			if (!isServitor())
			{
				getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
			}
			player.sendActionFailed();
			return;
		}

		// Break duels if the target is not a duelist
		final DuelEvent duelEvent = getEvent(DuelEvent.class);
		if (duelEvent != null && target.getEvent(DuelEvent.class) != duelEvent)
		{
			duelEvent.abortDuel(getPlayer());
		}

		final WeaponTemplate weaponItem = getActiveWeaponTemplate();
		if (weaponItem != null)
		{
			int weaponMpConsume = weaponItem.getMpConsume();
			final int[] reducedMPConsume = weaponItem.getReducedMPConsume();
			if (reducedMPConsume[0] > 0 && Rnd.chance(reducedMPConsume[0]))
			{
				weaponMpConsume = reducedMPConsume[1];
			}

			final boolean isBowOrCrossbow = weaponItem.getItemType() == WeaponType.BOW || weaponItem.getItemType() == WeaponType.CROSSBOW || weaponItem.getItemType() == WeaponType.TWOHANDCROSSBOW || weaponItem.getItemType() == WeaponType.FIREARMS;
			if (isBowOrCrossbow)
			{
				final double cheapShot = getStat().calc(Stats.CHEAP_SHOT, 0., target, null);
				if (Rnd.chance(cheapShot))
				{
					weaponMpConsume = 0;
				}
			}

			if (weaponMpConsume > 0)
			{
				if (_currentMp < weaponMpConsume)
				{
					getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
					player.sendPacket(SystemMsg.NOT_ENOUGH_MP);
					player.sendActionFailed();
					return;
				}
				reduceCurrentMp(weaponMpConsume, null);
			}

			if (isBowOrCrossbow)
			{
				if (!player.checkAndEquipArrows())
				{
					getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
					if (player.getActiveWeaponInstance().getItemType() == WeaponType.BOW)
					{
						player.sendPacket(SystemMsg.YOU_HAVE_RUN_OUT_OF_ARROWS);
					}
					else if (player.getActiveWeaponInstance().getItemType() == WeaponType.CROSSBOW || player.getActiveWeaponInstance().getItemType() == WeaponType.TWOHANDCROSSBOW)
					{
						player.sendPacket(SystemMsg.NOT_ENOUGH_BOLTS);
					}
					else
					{
						player.sendPacket(SystemMsg.YOU_CANNOT_ATTACK_BECAUSE_YOU_DONT_HAVE_AN_ELEMENTAL_ORB);
					}
					player.sendActionFailed();
					return;
				}
			}
		}

		super.doAttack(target);
	}

	@Override
	public boolean doCast(final SkillEntry skillEntry, final Creature target, boolean forceUse)
	{
		if (skillEntry == null)
			return false;

		// Прерывать дуэли если цель не дуэлянт
		final DuelEvent duelEvent = getEvent(DuelEvent.class);
		if (duelEvent != null && target.getEvent(DuelEvent.class) != duelEvent)
		{
			duelEvent.abortDuel(getPlayer());
		}

		final Skill skill = skillEntry.getTemplate();

		if (skill.getSkillType() == SkillType.DEBUFF && target.isNpc() && target.isInvulnerable() && !target.isMonster())
		{
			getPlayer().sendPacket(SystemMsg.INVALID_TARGET);
			return false;
		}

		return super.doCast(skillEntry, target, forceUse);
	}

	@Override
	public void reduceCurrentHp(double damage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflectAndAbsorb, boolean transferDamage, boolean isDot, boolean sendReceiveMessage, boolean sendGiveMessage, boolean crit, boolean miss, boolean shld, double elementalDamage, boolean elementalCrit)
	{
		if (attacker == null || isDead() || (attacker.isDead() && !isDot))
			return;

		final boolean damageBlocked = isDamageBlocked(attacker);
		if (damageBlocked && transferDamage)
			return;

		if (damageBlocked && attacker != this)
		{
			if (attacker.isPlayer())
			{
				if (sendGiveMessage)
				{
					attacker.sendPacket(SystemMsg.THE_ATTACK_HAS_BEEN_BLOCKED);
				}
			}
			return; // return anyway, if damage is blocked it's blocked from everyone!!
		}

		if (attacker != this && attacker.isPlayable())
		{
			final Player player = getPlayer();
			final Player pcAttacker = attacker.getPlayer();
			if (pcAttacker != player)
				if (player.isInOlympiadMode() && !player.isOlympiadCompStart())
				{
					if (sendGiveMessage)
					{
						pcAttacker.sendPacket(SystemMsg.INVALID_TARGET);
					}
					return;
				}

			if (isInZoneBattle() != attacker.isInZoneBattle())
			{
				if (sendGiveMessage)
				{
					attacker.getPlayer().sendPacket(SystemMsg.INVALID_TARGET);
				}
				return;
			}

			final DuelEvent duelEvent = getEvent(DuelEvent.class);
			if (duelEvent != null && attacker.getEvent(DuelEvent.class) != duelEvent)
			{
				duelEvent.abortDuel(player);
			}
		}

		super.reduceCurrentHp(damage, attacker, skill, awake, standUp, directHp, canReflectAndAbsorb, transferDamage, isDot, sendReceiveMessage, sendGiveMessage, crit, miss, shld, elementalDamage, elementalCrit);
	}

	@Override
	public boolean isAttackable(Creature attacker)
	{
		return isCtrlAttackable(attacker, true, false);
	}

	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return isCtrlAttackable(attacker, false, false);
	}

	/**
	 * force - Ctrl нажат или нет. nextAttackCheck - для флагнутых не нужно нажимать
	 * Ctrl, но нет и автоатаки.
	 */
	public boolean isCtrlAttackable(Creature attacker, boolean force, boolean nextAttackCheck)
	{
		final Player player = getPlayer();
		if (attacker == null || player == null || attacker == this || attacker == player && !force || isDead() || attacker.isAlikeDead())
			return false;

		if (player.isMyServitor(attacker.getObjectId()) || isInvisible(attacker) || getReflection() != attacker.getReflection())
			return false;

		Boat boat = player.getBoat();
		if (boat != null)
			return false;

		final Player pcAttacker = attacker.getPlayer();
		if (isPlayer() && pcAttacker == this)
			return false;

		if (pcAttacker != null && pcAttacker != player)
		{
			boat = pcAttacker.getBoat();
			if (boat != null)
				return false;
			if ((player.isInOlympiadMode() || pcAttacker.isInOlympiadMode()) && player.getOlympiadGame() != pcAttacker.getOlympiadGame())
				return false;
			if (player.isInOlympiadMode() && !player.isOlympiadCompStart()) // Бой еще не начался
				return false;
			if (player.isInOlympiadMode() && player.isOlympiadCompStart() && player.getOlympiadSide() == pcAttacker.getOlympiadSide() && !force) // нельзя
				return false;
			if (player.isInNonPvpTime())
				return false;
			if (!force && player.getParty() != null && player.getParty() == pcAttacker.getParty())
				return false;
			if (!force && player.isInParty() && player.getParty().getCommandChannel() != null && pcAttacker.isInParty() && pcAttacker.getParty().getCommandChannel() != null && player.getParty().getCommandChannel() == pcAttacker.getParty().getCommandChannel())
				return false;

			for (final Event e : attacker.getEvents())
				if (e.checkForAttack(this, attacker, null, force) != null)
					return false;

			if (isInZoneBattle())
				return true;
			if (isInPeaceZone())
				return false;

			for (final Event e : attacker.getEvents())
				if (e.canAttack(this, attacker, null, force, nextAttackCheck))
					return true;

			if (!force && player.getClan() != null && player.getClan() == pcAttacker.getClan())
				return false;
			if (!force && player.getClan() != null && player.getClan().getAlliance() != null && pcAttacker.getClan() != null && pcAttacker.getClan().getAlliance() != null && player.getClan().getAlliance() == pcAttacker.getClan().getAlliance())
				return false;
			if (isInSiegeZone())
				return true;
			if (pcAttacker.atMutualWarWith(player))
				return true;
			if (player.isPK())
				return true;
			if (player.getPvpFlag() != 0)
				return !nextAttackCheck;

			return force;
		}

		return true;
	}

	@Override
	public int getKarma()
	{
		final Player player = getPlayer();
		return player == null ? 0 : player.getKarma();
	}

	@Override
	public void callSkill(Creature aimingTarget, SkillEntry skillEntry, Set<Creature> targets, boolean useActionSkills, boolean trigger)
	{
		final Player player = getPlayer();
		if (player == null)
			return;

		final Skill skill = skillEntry.getTemplate();

		for (final Creature target : targets)
		{
			if (target.isNpc())
			{
				if (!trigger && skill.isDebuff()) // На оффе триггеры не накладывают паралич. Проверено 20.08.16
				{
					// mobs will hate on debuff
					if (target.paralizeOnAttack(player))
					{
						if (Config.PARALIZE_ON_RAID_DIFF)
						{
							paralizeMe(target);
							return;
						}
					}
				}
				target.getAI().notifyEvent(CtrlEvent.EVT_SEE_SPELL, skill, this, target);
			}
			else // исключать баффы питомца на владельца
			if (target.isPlayable() && player != target && !player.isMyServitor(target.getObjectId()))
			{
				final int aggro = skill.getEffectPoint();

				final List<NpcInstance> npcs = World.getAroundNpc(target);
				for (final NpcInstance npc : npcs)
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_SEE_SPELL, skill, this, target);

					if (!trigger && useActionSkills && !skillEntry.isAltUse() && !npc.isDead() && npc.isInRangeZ(this, 2000))
					{
						if (npc.getAggroList().getHate(target) > 0)
						{
							if (!skill.isHandler() && npc.paralizeOnAttack(player))
							{
								if (Config.PARALIZE_ON_RAID_DIFF)
								{
									final Skill revengeSkill = SkillHolder.getInstance().getSkill(Skill.SKILL_RAID_CURSE_2, 1);
									if (revengeSkill != null)
									{
										revengeSkill.getEffects(npc, this);
									}
								}
								return;
							}
						}

						if (aggro > 0)
						{
							final AggroInfo ai = npc.getAggroList().get(target);
							// Skip if the target is not in the hitlist
							// If the hate is less than 100, skip
							if ((ai == null) || (ai.hate < 100))
							{
								continue;
							}

							if (GeoEngine.canSeeTarget(npc, target))
							{
								// Mob will only aggro if it sees a target that you are healing/buffing.
								npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, this, ai.damage == 0 ? aggro / 2 : aggro);
							}
						}
					}
				}
			}

			// Check for PvP Flagging / Drawing Aggro
			if (!trigger && checkPvP(target, skillEntry))
			{
				startPvPFlag(target);
			}
		}
		super.callSkill(aimingTarget, skillEntry, targets, useActionSkills, trigger);
	}

	/**
	 * Оповещает других игроков о поднятии вещи
	 * 
	 * @param item предмет который был поднят
	 */
	public void broadcastPickUpMsg(ItemInstance item)
	{
		final Player player = getPlayer();

		if (item == null || player == null)
			return;

		if (item.isEquipable() && !(item.getTemplate() instanceof EtcItemTemplate))
		{
			SystemMessage msg = null;
			final String player_name = player.getName();
			if (item.getEnchantLevel() > 0)
			{
				final int msg_id = isPlayer() ? SystemMessage.ATTENTION_S1_PICKED_UP__S2_S3 : SystemMessage.ATTENTION_S1_PET_PICKED_UP__S2_S3;
				msg = new SystemMessage(msg_id).addString(player_name).addNumber(item.getEnchantLevel()).addItemName(item.getItemId());
			}
			else
			{
				final int msg_id = isPlayer() ? SystemMessage.ATTENTION_S1_PICKED_UP_S2 : SystemMessage.ATTENTION_S1_PET_PICKED_UP__S2_S3;
				msg = new SystemMessage(msg_id).addString(player_name).addItemName(item.getItemId());
			}
			for (final Player target : World.getAroundObservers(this))
			{
				if (!isInvisible(target))
				{
					target.sendPacket(msg);
				}
			}
		}
	}

	public void paralizeMe(Creature effector)
	{
		final Skill revengeSkill = SkillHolder.getInstance().getSkill(Skill.SKILL_RAID_CURSE, 1);
		revengeSkill.getEffects(effector, this);
	}

	public final void setPendingRevive(boolean value)
	{
		_isPendingRevive = value;
	}

	public boolean isPendingRevive()
	{
		return _isPendingRevive;
	}

	/** Sets HP, MP and CP and revives the L2Playable. */
	public void doRevive()
	{
		getListeners().onRevive();

		if (!isTeleporting())
		{
			setPendingRevive(false);
			setNonAggroTime(System.currentTimeMillis() + Config.NONAGGRO_TIME_ONTELEPORT);
			setNonPvpTime(System.currentTimeMillis() + Config.NONPVP_TIME_ONTELEPORT);

			if (isSalvation() || (isPlayer() && getPlayer().isInFightClub()))
			{
				getAbnormalList().stop(AbnormalType.RESURRECTION_SPECIAL);
				setCurrentHp(getMaxHp(), true);
				setCurrentMp(getMaxMp());
				setCurrentCp(getMaxCp());
			}
			else
			{
				setCurrentHp(Math.max(1, getMaxHp() * Config.RESPAWN_RESTORE_HP), true);

				if (Config.RESPAWN_RESTORE_MP >= 0)
				{
					setCurrentMp(getMaxMp() * Config.RESPAWN_RESTORE_MP);
				}

				if (isPlayer() && Config.RESPAWN_RESTORE_CP >= 0)
				{
					setCurrentCp(getMaxCp() * Config.RESPAWN_RESTORE_CP);
				}
			}

			broadcastPacket(new RevivePacket(this));
		}
		else
		{
			setPendingRevive(true);
		}
	}

	public abstract void doPickupItem(GameObject object);

	public void sitDown(ChairInstance chair)
	{
	}

	public void standUp()
	{
	}

	private long _nonAggroTime;

	public boolean isInNonAggroTime()
	{
		return _nonAggroTime > System.currentTimeMillis();
	}

	public void setNonAggroTime(long time)
	{
		_nonAggroTime = time;
	}

	private long _nonPvpTime;

	public boolean isInNonPvpTime()
	{
		return _nonPvpTime > System.currentTimeMillis();
	}

	public void setNonPvpTime(long time)
	{
		_nonPvpTime = time;
	}

	/**
	 * @return True if the Silent Moving mode is active.<BR>
	 *         <BR>
	 */
	public boolean isSilentMoving()
	{
		return getFlags().getSilentMoving().get();
	}

	public int getMaxLoad()
	{
		return 0;
	}

	public int getInventoryLimit()
	{
		return 0;
	}

	@Override
	public boolean isPlayable()
	{
		return true;
	}

	public boolean isSharedGroupDisabled(int groupId)
	{
		final TimeStamp sts = getSharedGroupReuse(groupId);
		if (sts == null)
			return false;
		if (sts.hasNotPassed())
			return true;
		_sharedGroupReuses.remove(groupId);
		return false;
	}

	public TimeStamp getSharedGroupReuse(int groupId)
	{
		return _sharedGroupReuses.get(groupId);
	}

	public void addSharedGroupReuse(int group, TimeStamp stamp)
	{
		_sharedGroupReuses.put(group, stamp);
	}

	public Collection<IntObjectPair<TimeStamp>> getSharedGroupReuses()
	{
		return _sharedGroupReuses.entrySet();
	}

	public boolean useItem(ItemInstance item, boolean ctrl, boolean sendMsg)
	{
		return false;
	}

	public int getCurrentLoad()
	{
		return 0;
	}

	public int getWeightPenalty()
	{
		return 0;
	}

	@Override
	public boolean isInBoat()
	{
		return _boat != null;
	}

	@Override
	public boolean isInShuttle()
	{
		return _boat != null && _boat.isShuttle();
	}

	public Boat getBoat()
	{
		return _boat;
	}

	public void setBoat(Boat boat)
	{
		_boat = boat;
	}

	public Location getInBoatPosition()
	{
		return _inBoatPosition;
	}

	public void setInBoatPosition(Location loc)
	{
		_inBoatPosition = loc;
	}

	public int getNameColor()
	{
		return 0;
	}

	@Override
	public PlayableBaseStats getBaseStats()
	{
		if (_baseStats == null)
		{
			_baseStats = new PlayableBaseStats(this);
		}
		return (PlayableBaseStats) _baseStats;
	}

	@Override
	public PlayableFlags getFlags()
	{
		if (_statuses == null)
		{
			_statuses = new PlayableFlags(this);
		}
		return (PlayableFlags) _statuses;
	}

	public abstract SkillEntry getAdditionalSSEffect(boolean spiritshot, boolean blessed);

	public long getRelation(Player target)
	{
		final Player player = getPlayer();
		if (player != null)
			return player.getRelation(target);
		return 0;
	}

	public int getCurrentAp()
	{
		return 0; 
	}

	public int getMaxAp()
	{
		return 0;  
	}

	public int getCurrentLp()
	{
		return 0;  
	}

	public int getMaxLp()
	{
		return 0;  
	}

	public int getCurrentWp()
	{
		return 0; 
	}

	public int getMaxWp()
	{
		return 0;
	}
}