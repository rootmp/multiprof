package handler.dailymissions;

/**
 * @author nexvill
 */
public class ExploringAntharasLair extends WeeklyHunting
{
	private final int[] MONSTER_IDS =
	{
		20405, // Knoriks
		20407, // Bloody Banshee
		20620, // Cave Beast
		20621, // Death Wave
		20622, // Maluk Soldier
		20623, // Plando
		20624, // Cave Howler
		20625, // Maluk Knight
		20626, // Maluk Berserker
		20627, // Maluk Lord
		20628, // Limal Karinness
		20629, // Karik
		20761, // Pytan
		21084, // Bloody Lady
		21085, // Bloody Sniper
		21086, // Drake Scout
		21087, // Drake Commander
		21089 // Drake Mage
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
