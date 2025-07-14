package l2s.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.spawn.SpawnTemplate;

/**
 * @author VISTALL
 * @date 18:38/10.12.2010
 */
public final class SpawnHolder extends AbstractHolder
{
	private Map<String, List<SpawnTemplate>> _spawns = new HashMap<String, List<SpawnTemplate>>();
	private Map<SpawnTemplate, String> _filesToTemplate = new HashMap<>();

	public void addSpawn(String currentFileName, String group, SpawnTemplate spawn)
	{
		_filesToTemplate.put(spawn, currentFileName);
		final List<SpawnTemplate> spawns = _spawns.computeIfAbsent(group, k -> new ArrayList<>());
		spawns.add(spawn);
	}

	public List<SpawnTemplate> getAllTemplates()
	{
		final List<SpawnTemplate> result = new ArrayList<>();
		for(String group : _spawns.keySet())
		{
			result.addAll(_spawns.get(group));
		}
		return result;
	}

	public String getFileByTemplate(SpawnTemplate template)
	{
		return _filesToTemplate.get(template);
	}

	public List<SpawnTemplate> getAllTemplateBySimple(SpawnTemplate template)
	{
		final String filename = _filesToTemplate.get(template);
		final List<SpawnTemplate> templates = new ArrayList<>();
		for(SpawnTemplate temp : _filesToTemplate.keySet())
		{
			if(_filesToTemplate.get(temp).equals(filename))
			{
				templates.add(temp);
			}
		}
		return templates;
	}

	public List<SpawnTemplate> getSpawn(String name)
	{
		final List<SpawnTemplate> template = _spawns.get(name);
		return template == null ? Collections.<SpawnTemplate> emptyList() : template;
	}

	@Override
	public int size()
	{
		int i = 0;
		for(List<SpawnTemplate> l : _spawns.values())
		{
			i += l.size();
		}
		return i;
	}

	@Override
	public void clear()
	{
		_spawns.clear();
	}

	public Map<String, List<SpawnTemplate>> getSpawns()
	{
		return _spawns;
	}

	public static SpawnHolder getInstance()
	{
		return SingletonHolder._instance;
	}

	private static class SingletonHolder
	{
		protected final static SpawnHolder _instance = new SpawnHolder();
	}
}
