package handler.dailymissions;

/**
 * @author nexvill
 */
public class ExploringDreamland extends WeeklyHunting
{
	private final int[] MONSTER_IDS =
	{
		18678, // Gustav
		18679, // Gustav's Steward
		18680, // Gabrielle
		18681, // Gabrielle's Minion
		18682, // Ventus
		18683, // Rekario
		18684, // Tiat
		18685 // Baint
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
