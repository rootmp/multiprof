package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.AppearanceStoneHolder;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.templates.item.ExItemType;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.Visual;
import l2s.gameserver.templates.item.support.AppearanceStone;
import l2s.gameserver.templates.item.support.AppearanceStone.ShapeTargetType;
import l2s.gameserver.templates.item.support.AppearanceStone.ShapeType;

/**
 * @author Bonux
**/
public class AppearanceStoneParser extends AbstractParser<AppearanceStoneHolder>
{
	private static AppearanceStoneParser _instance = new AppearanceStoneParser();

	public static AppearanceStoneParser getInstance()
	{
		return _instance;
	}

	private AppearanceStoneParser()
	{
		super(AppearanceStoneHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/appearance_stones.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "appearance_stones.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> iterator = rootElement.elementIterator("config"); iterator.hasNext();)
		{
			Element stoneElement = iterator.next();
			Config.APPEARANCE_STONE_CHECK_ARMOR_TYPE = Boolean.parseBoolean(stoneElement.attributeValue("check_armor_type"));
			Config.APPEARANCE_STONE_RETURT_STONE = Boolean.parseBoolean(stoneElement.attributeValue("return_stone"));
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("items_dressing"); iterator.hasNext();)
		{
			Element stoneElement = iterator.next();
			int id = Integer.parseInt(stoneElement.attributeValue("id"));
			int price_id = Integer.parseInt(stoneElement.attributeValue("price_id"));
			int price_count = Integer.parseInt(stoneElement.attributeValue("price_count"));
			getHolder().addItemDreassing(id, price_id, price_count);
		}
		
		for(Iterator<Element> iterator = rootElement.elementIterator("stone"); iterator.hasNext();)
		{
			Element stoneElement = iterator.next();
			int itemId = Integer.parseInt(stoneElement.attributeValue("id"));

			String[] targetTypesStr = stoneElement.attributeValue("target_type").split(",");
			ShapeTargetType[] targetTypes = new ShapeTargetType[targetTypesStr.length];
			for(int i = 0; i < targetTypesStr.length; i++)
				targetTypes[i] = ShapeTargetType.valueOf(targetTypesStr[i].toUpperCase());

			ShapeType type = ShapeType.valueOf(stoneElement.attributeValue("shifting_type").toUpperCase());

			String[] gradesStr = stoneElement.attributeValue("grade") == null ? new String[0] : stoneElement.attributeValue("grade").split(",");
			ItemGrade[] grades = new ItemGrade[gradesStr.length];
			for(int i = 0; i < gradesStr.length; i++)
				grades[i] = ItemGrade.valueOf(gradesStr[i].toUpperCase());

			long cost = stoneElement.attributeValue("cost") == null ? 0L : Long.parseLong(stoneElement.attributeValue("cost"));

			int period = stoneElement.attributeValue("period") == null ? -1 : Integer.parseInt(stoneElement.attributeValue("period"));
			Map<ExItemType,Visual> _visual = new HashMap<ExItemType,Visual>();

			for(Iterator<Element> visualIterator = stoneElement.elementIterator("visual"); visualIterator.hasNext();)
			{
				Element visualElement = visualIterator.next();

				Map<Race, Integer> _alternative = new HashMap<Race, Integer>();

				Stream.of(visualElement.attributeValue("alternative","").split(",")).filter(t->!t.isEmpty()).forEach(o->{
					String[] s = o.split(":");
					if(s.length==2)
						_alternative.put(Race.valueOf(s[0].toLowerCase()), Integer.parseInt(s[1]));
				});

				int extract_id = Integer.parseInt(visualElement.attributeValue("extract_id"));
				ExItemType item_type = ExItemType.valueOf(visualElement.attributeValue("item_type").toUpperCase());
				
				_visual.put(item_type, new Visual(extract_id,_alternative));
			}
			
			AppearanceStone stone = new AppearanceStone(itemId, targetTypes, type, grades, cost, _visual, period);

			for(Iterator<Element> skillIterator = stoneElement.elementIterator("skill"); skillIterator.hasNext();)
			{
				Element skillElement = skillIterator.next();
				int skillId = Integer.parseInt(skillElement.attributeValue("id"));
				int skillLevel = Integer.parseInt(skillElement.attributeValue("level"));

				stone.addSkill(skillId, skillLevel);
			}

			getHolder().addAppearanceStone(stone);
		}
	}
}
