package l2s.gameserver.handler.effects;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.skills.effects.EffectAbsorbDamageToEffector;
import l2s.gameserver.skills.effects.EffectAbsorbDamageToMp;
import l2s.gameserver.skills.effects.EffectAbsorbDamageToSummon;
import l2s.gameserver.skills.effects.EffectAddSkills;
import l2s.gameserver.skills.effects.EffectAgathionResurrect;
import l2s.gameserver.skills.effects.EffectArmorBreaker;
import l2s.gameserver.skills.effects.EffectBetray;
import l2s.gameserver.skills.effects.EffectBpReduceOnHit;
import l2s.gameserver.skills.effects.EffectBuff;
import l2s.gameserver.skills.effects.EffectCPDamPercent;
import l2s.gameserver.skills.effects.EffectCPDrain;
import l2s.gameserver.skills.effects.EffectCharge;
import l2s.gameserver.skills.effects.EffectCharmOfCourage;
import l2s.gameserver.skills.effects.EffectCurseOfLifeFlow;
import l2s.gameserver.skills.effects.EffectDamageBlock;
import l2s.gameserver.skills.effects.EffectDamageBlockCount;
import l2s.gameserver.skills.effects.EffectDamageHealToEffector;
import l2s.gameserver.skills.effects.EffectDamageOnSkillUse;
import l2s.gameserver.skills.effects.EffectDeathImmunity;
import l2s.gameserver.skills.effects.EffectDestroySummon;
import l2s.gameserver.skills.effects.EffectDisarm;
import l2s.gameserver.skills.effects.EffectDiscord;
import l2s.gameserver.skills.effects.EffectDispelOnHit;
import l2s.gameserver.skills.effects.EffectDistortedSpace;
import l2s.gameserver.skills.effects.EffectEffectImmunity;
import l2s.gameserver.skills.effects.EffectEnervation;
import l2s.gameserver.skills.effects.EffectFakeDeath;
import l2s.gameserver.skills.effects.EffectFear;
import l2s.gameserver.skills.effects.EffectFlyUp;
import l2s.gameserver.skills.effects.EffectGrow;
import l2s.gameserver.skills.effects.EffectHPDamPercent;
import l2s.gameserver.skills.effects.EffectHPDrain;
import l2s.gameserver.skills.effects.EffectHate;
import l2s.gameserver.skills.effects.EffectHealBlock;
import l2s.gameserver.skills.effects.EffectHpToOne;
import l2s.gameserver.skills.effects.EffectIgnoreSkill;
import l2s.gameserver.skills.effects.EffectInterrupt;
import l2s.gameserver.skills.effects.EffectInvisible;
import l2s.gameserver.skills.effects.EffectInvulnerable;
import l2s.gameserver.skills.effects.EffectKnockBack;
import l2s.gameserver.skills.effects.EffectKnockDown;
import l2s.gameserver.skills.effects.EffectLDManaDamOverTime;
import l2s.gameserver.skills.effects.EffectLaksis;
import l2s.gameserver.skills.effects.EffectLockInventory;
import l2s.gameserver.skills.effects.EffectMPDamPercent;
import l2s.gameserver.skills.effects.EffectMPDrain;
import l2s.gameserver.skills.effects.EffectManaDamOverTime;
import l2s.gameserver.skills.effects.EffectMeditation;
import l2s.gameserver.skills.effects.EffectMoveToEffector;
import l2s.gameserver.skills.effects.EffectMutation;
import l2s.gameserver.skills.effects.EffectMute;
import l2s.gameserver.skills.effects.EffectMuteAll;
import l2s.gameserver.skills.effects.EffectMuteAttack;
import l2s.gameserver.skills.effects.EffectMuteChance;
import l2s.gameserver.skills.effects.EffectMutePhisycal;
import l2s.gameserver.skills.effects.EffectNegateMark;
import l2s.gameserver.skills.effects.EffectParalyze;
import l2s.gameserver.skills.effects.EffectPercentPatk;
import l2s.gameserver.skills.effects.EffectPetrification;
import l2s.gameserver.skills.effects.EffectRelax;
import l2s.gameserver.skills.effects.EffectReplaceSkill;
import l2s.gameserver.skills.effects.EffectRestoreCP;
import l2s.gameserver.skills.effects.EffectRestoreHP;
import l2s.gameserver.skills.effects.EffectRestoreMP;
import l2s.gameserver.skills.effects.EffectSalvation;
import l2s.gameserver.skills.effects.EffectServitorShare;
import l2s.gameserver.skills.effects.EffectShadowStep;
import l2s.gameserver.skills.effects.EffectSilentMove;
import l2s.gameserver.skills.effects.EffectSleep;
import l2s.gameserver.skills.effects.EffectStun;
import l2s.gameserver.skills.effects.EffectThrowHorizontal;
import l2s.gameserver.skills.effects.EffectThrowUp;
import l2s.gameserver.skills.effects.EffectTransformation;
import l2s.gameserver.skills.effects.EffectVisualTransformation;
import l2s.gameserver.skills.effects.EffectVitality;
import l2s.gameserver.skills.effects.consume.c_mp;
import l2s.gameserver.skills.effects.consume.c_mp_by_level;
import l2s.gameserver.skills.effects.instant.i_add_dp;
import l2s.gameserver.skills.effects.instant.i_add_hate;
import l2s.gameserver.skills.effects.instant.i_add_magic_lamp_points;
import l2s.gameserver.skills.effects.instant.i_add_random_craft_points;
import l2s.gameserver.skills.effects.instant.i_add_sayha_grace_points;
import l2s.gameserver.skills.effects.instant.i_add_soul;
import l2s.gameserver.skills.effects.instant.i_align_direction;
import l2s.gameserver.skills.effects.instant.i_call_random_skill;
import l2s.gameserver.skills.effects.instant.i_call_skill;
import l2s.gameserver.skills.effects.instant.i_call_skill_by_target;
import l2s.gameserver.skills.effects.instant.i_death;
import l2s.gameserver.skills.effects.instant.i_death_link;
import l2s.gameserver.skills.effects.instant.i_delete_hate;
import l2s.gameserver.skills.effects.instant.i_delete_hate_of_me;
import l2s.gameserver.skills.effects.instant.i_dispel_all;
import l2s.gameserver.skills.effects.instant.i_dispel_by_category;
import l2s.gameserver.skills.effects.instant.i_dispel_by_slot;
import l2s.gameserver.skills.effects.instant.i_dispel_by_slot_myself;
import l2s.gameserver.skills.effects.instant.i_dispel_by_slot_probability;
import l2s.gameserver.skills.effects.instant.i_fishing_shot;
import l2s.gameserver.skills.effects.instant.i_get_agro;
import l2s.gameserver.skills.effects.instant.i_get_exp;
import l2s.gameserver.skills.effects.instant.i_hp_drain;
import l2s.gameserver.skills.effects.instant.i_m_attack;
import l2s.gameserver.skills.effects.instant.i_my_summon_kill;
import l2s.gameserver.skills.effects.instant.i_p_attack;
import l2s.gameserver.skills.effects.instant.i_p_hit;
import l2s.gameserver.skills.effects.instant.i_pledge_reputation;
import l2s.gameserver.skills.effects.instant.i_randomize_hate;
import l2s.gameserver.skills.effects.instant.i_refresh_instance;
import l2s.gameserver.skills.effects.instant.i_refresh_instance_group;
import l2s.gameserver.skills.effects.instant.i_reset_skill_reuse;
import l2s.gameserver.skills.effects.instant.i_restore_time_restrict_field;
import l2s.gameserver.skills.effects.instant.i_set_skill;
import l2s.gameserver.skills.effects.instant.i_soul_shot;
import l2s.gameserver.skills.effects.instant.i_sp;
import l2s.gameserver.skills.effects.instant.i_spirit_shot;
import l2s.gameserver.skills.effects.instant.i_spoil;
import l2s.gameserver.skills.effects.instant.i_summon_agathion;
import l2s.gameserver.skills.effects.instant.i_summon_cubic;
import l2s.gameserver.skills.effects.instant.i_summon_soul_shot;
import l2s.gameserver.skills.effects.instant.i_summon_spirit_shot;
import l2s.gameserver.skills.effects.instant.i_target_cancel;
import l2s.gameserver.skills.effects.instant.i_target_me;
import l2s.gameserver.skills.effects.instant.i_unsummon_agathion;
import l2s.gameserver.skills.effects.permanent.p_attack_trait;
import l2s.gameserver.skills.effects.permanent.p_block_buff_slot;
import l2s.gameserver.skills.effects.permanent.p_block_chat;
import l2s.gameserver.skills.effects.permanent.p_block_debuff;
import l2s.gameserver.skills.effects.permanent.p_block_escape;
import l2s.gameserver.skills.effects.permanent.p_block_move;
import l2s.gameserver.skills.effects.permanent.p_block_party;
import l2s.gameserver.skills.effects.permanent.p_block_target;
import l2s.gameserver.skills.effects.permanent.p_critical_damage;
import l2s.gameserver.skills.effects.permanent.p_defence_trait;
import l2s.gameserver.skills.effects.permanent.p_get_item_by_exp;
import l2s.gameserver.skills.effects.permanent.p_heal_effect;
import l2s.gameserver.skills.effects.permanent.p_magic_critical_dmg;
import l2s.gameserver.skills.effects.permanent.p_max_cp;
import l2s.gameserver.skills.effects.permanent.p_max_hp;
import l2s.gameserver.skills.effects.permanent.p_max_mp;
import l2s.gameserver.skills.effects.permanent.p_passive;
import l2s.gameserver.skills.effects.permanent.p_preserve_abnormal;
import l2s.gameserver.skills.effects.permanent.p_raid_berserk;
import l2s.gameserver.skills.effects.permanent.p_skill_critical_damage;
import l2s.gameserver.skills.effects.permanent.p_target_me;
import l2s.gameserver.skills.effects.permanent.p_violet_boy;
import l2s.gameserver.skills.effects.tick.t_hp;
import l2s.gameserver.skills.effects.tick.t_hp_magic;
import l2s.gameserver.skills.effects.tick.t_hp_percent_patk;
import l2s.gameserver.templates.skill.EffectTemplate;

