package l2s.gameserver.skills.effects;

import l2s.gameserver.handler.effects.EffectHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.templates.skill.EffectTemplate;

public class EffectPercentPatk extends EffectHandler
{
	public EffectPercentPatk(EffectTemplate template)
	{
		super(template);
	}

	@Override
	protected boolean checkCondition(Abnormal abnormal, Creature effector, Creature effected)
	{
		if (effected.isDead() || effected.isRaid())
			return false;
		return true;
	}

	@Override
	public void onStart(Abnormal abnormal, Creature effector, Creature effected)
	{
		if (effected.isDead())
			return;

		double damage = (effector.getPAtk(effector) / 100 * getValue());
		effected.reduceCurrentHp(damage, effector, getSkill(), true, true, false, false, false, false, true, true, false, false, false, 0, false);
	}
}