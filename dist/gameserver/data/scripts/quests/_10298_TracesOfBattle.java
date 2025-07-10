package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10298_TracesOfBattle extends Quest
{
	// Npcs
	private static final int ORVEN = 30857;

	// Monsters
	private static final int[] MOBS =
	{
		20659, // Graveyard Wanderer
		20660, // Archer of Greed
		20661, // Hatar Ratman Thief
		20662, // Hatar Ratman Boss
		20663, // Hatar Hanishee
		20664, // Deprive
		20665, // Taik Orc Elder
		20667, // Farcran
		22103 // Fierce Guard
	};

	// Items
	private static final int SOE_ORVEN = 91768;
	private static final int SOE_WAR_TORN_PLAINS = 95594;
	private static final int MAGIC_LAMP_CHARGING_POTION = 91757;
	private static final int SAYHAS_GUST = 91776;
	private static final int SPIRIT_ORE = 3031;
	private static final int SOULSHOT_TICKET = 90907;

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10298_TracesOfBattle()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ORVEN);
		addTalkId(ORVEN);
		addKillNpcWithLog(1, NpcString.KILL_MONSTERS_ON_THE_WAR_TORN_PLAINS.getId(), A_LIST, 700, MOBS);
		addLevelCheck("bad_level.htm", 64, 70);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "orven_q10298_04.htm":
			{
				st.setCond(1);
				st.giveItems(SOE_WAR_TORN_PLAINS, 1);
				break;
			}
			case "orven_q10298_07.htm":
			{
				st.addExpAndSp(50_000_000, 1_350_000);
				st.giveItems(MAGIC_LAMP_CHARGING_POTION, 3);
				st.giveItems(SOULSHOT_TICKET, 10);
				st.giveItems(SAYHAS_GUST, 9);
				st.giveItems(SPIRIT_ORE, 450);
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
			case ORVEN:
			{
				switch (cond)
				{
					case 0:
					{
						return "orven_q10298_01.htm";
					}
					case 1:
					{
						return "orven_q10298_05.htm";
					}
					case 2:
					{
						return "orven_q10298_06.htm";
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
		if ((qs.getCond() == 1) && updateKill(npc, qs))
		{
			qs.unset(A_LIST);
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.ALL_MISSIONS_ARE_COMPLETED_USE_SCROLL_OF_ESCAPE_HIGH_PRIEST_ORVEN_TO_GET_TO_HIGH_PRIEST_ORVEN_IN_ADEN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.giveItems(SOE_ORVEN, 1);
			qs.setCond(2);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
