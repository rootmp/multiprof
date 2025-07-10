package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10306_StopSelMahumsTroops extends Quest
{
	// Npcs
	private static final int ORVEN = 30857;

	// Monsters
	private static final int[] MOBS =
	{
		22237, // Sel Mahum Sniper
		22238, // Sel Mahum Raider
		22239, // Sel Mahum Berserker
		22240, // Sel Mahum Mage
		22241, // Sel Mahum Thief
		22242, // Sel Mahum Wizard
		22243, // Sel Mahum Knight
		22244, // Sel Mahum Footman
		22245 // Sel Mahum Bowman
	};

	// Items
	private static final int MAGIC_LAMP_CHARGING_POTION = 91757;
	private static final int SAYHAS_COOKIE = 93274;
	private static final int SAYHAS_STORM = 91712;

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10306_StopSelMahumsTroops()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ORVEN);
		addTalkId(ORVEN);
		addKillNpcWithLog(1, NpcString.DEFEAT_MONSTERS_IN_SEL_MAHUM_BASE.getId(), A_LIST, 1000, MOBS);
		addLevelCheck("bad_level.htm", 85);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "orven_q10306_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "orven_q10306_07.htm":
			{
				st.addExpAndSp(400_000_000, 10_800_000);
				st.giveItems(MAGIC_LAMP_CHARGING_POTION, 2);
				st.giveItems(SAYHAS_COOKIE, 20);
				st.giveItems(SAYHAS_STORM, 12);
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
						return "orven_q10306_01.htm";
					}
					case 1:
					{
						return "orven_q10306_05.htm";
					}
					case 2:
					{
						return "orven_q10306_06.htm";
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
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.MONSTERS_IN_THE_SEL_MAHUM_BASE_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.setCond(2);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
