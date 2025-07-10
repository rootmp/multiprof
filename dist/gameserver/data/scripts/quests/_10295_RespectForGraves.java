package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10295_RespectForGraves extends Quest
{
	// Npcs
	private static final int ORVEN = 30857;

	// Monsters
	private static final int[] MOBS =
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
		21000, // Soul of Ruins
		22128, // Grave Warden
	};

	// Items
	private static final int SOE_CEMETERY = 95591;
	private static final int SOE_ORVEN = 91768;
	private static final int ASOFE = 92994;
	private static final int SAYHAS_GUST = 91776;

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10295_RespectForGraves()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ORVEN);
		addTalkId(ORVEN);
		addKillNpcWithLog(1, NpcString.KILL_MONSTERS_IN_THE_CEMETERY.getId(), A_LIST, 300, MOBS);
		addLevelCheck("bad_level.htm", 45, 52);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "orven_q10295_04.htm":
			{
				st.setCond(1);
				st.giveItems(SOE_CEMETERY, 1);
				break;
			}
			case "orven_q10295_07.htm":
			{
				st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.FROM_NOW_TRY_TO_GET_AS_MUCH_QUESTS_AS_YOU_CAN_ILL_TELL_YOU_WHAT_TO_DO_NEXT, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				st.addExpAndSp(50_000_000, 1_350_000);
				st.giveItems(ASOFE, 1);
				st.giveItems(SAYHAS_GUST, 9);
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
						return "orven_q10295_01.htm";
					}
					case 1:
					{
						return "orven_q10295_05.htm";
					}
					case 2:
					{
						return "orven_q10295_06.htm";
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
