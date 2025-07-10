package l2s.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.CollectionTemplate;

/**
 * @author nexvill
 */
public final class CollectionsHolder extends AbstractHolder
{
	private static final CollectionsHolder INSTANCE = new CollectionsHolder();

	public static CollectionsHolder getInstance()
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

	public CollectionTemplate getCollection(int id)
	{
		return _collections.get(id);
	}

	public List<CollectionTemplate> getCollectionsByTabId(int tabId)
	{
		List<CollectionTemplate> result = _collectionsByTabId.get(tabId);
		if (result == null)
			return Collections.emptyList();
		return result;
	}

	@Override
	public int size()
	{
		return _collections.size();
	}

	@Override
	public void clear()
	{
		_collections.clear();
	}
}
