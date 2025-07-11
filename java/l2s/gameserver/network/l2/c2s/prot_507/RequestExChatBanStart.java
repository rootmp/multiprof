package l2s.gameserver.network.l2.c2s.prot_507;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExChatBanStart implements IClientIncomingPacket
{
	private int remainBanSec;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		remainBanSec = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;
	}
}
