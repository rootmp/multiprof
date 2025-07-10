package handler.dailymissions;

/**
 * @author nexvill
 */
public class HuntAlligatorIsland extends DailyHunting
{
	private final int[] MONSTER_IDS =
	{
		22192, // Crokian Warrior
		22197 // Swamp Tribe
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
