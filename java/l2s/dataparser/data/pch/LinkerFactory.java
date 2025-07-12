package l2s.dataparser.data.pch;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.dataparser.data.pch.linker.CastledataPchLinker;
import l2s.dataparser.data.pch.linker.CategoryPchLinker;
import l2s.dataparser.data.pch.linker.InstantzoneDataPchLinker;
import l2s.dataparser.data.pch.linker.ItemPchLinker;
import l2s.dataparser.data.pch.linker.ManualPchLinker;
import l2s.dataparser.data.pch.linker.MultisellPchLinker;
import l2s.dataparser.data.pch.linker.NpcPchLinker;
import l2s.dataparser.data.pch.linker.OptionPchLinker;
import l2s.dataparser.data.pch.linker.QuestPchLinker;
import l2s.dataparser.data.pch.linker.SkillPchLinker;

/**
 * Created with IntelliJ IDEA. User: camelion Date: 1/13/13 Time: 1:47 PM To
 * change this template use File | Settings | File Templates.
 */
public class LinkerFactory
{
	private static final Logger LOGGER = LoggerFactory.getLogger(LinkerFactory.class);
	private static Map<String, String> links = new HashMap<>();
	private static Map<String, String> itemlinks = new HashMap<>();
	private static Map<String, String> npclinks = new HashMap<>();
	private static Map<String, String> manual_pch_links = new HashMap<>();
	private static Map<String, int[]> skill_pch = new HashMap<>();
	private static Map<String, String> option_pch_links = new HashMap<>();

	private static LinkerFactory ourInstance = new LinkerFactory();
	private boolean isLoaded;

	public static LinkerFactory getInstance()
	{
		return ourInstance;
	}

	private LinkerFactory()
	{
		load();
	}

	private void load()
	{
		if(!isLoaded)
		{
			CastledataPchLinker.getInstance().load();
			CategoryPchLinker.getInstance().load();
			ManualPchLinker.getInstance().load();
			InstantzoneDataPchLinker.getInstance().load();
			ItemPchLinker.getInstance().load();
			SkillPchLinker.getInstance().load();
			MultisellPchLinker.getInstance().load();
			NpcPchLinker.getInstance().load();
			OptionPchLinker.getInstance().load();
			QuestPchLinker.getInstance().load();
			isLoaded = true;
		}
	}

	public void unload()
	{
		if(isLoaded)
		{
			links.clear();
			isLoaded = false;
		}
	}

	public String findValueFor(String link)
	{
		return links.get(link);
	}

	public long findValueForLink(String link)
	{
		final String str = "[".concat(link.replace("@", "")).concat("]");
		return Long.parseLong(links.get(str));
	}

	public long findManualClearValue(String link)
	{
		long value = 0;
		try
		{
			value = Long.parseLong(manual_pch_links.get("@" + link));
		}
		catch(final Exception e)
		{
			LOGGER.warn("findManualClearValue returned exception for |{}| link", link);
		}
		return value;
	}

	public int findClearValue(String link)
	{
	String c_link = link.replaceAll("\\[", "").replaceAll("\\]", "");
		int value = 0;
		try
		{
			value = Integer.parseInt(links.get("@" + c_link));
		}
		catch(final Exception e)
		{
			LOGGER.warn("findClearValue returned exception for |{}| link", c_link);
		}
		return value;
	}

	public String findLinkFromValue(long linkId)
	{
		final String key = "";
		for(Entry<String, String> entry : links.entrySet())
			if(entry.getValue().equals(String.valueOf(linkId)))
				return entry.getKey().replace("@", "");
		LOGGER.warn("method findLinkFromValue returned null from ID: " + linkId);
		return key;
	}

	public static void addLink(String link, String value)
	{
		links.put(link, value);
		manual_pch_links.put(link, value);
	}

	public static void addLinkItem(String link, String value)
	{
		itemlinks.put(link, value);
	}

	public int itemPchfindClearValue(String link)
	{
		link = link.replace("[", "").replace("]", "");

		if (itemlinks.containsKey("@" + link)) 
			return Integer.parseInt(itemlinks.get("@" + link));
		else if (itemlinks.containsKey(link)) 
			return Integer.parseInt(itemlinks.get(link));
		else 
			return 0;
	}


	public String findLinkItemFromValue(long linkId)
	{
		final String key = "";
		for(Entry<String, String> entry : itemlinks.entrySet())
			if(entry.getValue().equals(String.valueOf(linkId)))
				return entry.getKey().replace("@", "");
		LOGGER.warn("method findLinkItemFromValue returned null from ID: " + linkId);
		return key;
	}
	
	public String findLinkManualFromValue(long linkId)
	{
		final String key = "";
		for(Entry<String, String> entry : manual_pch_links.entrySet())
			if(entry.getValue().equals(String.valueOf(linkId)))
				return entry.getKey().replace("@", "");
		LOGGER.warn("method findLinkManualFromValue returned null from ID: " + linkId);
		return key;
	}


	public String optionPchfindValueFor(String link)
	{
		return option_pch_links.get(link);
	}

	public int optionPchfindClearValue(String link)
	{
		String c_link = link.replaceAll("\\[", "").replaceAll("\\]", "");
		int value = Integer.parseInt(option_pch_links.get("@" + c_link));
		return value;
	}

	public String optionPchfindLinkFromValue(long linkId)
	{
		final String key = "";
		for(Entry<String, String> entry : option_pch_links.entrySet())
			if(entry.getValue().equals(String.valueOf(linkId)))
				return entry.getKey().replace("@", "");
		LOGGER.warn("method optionPchfindLinkFromValue returned null from ID: " + linkId);
		return key;
	}

	public static void addOptionPchLink(String link, String value)
	{
		option_pch_links.put(link, value);
	}

	public static void addLinkSkillPch(String link, int[] objects)
	{
		skill_pch.put(link, objects);
	}

	public String optionSkillfindLinkFromValue(long linkId) 
	{
		try 
		{
			final int skill_id = (int) (linkId / 65536 / 65536);
			final int skill_level = (int) (linkId - 65536 * 65536 * skill_id);

			for (Map.Entry<String, int[]> entry : skill_pch.entrySet()) 
			{
				int[] skillData = entry.getValue();
				if (skillData != null && skillData.length > 0) 
				{
					if (skillData[0] == skill_id && skillData[1] == skill_level) 
						return entry.getKey().replace("@", "");
				}
			}

			LOGGER.warn("Skill name not found for ID: {} and Level: {}", skill_id, skill_level);
		} catch (Exception e) {
			LOGGER.error("Error while resolving skill name for linkId: " + linkId, e);
		}

		return "UnknownSkill";
	}

	/**
	 * 
	 * @param link (@skill_name)
	 * @return array[id, level]
	 */
	public int[] skillPchIdfindClearValue(String link)
	{
		int[] value = skill_pch.get("@" + link);//.clone();
		if(value == null || value.length < 2)
			LOGGER.error(link + " skill not found!");
		return value;
	}

	public static void addLinkNpc(String link, String group)
	{
		npclinks.put(link, group);
	}

	public int npcPchfindClearValue(String link)
	{
		int value = 0;
		try
		{
			value = Integer.parseInt(npclinks.get("@" + link));
		}
		catch(final Exception e)
		{
			LOGGER.warn("npcPchfindClearValue returned exception for |{}| link", link);
		}
		return value;
	}
}