package l2s.gameserver.stats.conditions;

import l2s.gameserver.model.Creature;
import l2s.gameserver.stats.Env;

/**
 * @author Hl4p3x/JAVA-MAN
 */
public class ConditionPlayerHasDpBetween extends Condition
{
	private final int _min;
	private final int _max;

	public ConditionPlayerHasDpBetween(int min, int max)
	{
		_min = min;
		_max = max;
	}

	protected boolean testImpl(Env env)
	{
		Creature character = env.character;
		if (character == null)
		{
			return false;
		}

		if (_max == -1)
		{
			return (character.getMaxDp() >= _min);
		}

		return (character.getMaxDp() >= _min && character.getMaxDp() <= _max);
	}
}
