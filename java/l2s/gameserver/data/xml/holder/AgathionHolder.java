package l2s.gameserver.data.xml.holder;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.agathion.AgathionTemplate;

/**
 * @author Bonux
 **/
public final class AgathionHolder extends AbstractHolder
{
	private final Map<Integer, AgathionTemplate> agathions = new HashMap<>();
	private final Map<Integer, AgathionTemplate> agathionsByItemId = new HashMap<>();

	private AgathionHolder()
	{
		//
	}

	public void addAgathionTemplate(AgathionTemplate template)
	{
		agathions.put(template.getId(), template);
		for (int itemId : template.getItemIds())
		{
			agathionsByItemId.put(itemId, template);
		}
	}

	public AgathionTemplate getTemplate(int id)
	{
		return agathions.get(id);
	}

	public AgathionTemplate getTemplateByItemId(int itemId)
	{
		return agathionsByItemId.get(itemId);
	}

	@Override
	public int size()
	{
		return agathions.size();
	}

	@Override
	public void clear()
	{
		agathions.clear();
	}
	
 	public static AgathionHolder getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected final static AgathionHolder _instance = new AgathionHolder();
	}
}
