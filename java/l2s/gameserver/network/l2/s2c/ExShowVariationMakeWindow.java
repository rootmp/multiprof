package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * Открывает окно аугмента, название от фонаря.
 */
public class ExShowVariationMakeWindow implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket STATIC = new ExShowVariationMakeWindow();

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		return true;
	}
}