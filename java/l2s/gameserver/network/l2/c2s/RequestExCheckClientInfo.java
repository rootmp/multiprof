package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class RequestExCheckClientInfo implements IClientIncomingPacket
{

	private String info;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		info = packet.readSizedString();
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
