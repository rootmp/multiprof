package l2s.gameserver.network.l2.s2c.collection;

import l2s.gameserver.data.xml.holder.CollectionsHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.CollectionTemplate;
import l2s.gameserver.templates.item.data.CollectionItemData;

/**
 * @author nexvill
 */
public class ExCollectionRegister extends L2GameServerPacket
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
	protected final void writeImpl()
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
			return;
		}

		writeH(_collectionId); // collection id
		writeC(1); // activate?
		writeH(14); // unk, but with 0 - not register
		writeC(_slotId);
		writeD(_item.getItemId());
		writeC(0);
		writeH(_item.getEnchantLevel());
		writeD((int) itemCount);
	}
}