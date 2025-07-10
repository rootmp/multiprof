package npc.model;

import l2s.commons.collections.MultiValueSet;
import l2s.commons.util.Rnd;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author nexvill
 */
public class TironInstance extends MonsterInstance
{
	// Npcs
	private static final int TELEPORTER = 34232;

	// Bosses
	private static final int NORMAL_GLAKIAS = 29136;
	private static final int HARD_GLAKIAS = 29137;
	private static final int DREADFUL_NORMAL_GLAKIAS = 29138;
	private static final int DREADFUL_HARD_GLAKIAS = 29139;

	// Vars
	private static final String RAID_VAR = "frost_lord_castle_first_rb";

	public TironInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);

		NpcUtils.spawnSingle(TELEPORTER, 149224, 143816, -12232, 25769, ReflectionManager.FROST_LORD_CASTLE, 7_200_000); // Charged
																															// Crystal
		if (ServerVariables.getString(RAID_VAR, "reggiesys") == "reggiesys")
		{
			if (Rnd.get(100) < 50)
			{
				NpcUtils.spawnSingle(NORMAL_GLAKIAS, 114728, -114792, -11200, 16165, ReflectionManager.FROST_LORD_CASTLE, 7_200_000);
			}
			else
			{
				NpcUtils.spawnSingle(HARD_GLAKIAS, 114728, -114792, -11200, 16165, ReflectionManager.FROST_LORD_CASTLE, 7_200_000);
			}
		}
		else
		{
			if (Rnd.get(100) < 50)
			{
				NpcUtils.spawnSingle(DREADFUL_NORMAL_GLAKIAS, 114728, -114792, -11200, 16165, ReflectionManager.FROST_LORD_CASTLE, 7_200_000);
			}
			else
			{
				NpcUtils.spawnSingle(DREADFUL_HARD_GLAKIAS, 114728, -114792, -11200, 16165, ReflectionManager.FROST_LORD_CASTLE, 7_200_000);
			}
		}

		ServerVariables.unset(RAID_VAR);
	}
}