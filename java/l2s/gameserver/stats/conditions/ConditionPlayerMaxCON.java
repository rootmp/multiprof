package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMaxCON extends Condition
{
	private final int _con;

	public ConditionPlayerMaxCON(int con)
	{
		_con = con;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getCON() <= _con;
	}
}