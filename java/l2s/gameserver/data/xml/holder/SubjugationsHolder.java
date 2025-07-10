package l2s.gameserver.data.xml.holder;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.SubjugationTemplate;

/**
 * @author nexvill
 **/
public class SubjugationsHolder extends AbstractHolder
{
	private static final SubjugationsHolder _instance = new SubjugationsHolder();

	private final Map<Integer, SubjugationTemplate> _subjugations = new HashMap<>();

	public static SubjugationsHolder getInstance()
	{
		return _instance;
	}

	public void addSubjugationZoneInfo(SubjugationTemplate info)
	{
		_subjugations.put(info.getZoneId(), info);
	}

	public Map<Integer, SubjugationTemplate> getFields()
	{
		return _subjugations;
	}

	@Override
	public int size()
	{
		return _subjugations.size();
	}

	@Override
	public void clear()
	{
		_subjugations.clear();
	}
}
