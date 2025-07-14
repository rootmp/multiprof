package l2s.gameserver.skills.effects;

import l2s.commons.lang.reference.HardReference;
import l2s.gameserver.handler.effects.EffectHandler;
import l2s.gameserver.listener.actor.OnAttackListener;
import l2s.gameserver.listener.actor.OnMagicUseListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.templates.skill.EffectTemplate;

/**
 * @author nexvill
 */
public class EffectBpReduceOnHit extends EffectHandler
{
	private class AttackListener implements OnAttackListener, OnMagicUseListener
	{
		private final HardReference<? extends Creature> _effectorRef;
		private final HardReference<? extends Creature> _effectedRef;

		public AttackListener(Abnormal abnormal, Creature effector, Creature effected)
		{
			_effectorRef = effector.getRef();
			_effectedRef = effected.getRef();
		}

		@Override
		public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt)
		{
			if(!skill.isDebuff())
				return;

			EffectBpReduceOnHit.this.onAttack(_effectorRef.get(), _effectedRef.get());
		}

		@Override
		public void onAttack(Creature actor, Creature target)
		{
			EffectBpReduceOnHit.this.onAttack(_effectorRef.get(), _effectedRef.get());
		}
	}

	private final int _reduceAmount;
	private AttackListener _listener;

	public EffectBpReduceOnHit(EffectTemplate template)
	{
		super(template);
		_reduceAmount = getParams().getInteger("reduceAmount", 0);
	}

	@Override
	protected boolean checkCondition(Abnormal abnormal, Creature effector, Creature effected)
	{
		if(effected.isDead() || effected.isRaid())
			return false;
		return true;
	}

	private void onAttack(Creature effector, Creature effected)
	{
		if(effector.getCurrentBp() < _reduceAmount)
		{
			effector.getAbnormalList().stop(getSkill(), false);
		}
		effector.reduceCurrentBp(effector.getCurrentBp() - _reduceAmount, effector);
		if(effector.getCurrentBp() <= 0)
		{
			effector.getAbnormalList().stop(getSkill(), false);
		}
	}

	@Override
	public void onStart(Abnormal abnormal, Creature effector, Creature effected)
	{
		_listener = new AttackListener(abnormal, effector, effected);
		effected.addListener(_listener);
	}

	@Override
	public void onExit(Abnormal abnormal, Creature effector, Creature effected)
	{
		effected.removeListener(_listener);
		_listener = null;
	}
}