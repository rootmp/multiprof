package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInfo;
import l2s.gameserver.model.items.ItemInstance;

/**
 * @author Hl4p3x
 */
public class InventoryUpdatePacket implements IClientOutgoingPacket
{
	public static final int UNCHANGED = 0;
	public static final int ADDED = 1;
	public static final int MODIFIED = 2;
	public static final int REMOVED = 3;
	private final List<ItemInfo> _items = new ArrayList<>(1);

	public InventoryUpdatePacket addNewItem(Player player, ItemInstance item)
	{
		addItem(player, item).setLastChange(ADDED);
		return this;
	}

	public InventoryUpdatePacket addModifiedItem(Player player, ItemInstance item)
	{
		addItem(player, item).setLastChange(MODIFIED);
		return this;
	}

	public InventoryUpdatePacket addRemovedItem(Player player, ItemInstance item)
	{
		addItem(player, item).setLastChange(REMOVED);
		return this;
	}

	private ItemInfo addItem(Player player, ItemInstance item)
	{
		ItemInfo info;
		_items.add(info = new ItemInfo(item, item.getTemplate().isBlocked(player, item)));
		return info;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// 140 PROTOCOL
		packetWriter.writeC(0x00);
		packetWriter.writeD(0x00);
		packetWriter.writeD(_items.size());
		for (ItemInfo item : _items)
		{
			packetWriter.writeH(item.getLastChange()); // Update type : 01-add, 02-modify, 03-remove
			writeItemInfo(packetWriter, item);
		}
		return true;
	}
}