package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExCuriousHouseObserveMode implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket ENTER = new ExCuriousHouseObserveMode(false);
	public static final IClientOutgoingPacket LEAVE = new ExCuriousHouseObserveMode(true);

	private final boolean _leave;

	public ExCuriousHouseObserveMode(boolean leave)
	{
		_leave = leave;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_leave);
		return true;
	}
}
