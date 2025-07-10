package quests;

import l2s.gameserver.geometry.Location;
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
public class _10293_DeathMysteries extends Quest
{
	// Npcs
	private static final int RAYMOND = 30289;
	private static final int MAXIMILIAN = 30120;

	// Items
	private static final int SOE_DEATH_PASS = 95589;
	private static final int SOE_MAXIMILIAN = 95595;
	private static final int MAGIC_LAMP_CHARGING_POTION = 91757;
	private static final int SAYHAS_GUST = 91776;
	private static final int SCROLL_ENCHANT_ADEN_WEAPON = 93038;

	// Location
	private static final Location MAXIMILIAN_LOC = new Location(86868, 148637, -3400);

	// Monsters
	private static final int[] MOBS =
	{
		20176, // Wyrm
		20550, // Guardian Basilisk
		20551, // Road Scavenger
		20552, // Fettered Soul
		20553, // Windsus
		20554 // Grandis
	};

	// Etc
	private static final String A_LIST = "A_LIST";

	public _10293_DeathMysteries()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(RAYMOND);
		addTalkId(RAYMOND, MAXIMILIAN);
		addKillNpcWithLog(2, NpcString.CLEAR_DEATH_PASS.getId(), A_LIST, 100, MOBS);
		addLevelCheck("bad_level.htm", 35);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "raymond_q10293_04.htm":
			{
				st.setCond(1);
				player.sendPacket(new ExShowScreenMessage(NpcString.BEFORE_YOU_GO_FOR_A_BATTLE_CHECK_THE_SKILL_WINDOW_NEW_SKILLS_WILL_HELP_YOU_TO_GET_STRONGER, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				break;
			}
			case "teleport":
			{
				player.teleToLocation(MAXIMILIAN_LOC);
				return null;
			}
			case "maximilian_q10293_04.htm":
			{
				st.setCond(2);
				st.giveItems(SOE_DEATH_PASS, 1);
				break;
			}
			case "maximilian_q10293_07.htm":
			{
				if (player.getLevel() < 40)
				{
					long expAdd = Experience.getExpForLevel(40) - player.getExp();
					st.addExpAndSp(expAdd, 160_000);
				}
				else
				{
					st.addExpAndSp(0, 160_000);
				}
				st.giveItems(MAGIC_LAMP_CHARGING_POTION, 1);
				st.giveItems(SAYHAS_GUST, 9);
				st.giveItems(SCROLL_ENCHANT_ADEN_WEAPON, 2);
				player.checkElementalInfo();
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
			case MAXIMILIAN:
			{
				switch (cond)
				{
					case 1:
					{
						return "maximilian_q10293_01.htm";
					}
					case 2:
					{
						return "maximilian_q10293_05.htm";
					}
					case 3:
					{
						return "maximilian_q10293_06.htm";
					}
				}
				break;
			}
			case RAYMOND:
			{
				switch (cond)
				{
					case 0:
					{
						return "raymond_q10293_01.htm";
					}
					case 1:
					{
						return "raymond_q10293_05.htm";
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
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.MONSTERS_OF_THE_DEATH_PASS_ARE_KILLED_USE_THE_TELEPORT_OR_THE_SCROLL_OF_ESCAPE_TO_GET_TO_HIGH_PRIEST_MAXIMILIAN_IN_GIRAN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.giveItems(SOE_MAXIMILIAN, 1);
			qs.setCond(3);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
