package l2s.gameserver.network.l2.s2c;

import l2s.gameserver.model.Player;

public class ExBasicActionList extends L2GameServerPacket
{
	private static final int[] BASIC_ACTIONS =
	{
		0, // (/sit, /stand)
		1, // (/walk, /run)
		2, // Attack the selected target(s). Ctrl-click to force attack. (/attack, /attackforce)
		3, // Request to trade with the selected player. (/trade)
		4, // Select the closest target to attack. (/targetnext)
		5, // Pick up nearby items. (/pickup)
		6, // Switch to the target of the selected player. (/assist)
		7, // Invite the selected player to your party. (/invite)
		8, // Leave the group. (/leave)
		9, // If you are the leader of the group, remove the selected player(s) from the group (/dismiss)
		10, // Set up a personal store to sell items.(/vendor)
		12, // Emote: Greet others. (/socialhello)
		15, // Your pet either follows you or stays where you are.
		16, // Attack target.
		17, // Abort the current action.
		18, // Pick up nearby items.
		19, // Removes Pet from inventory.
		20, // Use a special skill.
		21, // Your Minions either follow you or stay put.
		22, // Attack target.
		23, // Abort the current action.
		26, // Emotion: A bow, as a sign of respect. (/socialbow)
		27, // Use a special skill.
		28, // Set up a personal store to buy items. (/buy)
		32, // Switch between attack/move modes.
		34, // Emote: Show everyone your best dance. (/social_dance)
		36, // Poison Gas Attack.
		37, // Set up a personal workshop to create items using Dwarven recipes for reward. (/dwarvenmanufacture)
		38, // Mount/dismount switch when near a Pet, which can be saddled. (/mount, /dismount, /mountdismount)
		39, // Attack with exploding corpses.
		40, // Increases the target's score (/evaluate)
		41, // Attack castle gates, walls, or headquarters with a cannon shot.
		42, // Returns damage back to the enemy.
		43, // Attack an enemy by creating a seething whirlpool.
		44, // Attack the enemy with a powerful explosion.
		45, // Restores the summoner's MP.
		46, // Attack the enemy with a devastating storm.
		47, // Damages an enemy and heals a servant at the same time.
		48, // Attack an enemy with a cannon shot.
		49, // Attack in a fit of rage.
		50, // Selected party member becomes group leader.(/changepartyleader)
		51, // Create an item using a normal recipe for reward.(/generalmanufacture)
		52, // Unbinds a minion and frees it.
		53, // Move towards the target.
		54, // Move towards the target.
		55, // Switch to record and stop recording replays. (/start_videorecording, /end_videorecording, /startend_videorecording)
		56, // Invite the selected target to the team channel. (/channelinvite)
		63, // Starts a fun and simple mini-game that can be played at any time. (command: /minigame)
		64, // Opens a free teleport window that allows you to move freely between locations with teleports. (command: /teleportbookmark)
		65, // Reports suspicious behavior of an object whose actions allow assume the use of a bot program.
		67, // Ship control
		68, // Termination of control of the ship
		69, // Departure of the ship
		70, // Descent from the ship
		71, // Bow
		72, // High Five
		73, // Dance Together
		74, // Enable/Disable status data
		76, // Invite a friend
		78, // Using Sign 1
		79, // Using Sign 2
		80, // Using Sign 3
		81, // Using Sign 4
		82, // Auto-aim Znakom 1
		83, // Auto-aim Znakom 2
		84, // Auto-aim Znakom 3
		85, // Auto-aim Znakom 4
		87, // Propose
		88, // Provoke
		89, // Bragging
		93, // Next Target (ranged attack)
		96, // Strike Back
		97, // Combat Mode
		98, // Disapproval
		99, // Share Location
		
		100, // Party/Command Channel Request
		1000, // Siege Hammer
		1001, // Ultimate Accelerator
		1002, // Hostility
		1003, // Wild Stun
		1004, // Wild Defense
		1005, // Bright Flash
		1006, // Healing Light
		1007, // Queen's Blessing
		1008, // Gift of the Queen
		1009, // Queen healed
		1010, // Blessing of the Seraphim
		1011, // Gift of the Seraphim
		1012, // Healing of Seraphim
		1013, // Curse of the Shadow
		1014, // Mass Shadow Curse
		1015, // Shadow's Despair
		1016, // Cursed Impulse
		1017, // Cursed Strike
		1018, // Energy Absorption Curse
		1019, // Kat Skill 2
		1020, // Meow Skill 2
		1021, // Kai Skill 2
		1022, // Jupiter Skill 2
		1023, // Mirage Skill 2
		1024, // Bekar Skill 2
		1025, // Shadow Skill 1
		1026, // Shadow Skill 2
		1027, // Hekate's skill
		1028, // Resurrected Skill 1
		1029, // Resurrected Skill 2
		1030, // Vicious Skill 2
		1031, // Dissection
		1032, // Cutting Whirlwind
		1033, // Cat's Grip
		1034, // Whip
		1035, // Tidal Wave
		1036, // Corpse Explosion
		1037, // Accidental Death
		1038, // Power of Curse
		1039, // Cannon Fodder
		1040, // Big Boom
		1041, // Bite
		1042, // Sledgehammer
		1043, // Wolf Roar
		1044, // Awakening
		1045, // Wolf Howl
		1046, // Roar of the Riding Dragon
		1047, // Divine Beast Bite
		1048, // Divine Beast's Stunning Attack
		1049, // Divine Beast Fire Breath
		1050, // Divine Beast Roar
		1051, // Blessing of the Body
		1052, // Blessing of the Spirit
		1053, // Acceleration
		1054, // Insight
		1055, // Purity
		1056, // Inspiration
		1057, // Wild Magic
		1058, // Whispers of Death
		1059, // Focus
		1060, // Hover
		1061, // Death Strike
		1062, // Double Attack
		1063, // Whirlwind Attack
		1064, // Meteor shower
		1065, // Awake
		1066, // Thunderbolt
		1067, // Lightning
		1068, // Light Wave
		1069, // Flash
		1070, // Effect Control
		1071, // Slam
		1072, // Piercing Attack
		1073, // Furious Wind
		1074, // Spear Strike
		1075, // Battle Cry
		1076, // Mighty Crush
		1077, // Fireball
		1078, // Shock Wave
		1079, // Howl
		1080, // Phoenix Tide
		1081, // Purification of the Phoenix
		1082, // Burning Phoenix Feather
		1083, // Flaming Phoenix Beak
		1084, // Change Mode
		1085, // Victim of the Shadow
		1086, // Panther's Onslaught
		1087, // Panther's Dark Claw
		1088, // Panther's Deadly Claw
		1089, // Tail
		1090, // Rider's Bite
		1091, // Intimidate the Riding Dragon
		1092, // Riding Dragon Charge
		1093, // Magven's Strike
		1094, // Magven's Light Walk
		1095, // Magven's Slam
		1096, // Elite Magven Light Walk
		1097, // Return of Magven
		1098, // Magven's Group Return
		
		1099, // Attack
		1100, // Move
		1101, // Termination
		1102, // Summons canceled
		
		1103, // All Passive mode
		1104, // All Defending mode
		
		1106, // Bear Claw
		1107, // Bear Stomp
		1108, // Cougar Bite
		1109, // Cougar Leap
		1110, // Ripper's Touch
		1111, // Power of the Ripper
		1113, // Lion's Roar
		1114, // Lion's Claw
		1115, // Lion Throw
		1116, // Lionfire
		1117, // Flight of the Thunder Serpent
		1118, // Purification of the Thunder Serpent
		1120, // Shooting Thunder Serpent Feathers
		1121, // Sharp Claws of the Thunder Serpent
		1122, // Blessing of Life
		1123, // Siege Strike
		1124, // Cat Aggression
		1125, // Cat Stun
		1126, // Cat Bite
		1127, // Cat Attack Leap
		1128, // Cat's Touch
		1129, // Power of the Cat
		1130, // Unicorn Aggression
		1131, // Unicorn Stun
		1132, // Unicorn Bite
		1133, // Attack Leap Unicorn
		1134, // Touch of the Unicorn
		1135, // Power of the Unicorn
		1136, // Phantom Aggression
		1137, // Phantom Stun
		1138, // Phantom Bite
		1139, // Phantom Attack Jump
		1140, // Phantom's Touch
		1141, // Power of the Phantom
		1142, // Panther Roar
		1143, // Panther's Swift Throw
		
		// Hl4p3x
		1144, // Commando Cat - Commando Jumping Attack
		1145, // Commando Cat - Commando Double Slash		
		1146, // Witch Cat - Elemental Slam
		1147, // Witch Cat - Witch Cat Power		
		1148, // Unicorn Lancer - Lancer Rush
		1149, // Unicorn Lancer - Power Stamp
		1150, // Unicorn Cherub - Multiple Icicles
		1151, // Unicorn Cherub - Cherub Power
		1152, // Dark Crusader - Phantom Sword Attack
		1153, // Dark Crusader - Phantom Blow
		1154, // Banshee Queen - Phantom Spike
		1155, // Banshee Queen - Phantom Crash	
		1168, // Emperor's Blessing
		1169, // Outcry
		1170, // Guard's Blessing
		1171, // Trample 
		1172, // Lord's Blessing
		1173, // Specter's Dance
		
		5000, // Baby Rudolph - Reindeer Scratch
		5001, // Deseloph, Hyum, Rekang, Lilias, Lapham, Mafum - Rosy Seduction
		5002, // Deseloph, Hyum, Rekang, Lilias, Lapham, Mafum - Critical Seduction
		5003, // Hyum, Lapham, Hyum, Lapham - Thunder Bolt
		5004, // Hyum, Lapham, Hyum, Lapham - Flash
		5005, // Hyum, Lapham, Hyum, Lapham - Lightning Wave
		5006, // Deseloph, Hyum, Rekang, Lilias, Lapham, Mafum, Deseloph, Hyum, Rekang, Lilias, Lapham, Mafum - Buff Control
		5007, // Deseloph, Lilias, Deseloph, Lilias - Piercing Attack
		5008, // Deseloph, Lilias, Deseloph, Lilias - Spin Attack
		5009, // Deseloph, Lilias, Deseloph, Lilias - Smash
		5010, // Deseloph, Lilias, Deseloph, Lilias - Ignite
		5011, // Rekang, Mafum, Rekang, Mafum - Power Smash
		5012, // Rekang, Mafum, Rekang, Mafum - Energy Burst
		5013, // Rekang, Mafum, Rekang, Mafum - Shockwave
		5014, // Rekang, Mafum, Rekang, Mafum - Ignite
		5015, // Deseloph, Hyum, Rekang, Lilias, Lapham, Mafum, Deseloph, Hyum, Rekang, Lilias, Lapham, Mafum - Switch Stance
		5016, // Cat Ranger buff
	};

	private final int[] _actions;

	public ExBasicActionList(Player activeChar)
	{
		if (activeChar.isTransformed() && activeChar.getTransform().getActions().length > 0)
		{
			_actions = activeChar.getTransform().getActions();
		}
		else
		{
			_actions = BASIC_ACTIONS;
		}
	}

	@Override
	protected void writeImpl()
	{
		// Sync with Meow
		writeD(_actions.length);
		for (int _actionId : _actions)
		{
			writeD(_actionId);
		}
	}
}