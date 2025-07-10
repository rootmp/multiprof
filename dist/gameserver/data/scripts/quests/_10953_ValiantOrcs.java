package quests;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10953_ValiantOrcs extends Quest
{
	// Npcs
	private static final int TANAI = 30602;
	private static final int GANTAKI_ZU_URUTU = 30587;

	// Monsters
	private static final int[] MOBS =
	{
		20364, // Maraku Werewolf Chieftain
		20428, // Evil Eye Patrol
		20474, // Kasha Spider
		20478 // Kasha Blade Spider
	};

	// Items
	private static final int SOE_NOVICE = 10650;
	private static final int SPIRIT_ORE = 3031;
	private static final int XP_GROWTH_SCROLL = 49674;
	private static final int HEALING_POTION = 91912;
	private static final int SOE_GANTAKI_ZU_URUTU = 97231;

	// Etc
	private static final String A_LIST = "A_LIST";

	// Location
	private static final Location NORTHERN_IMMORTAL_PLATEAU_TELEPORT = new Location(9594, -130417, -2264);

	public _10953_ValiantOrcs()
	{
		super(PARTY_NONE, ONETIME);
		addStartNpc(TANAI);
		addTalkId(TANAI, GANTAKI_ZU_URUTU);
		addKillNpcWithLog(1, NpcString.SUBJUGATION_IN_THE_NORTHERN_AREA_OF_THE_IMMORTAL_PLATEAU.getId(), A_LIST, 20, MOBS);
		addLevelCheck("high_level.htm", 2, 15);
		addClassIdCheck("wrong_class.htm", 217);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "tanai_q10953_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "teleport":
			{
				player.teleToLocation(NORTHERN_IMMORTAL_PLATEAU_TELEPORT);
				return null;
			}
			case "gantaki_zu_urutu_q10953_02.htm":
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.YOUVE_FINISHED_THE_TUTORIAL_TAKE_YOUR_1ST_CLASS_TRANSFER_AND_COMPLETE_YOUR_TRAINING_WITH_BATHIS_TO_BECOME_STRONGER, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				st.addExpAndSp(600_000, 13_500);
				st.giveItems(SOE_NOVICE, 20);
				st.giveItems(SPIRIT_ORE, 50);
				st.giveItems(XP_GROWTH_SCROLL, 1);
				st.giveItems(HEALING_POTION, 50);
				st.finishQuest();
				break;
			}
		}

		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();

		switch (npcId)
		{
			case TANAI:
			{
				switch (cond)
				{
					case 0:
						return "tanai_q10953_01.htm";
					case 1:
						return "tanai_q10953_05.htm";
				}
				break;
			}
			case GANTAKI_ZU_URUTU:
			{
				if (cond == 2)
					return "gantaki_zu_urutu_q10953_01.htm";
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
			qs.setCond(2);
			qs.getPlayer().getInventory().addItem(SOE_GANTAKI_ZU_URUTU, 1);
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.THE_TRAINING_IN_OVER_USE_A_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_GO_BACK_TO_GANTAKI_ZU_URUTU, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
