package l2s.gameserver.network.l2.c2s.quest;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.quest.ExQuestUi;

public class RequestExQuestUi implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		activeChar.sendPacket(new ExQuestUi(activeChar));
	}
}