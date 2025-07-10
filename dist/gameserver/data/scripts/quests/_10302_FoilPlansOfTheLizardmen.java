package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10302_FoilPlansOfTheLizardmen extends Quest
{
	// Npcs
	private static final int ORVEN = 30857;

	// Monsters
	private static final int[] MOBS =
	{
		22151, // Tanta Lizardman
		22152, // Tanta Lizardman Warrior
		22153, // Tanta Lizardman Berserker
		22154, // Tanta Lizardman Archer
		22155 // Tanta Lizardman Summoner
	};

	// Items
	private static final int MAGIC_LAMP_CHARGING_POTION = 91757;
	private static final int SAYHAS_COOKIE = 93274;
	private static final int SAYHAS_STORM = 91712;

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10302_FoilPlansOfTheLizardmen()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ORVEN);
		addTalkId(ORVEN);
		addKillNpcWithLog(1, NpcString.ERADICATE_MONSTERS_IN_THE_PLAINS_OF_THE_LIZARDMEN.getId(), A_LIST, 1000, MOBS);
		addLevelCheck("bad_level.htm", 76);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "orven_q10302_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "orven_q10302_07.htm":
			{
				st.addExpAndSp(100_000_000, 2_700_000);
				st.giveItems(MAGIC_LAMP_CHARGING_POTION, 1);
				st.giveItems(SAYHAS_COOKIE, 10);
				st.giveItems(SAYHAS_STORM, 6);
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
						return "orven_q10302_01.htm";
					}
					case 1:
					{
						return "orven_q10302_05.htm";
					}
					case 2:
					{
						return "orven_q10302_06.htm";
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
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.MONSTERS_IN_THE_PLAINS_OF_THE_LIZARDMEN_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.setCond(2);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
