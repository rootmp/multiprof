package quests;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10958_ExploringNewOpportunities extends Quest
{
	// Npcs
	private static final int KILREMANGE = 34138;
	private static final int MATHORN = 34139;

	// Monsters
	private static final int[] MOBS =
	{
		22184, // Skeleton Archer
		22185 // Skeleton Warrior
	};

	// Items
	private static final int SOE_NOVICE = 10650;
	private static final int NOVICE_NECKLACE = 49039;
	private static final int NOVICE_EARRING = 49040;
	private static final int NOVICE_RING = 49041;
	private static final int SOE_MATHORN = 93319;

	// Etc
	private static final String A_LIST = "A_LIST";

	// Location
	private static final Location TRAINING_GROUNDS_TELEPORT = new Location(43821, 148444, -3696);

	public _10958_ExploringNewOpportunities()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(KILREMANGE);
		addTalkId(MATHORN);
		addKillNpcWithLog(1, NpcString.CLEAR_TRAINING_GROUNDS_I.getId(), A_LIST, 20, MOBS);
		addLevelCheck("high_level.htm", 2, 15);
		addClassIdCheck("wrong_class.htm", 196, 200, 204);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "kilremange_q10958_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "teleport":
			{
				player.teleToLocation(TRAINING_GROUNDS_TELEPORT);
				return null;
			}
			case "mathorn_q10958_02.htm":
			{
				st.addExpAndSp(260_000, 6_000);
				st.giveItems(SOE_NOVICE, 10);
				st.giveItems(NOVICE_NECKLACE, 1);
				st.giveItems(NOVICE_EARRING, 2);
				st.giveItems(NOVICE_RING, 2);
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
			case KILREMANGE:
			{
				switch (cond)
				{
					case 0:
					{
						return "kilremange_q10958_01.htm";
					}
					case 1:
					{
						return "kilremange_q10958_05.htm";
					}
					case 2:
					{
						return "kilremange_q10958_06.htm";
					}
				}
				break;
			}
			case MATHORN:
			{
				if (cond == 2)
					return "mathorn_q10958_01.htm";
				break;
			}
		}
		return NO_QUEST_DIALOG;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && updateKill(npc, qs))
		{
			qs.unset(A_LIST);
			qs.setCond(2);
			qs.getPlayer().getInventory().addItem(SOE_MATHORN, 1);
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.THE_TRAINING_IN_OVER_USE_A_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_GO_BACK_TO_QUARTERMASTER_MATHORN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}