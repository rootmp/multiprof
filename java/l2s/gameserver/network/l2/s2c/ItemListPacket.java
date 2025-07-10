package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.enums.LockType;
import l2s.gameserver.model.items.ItemInstance;

public class ItemListPacket implements IClientOutgoingPacket
{
	private Player _player;
	private final int _type;
	private final int _size;
	private final ItemInstance[] _items;
	
	@SuppressWarnings("unused")
	private LockType _lockType;
	@SuppressWarnings("unused")
	private int[] _lockItems;

	/**
	 * ItemListPacket method
	 * 
	 * @param type
	 * @param player
	 * @param size
	 * @param items
	 * @param showWindow
	 * @param lockType
	 * @param lockItems
	 */
	public ItemListPacket(int type, Player player, int size, ItemInstance[] items, boolean showWindow, LockType lockType, int[] lockItems)
	{
		_type = type;
		_player = player;
		_size = size;
		_items = items;
		_lockType = lockType;
		_lockItems = lockItems;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if (_type == 2)
		{
			packetWriter.writeC(_type);
			packetWriter.writeD(_size); // Total items
			packetWriter.writeD(_size); // Items in this page
			for (ItemInstance temp : _items)
			{
				if (temp.getTemplate().isQuest())
				{
					continue;
				}
				writeItemInfo(_player, temp);
			}
		}
		else
		{
			packetWriter.writeH(0x01); // _showWindow ? 1 : 0
			packetWriter.writeH(0x00);
			packetWriter.writeD(_size); // Total items
		}
	}
}