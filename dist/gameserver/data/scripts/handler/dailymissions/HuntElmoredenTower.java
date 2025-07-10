package handler.dailymissions;

/**
 * @author nexvill
 */
public class HuntElmoredenTower extends DailyHunting
{
	private final int[] MONSTER_IDS =
	{
		18639,
		18640,
		18641,
		18642,
		18643,
		18644,
		18645,
		18646,
		18647,
		18648,
		18656,
		18657,
		18658,
		18660,
		18661,
		18663,
		18665,
		18666
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
