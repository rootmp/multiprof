package l2s.gameserver.stats;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.GameTimeController;
import l2s.gameserver.data.xml.holder.HitCondBonusHolder;
import l2s.gameserver.data.xml.holder.KarmaIncreaseDataHolder;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.base.BaseStats;
import l2s.gameserver.model.base.Element;
import l2s.gameserver.model.base.ElementalElement;
import l2s.gameserver.model.base.HitCondBonusType;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExMagicAttackInfo;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.skills.BasicPropertyResist;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.AbnormalType;
import l2s.gameserver.skills.enums.BasicProperty;
import l2s.gameserver.skills.enums.SkillCastingType;
import l2s.gameserver.skills.enums.SkillTrait;
import l2s.gameserver.skills.enums.SkillTraitType;
import l2s.gameserver.skills.enums.SkillType;
import l2s.gameserver.templates.item.WeaponTemplate;
import l2s.gameserver.templates.item.WeaponTemplate.WeaponType;
import l2s.gameserver.utils.PositionUtils;

public class Formulas
{
	private static final Lock _lock = new ReentrantLock();

	public static class AttackInfo
	{
		public double damage = 0;
		public double defence = 0;
		public boolean crit = false;
		public boolean shld = false;
		public boolean miss = false;
		public boolean blow = false;
		public double elementalDamage = 0;
		public boolean elementalCrit = false;
	}

	public static AttackInfo calcElementalDamage(Creature attacker, Creature target, Skill skill, double atkMod, boolean useShot)
	{
		return calcElementalDamage(attacker, target, skill, skill != null ? skill.getPower(target) : 0., atkMod, useShot);
	}

	public static AttackInfo calcElementalDamage(Creature attacker, Creature target, Skill skill, double power, double atkMod, boolean useShot)
	{
		_lock.lock();
		final AttackInfo info = new AttackInfo();
		try
		{
			// TODO: Переделать по оффу.
			final ElementalElement element = attacker.getActiveElement();
			if(element != ElementalElement.NONE)
			{
				// Influence protection right,judging on
				// table:https://4gameforum.com/threads/709735/
				final double elementalDamagePower = attacker.getStat().getElementalAttackPower(element) - target.getStat().getElementalDefence(element);
				double damage = 0.;
				if(skill == null)
				{
					damage = attacker.getPAtk(target) * atkMod;
					if(useShot)
					{
						damage *= ((100 + attacker.getChargedSoulshotPower()) / 100.);
					}
					damage *= 135. / target.getPDef(attacker);
					damage *= elementalDamagePower / 806.;
				}
				else if(power > 0)
				{
					if(skill.isMagic())
					{
						double mAtk = attacker.getMAtk(target, skill) * atkMod;
						if(useShot)
						{
							mAtk *= ((100 + attacker.getChargedSpiritshotPower()) / 100.);
						}

						damage = (45 * power * Math.sqrt(mAtk)) / target.getMDef(null, skill);
						damage *= elementalDamagePower / 806.;
					}
					else
					{
						damage = attacker.getPAtk(target) * atkMod + power;
						if(useShot)
						{
							damage *= ((100 + attacker.getChargedSoulshotPower()) / 100.);
						}
						damage *= 35. / target.getPDef(attacker);
						damage *= elementalDamagePower / 806.;
					}
				}

				if(damage != 0)
				{
					final double critChance = attacker.getStat().getElementalCritRate(element) / 100. + 1 * 10;
					if(Rnd.chance(critChance))
					{
						info.elementalCrit = true;
						final double critMod = 1. + attacker.getStat().getElementalCritAttack(element) * 0.4;
						damage += Math.abs(damage) * critMod;
					}
				}
				info.elementalDamage = damage;
			}
		}
		finally
		{
			_lock.unlock();
		}
		return info;
	}

	/**
	 * For simple hits patk = patk For a simple hit crit: patk = patk * (1 +
	 * crit_damage_rcpt) * crit_damage_mod + crit_damage_static For blow skills TODO
	 * For skill crits, damage is simply doubled, buffs do not affect (except for
	 * blow, above for them) patk = (1 + crit_damage_rcpt) * (patk + skill_power)
	 * For normal attacks damage = patk * ss_bonus * 70 / pdef
	 */
	public static AttackInfo calcAutoAttackDamage(Creature attacker, Creature target, double pAtkMod, boolean range, boolean useShot, boolean canCrit)
	{
		_lock.lock();
		final AttackInfo info = new AttackInfo();
		try
		{
			final double pAtk = attacker.getPAtk(target) * pAtkMod;
			final double pDef = target.getPDef(attacker);
			info.damage = pAtk;
			info.defence = pDef;
			info.crit = canCrit && calcPCrit(attacker, target, null, false);
			info.shld = calcShldUse(attacker, target);
			info.miss = false;
			info.blow = false;
			final boolean isPvP = attacker.isPlayable() && target.isPlayable();
			final boolean isPvE = attacker.isPlayable() && target.isNpc();

			if(info.shld)
			{
				info.defence += target.getShldDef();
			}

			if(info.crit)
			{
				double critDmg = info.damage;
				critDmg *= 2 * attacker.getStat().getMul(Stats.CRITICAL_DAMAGE, target, null);
				critDmg += attacker.getStat().getAdd(Stats.CRITICAL_DAMAGE, target, null);
				critDmg = target.getStat().calc(Stats.P_CRIT_DAMAGE_RECEPTIVE, critDmg);
				critDmg = Math.max(0, critDmg);
				info.damage += critDmg;
			}

			if(range)
			{
				info.damage += Math.max(0, (Config.LONG_RANGE_AUTO_ATTACK_P_ATK_MOD - 1)) * pAtk;
			}
			else
			{
				info.damage += Math.max(0, (Config.SHORT_RANGE_AUTO_ATTACK_P_ATK_MOD - 1)) * pAtk;
			}

			if(attacker.isDistortedSpace())
			{
				info.damage += pAtk * 0.2;
			}
			else
			{
				switch(PositionUtils.getDirectionTo(target, attacker))
				{
					case BEHIND:
						info.damage += pAtk * 0.2;
						break;
					case SIDE:
						info.damage += pAtk * 0.1;
						break;
				}
			}

			info.damage *= 1 + (Rnd.get() * attacker.getRandomDamage() * 2 - attacker.getRandomDamage()) / 100.;

			if(useShot)
			{
				info.damage *= ((100 + attacker.getChargedSoulshotPower()) / 100.);
			}

			info.damage *= 70. / info.defence; // 177?
			info.damage *= calcAttackTraitBonus(attacker, target);
			info.damage *= calcAttributeBonus(attacker, target, null);

			info.damage = attacker.getStat().calc(Stats.INFLICTS_P_DAMAGE_POWER, info.damage, target, null);
			info.damage = target.getStat().calc(Stats.RECEIVE_P_DAMAGE_POWER, info.damage, attacker, null);

			if(info.shld)
			{
				if(Rnd.chance(Config.EXCELLENT_SHIELD_BLOCK_CHANCE))
				{
					info.damage = Config.EXCELLENT_SHIELD_BLOCK_RECEIVED_DAMAGE;
					return info;
				}
			}

			if(isPvP)
			{
				info.damage *= attacker.getStat().calc(Stats.PVP_PHYS_DMG_BONUS, 1);
				info.damage /= target.getStat().calc(Stats.PVP_PHYS_DEFENCE_BONUS, 1);
			}
			else if(isPvE)
			{
				info.damage *= attacker.getStat().calc(Stats.PVE_PHYS_DMG_BONUS, 1);
				info.damage /= target.getStat().calc(Stats.PVE_PHYS_DEFENCE_BONUS, 1);
			}

			if(info.crit)
			{
				info.damage = info.damage * getPCritDamageMode(attacker, true);
			}
			else
			{
				info.damage = info.damage * getPDamModifier(attacker);
			}

			final AttackInfo elementalInfo = calcElementalDamage(attacker, target, null, pAtkMod, useShot);
			if(elementalInfo != null)
			{
				info.elementalDamage = elementalInfo.elementalDamage;
				info.elementalCrit = elementalInfo.elementalCrit;
				info.damage += elementalInfo.elementalDamage;
			}
		}
		finally
		{
			_lock.unlock();
		}
		return info;
	}

