package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

public final class ConditionHasDp extends Condition
{
	private final int _count;

	public ConditionHasDp(int count)
	{
		_count = count;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getCurrentDp() >= _count;
	}
}
