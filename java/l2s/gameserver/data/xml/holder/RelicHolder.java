package l2s.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.relics.RelicsCollectionTemplate;
import l2s.gameserver.templates.relics.RelicsProb;
import l2s.gameserver.templates.relics.RelicsSummonInfo;
import l2s.gameserver.templates.relics.RelicsTemplate;

public final class RelicHolder extends AbstractHolder
{
	private static final RelicHolder _instance = new RelicHolder();

	public static RelicHolder getInstance()
	{
		return _instance;
	}

	private Map<Integer, RelicsTemplate> _relicsTemplates = new HashMap<>();
	private Map<Integer, List<RelicsTemplate>> _relicsTemplatesByGrade = new HashMap<>();

	private Map<Integer, RelicsCollectionTemplate> _relicsCollectionTemplates = new HashMap<>();

	public void addRelic(int relics_id, RelicsTemplate relicTemplate)
	{
		_relicsTemplates.put(relics_id, relicTemplate);

		int grade = relicTemplate.getGrade();
		_relicsTemplatesByGrade.computeIfAbsent(grade, k -> new ArrayList<>()).add(relicTemplate);
	}

	public List<RelicsTemplate> getRelicsByGrades(int[] grades)
	{
		List<RelicsTemplate> relics = new ArrayList<>();
		for(int grade : grades)
		{
			List<RelicsTemplate> relicsForGrade = _relicsTemplatesByGrade.get(grade);
			if(relicsForGrade != null)
				relics.addAll(relicsForGrade);
		}
		return relics;
	}

	public RelicsTemplate getRelic(int relicId)
	{
		return _relicsTemplates.get(relicId);
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

	private final Map<Integer, RelicsSummonInfo> summonInfos = new HashMap<>();

	public void addSummonInfo(RelicsSummonInfo info)
	{
		summonInfos.put(info.getSummonId(), info);
	}

	public RelicsSummonInfo getSummonInfo(int nSummonID)
	{
		return summonInfos.get(nSummonID);
	}

	public Collection<RelicsSummonInfo> getSummonInfos()
	{
		return summonInfos.values();
	}

	@Override
	public int size()
	{
		return _relicsTemplates.size();
	}

	@Override
	public void clear()
	{
		_relicsTemplates.clear();
	}

	public List<RelicsProb> getSummonRelicsProb(int key)
	{
		return getSummonInfo(key).getRelicProbs();
	}
}
