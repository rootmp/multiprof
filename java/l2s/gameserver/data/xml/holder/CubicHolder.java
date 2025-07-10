package l2s.gameserver.data.xml.holder;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.cubic.CubicTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author VISTALL
 * @date 15:15/22.12.2010
 */
public final class CubicHolder extends AbstractHolder
{
	private static CubicHolder _instance = new CubicHolder();
	private final TIntObjectHashMap<CubicTemplate> _cubics = new TIntObjectHashMap<CubicTemplate>(10);

	public static CubicHolder getInstance()
	{
		return _instance;
	}

	private CubicHolder()
	{
	}

	public void addCubicTemplate(CubicTemplate template)
	{
		_cubics.put(SkillHolder.getInstance().getHashCode(template.getId(), template.getLevel()), template);
	}

	public CubicTemplate getTemplate(int id, int level)
	{
		return _cubics.get(SkillHolder.getInstance().getHashCode(id, level));
	}

	@Override
	public int size()
	{
		return _cubics.size();
	}

	@Override
	public void clear()
	{
		_cubics.clear();
	}
}
