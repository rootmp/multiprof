package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMaxDEX extends Condition
{
	private final int _dex;

	public ConditionPlayerMaxDEX(int dex)
	{
		_dex = dex;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getDEX() <= _dex;
	}
}