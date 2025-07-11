package l2s.gameserver.network.l2.s2c.collection;

import l2s.gameserver.data.xml.holder.CollectionsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.templates.CollectionTemplate;
import l2s.gameserver.templates.item.data.CollectionItemData;

/**
 * @author nexvill
 */
public class ExCollectionRegister implements IClientOutgoingPacket
{
	private Player _player;
	private int _collectionId, _slotId;
	private ItemInstance _item;

	public ExCollectionRegister(Player player, int collectionId, int slotId, ItemInstance item)
	{
		_player = player;
		_collectionId = collectionId;
		_slotId = slotId;
		_item = item;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		CollectionTemplate collection = CollectionsHolder.getInstance().getCollection(_collectionId);
		CollectionTemplate newCollection = new CollectionTemplate(_collectionId, collection.getTabId(), 0);
		long itemCount = 0;
		boolean success = false;
		for (CollectionItemData temp : collection.getItems())
		{
			if (((temp.getId() == _item.getItemId()) || (temp.getAlternativeId() == _item.getItemId())) && (temp.getEnchantLevel() == _item.getEnchantLevel()) && (temp.getCount() <= _item.getCount()))
			{
				CollectionItemData itemData = new CollectionItemData(_item.getItemId(), temp.getCount(), temp.getEnchantLevel(), 0, _slotId);
				newCollection.addItem(itemData);
				_player.getCollectionList().add(newCollection);
				itemCount = temp.getCount();
				_player.getInventory().destroyItem(_item, itemCount);
				success = true;
				break;
			}
		}

		if (!success)
		{
			return true;
		}

		packetWriter.writeH(_collectionId); // collection id
		packetWriter.writeC(1); // activate?
		packetWriter.writeH(14); // unk, but with 0 - not register
		packetWriter.writeC(_slotId);
		packetWriter.writeD(_item.getItemId());
		packetWriter.writeC(0);
		packetWriter.writeH(_item.getEnchantLevel());
		packetWriter.writeD((int) itemCount);
		return true;
	}
}