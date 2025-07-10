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
public class _10982_SpiderHunt extends Quest
{
	// Npcs
	private static final int JACKSON = 30002;
	private static final int BATHIS = 30332;

	// Monsters
	private static final int[] MOBS =
	{
		20103, // Giant Spider
		20106, // Giant Fang Spider
		20108 // Giant Blade Spider
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
	private static final Location TRAINING_GROUNDS_TELEPORT = new Location(-117389, 227211, -2896);

	public _10982_SpiderHunt()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(JACKSON);
		addTalkId(BATHIS);
		addKillNpcWithLog(1, NpcString.HUNT_GIANT_SPIDER.getId(), A_LIST, 30, MOBS);
		addLevelCheck("bad_level.htm", 15, 20);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "jackson_q10982_02.htm":
			{
				st.setCond(1);
				break;
			}
			case "teleport":
			{
				player.teleToLocation(TRAINING_GROUNDS_TELEPORT);
				return null;
			}
			case "bathis_q10982_02.htm":
			{
				st.addExpAndSp(600_000, 13_500);
				st.giveItems(SPIRIT_ORE, 50);
				st.giveItems(SOE_NOVICE, 10);
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
			case JACKSON:
			{
				switch (cond)
				{
					case 0:
					{
						return "jackson_q10982_01.htm";
					}
					case 1:
					{
						return "jackson_q10982_03.htm";
					}
					case 2:
					{
						return "jackson_q10982_04.htm";
					}
				}
				break;
			}
			case BATHIS:
			{
				if (cond == 2)
					return "bathis_q10982_01.htm";
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