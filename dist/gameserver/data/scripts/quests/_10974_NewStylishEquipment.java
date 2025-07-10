package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;

/**
 * @author nexvill
 */
public class _10974_NewStylishEquipment extends Quest
{
	// Npcs
	private static final int ORVEN = 30857;

	// Items
	private static final int ADVENTURERS_SCROLL_ENCHANT_HAIR_ACCESSORY = 93043;
	private static final int ADVENTURERS_SHEEP_HAT = 93044;
	private static final int ADVENTURERS_BELT = 93042;
	private static final int SCROLL_ENCHANT_ADVENTURERS_BELT = 93046;
	private static final int ADVENTURERS_CLOAK = 93041;
	private static final int SCROLL_ENCHANT_ADVENTURERS_CLOAK = 93045;
	private static final int SAYHAS_GUST = 91776;
	private static final int ADVENTURERS_PENDANT = 95690;

	public _10974_NewStylishEquipment()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ORVEN);
		addTalkId(ORVEN);
		addLevelCheck("notcompleted.htm", 40);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("orven_q10974_04.htm"))
		{
			st.setCond(1);
			st.giveItems(ADVENTURERS_SCROLL_ENCHANT_HAIR_ACCESSORY, 1);
			st.giveItems(ADVENTURERS_SHEEP_HAT, 1);
		}
		else if (event.equalsIgnoreCase("orven_q10974_07.htm"))
		{
			st.setCond(2);
			st.giveItems(ADVENTURERS_BELT, 1);
			st.giveItems(SCROLL_ENCHANT_ADVENTURERS_BELT, 1);
		}
		else if (event.equalsIgnoreCase("orven_q10974_10.htm"))
		{
			st.setCond(3);
			st.giveItems(ADVENTURERS_CLOAK, 1);
			st.giveItems(SCROLL_ENCHANT_ADVENTURERS_CLOAK, 1);
		}
		else if (event.equalsIgnoreCase("orven_q10974_13.htm"))
		{
			st.setCond(4);
		}
		else if (event.equalsIgnoreCase("orven_q10974_14.htm"))
		{
			st.giveItems(SAYHAS_GUST, 2);
			st.giveItems(ADVENTURERS_PENDANT, 1);
			st.finishQuest();
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
						return "orven_q10974_01.htm";
					}
					case 1:
					{
						ItemInstance hat = st.getPlayer().getInventory().getItemByItemId(ADVENTURERS_SHEEP_HAT);
						if ((hat == null) || (hat.getEnchantLevel() != 1))
						{
							return "orven_q10974_05.htm";
						}
						else
						{
							return "orven_q10974_06.htm";
						}
					}
					case 2:
					{
						ItemInstance belt = st.getPlayer().getInventory().getItemByItemId(ADVENTURERS_BELT);
						if ((belt == null) || (belt.getEnchantLevel() != 1))
						{
							return "orven_q10974_08.htm";
						}
						else
						{
							return "orven_q10974_09.htm";
						}
					}
					case 3:
					{
						ItemInstance cloak = st.getPlayer().getInventory().getItemByItemId(ADVENTURERS_CLOAK);
						if ((cloak == null) || (cloak.getEnchantLevel() != 1))
						{
							return "orven_q10974_11.htm";
						}
						else
						{
							return "orven_q10974_12.htm";
						}
					}
				}
				break;
			}
		}
		return NO_QUEST_DIALOG;
	}
}
