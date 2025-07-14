package l2s.gameserver.model.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.ArrayUtils;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.math.SafeMath;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.AdditionalDropHolder;
import l2s.gameserver.data.xml.holder.SubjugationsHolder;
import l2s.gameserver.model.AggroList.HateInfo;
import l2s.gameserver.model.CommandChannel;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.PlayerGroup;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.base.TeamType;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestEventType;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.model.reward.RewardData;
import l2s.gameserver.model.reward.RewardItem;
import l2s.gameserver.model.reward.RewardList;
import l2s.gameserver.model.reward.RewardType;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExMagicAttackInfo;
import l2s.gameserver.network.l2.s2c.SocialActionPacket;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.subjugation.ExSubjugationSidebar;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.templates.SubjugationTemplate;
import l2s.gameserver.templates.item.data.RewardItemData;
import l2s.gameserver.templates.npc.AdditionalDrop;
import l2s.gameserver.templates.npc.Faction;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.ItemFunctions;

/**
 * This class manages all Monsters.
 **/
public class MonsterInstance extends NpcInstance
{
	protected class GroupInfo
	{
		public HashSet<Player> players;
		public double damage;

		public GroupInfo()
		{
			this.players = new HashSet<Player>();
			this.damage = 0.;
		}
	}

	private int overhitAttackerId;
	/**
	 * Stores the extra (over-hit) damage done to the L2NpcInstance when the
	 * attacker uses an over-hit enabled skill
	 */
	private double _overhitDamage;

	/** True if a Dwarf has used Spoil on this L2NpcInstance */
	private boolean _isSpoiled;
	private int spoilerId;
	/** Table containing all Items that a Dwarf can Sweep on this L2NpcInstance */
	private List<RewardItem> _sweepItems;
	private boolean _sweeped;
	private final Lock sweepLock = new ReentrantLock();

	private int _isChampion;

	private final boolean _canMove;

