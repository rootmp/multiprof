package l2s.gameserver.skills.effects.instant;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.skill.EffectTemplate;

/**
 * @author nexvill
 */
public final class i_refresh_instance_group extends i_abstract_effect
{
	public i_refresh_instance_group(EffectTemplate template)
	{
		super(template);
	}

	@Override
	protected boolean checkCondition(Creature effector, Creature effected)
	{
		return effected.isPlayer();
	}

	@Override
	public void instantUse(Creature effector, Creature effected, boolean reflected)
	{
		Player player = effected.getPlayer();
		if(player != null)
		{
			int groupId = (int) getValue();
			if(groupId == -1)
				player.removeAllInstanceReuses();
			else
				player.removeInstanceReusesByGroupId(groupId);
		}
	}
}