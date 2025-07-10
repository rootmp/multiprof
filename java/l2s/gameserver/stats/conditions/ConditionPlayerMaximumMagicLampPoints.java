package l2s.gameserver.stats.conditions;

import l2s.gameserver.stats.Env;

/**
 * @author nexvill
 */
public class ConditionPlayerMaximumMagicLampPoints extends Condition
{
	private final long _magicLampPoints;

	public ConditionPlayerMaximumMagicLampPoints(long points)
	{
		_magicLampPoints = points;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getPlayer().getMagicLampPoints() <= _magicLampPoints;
	}
}