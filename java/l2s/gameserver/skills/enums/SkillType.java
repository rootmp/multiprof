package l2s.gameserver.skills.enums;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.model.Skill;
import l2s.gameserver.skills.skillclasses.Balance;
import l2s.gameserver.skills.skillclasses.BuffCharger;
import l2s.gameserver.skills.skillclasses.CPDam;
import l2s.gameserver.skills.skillclasses.Call;
import l2s.gameserver.skills.skillclasses.ChainHeal;
import l2s.gameserver.skills.skillclasses.Charge;
import l2s.gameserver.skills.skillclasses.ClanGate;
import l2s.gameserver.skills.skillclasses.Continuous;
import l2s.gameserver.skills.skillclasses.Craft;
import l2s.gameserver.skills.skillclasses.DebuffRenewal;
import l2s.gameserver.skills.skillclasses.Decoy;
import l2s.gameserver.skills.skillclasses.Default;
import l2s.gameserver.skills.skillclasses.DefuseTrap;
import l2s.gameserver.skills.skillclasses.DestroySummon;
import l2s.gameserver.skills.skillclasses.DetectTrap;
import l2s.gameserver.skills.skillclasses.Disablers;
import l2s.gameserver.skills.skillclasses.Drain;
import l2s.gameserver.skills.skillclasses.DrainSoul;
import l2s.gameserver.skills.skillclasses.EffectsFromSkills;
import l2s.gameserver.skills.skillclasses.EnergyReplenish;
import l2s.gameserver.skills.skillclasses.ExtractStone;
import l2s.gameserver.skills.skillclasses.HideHairAccessories;
import l2s.gameserver.skills.skillclasses.LethalShot;
import l2s.gameserver.skills.skillclasses.MDam;
import l2s.gameserver.skills.skillclasses.ManaDam;
import l2s.gameserver.skills.skillclasses.PDam;
import l2s.gameserver.skills.skillclasses.PcBangPointsAdd;
import l2s.gameserver.skills.skillclasses.PetFeed;
import l2s.gameserver.skills.skillclasses.PetSummon;
import l2s.gameserver.skills.skillclasses.Recall;
import l2s.gameserver.skills.skillclasses.Replace;
import l2s.gameserver.skills.skillclasses.Restoration;
import l2s.gameserver.skills.skillclasses.Resurrect;
import l2s.gameserver.skills.skillclasses.Ride;
import l2s.gameserver.skills.skillclasses.Sacrifice;
import l2s.gameserver.skills.skillclasses.SayhasGraceHeal;
import l2s.gameserver.skills.skillclasses.ShiftAggression;
import l2s.gameserver.skills.skillclasses.StealBuff;
import l2s.gameserver.skills.skillclasses.Summon;
import l2s.gameserver.skills.skillclasses.SummonSiegeFlag;
import l2s.gameserver.skills.skillclasses.Sweep;
import l2s.gameserver.skills.skillclasses.TakeCastle;
import l2s.gameserver.skills.skillclasses.TakeFortress;
import l2s.gameserver.skills.skillclasses.TrapActivation;
import l2s.gameserver.skills.skillclasses.Unlock;
import l2s.gameserver.templates.StatsSet;

/**
 * @author Hl4p3x
 */
