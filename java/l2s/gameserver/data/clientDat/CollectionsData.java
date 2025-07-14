package l2s.gameserver.data.clientDat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import l2s.gameserver.data.xml.holder.OptionDataHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.CollectionList;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionInfo;
import l2s.gameserver.templates.CollectionTemplate;
import l2s.gameserver.templates.OptionDataTemplate;

public final class CollectionsData
{
	private static final CollectionsData INSTANCE = new CollectionsData();

	public static CollectionsData getInstance()
	{
		return INSTANCE;
	}

	private final Map<Integer, CollectionTemplate> _collections = new TreeMap<>();
	private final Map<Integer, List<CollectionTemplate>> _collectionsByTabId = new HashMap<>();

	public void addCollection(CollectionTemplate collectionTemplate)
	{
		_collections.put(collectionTemplate.getId(), collectionTemplate);
		_collectionsByTabId.computeIfAbsent(collectionTemplate.getTabId(), (l) -> new ArrayList<>()).add(collectionTemplate);
	}

	public Map<Integer, CollectionTemplate> getCollections()
	{
		return _collections;
	}

	public CollectionTemplate getCollection(int id)
	{
		return _collections.get(id);
	}

	public List<CollectionTemplate> getCollectionsByTabId(int tabId)
	{
		List<CollectionTemplate> result = _collectionsByTabId.get(tabId);
		if(result == null)
			return Collections.emptyList();
		return result;
	}

	public void sendExCollectionInfo(Player player)
	{
		for(int tabId = 1; tabId < 8; tabId++)
		{
			player.sendPacket(new ExCollectionInfo(tabId, getPlayerCollection(player, tabId), player.getCollectionFavorites().toArray(), player.getCollectionReward().toArray()));
		}
		player.sendUserInfo(true);
		player.broadcastUserInfo(true);
	}

	public void initCollectionInfo(Player player)
	{
		for(int tabId = 1; tabId < 8; tabId++)
		{
			getPlayerCollection(player, tabId);
		}
	}

	public Map<Integer, CollectionTemplate> getPlayerCollection(Player player, int tabId)
	{
		List<CollectionTemplate> list = CollectionsData.getInstance().getCollectionsByTabId(tabId);
		CollectionList collections = player.getCollectionList();
		Map<Integer, CollectionTemplate> collection = new TreeMap<>();

		for(CollectionTemplate template : list)
		{
			if(collections.contains(template.getId()))
			{
				List<CollectionTemplate> temp = collections.get(template.getId());
				CollectionTemplate newTemplate = new CollectionTemplate(template.getId(), tabId, 0);
				newTemplate.setMaxSlot(template.getMaxSlot());

				for(CollectionTemplate tmp : temp)
					newTemplate.getItems().addAll(tmp.getItems());

				collection.put(template.getId(), newTemplate);
			}
		}
		player.getStatsRecorder().block();
		for(int id : collection.keySet())
		{
			for(CollectionTemplate temp : list)
			{
				if(temp.getId() == id
						&& temp.getMaxSlot() == collection.get(id).getItems().stream().collect(Collectors.groupingBy(r -> r.getSlotId())).size())
				{
					int id2 = CollectionsData.getInstance().getCollection(id).getOptionId();
					OptionDataTemplate option = OptionDataHolder.getInstance().getTemplate(id2);
					player.addOptionData(option);
				}
			}
		}
		player.getStatsRecorder().unblock();
		return collection;
	}
}
