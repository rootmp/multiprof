package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExWaitWaitingSubStituteInfo implements IClientOutgoingPacket
{
	public static final L2GameServerPacket OPEN = new ExWaitWaitingSubStituteInfo(true);
	public static final L2GameServerPacket CLOSE = new ExWaitWaitingSubStituteInfo(false);

	private boolean _open;

	public ExWaitWaitingSubStituteInfo(boolean open)
	{
		_open = open;
	}

	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_open);
	}
}