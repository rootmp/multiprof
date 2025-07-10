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
public class _10962_NewHorizons extends Quest
{
	// Npcs
	private static final int REAHEN = 34111;
	private static final int BATHIS = 30332;

	// Monsters
	private static final int[] MOBS =
	{
		21985, // Mountain Werewolf
		21986, // Mountain Fungus
		21987, // Muertos Fighter
		21988 // Muertos Captain
	};

	// Items
	private static final int SOE_BATHIS = 91651;
	private static final int SPIRIT_ORE = 3031;
	private static final int SOE_NOVICE = 10650;
	private static final int XP_GROWTH_SCROLL = 49674;
	private static final int HP_POTION = 91912;

	// Etc
	private static final String A_LIST = "A_LIST";

	// Location
	private static final Location TRAINING_GROUNDS_TELEPORT = new Location(-107812, 47531, -1448);

	public _10962_NewHorizons()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(REAHEN);
		addTalkId(BATHIS);
		addKillNpcWithLog(1, NpcString.CLEAR_HILLS_OF_GOLD.getId(), A_LIST, 30, MOBS);
		addLevelCheck("bad_level.htm", 15, 20);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "reahen_q10962_02.htm":
			{
				st.setCond(1);
				break;
			}
			case "teleport":
			{
				player.teleToLocation(TRAINING_GROUNDS_TELEPORT);
				return null;
			}
			case "bathis_q10962_02.htm":
			{
				st.addExpAndSp(600_000, 13_500);
				st.giveItems(SPIRIT_ORE, 50);
				st.giveItems(SOE_NOVICE, 20);
				st.giveItems(XP_GROWTH_SCROLL, 1);
				st.giveItems(HP_POTION, 50);
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
			case REAHEN:
			{
				switch (cond)
				{
					case 0:
					{
						return "reahen_q10962_01.htm";
					}
					case 1:
					{
						return "reahen_q10962_03.htm";
					}
					case 2:
					{
						return "reahen_q10962_04.htm";
					}
				}
				break;
			}
			case BATHIS:
			{
				if (cond == 2)
				{
					return "bathis_q10962_01.htm";
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
			qs.giveItems(SOE_BATHIS, 1);
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.YOUVE_KILLED_ALL_THE_MONSTERS_USE_THE_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_RETURN_TO_CAPTAIN_BATHIS_IN_GLUDIO, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}