	public static AttackInfo calcSkillPDamage(Creature attacker, Creature target, Skill skill, boolean blow, boolean useShot)
	{
		return calcSkillPDamage(attacker, target, skill, skill.getPower(target), blow, useShot);
	}

	public static AttackInfo calcSkillPDamage(Creature attacker, Creature target, Skill skill, double power, boolean blow, boolean useShot)
	{
		_lock.lock();
		final AttackInfo info = new AttackInfo();
		try
		{
			// @Rivelia. Send empty AttackInfo so it does not show up in system messages.
			if(power == 0)
				return info;

			final double pAtk = attacker.getPAtk(target);
			info.damage = pAtk;
			info.defence = target.getPDef(attacker);
			info.blow = blow;
			info.crit = calcPCrit(attacker, target, skill, info.blow);
			info.shld = !skill.getShieldIgnore() && calcShldUse(attacker, target);
			info.miss = false;
			final boolean isPvP = attacker.isPlayable() && target.isPlayable();
			final boolean isPvE = attacker.isPlayable() && target.isNpc();

			if(info.shld)
			{
				double shldDef = target.getShldDef();
				if(skill.getShieldIgnorePercent() > 0)
				{
					shldDef -= shldDef * skill.getShieldIgnorePercent() / 100.;
				}
				info.defence += shldDef;
			}

			if(skill.getDefenceIgnorePercent() > 0)
			{
				info.defence *= (1. - (skill.getDefenceIgnorePercent() / 100.));
			}

			if(info.damage > 0 && skill.canBeEvaded() && Rnd.chance(target.getStat().calc(Stats.P_SKILL_EVASION, 100, attacker, skill) - 100))
			{
				// @Rivelia. info.miss makes the Damage Text "Evaded" appear.
				info.miss = true;
				info.damage = 0;
				return info;
			}

			info.damage += attacker.getStat().calc(Stats.P_SKILL_POWER, (attacker.isServitor() ? Config.SERVITOR_P_SKILL_POWER_MODIFIER : 1.) + power);
			info.damage += attacker.getStat().calc(Stats.P_SKILL_POWER_STATIC);

			// Заряжаемые скилы имеют постоянный урон
			if(!skill.isChargeBoost())
			{
				info.damage *= 1 + (Rnd.get() * attacker.getRandomDamage() * 2 - attacker.getRandomDamage()) / 100.;
			}

			if(info.blow)
			{
				double critDmg = info.damage;
				critDmg *= attacker.getStat().getMul(Stats.CRITICAL_DAMAGE, target, skill) * 0.666;
				critDmg += 6 * attacker.getStat().getAdd(Stats.CRITICAL_DAMAGE, target, skill);
				critDmg -= (critDmg - target.getStat().calc(Stats.P_SKILL_CRIT_DAMAGE_RECEPTIVE, critDmg)) / 2;
				critDmg = Math.max(0, critDmg);
				info.damage += critDmg;
			}

			if(skill.isChargeBoost())
			{
				int force = attacker.getIncreasedForce();
				if(force > 3)
				{
					force = 3;
				}

				// @Rivelia. Momentum increases damage up to 30% if 3 forces used, so 10% per
				// momentum.
				info.damage *= 1 + 0.1 * force;
			}

			if(info.crit)
			{
				if(info.blow)
				{
					info.damage *= 2;
				}
				else
				{
					double critDmg = info.damage;
					critDmg *= 2 * attacker.getStat().getMul(Stats.SKILL_CRITICAL_DAMAGE, target, skill);
					critDmg += attacker.getStat().getAdd(Stats.SKILL_CRITICAL_DAMAGE, target, null);
					info.damage += critDmg;
				}
			}

			final ElementalElement element = attacker.getActiveElement();
			if(element != ElementalElement.NONE)
			{
				// TODO: Make formulas.
				// info.elementalDamage = 1;
				// info.elementalCrit = Rnd.chance(5);
			}

			if(attacker.isDistortedSpace())
			{
				info.damage += pAtk * 0.2;
			}
			else
			{
				switch(PositionUtils.getDirectionTo(target, attacker))
				{
					case BEHIND:
						info.damage += pAtk * 0.2;
						break;
					case SIDE:
						info.damage += pAtk * 0.1;
						break;
				}
			}

			if(useShot)
			{
				info.damage *= ((100 + attacker.getChargedSoulshotPower()) / 100.);
			}

			info.damage *= 70. / info.defence; // 177?
			info.damage *= calcWeaponTraitBonus(attacker, target);
			info.damage *= calcGeneralTraitBonus(attacker, target, skill.getTraitType(), true);
			info.damage *= calcAttributeBonus(attacker, target, skill);

			info.damage = attacker.getStat().calc(Stats.INFLICTS_P_DAMAGE_POWER, info.damage, target, skill);
			info.damage = target.getStat().calc(Stats.RECEIVE_P_DAMAGE_POWER, info.damage, attacker, skill);

			if(info.shld)
			{
				if(Rnd.chance(Config.EXCELLENT_SHIELD_BLOCK_CHANCE))
				{
					info.damage = Config.EXCELLENT_SHIELD_BLOCK_RECEIVED_DAMAGE;
					return info;
				}
			}

			if(isPvP)
			{
				info.damage *= attacker.getStat().calc(Stats.PVP_PHYS_SKILL_DMG_BONUS, 1);
				info.damage /= target.getStat().calc(Stats.PVP_PHYS_SKILL_DEFENCE_BONUS, 1);
			}
			else if(isPvE)
			{
				info.damage *= attacker.getStat().calc(Stats.PVE_PHYS_SKILL_DMG_BONUS, 1);
				info.damage /= target.getStat().calc(Stats.PVE_PHYS_SKILL_DEFENCE_BONUS, 1);
			}

			if(info.crit)
			{
				info.damage = info.damage * getPCritDamageMode(attacker, false);
			}
			if(info.blow)
			{
				info.damage = info.damage * Config.ALT_BLOW_DAMAGE_MOD;
			}
			if(!info.crit && !info.blow)
			{
				info.damage = info.damage * getPDamModifier(attacker);
			}

			if(info.damage > 0 && skill.isDeathlink())
			{
				info.damage *= 1.8 * ((skill.isPhysic() ? 2.0 : 1.0) - attacker.getCurrentHpRatio());
			}

			if(info.blow)
			{
				// Тупая заглушка, переделать.
				if(attacker.getSkillCast(SkillCastingType.NORMAL).isCriticalBlow()
						&& attacker.getSkillCast(SkillCastingType.NORMAL).getSkillEntry().getTemplate().equals(skill))
				{
					//
				}
				else if(attacker.getSkillCast(SkillCastingType.NORMAL_SECOND).isCriticalBlow()
						&& attacker.getSkillCast(SkillCastingType.NORMAL_SECOND).getSkillEntry().getTemplate().equals(skill))
				{
					//
				}
				else
					return null;
			}

			final AttackInfo elementalInfo = calcElementalDamage(attacker, target, skill, 1., useShot);
			if(elementalInfo != null)
			{
				info.elementalDamage = elementalInfo.elementalDamage;
				info.elementalCrit = elementalInfo.elementalCrit;
				info.damage += elementalInfo.elementalDamage;
			}

			if(info.damage > 0)
			{
				final WeaponTemplate weaponItem = attacker.getActiveWeaponTemplate();
				if(skill.getIncreaseOnPole() > 0. && weaponItem != null && weaponItem.getItemType() == WeaponType.POLE)
				{
					info.damage *= skill.getIncreaseOnPole();
				}
				if(skill.getDecreaseOnNoPole() > 0. && weaponItem != null && weaponItem.getItemType() != WeaponType.POLE)
				{
					info.damage *= skill.getDecreaseOnNoPole();
				}

				if(calcStunBreak(info.crit, true, false))
				{
					target.getAbnormalList().stop(AbnormalType.STUN);
					target.getAbnormalList().stop(AbnormalType.TURN_FLEE);
				}

				if(calcCastBreak(target, info.crit))
				{
					target.abortCast(false, true);
				}

				for(final Abnormal abnormal : target.getAbnormalList())
				{
					final double chance = info.crit ? abnormal.getSkill().getOnCritCancelChance() : abnormal.getSkill().getOnAttackCancelChance();
					if(chance > 0 && Rnd.chance(chance))
					{
						abnormal.exit();
					}
				}
			}
		}
		finally
		{
			_lock.unlock();
		}
		return info;
	}

