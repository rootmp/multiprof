package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.items.ItemInfo;

/**
 * @author Bonux
 */
public class ExChangeAttributeItemList implements IClientOutgoingPacket
{
	private ItemInfo[] _itemsList;
	private int _itemId;

	public ExChangeAttributeItemList(int itemId, ItemInfo[] itemsList)
	{
		_itemId = itemId;
		_itemsList = itemsList;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemId);
		packetWriter.writeD(_itemsList.length); // size
		for(ItemInfo item : _itemsList)
		{
			writeItemInfo(packetWriter, item);
		}
		return true;
	}
}