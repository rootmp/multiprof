package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import org.napile.primitive.pair.IntObjectPair;

import l2s.gameserver.listener.actor.player.OnAnswerListener;
import l2s.gameserver.model.Player;

public class ConfirmDlg implements IClientIncomingPacket
{
	private int _answer, _requestId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		packet.readD();
		_answer = packet.readD();
		_requestId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		IntObjectPair<OnAnswerListener> entry = activeChar.getAskListener(true);
		if (entry == null || entry.getKey() != _requestId)
			return;

		OnAnswerListener listener = entry.getValue();
		if (_answer == 1)
			listener.sayYes();
		else
			listener.sayNo();
	}
}