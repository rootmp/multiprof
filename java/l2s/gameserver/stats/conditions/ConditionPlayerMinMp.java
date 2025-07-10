package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMinMp extends Condition
{
	private final int _mp;

	public ConditionPlayerMinMp(int mp)
	{
		_mp = mp;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getMaxMp() >= _mp;
	}
}