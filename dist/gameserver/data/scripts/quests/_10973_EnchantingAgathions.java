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
public class _10973_EnchantingAgathions extends Quest
{
	// Npcs
	private static final int RAYMOND = 30289;

	// Items
	private static final int ADVENTURERS_AGATHION_GRIFFIN = 91935;
	private static final int SCROLL_ENCHANT_ADVENTURERS_AGATHION = 93040;

	public _10973_EnchantingAgathions()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(RAYMOND);
		addTalkId(RAYMOND);
		addQuestCompletedCheck("notcompleted.htm", 10292);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		if (event.equalsIgnoreCase("raymond_q10973_04.htm"))
		{
			st.setCond(1);
			st.giveItems(SCROLL_ENCHANT_ADVENTURERS_AGATHION, 1);
			player.sendPacket(new TutorialShowHtmlPacket(TutorialShowHtmlPacket.LARGE_WINDOW, "..\\L2text\\QT_030_agathion_02.htm"));
		}
		else if (event.equalsIgnoreCase("raymond_q10973_07.htm"))
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

		if (npcId == RAYMOND)
		{
			switch (cond)
			{
				case 0:
				{
					return "raymond_q10973_01.htm";
				}
				case 1:
				{
					ItemInstance agathion = st.getPlayer().getInventory().getItemByItemId(ADVENTURERS_AGATHION_GRIFFIN);
					if ((agathion == null) || (agathion.getEnchantLevel() != 1))
					{
						st.getPlayer().sendPacket(new TutorialShowHtmlPacket(TutorialShowHtmlPacket.LARGE_WINDOW, "..\\L2text\\QT_030_agathion_02.htm"));
						return "raymond_q10973_05.htm";
					}
					else
					{
						return "raymond_q10973_06.htm";
					}
				}
			}
		}

		return NO_QUEST_DIALOG;
	}
}
