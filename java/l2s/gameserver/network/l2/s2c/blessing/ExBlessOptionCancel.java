package l2s.gameserver.network.l2.s2c.blessing;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExBlessOptionCancel implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExBlessOptionCancel();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}