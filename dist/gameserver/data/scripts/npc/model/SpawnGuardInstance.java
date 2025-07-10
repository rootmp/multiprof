package npc.model;

import org.apache.commons.lang3.ArrayUtils;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author nexvill
 */
public class SpawnGuardInstance extends MonsterInstance
{
	// Npcs
	private static final int WYRM = 21657;
	private static final int[] SEA_OF_SPORES_MOBS =
	{
		20555, // Giant Fungus
		20556, // Giant Monster Eye
		20557, // Dire Wyrm
		20558, // Rotting Tree
		20559, // Rotting Golem
		20560, // Trisalim
		20561, // Trisalim Tarantula
		20562 // Spore Zombie
	};
	private static final int GRAVE_WARDEN = 22128;
	private static final int[] CEMETERY_MOBS =
	{
		20666, // Taik Orc Watchman
		20668, // Grave Guard
		20669, // Taik Orc Supply Officer
		20675, // Tairim
		20678, // Tortured Undead
		20996, // Spiteful Ghost of Ruins
		20997, // Soldier of Grief
		20998, // Cruel Punisher
		20999, // Roving Soul
		21000 // Soul of Ruins
	};
	private static final int GUARD_BUTCHER = 22101;
	private static final int[] FIELDS_OF_MASSACRE_MOBS =
	{
		20674, // Doom Knight
		21001, // Archer of Destruction
		21002, // Doom Scout
		21003, // Graveyard Lich
		21004, // Dismal Pole
		21005, // Graveyard Predator
		21006, // Doom Servant
		21007, // Doom Guard
		21008, // Doom Archer
		21009, // Doom Trooper
		21010 // Doom Warrior
	};
	private static final int GUARD_OF_HONOR = 22102;
	private static final int[] PLAINS_OF_GLORY_MOBS =
	{
		20681, // Vanor Silenos
		20682, // Vanor Silenos Soldier
		20683, // Vanor Silenos Scout
		20684, // Vanor Silenos Warrior
		20685, // Vanor Silenos Shaman
		20686, // Vanor Silenos Chieftain
		24014 // Vanor
	};
	private static final int FIERCE_GUARD = 22103;
	private static final int[] WAR_TORN_PLAINS_MOBS =
	{
		20659, // Graveyard Wanderer
		20660, // Archer of Greed
		20661, // Hatar Ratman Thief
		20662, // Hatar Ratman Boss
		20663, // Hatar Hanishee
		20664, // Deprive
		20665, // Taik Orc Elder
		20667 // Farcran
	};
	private static final int ANCIENT_GUARDIAN = 22106;
	private static final int[] SILENT_VALLEY_MOBS =
	{
		20967, // Creature of the Past
		20968, // Forgotten Face
		20969, // Giant's Shadow
		20971, // Warrior of Ancient Times
		20972, // Shaman of Ancient Times
		20973 // Forgotten Ancient Creature
	};
	private static final int TAG = 22297; // FIXME Если есть пет(вызван), то спавнятся 22303
	private static final int[] BEE_HIVE_LOW_MOBS =
	{
		22293, // Bear
		22294, // Hog
		22295, // Kima
		22296 // Honeybee
	};
	private static final int RUDE_TAG = 22302; // FIXME Если есть пет(вызван), то спавнятся 22304
	private static final int[] BEE_HIVE_HIGH_MOBS =
	{
		22298, // Mighty Bear
		22299, // Stubborn Hog
		22300, // Strong Kima
		22301 // Big Honeybee
	};
	private static final int GUARD_OF_THE_PLAIN = 22182;
	private static final int[] PLAINS_OF_THE_LIZARDMEN_MOBS =
	{
		22151, // Tanta Lizardman
		22152, // Tanta Lizardman Warrior
		22153, // Tanta Lizardman Berserker
		22154, // Tanta Lizardman Archer
		22155 // Tanta Lizardman Summoner
	};
	private static final int SEL_MAHUM_THIEF = 22241;
	private static final int[] SEL_MAHUM_BASE_MOBS =
	{
		22237, // Sel Mahum Sniper
		22238, // Sel Mahum Raider
		22239, // Sel Mahum Berserker
		22240, // Sel Mahum Mage
		22242, // Sel Mahum Wizard
		22243, // Sel Mahum Knight
		22244, // Sel Mahum Footman
		22245 // Sel Mahum Bowman
	};
	private static final int PYTAN = 20761;
	private static final int PYTAN_KNIGHT = 20762;
	private static final int KNORIKS = 20405;
	private static final int KNORIKS_KNIGHT = 22212;

