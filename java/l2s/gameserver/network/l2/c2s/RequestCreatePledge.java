package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

public class RequestCreatePledge implements IClientIncomingPacket
{
	// Format: cS
	private String _pledgename;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_pledgename = packet.readS(64);
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		// TODO not implemented
	}
}