package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMinCp extends Condition
{
	private final int _cp;

	public ConditionPlayerMinCp(int cp)
	{
		_cp = cp;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getMaxCp() >= _cp;
	}
}