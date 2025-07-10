package l2s.gameserver.data.xml.holder;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.FestivalBMTemplate;

/**
 * @author nexvill
 **/
public final class FestivalBMHolder extends AbstractHolder
{
	private static final FestivalBMHolder _instance = new FestivalBMHolder();

	private final Map<Integer, FestivalBMTemplate> _festivalBMInfos = new HashMap<>();

	public static FestivalBMHolder getInstance()
	{
		return _instance;
	}

	public void addFestivalBMInfo(int i, FestivalBMTemplate info)
	{
		_festivalBMInfos.put(i, info);
	}

	public Map<Integer, FestivalBMTemplate> getItems()
	{
		return _festivalBMInfos;
	}

	@Override
	public int size()
	{
		return _festivalBMInfos.size();
	}

	@Override
	public void clear()
	{
		_festivalBMInfos.clear();
	}
}
