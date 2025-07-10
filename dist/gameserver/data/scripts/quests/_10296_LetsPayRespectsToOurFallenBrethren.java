package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10296_LetsPayRespectsToOurFallenBrethren extends Quest
{
	// Npcs
	private static final int ORVEN = 30857;

	// Monsters
	private static final int[] MOBS =
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
		21010, // Doom Warrior
		22101 // Guard Butcher
	};

	// Items
	private static final int SOE_ORVEN = 91768;
	private static final int SOE_FIELDS_OF_MASSACRE = 95592;
	private static final int MAGIC_LAMP_CHARGING_POTION = 91757;
	private static final int SAYHAS_GUST = 91776;
	private static final int SPIRIT_ORE = 3031;
	private static final int SOULSHOT_TICKET = 90907;

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10296_LetsPayRespectsToOurFallenBrethren()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ORVEN);
		addTalkId(ORVEN);
		addKillNpcWithLog(1, NpcString.KILL_MONSTERS_IN_THE_FIELDS_OF_MASSACRE.getId(), A_LIST, 400, MOBS);
		addLevelCheck("bad_level.htm", 52, 56);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "orven_q10296_04.htm":
			{
				st.setCond(1);
				st.giveItems(SOE_FIELDS_OF_MASSACRE, 1);
				break;
			}
			case "orven_q10296_07.htm":
			{
				st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.FROM_NOW_TRY_TO_GET_AS_MUCH_QUESTS_AS_YOU_CAN_ILL_TELL_YOU_WHAT_TO_DO_NEXT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
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
						return "orven_q10296_01.htm";
					}
					case 1:
					{
						return "orven_q10296_05.htm";
					}
					case 2:
					{
						return "orven_q10296_06.htm";
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
