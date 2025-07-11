package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.GameClient;

public class RequestTutorialLinkHtml implements IClientIncomingPacket
{
	// format: cdS
	private int _unk;
	private String _bypass;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_unk = packet.readD(); // maybe itemId?
		_bypass = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		for (QuestState qs : player.getAllQuestsStates())
			qs.getQuest().notifyTutorialEvent("LINK", false, _bypass, qs);
	}
}