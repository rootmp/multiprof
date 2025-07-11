package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.GameClient;

public class RequestTutorialClientEvent implements IClientIncomingPacket
{
	// format: cd
	private int _event = 0;

	/**
	 * Пакет от клиента, если вы в туториале подергали мышкой как надо - клиент
	 * пришлет его со значением 1 ну или нужным ивентом
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_event = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		for (QuestState qs : player.getAllQuestsStates())
			qs.getQuest().notifyTutorialEvent("CE", false, String.valueOf(_event), qs);
	}
}