	public MonsterInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);

		_canMove = getParameter("canMove", true);
	}

	@Override
	public boolean isMovementDisabled()
	{
		return !_canMove || super.isMovementDisabled();
	}

	@Override
	public boolean isLethalImmune()
	{
		return _isChampion > 0 || super.isLethalImmune();
	}

	@Override
	public boolean isFearImmune()
	{
		return _isChampion > 0 || super.isFearImmune();
	}

	@Override
	public boolean isParalyzeImmune()
	{
		return _isChampion > 0 || super.isParalyzeImmune();
	}

	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return attacker.getPlayer() != null || attacker.isDefender();
	}

	public int getChampion()
	{
		return _isChampion;
	}

	public void setChampion()
	{
		if(getReflection().canChampions() && canChampion())
		{
			double random = Rnd.nextDouble();
			if(Config.ALT_CHAMPION_CHANCE2 / 100. >= random)
				setChampion(2);
			else if((Config.ALT_CHAMPION_CHANCE1 + Config.ALT_CHAMPION_CHANCE2) / 100. >= random)
				setChampion(1);
			else
				setChampion(0);
		}
		else
			setChampion(0);
	}

	public void setChampion(int level)
	{
		if(level == 0)
		{
			removeSkillById(4407);
			_isChampion = 0;
		}
		else
		{
			addSkill(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 4407, level));
			_isChampion = level;
		}
	}

	public boolean canChampion()
	{
		return !isMinion() && getTemplate().rewardExp > 0 && getTemplate().level >= Config.ALT_CHAMPION_MIN_LEVEL
				&& getTemplate().level <= Config.ALT_CHAMPION_TOP_LEVEL;
	}

	@Override
	public TeamType getTeam()
	{
		return getChampion() == 2 ? TeamType.RED : getChampion() == 1 ? TeamType.BLUE : TeamType.NONE;
	}

	@Override
	protected void onDespawn()
	{
		setOverhitDamage(0);
		setOverhitAttacker(null);
		clearSweep();

		super.onDespawn();
	}

	@Override
	public void onSpawnMinion(NpcInstance minion)
	{
		if(minion.isMonster())
		{
			if(getChampion() == 2)
				((MonsterInstance) minion).setChampion(1);
			else
				((MonsterInstance) minion).setChampion(0);
		}
		super.onSpawnMinion(minion);
	}

	@Override
	protected void onDeath(Creature killer)
	{
		calculateRewards(killer);

		super.onDeath(killer);
	}

	@Override
	protected void onReduceCurrentHp(double damage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean isDot)
	{
		if(skill != null && skill.isOverhit())
		{
			// Calculate the over-hit damage
			// Ex: mob had 10 HP left, over-hit skill did 50 damage total, over-hit damage
			// is 40
			double overhitDmg = (getCurrentHp() - damage) * -1;
			if(overhitDmg <= 0)
			{
				setOverhitDamage(0);
				setOverhitAttacker(null);
			}
			else
			{
				setOverhitDamage(overhitDmg);
				setOverhitAttacker(attacker);
			}
		}

		super.onReduceCurrentHp(damage, attacker, skill, awake, standUp, directHp, isDot);
	}

	public void calculateRewards(Creature lastAttacker)
	{
		Creature topDamager = getAggroList().getTopDamager(lastAttacker);
		if(lastAttacker == null || !lastAttacker.isPlayable())
			lastAttacker = topDamager;

		if(lastAttacker == null || !lastAttacker.isPlayable())
			return;

		Player killer = lastAttacker.getPlayer();
		if(killer == null)
			return;

		Map<Playable, HateInfo> aggroMap = getAggroList().getPlayableMap();

		Set<Quest> quests = getTemplate().getEventQuests(QuestEventType.MOB_KILLED_WITH_QUEST);
		if(quests != null && !quests.isEmpty())
		{
			List<Player> players = null; // массив с игроками, которые могут быть заинтересованы в квестах
			if(isRaid() && Config.ALT_NO_LASTHIT) // Для альта на ластхит берем всех игроков вокруг
			{
				players = new ArrayList<Player>();
				for(Playable pl : aggroMap.keySet())
					if(!pl.isDead() && (isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE) || killer.isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE)))
						if(!players.contains(pl.getPlayer())) // не добавляем дважды если есть пет
							players.add(pl.getPlayer());
			}
			else if(killer.getParty() != null) // если пати то собираем всех кто подходит
			{
				players = new ArrayList<Player>(killer.getParty().getMemberCount());
				for(Player pl : killer.getParty().getPartyMembers())
					if(!pl.isDead() && (isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE) || killer.isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE)))
						players.add(pl);
			}

			for(Quest quest : quests)
			{
				Player toReward = killer;
				if(quest.getPartyType() != Quest.PARTY_NONE && players != null)
					if(isRaid() || quest.getPartyType() == Quest.PARTY_ALL) // если цель рейд или квест для всей пати
					// награждаем всех участников
					{
						for(Player pl : players)
						{
							QuestState qs = pl.getQuestState(quest);
							if(qs != null && !qs.isCompleted())
								quest.notifyKill(this, qs);
						}
						toReward = null;
					}
					else
					{ // иначе выбираем одного
						List<Player> interested = new ArrayList<Player>(players.size());
						for(Player pl : players)
						{
							QuestState qs = pl.getQuestState(quest);
							if(qs != null && !qs.isCompleted()) // из тех, у кого взят квест
								interested.add(pl);
						}

						if(interested.isEmpty())
							continue;

						toReward = interested.get(Rnd.get(interested.size()));
						if(toReward == null)
							toReward = killer;
					}

				if(toReward != null)
				{
					QuestState qs = toReward.getQuestState(quest);
					if(qs != null && !qs.isCompleted())
						quest.notifyKill(this, qs);
				}
			}
		}

		Map<PlayerGroup, GroupInfo> groupsInfo = new HashMap<PlayerGroup, GroupInfo>();
		double totalDamage = 0;

		// Разбиваем игроков по группам. По возможности используем наибольшую из
		// доступных групп: Command Channel → Party → StandAlone (сам плюс пет :)
		for(HateInfo ai : aggroMap.values())
		{
			Player player = ai.attacker.getPlayer();
			if(player == null)
				continue;

			// Только при убийстве РБ опыт делиться на все CC.
			PlayerGroup group = isRaid() ? player.getPlayerGroup() : (player.getParty() != null ? player.getParty() : player);
			GroupInfo info = groupsInfo.get(group);
			boolean addDamage = true;
			if(info == null)
			{
				info = new GroupInfo();
				groupsInfo.put(group, info);
				addDamage = false;
			}

			for(Player p : group)
			{
				if(!p.isDead() && p.isInRangeZ(this, Config.ALT_PARTY_DISTRIBUTION_RANGE))
				{
					info.players.add(p);
					addDamage = true;
				}
			}

			if(addDamage)
			{
				info.damage += ai.damage;
				totalDamage += Math.max(0, ai.damage);
			}
		}

		totalDamage = Math.max(totalDamage, getMaxHp());

		for(Map.Entry<PlayerGroup, GroupInfo> groupInfo : groupsInfo.entrySet())
		{
			PlayerGroup group = groupInfo.getKey();
			GroupInfo info = groupInfo.getValue();

			double damage = info.damage;
			if(damage <= 1) // TODO: Чего 1 а не 0?
				continue;

			if(group instanceof CommandChannel)
			{
				// Опыт CC делиться поровну на все группы не зависимо сколько членов в группе.
				CommandChannel commandChannel = (CommandChannel) group;

				Set<Party> rewardedParties = new HashSet<Party>();
				for(Player p : info.players)
				{
					Party party = p.getParty();
					if(party != null && commandChannel.getParties().contains(party))
						rewardedParties.add(party);
				}

				for(Party party : rewardedParties)
				{
					HashSet<Player> rewardedMembers = new HashSet<Player>();
					int partylevel = 1;
					for(Player partyMember : party.getPartyMembers())
					{
						if(info.players.remove(partyMember))
						{
							if(partyMember.getLevel() > partylevel)
								partylevel = partyMember.getLevel();

							rewardedMembers.add(partyMember);
						}
					}
					double[] xpsp = calculateExpAndSp(partylevel, damage / rewardedParties.size(), totalDamage);
					xpsp[0] = applyOverhit(killer, xpsp[0]);
					party.distributeXpAndSp(xpsp[0], xpsp[1], rewardedMembers, lastAttacker, this);
				}
			}
			else if(group instanceof Party)
			{
				Party party = (Party) group;
				int partylevel = 1;
				for(Player p : info.players)
				{
					if(p.getLevel() > partylevel)
						partylevel = p.getLevel();
				}
				double[] xpsp = calculateExpAndSp(partylevel, damage, totalDamage);
				xpsp[0] = applyOverhit(killer, xpsp[0]);
				party.distributeXpAndSp(xpsp[0], xpsp[1], info.players, lastAttacker, this);
			}
			else if(group instanceof Player)
			{
				Player player = (Player) group;
				double[] xpsp = calculateExpAndSp(player.getLevel(), damage, totalDamage);
				xpsp[0] = applyOverhit(killer, xpsp[0]);
				player.addExpAndCheckBonus(this, (long) xpsp[0], (long) xpsp[1]);
			}
		}

		if(topDamager != null && topDamager.isPlayable())
		{
			for(RewardList rewardList : getRewardLists())
				rollRewards(rewardList, lastAttacker, topDamager);

			Player player = topDamager.getPlayer();
			if(player != null)
			{
				final double penaltyMod = Experience.penaltyModifier(calculateLevelDiffForDrop(topDamager.getLevel()), 9);
				for(AdditionalDrop drop : AdditionalDropHolder.getInstance().getDrop())
				{
					if(!drop.checkMonster(this))
						continue;

					double mod = drop.isLevelPenalty() ? penaltyMod : 1;

					for(RewardData rewardData : drop.getRewardItems())
					{
						RewardItem rewardItem = rewardData.getItem().isAdena() ? rewardData.rollAdena(mod, 1) : rewardData.rollItem(mod, 1, 1);
						if(rewardItem != null)
						{
							ItemFunctions.addItem(player, rewardItem.itemId, rewardItem.count, true);
						}
					}
				}

				if(Math.abs(getLevel() - player.getLevel()) < 9)
				{
					for(RewardItemData reward : player.getPremiumAccount().getRewards())
					{
						if(Rnd.chance(reward.getChance()))
							ItemFunctions.addItem(player, reward.getId(), Rnd.get(reward.getMinCount(), reward.getMaxCount()));
					}

					for(RewardItemData reward : player.getVIP().getTemplate().getRewards())
					{
						if(Rnd.chance(reward.getChance()))
							ItemFunctions.addItem(player, reward.getId(), Rnd.get(reward.getMinCount(), reward.getMaxCount()));
					}

					if(getChampion() > 0 && Config.SPECIAL_ITEM_ID > 0 && Config.SPECIAL_ITEM_COUNT > 0 && Math.abs(getLevel() - player.getLevel()) < 9
							&& Rnd.chance(Config.SPECIAL_ITEM_DROP_CHANCE))
						ItemFunctions.addItem(player, Config.SPECIAL_ITEM_ID, Config.SPECIAL_ITEM_COUNT);
				}
			}
		}

		if(killer.getClan() != null)
		{
			int diff = killer.getLevel() - getLevel();
			if((diff < 15) && (diff > -15))
				killer.addKilledMob();
		}

		if((Config.SUBJUGATION_ENABLED) && (killer.getSayhasGrace() > 0))
			for(int i : SubjugationsHolder.getInstance().getFields().keySet())
			{
				SubjugationTemplate temp = SubjugationsHolder.getInstance().getFields().get(i);
				if((killer.getLevel() >= temp.getMinLevel()) && (killer.getLevel() <= temp.getMaxLevel()))
					if(ArrayUtils.contains(temp.getMobs(), getNpcId()))
					{
						long points = killer.getVarLong(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + i, 0);
						points += (getExpReward() / 5000);
						killer.setVar(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + i, points);
						killer.sendPacket(new ExSubjugationSidebar(killer, i));
					}
			}

		if(Config.STEADY_BOX_ENABLED)
		{
			int diff = killer.getLevel() - getLevel();
			if((diff < 15) && (diff > -15))
			{
				int killedMobs = killer.getVarInt(PlayerVariables.SB_KILLED_MOBS, 0) + 1;
				if(killedMobs > Config.STEADY_BOX_KILL_MOBS)
					killedMobs = Config.STEADY_BOX_KILL_MOBS;
				killer.setVar(PlayerVariables.SB_KILLED_MOBS, killedMobs);
				if(killedMobs == Config.STEADY_BOX_KILL_MOBS)
					killer.generateSteadyBox(true);
			}
		}
	}

	@Override
	public void onRandomAnimation()
	{
		if(System.currentTimeMillis() - _lastSocialAction > 10000L)
		{
			broadcastPacket(new SocialActionPacket(getObjectId(), 1));
			_lastSocialAction = System.currentTimeMillis();
		}
	}

	@Override
	public void startRandomAnimation()
	{
		// У мобов анимация обрабатывается в AI
	}

	@Override
	public int getKarma()
	{
		return 0;
	}

	/**
	 * Return True if this L2NpcInstance has drops that can be sweeped.<BR>
	 * <BR>
	 */
	public boolean isSpoiled()
	{
		return _isSpoiled;
	}

	public boolean isSpoiled(Player player)
	{
		if(!isSpoiled()) // если не заспойлен то false
			return false;

		// заспойлен этим игроком, и смерть наступила не более 20 секунд назад
		if(player.getObjectId() == spoilerId && (System.currentTimeMillis() - getDeathTime()) < 20000L)
			return true;

		if(player.isInParty())
			for(Player pm : player.getParty().getPartyMembers())
				if(pm.getObjectId() == spoilerId && getDistance(pm) < Config.ALT_PARTY_DISTRIBUTION_RANGE)
					return true;

		return false;
	}

	/**
	 * Set the spoil state of this L2NpcInstance.<BR>
	 * <BR>
	 * 
	 * @param player
	 */
	public boolean setSpoiled(Player player)
	{
		sweepLock.lock();
		try
		{
			if(isSpoiled())
				return false;
			_isSpoiled = true;
			spoilerId = player.getObjectId();
		}
		finally
		{
			sweepLock.unlock();
		}
		return true;
	}

	/**
	 * Return True if a Dwarf use Sweep on the L2NpcInstance and if item can be
	 * spoiled.<BR>
	 * <BR>
	 */
	public boolean isSweepActive()
	{
		sweepLock.lock();
		try
		{
			return _sweepItems != null && _sweepItems.size() > 0;
		}
		finally
		{
			sweepLock.unlock();
		}
	}

	public boolean takeSweep(final Player player)
	{
		sweepLock.lock();
		try
		{
			_sweeped = true;

			if(_sweepItems == null || _sweepItems.isEmpty())
			{
				clearSweep();
				return false;
			}

			for(RewardItem item : _sweepItems)
			{
				final ItemInstance sweep = ItemFunctions.createItem(item.itemId);
				sweep.setCount(item.count);

				if(player.isInParty() && player.getParty().isDistributeSpoilLoot())
				{
					player.getParty().distributeItem(player, sweep, null);
					continue;
				}

				if(!player.getInventory().validateCapacity(sweep) || !player.getInventory().validateWeight(sweep))
				{
					sweep.dropToTheGround(player, this);
					continue;
				}

				player.getInventory().addItem(sweep);

				SystemMessagePacket smsg;
				if(item.count == 1)
				{
					smsg = new SystemMessagePacket(SystemMsg.YOU_HAVE_OBTAINED_S1);
					smsg.addItemName(item.itemId);
					player.sendPacket(smsg);
				}
				else
				{
					smsg = new SystemMessagePacket(SystemMsg.YOU_HAVE_OBTAINED_S2_S1);
					smsg.addItemName(item.itemId);
					smsg.addLong(item.count);
					player.sendPacket(smsg);
				}

				if(player.isInParty())
				{
					if(item.count == 1)
					{
						smsg = new SystemMessagePacket(SystemMsg.C1_HAS_OBTAINED_S2_BY_USING_SWEEPER);
						smsg.addName(player);
						smsg.addItemName(item.itemId);
						player.getParty().getPartyLeader().sendPacket(smsg);
					}
					else
					{
						smsg = new SystemMessagePacket(SystemMsg.C1_HAS_OBTAINED_S3_S2_BY_USING_SWEEPER);
						smsg.addName(player);
						smsg.addItemName(item.itemId);
						smsg.addLong(item.count);
						player.getParty().getPartyLeader().sendPacket(smsg);
					}
				}
			}
			clearSweep();
			return true;
		}
		finally
		{
			sweepLock.unlock();
		}
	}

	public boolean isSweeped()
	{
		return _sweeped;
	}

	public void clearSweep()
	{
		sweepLock.lock();
		try
		{
			_isSpoiled = false;
			spoilerId = 0;
			_sweepItems = null;
		}
		finally
		{
			sweepLock.unlock();
		}
	}

	public void rollRewards(RewardList list, final Creature lastAttacker, Creature topDamager)
	{
		RewardType type = list.getType();
		if(type == RewardType.SWEEP && !isSpoiled())
			return;

		final Creature activeChar = type == RewardType.SWEEP ? lastAttacker : topDamager;
		final Player activePlayer = activeChar.getPlayer();

		if(activePlayer == null)
			return;

		final double penaltyMod = Experience.penaltyModifier(calculateLevelDiffForDrop(topDamager.getLevel()), 9);

		List<RewardItem> rewardItems = list.roll(activePlayer, penaltyMod, this);
		switch(type)
		{
			case SWEEP:
				_sweepItems = rewardItems;
				break;
			default:
				for(RewardItem drop : rewardItems)
				{
					if(!Config.DROP_ONLY_THIS.isEmpty() && !Config.DROP_ONLY_THIS.contains(drop.itemId))
					{
						if(!(Config.INCLUDE_RAID_DROP && isRaid()))
							return;
					}
					dropItem(activePlayer, drop.itemId, drop.count);
				}
				break;
		}
	}

	private double[] calculateExpAndSp(int level, double damage, double totalDamage)
	{
		int diff = Math.min(Math.max(0, level - getLevel()), Config.MONSTER_LEVEL_DIFF_EXP_PENALTY.length - 1);

		double xp = SafeMath.mulAndLimit((double) getExpReward(), damage / totalDamage);
		double sp = SafeMath.mulAndLimit((double) getSpReward(), damage / totalDamage);

		double mod = (100. - Config.MONSTER_LEVEL_DIFF_EXP_PENALTY[diff]) / 100.;
		xp = SafeMath.mulAndLimit(xp, mod);
		sp = SafeMath.mulAndLimit(sp, mod);

		xp = Math.max(0., xp);
		sp = Math.max(0., sp);

		return new double[] {
				xp,
				sp
		};
	}

	private double applyOverhit(Player killer, double xp)
	{
		if(xp > 0 && killer.getObjectId() == overhitAttackerId)
		{
			int overHitExp = calculateOverhitExp(xp);
			killer.sendPacket(SystemMsg.OVERHIT);
			killer.sendPacket(new ExMagicAttackInfo(killer.getObjectId(), getObjectId(), ExMagicAttackInfo.OVERHIT));
			xp += overHitExp;
		}
		return xp;
	}

	@Override
	public void setOverhitAttacker(Creature attacker)
	{
		overhitAttackerId = attacker == null ? 0 : attacker.getObjectId();
	}

	public double getOverhitDamage()
	{
		return _overhitDamage;
	}

	@Override
	public void setOverhitDamage(double damage)
	{
		_overhitDamage = damage;
	}

	public int calculateOverhitExp(final double normalExp)
	{
		double overhitPercentage = getOverhitDamage() * 100 / getMaxHp();
		if(overhitPercentage > 25)
			overhitPercentage = 25;
		double overhitExp = overhitPercentage / 100 * normalExp;
		setOverhitAttacker(null);
		setOverhitDamage(0);
		return (int) Math.round(overhitExp);
	}

	@Override
	public boolean isAggressive()
	{
		return (Config.ALT_CHAMPION_CAN_BE_AGGRO || getChampion() == 0) && super.isAggressive();
	}

	@Override
	public Faction getFaction()
	{
		if(getTemplate().isNoClan())
			return Faction.NONE;

		return Config.ALT_CHAMPION_CAN_BE_SOCIAL || getChampion() == 0 ? super.getFaction() : Faction.NONE;
	}

	@Override
	public boolean isMonster()
	{
		return true;
	}

	@Override
	public Clan getClan()
	{
		return null;
	}

	@Override
	public boolean isPeaceNpc()
	{
		return false;
	}
}