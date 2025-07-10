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
public class _10959_ChallengingYourDestiny extends Quest
{
	// Npcs
	private static final int MATHORN = 34139;

	// Monsters
	private static final int[] MOBS =
	{
		22186, // Skeleton Scout
		22187 // Skeleton Hunter
	};

	// Items
	private static final int SOE_NOVICE = 10650;
	private static final int SPIRIT_ORE = 3031;
	private static final int XP_GROWTH_SCROLL = 49674;
	private static final int HEALING_POTION = 91912;
	private static final int SOE_MATHORN = 93319;

	// Etc
	private static final String A_LIST = "A_LIST";

	// Location
	private static final Location TRAINING_GROUNDS_TELEPORT = new Location(44636, 141740, -3024);

	public _10959_ChallengingYourDestiny()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(MATHORN);
		addTalkId(MATHORN);
		addKillNpcWithLog(1, NpcString.CLEAR_TRAINING_GROUNDS_II.getId(), A_LIST, 30, MOBS);
		addLevelCheck("high_level.htm", 15, 20);
		addClassIdCheck("wrong_class.htm", 196, 200, 204);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();

		switch (event)
		{
			case "mathorn_q10959_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "teleport":
			{
				player.teleToLocation(TRAINING_GROUNDS_TELEPORT);
				return null;
			}
			case "mathorn_q10959_07.htm":
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.YOUVE_FINISHED_THE_TUTORIAL_TAKE_YOUR_FIRST_CLASS_TRANSFER_AND_COMPLETE_YOUR_TRAINING_WITH_MATHORN_TO_BECOME_STRONGER, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
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
		int npcId = npc.getNpcId();
		int cond = st.getCond();

		switch (npcId)
		{
			case MATHORN:
			{
				switch (cond)
				{
					case 0:
						return "mathorn_q10959_01.htm";
					case 1:
						return "mathorn_q10959_05.htm";
					case 2:
						return "mathorn_q10959_06.htm";
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
			qs.getPlayer().getInventory().addItem(SOE_MATHORN, 1);
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.THE_TRAINING_IN_OVER_USE_A_SCROLL_OF_ESCAPE_IN_YOUR_INVENTORY_TO_GO_BACK_TO_QUARTERMASTER_MATHORN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}