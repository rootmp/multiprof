package l2s.gameserver.skills.effects.permanent;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.stats.StatModifierType;
import l2s.gameserver.stats.Stats;
import l2s.gameserver.templates.skill.EffectTemplate;

/**
 * @author Bonux
 **/
public final class p_max_hp extends p_abstract_stat_effect
{
	private final boolean _restore;

	public p_max_hp(EffectTemplate template)
	{
		super(template, Stats.MAX_HP);
		_restore = getParams().getBool("restore", false);
	}

	@Override
	public void onApplied(Abnormal abnormal, Creature effector, Creature effected)
	{
		if(!_restore || effected.isHealBlocked())
			return;

		double power = getValue();
		if(getModifierType() == StatModifierType.PER)
			power = power / 100. * effected.getMaxHp();

		if(power > 0)
		{
			// effected.sendPacket(new
			// SystemMessagePacket(SystemMsg.S1_HP_HAS_BEEN_RESTORED).addInteger(power));
			// TODO: Проверить на оффе, должно ли быть сообщение.
			effected.setCurrentHp(effected.getCurrentHp() + power, false);
		}
	}
}