	public static double calcLethalDamage(Creature attacker, Creature target, Skill skill)
	{
		_lock.lock();
		double damage = 0.;
		try
		{
			if((skill == null) || target.isLethalImmune())
				return 0.;

			final double deathRcpt = 0.01 * target.getStat().calc(Stats.DEATH_VULNERABILITY, attacker, skill);
			final double lethal1Chance = skill.getLethal1(attacker) * deathRcpt;
			final double lethal2Chance = skill.getLethal2(attacker) * deathRcpt;

			if(Rnd.chance(lethal2Chance))
			{
				if(target.isPlayer())
				{
					damage = target.getCurrentHp() + target.getCurrentCp() - 1.1;
					target.sendPacket(SystemMsg.LETHAL_STRIKE);
				}
				else
				{
					damage = target.getCurrentHp() - 1;
				}
				attacker.sendPacket(SystemMsg.YOUR_LETHAL_STRIKE_WAS_SUCCESSFUL);
			}
			else if(Rnd.chance(lethal1Chance))
			{
				if(target.isPlayer())
				{
					damage = target.getCurrentCp();
					target.sendPacket(SystemMsg.YOUR_CP_WAS_DRAINED_BECAUSE_YOU_WERE_HIT_WITH_A_CP_SIPHON_SKILL);
				}
				else
				{
					damage = target.getCurrentHp() / 2.;
				}
				attacker.sendPacket(SystemMsg.CP_SIPHON);
			}
		}
		finally
		{
			_lock.unlock();
		}
		return damage;
	}

	private static double getMSimpleDamageMode(Creature attacker) // ONLY for 4-th proffs
	{
		if(!attacker.isPlayer())
		{ return Config.ALT_M_SIMPLE_DAMAGE_MOD; }
		return Config.ALT_M_SIMPLE_DAMAGE_MOD;
	}

	public static double getMCritDamageMode(Creature attacker) // ONLY for 4-th proffs
	{
		if(!attacker.isPlayer())
		{ return Config.ALT_M_CRIT_DAMAGE_MOD; }
		return Config.ALT_M_CRIT_DAMAGE_MOD;
	}

	private static double getPDamModifier(Creature attacker) // ONLY for 4-th proffs
	{
		if(!attacker.isPlayer())
		{ return Config.ALT_P_DAMAGE_MOD; }
		return Config.ALT_P_DAMAGE_MOD;
	}

	private static double getPCritDamageMode(Creature attacker, boolean notSkill) // ONLY for 4-th proffs
	{
		if(!attacker.isPlayer())
		{ return Config.ALT_P_CRIT_DAMAGE_MOD; }
		return Config.ALT_P_CRIT_DAMAGE_MOD;
	}

	private static double getPCritChanceMode(Creature attacker) // ONLY for 4-th proffs
	{
		if(!attacker.isPlayer())
		{ return Config.ALT_P_CRIT_CHANCE_MOD; }
		return Config.ALT_P_CRIT_CHANCE_MOD;
	}

	private static double getMCritChanceMode(Creature attacker) // ONLY for 4-th proffs
	{
		if(!attacker.isPlayer())
		{ return Config.ALT_M_CRIT_CHANCE_MOD; }
		return Config.ALT_M_CRIT_CHANCE_MOD;
	}

	public static AttackInfo calcMagicDam(Creature attacker, Creature target, Skill skill, boolean useShot, boolean canMiss)
	{
		return calcMagicDam(attacker, target, skill, skill.getPower(target), useShot, canMiss);
	}

