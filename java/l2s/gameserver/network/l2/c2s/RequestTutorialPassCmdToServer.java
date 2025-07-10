package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubEventManager;
import l2s.gameserver.model.quest.QuestState;

public class RequestTutorialPassCmdToServer extends L2GameClientPacket
{
	// format: cS
	private String _bypass = null;

	@Override
	protected boolean readImpl()
	{
		_bypass = readS();
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
			FightClubEventManager.getInstance().requestEventPlayerMenuBypass(player, this._bypass);
			return;
		}
		for (QuestState qs : player.getAllQuestsStates())
		{
			qs.getQuest().notifyTutorialEvent("BYPASS", false, _bypass, qs);
		}
	}
}