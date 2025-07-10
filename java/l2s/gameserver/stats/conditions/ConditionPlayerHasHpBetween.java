package l2s.gameserver.stats.conditions;

import l2s.gameserver.model.Creature;
import l2s.gameserver.stats.Env;

/**
 * @author Hl4p3x/JAVA-MAN
 */
public class ConditionPlayerHasHpBetween extends Condition
{
	private final int _min;
	private final int _max;
	private final boolean _percentage;

	public ConditionPlayerHasHpBetween(int min, int max, boolean percentage)
	{
		_min = min;
		_max = max;
		_percentage = percentage;
	}

	protected boolean testImpl(Env env)
	{
		Creature character = env.character;
		if (character == null)
		{
			return false;
		}

		if (_percentage)
		{
			int hpPer = (int) character.getCurrentHpPercents();
			return (hpPer >= _min && hpPer <= _max);
		}
		if (_max == -1)
		{
			return (character.getMaxHp() >= _min);
		}
		
		return (character.getMaxHp() >= _min && character.getMaxHp() <= _max);
	}
}