/**
 * @author Bonux
 **/
public class EffectHandlerHolder extends AbstractHolder
{
	private static final EffectHandlerHolder _instance = new EffectHandlerHolder();

	public static EffectHandlerHolder getInstance()
	{
		return _instance;
	}

	private Map<String, Constructor<? extends EffectHandler>> _handlerConstructors = new HashMap<String, Constructor<? extends EffectHandler>>();

	private EffectHandlerHolder()
	{
		// Old Effect
		// TODO: Remake to offlike effects
		registerHandler(EffectAddSkills.class);
		registerHandler(EffectAgathionResurrect.class);
		registerHandler(EffectBetray.class);
		registerHandler(EffectBpReduceOnHit.class);
		registerHandler(EffectBuff.class);
		registerHandler(EffectDamageBlock.class);
		registerHandler(EffectDamageBlockCount.class);
		registerHandler(EffectDistortedSpace.class);
		registerHandler(EffectCharge.class);
		registerHandler(EffectCharmOfCourage.class);
		registerHandler(EffectCPDamPercent.class);
		registerHandler(EffectDamageHealToEffector.class);
		registerHandler(EffectDestroySummon.class);
		registerHandler(EffectDeathImmunity.class);
		registerHandler(EffectDisarm.class);
		registerHandler(EffectDiscord.class);
		registerHandler(EffectDispelOnHit.class);
		registerHandler(EffectEffectImmunity.class);
		registerHandler(EffectEnervation.class);
		registerHandler(EffectFakeDeath.class);
		registerHandler(EffectFear.class);
		registerHandler(EffectMoveToEffector.class);
		registerHandler(EffectGrow.class);
		registerHandler(EffectHate.class);
		registerHandler(EffectHealBlock.class);
		registerHandler(EffectHPDamPercent.class);
		registerHandler(EffectHpToOne.class);
		registerHandler(EffectIgnoreSkill.class);
		registerHandler(EffectInterrupt.class);
		registerHandler(EffectInvulnerable.class);
		registerHandler(EffectInvisible.class);
		registerHandler(EffectLockInventory.class);
		registerHandler(EffectCurseOfLifeFlow.class);
		registerHandler(EffectLaksis.class);
		registerHandler(EffectLDManaDamOverTime.class);
		registerHandler(EffectManaDamOverTime.class);
		registerHandler(EffectMeditation.class);
		registerHandler(EffectMPDamPercent.class);
		registerHandler(EffectMute.class);
		registerHandler(EffectMuteChance.class);
		registerHandler(EffectMuteAll.class);
		registerHandler(EffectMutation.class);
		registerHandler(EffectMuteAttack.class);
		registerHandler(EffectMutePhisycal.class);
		registerHandler(EffectNegateMark.class);
		registerHandler(EffectParalyze.class);
		registerHandler(EffectPetrification.class);
		registerHandler(EffectRelax.class);
		registerHandler(EffectReplaceSkill.class);
		registerHandler(EffectSalvation.class);
		registerHandler(EffectServitorShare.class);
		registerHandler(EffectSilentMove.class);
		registerHandler(EffectSleep.class);
		registerHandler(EffectStun.class);
		registerHandler(EffectKnockDown.class);
		registerHandler(EffectKnockBack.class);
		registerHandler(EffectFlyUp.class);
		registerHandler(EffectThrowHorizontal.class);
		registerHandler(EffectThrowUp.class);
		registerHandler(EffectTransformation.class);
		registerHandler(EffectVisualTransformation.class);
		registerHandler(EffectVitality.class);
		registerHandler(EffectShadowStep.class);
		registerHandler(EffectPercentPatk.class);

		registerHandler(EffectRestoreCP.class);
		registerHandler(EffectRestoreHP.class);
		registerHandler(EffectRestoreMP.class);

		registerHandler(EffectCPDrain.class);
		registerHandler(EffectHPDrain.class);
		registerHandler(EffectMPDrain.class);

		registerHandler(EffectAbsorbDamageToEffector.class); // абсорбирует часть дамага к еффектора еффекта
		registerHandler(EffectAbsorbDamageToMp.class); // абсорбирует часть дамага в мп
		registerHandler(EffectAbsorbDamageToSummon.class); // абсорбирует часть дамага к сумону

		registerHandler(EffectArmorBreaker.class);
		registerHandler(EffectDamageOnSkillUse.class);

		// Offlike Effects

		// Consume Effects
		registerHandler(c_mp.class);
		registerHandler(c_mp_by_level.class);

		// Instant Effects
		registerHandler(i_add_hate.class);
		registerHandler(i_add_magic_lamp_points.class);
		registerHandler(i_add_random_craft_points.class);
		registerHandler(i_add_sayha_grace_points.class);
		registerHandler(i_add_soul.class);
		registerHandler(i_align_direction.class);
		registerHandler(i_call_random_skill.class);
		registerHandler(i_call_skill.class);
		registerHandler(i_call_skill_by_target.class);
		registerHandler(i_dispel_all.class);
		registerHandler(i_dispel_by_category.class);
		registerHandler(i_dispel_by_slot.class);
		registerHandler(i_dispel_by_slot_myself.class);
		registerHandler(i_dispel_by_slot_probability.class);
		registerHandler(i_death.class);
		registerHandler(i_death_link.class);
		registerHandler(i_delete_hate.class);
		registerHandler(i_delete_hate_of_me.class);
		registerHandler(i_fishing_shot.class);
		registerHandler(i_get_agro.class);
		registerHandler(i_get_exp.class);
		registerHandler(i_hp_drain.class);
		registerHandler(i_m_attack.class);
		registerHandler(i_my_summon_kill.class);
		registerHandler(i_p_attack.class);
		registerHandler(i_p_hit.class);
		registerHandler(i_pledge_reputation.class);
		registerHandler(i_randomize_hate.class);
		registerHandler(i_refresh_instance.class);
		registerHandler(i_refresh_instance_group.class);
		registerHandler(i_reset_skill_reuse.class);
		registerHandler(i_restore_time_restrict_field.class);
		registerHandler(i_set_skill.class);
		registerHandler(i_sp.class);
		registerHandler(i_soul_shot.class);
		registerHandler(i_spirit_shot.class);
		registerHandler(i_spoil.class);
		registerHandler(i_summon_agathion.class);
		registerHandler(i_summon_cubic.class);
		registerHandler(i_summon_soul_shot.class);
		registerHandler(i_summon_spirit_shot.class);
		registerHandler(i_target_cancel.class);
		registerHandler(i_target_me.class);
		registerHandler(i_unsummon_agathion.class);
		registerHandler(i_add_dp.class);

		// Permanent Effects
		registerHandler(p_attack_trait.class);
		registerHandler(p_block_buff_slot.class);
		registerHandler(p_block_chat.class);
		registerHandler(p_block_debuff.class);
		registerHandler(p_block_escape.class);
		registerHandler(p_block_move.class);
		registerHandler(p_block_party.class);
		registerHandler(p_block_target.class);
		registerHandler(p_critical_damage.class);
		registerHandler(p_defence_trait.class);
		registerHandler(p_get_item_by_exp.class);
		registerHandler(p_heal_effect.class);
		registerHandler(p_magic_critical_dmg.class);
		registerHandler(p_max_cp.class);
		registerHandler(p_max_hp.class);
		registerHandler(p_max_mp.class);
		registerHandler(p_passive.class);
		registerHandler(p_preserve_abnormal.class);
		registerHandler(p_raid_berserk.class);
		registerHandler(p_skill_critical_damage.class);
		registerHandler(p_target_me.class);
		registerHandler(p_violet_boy.class);

		// Tick Effects
		registerHandler(t_hp.class);
		registerHandler(t_hp_magic.class);
		registerHandler(t_hp_percent_patk.class);
	}

