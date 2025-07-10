package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.Map;

import l2s.gameserver.model.items.Inventory;

public class ShopPreviewInfoPacket implements IClientOutgoingPacket
{
	private Map<Integer, Integer> _itemlist;

	public ShopPreviewInfoPacket(Map<Integer, Integer> itemlist)
	{
		_itemlist = itemlist;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(Inventory.PAPERDOLL_MAX);

		// Slots
		for (int PAPERDOLL_ID : Inventory.PAPERDOLL_ORDER)
			packetWriter.writeD(getFromList(PAPERDOLL_ID));
	}

	private int getFromList(int key)
	{
		return ((_itemlist.get(key) != null) ? _itemlist.get(key) : 0);
	}
}