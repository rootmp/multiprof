package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 **/
public class ConditionPlayerHasSayhasGrace extends Condition
{
	private final boolean _value;

	public ConditionPlayerHasSayhasGrace(boolean value)
	{
		_value = value;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		if(!env.character.isPlayer())
			return !_value;

		return (env.character.getPlayer().getSayhasGrace() > 0) == _value;
	}
}