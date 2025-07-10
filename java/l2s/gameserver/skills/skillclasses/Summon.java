package l2s.gameserver.skills.skillclasses;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.data.xml.holder.NpcHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.FakePlayer;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.instances.SummonInstance;
import l2s.gameserver.model.instances.SummonInstance.RestoredSummon;
import l2s.gameserver.model.instances.SymbolInstance;
import l2s.gameserver.model.instances.TrapInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.skills.enums.SkillTargetType;
import l2s.gameserver.stats.Stats;
import l2s.gameserver.stats.funcs.FuncAbsorb;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.npc.NpcTemplate;

public class Summon extends Skill
{
	private static final Logger _log = LoggerFactory.getLogger(Summon.class);

	private static final int DEFAULT_LIFE_TIME = 2000000;

	private final SummonType _summonType;

	private final double _expPenalty;
	private final int _itemConsumeIdInTime;
	private final int _itemConsumeCountInTime;
	private final int _itemConsumeDelay;
	private final int _lifeTime;
	private final int _summonsCount;
	private final boolean _isSaveableSummon;
	private final boolean _randomOffset;

	private static enum SummonType
	{
		PET,
		SIEGE_SUMMON,
		TRAP,
		NPC,
		SYMBOL,
		CLONE,
		GROUND_ZONE
	}

	public Summon(StatsSet set)
	{
		super(set);

		_summonType = Enum.valueOf(SummonType.class, set.getString("summonType", "PET").toUpperCase());
		_expPenalty = set.getDouble("expPenalty", 0.f);
		_itemConsumeIdInTime = set.getInteger("itemConsumeIdInTime", 0);
		_itemConsumeCountInTime = set.getInteger("itemConsumeCountInTime", 0);
		_itemConsumeDelay = set.getInteger("itemConsumeDelay", 240) * 1000;
		_lifeTime = set.getInteger("lifeTime", (_summonType == SummonType.NPC || _summonType == SummonType.SYMBOL || _summonType == SummonType.GROUND_ZONE) ? -1 : DEFAULT_LIFE_TIME) * 1000;
		_summonsCount = Math.max(set.getInteger("summon_count", 1), 1);
		_isSaveableSummon = set.getBool("is_saveable_summon", true);
		_randomOffset = set.getBool("random_offset_on_spawn", _summonType == SummonType.CLONE);
	}