public enum SkillType
{
	ADD_PC_BANG(PcBangPointsAdd.class),
	AIEFFECTS(Continuous.class),
	BALANCE(Balance.class),
	BUFF(Continuous.class),
	BUFF_CHARGER(BuffCharger.class),
	CALL(Call.class),
	CHAIN_HEAL(ChainHeal.class),
	CHARGE(Charge.class),
	CLAN_GATE(ClanGate.class),
	CPDAM(CPDam.class),
	CPHOT(Continuous.class),
	CRAFT(Craft.class),
	DEBUFF(Continuous.class),
	DEBUFF_RENEWAL(DebuffRenewal.class),
	DECOY(Decoy.class),
	DEFUSE_TRAP(DefuseTrap.class),
	DELETE_HATE(Continuous.class),
	DESTROY_SUMMON(DestroySummon.class),
	DETECT_TRAP(DetectTrap.class),
	DISCORD(Continuous.class),
	DOT(Continuous.class),
	DRAIN(Drain.class),
	DRAIN_SOUL(DrainSoul.class),
	EFFECT(Skill.class),
	EFFECTS_FROM_SKILLS(EffectsFromSkills.class),
	ENCHANT_ARMOR,
	ENCHANT_WEAPON,
	ENERGY_REPLENISH(EnergyReplenish.class),
	EXTRACT_STONE(ExtractStone.class),
	HARDCODED(Skill.class),
	HEAL(Continuous.class),
	HEAL_PERCENT(Continuous.class),
	HIDE_HAIR_ACCESSORIES(HideHairAccessories.class),
	HOT(Continuous.class),
	LETHAL_SHOT(LethalShot.class),
	LUCK,
	MANADAM(ManaDam.class),
	MDAM(MDam.class),
	MDOT(Continuous.class),
	MPHOT(Continuous.class),
	MUTE(Disablers.class),
	NOTDONE,
	NOTUSED,
	PARALYZE(Disablers.class),
	PASSIVE,
	PDAM(PDam.class),
	PET_FEED(PetFeed.class),
	PET_SUMMON(PetSummon.class),
	POISON(Continuous.class),
	RECALL(Recall.class),
	REPLACE(Replace.class),
	RESTORATION(Restoration.class),
	RESURRECT(Resurrect.class),
	RIDE(Ride.class),
	ROOT(Disablers.class),
	SACRIFICE(Sacrifice.class),
	SAYHAS_GRACE_HEAL(SayhasGraceHeal.class),
	SHIFT_AGGRESSION(ShiftAggression.class),
	SLEEP(Disablers.class),
	STEAL_BUFF(StealBuff.class),
	STUN(Disablers.class),
	SUMMON(Summon.class),
	SUMMON_FLAG(SummonSiegeFlag.class),
	SWEEP(Sweep.class),
	TAKECASTLE(TakeCastle.class),
	TAKEFORTRESS(TakeFortress.class),
	TRAP_ACTIVATION(TrapActivation.class),
	UNLOCK(Unlock.class),
	WATCHER_GAZE(Continuous.class);

	protected static final Logger LOGGER = LoggerFactory.getLogger(SkillType.class);
	private final Class<? extends Skill> _classSkill;

	private SkillType()
	{
		_classSkill = Default.class;
	}

	private SkillType(Class<? extends Skill> getClass)
	{
		_classSkill = getClass;
	}

	public Skill makeSkill(StatsSet set)
	{
		try
		{
			final Constructor<? extends Skill> c = _classSkill.getConstructor(StatsSet.class);
			return c.newInstance(set);
		}
		catch (final Exception e)
		{
			LOGGER.error("Skill ID[" + set.getInteger("skill_id") + "], LEVEL[" + set.getInteger("level") + "]", e);
			throw new RuntimeException(e);
		}
	}

	public final boolean isPvM()
	{
		switch (this)
		{
			case DISCORD:
				return true;
			default:
				return false;
		}
	}

	public boolean isAI()
	{
		switch (this)
		{
			case AIEFFECTS:
			case DELETE_HATE:
				return true;
			default:
				return false;
		}
	}

	public final boolean isPvpSkill()
	{
		switch (this)
		{
			case DEBUFF:
			case DOT:
			case MDOT:
			case MUTE:
			case PARALYZE:
			case POISON:
			case ROOT:
			case SLEEP:
			case MANADAM:
			case STEAL_BUFF:
			case DELETE_HATE:
			case DEBUFF_RENEWAL:
				return true;
			default:
				return false;
		}
	}

	public boolean isDebuff()
	{
		switch (this)
		{
			case AIEFFECTS:
			case DEBUFF:
			case DOT:
			case DRAIN:
			case DRAIN_SOUL:
			case LETHAL_SHOT:
			case MANADAM:
			case MDAM:
			case MDOT:
			case MUTE:
			case PARALYZE:
			case PDAM:
			case CPDAM:
			case POISON:
			case ROOT:
			case SLEEP:
			case STUN:
			case SWEEP:
			case DELETE_HATE:
			case STEAL_BUFF:
			case DISCORD:
			case DEBUFF_RENEWAL:
				return true;
			default:
				return false;
		}
	}
}
