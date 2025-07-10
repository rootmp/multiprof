package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMaximumSayhasGrace extends Condition
{
	private final int _sayhas_grace;

	public ConditionPlayerMaximumSayhasGrace(int points)
	{
		_sayhas_grace = points;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getPlayer().getSayhasGrace() <= _sayhas_grace;
	}
}