	public static AttackInfo calcMagicDam(Creature attacker, Creature target, Skill skill, double power, boolean useShot, boolean canMiss)
	{
		_lock.lock();
		final AttackInfo info = new AttackInfo();
		try
		{
			final boolean isPvP = attacker.isPlayable() && target.isPlayable();
			final boolean isPvE = attacker.isPlayable() && target.isNpc();
			// @Rivelia. If skill doesn't ignore shield and CalcShieldUse returns true,
			// shield = true.
			final boolean shield = !skill.getShieldIgnore() && calcShldUse(attacker, target);

			double mAtk = attacker.getMAtk(target, skill);

			if(useShot)
			{
				mAtk *= ((100 + attacker.getChargedSpiritshotPower()) / 100.);
			}

			double mdef = target.getMDef(null, skill);

			if(shield)
			{
				double shldDef = target.getShldDef();
				if(skill.getShieldIgnorePercent() > 0)
				{
					shldDef -= shldDef * skill.getShieldIgnorePercent() / 100.;
				}
				mdef += shldDef;
			}

			if(skill.getDefenceIgnorePercent() > 0)
			{
				mdef *= (1. - (skill.getDefenceIgnorePercent() / 100.));
			}

			mdef = Math.max(mdef, 1);

			if(power == 0)
				return info;

			info.damage = (91 * power * Math.sqrt(mAtk)) / mdef; // 41?

			if(target.isTargetUnderDebuff())
			{
				info.damage *= skill.getPercentDamageIfTargetDebuff();
			}

			if(canMiss)
			{
				// TODO: [Bonux] Переделать по оффу.
				if(calcMagicHitMiss(skill, attacker, target))
				{
					info.miss = true;
					info.damage = 0;
					return info;
				}
			}

			info.damage *= 1 + (((Rnd.get() * attacker.getRandomDamage() * 2) - attacker.getRandomDamage()) / 100.);
			info.damage += Math.max(0., attacker.getStat().calc(Stats.M_SKILL_POWER, (attacker.isServitor() ? Config.SERVITOR_M_SKILL_POWER_MODIFIER : 1.)
					+ power));
			info.crit = calcMCrit(attacker, target, skill);

			if(info.crit)
			{
				// @Rivelia. Based on config, Magic skills can be reduced by Critical Damage
				// Reduction if Critical Damage Receptive < 1.
				if(Config.ENABLE_CRIT_DMG_REDUCTION_ON_MAGIC)
				{
					double critDmg = info.damage;
					critDmg *= 1 + attacker.getStat().getMul(Stats.MAGIC_CRITICAL_DMG, target, skill);
					critDmg += attacker.getStat().getAdd(Stats.MAGIC_CRITICAL_DMG, target, skill);
					critDmg *= getMCritDamageMode(attacker);
					final double tempDamage = target.getStat().calc(Stats.M_CRIT_DAMAGE_RECEPTIVE, critDmg, attacker, skill);
					critDmg = Math.min(tempDamage, critDmg);
					critDmg = Math.max(0, critDmg);
					info.damage += critDmg;
				}
				else
				{
					info.damage *= 1 + attacker.getStat().getMul(Stats.MAGIC_CRITICAL_DMG, target, skill);
					info.damage += attacker.getStat().getAdd(Stats.MAGIC_CRITICAL_DMG, target, skill);
					info.damage *= getMCritDamageMode(attacker);
				}
			}
			else
			{
				info.damage = info.damage * getMSimpleDamageMode(attacker);
			}

			info.damage *= calcGeneralTraitBonus(attacker, target, skill.getTraitType(), true);
			info.damage *= calcAttributeBonus(attacker, target, skill);

			final ElementalElement element = attacker.getActiveElement();
			if(element != ElementalElement.NONE)
			{
				// TODO: Make formulas.
				info.elementalDamage = 1;
				info.elementalCrit = Rnd.chance(5);
			}

			info.damage = attacker.getStat().calc(Stats.INFLICTS_M_DAMAGE_POWER, info.damage, target, skill);
			info.damage = target.getStat().calc(Stats.RECEIVE_M_DAMAGE_POWER, info.damage, attacker, skill);

			if(shield)
			{
				info.shld = true;
				if(Rnd.chance(Config.EXCELLENT_SHIELD_BLOCK_CHANCE))
				{
					info.damage = Config.EXCELLENT_SHIELD_BLOCK_RECEIVED_DAMAGE;
					return info;
				}
			}

			final int levelDiff = target.getLevel() - attacker.getLevel();
			if(info.damage > 0 && skill.isDeathlink())
			{
				info.damage *= 1.8 * (1.0 - attacker.getCurrentHpRatio());
			}

			if(info.damage > 0 && skill.isBasedOnTargetDebuff())
			{
				info.damage *= 1 + 0.05 * target.getAbnormalList().size();
			}

			if(skill.getSkillType() == SkillType.MANADAM)
			{
				info.damage = Math.max(1, info.damage / 4.);
			}
			else if(info.damage > 0)
			{
				if(isPvP)
				{
					info.damage *= attacker.getStat().calc(Stats.PVP_MAGIC_SKILL_DMG_BONUS, 1);
					info.damage /= target.getStat().calc(Stats.PVP_MAGIC_SKILL_DEFENCE_BONUS, 1);
				}
				else if(isPvE)
				{
					info.damage *= attacker.getStat().calc(Stats.PVE_MAGIC_SKILL_DMG_BONUS, 1);
					info.damage /= target.getStat().calc(Stats.PVE_MAGIC_SKILL_DEFENCE_BONUS, 1);
				}
			}

			final double magic_rcpt = target.getStat().calc(Stats.MAGIC_RESIST, attacker, skill)
					- attacker.getStat().calc(Stats.MAGIC_POWER, target, skill);
			final double failChance = 4. * Math.max(1., levelDiff) * (1. + magic_rcpt / 100.);
			if(Rnd.chance(failChance))
			{
				if(levelDiff > 9)
				{
					info.damage = 0;
					final SystemMessagePacket msg = new SystemMessagePacket(SystemMsg.C1_RESISTED_C2S_MAGIC).addName(target).addName(attacker);
					attacker.sendPacket(msg);
					target.sendPacket(msg);
					attacker.sendPacket(new ExMagicAttackInfo(attacker.getObjectId(), target.getObjectId(), ExMagicAttackInfo.RESISTED));
					target.sendPacket(new ExMagicAttackInfo(attacker.getObjectId(), target.getObjectId(), ExMagicAttackInfo.RESISTED));
				}
				else
				{
					info.damage /= 2;
					final SystemMessagePacket msg = new SystemMessagePacket(SystemMsg.DAMAGE_IS_DECREASED_BECAUSE_C1_RESISTED_C2S_MAGIC).addName(target).addName(attacker);
					attacker.sendPacket(msg);
					target.sendPacket(msg);
				}
			}
			else
			{
				final AttackInfo elementalInfo = calcElementalDamage(attacker, target, skill, power, 1., useShot);
				if(elementalInfo != null)
				{
					info.elementalDamage = elementalInfo.elementalDamage;
					info.elementalCrit = elementalInfo.elementalCrit;
					info.damage += elementalInfo.elementalDamage;
				}
			}

			if(calcCastBreak(target, info.crit))
			{
				target.abortCast(false, true);
			}

			if(calcStunBreak(info.crit, true, true) && info.damage > 0)
			{
				target.getAbnormalList().stop(AbnormalType.STUN);
				// target.getAbnormalList().stop(AbnormalType.TURN_FLEE); На классике вроде как
				// не должно сбивать.
			}

			for(final Abnormal abnormal : target.getAbnormalList())
			{
				final double chance = info.crit ? abnormal.getSkill().getOnCritCancelChance() : abnormal.getSkill().getOnAttackCancelChance();
				if(chance > 0 && Rnd.chance(chance))
				{
					abnormal.exit();
				}
			}
		}
		finally
		{
			_lock.unlock();
		}
		return info;
	}

