package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

public class RequestSEKCustom implements IClientIncomingPacket
{
	private int SlotNum, Direction;

	/**
	 * format: dd
	 */
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		SlotNum = packet.readD();
		Direction = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		// TODO not implemented
	}
}