package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMinMEN extends Condition
{
	private final int _men;

	public ConditionPlayerMinMEN(int men)
	{
		_men = men;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getMEN() >= _men;
	}
}