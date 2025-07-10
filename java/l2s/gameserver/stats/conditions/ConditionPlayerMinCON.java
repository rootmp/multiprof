package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMinCON extends Condition
{
	private final int _con;

	public ConditionPlayerMinCON(int con)
	{
		_con = con;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getCON() >= _con;
	}
}