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
public class _10291_MoreExperience extends Quest
{
	// Npcs
	private static final int BATHIS = 30332;

	// Monsters
	private static final int[] MOBS =
	{
		20063, // Ol Mahum Shooter
		20066, // Ol Mahum Officer
		20076, // Ol Mahum Commander
		20438, // Ol Mahum General
		20439 // Ol Mahum Sergeant
	};

	// Items
	private static final int SOE_ABANDONED_CAMP = 91725;
	private static final int SOE_BATHIS = 91651;
	private static final int ADVENTURER_BROOCH = 91932;
	private static final int JEWEL_OF_ADVENTURER_FRAGMENT = 91936;
	private static final int SCROLL_ENCHANT_ADEN_WEAPON = 93038;

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10291_MoreExperience()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(BATHIS);
		addTalkId(BATHIS);
		addKillNpcWithLog(1, NpcString.CLEAR_ABANDONED_CAMP.getId(), A_LIST, 50, MOBS);
		addLevelCheck("bad_level.htm", 25);
		addQuestItem(SOE_ABANDONED_CAMP);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "bathis_q10291_05.htm":
			{
				st.setCond(1);
				player.sendPacket(new ExShowScreenMessage(NpcString.BEFORE_YOU_GO_FOR_A_BATTLE_CHECK_THE_SKILL_WINDOW_NEW_SKILLS_WILL_HELP_YOU_TO_GET_STRONGER, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				st.giveItems(SOE_ABANDONED_CAMP, 1);
				giveStoryBuff(npc, player);
				break;
			}
			case "bathis_q10291_08.htm":
			{
				if (player.getLevel() < 30)
				{
					long expAdd = Experience.getExpForLevel(30) - player.getExp();
					st.addExpAndSp(expAdd, 117_500);
				}
				else
				{
					st.addExpAndSp(0, 117_500);
				}
				st.giveItems(ADVENTURER_BROOCH, 1);
				st.giveItems(JEWEL_OF_ADVENTURER_FRAGMENT, 1);
				st.giveItems(SCROLL_ENCHANT_ADEN_WEAPON, 2);
				player.sendPacket(new ExShowScreenMessage(NpcString.YOUVE_GOT_ADVENTURERS_BROOCH_AND_ADVENTURERS_ROUGH_JEWEL_COMPLETE_THE_TUTORIAL_AND_TRY_TO_USE_THE_JEWEL, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
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
			case BATHIS:
			{
				switch (cond)
				{
					case 0:
					{
						return "bathis_q10291_01.htm";
					}
					case 1:
					{
						return "bathis_q10291_06.htm";
					}
					case 2:
					{
						return "bathis_q10291_07.htm";
					}
				}
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
			qs.giveItems(SOE_BATHIS, 1);
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.YOUVE_MADE_THE_FIRST_STEPS_ON_THE_ADVENTURERS_PATH_RETURN_TO_BATHIS_TO_GET_YOUR_REWARD, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.setCond(2);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
