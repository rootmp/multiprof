package l2s.gameserver.skills.effects.instant;

import l2s.gameserver.model.Creature;
import l2s.gameserver.stats.Stats;
import l2s.gameserver.templates.skill.EffectTemplate;

/**
 * @author nexvill
 **/
public class i_add_sayha_grace_points extends i_abstract_effect
{
	public i_add_sayha_grace_points(EffectTemplate template)
	{
		super(template);
	}

	@Override
	protected boolean checkCondition(Creature effector, Creature effected)
	{
		int resultValue = effected.getPlayer().getSayhasGrace() + (int) effected.getStat().calc(Stats.SAYHAS_GRACE_CHARGING_RATE_MULTIPLIER, (int) getValue());
		if (resultValue > effected.getPlayer().MAX_SAYHAS_GRACE_POINTS)
		{
			return false;
		}

		return true;
	}

	@Override
	public void instantUse(Creature effector, Creature effected, boolean reflected)
	{
		int resultValue = effected.getPlayer().getSayhasGrace() + (int) effected.getStat().calc(Stats.SAYHAS_GRACE_CHARGING_RATE_MULTIPLIER, (int) getValue());
		effected.getPlayer().setSayhasGrace(resultValue);
		effected.getPlayer().updateUserBonus();
	}
}
