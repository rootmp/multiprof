package l2s.gameserver.network.l2.s2c.blessing;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExBlessOptionCancel implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExBlessOptionCancel();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}