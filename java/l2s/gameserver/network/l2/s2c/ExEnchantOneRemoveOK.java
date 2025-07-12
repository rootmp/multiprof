package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
**/
public final class ExEnchantOneRemoveOK implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExEnchantOneRemoveOK();

	public ExEnchantOneRemoveOK()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		//
		return true;
	}
}