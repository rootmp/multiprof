package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMaxCp extends Condition
{
	private final int _cp;

	public ConditionPlayerMaxCp(int cp)
	{
		_cp = cp;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getMaxCp() <= _cp;
	}
}