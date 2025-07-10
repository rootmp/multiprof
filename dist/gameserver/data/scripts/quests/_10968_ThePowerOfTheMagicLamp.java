package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;

/**
 * @author nexvill
 */
public class _10968_ThePowerOfTheMagicLamp extends Quest
{
	// Npcs
	private static final int MAXIMILIAN = 30120;

	// Items
	private static final int SAYHAS_GUST = 91776;
	private static final int MAGIC_FIRE = 92033;

	public _10968_ThePowerOfTheMagicLamp()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(MAXIMILIAN);
		addTalkId(MAXIMILIAN);
		addLevelCheck("notcompleted.htm", 40);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("maximilian_q10968_04.htm"))
		{
			st.setCond(1);
		}
		else if (event.equalsIgnoreCase("maximilian_q10968_06.htm"))
		{
			st.giveItems(SAYHAS_GUST, 1);
			st.giveItems(MAGIC_FIRE, 1);
			st.finishQuest();
		}

		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();

		if (npcId == MAXIMILIAN)
		{
			switch (cond)
			{
				case 0:
				{
					return "maximilian_q10968_01.htm";
				}
				case 1:
				{
					return "maximilian_q10968_05.htm";
				}
			}
		}

		return NO_QUEST_DIALOG;
	}
}
