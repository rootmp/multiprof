package services;

import java.util.HashSet;
import java.util.Set;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;

public class SupportMagic
{
	private final static int[][] _mageBuff = new int[][]
	{
		// minlevel maxlevel skill skilllevel
		{
			1,
			75,
			4322,
			1
		}, // windwalk
		{
			1,
			75,
			4323,
			1
		}, // shield
		{
			1,
			75,
			5637,
			1
		}, // Magic Barrier 1
		{
			1,
			75,
			4328,
			1
		}, // blessthesoul
		{
			1,
			75,
			4329,
			1
		}, // acumen
		{
			1,
			75,
			4330,
			1
		}, // concentration
		{
			1,
			75,
			4331,
			1
		}, // empower
		{
			16,
			34,
			4338,
			1
		}, // life cubic
	};

	private final static int[][] _warrBuff = new int[][]
	{
		// minlevel maxlevel skill
		{
			1,
			75,
			4322,
			1
		}, // windwalk
		{
			1,
			75,
			4323,
			1
		}, // shield
		{
			1,
			75,
			5637,
			1
		}, // Magic Barrier 1
		{
			1,
			75,
			4324,
			1
		}, // btb
		{
			1,
			75,
			4325,
			1
		}, // vampirerage
		{
			1,
			75,
			4326,
			1
		}, // regeneration
		{
			1,
			39,
			4327,
			1
		}, // haste 1
		{
			40,
			75,
			5632,
			1
		}, // haste 2
		{
			16,
			34,
			4338,
			1
		}, // life cubic
	};

	private final static int[][] _summonBuff = new int[][]
	{
		// minlevel maxlevel skill
		{
			1,
			75,
			4322,
			1
		}, // windwalk
		{
			1,
			75,
			4323,
			1
		}, // shield
		{
			1,
			75,
			5637,
			1
		}, // Magic Barrier 1
		{
			1,
			75,
			4324,
			1
		}, // btb
		{
			1,
			75,
			4325,
			1
		}, // vampirerage
		{
			1,
			75,
			4326,
			1
		}, // regeneration
		{
			1,
			75,
			4328,
			1
		}, // blessthesoul
		{
			1,
			75,
			4329,
			1
		}, // acumen
		{
			1,
			75,
			4330,
			1
		}, // concentration
		{
			1,
			75,
			4331,
			1
		}, // empower
		{
			1,
			39,
			4327,
			1
		}, // haste 1
		{
			40,
			75,
			5632,
			1
		}, // haste 2
	};

	public static void doSupportMagic(NpcInstance npc, Player player, boolean servitor)
	{
		int lvl = player.getLevel();

		Set<Creature> targets = new HashSet<Creature>();

		if (servitor)
		{
			for (Servitor s : player.getServitors())
			{
				if (!s.isSummon())
					continue;

				targets.add(s);
				for (int[] buff : _summonBuff)
				{
					if (lvl >= buff[0] && lvl <= buff[1])
					{
						npc.broadcastPacket(new MagicSkillUse(npc, s, buff[2], buff[3], 0, 0));
						npc.callSkill(s, SkillEntry.makeSkillEntry(SkillEntryType.NONE, buff[2], buff[3]), targets, true, false);
					}
				}
				targets.clear();
			}
		}
		else
		{
			targets.add(player);

			if (!player.isMageClass() || player.getTemplate().getRace() == Race.ORC)
			{
				for (int[] buff : _warrBuff)
				{
					if (lvl >= buff[0] && lvl <= buff[1])
					{
						npc.broadcastPacket(new MagicSkillUse(npc, player, buff[2], buff[3], 0, 0));
						npc.callSkill(player, SkillEntry.makeSkillEntry(SkillEntryType.NONE, buff[2], buff[3]), targets, true, false);
					}
				}
			}
			else
			{
				for (int[] buff : _mageBuff)
				{
					if (lvl >= buff[0] && lvl <= buff[1])
					{
						npc.broadcastPacket(new MagicSkillUse(npc, player, buff[2], buff[3], 0, 0));
						npc.callSkill(player, SkillEntry.makeSkillEntry(SkillEntryType.NONE, buff[2], buff[3]), targets, true, false);
					}
				}
			}
		}
	}
}
