package quests;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.TutorialShowHtmlPacket;

/**
 * @author nexvill
 */
public class _10971_TalismanEnchant extends Quest
{
	// Npcs
	private static final int BATHIS = 30332;

	// Items
	private static final int ADVENTURERS_TALISMAN = 91937;
	private static final int SCROLL_ENCHANT_ADVENTURERS_TALISMAN = 95688;

	public _10971_TalismanEnchant()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(BATHIS);
		addTalkId(BATHIS);
		addQuestCompletedCheck("notcompleted.htm", 10290);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		if (event.equalsIgnoreCase("bathis_q10971_04.htm"))
		{
			st.setCond(1);
			st.giveItems(SCROLL_ENCHANT_ADVENTURERS_TALISMAN, 1);
			player.sendPacket(new TutorialShowHtmlPacket(TutorialShowHtmlPacket.LARGE_WINDOW, "..\\L2text\\QT_028_talisman_02.htm"));
		}
		else if (event.equalsIgnoreCase("bathis_q10971_07.htm"))
		{
			st.addExpAndSp(0, 10_000);
			st.finishQuest();
		}

		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();

		if (npcId == BATHIS)
		{
			switch (cond)
			{
				case 0:
				{
					return "bathis_q10971_01.htm";
				}
				case 1:
				{
					ItemInstance talisman = st.getPlayer().getInventory().getItemByItemId(ADVENTURERS_TALISMAN);
					if ((talisman == null) || (talisman.getEnchantLevel() != 1))
					{
						st.getPlayer().sendPacket(new TutorialShowHtmlPacket(TutorialShowHtmlPacket.LARGE_WINDOW, "..\\L2text\\QT_028_talisman_02.htm"));
						return "bathis_q10971_05.htm";
					}
					else
					{
						return "bathis_q10971_06.htm";
					}
				}
			}
		}

		return NO_QUEST_DIALOG;
	}
}
