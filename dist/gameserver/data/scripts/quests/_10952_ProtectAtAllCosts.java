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
public class _10952_ProtectAtAllCosts extends Quest
{
	// Npc
	private static final int TANAI = 30602;

	// Monsters
	private static final int[] MOBS =
	{
		20312, // Rakeclaw Imp Hunter
		20319, // Goblin Tomb Raider
		20475, // Kasha Wolf
		20477 // Kasha Forest Wolf
	};

	// Items
	private static final int SOE_NOVICE = 10650;
	private static final int NOVICE_NECKLACE = 49039;
	private static final int NOVICE_EARRING = 49040;
	private static final int NOVICE_RING = 49041;
	private static final int SOE_TANAI = 97230;

	// Etc
	private static final String A_LIST = "A_LIST";

	// Location
	private static final Location VALLEY_OF_HEROES_TELEPORT = new Location(-37158, -116702, -1632);

	public _10952_ProtectAtAllCosts()
	{
		super(PARTY_NONE, ONETIME);
		addStartNpc(TANAI);
		addTalkId(TANAI);
		addKillNpcWithLog(1, NpcString.SUBJUGATION_IN_THE_VALLEY_OF_HEROES.getId(), A_LIST, 20, MOBS);
		addLevelCheck("high_level.htm", 2, 15);
		addClassIdCheck("wrong_class.htm", 217);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "tanai_q10952_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "teleport":
			{
				player.teleToLocation(VALLEY_OF_HEROES_TELEPORT);
				return null;
			}
			case "tanai_q10952_07.htm":
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
		int cond = st.getCond();

		if (npc.getNpcId() == TANAI)
		{
			switch (cond)
			{
				case 0:
					return "tanai_q10952_01.htm";
				case 1:
					return "tanai_q10952_05.htm";
				case 2:
					return "tanai_q10952_06.htm";
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
			qs.getPlayer().getInventory().addItem(SOE_TANAI, 1);
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.THE_TRAINING_IN_OVER_USE_A_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_GO_BACK_TO_TANAI, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
