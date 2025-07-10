package handler.dailymissions;

/**
 * @author nexvill
 */
public class ExploringFrostLordCastle extends WeeklyHunting
{
	private final int[] MONSTER_IDS =
	{
		22345, // Frosty Knight
		22346, // Frosty Mage
		22347, // Frosty Sniper
		22348, // Emperor's Knight
		22349, // Emperor's Mage
		22350, // Emperor's Sniper
		22351, // Frosty Archer
		22352 // Emperor's Archer
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
