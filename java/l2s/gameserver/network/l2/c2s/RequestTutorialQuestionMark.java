package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubEventManager;
import l2s.gameserver.model.quest.QuestState;

public class RequestTutorialQuestionMark implements IClientIncomingPacket
{
	// format: ccd
	private boolean _quest = false;
	private int _tutorialId = 0;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_quest = readC() > 0;
		_tutorialId = packet.readD(); // Type
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
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