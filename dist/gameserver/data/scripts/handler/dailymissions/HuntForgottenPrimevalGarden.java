package handler.dailymissions;

/**
 * @author nexvill
 */
public class HuntForgottenPrimevalGarden extends WeeklyHunting
{
	private final int[] MONSTER_IDS =
	{
		22269, // Kalix
		22270 // Kastia's Elite Captain
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
