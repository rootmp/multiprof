package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMaxSTR extends Condition
{
	private final int _str;

	public ConditionPlayerMaxSTR(int str)
	{
		_str = str;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getSTR() <= _str;
	}
}