package quests;

import l2s.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.model.base.ClassType;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.components.NpcString;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;

/**
 * @author nexvill
 */
public class _10957_TheLifeOfADeathKnight extends Quest
{
	private class PlayerEnterListener implements OnPlayerEnterListener
	{
		@Override
		public void onPlayerEnter(Player player)
		{
			if ((player.getClassId().isOfType(ClassType.DEATH_KNIGHT)) && (player.getLevel() < 5))
			{
				QuestState st = player.getQuestState(_10957_TheLifeOfADeathKnight.this);
				if (st == null)
				{
					newQuestState(player);
					st = player.getQuestState(_10957_TheLifeOfADeathKnight.this);
					st.startQuestTimer("questStart", 10000L);
				}
			}
		}
	}

	// Npc
	private static final int KILREMANGE = 34138;

	// Monsters
	private static final int TRAINING_DUMMY = 22183;

	// Items
	private static final int SOULSHOTS = 91927;
	private static final int HEALING_POTIONS = 91912;
	private static final int NOVICE_WIND_WALK_POTION = 49036;
	private static final int SOE_NOVICE = 10650;

	private final OnPlayerEnterListener _playerEnterListener = new PlayerEnterListener();

	public _10957_TheLifeOfADeathKnight()
	{
		super(PARTY_ONE, ONETIME);
		addStartNpc(KILREMANGE);
		addTalkId(KILREMANGE);
		addKillId(TRAINING_DUMMY);
		addLevelCheck("high_level.htm", 1, 2);
		addClassIdCheck("wrong_class.htm", 196, 200, 204);

		CharListenerList.addGlobal(_playerEnterListener);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		switch (event)
		{
			case "questStart":
			{
				st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.SPEAK_TO_HEAD_TRAINER_KILREMANGE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
				return null;
			}
			case "kilremange_q10957_04.htm":
			{
				st.setCond(1);
				break;
			}
			case "kilremange_q10957_07.htm":
			{
				st.setCond(3);
				st.giveItems(SOULSHOTS, 200);
				break;
			}
			case "kilremange_q10957_10.htm":
			{
				st.addExpAndSp(224, 3);
				st.giveItems(SOE_NOVICE, 5);
				st.giveItems(SOULSHOTS, 200);
				st.giveItems(HEALING_POTIONS, 50);
				st.giveItems(NOVICE_WIND_WALK_POTION, 5);
				giveStoryBuff(npc, st.getPlayer());
				SkillEntry skillEntry = SkillEntry.makeSkillEntry(SkillEntryType.NONE, 48057, 1);
				npc.forceUseSkill(skillEntry, st.getPlayer());
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
			case KILREMANGE:
			{
				switch (cond)
				{
					case 0:
						return "kilremange_q10957_01.htm";
					case 1:
						return "kilremange_q10957_05.htm";
					case 2:
						return "kilremange_q10957_06.htm";
					case 3:
						return "kilremange_q10957_08.htm";
					case 4:
						return "kilremange_q10957_09.htm";
				}
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
