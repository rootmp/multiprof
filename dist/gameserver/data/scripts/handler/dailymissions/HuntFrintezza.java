package handler.dailymissions;

/**
 * @author nexvill
 */
public class HuntFrintezza extends WeeklyHunting
{
	private final int[] MONSTER_IDS =
	{
		29047 // Scarlet van Halisha
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