	/*
	 * @Rivelia. Default chances: On magical skill non-crit: 33.33% On magical skill
	 * crit: 66.67% On physical skill non-crit: 33.33% On physical skill crit:
	 * 66.67% On regular hit non-crit: 16.67% On regular hit crit: 33.33%
	 */
	public static boolean calcStunBreak(boolean crit, boolean isSkill, boolean isMagic)
	{
		if(!Config.ENABLE_STUN_BREAK_ON_ATTACK)
			return false;

		if(isSkill)
		{
			if(isMagic)
			{ return Rnd.chance(crit ? Config.CRIT_STUN_BREAK_CHANCE_ON_MAGICAL_SKILL : Config.NORMAL_STUN_BREAK_CHANCE_ON_MAGICAL_SKILL); }
			return Rnd.chance(crit ? Config.CRIT_STUN_BREAK_CHANCE_ON_PHYSICAL_SKILL : Config.NORMAL_STUN_BREAK_CHANCE_ON_PHYSICAL_SKILL);
		}
		return Rnd.chance(crit ? Config.CRIT_STUN_BREAK_CHANCE_ON_REGULAR_HIT : Config.NORMAL_STUN_BREAK_CHANCE_ON_REGULAR_HIT);
	}

	/** Returns true in case of fatal blow success */
	public static boolean calcBlow(Creature activeChar, Creature target, Skill skill)
	{
		// TODO: Правильная ли формула резиста блов умений?
		final double vulnMod = target.getStat().calc(Stats.BLOW_RESIST, activeChar, skill);
		final double profMod = activeChar.getStat().calc(Stats.BLOW_POWER, target, skill);
		if(vulnMod == Double.POSITIVE_INFINITY || profMod == Double.NEGATIVE_INFINITY)
			return false;

		if(vulnMod == Double.NEGATIVE_INFINITY || profMod == Double.POSITIVE_INFINITY)
			return true;

		final WeaponTemplate weapon = activeChar.getActiveWeaponTemplate();

		final double base_weapon_crit = weapon == null ? 4. : weapon.getCritical();
		double crit_height_bonus = 1;
		if(Config.ENABLE_CRIT_HEIGHT_BONUS)
		{
			crit_height_bonus = 0.008 * Math.min(25, Math.max(-25, target.getZ() - activeChar.getZ())) + 1.1;
		}
		final double buffs_mult = activeChar.getStat().calc(Stats.FATALBLOW_RATE, target, skill);
		// @Rivelia. Default values: BLOW_SKILL_CHANCE_MOD_ON_BEHIND = 5,
		// BLOW_SKILL_CHANCE_MOD_ON_FRONT = 4
		final double skill_mod = skill.isBehind() ? Config.BLOW_SKILL_CHANCE_MOD_ON_BEHIND : Config.BLOW_SKILL_CHANCE_MOD_ON_FRONT;
		double chance = base_weapon_crit * buffs_mult * crit_height_bonus * skill_mod;
		final double modDiff = profMod - vulnMod;
		if(modDiff != 1)
		{
			chance *= 1. + ((80 + modDiff) / 200.);
		}

		if(!target.isInCombat())
		{
			chance *= 1.1;
		}

		if(activeChar.isDistortedSpace())
		{
			chance *= 1.3;
		}
		else
		{
			switch(PositionUtils.getDirectionTo(target, activeChar))
			{
				case BEHIND:
					chance *= 1.3;
					break;
				case SIDE:
					chance *= 1.1;
					break;
				case FRONT:
					if(skill.isBehind())
					{
						chance = 3.0;
					}
					break;
			}
		}
		// @Rivelia. Default values: MAX_BLOW_RATE_ON_BEHIND = 100,
		// MAX_BLOW_RATE_ON_FRONT_AND_SIDE = 80.
		chance = Math.min(skill.isBehind() ? Config.MAX_BLOW_RATE_ON_BEHIND : Config.MAX_BLOW_RATE_ON_FRONT_AND_SIDE, chance);
		return Rnd.chance(chance);
	}

	/** Возвращает шанс крита в процентах */
	public static boolean calcPCrit(Creature attacker, Creature target, Skill skill, boolean blow)
	{
		if(skill != null)
		{
			final boolean dexDep = attacker.getStat().calc(Stats.P_SKILL_CRIT_RATE_DEX_DEPENDENCE) > 0;
			final double skillCritRate = skill.getCriticalRate();
			final double skillCritChanceMod = attacker.getStat().calc(Stats.P_SKILL_CRITICAL_RATE, 1., target, skill);
			final double pCritChanceMode = getPCritChanceMode(attacker);
			final double pCritChanceReceptive = target.getStat().calc(Stats.P_SKILL_CRIT_CHANCE_RECEPTIVE, attacker, skill) * 0.01;
			final double critRate = skillCritRate * skillCritChanceMod * pCritChanceMode * pCritChanceReceptive * 10;
			final double blowCritModifier = Config.ALT_BLOW_CRIT_RATE_MODIFIER;
			double finalRate = critRate;
			double statModifier;
			if(dexDep)
			{
				statModifier = BaseStats.DEX.calcBonus(attacker);
				if(blow)
				{
					statModifier *= Config.BLOW_SKILL_DEX_CHANCE_MOD;
				}
				else
				{
					statModifier *= Config.NORMAL_SKILL_DEX_CHANCE_MOD;
				}
			}
			else
			{
				statModifier = BaseStats.STR.calcBonus(attacker);
			}

			finalRate *= statModifier;

			if(blow)
			{
				finalRate *= blowCritModifier;
			}

			final boolean result = finalRate > Rnd.get(1000);
			return result;
		}
		else
		{
			final double pCriticalHit = attacker.getPCriticalHit(target);
			final double pCritChanceReceptive = target.getStat().calc(Stats.P_CRIT_CHANCE_RECEPTIVE, attacker, skill) * 0.01;
			final double pCritChanceMode = getPCritChanceMode(attacker);
			final double critRate = pCriticalHit * pCritChanceReceptive * pCritChanceMode;
			double directionRate = 1.;
			if(attacker.isDistortedSpace())
			{
				directionRate *= 1.4;
			}
			else
			{
				switch(PositionUtils.getDirectionTo(target, attacker))
				{
					case BEHIND:
						directionRate *= 1.4;
						break;
					case SIDE:
						directionRate *= 1.2;
						break;
				}
			}

			double finalRate = (critRate) * directionRate / 10;

			// Autoattack critical rate is limited between 3%-97%.
			finalRate = Math.min(Math.max(finalRate, 3), 97);

			final boolean result = Rnd.chance(finalRate);
			return result;
		}
	}

