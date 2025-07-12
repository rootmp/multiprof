package l2s.gameserver.network.l2.s2c.collection;

import java.util.Map;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.CollectionTemplate;
import l2s.gameserver.templates.item.data.CollectionItemData;

public class ExCollectionInfo implements IClientOutgoingPacket
{
	private int _tabId;
	private Map<Integer, CollectionTemplate> _collection;
	private int[] _collectionFavorites;
	private int[] _collectionReward;
	
	public ExCollectionInfo(int tabId, Map<Integer, CollectionTemplate> collection, int[] collectionFavorites, int[] collectionReward)
	{
		_tabId = tabId;
		_collection = collection;
		_collectionFavorites = collectionFavorites;
		_collectionReward = collectionReward;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_collection.size());
		for (int id : _collection.keySet())
		{
			packetWriter.writeD(_collection.get(id).getItems().size()); // items added count
			for (CollectionItemData itemData : _collection.get(id).getItems())
			{
				packetWriter.writeC(itemData.getSlotId()); //cSlotIndex
				packetWriter.writeD(itemData.getId());//nItemClassID
				packetWriter.writeC(itemData.getEnchantLevel()); // cEnchant
				packetWriter.writeC(itemData.getBless()); //bBless
				packetWriter.writeC(itemData.getBlessCondition()); 
				packetWriter.writeD((int) itemData.getCount());
			}
			packetWriter.writeH(id); // collection id
		}
		packetWriter.writeD(_collectionFavorites.length); // nSize
		for (int i = 0; i < _collectionFavorites.length; i++)
			packetWriter.writeH(_collectionFavorites[i]); // nCollectionID

		packetWriter.writeD(_collectionReward.length); // nSize
		for (int i = 0; i < _collectionReward.length; i++)
			packetWriter.writeH(_collectionReward[i]); //rewardList

		packetWriter.writeC(_tabId);//cCategory
		packetWriter.writeH(_collection.size());//nTotalCollectionCount
		return true;
	}
}