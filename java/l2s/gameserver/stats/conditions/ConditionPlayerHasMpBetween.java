package l2s.gameserver.stats.conditions;

import l2s.gameserver.model.Creature;
import l2s.gameserver.stats.Env;

/**
 * @author Hl4p3x/JAVA-MAN
 */
public class ConditionPlayerHasMpBetween extends Condition
{
	private final int _min;
	private final int _max;
	private final boolean _percentage;

	public ConditionPlayerHasMpBetween(int min, int max, boolean percentage)
	{
		_min = min;
		_max = max;
		_percentage = percentage;
	}

	protected boolean testImpl(Env env)
	{
		Creature character = env.character;
		if(character == null)
		{ return false; }

		if(_percentage)
		{
			int hpPer = (int) character.getCurrentMpPercents();
			return (hpPer >= _min && hpPer <= _max);
		}
		if(_max == -1)
		{ return (character.getMaxMp() >= _min); }

		return (character.getMaxMp() >= _min && character.getMaxMp() <= _max);
	}
}