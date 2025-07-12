package l2s.gameserver.data.xml.holder;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.relics.RelicsProb;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RelicsSynthesisHolder extends AbstractHolder
{
	private static final RelicsSynthesisHolder _instance = new RelicsSynthesisHolder();
	private Map<Integer, List<RelicsProb>> _relicsSynthesis  = new HashMap<>();
	
	public static RelicsSynthesisHolder getInstance()
	{
		return _instance;
	}

	@Override
	public int size()
	{
		return _relicsSynthesis.size();
	}

	@Override
	public void clear()
	{
		_relicsSynthesis.clear();
	}

	public void addSynthesis(int id, List<RelicsProb> _prob)
	{
		_relicsSynthesis.put(id, _prob);
	}

	public List<RelicsProb> getRelicsProb(int key)
	{
		return _relicsSynthesis.getOrDefault(key, Collections.emptyList());    
	}
}
