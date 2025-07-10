package l2s.gameserver.data.xml.holder;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.TeleportTemplate;

public final class TeleportListHolder extends AbstractHolder
{
	private static final TeleportListHolder _instance = new TeleportListHolder();

	private final Map<Integer, TeleportTemplate> _teleportsInfos = new HashMap<>();

	public static TeleportListHolder getInstance()
	{
		return _instance;
	}

	public void addTeleportInfo(TeleportTemplate info)
	{
		_teleportsInfos.put(info.getId(), info);
	}

	public TeleportTemplate getTeleportInfo(int id)
	{
		return _teleportsInfos.get(id);
	}

	@Override
	public int size()
	{
		return _teleportsInfos.size();
	}

	@Override
	public void clear()
	{
		_teleportsInfos.clear();
	}
}