	public void registerHandler(Class<? extends EffectHandler> handlerClass)
	{
		String name = EffectHandler.getName(handlerClass);
		if (_handlerConstructors.containsKey(name))
		{
			warn("EffectHandlerHolder: Dublicate handler registered! Handler: CLASS[" + handlerClass.getSimpleName() + "], NAME[" + name + "]");
			return;
		}

		try
		{
			_handlerConstructors.put(name, handlerClass.getConstructor(new Class<?>[]
			{
				EffectTemplate.class
			}));
		}
		catch (Exception e)
		{
			error("EffectHandlerHolder: Error while loading handler: " + e, e);
		}
	}

	public EffectHandler makeHandler(String handlerName, EffectTemplate template)
	{
		if (StringUtils.isEmpty(handlerName))
			return new EffectHandler(template);

		Constructor<? extends EffectHandler> constructor = _handlerConstructors.get(handlerName.toLowerCase());
		if (constructor == null)
		{
			warn("EffectHandlerHolder: Not found handler: " + handlerName);
			return new EffectHandler(template);
		}

		try
		{
			return (EffectHandler) constructor.newInstance(template);
		}
		catch (Exception e)
		{
			error("EffectHandlerHolder: Error while making handler: " + e, e);
			return new EffectHandler(template);
		}
	}

	@Override
	public int size()
	{
		return _handlerConstructors.size();
	}

	@Override
	public void clear()
	{
		_handlerConstructors.clear();
	}
}
