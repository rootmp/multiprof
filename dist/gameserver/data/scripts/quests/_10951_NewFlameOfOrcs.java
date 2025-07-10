package quests;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.model.base.ClassType;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;

/**
 * @author nexvill
 */
public class _10951_NewFlameOfOrcs extends Quest
{
	private class PlayerEnterListener implements OnPlayerEnterListener
	{
		@Override
		public void onPlayerEnter(Player player)
		{
			if ((player.getClassId().isOfType(ClassType.VANGUARD_RIDER)) && (player.getLevel() < 5))
			{
				QuestState st = player.getQuestState(_10951_NewFlameOfOrcs.this);
				if (st == null)
				{
					newQuestState(player);
					st = player.getQuestState(_10951_NewFlameOfOrcs.this);
					st.startQuestTimer("questStart", 10000L);
				}
			}
		}
	}

	// Npc
	private static final int SABITUS = 34335;
	private static final int TANAI = 30602;

	// Monsters
	private static final int TRAINING_DUMMY = 22183;

	// Items
	private static final int SOULSHOTS = 91927;
	private static final int HEALING_POTIONS = 91912;
	private static final int NOVICE_WIND_WALK_POTION = 49036;
	private static final int SOE_NOVICE = 10650;

	// Location
	private static final Location TELEPORT_TO_TANAI = new Location(-45079, -113511, -208);

	private final OnPlayerEnterListener _playerEnterListener = new PlayerEnterListener();

	public _10951_NewFlameOfOrcs()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(SABITUS);
		addTalkId(SABITUS, TANAI);
		addKillId(TRAINING_DUMMY);
		addLevelCheck("high_level.htm", 1, 2);
		addClassIdCheck("wrong_class.htm", 217);

		CharListenerList.addGlobal(_playerEnterListener);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "questStart":
			{
				st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.TALK_TO_FIRST_VANGUARD_RIDER_SABITUS, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				return null;
			}
			case "sabitus_q10951_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "sabitus_q10951_07.htm":
			{
				st.setCond(3);
				st.giveItems(SOULSHOTS, 200);
				break;
			}
			case "sabitus_q10951_10.htm":
			{
				st.getPlayer().setCurrentBp(st.getPlayer().getMaxBp());
				st.setCond(5);
				break;
			}
			case "teleport":
			{
				st.getPlayer().teleToLocation(TELEPORT_TO_TANAI);
				return null;
			}
			case "tanai_q10951_02.htm":
			{
				st.addExpAndSp(224, 3);
				st.giveItems(SOE_NOVICE, 5);
				st.giveItems(SOULSHOTS, 200);
				st.giveItems(HEALING_POTIONS, 50);
				st.giveItems(NOVICE_WIND_WALK_POTION, 5);
				giveStoryBuff(npc, st.getPlayer());
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
			case SABITUS:
			{
				switch (cond)
				{
					case 0:
						return "sabitus_q10951_01.htm";
					case 1:
						return "sabitus_q10951_05.htm";
					case 2:
						return "sabitus_q10951_06.htm";
					case 3:
						return "sabitus_q10951_08.htm";
					case 4:
						return "sabitus_q10951_09.htm";
					case 5:
						return "sabitus_q10951_10.htm";
				}
				break;
			}
			case TANAI:
			{
				if (cond == 5)
					return "tanai_q10951_01.htm";
				break;
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
