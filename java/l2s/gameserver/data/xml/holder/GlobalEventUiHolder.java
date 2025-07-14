package l2s.gameserver.data.xml.holder;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;

public class GlobalEventUiHolder extends AbstractHolder
{
	private static final GlobalEventUiHolder _instance = new GlobalEventUiHolder();

	private Map<Integer, Integer> _EventList = new HashMap<>();

	public Map<Integer, Integer> getEventList()
	{
		return _EventList;
	}

	public static GlobalEventUiHolder getInstance()
	{
		return _instance;
	}

	@Override
	public int size()
	{
		return _EventList.size();
	}

	@Override
	public void clear()
	{
		_EventList.clear();
	}

	public void addEvent(int id, int multisell)
	{
		_EventList.put(id, multisell);
	}

	public int getEvent(int nEventIndex)
	{
		return _EventList.getOrDefault(nEventIndex, 0);
	}
}
