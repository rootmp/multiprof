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
public class _10956_WeSylphs extends Quest
{
	// Npcs
	private static final int KERKIR = 34210;
	private static final int EVIA = 34211;

	// Monsters
	private static final int[] MOBS =
	{
		22328, // Volatu
		22329, // Photoroni
		22330 // Ales
	};

	// Items
	private static final int SOE_NOVICE = 10650;
	private static final int SPIRIT_ORE = 3031;
	private static final int XP_GROWTH_SCROLL = 49674;
	private static final int HEALING_POTION = 91912;
	private static final int SOE_EVIA = 95587;
	private static final int SOE_BATHIS = 91651;

	// Etc
	private static final String A_LIST = "A_LIST";

	// Location
	private static final Location TRAINING_GROUNDS_TELEPORT = new Location(125682, 52756, -4576);

	public _10956_WeSylphs()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(KERKIR);
		addTalkId(KERKIR, EVIA);
		addKillNpcWithLog(1, NpcString.KILL_MONSTERS_IN_THE_WHISPERING_WOODS.getId(), A_LIST, 30, MOBS);
		addLevelCheck("high_level.htm", 15, 20);
		addClassIdCheck("wrong_class.htm", 208);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "kerkir_q10956_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "teleport":
			{
				player.teleToLocation(TRAINING_GROUNDS_TELEPORT);
				return null;
			}
			case "evia_q10956_02.htm":
			{
				st.addExpAndSp(600_000, 13_500);
				st.giveItems(SOE_NOVICE, 20);
				st.giveItems(SPIRIT_ORE, 50);
				st.giveItems(XP_GROWTH_SCROLL, 1);
				st.giveItems(HEALING_POTION, 50);
				st.giveItems(SOE_BATHIS, 1);
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
						return "kerkir_q10956_01.htm";
					case 1:
						return "kerkir_q10956_04.htm";
				}
				break;
			}
			case EVIA:
			{
				switch (cond)
				{
					case 2:
						return "evia_q10956_01.htm";
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
			qs.setCond(2);
			qs.getPlayer().getInventory().addItem(SOE_EVIA, 1);
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.THE_TRAINING_IS_OVER_USE_A_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_GO_BACK_TO_GROCER_EVIA, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}