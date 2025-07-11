package l2s.gameserver.network.l2.s2c.enchant;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExResultSetMultiEnchantItemList implements IClientOutgoingPacket
{
	private final int _resultType;

	public ExResultSetMultiEnchantItemList(int resultType)
	{
		_resultType = resultType;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_resultType);
		return true;
	}
}