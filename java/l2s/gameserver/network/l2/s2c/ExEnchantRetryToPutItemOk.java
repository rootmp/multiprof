package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExEnchantRetryToPutItemOk implements IClientOutgoingPacket
{
	public static final ExEnchantRetryToPutItemOk STATIC_PACKET = new ExEnchantRetryToPutItemOk();

	private ExEnchantRetryToPutItemOk()
	{}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}