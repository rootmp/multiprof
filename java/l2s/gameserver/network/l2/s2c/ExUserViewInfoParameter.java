package l2s.gameserver.network.l2.s2c;

import l2s.gameserver.model.Player;
import l2s.gameserver.stats.Stats;

/**
 * @author nexvill
 */
public class ExUserViewInfoParameter extends L2GameServerPacket
{
	Player _player;

	public ExUserViewInfoParameter(Player player)
	{
		_player = player;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(185); // count

		writeH(0); // pAtk (%)
		writeD((int) (_player.getStat().getMul(Stats.POWER_ATTACK) * 100) - 100); // param value

		writeH(1); // pAtk (num)
		writeD((int) (_player.getStat().getAdd(Stats.POWER_ATTACK)));

		writeH(2); // mAtk(%)
		writeD((int) (_player.getStat().getMul(Stats.MAGIC_ATTACK) * 100) - 100);

		writeH(3); // mAtk(num)
		writeD((int) (_player.getStat().getAdd(Stats.MAGIC_ATTACK)));

		writeH(4); // Soulshot Damage - Activation
		writeD((int) (_player.getStat().getMul(Stats.SOULSHOT_POWER) * 100) - 100);

		writeH(5); // Spiritshot Damage - Activation
		writeD((int) (_player.getStat().getMul(Stats.SPIRITSHOT_POWER) * 100) - 100);

		writeH(6); // Soulshot Damage - Enchanted Weapons
		if (_player.getActiveWeaponInstance() != null)
		{
			writeD((int) (_player.getActiveWeaponInstance().getEnchantLevel() * 0.7 * 100));
		}
		else
		{
			writeD(0);
		}

		writeH(7); // Spiritshot Damage - Enchanted Weapons
		if (_player.getActiveWeaponInstance() != null)
		{
			writeD((int) (_player.getActiveWeaponInstance().getEnchantLevel() * 0.7 * 100));
		}
		else
		{
			writeD(0);
		}

		writeH(8); // Soulshot Damage - Misc
		if (_player.getActiveWeaponInstance() != null)
		{
			writeD((int) ((_player.getStat().getMul(Stats.SOULSHOT_POWER) * 100) + (_player.getActiveWeaponInstance().getEnchantLevel() * 0.7 * 100) - 100));
		}
		else
		{
			writeD(0);
		}

		writeH(9); // Spiritshot Damage - Misc
		if (_player.getActiveWeaponInstance() != null)
		{
			writeD((int) ((_player.getStat().getMul(Stats.SPIRITSHOT_POWER) * 100) + (_player.getActiveWeaponInstance().getEnchantLevel() * 0.7 * 100) - 100));
		}
		else
		{
			writeD(0);
		}

		writeH(10); // basic pvp damage
		writeD((int) (_player.getStat().getMul(Stats.PVP_PHYS_DMG_BONUS) * 100) - 100);

		writeH(11); // p. skill damage in pvp
		writeD((int) (_player.getStat().getMul(Stats.PVP_PHYS_SKILL_DMG_BONUS) * 100) - 100);

		writeH(12); // m. skill damage in pvp
		writeD((int) (_player.getStat().getMul(Stats.PVP_MAGIC_SKILL_DMG_BONUS) * 100) - 100);

		writeH(13); // TODO: inflicted pvp damage
		writeD(0);

		writeH(14); // TODO: damage decrease ignore
		writeD(0);

		writeH(15); // basic pve damage
		writeD((int) (_player.getStat().getMul(Stats.PVE_PHYS_DMG_BONUS) * 100) - 100);

		writeH(16); // p. skill damage in pve
		writeD((int) (_player.getStat().getMul(Stats.PVE_PHYS_SKILL_DMG_BONUS) * 100) - 100);

		writeH(17); // m. skill damage in pve
		writeD((int) (_player.getStat().getMul(Stats.PVE_MAGIC_SKILL_DMG_BONUS) * 100) - 100);

		writeH(18); // TODO: inflicted pve damage
		writeD(0);

		writeH(19); // TODO: PvE damage decrease ignore
		writeD(0);

		writeH(20); // TODO: basic power
		writeD(0);

		writeH(21); // p. skill power
		writeD((int) (_player.getStat().getMul(Stats.P_SKILL_POWER) * 100) - 100);

		writeH(22); // m. skill power
		writeD((int) (_player.getStat().getMul(Stats.M_SKILL_POWER) * 100) - 100);

		writeH(23); // TODO: AoE skill damage
		writeD(0);

		writeH(24); // damage bonus - sword
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_SWORD) * 100) - 100);

		writeH(25); // damage bonus - ancient sword
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_ANCIENTSWORD) * 100) - 100);

		writeH(26); // damage bonus - dagger
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_DAGGER) * 100) - 100);

		writeH(27); // damage bonus - rapier
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_RAPIER) * 100) - 100);

		writeH(28); // damage bonus - blunt
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_BLUNT) * 100) - 100);

		writeH(29); // damage bonus - spear
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_POLE) * 100) - 100);

		writeH(30); // damage bonus - fists
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_DUALFIST) * 100) - 100);

		writeH(31); // damage bonus dual swords
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_DUAL) * 100) - 100);

		writeH(32); // damage bonus - bow
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_BOW) * 100) - 100);

		writeH(33); // damage bonus - firearms
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_FIREARMS) * 100) - 100);

		writeH(34); // pDef(%)
		writeD((int) (_player.getStat().getMul(Stats.POWER_DEFENCE) * 100) - 100);

		writeH(35); // pDef(num)
		writeD((int) (_player.getStat().getAdd(Stats.POWER_DEFENCE)));

		writeH(36); // mDef %
		writeD((int) (_player.getStat().getMul(Stats.MAGIC_DEFENCE) * 100) - 100);

		writeH(37); // mDef(num)
		writeD((int) (_player.getStat().getAdd(Stats.MAGIC_DEFENCE)));

		writeH(38); // TODO: Soulshot Damage Decrease
		writeD(0);

		writeH(39); // TODO: Spiritshot Damage Decrease
		writeD(0);

		writeH(40); // Received basic pvp damage
		writeD((int) (_player.getStat().getMul(Stats.PVP_PHYS_DEFENCE_BONUS) * 100) - 100);

		writeH(41); // Received P. Skill damage in pvp
		writeD((int) (_player.getStat().getMul(Stats.PVP_PHYS_SKILL_DEFENCE_BONUS) * 100) - 100);

		writeH(42); // Received M. Skill damage in pvp
		writeD((int) (_player.getStat().getMul(Stats.PVP_MAGIC_SKILL_DEFENCE_BONUS) * 100) - 100);

		writeH(43); // TODO: Received PvP damage
		writeD(0);

		writeH(44); // TODO: PvP Damage decrease
		writeD(0);

		writeH(45); // received basic pve damage
		writeD((int) (_player.getStat().getMul(Stats.PVE_PHYS_DEFENCE_BONUS) * 100) - 100);

		writeH(46); // received p. skill damage in pve
		writeD((int) (_player.getStat().getMul(Stats.PVE_PHYS_SKILL_DEFENCE_BONUS) * 100) - 100);

		writeH(47); // received m. skill damage in pve
		writeD((int) (_player.getStat().getMul(Stats.PVE_MAGIC_SKILL_DEFENCE_BONUS) * 100) - 100);

		writeH(48); // TODO: received pve damage
		writeD(0);

		writeH(49); // TODO: pve damage decrease
		writeD(0);

		writeH(50); // TODO: received basic damage power
		writeD(0);

		writeH(51); // TODO: received p. skill power damage
		writeD(0);

		writeH(52); // TODO: received m. skill power damage
		writeD(0);

		writeH(53); // TODO: received AoE skill damage
		writeD(0);

		writeH(54); // damage resistance bonus - sword
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_SWORD) * 100) - 100);

		writeH(55); // damage resistance bonus - ancient sword
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_ANCIENTSWORD) * 100) - 100);

		writeH(56); // damage resistance bonus - dagger
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_DAGGER) * 100) - 100);

		writeH(57); // damage resistance bonus - rapier
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_RAPIER) * 100) - 100);

		writeH(58); // damage resistance bonus - blunt
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_BLUNT) * 100) - 100);

		writeH(59); // damage resistance bonus - spear
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_POLE) * 100) - 100);

		writeH(60); // damage resistance bonus - first
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_DUALFIST) * 100) - 100);

		writeH(61); // damage resistance bonus - dual sword
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_DUAL) * 100) - 100);

		writeH(62); // damage resistance bonus - bow
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_BOW) * 100) - 100);

		writeH(63); // damage resistance bonus - firearms
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_FIREARMS) * 100) - 100);

		writeH(64); // shield defense %
		writeD((int) (_player.getStat().getMul(Stats.SHIELD_DEFENCE) * 100) - 100);

		writeH(65); // shield defense (num)
		writeD((int) (_player.getStat().getAdd(Stats.SHIELD_DEFENCE)));

		writeH(66); // shield defense rate
		writeD((int) (_player.getStat().getMul(Stats.SHIELD_RATE) * 100) - 100);

		writeH(67); // magic damage resistance %
		writeD((int) (_player.getStat().getMul(Stats.MAGIC_RESIST) * 100) - 100);

		writeH(68); // magic damage resistance (num)
		writeD((int) (_player.getStat().getAdd(Stats.MAGIC_RESIST)));

		writeH(69); // magic damage reflection %
		writeD((int) (_player.getStat().getMul(Stats.REFLECT_MAGIC_SKILL) * 100) - 100);

		writeH(70); // TODO: magic damage reflection resistance
		writeD((int) (_player.getStat().getMul(Stats.RESIST_REFLECT_DAM) * 100) - 100);

		writeH(71); // TODO: received fixed damage %
		writeD(0);

		writeH(72); // casting interruption rate %
		writeD((int) (_player.getStat().getMul(Stats.CAST_INTERRUPT) * 100) - 100);

		writeH(73); // casting interruption rate (num)
		writeD((int) (_player.getStat().getAdd(Stats.CAST_INTERRUPT)));

		writeH(74); // p. accuracy %
		writeD((int) (_player.getStat().getMul(Stats.P_ACCURACY_COMBAT) * 100) - 100);

		writeH(75); // p accuracy (num)
		writeD((int) (_player.getStat().getAdd(Stats.P_ACCURACY_COMBAT)));

		writeH(76); // m accuracy %
		writeD((int) (_player.getStat().getMul(Stats.M_ACCURACY_COMBAT) * 100) - 100);

		writeH(77); // m accuracy (num)
		writeD((int) (_player.getStat().getAdd(Stats.M_ACCURACY_COMBAT)));

		writeH(78); // vital point attack rate %
		writeD((int) (_player.getStat().getMul(Stats.FATALBLOW_RATE) * 100) - 100);

		writeH(79); // vital point attack rate (num)
		writeD((int) (_player.getStat().getAdd(Stats.FATALBLOW_RATE)));

		writeH(80); // pEvasion %
		writeD((int) (_player.getStat().getMul(Stats.P_EVASION_RATE) * 100) - 100);

		writeH(81); // pEvasion (num)
		writeD((int) (_player.getStat().getAdd(Stats.P_EVASION_RATE)));

		writeH(82); // mEvasion %
		writeD((int) (_player.getStat().getMul(Stats.M_EVASION_RATE) * 100) - 100);

		writeH(83); // mEvasion (num)
		writeD((int) (_player.getStat().getAdd(Stats.M_EVASION_RATE)));

		writeH(84); // received vital point attack rate %
		writeD((int) (_player.getStat().getMul(Stats.BLOW_RESIST) * 100) - 100);

		writeH(85); // received vital point attack rate (num)
		writeD((int) (_player.getStat().getAdd(Stats.BLOW_RESIST)));

		writeH(86); // pSkillEvasion (%)
		writeD((int) (_player.getStat().getMul(Stats.P_SKILL_EVASION) * 100) - 100);

		writeH(87); // mSkillEvasion (%)
		writeD((int) (_player.getStat().getMul(Stats.M_EVASION_RATE) * 100) - 100);

		writeH(88); // atkSpd (%)
		writeD((int) (_player.getStat().getMul(Stats.POWER_ATTACK_SPEED) * 100) - 100);

		writeH(89); // atkSpd (num)
		writeD((int) (_player.getStat().getAdd(Stats.POWER_ATTACK_SPEED)));

		writeH(90); // cast spd (%)
		writeD((int) (_player.getStat().getMul(Stats.MAGIC_ATTACK_SPEED) * 100) - 100);

		writeH(91); // cast Spd (num)
		writeD((int) (_player.getStat().getAdd(Stats.MAGIC_ATTACK_SPEED)));

		writeH(92); // speed %
		writeD((int) (_player.getStat().getMul(Stats.RUN_SPEED) * 100) - 100);

		writeH(93); // speed (num)
		writeD((int) (_player.getStat().getAdd(Stats.RUN_SPEED)));

		writeH(94); // basic crit rate(%)
		writeD((int) (_player.getStat().getMul(Stats.P_CRITICAL_RATE) * 100) - 100);

		writeH(95); // basic crit rate (num)
		writeD((int) (_player.getStat().getAdd(Stats.P_CRITICAL_RATE)));

		writeH(96);// p skill crit rate (%)
		writeD((int) (_player.getStat().getMul(Stats.P_SKILL_CRITICAL_RATE) * 100) - 100);

		writeH(97); // p skill crit rate (num)
		writeD((int) (_player.getStat().getAdd(Stats.P_SKILL_CRITICAL_RATE)));

		writeH(98);// m skill crit rate(%)
		writeD((int) (_player.getStat().getMul(Stats.M_CRITICAL_RATE) * 100) - 100);

		writeH(99); // m skill crit rate (num)
		writeD((int) (_player.getStat().getAdd(Stats.M_CRITICAL_RATE)));

		writeH(100); // received crit rate(%)
		writeD((int) (_player.getStat().getMul(Stats.P_CRIT_CHANCE_RECEPTIVE) * 100) - 100);

		writeH(101); // received crit rate(num)
		writeD((int) (_player.getStat().getAdd(Stats.P_CRIT_CHANCE_RECEPTIVE)));

		writeH(102); // received p skill crit rate (%)
		writeD((int) (_player.getStat().getMul(Stats.P_SKILL_CRIT_CHANCE_RECEPTIVE) * 100) - 100);

		writeH(103); // received p skill crit rate (num)
		writeD((int) (_player.getStat().getAdd(Stats.P_SKILL_CRIT_CHANCE_RECEPTIVE)));

		writeH(104); // received m skill crit rate (%)
		writeD((int) (_player.getStat().getMul(Stats.M_CRIT_CHANCE_RECEPTIVE) * 100) - 100);

		writeH(105); // received m skill crit rate (num)
		writeD((int) (_player.getStat().getAdd(Stats.M_CRIT_CHANCE_RECEPTIVE)));

		writeH(106); // basic critical damage %
		writeD((int) (_player.getStat().getMul(Stats.CRITICAL_DAMAGE) * 100) - 100);

		writeH(107); // basic critical damage (num)
		writeD((int) (_player.getStat().getAdd(Stats.CRITICAL_DAMAGE)));

		writeH(108); // p skill crit damage %
		writeD((int) (_player.getStat().getMul(Stats.SKILL_CRITICAL_DAMAGE) * 100) - 100);

		writeH(109); // p skill crit damage (num)
		writeD((int) (_player.getStat().getAdd(Stats.SKILL_CRITICAL_DAMAGE)));

		writeH(110); // m skill crit damage %
		writeD((int) (_player.getStat().getMul(Stats.MAGIC_CRITICAL_DMG) * 100) - 100);

		writeH(111); // m skill crit damage (num)
		writeD((int) (_player.getStat().getAdd(Stats.MAGIC_CRITICAL_DMG)));

		writeH(112); // received critical damage %
		writeD((int) (_player.getStat().getMul(Stats.P_CRIT_DAMAGE_RECEPTIVE) * 100) - 100);

		writeH(113); // received critical damage (num)
		writeD((int) (_player.getStat().getAdd(Stats.P_CRIT_DAMAGE_RECEPTIVE)));

		writeH(114); // received p. skill critical damage %
		writeD((int) (_player.getStat().getMul(Stats.P_SKILL_CRIT_DAMAGE_RECEPTIVE) * 100) - 100);

		writeH(115); // received p. skill critical damage num
		writeD((int) (_player.getStat().getAdd(Stats.P_SKILL_CRIT_DAMAGE_RECEPTIVE)));

		writeH(116); // received m skill critial damage %
		writeD((int) (_player.getStat().getMul(Stats.M_CRIT_DAMAGE_RECEPTIVE) * 100) - 100);

		writeH(117); // received m skill critial damage num
		writeD((int) (_player.getStat().getAdd(Stats.M_CRIT_DAMAGE_RECEPTIVE)));

		writeH(118); // hp recovery potions %
		writeD((int) (_player.getStat().getMul(Stats.POTION_HP_HEAL_EFFECT) * 100) - 100);

		writeH(119); // hp recovery potions num
		writeD((int) (_player.getStat().getAdd(Stats.POTION_HP_HEAL_EFFECT)));

		writeH(120); // mp recovery potions %
		writeD((int) (_player.getStat().getMul(Stats.POTION_MP_HEAL_EFFECT) * 100) - 100);

		writeH(121); // mp recovery potions num
		writeD((int) (_player.getStat().getAdd(Stats.POTION_MP_HEAL_EFFECT)));

		writeH(122); // hp recovery rate (%)
		writeD((int) (_player.getStat().getMul(Stats.REGENERATE_HP_RATE) * 100) - 100);

		writeH(123); // hp recovery rate (num)
		writeD((int) (_player.getStat().getAdd(Stats.REGENERATE_HP_RATE)));

		writeH(124); // TODO: hp recovery rate while standing %
		writeD(0);

		writeH(125); // TODO: hp recovery rate while standing num
		writeD(0);

		writeH(126); // TODO: hp recovery rate while sitting %
		writeD(0);

		writeH(127); // TODO : hp recovery rate while sitting num
		writeD(0);

		writeH(128); // TODO: hp recovery rate while walking %
		writeD(0);

		writeH(129); // TODO: hp recovery rate while walking num
		writeD(0);

		writeH(130); // TODO: hp recovery rate while running %
		writeD(0);

		writeH(131); // TODO: hp recovery rate while running num
		writeD(0);

		writeH(132); // mp recovery rate (%)
		writeD((int) (_player.getStat().getMul(Stats.REGENERATE_MP_RATE) * 100) - 100);

		writeH(133); // mp recovery rate (num)
		writeD((int) (_player.getStat().getAdd(Stats.REGENERATE_MP_RATE)));

		writeH(134); // TODO: mp recovery rate while standing %
		writeD(0);

		writeH(135); // TODO: mp recovery rate while standing num
		writeD(0);

		writeH(136); // TODO: mp recovery rate while sitting %
		writeD(0);

		writeH(137); // TODO: mp recovery rate while sitting num
		writeD(0);

		writeH(138); // TODO: mp recovery rate while walking %
		writeD(0);

		writeH(139); // TODO: mp recovery rate while walking num
		writeD(0);

		writeH(140); // TODO: mp recovery rate while running %
		writeD(0);

		writeH(141); // TODO: mp recovery rate while running num
		writeD(0);

		writeH(142); // cp recovery rate (%)
		writeD((int) (_player.getStat().getMul(Stats.REGENERATE_CP_RATE) * 100) - 100);

		writeH(143); // cp recovery rate (num)
		writeD((int) (_player.getStat().getAdd(Stats.REGENERATE_CP_RATE)));

		writeH(144); // TODO: cp recovery rate while standing %
		writeD(0);

		writeH(145); // TODO: cp recovery rate while standing num
		writeD(0);

		writeH(146); // TODO: cp recovery rate while sitting %
		writeD(0);

		writeH(147); // TODO: cp recovery rate while sitting num
		writeD(0);

		writeH(148); // TODO: cp recovery rate while walking %
		writeD(0);

		writeH(149); // TODO: cp recovery rate while walking num
		writeD(0);

		writeH(150); // TODO: cp recovery rate while running %
		writeD(0);

		writeH(151); // TODO: cp recovery rate while running num
		writeD(0);

		writeH(152); // p skill cooldown%
		writeD((int) (_player.getStat().getMul(Stats.PHYSIC_REUSE_RATE) * 100) - 100);

		writeH(153); // m skill cooldown %
		writeD((int) (_player.getStat().getMul(Stats.MAGIC_REUSE_RATE) * 100) - 100);

		writeH(154); // music cooldown %
		writeD((int) (_player.getStat().getMul(Stats.MUSIC_REUSE_RATE) * 100) - 100);

		writeH(155); // p skill mp consume decrease (%)
		writeD((int) (_player.getStat().getMul(Stats.MP_PHYSICAL_SKILL_CONSUME) * 100) - 100);

		writeH(156); // m skill mp consume decrease (%)
		writeD((int) (_player.getStat().getMul(Stats.MP_MAGIC_SKILL_CONSUME) * 100) - 100);

		writeH(157); // song/dance mp consume decrease (%)
		writeD((int) (_player.getStat().getMul(Stats.MP_DANCE_SKILL_CONSUME) * 100) - 100);

		writeH(158); // p skill mp consume decrease (num)
		writeD((int) (_player.getStat().getAdd(Stats.MP_PHYSICAL_SKILL_CONSUME)));

		writeH(159); // m skill mp consume decrease (num)
		writeD((int) (_player.getStat().getAdd(Stats.MP_MAGIC_SKILL_CONSUME)));

		writeH(160); // song/dance mp consume decrease (num)
		writeD((int) (_player.getStat().getAdd(Stats.MP_DANCE_SKILL_CONSUME)));

		writeH(161); // buff cancel resistance bonus
		writeD((int) (_player.getStat().getMul(Stats.CANCEL_RESIST) * 100) - 100);

		writeH(162); // debuff/ anomaly resistance bonus
		writeD((int) (_player.getStat().getMul(Stats.RESIST_ABNORMAL_DEBUFF) * 100) - 100);

		writeH(163); // paralysis atk rate
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_PARALYZE) * 100) - 100);

		writeH(164); // shock atk rate
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_SHOCK) * 100) - 100);

		writeH(165); // knockback atk rate
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_KNOCKBACK) * 100) - 100);

		writeH(166); // sleep atk rate
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_SLEEP) * 100) - 100);

		writeH(167); // imprisonment atk rate
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_TURN_STONE) * 100) - 100);

		writeH(168); // pull atk rate
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_PULL) * 100) - 100);

		writeH(169); // TODO: fear atk rate
		writeD(0);

		writeH(170); // TODO: silence attack rate
		writeD(0);

		writeH(171); // hold attack rate
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_ROOT_PHYSICALLY) * 100) - 100);

		writeH(172); // TODO: supression attack rate
		writeD(0);

		writeH(173); // infection attack rate
		writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_BLEED) * 100) - 100);

		writeH(174); // paralysis resistance (%)
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_PARALYZE) * 100) - 100);

		writeH(175); // shock resistance
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_SHOCK) * 100) - 100);

		writeH(176); // knockback resistance
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_KNOCKBACK) * 100) - 100);

		writeH(177); // sleep resistance
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_SLEEP) * 100) - 100);

		writeH(178); // imprisonment resistance
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_TURN_STONE) * 100) - 100);

		writeH(179); // pull atk rate
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_PULL) * 100) - 100);

		writeH(180); // TODO: fear resistance
		writeD(0);

		writeH(181); // TODO: silence resistance
		writeD(0);

		writeH(182); // hold resistance
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_ROOT_PHYSICALLY) * 100) - 100);

		writeH(183); // TODO: supression resistance
		writeD(0);

		writeH(184); // infection attack resistance
		writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_BLEED) * 100) - 100);
	}
}