package handler.dailymissions;

/**
 * @author nexvill
 */
public class ExploringGoddard extends WeeklyHunting
{
	private final int[] MONSTER_IDS =
	{
		22426, // Tailed Warrior
		22427, // Tailed Hunter
		22428, // Tailed Berserker
		22429, // Tailed Wizard
		22431, // Morgos' Hot Springs Hunter
		22432, // Morgos' Supplier
		22433, // Gorde Grendel
		22434, // Gorde Antelope
		22435, // Gorde Bandersnatch
		22436, // Gorde Buffalo
		22437, // Eye of Restrainer
		22439, // Gorde Warrior
		22440, // Gorde Soldier
		22441, // Gorde Hunter
		22442, // Gorde Wizard
		22443, // Eye of Watchman
		22445, // Morgos' Canyon Hunter
		22446, // Morgos' Infantryman
		22447, // Morgos' Footman
		22448, // Morgos' Shield Breaker
		22449, // Morgos' Thrower
		22450, // Morgos' Shaman
		22451, // Morgos' Officer
		22452, // Infantry Commander
		22453, // Morgos' Guard
		22454, // Morgos' Raider
		22455, // Morgos' Sniper
		22456, // Morgos' Wizard
		22457, // Morgos' Commander
		22458, // Morgos
		22459, // Morgos' Recruit
		22460, // Morgos' Sergeant
		22461, // Morgos' Training Mentor
		22462, // Morgos' Sentry
		22463, // Morgos' Elite Guard
		22464, // Morgos' Commando
		22465, // Bloodhound
		22466, // Elite Soldier
		22467, // Elite Soldier
		22468, // Elite Soldier
		22469, // Elite Soldier
		22470, // Elite Soldier
		22471, // Elite Soldier
		22472, // Elite Soldier
		22473, // Elite Soldier
		22474, // Xilenos Soldier
		22475, // Xilenos Warlord
		22476, // Xilenos Archer
		22477, // Xilenos Wizard
		22478, // Xilenos Spy
		22479, // Xilenos Warrior
		22480, // Xilenos Footman
		22481, // Xilenos Shooter
		22482, // Xilenos Pikeman
		22483, // Xilenos Elder
		22484, // Xilenos Summoner
		22485, // Xilenos Scout
		22486, // Jakal
		22487, // Morgos' Messenger
		22488, // Jakal's Guard
		22489 // Varka
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
