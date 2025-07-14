package l2s.gameserver.skills.effects.instant;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.skill.EffectTemplate;

public class i_add_dp extends i_abstract_effect
{
	private final int _chance;

	public i_add_dp(EffectTemplate template)
	{
		super(template);
		_chance = getParams().getInteger("chance");
	}

	@Override
	protected boolean checkCondition(Creature effector, Creature effected)
	{
		return effector.isPlayer();
	}

	@Override
	public void instantUse(Creature effector, Creature effected, boolean reflected)
	{
		Player player = effector.getPlayer();
		if((Rnd.get(100) < _chance) && (player.getCurrentDp() < player.getMaxDp()))
			player.setCurrentDp(Math.min(player.getCurrentDp() + (int) getValue(), player.getMaxDp()));
	}
}
