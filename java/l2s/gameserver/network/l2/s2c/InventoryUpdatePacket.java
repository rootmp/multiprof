package l2s.gameserver.network.l2.s2c;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInfo;
import l2s.gameserver.model.items.ItemInstance;

public class InventoryUpdatePacket implements IClientOutgoingPacket
{
	public static final int UNCHANGED = 0;
	public static final int ADDED = 1;
	public static final int MODIFIED = 2;
	public static final int REMOVED = 3;

	public enum EInventoryUpdateType
	{
		IVUT_NONE, //               =0,
		IVUT_ADD, //                =1,
		IVUT_UPDATE, //             =2,
		IVUT_DELETE, //             =3,
		IVUT_MAX,//                =4,
	};

	private final List<ItemInfo> _items = new ArrayList<>(1);
	private final Player _player;
	
	public InventoryUpdatePacket(Player player)
	{
		_player = player;
	}

	public InventoryUpdatePacket addNewItem(ItemInstance item)
	{
		addItem(item).setLastChange(ADDED);
		return this;
	}

	public InventoryUpdatePacket addModifiedItem(ItemInstance item)
	{
		addItem(item).setLastChange(MODIFIED);
		return this;
	}

	public InventoryUpdatePacket addRemovedItem(ItemInstance item)
	{
		addItem(item).setLastChange(REMOVED);
		return this;
	}

	private ItemInfo addItem(ItemInstance item)
	{
		ItemInfo info;
		_items.add(info = new ItemInfo(item, item.getTemplate().isBlocked(_player, item)/*, _player.getSharedGroupReuseTime(item.getTemplate().getReuseGroup())*/));
		return info;
	}

	public List<ItemInfo> getItems()
	{
		return _items;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(0x00); // 140 PROTOCOL
		packetWriter.writeD(_items.size());
		packetWriter.writeD(_items.size());
		for(ItemInfo temp : _items)
		{
			packetWriter.writeH(temp.getLastChange());
			writeItemInfo(packetWriter, temp);
		}
		return true;
	}
}