	@Override
	public boolean checkCondition(SkillEntry skillEntry, Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first, boolean sendMsg, boolean trigger)
	{
		if (!super.checkCondition(skillEntry, activeChar, target, forceUse, dontMove, first, sendMsg, trigger))
			return false;

		Player player = activeChar.getPlayer();
		if (player == null)
			return false;

		if (player.isProcessingRequest())
		{
			player.sendPacket(SystemMsg.PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
			return false;
		}

		switch (_summonType)
		{
			case TRAP:
			case CLONE:
			case GROUND_ZONE:
				if (player.isInPeaceZone() && isDebuff())
				{
					player.sendPacket(SystemMsg.A_MALICIOUS_SKILL_CANNOT_BE_USED_IN_A_PEACE_ZONE);
					return false;
				}
				break;
			case PET:
			case SIEGE_SUMMON:
				break;
			case SYMBOL:
				if (player.getSymbol() != null)
				{
					player.sendPacket(new SystemMessagePacket(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
					return false;
				}
				break;
		}

		return true;
	}

	@Override
	public void onEndCast(Creature caster, Set<Creature> targets)
	{
		super.onEndCast(caster, targets);

		Player activeChar = caster.getPlayer();
		if (activeChar == null)
			return;

		NpcTemplate npcTemplate;
		NpcInstance npc;
		switch (_summonType)
		{
			case TRAP:
				SkillEntry trapSkillEntry = getFirstAddedSkill();

				List<TrapInstance> traps = activeChar.getPlayer().getTraps();
				if (!traps.isEmpty()) // GOD updated only one trap
					traps.get(0).deleteMe();

				TrapInstance trap = new TrapInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(getNpcId()), activeChar, trapSkillEntry);
				activeChar.addTrap(trap);
				trap.spawnMe();
				break;
			case CLONE:
				FakePlayer fp;
				for (int i = 0; i < _summonsCount; i++)
				{
					fp = new FakePlayer(IdFactory.getInstance().getNextId(), activeChar.getTemplate(), activeChar, false);
					fp.setReflection(activeChar.getReflection());
					if (_randomOffset)
						fp.spawnMe(Location.findAroundPosition(activeChar, (int) (50 + fp.getCurrentCollisionRadius()), (int) (70 + fp.getCurrentCollisionRadius())));
					else
						fp.spawnMe(activeChar.getLoc());
					fp.setFollowMode(true);
				}
				break;
			case PET:
			case SIEGE_SUMMON:
				summon(activeChar, targets, null);
				break;
			case NPC:
				if (activeChar.hasSummon() || activeChar.isMounted())
					return;

				npcTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				npc = npcTemplate.getNewInstance();

				npc.setCurrentHp(npc.getMaxHp(), false);
				npc.setCurrentMp(npc.getMaxMp());
				npc.setHeading(activeChar.getHeading());
				npc.setReflection(activeChar.getReflection());
				npc.setOwner(activeChar);
				if (_randomOffset)
					npc.spawnMe(Location.findAroundPosition(activeChar, (int) (40 + npc.getCurrentCollisionRadius()), (int) (40 + npc.getCurrentCollisionRadius())));
				else
					npc.spawnMe(activeChar.getLoc());

				if (_lifeTime > 0)
					npc.startDeleteTask(_lifeTime);
				break;
			case SYMBOL:
				SymbolInstance symbol = activeChar.getSymbol();
				if (symbol != null)
				{
					activeChar.setSymbol(null);
					symbol.deleteMe();
				}

				npcTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				npc = npcTemplate.getNewInstance();

				npc.setCurrentHp(npc.getMaxHp(), false);
				npc.setCurrentMp(npc.getMaxMp());
				npc.setHeading(activeChar.getHeading());

				if (npc instanceof SymbolInstance)
				{
					symbol = (SymbolInstance) npc;
					activeChar.setSymbol(symbol);
					symbol.setOwner(activeChar);
				}

				Location loc = Location.findPointToStay(activeChar.getLoc(), 50, 100, activeChar.getReflection().getGeoIndex());
				if (activeChar.getGroundSkillLoc() != null)
				{
					loc = activeChar.getGroundSkillLoc();
					activeChar.setGroundSkillLoc(null);
				}
				npc.setReflection(activeChar.getReflection());
				npc.spawnMe(loc);

				if (_lifeTime > 0)
					npc.startDeleteTask(_lifeTime);
				break;
			case GROUND_ZONE:
				// @Rivelia. Similar to SYMBOL, except you can spawn more than one.
				if (activeChar.isMounted())
					return;

				npcTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				npc = npcTemplate.getNewInstance();

				npc.setCurrentHp(npc.getMaxHp(), false);
				npc.setCurrentMp(npc.getMaxMp());
				npc.setHeading(activeChar.getHeading());

				if (npc instanceof SymbolInstance)
					((SymbolInstance) npc).setOwner(activeChar);

				Location loc2 = activeChar.getLoc();
				if (activeChar.getGroundSkillLoc() != null)
				{
					loc2 = activeChar.getGroundSkillLoc();
					activeChar.setGroundSkillLoc(null);
				}
				npc.setReflection(activeChar.getReflection());
				npc.spawnMe(loc2);

				if (_lifeTime > 0)
					npc.startDeleteTask(_lifeTime);
				break;
		}
	}

	@Override
	public boolean isDebuff()
	{
		return getTargetType() == SkillTargetType.TARGET_CORPSE;
	}

	public void summon(Player player, Set<Creature> targets, RestoredSummon restored)
	{
		// Удаление трупа, если идет суммон из трупа.
		Location loc = null;
		if (restored == null)
		{
			if (getTargetType() == SkillTargetType.TARGET_CORPSE)
			{
				for (Creature target : targets)
				{
					if (target != null && target.isDead())
					{
						player.getAI().setAttackTarget(null);
						loc = target.getLoc();
						if (target.isNpc())
							((NpcInstance) target).endDecayTask();
						else if (target.isSummon())
							((SummonInstance) target).endDecayTask();
						else
							return; // кто труп ?
					}
				}
			}
		}
		else if (player.getSkillLevel(restored.skillId, 0) < restored.skillLvl)
			return;

		NpcTemplate summonTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
		if (summonTemplate == null)
		{
			_log.warn("Summon: Template ID " + getNpcId() + " is NULL FIX IT!");
			return;
		}

		SummonInstance currentSummon = player.getSummon();
		if (currentSummon != null)
			currentSummon.unSummon(false);

		final SummonInstance summon = new SummonInstance(IdFactory.getInstance().getNextId(), summonTemplate, player, _lifeTime, _itemConsumeIdInTime, _itemConsumeCountInTime, _itemConsumeDelay, this, _isSaveableSummon);

		player.setSummon(summon);

		summon.setTitle(Servitor.TITLE_BY_OWNER_NAME);
		summon.setExpPenalty(_expPenalty);
		summon.setExp(Experience.getExpForLevel(Math.min(summon.getLevel(), Experience.getMaxAvailableLevel())));
		summon.setHeading(player.getHeading());
		summon.setReflection(player.getReflection());
		summon.setRunning();
		summon.spawnMe(loc == null ? Location.findAroundPosition(player, 50, 70) : loc);
		summon.setFollowMode(true);

		if (summon.getSkillLevel(4140) > 0)
			summon.altUseSkill(SkillEntry.makeSkillEntry(SkillEntryType.NONE, 4140, summon.getSkillLevel(4140)), player);

		if (summon.getName().equalsIgnoreCase("Shadow"))// FIXME [G1ta0] идиотский хардкод
			summon.getStat().addFuncs(new FuncAbsorb(Stats.VAMPIRIC_ATTACK, 0x40, this, 15, StatsSet.simpleStatsSet("chance", 20.)));

		if (restored == null)
			summon.setCurrentHpMp(summon.getMaxHp(), summon.getMaxMp(), false);
		else
		{
			summon.setCurrentHpMp(restored.curHp, restored.curMp, false);
			summon.setConsumeCountdown(restored.time);
		}

		if (_summonType == SummonType.SIEGE_SUMMON)
		{
			summon.setSiegeSummon(true);

			for (SiegeEvent<?, ?> siegeEvent : player.getEvents(SiegeEvent.class))
				siegeEvent.addSiegeSummon(player, summon);
		}

		player.getListeners().onSummonServitor(summon);
	}
}