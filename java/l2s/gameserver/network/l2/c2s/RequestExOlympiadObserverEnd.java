package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

/**
 * format ch c: (id) 0xD0 h: (subid) 0x2F
 */
public class RequestExOlympiadObserverEnd implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		// ignored
	}
}