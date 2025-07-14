package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

/**
 * format: chS
 */
public class RequestPCCafeCouponUse implements IClientIncomingPacket
{
	// format: (ch)S
	private String _unknown;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_unknown = packet.readS();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		// TODO not implemented
	}
}