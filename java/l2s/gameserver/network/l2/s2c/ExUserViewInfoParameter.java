package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;
import l2s.gameserver.stats.Stats;

/**
 * @author nexvill
 */
public class ExUserViewInfoParameter implements IClientOutgoingPacket
{
	Player _player;

	public ExUserViewInfoParameter(Player player)
	{
		_player = player;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(185); // count

		packetWriter.writeH(0); // pAtk (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.POWER_ATTACK) * 100) - 100); // param value

		packetWriter.writeH(1); // pAtk (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.POWER_ATTACK)));

		packetWriter.writeH(2); // mAtk(%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.MAGIC_ATTACK) * 100) - 100);

		packetWriter.writeH(3); // mAtk(num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.MAGIC_ATTACK)));

		packetWriter.writeH(4); // Soulshot Damage - Activation
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.SOULSHOT_POWER) * 100) - 100);

		packetWriter.writeH(5); // Spiritshot Damage - Activation
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.SPIRITSHOT_POWER) * 100) - 100);

		packetWriter.writeH(6); // Soulshot Damage - Enchanted Weapons
		if (_player.getActiveWeaponInstance() != null)
		{
			packetWriter.writeD((int) (_player.getActiveWeaponInstance().getEnchantLevel() * 0.7 * 100));
		}
		else
		{
			packetWriter.writeD(0);
		}

		packetWriter.writeH(7); // Spiritshot Damage - Enchanted Weapons
		if (_player.getActiveWeaponInstance() != null)
		{
			packetWriter.writeD((int) (_player.getActiveWeaponInstance().getEnchantLevel() * 0.7 * 100));
		}
		else
		{
			packetWriter.writeD(0);
		}

		packetWriter.writeH(8); // Soulshot Damage - Misc
		if (_player.getActiveWeaponInstance() != null)
		{
			packetWriter.writeD((int) ((_player.getStat().getMul(Stats.SOULSHOT_POWER) * 100) + (_player.getActiveWeaponInstance().getEnchantLevel() * 0.7 * 100) - 100));
		}
		else
		{
			packetWriter.writeD(0);
		}

		packetWriter.writeH(9); // Spiritshot Damage - Misc
		if (_player.getActiveWeaponInstance() != null)
		{
			packetWriter.writeD((int) ((_player.getStat().getMul(Stats.SPIRITSHOT_POWER) * 100) + (_player.getActiveWeaponInstance().getEnchantLevel() * 0.7 * 100) - 100));
		}
		else
		{
			packetWriter.writeD(0);
		}

		packetWriter.writeH(10); // basic pvp damage
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVP_PHYS_DMG_BONUS) * 100) - 100);

		packetWriter.writeH(11); // p. skill damage in pvp
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVP_PHYS_SKILL_DMG_BONUS) * 100) - 100);

		packetWriter.writeH(12); // m. skill damage in pvp
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVP_MAGIC_SKILL_DMG_BONUS) * 100) - 100);

		packetWriter.writeH(13); // TODO: inflicted pvp damage
		packetWriter.writeD(0);

		packetWriter.writeH(14); // TODO: damage decrease ignore
		packetWriter.writeD(0);

		packetWriter.writeH(15); // basic pve damage
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVE_PHYS_DMG_BONUS) * 100) - 100);

		packetWriter.writeH(16); // p. skill damage in pve
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVE_PHYS_SKILL_DMG_BONUS) * 100) - 100);

		packetWriter.writeH(17); // m. skill damage in pve
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVE_MAGIC_SKILL_DMG_BONUS) * 100) - 100);

		packetWriter.writeH(18); // TODO: inflicted pve damage
		packetWriter.writeD(0);

		packetWriter.writeH(19); // TODO: PvE damage decrease ignore
		packetWriter.writeD(0);

		packetWriter.writeH(20); // TODO: basic power
		packetWriter.writeD(0);

		packetWriter.writeH(21); // p. skill power
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.P_SKILL_POWER) * 100) - 100);

		packetWriter.writeH(22); // m. skill power
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.M_SKILL_POWER) * 100) - 100);

		packetWriter.writeH(23); // TODO: AoE skill damage
		packetWriter.writeD(0);

		packetWriter.writeH(24); // damage bonus - sword
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_SWORD) * 100) - 100);

		packetWriter.writeH(25); // damage bonus - ancient sword
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_ANCIENTSWORD) * 100) - 100);

		packetWriter.writeH(26); // damage bonus - dagger
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_DAGGER) * 100) - 100);

		packetWriter.writeH(27); // damage bonus - rapier
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_RAPIER) * 100) - 100);

		packetWriter.writeH(28); // damage bonus - blunt
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_BLUNT) * 100) - 100);

		packetWriter.writeH(29); // damage bonus - spear
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_POLE) * 100) - 100);

		packetWriter.writeH(30); // damage bonus - fists
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_DUALFIST) * 100) - 100);

		packetWriter.writeH(31); // damage bonus dual swords
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_DUAL) * 100) - 100);

		packetWriter.writeH(32); // damage bonus - bow
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_BOW) * 100) - 100);

		packetWriter.writeH(33); // damage bonus - firearms
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_FIREARMS) * 100) - 100);

		packetWriter.writeH(34); // pDef(%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.POWER_DEFENCE) * 100) - 100);

		packetWriter.writeH(35); // pDef(num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.POWER_DEFENCE)));

		packetWriter.writeH(36); // mDef %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.MAGIC_DEFENCE) * 100) - 100);

		packetWriter.writeH(37); // mDef(num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.MAGIC_DEFENCE)));

		packetWriter.writeH(38); // TODO: Soulshot Damage Decrease
		packetWriter.writeD(0);

		packetWriter.writeH(39); // TODO: Spiritshot Damage Decrease
		packetWriter.writeD(0);

		packetWriter.writeH(40); // Received basic pvp damage
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVP_PHYS_DEFENCE_BONUS) * 100) - 100);

		packetWriter.writeH(41); // Received P. Skill damage in pvp
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVP_PHYS_SKILL_DEFENCE_BONUS) * 100) - 100);

		packetWriter.writeH(42); // Received M. Skill damage in pvp
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVP_MAGIC_SKILL_DEFENCE_BONUS) * 100) - 100);

		packetWriter.writeH(43); // TODO: Received PvP damage
		packetWriter.writeD(0);

		packetWriter.writeH(44); // TODO: PvP Damage decrease
		packetWriter.writeD(0);

		packetWriter.writeH(45); // received basic pve damage
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVE_PHYS_DEFENCE_BONUS) * 100) - 100);

		packetWriter.writeH(46); // received p. skill damage in pve
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVE_PHYS_SKILL_DEFENCE_BONUS) * 100) - 100);

		packetWriter.writeH(47); // received m. skill damage in pve
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PVE_MAGIC_SKILL_DEFENCE_BONUS) * 100) - 100);

		packetWriter.writeH(48); // TODO: received pve damage
		packetWriter.writeD(0);

		packetWriter.writeH(49); // TODO: pve damage decrease
		packetWriter.writeD(0);

		packetWriter.writeH(50); // TODO: received basic damage power
		packetWriter.writeD(0);

		packetWriter.writeH(51); // TODO: received p. skill power damage
		packetWriter.writeD(0);

		packetWriter.writeH(52); // TODO: received m. skill power damage
		packetWriter.writeD(0);

		packetWriter.writeH(53); // TODO: received AoE skill damage
		packetWriter.writeD(0);

		packetWriter.writeH(54); // damage resistance bonus - sword
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_SWORD) * 100) - 100);

		packetWriter.writeH(55); // damage resistance bonus - ancient sword
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_ANCIENTSWORD) * 100) - 100);

		packetWriter.writeH(56); // damage resistance bonus - dagger
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_DAGGER) * 100) - 100);

		packetWriter.writeH(57); // damage resistance bonus - rapier
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_RAPIER) * 100) - 100);

		packetWriter.writeH(58); // damage resistance bonus - blunt
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_BLUNT) * 100) - 100);

		packetWriter.writeH(59); // damage resistance bonus - spear
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_POLE) * 100) - 100);

		packetWriter.writeH(60); // damage resistance bonus - first
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_DUALFIST) * 100) - 100);

		packetWriter.writeH(61); // damage resistance bonus - dual sword
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_DUAL) * 100) - 100);

		packetWriter.writeH(62); // damage resistance bonus - bow
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_BOW) * 100) - 100);

		packetWriter.writeH(63); // damage resistance bonus - firearms
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_FIREARMS) * 100) - 100);

		packetWriter.writeH(64); // shield defense %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.SHIELD_DEFENCE) * 100) - 100);

		packetWriter.writeH(65); // shield defense (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.SHIELD_DEFENCE)));

		packetWriter.writeH(66); // shield defense rate
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.SHIELD_RATE) * 100) - 100);

		packetWriter.writeH(67); // magic damage resistance %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.MAGIC_RESIST) * 100) - 100);

		packetWriter.writeH(68); // magic damage resistance (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.MAGIC_RESIST)));

		packetWriter.writeH(69); // magic damage reflection %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.REFLECT_MAGIC_SKILL) * 100) - 100);

		packetWriter.writeH(70); // TODO: magic damage reflection resistance
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.RESIST_REFLECT_DAM) * 100) - 100);

		packetWriter.writeH(71); // TODO: received fixed damage %
		packetWriter.writeD(0);

		packetWriter.writeH(72); // casting interruption rate %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.CAST_INTERRUPT) * 100) - 100);

		packetWriter.writeH(73); // casting interruption rate (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.CAST_INTERRUPT)));

		packetWriter.writeH(74); // p. accuracy %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.P_ACCURACY_COMBAT) * 100) - 100);

		packetWriter.writeH(75); // p accuracy (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.P_ACCURACY_COMBAT)));

		packetWriter.writeH(76); // m accuracy %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.M_ACCURACY_COMBAT) * 100) - 100);

		packetWriter.writeH(77); // m accuracy (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.M_ACCURACY_COMBAT)));

		packetWriter.writeH(78); // vital point attack rate %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.FATALBLOW_RATE) * 100) - 100);

		packetWriter.writeH(79); // vital point attack rate (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.FATALBLOW_RATE)));

		packetWriter.writeH(80); // pEvasion %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.P_EVASION_RATE) * 100) - 100);

		packetWriter.writeH(81); // pEvasion (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.P_EVASION_RATE)));

		packetWriter.writeH(82); // mEvasion %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.M_EVASION_RATE) * 100) - 100);

		packetWriter.writeH(83); // mEvasion (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.M_EVASION_RATE)));

		packetWriter.writeH(84); // received vital point attack rate %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.BLOW_RESIST) * 100) - 100);

		packetWriter.writeH(85); // received vital point attack rate (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.BLOW_RESIST)));

		packetWriter.writeH(86); // pSkillEvasion (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.P_SKILL_EVASION) * 100) - 100);

		packetWriter.writeH(87); // mSkillEvasion (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.M_EVASION_RATE) * 100) - 100);

		packetWriter.writeH(88); // atkSpd (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.POWER_ATTACK_SPEED) * 100) - 100);

		packetWriter.writeH(89); // atkSpd (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.POWER_ATTACK_SPEED)));

		packetWriter.writeH(90); // cast spd (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.MAGIC_ATTACK_SPEED) * 100) - 100);

		packetWriter.writeH(91); // cast Spd (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.MAGIC_ATTACK_SPEED)));

		packetWriter.writeH(92); // speed %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.RUN_SPEED) * 100) - 100);

		packetWriter.writeH(93); // speed (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.RUN_SPEED)));

		packetWriter.writeH(94); // basic crit rate(%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.P_CRITICAL_RATE) * 100) - 100);

		packetWriter.writeH(95); // basic crit rate (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.P_CRITICAL_RATE)));

		packetWriter.writeH(96);// p skill crit rate (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.P_SKILL_CRITICAL_RATE) * 100) - 100);

		packetWriter.writeH(97); // p skill crit rate (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.P_SKILL_CRITICAL_RATE)));

		packetWriter.writeH(98);// m skill crit rate(%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.M_CRITICAL_RATE) * 100) - 100);

		packetWriter.writeH(99); // m skill crit rate (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.M_CRITICAL_RATE)));

		packetWriter.writeH(100); // received crit rate(%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.P_CRIT_CHANCE_RECEPTIVE) * 100) - 100);

		packetWriter.writeH(101); // received crit rate(num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.P_CRIT_CHANCE_RECEPTIVE)));

		packetWriter.writeH(102); // received p skill crit rate (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.P_SKILL_CRIT_CHANCE_RECEPTIVE) * 100) - 100);

		packetWriter.writeH(103); // received p skill crit rate (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.P_SKILL_CRIT_CHANCE_RECEPTIVE)));

		packetWriter.writeH(104); // received m skill crit rate (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.M_CRIT_CHANCE_RECEPTIVE) * 100) - 100);

		packetWriter.writeH(105); // received m skill crit rate (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.M_CRIT_CHANCE_RECEPTIVE)));

		packetWriter.writeH(106); // basic critical damage %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.CRITICAL_DAMAGE) * 100) - 100);

		packetWriter.writeH(107); // basic critical damage (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.CRITICAL_DAMAGE)));

		packetWriter.writeH(108); // p skill crit damage %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.SKILL_CRITICAL_DAMAGE) * 100) - 100);

		packetWriter.writeH(109); // p skill crit damage (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.SKILL_CRITICAL_DAMAGE)));

		packetWriter.writeH(110); // m skill crit damage %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.MAGIC_CRITICAL_DMG) * 100) - 100);

		packetWriter.writeH(111); // m skill crit damage (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.MAGIC_CRITICAL_DMG)));

		packetWriter.writeH(112); // received critical damage %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.P_CRIT_DAMAGE_RECEPTIVE) * 100) - 100);

		packetWriter.writeH(113); // received critical damage (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.P_CRIT_DAMAGE_RECEPTIVE)));

		packetWriter.writeH(114); // received p. skill critical damage %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.P_SKILL_CRIT_DAMAGE_RECEPTIVE) * 100) - 100);

		packetWriter.writeH(115); // received p. skill critical damage num
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.P_SKILL_CRIT_DAMAGE_RECEPTIVE)));

		packetWriter.writeH(116); // received m skill critial damage %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.M_CRIT_DAMAGE_RECEPTIVE) * 100) - 100);

		packetWriter.writeH(117); // received m skill critial damage num
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.M_CRIT_DAMAGE_RECEPTIVE)));

		packetWriter.writeH(118); // hp recovery potions %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.POTION_HP_HEAL_EFFECT) * 100) - 100);

		packetWriter.writeH(119); // hp recovery potions num
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.POTION_HP_HEAL_EFFECT)));

		packetWriter.writeH(120); // mp recovery potions %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.POTION_MP_HEAL_EFFECT) * 100) - 100);

		packetWriter.writeH(121); // mp recovery potions num
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.POTION_MP_HEAL_EFFECT)));

		packetWriter.writeH(122); // hp recovery rate (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.REGENERATE_HP_RATE) * 100) - 100);

		packetWriter.writeH(123); // hp recovery rate (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.REGENERATE_HP_RATE)));

		packetWriter.writeH(124); // TODO: hp recovery rate while standing %
		packetWriter.writeD(0);

		packetWriter.writeH(125); // TODO: hp recovery rate while standing num
		packetWriter.writeD(0);

		packetWriter.writeH(126); // TODO: hp recovery rate while sitting %
		packetWriter.writeD(0);

		packetWriter.writeH(127); // TODO : hp recovery rate while sitting num
		packetWriter.writeD(0);

		packetWriter.writeH(128); // TODO: hp recovery rate while walking %
		packetWriter.writeD(0);

		packetWriter.writeH(129); // TODO: hp recovery rate while walking num
		packetWriter.writeD(0);

		packetWriter.writeH(130); // TODO: hp recovery rate while running %
		packetWriter.writeD(0);

		packetWriter.writeH(131); // TODO: hp recovery rate while running num
		packetWriter.writeD(0);

		packetWriter.writeH(132); // mp recovery rate (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.REGENERATE_MP_RATE) * 100) - 100);

		packetWriter.writeH(133); // mp recovery rate (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.REGENERATE_MP_RATE)));

		packetWriter.writeH(134); // TODO: mp recovery rate while standing %
		packetWriter.writeD(0);

		packetWriter.writeH(135); // TODO: mp recovery rate while standing num
		packetWriter.writeD(0);

		packetWriter.writeH(136); // TODO: mp recovery rate while sitting %
		packetWriter.writeD(0);

		packetWriter.writeH(137); // TODO: mp recovery rate while sitting num
		packetWriter.writeD(0);

		packetWriter.writeH(138); // TODO: mp recovery rate while walking %
		packetWriter.writeD(0);

		packetWriter.writeH(139); // TODO: mp recovery rate while walking num
		packetWriter.writeD(0);

		packetWriter.writeH(140); // TODO: mp recovery rate while running %
		packetWriter.writeD(0);

		packetWriter.writeH(141); // TODO: mp recovery rate while running num
		packetWriter.writeD(0);

		packetWriter.writeH(142); // cp recovery rate (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.REGENERATE_CP_RATE) * 100) - 100);

		packetWriter.writeH(143); // cp recovery rate (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.REGENERATE_CP_RATE)));

		packetWriter.writeH(144); // TODO: cp recovery rate while standing %
		packetWriter.writeD(0);

		packetWriter.writeH(145); // TODO: cp recovery rate while standing num
		packetWriter.writeD(0);

		packetWriter.writeH(146); // TODO: cp recovery rate while sitting %
		packetWriter.writeD(0);

		packetWriter.writeH(147); // TODO: cp recovery rate while sitting num
		packetWriter.writeD(0);

		packetWriter.writeH(148); // TODO: cp recovery rate while walking %
		packetWriter.writeD(0);

		packetWriter.writeH(149); // TODO: cp recovery rate while walking num
		packetWriter.writeD(0);

		packetWriter.writeH(150); // TODO: cp recovery rate while running %
		packetWriter.writeD(0);

		packetWriter.writeH(151); // TODO: cp recovery rate while running num
		packetWriter.writeD(0);

		packetWriter.writeH(152); // p skill cooldown%
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.PHYSIC_REUSE_RATE) * 100) - 100);

		packetWriter.writeH(153); // m skill cooldown %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.MAGIC_REUSE_RATE) * 100) - 100);

		packetWriter.writeH(154); // music cooldown %
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.MUSIC_REUSE_RATE) * 100) - 100);

		packetWriter.writeH(155); // p skill mp consume decrease (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.MP_PHYSICAL_SKILL_CONSUME) * 100) - 100);

		packetWriter.writeH(156); // m skill mp consume decrease (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.MP_MAGIC_SKILL_CONSUME) * 100) - 100);

		packetWriter.writeH(157); // song/dance mp consume decrease (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.MP_DANCE_SKILL_CONSUME) * 100) - 100);

		packetWriter.writeH(158); // p skill mp consume decrease (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.MP_PHYSICAL_SKILL_CONSUME)));

		packetWriter.writeH(159); // m skill mp consume decrease (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.MP_MAGIC_SKILL_CONSUME)));

		packetWriter.writeH(160); // song/dance mp consume decrease (num)
		packetWriter.writeD((int) (_player.getStat().getAdd(Stats.MP_DANCE_SKILL_CONSUME)));

		packetWriter.writeH(161); // buff cancel resistance bonus
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.CANCEL_RESIST) * 100) - 100);

		packetWriter.writeH(162); // debuff/ anomaly resistance bonus
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.RESIST_ABNORMAL_DEBUFF) * 100) - 100);

		packetWriter.writeH(163); // paralysis atk rate
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_PARALYZE) * 100) - 100);

		packetWriter.writeH(164); // shock atk rate
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_SHOCK) * 100) - 100);

		packetWriter.writeH(165); // knockback atk rate
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_KNOCKBACK) * 100) - 100);

		packetWriter.writeH(166); // sleep atk rate
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_SLEEP) * 100) - 100);

		packetWriter.writeH(167); // imprisonment atk rate
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_TURN_STONE) * 100) - 100);

		packetWriter.writeH(168); // pull atk rate
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_PULL) * 100) - 100);

		packetWriter.writeH(169); // TODO: fear atk rate
		packetWriter.writeD(0);

		packetWriter.writeH(170); // TODO: silence attack rate
		packetWriter.writeD(0);

		packetWriter.writeH(171); // hold attack rate
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_ROOT_PHYSICALLY) * 100) - 100);

		packetWriter.writeH(172); // TODO: supression attack rate
		packetWriter.writeD(0);

		packetWriter.writeH(173); // infection attack rate
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.ATTACK_TRAIT_BLEED) * 100) - 100);

		packetWriter.writeH(174); // paralysis resistance (%)
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_PARALYZE) * 100) - 100);

		packetWriter.writeH(175); // shock resistance
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_SHOCK) * 100) - 100);

		packetWriter.writeH(176); // knockback resistance
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_KNOCKBACK) * 100) - 100);

		packetWriter.writeH(177); // sleep resistance
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_SLEEP) * 100) - 100);

		packetWriter.writeH(178); // imprisonment resistance
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_TURN_STONE) * 100) - 100);

		packetWriter.writeH(179); // pull atk rate
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_PULL) * 100) - 100);

		packetWriter.writeH(180); // TODO: fear resistance
		packetWriter.writeD(0);

		packetWriter.writeH(181); // TODO: silence resistance
		packetWriter.writeD(0);

		packetWriter.writeH(182); // hold resistance
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_ROOT_PHYSICALLY) * 100) - 100);

		packetWriter.writeH(183); // TODO: supression resistance
		packetWriter.writeD(0);

		packetWriter.writeH(184); // infection attack resistance
		packetWriter.writeD((int) (_player.getStat().getMul(Stats.DEFENCE_TRAIT_BLEED) * 100) - 100);
	}
}