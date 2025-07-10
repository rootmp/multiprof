package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10303_SymbolOfHubris extends Quest
{
	// Npcs
	private static final int ORVEN = 30857;

	// Monsters
	private static final int[] MOBS =
	{
		21989, // Ghost of the Tower
		21990, // Tower Watchman
		21991, // Ghastly Warrior
		21992, // Archer of Despair
		21994, // Crendion
		21995, // Swordsman of Ordeal
		21996, // Hound of Destruction
		21997, // Royal Guard of Insolence
		21998, // Archer of Insolence
		22000, // Corrupted Ghost
		22001, // Corrupted Sage
		22002, // Corrupted Warrior
		22003, // Archer of Abyss
		22005, // Hallate's Inspector
		22006, // Hallate's Knight
		22007, // Hallate's Commander
		22008, // Hallate's Maid
		22010, // Platinum Tribe Soldier
		22011, // Platinum Tribe Shaman
		22012, // Platinum Tribe Guard
		22013, // Platinum Tribe Knight
		22015, // Platinum Tribe Warrior
		22016, // Platinum Tribe Elite Soldier
		22017, // Platinum Tribe Archer
		22018, // Platinum Tribe Gladiator
		22020, // Guardian Angel
		22021, // Guardian Archangel
		22022, // Guardian Angel Messenger
		22024, // Angel Messenger of Insolence
		22025, // Prime Messenger of Insolence
		22026, // Wise Messenger of Insolence
		22028, // Virtuous Guardian Angel
		22029, // Messenger Commander
		22030, // Messenger Scout
		22032, // Sealed Angel
		22033, // Sealed Guardian Angel
		22035, // Tower Guardian Archangel
		22036, // Tower Guardian Archer
		22038, // Tower Guardian Shaman
		22039, // Tower Guardian Messenger
		22042, // Mool
		22043 // Etiko
	};

	// Items
	private static final int MAGIC_LAMP_CHARGING_POTION = 91757;
	private static final int SAYHAS_COOKIE = 93274;
	private static final int SAYHAS_STORM = 91712;

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10303_SymbolOfHubris()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ORVEN);
		addTalkId(ORVEN);
		addKillNpcWithLog(1, NpcString.DEFEAT_MONSTERS_IN_THE_TOWER_OF_INSOLENCE.getId(), A_LIST, 1000, MOBS);
		addLevelCheck("bad_level.htm", 78);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "orven_q10303_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "orven_q10303_07.htm":
			{
				st.addExpAndSp(100_000_000, 2_700_000);
				st.giveItems(MAGIC_LAMP_CHARGING_POTION, 1);
				st.giveItems(SAYHAS_COOKIE, 10);
				st.giveItems(SAYHAS_STORM, 8);
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
						return "orven_q10303_01.htm";
					}
					case 1:
					{
						return "orven_q10303_05.htm";
					}
					case 2:
					{
						return "orven_q10303_06.htm";
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
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.MONSTERS_IN_THE_TOWER_OF_INSOLENCE_HAVE_BEEN_DEFEATED_TALK_TO_HIGH_PRIEST_ORVEN_IN_ADEN_TOWN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.setCond(2);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
