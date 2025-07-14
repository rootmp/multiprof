package l2s.gameserver.templates.item;

import java.util.Map;

import l2s.gameserver.model.base.Race;

public final class Visual
{
	private final int _extract_id;
	private final Map<Race, Integer> _alternative;

	public Visual(int extract_id,Map<Race, Integer> alternative)
	{
		_extract_id = extract_id;
		_alternative = alternative;
	}

	public int getExtractId()
	{
		return _extract_id;
	}

	public Map<Race, Integer> getAlternative()
	{
		return _alternative;
	}

}
