package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

public class ConditionPlayerMaxHp extends Condition
{
	private final int _hp;

	public ConditionPlayerMaxHp(int hp)
	{
		_hp = hp;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getMaxHp() <= _hp;
	}
}