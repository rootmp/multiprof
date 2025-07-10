package handler.dailymissions;

/**
 * @author nexvill
 */
public class DifficultAssaultOrcFortress extends DailyHunting
{
	private final int[] MONSTER_IDS =
	{
		22156, // Greg
		22157, // Gilatu
		22158, // Ogre
		22159, // Jeras
		22160, // Tebot
		22161, // Tegaffe
		22162, // Thesakar
		22163, // Theor
		22164, // Laokan
		22165, // Taklacan
		22166, // Vulcan
		22167 // Zakan
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
