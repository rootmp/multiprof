package quests;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10294_SporeInfestedPlace extends Quest
{
	// Npcs
	private static final int MAXIMILIAN = 30120;
	private static final int ORVEN = 30857;

	// Items
	private static final int SOE_SEA_OF_SPORES = 95590;
	private static final int SOE_ORVEN = 91768;
	private static final int SPIRIT_ORE = 3031;
	private static final int SOULSHOT_TICKET = 90907;
	private static final int SAYHAS_GUST = 91776;
	private static final int HP_POTION = 91912;

	// Location
	private static final Location ORVEN_LOC = new Location(147452, 22638, -1984);

	// Monsters
	private static final int[] MOBS =
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

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10294_SporeInfestedPlace()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(MAXIMILIAN);
		addTalkId(MAXIMILIAN, ORVEN);
		addKillNpcWithLog(2, NpcString.KILL_MONSTERS_IN_THE_SEA_OF_SPORES.getId(), A_LIST, 200, MOBS);
		addLevelCheck("bad_level.htm", 40, 45);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "maximilian_q10294_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "teleport":
			{
				player.teleToLocation(ORVEN_LOC);
				return null;
			}
			case "orven_q10294_03.htm":
			{
				st.setCond(2);
				st.giveItems(SOE_SEA_OF_SPORES, 1);
				break;
			}
			case "orven_q10294_08.htm":
			{
				if (player.getLevel() < 45)
				{
					long expAdd = Experience.getExpForLevel(45) - player.getExp();
					st.addExpAndSp(expAdd, 270_000);
				}
				else
				{
					st.addExpAndSp(0, 270_000);
				}
				st.giveItems(SPIRIT_ORE, 500);
				st.giveItems(SOULSHOT_TICKET, 50);
				st.giveItems(SAYHAS_GUST, 9);
				st.giveItems(HP_POTION, 100);
				st.finishQuest();
				break;
			}
		}

		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();

		switch (npcId)
		{
			case MAXIMILIAN:
			{
				switch (cond)
				{
					case 0:
					{
						return "maximilian_q10294_01.htm";
					}
					case 1:
					{
						return "maximilian_q10294_05.htm";
					}
				}
				break;
			}
			case ORVEN:
			{
				switch (cond)
				{
					case 1:
					{
						return "orven_q10294_01.htm";
					}
					case 2:
					{
						return "orven_q10294_06.htm";
					}
					case 3:
					{
						return "orven_q10294_07.htm";
					}
				}
				break;
			}
		}
		return NO_QUEST_DIALOG;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 2) && updateKill(npc, qs))
		{
			qs.unset(A_LIST);
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.ALL_MISSIONS_ARE_COMPLETED_USE_SCROLL_OF_ESCAPE_HIGH_PRIEST_ORVEN_TO_GET_TO_HIGH_PRIEST_ORVEN_IN_ADEN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.giveItems(SOE_ORVEN, 1);
			qs.setCond(3);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
