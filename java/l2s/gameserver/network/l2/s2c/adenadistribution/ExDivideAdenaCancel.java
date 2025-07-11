package l2s.gameserver.network.l2.s2c.adenadistribution;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author Sdw
 */
public class ExDivideAdenaCancel implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExDivideAdenaCancel();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(0x00);
		return true;
	}
}