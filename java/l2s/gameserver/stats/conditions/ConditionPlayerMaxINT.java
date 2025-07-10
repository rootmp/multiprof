package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMaxINT extends Condition
{
	private final int _int;

	public ConditionPlayerMaxINT(int intelligence)
	{
		_int = intelligence;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getINT() <= _int;
	}
}