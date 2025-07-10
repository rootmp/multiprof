package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

public class ConditionPlayerMinPDef extends Condition
{
	private final int _pDef;

	public ConditionPlayerMinPDef(int pDef)
	{
		_pDef = pDef;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getPDef(env.character) >= _pDef;
	}
}