	private boolean _spawned = false;

	public SpawnGuardInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	protected void onReduceCurrentHp(double damage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean isDot)
	{
		super.onReduceCurrentHp(damage, attacker, skill, awake, standUp, directHp, isDot);
		if (!attacker.isPlayer())
		{
			return;
		}
		int chance = Rnd.get(100);
		NpcInstance npc = null;
		if (!_spawned && (chance < 1) && ArrayUtils.contains(SEA_OF_SPORES_MOBS, getNpcId()))
		{
			npc = NpcUtils.createNpc(WYRM);
			spawnGuard(npc, attacker);
		}
		else if (!_spawned && (chance < 5) && ArrayUtils.contains(CEMETERY_MOBS, getNpcId()))
		{
			npc = NpcUtils.createNpc(GRAVE_WARDEN);
			spawnGuard(npc, attacker);
		}
		else if (!_spawned && (chance < 5) && ArrayUtils.contains(FIELDS_OF_MASSACRE_MOBS, getNpcId()))
		{
			npc = NpcUtils.createNpc(GUARD_BUTCHER);
			spawnGuard(npc, attacker);
		}
		else if (!_spawned && (chance < 5) && ArrayUtils.contains(PLAINS_OF_GLORY_MOBS, getNpcId()))
		{
			npc = NpcUtils.createNpc(GUARD_OF_HONOR);
			spawnGuard(npc, attacker);
		}
		else if (!_spawned && (chance < 5) && ArrayUtils.contains(WAR_TORN_PLAINS_MOBS, getNpcId()))
		{
			npc = NpcUtils.createNpc(FIERCE_GUARD);
			spawnGuard(npc, attacker);
		}
		else if (!_spawned && (chance < 5) && ArrayUtils.contains(SILENT_VALLEY_MOBS, getNpcId()))
		{
			npc = NpcUtils.createNpc(ANCIENT_GUARDIAN);
			spawnGuard(npc, attacker);
		}
		else if (!_spawned && (chance < 5) && ArrayUtils.contains(BEE_HIVE_LOW_MOBS, getNpcId()))
		{
			npc = NpcUtils.createNpc(TAG);
			spawnGuard(npc, attacker);
		}
		else if (!_spawned && (chance < 5) && ArrayUtils.contains(BEE_HIVE_HIGH_MOBS, getNpcId()))
		{
			npc = NpcUtils.createNpc(RUDE_TAG);
			spawnGuard(npc, attacker);
		}
		else if (!_spawned && (chance < 5) && ArrayUtils.contains(PLAINS_OF_THE_LIZARDMEN_MOBS, getNpcId()))
		{
			npc = NpcUtils.createNpc(GUARD_OF_THE_PLAIN);
			spawnGuard(npc, attacker);
		}
		else if (!_spawned && (chance < 5) && ArrayUtils.contains(SEL_MAHUM_BASE_MOBS, getNpcId()))
		{
			npc = NpcUtils.createNpc(SEL_MAHUM_THIEF);
			spawnGuard(npc, attacker);
		}
		else if (!_spawned && (getCurrentHp() < 50) && (getNpcId() == PYTAN))
		{
			for (int i = 0; i < 3; i++)
			{
				npc = NpcUtils.createNpc(PYTAN_KNIGHT);
				spawnGuard(npc, attacker);
			}
		}
		else if (!_spawned && (getCurrentHp() < 50) && (getNpcId() == KNORIKS))
		{
			for (int i = 0; i < 3; i++)
			{
				npc = NpcUtils.createNpc(KNORIKS_KNIGHT);
				spawnGuard(npc, attacker);
			}
		}
	}

	private void spawnGuard(NpcInstance npc, Creature attacker)
	{
		if (npc != null)
		{
			npc.setAI(new Fighter(npc));
			NpcUtils.spawnNpc(npc, getX(), getY(), getZ());
			npc.setTarget(attacker);
			npc.getAI().Attack(attacker, true, false);
			npc.getAggroList().addDamageHate(attacker, 1, 1000000);
			_spawned = true;
		}
	}
}