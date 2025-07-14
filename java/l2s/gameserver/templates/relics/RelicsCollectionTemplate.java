package l2s.gameserver.templates.relics;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import l2s.gameserver.templates.StatsSet;

public class RelicsCollectionTemplate
{
	private int relics_collection_id;
	private int category;
	private String relics_collection_name;
	private int option_id;
	private String option_filter;
	private List<int[]> need_relics;

	public RelicsCollectionTemplate(StatsSet relic)
	{
		relics_collection_id = relic.getInteger("relics_collection_id");
		category = relic.getInteger("category");
		relics_collection_name = relic.getString("relics_collection_name");
		option_id = relic.getInteger("option_id");
		option_filter = relic.getString("option_filter");

		need_relics = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\{(\\d+);(\\d+)\\}");
		Matcher matcher = pattern.matcher(relic.getString("need_relics"));

		while(matcher.find())
		{
			int id = Integer.parseInt(matcher.group(1));
			int unk = Integer.parseInt(matcher.group(2));//enchant?
			need_relics.add(new int[] {
					id, unk
			});
		}
	}

	public int getRelicsCollectionId()
	{
		return relics_collection_id;
	}

	public int getCategory()
	{
		return category;
	}

	public String getRelicsCollectionName()
	{
		return relics_collection_name;
	}

	public int getOptionId()
	{
		return option_id;
	}

	public String getOptionFilter()
	{
		return option_filter;
	}

	public List<int[]> getNeedRelics()
	{
		return need_relics;
	}
}
