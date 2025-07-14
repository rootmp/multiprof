package l2s.gameserver.skills.effects.instant;

import l2s.gameserver.model.Creature;
import l2s.gameserver.stats.Stats;
import l2s.gameserver.templates.skill.EffectTemplate;

/**
 * @author nexvill
 **/
public class i_add_magic_lamp_points extends i_abstract_effect
{
	public i_add_magic_lamp_points(EffectTemplate template)
	{
		super(template);
	}

	@Override
	protected boolean checkCondition(Creature effector, Creature effected)
	{
		long resultValue = effected.getPlayer().getMagicLampPoints()
				+ (long) effected.getStat().calc(Stats.MAGIC_LAMP_CHARGING_RATE_MULTIPLIER, (long) getValue());

		if(resultValue > effected.getPlayer().MAX_MAGIC_LAMP_POINTS)
		{ return false; }

		return true;
	}

	@Override
	public void instantUse(Creature effector, Creature effected, boolean reflected)
	{
		long resultValue = effected.getPlayer().getMagicLampPoints()
				+ (long) effected.getStat().calc(Stats.MAGIC_LAMP_CHARGING_RATE_MULTIPLIER, (long) getValue());
		effected.getPlayer().setMagicLampPoints(resultValue);
	}
}
