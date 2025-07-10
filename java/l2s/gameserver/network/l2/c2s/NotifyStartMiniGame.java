package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


public class NotifyStartMiniGame implements IClientIncomingPacket
{
	@Override
	public void run(GameClient client)
	{
		// just trigger
	}

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}
}