	public static double calcCriticalHeightBonus(Creature from, Creature target)
	{
		return ((((Math.min(Math.max(from.getZ() - target.getZ(), -25), 25) * 4) / 5) + 10) / 100) + 1;
	}

	// Magic Critical Rate
	public static boolean calcMCrit(Creature attacker, Creature target, Skill skill)
	{
		final double mCriticalHit = attacker.getMCriticalHit(target, skill);
		final double skillCriticalRateMod = skill.getCriticalRateMod();
		final double mCritChanceMode = getMCritChanceMode(attacker);
		final double critRate;
		if(target == null || !skill.isDebuff())
		{
			critRate = mCriticalHit * skillCriticalRateMod * mCritChanceMode;
			return Math.min(critRate, 320) > Rnd.get(1000);
		}

		final double mCritChanceReceptive = target.getStat().calc(Stats.M_CRIT_CHANCE_RECEPTIVE, attacker, skill) * 0.01;
		critRate = mCriticalHit * skillCriticalRateMod * mCritChanceMode * mCritChanceReceptive;

		double finalRate = critRate;

		double levelDiffChanceAdd = 0;
		if(attacker.getLevel() >= 78 && target.getLevel() >= 78)
		{
			levelDiffChanceAdd = Math.sqrt(attacker.getLevel()) + ((attacker.getLevel() - target.getLevel()) / 25);
			finalRate += levelDiffChanceAdd;
			finalRate = Math.min(finalRate, 320);
		}
		else
		{
			finalRate = Math.min(finalRate, 200);
		}

		final boolean result = finalRate > Rnd.get(1000);
		return result;
	}

	public static boolean calcCastBreak(Creature target, boolean crit)
	{
		if(target == null || target.isInvulnerable() || target.isRaid() || !target.isCastingNow())
			return false;

		Skill skill = null;
		SkillEntry skillEntry = target.getSkillCast(SkillCastingType.NORMAL).getSkillEntry();

		if(skillEntry != null)
		{
			skill = skillEntry.getTemplate();
			if(skill.isPhysic() || skill.getSkillType() == SkillType.TAKECASTLE)
			{ return false; }
		}

		skillEntry = target.getSkillCast(SkillCastingType.NORMAL_SECOND).getSkillEntry();
		if(skillEntry != null)
		{
			skill = skillEntry.getTemplate();
			if(skill.isPhysic() || skill.getSkillType() == SkillType.TAKECASTLE)
			{ return false; }
		}

		return Rnd.chance(target.getStat().calc(Stats.CAST_INTERRUPT, crit ? 75 : 10, null, skill));
	}

	/** Calculate delay (in milliseconds) before next ATTACK */
	public static int calcPAtkSpd(double rate)
	{
		if(rate < 2)
		{ return 2700; }
		return (int) (470000 / rate);
	}

	/** Calculate delay (in milliseconds) for skills cast */
	public static int calcSkillCastSpd(Creature attacker, Skill skill, double skillTime)
	{
		if(skill.isMagic())
		{ return (int) (skillTime * 333 / Math.max(attacker.getMAtkSpd(), 1)); }
		if(skill.isPhysic())
		{ return (int) (skillTime * 333 / Math.max(attacker.getPAtkSpd(), 1)); }
		return (int) skillTime;
	}

	/** Calculate reuse delay (in milliseconds) for skills */
	public static long calcSkillReuseDelay(Creature actor, Skill skill)
	{
		if(actor.isPlayer())
		{
			final Player plr = actor.getPlayer();

			final int effectId = skill.getRemoveReuseOnActiveEffect();
			if(effectId != 0)
			{
				for(final Abnormal e : plr.getAbnormalList())
				{
					if(e.getSkill().getId() == effectId)
						return 0;
				}
			}
		}

		long reuseDelay = skill.getReuseDelay();
		if(actor.isMonster())
		{
			reuseDelay = skill.getReuseForMonsters();
		}
		if(skill.isHandler() || skill.isItemSkill() || skill.isReuseDelayPermanent())
			return reuseDelay;
		if(skill.isMusic())
			return (long) actor.getStat().calc(Stats.MUSIC_REUSE_RATE, reuseDelay, null, skill);
		if(skill.isMagic())
			return (long) actor.getStat().calc(Stats.MAGIC_REUSE_RATE, reuseDelay, null, skill);
		return (long) actor.getStat().calc(Stats.PHYSIC_REUSE_RATE, reuseDelay, null, skill);
	}

	private static double getConditionBonus(Creature attacker, Creature target)
	{
		double mod = 100;
		// Get high or low bonus
		if((attacker.getZ() - target.getZ()) > 50)
		{
			mod += HitCondBonusHolder.getInstance().getHitCondBonus(HitCondBonusType.HIGH);
		}
		else if((attacker.getZ() - target.getZ()) < -50)
		{
			mod += HitCondBonusHolder.getInstance().getHitCondBonus(HitCondBonusType.LOW);
		}

		// Get weather bonus
		if(GameTimeController.getInstance().isNowNight())
		{
			mod += HitCondBonusHolder.getInstance().getHitCondBonus(HitCondBonusType.DARK);
		}

		if(attacker.isDistortedSpace())
		{
			mod += HitCondBonusHolder.getInstance().getHitCondBonus(HitCondBonusType.BACK);
		}
		else
		{
			final PositionUtils.TargetDirection direction = PositionUtils.getDirectionTo(attacker, target);
			switch(direction)
			{
				case BEHIND:
					mod += HitCondBonusHolder.getInstance().getHitCondBonus(HitCondBonusType.BACK);
					break;
				case SIDE:
					mod += HitCondBonusHolder.getInstance().getHitCondBonus(HitCondBonusType.SIDE);
					break;
				default:
					mod += HitCondBonusHolder.getInstance().getHitCondBonus(HitCondBonusType.AHEAD);
					break;
			}
		}

		// If (mod / 100) is less than 0, return 0, because we can't lower more than
		// 100%.
		return Math.max(mod / 100, 0);
	}

	/** Returns true if hit missed (target evaded) */
	public static boolean calcHitMiss(Creature attacker, Creature target)
	{
		int chance = (80 + (2 * (attacker.getPAccuracy() - target.getPEvasionRate(attacker)))) * 10;

		chance *= getConditionBonus(attacker, target);
		chance = (int) Math.max(chance, Config.PHYSICAL_MIN_CHANCE_TO_HIT * 10);
		chance = (int) Math.min(chance, Config.PHYSICAL_MAX_CHANCE_TO_HIT * 10);

		return chance < Rnd.get(1000);
	}

