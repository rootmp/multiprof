package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10304_ChangesInTheDragonValley extends Quest
{
	// Npcs
	private static final int ORVEN = 30857;

	// Monsters
	private static final int[] MOBS =
	{
		20134, // Cave Maiden
		20137, // Drake
		20142, // Blood Queen
		20146, // Headless Knight
		20235, // Convict
		20236, // Cave Servant
		20237, // Cave Servant Archer
		20238, // Cave Servant Warrior
		20239, // Cave Servant Captain
		20240, // Royal Cave Servant
		20241, // Gargoyle Hunter
		20242, // Dustwind Gargoyle
		20243, // Thunder Wyrm
		20244, // Maluk Succubus
		20246, // Cave Keeper
		20412, // Cave Banshee
		20758, // Dragon Bearer Captain
		20759, // Dragon Bearer Warrior
		20760, // Dragon Bearer Archer
		21960 // Lord Ishka
	};

	// Items
	private static final int MAGIC_LAMP_CHARGING_POTION = 91757;
	private static final int SAYHAS_COOKIE = 93274;
	private static final int SAYHAS_STORM = 91712;

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10304_ChangesInTheDragonValley()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ORVEN);
		addTalkId(ORVEN);
		addKillNpcWithLog(1, NpcString.DEFEAT_MONSTERS_IN_THE_DRAGON_VALLEY_WEST.getId(), A_LIST, 1000, MOBS);
		addLevelCheck("bad_level.htm", 78);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "orven_q10304_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "orven_q10304_07.htm":
			{
				st.addExpAndSp(100_000_000, 2_700_000);
				st.giveItems(MAGIC_LAMP_CHARGING_POTION, 2);
				st.giveItems(SAYHAS_COOKIE, 10);
				st.giveItems(SAYHAS_STORM, 10);
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
						return "orven_q10304_01.htm";
					}
					case 1:
					{
						return "orven_q10304_05.htm";
					}
					case 2:
					{
						return "orven_q10304_06.htm";
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
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.MONSTERS_IN_THE_DRAGON_VALLEY_WEST_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.setCond(2);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
