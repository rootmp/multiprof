package l2s.gameserver.network.l2.s2c.enchant;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

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