	private static boolean calcMagicHitMiss(Skill skill, Creature attacker, Creature target)
	{
		int chance = (81 + (2 * (attacker.getMAccuracy() + 3 - target.getMEvasionRate(attacker)))) * 10;

		chance = (int) Math.max(chance, Config.MAGIC_MIN_CHANCE_TO_HIT * 10);
		chance = (int) Math.min(chance, Config.MAGIC_MAX_CHANCE_TO_HIT * 10);

		return chance < Rnd.get(1000);
	}

	/** Returns true if shield defence successfull */
	public static boolean calcShldUse(Creature attacker, Creature target)
	{
		final WeaponTemplate template = target.getSecondaryWeaponTemplate();
		if(template == null || template.getItemType() != WeaponTemplate.WeaponType.NONE)
			return false;
		final int angle = (int) target.getStat().calc(Stats.SHIELD_ANGLE, attacker, null);
		if(angle < 360 && !PositionUtils.isFacing(target, attacker, angle))
			return false;
		return Rnd.chance((int) target.getStat().calc(Stats.SHIELD_RATE, attacker, null));
	}

	public static boolean calcEffectsSuccess(Creature caster, Creature target, Skill skill, int activateRate)
	{
		if(activateRate == -1)
			return true;

		int magicLevel = skill.getMagicLevel();
		if(magicLevel <= -1)
		{
			magicLevel = target.getLevel() + 3;
		}

		final double targetBasicProperty = getAbnormalResist(skill.getBasicProperty(), target);
		final double baseMod = ((((((magicLevel - target.getLevel()) + 3) * skill.getLevelBonusRate()) + activateRate)) - targetBasicProperty);
		final double elementMod = calcAttributeBonus(caster, target, skill);
		final double traitMod = calcGeneralTraitBonus(caster, target, skill.getTraitType(), false);
		final double basicPropertyResist = getBasicPropertyResistBonus(skill.getBasicProperty(), target);
		final double buffDebuffMod = 1.
				+ ((skill.isDebuff() ? target.getStat().calc(Stats.RESIST_ABNORMAL_DEBUFF, 0) : target.getStat().calc(Stats.RESIST_ABNORMAL_BUFF, 0)) / 100.);
		final double rate = baseMod * elementMod * traitMod * buffDebuffMod;
		final double finalRate = traitMod > 0 ? Math.min(skill.getMaxChance(), Math.max(rate, skill.getMinChance())) * basicPropertyResist : 0;

		final boolean result = (finalRate > Rnd.get(100));
		return result;
	}

	public static double calcDamageResists(Skill skill, Creature attacker, Creature defender, double value)
	{
		if(attacker == defender) // это дамаг от местности вроде ожога в лаве, наносится от своего имени
			return value; // TODO: по хорошему надо учитывать защиту, но поскольку эти скиллы немагические
		// то надо делать отдельный механизм
		// DEPRECATED
		if(attacker.isBoss())
		{
			value *= Config.RATE_EPIC_ATTACK;
		}
		else if(attacker.isRaid())
		{
			value *= Config.RATE_RAID_ATTACK;
		}

		if(defender.isBoss())
		{
			value /= Config.RATE_EPIC_DEFENSE;
		}
		else if(defender.isRaid())
		{
			value /= Config.RATE_RAID_DEFENSE;
		}

		final Player pAttacker = attacker.getPlayer();

		// если уровень игрока ниже чем на 2 и более уровней моба 78+, то его урон по
		// мобу снижается
		final int diff = defender.getLevel() - (pAttacker != null ? pAttacker.getLevel() : attacker.getLevel());
		if(attacker.isPlayable() && defender.isMonster() && defender.getLevel() >= 78 && diff > 2)
		{
			value *= .7 / Math.pow(diff - 2, .25);
		}

		Element element = Element.NONE;
		double power = 0.;

		// использует элемент умения
		if(skill != null)
		{
			element = skill.getTemplate().getElement();
			power = skill.getTemplate().getElementPower();
		}
		// используем максимально эффективный элемент
		else
		{
			element = getAttackElement(attacker, defender);
		}

		if(element == Element.NONE)
		{ return value; }

		return value * getElementMod(defender.getStat().calc(element.getDefence(), 0.), attacker.getStat().calc(element.getAttack(), power));
	}

	/**
	 * Возвращает множитель для атаки из значений атакующего и защитного элемента.
	 * <br />
	 * <br />
	 * Диапазон от 1.0 до 1.7 (Freya) <br />
	 * <br />
	 * 
	 * @param defense значение защиты
	 * @param attack  значение атаки
	 * @return множитель
	 */
	private static double getElementMod(double defense, double attack)
	{
		double diff = attack - defense;
		if(diff > 0)
		{
			diff = 1.025 + Math.sqrt(Math.pow(Math.abs(diff), 3) / 2.) * 0.0001;
		}
		else if(diff < 0)
		{
			diff = 0.975 - Math.sqrt(Math.pow(Math.abs(diff), 3) / 2.) * 0.0001;
		}
		else
		{
			diff = 1;
		}

		diff = Math.max(diff, 0.75);
		diff = Math.min(diff, 1.25);
		return diff;
	}

	/**
	 * Возвращает максимально эффективный атрибут, при атаке цели
	 * 
	 * @param attacker
	 * @param target
	 * @return
	 */
	public static Element getAttackElement(Creature attacker, Creature target)
	{
		double val, max = Double.MIN_VALUE;
		Element result = Element.NONE;
		for(final Element e : Element.VALUES)
		{
			val = attacker.getStat().calc(e.getAttack(), 0.);
			if(val <= 0.)
			{
				continue;
			}

			if(target != null)
			{
				val -= target.getStat().calc(e.getDefence(), 0.);
			}

			if(val > max)
			{
				result = e;
				max = val;
			}
		}

		return result;
	}

	public static int calculateKarmaLost(Player player, long exp)
	{
		if(Config.RATE_KARMA_LOST_STATIC != -1)
			return Config.RATE_KARMA_LOST_STATIC;

		final double karmaLooseMul = KarmaIncreaseDataHolder.getInstance().getData(player.getLevel());
		if(exp > 0)
		{ // Received exp
			exp /= Config.KARMA_RATE_KARMA_LOST == -1 ? Config.RATE_XP_BY_LVL[player.getLevel()] : Config.KARMA_RATE_KARMA_LOST;
		}
		return (int) ((Math.abs(exp) / karmaLooseMul) / 15);
	}

	public static boolean calcCancelSuccess(Creature attacker, Creature target, int dispelChance, Skill skill, Abnormal abnormal)
	{
		final int cancelLevel = skill.getMagicLevel() > 0 ? skill.getMagicLevel() : attacker.getLevel();
		final int buffLevel = abnormal.getSkill().getMagicLevel() > 0 ? abnormal.getSkill().getMagicLevel() : target.getLevel();
		final int abnormalTime = abnormal.getSkill().getAbnormalTime();
		final double cancelPower = attacker.getStat().calc(Stats.CANCEL_POWER, 100, null, null);
		final double cancelResist = target.getStat().calc(Stats.CANCEL_RESIST, 100, null, null);

		int chance = (int) (dispelChance + ((cancelLevel - buffLevel) * 2) + ((abnormalTime / 120) * 0.01 * cancelPower * 0.01 * cancelResist));
		chance = Math.max(Math.min(Config.CANCEL_SKILLS_HIGH_CHANCE_CAP, chance), Config.CANCEL_SKILLS_LOW_CHANCE_CAP);

		final boolean result = Rnd.get(100) < chance;
		return result;
	}

