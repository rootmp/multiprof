package l2s.gameserver.stats.conditions;

import l2s.commons.util.Rnd;
import l2s.gameserver.stats.Env;

/**
 * @author Hl4p3x/JAVA-MAN
 */
public final class ConditionHasChance extends Condition
{
	private final int _chance;

	public ConditionHasChance(int chance)
	{
		_chance = chance;
	}

	protected boolean testImpl(Env env)
	{
		boolean hasChance = Rnd.chance(_chance);
		return hasChance;
	}
}