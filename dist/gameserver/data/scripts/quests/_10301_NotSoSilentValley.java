package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10301_NotSoSilentValley extends Quest
{
	// Npcs
	private static final int ORVEN = 30857;

	// Monsters
	private static final int[] MOBS =
	{
		20967, // Creature of the Past
		20968, // Forgotten Face
		20969, // Giant's Shadow
		20971, // Warrior of Ancient Times
		20972, // Shaman of Ancient Times
		20973, // Forgotten Ancient Creature
		22106 // Ancient Guardian
	};

	// Items
	private static final int MAGIC_LAMP_CHARGING_POTION = 91757;
	private static final int SAYHAS_COOKIE = 93274;
	private static final int SAYHAS_STORM = 91712;

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10301_NotSoSilentValley()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ORVEN);
		addTalkId(ORVEN);
		addKillNpcWithLog(1, NpcString.DEFEAT_MONSTERS_IN_THE_SILENT_VALLEY.getId(), A_LIST, 1000, MOBS);
		addLevelCheck("bad_level.htm", 70);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "orven_q10301_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "orven_q10301_07.htm":
			{
				st.addExpAndSp(50_000_000, 1_350_000);
				st.giveItems(MAGIC_LAMP_CHARGING_POTION, 1);
				st.giveItems(SAYHAS_COOKIE, 5);
				st.giveItems(SAYHAS_STORM, 4);
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
						return "orven_q10301_01.htm";
					}
					case 1:
					{
						return "orven_q10301_05.htm";
					}
					case 2:
					{
						return "orven_q10301_06.htm";
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
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.MONSTERS_IN_THE_SILENT_VALLEY_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.setCond(2);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
