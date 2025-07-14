package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

public class RequestExCleftEnter implements IClientIncomingPacket
{
	private int unk;

	/**
	 * format: d
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		unk = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		// TODO not implemented
	}
}