package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public final class ExEnchantOneFail implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExEnchantOneFail();

	public ExEnchantOneFail()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}