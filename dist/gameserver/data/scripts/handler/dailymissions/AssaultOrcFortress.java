package handler.dailymissions;

/**
 * @author nexvill
 */
public class AssaultOrcFortress extends DailyHunting
{
	private final int[] MONSTER_IDS =
	{
		22168, // Chiliarch
		22169, // Chiliarch
		22170, // Chiliarch
		22171, // Chiliarch
		22172, // Centurion
		22173, // Centurion
		22174, // Centurion
		22175, // Orc Soldier
		22176, // Orc Warrior
		22177, // Orc Prefect
		22180, // Orc Archer
		22181 // Orc Archer
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
