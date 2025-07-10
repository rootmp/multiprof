package l2s.gameserver.model.actor.instances.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import l2s.gameserver.dao.CollectionsDAO;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.CollectionTemplate;

/**
 * @author nexvill
 */
public class CollectionList implements Iterable<List<CollectionTemplate>>
{
	private final Player owner;
	private final Map<Integer, List<CollectionTemplate>> collections;

	public CollectionList(Player owner)
	{
		this.owner = owner;
		collections = new TreeMap<>();
	}

	public Player getOwner()
	{
		return owner;
	}

	public void restore()
	{
		CollectionsDAO.getInstance().restore(owner, collections);
	}

	public int size()
	{
		return collections.size();
	}

	public Collection<List<CollectionTemplate>> values()
	{
		return collections.values();
	}

	public boolean add(CollectionTemplate collectionTemplate)
	{
		if (CollectionsDAO.getInstance().insert(owner, collectionTemplate))
		{
			collections.computeIfAbsent(collectionTemplate.getId(), (l) -> new ArrayList<>()).add(collectionTemplate);
			return true;
		}
		return false;
	}

	public List<CollectionTemplate> get(int id)
	{
		return collections.get(id);
	}

	public boolean contains(int id)
	{
		return get(id) != null;
	}

	@Override
	public String toString()
	{
		return "CollectionList[owner=" + owner.getName() + "]";
	}

	@Override
	public Iterator<List<CollectionTemplate>> iterator()
	{
		return collections.values().iterator();
	}
}
