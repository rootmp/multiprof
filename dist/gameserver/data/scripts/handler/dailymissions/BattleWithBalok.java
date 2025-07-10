package handler.dailymissions;

/**
 * @author nexvill
 */
public class BattleWithBalok extends DailyHunting
{
	private final int[] MONSTER_IDS =
	{
		22413, // Demonic Reaper
		22414, // Demon Warrior
		22415, // Demonic Knight
		22416, // Earth Scorpion
		22417 // Rattling Skeleton
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
