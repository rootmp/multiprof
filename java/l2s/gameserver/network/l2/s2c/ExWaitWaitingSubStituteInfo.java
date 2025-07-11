package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExWaitWaitingSubStituteInfo implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket OPEN = new ExWaitWaitingSubStituteInfo(true);
	public static final IClientOutgoingPacket CLOSE = new ExWaitWaitingSubStituteInfo(false);

	private boolean _open;

	public ExWaitWaitingSubStituteInfo(boolean open)
	{
		_open = open;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_open);
		return true;
	}
}