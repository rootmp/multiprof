package l2s.gameserver.data.xml.holder;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.support.Ensoul;
import l2s.gameserver.templates.item.support.EnsoulFee;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Bonux
 **/
public class EnsoulHolder extends AbstractHolder
{
	private static final EnsoulHolder _instance = new EnsoulHolder();

	private final TIntObjectMap<EnsoulFee> _ensoulsFee = new TIntObjectHashMap<>();
	private final TIntObjectMap<Ensoul> _ensouls = new TIntObjectHashMap<>();
	private final TIntObjectMap<Ensoul> _ensoulsByItemId = new TIntObjectHashMap<>();

	public static EnsoulHolder getInstance()
	{
		return _instance;
	}

	public void addEnsoulFee(ItemGrade grade, EnsoulFee ensoulFee)
	{
		_ensoulsFee.put(grade.ordinal(), ensoulFee);
	}

	public EnsoulFee getEnsoulFee(ItemGrade grade)
	{
		return _ensoulsFee.get(grade.ordinal());
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
