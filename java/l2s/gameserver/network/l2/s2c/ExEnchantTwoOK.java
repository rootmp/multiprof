package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public final class ExEnchantTwoOK implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExEnchantTwoOK();

	public ExEnchantTwoOK()
	{
		//
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}