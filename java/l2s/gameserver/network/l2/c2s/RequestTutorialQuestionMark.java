package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubEventManager;
import l2s.gameserver.model.quest.QuestState;

public class RequestTutorialQuestionMark extends L2GameClientPacket
{
	// format: ccd
	private boolean _quest = false;
	private int _tutorialId = 0;

	@Override
	protected boolean readImpl()
	{
		_quest = readC() > 0;
		_tutorialId = readD(); // Type
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}

		player.isntAfk();
		if (player.isInFightClub())
		{
			FightClubEventManager.getInstance().sendEventPlayerMenu(player);
			return;
		}
		for (QuestState qs : player.getAllQuestsStates())
		{
			qs.getQuest().notifyTutorialEvent("QM", _quest, String.valueOf(_tutorialId), qs);
		}
	}
}