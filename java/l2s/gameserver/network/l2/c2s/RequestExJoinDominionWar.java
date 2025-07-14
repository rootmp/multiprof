package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

public class RequestExJoinDominionWar implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		packet.readD();
		packet.readD();
		packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		//
	}
}