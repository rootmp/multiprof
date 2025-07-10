package handler.dailymissions;

/**
 * @author nexvill
 */
public class ExploringDreamlandSecretPlaces extends WeeklyHunting
{
	private final int[] MONSTER_IDS =
	{
		18686, // Feline King
		18687 // Feline Queen
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
