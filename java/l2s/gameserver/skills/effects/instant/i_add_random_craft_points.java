package l2s.gameserver.skills.effects.instant;

import l2s.gameserver.model.Creature;
import l2s.gameserver.templates.skill.EffectTemplate;

/**
 * @author nexvill
 **/
public class i_add_random_craft_points extends i_abstract_effect
{
	public i_add_random_craft_points(EffectTemplate template)
	{
		super(template);
	}

	@Override
	protected boolean checkCondition(Creature effector, Creature effected)
	{
		int resultValue = effected.getPlayer().getCraftGaugePoints() + (int) getValue();

		if(resultValue > effected.getPlayer().MAX_RANDOM_CRAFT_POINTS)
		{ return false; }

		return true;
	}

	@Override
	public void instantUse(Creature effector, Creature effected, boolean reflected)
	{
		int resultValue = effected.getPlayer().getCraftGaugePoints() + (int) getValue();
		effected.getPlayer().setCraftGaugePoints(resultValue, null);
	}
}
