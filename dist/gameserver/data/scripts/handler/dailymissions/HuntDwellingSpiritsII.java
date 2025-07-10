package handler.dailymissions;

/**
 * @author nexvill
 */
public class HuntDwellingSpiritsII extends WeeklyHunting
{
	private final int[] MONSTER_IDS =
	{
		29120, // Ignis
		29121, // Nebula
		29122, // Procella
		29123 // Petram
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
