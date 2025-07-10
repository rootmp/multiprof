package quests;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassLevel;
import l2s.gameserver.model.base.ClassType;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10290_ATripBegins extends Quest
{
	// Npcs
	private static final int BATHIS = 30332;
	private static final int BELLA = 30256;
	private static final int EVIA = 34211;
	private static final int MELLOS = 34213;
	private static final int MATHORN = 34139;
	private static final int MARILLIA = 34140;
	private static final int GANTAKI_ZU_URUTU = 30587;

	// Monsters
	private static final int[] MOBS =
	{
		20050, // Poisonous Thornleg
		20051, // Skeleton Bowman
		20054, // Ruin Spartoi
		20060, // Raging Spartoi
		20062, // Tumran Bugbear
		20064 // Tumran Bugbear Warrior
	};

	// Items
	private static final int SOE_RUINS_OF_AGONY = 91727;
	private static final int SOE_BATHIS = 91651;
	private static final int BLESSED_SOE = 91689;
	private static final int ADVENTURERS_TALISMAN = 91937;
	private static final int ADVENTURER_BRACELET = 91934;
	private static final int SCROLL_ENCHANT_ADEN_WEAPON = 93038;

	// Etc
	private static final String A_LIST = "A_LIST";

	// Location
	private static final Location GLUDIO_GK = new Location(-14489, 123974, -3128);

	public _10290_ATripBegins()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(BATHIS, EVIA, MATHORN, GANTAKI_ZU_URUTU);
		addTalkId(BELLA, BATHIS, EVIA, MELLOS, MATHORN, MARILLIA, GANTAKI_ZU_URUTU);
		addKillNpcWithLog(2, NpcString.KILL_MONSTERS_IN_THE_RUINS_OF_AGONY.getId(), A_LIST, 40, MOBS);
		addLevelCheck("bad_level.htm", 20);
		addQuestItem(SOE_RUINS_OF_AGONY);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "bathis_q10290_04.htm":
			case "evia_q10290_05.htm":
			case "mathorn_q10290_05.htm":
			case "gantaki_zu_urutu_q10290_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "teleport":
			{
				player.teleToLocation(GLUDIO_GK);
				return null;
			}
			case "bella_q10290_04.htm":
			case "mellos_q10290_04.htm":
			case "marillia_q10290_04.htm":
			{
				st.setCond(2);
				player.sendPacket(new ExShowScreenMessage(NpcString.BEFORE_YOU_GO_FOR_A_BATTLE_CHECK_THE_SKILL_WINDOW_NEW_SKILLS_WILL_HELP_YOU_TO_GET_STRONGER, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				st.giveItems(SOE_RUINS_OF_AGONY, 1);
				break;
			}
			case "bathis_q10290_08.htm":
			{
				if (player.getLevel() < 25)
				{
					long expAdd = Experience.getExpForLevel(25) - player.getExp();
					st.addExpAndSp(expAdd, 42_000);
				}
				else
				{
					st.addExpAndSp(0, 42_000);
				}
				st.giveItems(BLESSED_SOE, 10);
				st.giveItems(ADVENTURERS_TALISMAN, 1);
				st.giveItems(ADVENTURER_BRACELET, 1);
				st.giveItems(SCROLL_ENCHANT_ADEN_WEAPON, 2);
				player.sendPacket(new ExShowScreenMessage(NpcString.YOUVE_GOT_ADVENTURERS_BRACELET_AND_ADVENTURERS_TALISMAN_COMPLETE_THE_TUTORIAL_AND_TRY_TO_USE_THE_TALISMAN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
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
			case BELLA:
			{
				switch (cond)
				{
					case 1:
					{
						if (st.getPlayer().getRace() == Race.SYLPH)
						{
							return "bella_q10290_01_sylph.htm";
						}
						else if (st.getPlayer().getClassId().isOfType(ClassType.DEATH_KNIGHT))
						{
							return "bella_q10290_01_death_knight.htm";
						}
						return "bella_q10290_01.htm";
					}
					case 2:
					{
						return "bella_q10290_05.htm";
					}
					case 3:
					{
						return "bella_q10290_06.htm";
					}
				}
				break;
			}
			case BATHIS:
			{
				switch (cond)
				{
					case 0:
					{
						if (st.getPlayer().getClassLevel() == ClassLevel.NONE)
						{
							return "bathis_q10290_01_noproof.htm";
						}
						st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.CHECK_YOUR_INVENTORY_AND_EQUIP_YOUR_WEAPON, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
						return "bathis_q10290_01.htm";
					}
					case 1:
					{
						return "bathis_q10290_05.htm";
					}
					case 2:
					{
						return "bathis_q10290_06.htm";
					}
					case 3:
					{
						return "bathis_q10290_07.htm";
					}
				}
				break;
			}
			case EVIA:
			{
				switch (cond)
				{
					case 0:
					{
						if (st.getPlayer().getRace() == Race.SYLPH)
						{
							if (st.getPlayer().getClassLevel() == ClassLevel.NONE)
							{
								return "evia_q10290_01.htm";
							}
							else
							{
								st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.CHECK_YOUR_INVENTORY_AND_EQUIP_YOUR_WEAPON, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
								return "evia_q10290_02.htm";
							}
						}
						break;
					}
					case 1:
					{
						return "evia_q10290_06.htm";
					}
					case 2:
					{
						return "evia_q10290_07.htm";
					}
					case 3:
					{
						return "evia_q10290_08.htm";
					}
				}
				break;
			}
			case MELLOS:
			{
				switch (cond)
				{
					case 1:
					{
						return "mellos_q10290_01.htm";
					}
					case 2:
					{
						return "mellos_q10290_05.htm";
					}
					case 3:
					{
						return "mellos_q10290_06.htm";
					}
				}
				break;
			}
			case MATHORN:
			{
				switch (cond)
				{
					case 0:
					{
						if (st.getPlayer().getClassLevel() == ClassLevel.NONE)
						{
							return "mathorn_q10290_02.htm";
						}
						else
						{
							st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.CHECK_YOUR_INVENTORY_AND_EQUIP_YOUR_WEAPON, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
							return "mathorn_q10290_01.htm";
						}
					}
					case 1:
					{
						return "mathorn_q10290_06.htm";
					}
					case 2:
					{
						return "mathorn_q10290_07.htm";
					}
					case 3:
					{
						return "mathorn_q10290_08.htm";
					}
				}
				break;
			}
			case GANTAKI_ZU_URUTU:
			{
				if (cond == 0)
					return "gantaki_zu_urutu_q10290_01.htm";
				else if (cond == 1)
					return "gantaki_zu_urutu_q10290_04.htm";
				break;
			}
			case MARILLIA:
			{
				switch (cond)
				{
					case 1:
					{
						return "marillia_q10290_01.htm";
					}
					case 2:
					{
						return "marillia_q10290_05.htm";
					}
					case 3:
					{
						return "marillia_q10290_06.htm";
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
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.YOUVE_MADE_THE_FIRST_STEPS_ON_THE_ADVENTURERS_PATH_RETURN_TO_BATHIS_TO_GET_YOUR_REWARD, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
			qs.giveItems(SOE_BATHIS, 1);
			qs.setCond(3);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
