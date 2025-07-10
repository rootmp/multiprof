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
public class _10955_NewLifesLesson extends Quest
{
	// Npcs
	private static final int KERKIR = 34210;

	// Monsters
	private static final int[] MOBS =
	{
		22325, // Diffloe
		22326, // Apis
		22327 // Echinu
	};

	// Items
	private static final int SOE_NOVICE = 10650;
	private static final int NOVICE_NECKLACE = 49039;
	private static final int NOVICE_EARRING = 49040;
	private static final int NOVICE_RING = 49041;
	private static final int SOE_KERKIR = 95586;

	// Etc
	private static final String A_LIST = "A_LIST";

	// Location
	private static final Location TRAINING_GROUNDS_TELEPORT = new Location(104348, 43601, -4656);

	public _10955_NewLifesLesson()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(KERKIR);
		addTalkId(KERKIR);
		addKillNpcWithLog(1, NpcString.KILL_MONSTERS_IN_THE_QUIET_PLAIN.getId(), A_LIST, 20, MOBS);
		addLevelCheck("high_level.htm", 2, 15);
		addClassIdCheck("wrong_class.htm", 208);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "kerkir_q10955_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "teleport":
			{
				player.teleToLocation(TRAINING_GROUNDS_TELEPORT);
				return null;
			}
			case "kerkir_q10955_07.htm":
			{
				st.addExpAndSp(260_000, 6_000);
				st.giveItems(SOE_NOVICE, 10);
				st.giveItems(NOVICE_NECKLACE, 1);
				st.giveItems(NOVICE_EARRING, 2);
				st.giveItems(NOVICE_RING, 2);
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
			case KERKIR:
			{
				switch (cond)
				{
					case 0:
					{
						return "kerkir_q10955_01.htm";
					}
					case 1:
					{
						return "kerkir_q10955_05.htm";
					}
					case 2:
					{
						return "kerkir_q10955_06.htm";
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
			qs.setCond(2);
			qs.getPlayer().getInventory().addItem(SOE_KERKIR, 1);
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.THE_TRAINING_IN_OVER_USE_A_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_GO_BACK_TO_MASTER_KERKIR, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}