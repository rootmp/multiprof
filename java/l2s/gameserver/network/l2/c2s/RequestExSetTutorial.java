package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.quest.QuestState;

public class RequestExSetTutorial extends L2GameClientPacket
{
	// format: cd
	private int _event = 0;

	/**
	 * Пакет от клиента, если вы в туториале подергали мышкой как надо - клиент
	 * пришлет его со значением 1 ну или нужным ивентом
	 */
	@Override
	protected boolean readImpl()
	{
		_event = readD();
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
			return;

		for (QuestState qs : player.getAllQuestsStates())
			qs.getQuest().notifyTutorialEvent("CE", false, String.valueOf(_event), qs);
	}
}