	public static double getAbnormalResist(BasicProperty basicProperty, Creature target)
	{
		switch(basicProperty)
		{
			case PHYSICAL_ABNORMAL_RESIST:
				return target.getPhysicalAbnormalResist();
			case MAGIC_ABNORMAL_RESIST:
				return target.getMagicAbnormalResist();
		}
		return 0;
	}

	/**
	 * Calculates the attribute bonus with the following formula: <BR>
	 * diff > 0, so AttBonus = 1,025 + sqrt[(diff^3) / 2] * 0,0001, cannot be above
	 * 1,25! <BR>
	 * diff < 0, so AttBonus = 0,975 - sqrt[(diff^3) / 2] * 0,0001, cannot be below
	 * 0,75! <BR>
	 * diff == 0, so AttBonus = 1
	 * 
	 * @param attacker
	 * @param target
	 * @param skill    Can be {@code null} if there is no skill used for the attack.
	 * @return The attribute bonus
	 */
	public static double calcAttributeBonus(Creature attacker, Creature target, Skill skill)
	{
		int attack_attribute;
		int defence_attribute;

		if(skill != null)
		{
			if((skill.getElement() == Element.NONE) || (skill.getElement() == Element.NONE_ARMOR))
			{
				attack_attribute = 0;
				defence_attribute = target.getDefence(Element.NONE_ARMOR);
			}
			else
			{
				if(attacker.getAttackElement() == skill.getElement())
				{
					attack_attribute = attacker.getAttack(attacker.getAttackElement()) + skill.getElementPower();
					defence_attribute = target.getDefence(attacker.getAttackElement());
				}
				else
				{
					attack_attribute = skill.getElementPower();
					defence_attribute = target.getDefence(skill.getElement());
				}
			}
		}
		else
		{
			attack_attribute = attacker.getAttack(attacker.getAttackElement());
			defence_attribute = target.getDefence(attacker.getAttackElement());
		}

		final int diff = attack_attribute - defence_attribute;
		if(diff > 0)
			return Math.min(1.025 + (Math.sqrt(Math.pow(diff, 3) / 2) * 0.0001), 1.25);

		if(diff < 0)
			return Math.max(0.975 - (Math.sqrt(Math.pow(-diff, 3) / 2) * 0.0001), 0.75);

		return 1;
	}

	public static double calcGeneralTraitBonus(Creature attacker, Creature target, SkillTrait trait, boolean ignoreResistance)
	{
		if(trait == SkillTrait.NONE)
			return 1.;

		final Stats defenceStat = trait.getDefence();
		final double targetDefence = defenceStat == null ? 0. : target.getStat().calc(defenceStat) * trait.getDefenceMod();
		final double targetDefenceModifier = (targetDefence + 100.) / 100.;

		final Stats attackStat = trait.getAttack();
		final double attackerAttackModifier = ((attackStat == null ? 0. : attacker.getStat().calc(attackStat) * trait.getAttackMod()) + 100.) / 100.;

		switch(trait.getType())
		{
			case WEAKNESS:
			{
				if(attackerAttackModifier == 1. || targetDefenceModifier == 1.)
					return 1.;
				break;
			}
			case RESISTANCE:
			{
				if(ignoreResistance)
					return 1.;
				break;
			}
			default:
				return 1.;
		}

		if(targetDefence == Double.POSITIVE_INFINITY)
			return 0.;

		final double result = (attackerAttackModifier - targetDefenceModifier) + 1.;
		return Math.max(0.05, Math.min(2., result));
	}

	public static double calcWeaponTraitBonus(Creature attacker, Creature target)
	{
		final SkillTrait type = attacker.getBaseStats().getAttackType().getTrait();
		final Stats defenceStat = type.getDefence();
		if(defenceStat != null)
		{
			final double targetDefenceModifier = (target.getStat().calc(defenceStat) * type.getDefenceMod() + 100.) / 100.;
			final double result = targetDefenceModifier - 1.;
			return 1. - result;
		}
		return 1.;
	}

	public static double calcAttackTraitBonus(Creature attacker, Creature target)
	{
		final double weaponTraitBonus = calcWeaponTraitBonus(attacker, target);
		if(weaponTraitBonus == 0)
			return 0;

		double weaknessBonus = 1.;
		for(final SkillTrait traitType : SkillTrait.VALUES)
		{
			if(traitType.getType() == SkillTraitType.WEAKNESS)
			{
				weaknessBonus *= calcGeneralTraitBonus(attacker, target, traitType, true);
				if(weaknessBonus == 0)
					return 0;
			}
		}
		return Math.max(0.05, Math.min(2., (weaponTraitBonus * weaknessBonus)));
	}

	public static double getBasicPropertyResistBonus(BasicProperty basicProperty, Creature target)
	{
		_lock.lock();
		try
		{
			if((basicProperty == BasicProperty.NONE) || !target.hasBasicPropertyResist())
				return 1.0;

			final BasicPropertyResist resist = target.getBasicPropertyResist(basicProperty);
			switch(resist.getResistLevel())
			{
				case 0:
					return 1.0;
				case 1:
					return 0.6;
				case 2:
					return 0.3;
			}
		}
		finally
		{
			_lock.unlock();
		}
		return 0;
	}

	/**
	 * @param totalAttackTime the time needed to make a full attack.
	 * @param attackType      the weapon type used for attack.
	 * @param secondHit       calculates the second hit for dual attacks.
	 * @return the time required from the start of the attack until you hit the
	 *         target.
	 */
	public static int calculateTimeToHit(int totalAttackTime, WeaponTemplate.WeaponType attackType, boolean secondHit)
	{
		_lock.lock();
		try
		{
			switch(attackType)
			{
				// Bows
				case BOW:
				case CROSSBOW:
				case FIREARMS:
				{
					return (int) (totalAttackTime * 0.95);
				}
				// Dual weapons
				case DUALDAGGER:
				case DUAL:
				case DUALFIST:
				{
					if(secondHit)
						return (int) (totalAttackTime * 0.6);
					return (int) (totalAttackTime * 0.2426);
				}
				// One-hand weapons
				case SWORD:
				case BLUNT:
				case DAGGER:
				case RAPIER:
				case ETC:
					return (int) (totalAttackTime * 0.644); // 0.614?
				default:
				{
					return (int) (totalAttackTime * 0.735); // 0.705
				}
			}
		}
		finally
		{
			_lock.unlock();
		}
	}
}