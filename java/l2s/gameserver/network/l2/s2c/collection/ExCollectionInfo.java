package l2s.gameserver.network.l2.s2c.collection;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import l2s.gameserver.data.xml.holder.CollectionsHolder;
import l2s.gameserver.data.xml.holder.OptionDataHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.CollectionList;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.templates.CollectionTemplate;
import l2s.gameserver.templates.OptionDataTemplate;
import l2s.gameserver.templates.item.data.CollectionItemData;

/**
 * @author nexvill
 */
public class ExCollectionInfo implements IClientOutgoingPacket
{
	private Player _player;
	private int _tabId;

	public ExCollectionInfo(Player player, int tabId)
	{
		_player = player;
		_tabId = tabId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		List<CollectionTemplate> list = CollectionsHolder.getInstance().getCollectionsByTabId(_tabId);
		CollectionList collections = _player.getCollectionList();
		Map<Integer, CollectionTemplate> collection = new TreeMap<>();

		for (CollectionTemplate template : list)
		{
			if (collections.contains(template.getId()))
			{
				List<CollectionTemplate> temp = collections.get(template.getId());
				CollectionTemplate newTemplate = new CollectionTemplate(template.getId(), _tabId, 0);
				for (CollectionTemplate tmp : temp)
				{
					newTemplate.getItems().addAll(tmp.getItems());
				}
				collection.put(template.getId(), newTemplate);
			}
		}
		for (int id : collection.keySet())
		{
			for (CollectionTemplate temp : list)
			{
				if ((temp.getId() == id) && (temp.getItems().size() == collection.get(id).getItems().size()))
				{
					int id2 = CollectionsHolder.getInstance().getCollection(id).getOptionId();
					OptionDataTemplate option = OptionDataHolder.getInstance().getTemplate(id2);
					_player.addOptionData(option);
					_player.sendUserInfo(true);
					_player.broadcastUserInfo(true);
				}
			}
		}

		packetWriter.writeD(collection.size());
		for (int id : collection.keySet())
		{
			packetWriter.writeD(collection.get(id).getItems().size()); // items added count
			for (CollectionItemData itemData : collection.get(id).getItems())
			{
				packetWriter.writeC(itemData.getSlotId());
				packetWriter.writeD(itemData.getId());
				packetWriter.writeC(0); // ??
				packetWriter.writeH(0); // ??
				packetWriter.writeD((int) itemData.getCount());
			}
			packetWriter.writeH(id); // collection id
		}
		packetWriter.writeD(0); // ??
		packetWriter.writeD(0); // ??
		packetWriter.writeC(_tabId);
		packetWriter.writeH(collection.size());
		return true;
	}
}