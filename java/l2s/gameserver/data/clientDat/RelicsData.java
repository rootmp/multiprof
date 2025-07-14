package l2s.gameserver.data.clientDat;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.templates.relics.RelicsCollectionTemplate;

public final class RelicsData
{
	private static final RelicsData INSTANCE = new RelicsData();

	private Map<Integer, RelicsCollectionTemplate> _relicsCollectionTemplates = new HashMap<>();

	public static RelicsData getInstance()
	{
		return INSTANCE;
	}

	public RelicsCollectionTemplate getRelicCollection(int relicId)
	{
		return _relicsCollectionTemplates.get(relicId);
	}

	public void addRelicCollection(int relics_collection_id, RelicsCollectionTemplate relicsCollectionTemplate)
	{
		_relicsCollectionTemplates.put(relics_collection_id, relicsCollectionTemplate);
	}

	public Map<Integer, RelicsCollectionTemplate> getAllCollectionTemplates()
	{
		return _relicsCollectionTemplates;
	}
}
