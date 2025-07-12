package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.RelicHolder;
import l2s.gameserver.templates.relics.RelicsProb;
import l2s.gameserver.templates.relics.RelicsSummonInfo;
import l2s.gameserver.templates.relics.RelicsTemplate;

public final class RelicParser extends AbstractParser<RelicHolder>
{
	private static final RelicParser _instance = new RelicParser();

	public static RelicParser getInstance()
	{
		return _instance;
	}

	private RelicParser()
	{
		super(RelicHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/relics/relics.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "relics.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
    Element summonElement = rootElement.element("summon");
    if (summonElement != null)
    {
        for (Iterator<Element> slotIterator = summonElement.elementIterator("slot"); slotIterator.hasNext(); )
        {
            Element slotElement = slotIterator.next();
            int slotId = Integer.parseInt(slotElement.attributeValue("id"));
            int count = Integer.parseInt(slotElement.attributeValue("count"));
            int itemId = Integer.parseInt(slotElement.attributeValue("item_id"));
            int price = Integer.parseInt(slotElement.attributeValue("price"));
            int time = Integer.parseInt(slotElement.attributeValue("time"));
            int daily_limit = Integer.parseInt(slotElement.attributeValue("daily_limit","-1"));
            
          	List<RelicsProb> relicProbs = new ArrayList<>();
          	for (Iterator<Element> relicIt = slotElement.elementIterator("relic"); relicIt.hasNext(); )
          	{
          		Element relicElement = relicIt.next();
          		int relicId = Integer.parseInt(relicElement.attributeValue("relics_id"));
          		long chance = Long.parseLong(relicElement.attributeValue("prob", "0"));
          		relicProbs.add(new RelicsProb(relicId, chance));
          	}

            RelicHolder.getInstance().addSummonInfo(new RelicsSummonInfo(slotId, count, itemId, price, time, daily_limit, relicProbs));
        }
    }
    
		for(Iterator<Element> iterator = rootElement.elementIterator("relic"); iterator.hasNext();)
		{
			Element relicElement = iterator.next();
			int relicsId = Integer.parseInt(relicElement.attributeValue("relics_id"));
			int itemId = Integer.parseInt(relicElement.attributeValue("item_id"));
			int grade = Integer.parseInt(relicElement.attributeValue("grade"));
			int npcId = Integer.parseInt(relicElement.attributeValue("npc_id"));
			int level = Integer.parseInt(relicElement.attributeValue("level"));
			int sortOrder = Integer.parseInt(relicElement.attributeValue("sort_order"));


			List<Integer> enchantedList = new ArrayList<>();
			String enchantedStr = relicElement.attributeValue("enchanted");
			if (enchantedStr != null && !enchantedStr.isEmpty()) 
			{
				String[] enchantedArray = enchantedStr.split(";");
				for (String e : enchantedArray) 
					enchantedList.add(Integer.parseInt(e));
			}
			Map<Integer, int[]> skillMap = new HashMap<>();
			String skillIdStr = relicElement.attributeValue("skill_id");
			if (skillIdStr != null && !skillIdStr.isEmpty())
			{
				String regex = "\\{(\\d+);(\\d+)\\}";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(skillIdStr);

				int index = 0;
				while (matcher.find())
				{
					int skillId = Integer.parseInt(matcher.group(1));
					int skillLevel = Integer.parseInt(matcher.group(2));
					skillMap.put(index++, new int[]{skillId, skillLevel});
				}
			}

			RelicHolder.getInstance().addRelic(relicsId, new RelicsTemplate(relicsId, itemId, grade, enchantedList, npcId, level, sortOrder, skillMap));
		}
	}
}
