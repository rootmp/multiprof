package quests;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10292_SecretGarden extends Quest
{
	// Npcs
	private static final int BATHIS = 30332;
	private static final int RAYMOND = 30289;

	// Items
	private static final int SOE_GARDEN = 95588;
	private static final int SOE_RAYMOND = 91736;
	private static final int ADVENTURERS_AGATHION_BRACELET = 91933;
	private static final int ADVENTURERS_AGATHION_GRIFFIN = 91935;
	private static final int SCROLL_ENCHANT_ADEN_WEAPON = 93038;

	// Monsters
	private static final int[] MOBS =
	{
		20145, // Harpy
		20158, // Medusa
		20176, // Wyrm
		20199, // Amber Basilisk
		20248, // Turak Bugbear
		20249 // Turak Bugbear Warrior
	};

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10292_SecretGarden()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(BATHIS);
		addTalkId(BATHIS, RAYMOND);
		addKillNpcWithLog(2, NpcString.CLEAR_GORGON_FLOWER_GARDEN.getId(), A_LIST, 70, MOBS);
		addLevelCheck("bad_level.htm", 30);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "bathis_q10292_02.htm":
			{
				st.setCond(1);
				player.sendPacket(new ExShowScreenMessage(NpcString.BEFORE_YOU_GO_FOR_A_BATTLE_CHECK_THE_SKILL_WINDOW_NEW_SKILLS_WILL_HELP_YOU_TO_GET_STRONGER, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				break;
			}
			case "raymond_q10292_04.htm":
			{
				st.setCond(2);
				st.giveItems(SOE_GARDEN, 1);
				break;
			}
			case "raymond_q10292_07.htm":
			{
				if (player.getLevel() < 35)
				{
					long expAdd = Experience.getExpForLevel(35) - player.getExp();
					st.addExpAndSp(expAdd, 135_000);
				}
				else
				{
					st.addExpAndSp(0, 135_000);
				}
				st.giveItems(ADVENTURERS_AGATHION_BRACELET, 1);
				st.giveItems(ADVENTURERS_AGATHION_GRIFFIN, 1);
				st.giveItems(SCROLL_ENCHANT_ADEN_WEAPON, 2);
				player.sendPacket(new ExShowScreenMessage(NpcString.YOUVE_GOT_ADVENTURERS_AGATHION_BRACELET_AND_ADVENTURERS_AGATHION_GRIFFIN_COMPLETE_THE_TUTORIAL_AND_TRY_TO_USE_THE_AGATHION, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
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
			case RAYMOND:
			{
				switch (cond)
				{
					case 1:
					{
						return "raymond_q10292_01.htm";
					}
					case 2:
					{
						return "raymond_q10292_05.htm";
					}
					case 3:
					{
						return "raymond_q10292_06.htm";
					}
				}
				break;
			}
			case BATHIS:
			{
				switch (cond)
				{
					case 0:
					{
						return "bathis_q10292_01.htm";
					}
					case 1:
					{
						return "bathis_q10292_03.htm";
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
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.MONSTERS_OF_THE_GORGON_FLOWER_GARDEN_ARE_KILLED_USE_THE_TELEPORT_TO_GET_TO_HIGH_PRIEST_RAYMOND_IN_GLUDIO, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.giveItems(SOE_RAYMOND, 1);
			qs.setCond(3);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
