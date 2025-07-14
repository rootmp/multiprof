package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.SubjugationsHolder;
import l2s.gameserver.templates.SubjugationTemplate;
import l2s.gameserver.templates.item.data.ItemData;

/**
 * @author nexvill
 **/
public final class SubjugationsParser extends AbstractParser<SubjugationsHolder>
{
	private static final SubjugationsParser _instance = new SubjugationsParser();

	public static SubjugationsParser getInstance()
	{
		return _instance;
	}

	private SubjugationsParser()
	{
		super(SubjugationsHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/essence/subjugation_fields.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "subjugation_fields.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();

			int id = parseInt(element, "id");
			int pointsToKey = parseInt(element, "pointsToKey");
			int maximumKeys = parseInt(element, "maximumKeys", 1);
			int minLevel = parseInt(element, "min_level", 1);
			int maxLevel = parseInt(element, "max_level", Integer.MAX_VALUE);

			SubjugationTemplate zone = new SubjugationTemplate(id, pointsToKey, maximumKeys, minLevel, maxLevel);

			for(Iterator<Element> mobsIterator = element.elementIterator("mobs"); mobsIterator.hasNext();)
			{
				Element mobElement = mobsIterator.next();
				if(mobElement.getName().equals("mobs"))
				{
					for(Element e : mobElement.elements())
					{
						int mobId = Integer.parseInt(e.attributeValue("id"));
						zone.addMobId(mobId);
					}
				}
			}

			for(Iterator<Element> rewardsIterator = element.elementIterator("rewards"); rewardsIterator.hasNext();)
			{
				Element rewardElement = rewardsIterator.next();
				if(rewardElement.getName().equals("rewards"))
				{
					for(Element e : rewardElement.elements())
					{
						int rewardId = Integer.parseInt(e.attributeValue("id"));
						long rewardCount = Long.parseLong(e.attributeValue("count"));
						zone.addRewardItem(new ItemData(rewardId, rewardCount));
					}
				}
			}
			getHolder().addSubjugationZoneInfo(zone);
		}
	}
}