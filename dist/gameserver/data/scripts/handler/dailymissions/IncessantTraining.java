package handler.dailymissions;

/**
 * @author nexvill
 */
public class IncessantTraining extends DailyHunting
{
	private final int[] MONSTER_IDS =
	{
		25952, // Boojudu
		25953, // Petron
		25954, // Kerion
		25955, // Furious Tukhak
		25961, // Rilva
		25962, // Ryun
		25963 // Chel
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
