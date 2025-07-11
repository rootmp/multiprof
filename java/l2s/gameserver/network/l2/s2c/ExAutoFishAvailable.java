package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public final class ExAutoFishAvailable implements IClientOutgoingPacket
{
	public static final L2GameServerPacket REMOVE = new ExAutoFishAvailable(0);
	public static final L2GameServerPacket SHOW = new ExAutoFishAvailable(1);
	public static final L2GameServerPacket FISHING = new ExAutoFishAvailable(2);

	private final int _type;

	private ExAutoFishAvailable(int type)
	{
		_type = type;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_type);
		return true;
	}
}