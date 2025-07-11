package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public final class ExEnchantOneOK implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExEnchantOneOK();

	public ExEnchantOneOK()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}