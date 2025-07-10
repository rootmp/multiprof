package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

public class GMViewItemListPacket implements IClientOutgoingPacket
{
	private final int _type;
	private int _size;
	private ItemInstance[] _items;
	private int _limit;
	private String _name;
	private Player _player;

	public GMViewItemListPacket(int type, Player cha, ItemInstance[] items, int size)
	{
		_type = type;
		_size = size;
		_items = items;
		_name = cha.getName();
		_limit = cha.getInventoryLimit();
		_player = cha;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_type);
		if (_type == 1)
		{
			packetWriter.writeS(_name);
			packetWriter.writeD(_limit);
			packetWriter.writeD(_size);
		}
		else if (_type == 2)
		{
			packetWriter.writeD(_size);
			packetWriter.writeD(_size);
			for (ItemInstance temp : _items)
			{
				if (!temp.getTemplate().isQuest())
					writeItemInfo(_player, temp);
			}
		}
	}
}