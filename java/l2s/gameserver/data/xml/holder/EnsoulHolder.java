package l2s.gameserver.data.xml.holder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.item.support.Ensoul;
import l2s.gameserver.templates.item.support.EnsoulFee;

/**
 * @author Bonux
**/
public class EnsoulHolder extends AbstractHolder
{
	private static final EnsoulHolder _instance = new EnsoulHolder();

	private Map<Long, Map<Integer, EnsoulFee>> _ensoulsFee = new HashMap<>();

	private TIntObjectMap<Ensoul> _ensouls = new TIntObjectHashMap<Ensoul>();
	private final TIntObjectMap<Ensoul> _ensoulsByItemId = new TIntObjectHashMap<>();

	public static EnsoulHolder getInstance()
	{
		return _instance;
	}

	public void addEnsoulFee(long bodyPart, List<Integer> optionIds, EnsoulFee ensoulFee)
	{
		Map<Integer, EnsoulFee> entityFeeMap = _ensoulsFee.computeIfAbsent(bodyPart, k -> new HashMap<>());
		for(Integer optionId : optionIds)
			entityFeeMap.put(optionId, ensoulFee);
	}

	public EnsoulFee getEnsoulFee(long bodyPart, int optionId)
	{
		Map<Integer, EnsoulFee> entityFeeMap = _ensoulsFee.get(bodyPart);
		if(entityFeeMap != null)
			return entityFeeMap.get(optionId);
		else
			return null;
	}

	public void addEnsoul(Ensoul ensoul)
	{
		_ensouls.put(ensoul.getId(), ensoul);
		_ensoulsByItemId.put(ensoul.getItemId(), ensoul);
	}

	public Ensoul getEnsoul(int id)
	{
		return _ensouls.get(id);
	}

	public boolean isEnsoulItem(int itemId)
	{
		return _ensoulsByItemId.get(itemId) != null;
	}

	@Override
	public int size()
	{
		return _ensouls.size();
	}

	@Override
	public void clear()
	{
		_ensoulsFee.clear();
		_ensouls.clear();
		_ensoulsByItemId.clear();
	}
}
