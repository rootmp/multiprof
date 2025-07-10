package quests;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;

/**
 * @author nexvill
 */
public class _10954_SayhasChildren extends Quest
{
	// Npc
	private static final int ANDRA = 34209;
	private static final int KERKIR = 34210;

	// Monsters
	private static final int TRAINING_DUMMY = 22324;

	// Items
	private static final int SOULSHOTS = 91927;
	private static final int HEALING_POTIONS = 91912;
	private static final int NOVICE_WIND_WALK_POTION = 49036;
	private static final int SOE_NOVICE = 10650;

	// Location
	private static final Location TRAINING_GROUND = new Location(104348, 43601, -4656);

	public _10954_SayhasChildren()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(ANDRA);
		addTalkId(ANDRA, KERKIR);
		addKillId(TRAINING_DUMMY);
		addLevelCheck("high_level.htm", 1, 2);
		addClassIdCheck("wrong_class.htm", 208);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "andra_q10954_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "teleport":
			{
				player.teleToLocation(TRAINING_GROUND);
				return null;
			}
			case "kerkir_q10954_03.htm":
			{
				st.setCond(3);
				st.giveItems(SOULSHOTS, 200);
				break;
			}
			case "kerkir_q10954_06.htm":
			{
				st.addExpAndSp(224, 4);
				st.giveItems(SOE_NOVICE, 5);
				st.giveItems(SOULSHOTS, 400);
				st.giveItems(HEALING_POTIONS, 50);
				st.giveItems(NOVICE_WIND_WALK_POTION, 5);
				giveStoryBuff(npc, player);
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
			case ANDRA:
			{
				switch (cond)
				{
					case 0:
						return "andra_q10954_01.htm";
					case 1:
						return "andra_q10954_05.htm";
				}
				break;
			}
			case KERKIR:
			{
				switch (cond)
				{
					case 1:
						return "kerkir_q10954_01.htm";
					case 2:
						return "kerkir_q10954_02.htm";
					case 3:
						return "kerkir_q10954_04.htm";
					case 4:
						return "kerkir_q10954_05.htm";
				}
			}
		}
		return NO_QUEST_DIALOG;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		Player player = qs.getPlayer();
		if (qs.getCond() == 1)
		{
			qs.setCond(2);
		}
		else if (qs.getCond() == 3 && player.getChargedSoulshotPower() != 0)
		{
			qs.setCond(4);
		}
		return null;
	}
}
