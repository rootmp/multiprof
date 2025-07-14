package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.items.ItemInfo;

public class TradeUpdatePacket implements IClientOutgoingPacket
{
	private final int _type;
	private final ItemInfo _item;
	private final long _amount;

	public TradeUpdatePacket(int type, ItemInfo item, long amount)
	{
		_type = type;
		_item = item;
		_amount = amount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_type);
		packetWriter.writeD(1); // Count
		if(_type == 2)
		{
			packetWriter.writeH(1); // Count
			packetWriter.writeC(0x00); // UNK 140
			packetWriter.writeC(0x00); // UNK 140
			packetWriter.writeH(_amount > 0 && _item.getItem().isStackable() ? 3 : 2);
			writeItemInfo(packetWriter, _item, _amount);
		}
		return true;
	}
}