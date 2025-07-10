package l2s.gameserver.stats.conditions;

import l2s.gameserver.model.Creature;
import l2s.gameserver.stats.Env;

/**
 * @author Sanyadc@gmail.com
 **/
public class ConditionTargetMaxDistance extends Condition
{
	private final int _distance;

	public ConditionTargetMaxDistance(int distance)
	{
		_distance = distance;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		Creature target = env.target;
		if (target == null)
			return false;

		return env.character.isInRange(target.getLoc(), _distance);
	}
}
