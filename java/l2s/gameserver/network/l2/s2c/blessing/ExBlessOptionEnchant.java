package l2s.gameserver.network.l2.s2c.blessing;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExBlessOptionEnchant implements IClientOutgoingPacket
{
	private final boolean _success;

	public ExBlessOptionEnchant(boolean success)
	{
		_success = success;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_success); // success or fail
		return true;
	}
}