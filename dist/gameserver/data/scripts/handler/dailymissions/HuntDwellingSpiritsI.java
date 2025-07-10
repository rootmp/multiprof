package handler.dailymissions;

/**
 * @author nexvill
 */
public class HuntDwellingSpiritsI extends WeeklyHunting
{
	private final int[] MONSTER_IDS =
	{
		22271, // Corrupted Fairy
		22272, // Corrupted Fairy
		22273, // Ruip
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
