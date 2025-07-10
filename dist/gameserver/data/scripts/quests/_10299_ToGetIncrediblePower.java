package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10299_ToGetIncrediblePower extends Quest
{
	// Npcs
	private static final int ORVEN = 30857;

	// Monsters
	private static final int[] MOBS =
	{
		22107, // Rotting Tree
		22108, // Giant Fungus
		22109, // Dire Wyrm
		22110, // Taik Orc Supply Officer
		22111, // Tortured Undead
		22112, // Soul of Ruins
		22113, // Vanor Silenos Chieftain
		22114, // Vanor
		22115, // Vanor Silenos Shaman
		22116, // Farcran
		22117, // Deprive
		22118, // Hatar Ratman Boss
		22122, // Transcendent Treasure Chest
		22123, // Transcendent Treasure Chest
		22124, // Transcendent Treasure Chest
		22125 // Transcendent Treasure Chest
	};

	// Items
	private static final int SAYHAS_GUST = 91776;

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10299_ToGetIncrediblePower()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ORVEN);
		addTalkId(ORVEN, ORVEN);
		addKillNpcWithLog(1, NpcString.KILL_MONSTERS_IN_A_TRANSCENDENT_INSTANCE_ZONE.getId(), A_LIST, 100, MOBS);
		addLevelCheck("bad_level.htm", 45, 75);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "orven_q10299_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "orven_q10299_07.htm":
			{
				st.addExpAndSp(100_000_000, 2_700_000);
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
						return "orven_q10299_01.htm";
					}
					case 1:
					{
						return "orven_q10299_05.htm";
					}
					case 2:
					{
						return "orven_q10299_06.htm";
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
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.FIND_HIGH_PRIEST_ORVEN_AFTER_YOU_COME_OUT_A_TRANSCENDENT_INSTANCE_ZONE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.setCond(2);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
