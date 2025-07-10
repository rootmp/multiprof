package npc.model;

import l2s.commons.collections.MultiValueSet;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.templates.npc.NpcTemplate;

/**
 * @author Bonux
 **/
public class SoulTrackerInstance extends NpcInstance
{
	public SoulTrackerInstance(int objectId, NpcTemplate template, MultiValueSet<String> set)
	{
		super(objectId, template, set);
	}

	@Override
	public void onMenuSelect(Player player, int ask, long reply, int state)
	{
		if (ask == 933)
		{
			if (reply == 5) // Переместиться в Западное Крыло Тюрьмы Бездны
			{
				QuestState qs = player.getQuestState(933);
				if (getNpcId() == 31774)
				{
					if (qs != null && qs.isStarted())
						player.teleToLocation(-119440, -182464, -6752, ReflectionManager.MAIN);
					else
						showChatWindow(player, "default/" + getNpcId() + "-no_enter.htm", false);
				}
				else if (getNpcId() == 31775)
				{
					if (qs != null && qs.isStarted())
						player.teleToLocation(-119330, -179608, -6752, ReflectionManager.MAIN);
					else
						showChatWindow(player, "default/" + getNpcId() + "-no_enter.htm", false);
				}
			}
			else if (reply == 6) // Переместиться ко N-му входу в Тюрьму Бездны
			{
				if (getNpcId() == 31774)
					player.teleToLocation(-120313, -179623, -6752, ReflectionManager.MAIN);
				else if (getNpcId() == 31775)
					player.teleToLocation(-120313, -182464, -6752, ReflectionManager.MAIN);
			}
			else if (reply == 7) // Переместиться в Тюрму Смертников Вечности
			{
				// TODO: Реализовать.
				showChatWindow(player, "default/" + getNpcId() + "-no_key.htm", false);
			}
			else if (reply == 8) // Вернуться в Аден
			{
				player.teleToLocation(146945, 26764, -2200, ReflectionManager.MAIN);
			}
		}
		else if (ask == 935)
		{
			if (reply == 5) // Переместиться в Восточное Крыло Тюрьмы Бездны
			{
				QuestState qs = player.getQuestState(935);
				if (getNpcId() == 31776)
				{
					if (qs != null && qs.isStarted())
						player.teleToLocation(-110000, -180552, -6752, ReflectionManager.MAIN);
					else
						showChatWindow(player, "default/" + getNpcId() + "-no_enter.htm", false);
				}
				else if (getNpcId() == 31777)
				{
					if (qs != null && qs.isStarted())
						player.teleToLocation(-110000, -177720, -6752, ReflectionManager.MAIN);
					else
						showChatWindow(player, "default/" + getNpcId() + "-no_enter.htm", false);
				}
			}
			else if (reply == 6) // Переместиться ко N-му входу в Тюрьму Бездны
			{
				if (getNpcId() == 31776)
					player.teleToLocation(-109395, -177745, -6752, ReflectionManager.MAIN);
				else if (getNpcId() == 31777)
					player.teleToLocation(-109334, -180564, -6752, ReflectionManager.MAIN);
			}
			else if (reply == 7) // Переместиться в Тюрму Смертников Вечности
			{
				// TODO: Реализовать.
				showChatWindow(player, "default/" + getNpcId() + "-no_key.htm", false);
			}
			else if (reply == 8) // Вернуться в Аден
			{
				player.teleToLocation(146945, 26764, -2200, ReflectionManager.MAIN);
			}
		}
		else
			super.onMenuSelect(player, ask, reply, state);
	}
}