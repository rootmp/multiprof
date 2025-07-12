package l2s.gameserver.network.l2.c2s.locked;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExUnlockedItemCancel implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int dummy;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		dummy = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{

	}
}