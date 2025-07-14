package l2s.gameserver.skills.effects.tick;

import l2s.gameserver.handler.effects.EffectHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.templates.skill.EffectTemplate;

/**
 * @author SanyaDC
 **/
public class t_hp_percent_patk extends EffectHandler
{
	private final boolean _percent;

	public t_hp_percent_patk(EffectTemplate template)
	{
		super(template);
		_percent = getTemplate().getParams().getBool("percent", false);
	}

	@Override
	public void onStart(Abnormal abnormal, Creature effector, Creature effected)
	{
		giveDamage(effector, effected, true);
	}

	@Override
	public boolean onActionTime(Abnormal abnormal, Creature effector, Creature effected)
	{
		giveDamage(effector, effected, false);
		return true;
	}

	private void giveDamage(Creature effector, Creature effected, boolean first)
	{
		if(effected.isDead())
			return;

		double damage = (effector.getPAtk(effector) / 100 * getValue());
		damage = Math.min(damage, effected.getCurrentHp() - 1);
		if(getSkill().getAbsorbPart() > 0)
			effector.setCurrentHp(getSkill().getAbsorbPart() * Math.min(effected.getCurrentHp(), damage) + effector.getCurrentHp(), false);

		boolean awake = !effected.isNpc() && effected != effector; // TODO: Check this.
		boolean standUp = effected != effector; // TODO: Check this.
		boolean directHp = effector.isNpc() || effected == effector; // TODO: Check this.
		effected.reduceCurrentHp(damage, effector, getSkill(), awake, standUp, directHp, false, false, true, false);

	}
}