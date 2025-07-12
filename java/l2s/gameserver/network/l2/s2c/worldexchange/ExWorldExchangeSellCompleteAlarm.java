package l2s.gameserver.network.l2.s2c.worldexchange;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExWorldExchangeSellCompleteAlarm implements IClientOutgoingPacket
{
	private final int _itemId;
	private final long _amount;

	public ExWorldExchangeSellCompleteAlarm(int itemId, long amount)
	{
		_itemId = itemId;
		_amount = amount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemId);
		packetWriter.writeQ(_amount);
		return true;
	}
}
