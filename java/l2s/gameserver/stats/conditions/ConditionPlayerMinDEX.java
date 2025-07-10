package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMinDEX extends Condition
{
	private final int _dex;

	public ConditionPlayerMinDEX(int dex)
	{
		_dex = dex;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getDEX() >= _dex;
	}
}