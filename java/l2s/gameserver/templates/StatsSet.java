package l2s.gameserver.templates;

import l2s.commons.collections.MultiValueSet;

public class StatsSet extends MultiValueSet<String>
{
	public static StatsSet simpleStatsSet(String key, Object value)
	{
		StatsSet statsSet = new StatsSet();
		statsSet.put(key, value);
		return statsSet;
	}

	private static final long serialVersionUID = -2209589233655930756L;

	@SuppressWarnings("serial")
	public static final StatsSet EMPTY = new StatsSet()
	{
		@Override
		public Object put(String a, Object a2)
		{
			throw new UnsupportedOperationException();
		}
	};

	public StatsSet()
	{
		super();
	}

	public StatsSet(StatsSet set)
	{
		super(set);
	}

	@Override
	public StatsSet clone()
	{
		return new StatsSet(this);
	}

	public int incInt(String key, int inc)
	{
		final int newValue = getInteger(key) + inc;
		set(key, newValue);
		return newValue;
	}
	
	public int incInt(String key, int defaultValue, int inc)
	{
		final int newValue =  getInteger(key, defaultValue) + inc;
		set(key, newValue);
		return newValue;
	}
}