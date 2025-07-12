package l2s.gameserver.network.l2.s2c.worldexchange;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.instancemanager.WorldExchangeManager;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExWorldExchangeAveragePrice implements IClientOutgoingPacket
{
	private final int _itemId;
	private final long _averagePrice;

	public ExWorldExchangeAveragePrice(int itemId)
	{
		_itemId = itemId;
		_averagePrice = WorldExchangeManager.getInstance().getAveragePriceOfItem(itemId);
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemId);
		packetWriter.writeQ(_averagePrice);
		return true;
	}
}
