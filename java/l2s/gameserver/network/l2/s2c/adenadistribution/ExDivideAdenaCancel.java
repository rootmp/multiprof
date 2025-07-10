package l2s.gameserver.network.l2.s2c.adenadistribution;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author Sdw
 */
public class ExDivideAdenaCancel implements IClientOutgoingPacket
{
	public static final L2GameServerPacket STATIC = new ExDivideAdenaCancel();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(0x00);
	}
}