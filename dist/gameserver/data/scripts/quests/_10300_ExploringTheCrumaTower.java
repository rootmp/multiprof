package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10300_ExploringTheCrumaTower extends Quest
{
	// Npcs
	private static final int ORVEN = 30857;
	private static final int CARSUS = 30483;

	// Items
	private static final int MAGIC_LAMP_CHARGING_POTION = 91757;
	private static final int SAYHAS_COOKIE = 93274;
	private static final int SAYHAS_STORM = 91712;

	// Monsters
	private static final int[] MOBS =
	{
		22200, // Porta
		22201, // Excuro
		22202, // Mordeo
		22203, // Ricenseo
		22204, // Krator
		22205, // Catherok
		22206, // Premo
		22207, // Validus
		22208, // Dicor
		22209, // Perum
		22210, // Torfe
		22211 // Death Lord
	};

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10300_ExploringTheCrumaTower()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ORVEN);
		addTalkId(ORVEN, CARSUS);
		addKillNpcWithLog(2, NpcString.DEFEAT_MONSTERS_IN_THE_CRUMA_TOWER.getId(), A_LIST, 1000, MOBS);
		addLevelCheck("bad_level.htm", 65);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "orven_q10300_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "carsus_q10300_02.htm":
			{
				st.setCond(2);
				break;
			}
			case "carsus_q10300_05.htm":
			case "orven_q10300_07.htm":
			{
				st.addExpAndSp(50_000_000, 1_350_000);
				st.giveItems(MAGIC_LAMP_CHARGING_POTION, 1);
				st.giveItems(SAYHAS_COOKIE, 5);
				st.giveItems(SAYHAS_STORM, 2);
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
			case CARSUS:
			{
				switch (cond)
				{
					case 1:
					{
						return "carsus_q10300_01.htm";
					}
					case 2:
					{
						return "carsus_q10300_03.htm";
					}
					case 3:
					{
						return "carsus_q10300_04.htm";
					}
				}
				break;
			}
			case ORVEN:
			{
				switch (cond)
				{
					case 0:
					{
						return "orven_q10300_01.htm";
					}
					case 1:
					{
						return "orven_q10300_05.htm";
					}
					case 3:
					{
						return "orven_q10300_06.htm";
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
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.ALL_CRUMA_TOWER_EXPLORATION_SUPPORT_MISSIONS_ARE_FINISHED_TALK_TO_IVORY_TOWER_WIZARD_CARSUS_NEAR_THE_CRUMA_TOWER_ENTRANCE_OR_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.setCond(3);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
