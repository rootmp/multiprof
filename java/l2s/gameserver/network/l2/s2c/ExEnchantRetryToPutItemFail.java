package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExEnchantRetryToPutItemFail implements IClientOutgoingPacket
{
	public static final ExEnchantRetryToPutItemFail STATIC_PACKET = new ExEnchantRetryToPutItemFail();

	public ExEnchantRetryToPutItemFail()
	{

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}