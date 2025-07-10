package l2s.gameserver.data.xml.holder;

import l2s.commons.data.xml.AbstractHolder;

import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;

/**
 * @author Bonux
 **/
public final class KarmaIncreaseDataHolder extends AbstractHolder
{
	private static final KarmaIncreaseDataHolder _instance = new KarmaIncreaseDataHolder();

	private final TIntDoubleMap _bonusList = new TIntDoubleHashMap();

	public static KarmaIncreaseDataHolder getInstance()
	{
		return _instance;
	}

	public void addData(int lvl, double bonus)
	{
		_bonusList.put(lvl, bonus);
	}

	public double getData(int lvl)
	{
		return _bonusList.get(lvl);
	}

	@Override
	public int size()
	{
		return _bonusList.size();
	}

	@Override
	public void clear()
	{
		_bonusList.clear();
	}
}
