package handler.dailymissions;

/**
 * @author nexvill
 */
public class TranscendentHunt extends DailyHunting
{
	private final int[] MONSTER_IDS =
	{
		22107, // Rotting Tree
		22108, // Giant Fungus
		22109, // Dire Wyrm
		22110, // Taik Orc Supply Officer
		22111, // Tortured Undead
		22112, // Soul of Ruins
		22113, // Vanor Silenos Chieftain
		22114, // Vanor
		22115, // Vanor Silenos Shaman
		22116, // Farcran
		22117, // Deprive
		22118, // Hatar Ratman Boss
		22122, // Transcendent Treasure Chest
		22123, // Transcendent Treasure Chest
		22124, // Transcendent Treasure Chest
		22125, // Transcendent Treasure Chest
		22147, // Cave Maiden
		22148, // Cave Keeper
		22149, // Headless Knight
		22150, // Transcendent Treasure Chest
		22275, // Sel Mahum Raider
		22276, // Sel Mahum Footman
		22277, // Sel Mahum Knight
		22278 // Transcendent Treasure Chest
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
