package l2s.gameserver.network.l2.s2c.worldexchange;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExWorldExchangeBuyItem implements IClientOutgoingPacket
{
	public static final ExWorldExchangeBuyItem FAIL = new ExWorldExchangeBuyItem(-1, -1L, (byte) 0);

	private final int _itemObjectId;
	private final long _itemAmount;
	private final byte _type;

	public ExWorldExchangeBuyItem(int itemObjectId, long itemAmount, byte type)
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
