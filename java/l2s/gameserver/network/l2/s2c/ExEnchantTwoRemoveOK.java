package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public final class ExEnchantTwoRemoveOK implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExEnchantTwoRemoveOK();

	public ExEnchantTwoRemoveOK()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}