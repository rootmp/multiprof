package l2s.gameserver.network.l2.s2c;

import l2s.gameserver.model.items.ItemInfo;

public class TradeOwnAddPacket extends L2GameServerPacket
{
	private final int _type;
	private final ItemInfo _item;
	private final long _amount;

	public TradeOwnAddPacket(int type, ItemInfo item, long amount)
	{
		_type = type;
		_item = item;
		_amount = amount;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(_type);
		writeD(1); // Count
		if (_type == 2)
		{
			writeH(1); // Count
			writeC(0x00); // UNK 140
			writeC(0x00); // UNK 140
			writeItemInfo(_item, _amount);
		}
	}
}