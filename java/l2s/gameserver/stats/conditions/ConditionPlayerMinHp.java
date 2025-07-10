package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

public class ConditionPlayerMinHp extends Condition
{
	private final int _hp;

	public ConditionPlayerMinHp(int hp)
	{
		_hp = hp;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getMaxHp() >= _hp;
	}
}