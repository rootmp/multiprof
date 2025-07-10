package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMinWIT extends Condition
{
	private final int _wit;

	public ConditionPlayerMinWIT(int wit)
	{
		_wit = wit;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getWIT() >= _wit;
	}
}