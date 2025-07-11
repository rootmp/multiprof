package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class AnswerPartyLootModification implements IClientIncomingPacket
{
	public int _answer;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_answer = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		Party party = activeChar.getParty();
		if (party != null)
			party.answerLootChangeRequest(activeChar, _answer == 1);
	}
}
