package l2s.gameserver.skills.skillclasses;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.templates.StatsSet;

/**
 * @author nexvill
 */
public class SayhasGraceHeal extends Skill
{
	public SayhasGraceHeal(StatsSet set)
	{
		super(set);
	}

	@Override
	protected void useSkill(Creature activeChar, Creature target, boolean reflected)
	{
		if(!target.isPlayer())
			return;

		final Player player = target.getPlayer();
		if((player.getSayhasGrace() + getPower()) <= player.MAX_SAYHAS_GRACE_POINTS)
		{
			player.setSayhasGrace((int) (player.getSayhasGrace() + getPower()), true, true);
		}
		else
		{
			return;
		}
	}
}