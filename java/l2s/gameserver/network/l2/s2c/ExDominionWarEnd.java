package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 * @date 12:11/05.03.2011
 */
public class ExDominionWarEnd implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExDominionWarEnd();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}
