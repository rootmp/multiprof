package l2s.gameserver.network.l2.s2c.worldexchange;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExWorldExchangeSettleRecvResult implements IClientOutgoingPacket
{
	public static final ExWorldExchangeSettleRecvResult FAIL = new ExWorldExchangeSettleRecvResult(-1, -1L, (byte) 0);

	private final int _itemObjectId;
	private final long _itemAmount;
	private final int _type;

	public ExWorldExchangeSettleRecvResult(int itemObjectId, long itemAmount, int type)
	{
		_itemObjectId = itemObjectId;
		_itemAmount = itemAmount;
		_type = type;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemObjectId);
		packetWriter.writeQ(_itemAmount);
		packetWriter.writeC(_type);
		return true;
	}
}
