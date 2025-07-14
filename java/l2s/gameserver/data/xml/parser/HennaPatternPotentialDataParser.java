package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.HennaPatternPotentialDataHolder;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.item.data.ItemData;
import l2s.gameserver.templates.item.henna.DyePotential;
import l2s.gameserver.templates.item.henna.DyePotentialFee;

public class HennaPatternPotentialDataParser extends AbstractParser<HennaPatternPotentialDataHolder>
{
	private static final HennaPatternPotentialDataParser _instance = new HennaPatternPotentialDataParser();

	public static HennaPatternPotentialDataParser getInstance()
	{
		return _instance;
	}

	private HennaPatternPotentialDataParser()
	{
		super(HennaPatternPotentialDataHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/henna/hennaPatternPotential.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "hennaPatternPotential.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> firstIterator = rootElement.elementIterator(); firstIterator.hasNext();)
		{
			Element firstElement = firstIterator.next();
			switch(firstElement.getName())
			{
				case "enchantFees":
				{
					for(Iterator<Element> secondIterator = firstElement.elementIterator(); secondIterator.hasNext();)
					{
						Element secondElement = secondIterator.next();
						if("fee".equals(secondElement.getName()))
						{
							final StatsSet set = new StatsSet();

							for(Attribute attr : secondElement.attributes())
								set.set(attr.getName(), attr.getValue());

							final int step = Integer.parseInt(secondElement.attributeValue("step"));
							int itemId = 0;
							int altItemId = 0;
							long itemCount = 0;
							long altItemCount = 0;
							int dailyCount = 0;
							final Map<Integer, Double> enchantExp = new HashMap<>();
							for(Iterator<Element> thirdIterator = secondElement.elementIterator(); thirdIterator.hasNext();)
							{
								Element thirdElement = thirdIterator.next();
								switch(thirdElement.getName())
								{
									case "requiredItem":
									{
										itemId = Integer.parseInt(thirdElement.attributeValue("id"));
										itemCount = Long.parseLong(thirdElement.attributeValue("count"));
										break;
									}
									case "altRequiredItem":
									{
										altItemId = Integer.parseInt(thirdElement.attributeValue("id"));
										altItemCount = Long.parseLong(thirdElement.attributeValue("count"));
										break;
									}
									case "dailyCount":
									{
										dailyCount = Integer.parseInt(thirdElement.getStringValue());
										break;
									}
									case "enchantExp":
									{
										enchantExp.put(Integer.parseInt(thirdElement.attributeValue("count")), Double.parseDouble(thirdElement.attributeValue("chance")));
										break;
									}
									default:
										break;
								}
							}
							getHolder().addPotenFees(step, new DyePotentialFee(step, new ItemData(itemId, itemCount), new ItemData(altItemId, altItemCount), dailyCount, enchantExp));
						}
					}
					break;
				}
				case "experiencePoints":
				{
					for(Iterator<Element> secondIterator = firstElement.elementIterator(); secondIterator.hasNext();)
					{
						Element secondElement = secondIterator.next();

						if("hiddenPower".equals(secondElement.getName()))
						{

							final StatsSet set = new StatsSet();
							for(Attribute attr : secondElement.attributes())
								set.set(attr.getName(), attr.getValue());
							final int level = Integer.parseInt(secondElement.attributeValue("level"));
							final int exp = Integer.parseInt(secondElement.attributeValue("exp"));
							getHolder().addPotenExpTable(level, exp);

							if(getHolder().getMaxPotenLevel() < level)
								getHolder().setMaxPotenLevel(level);
						}
					}
					break;
				}
				case "hiddenPotentials":
				{
					for(Iterator<Element> secondIterator = firstElement.elementIterator(); secondIterator.hasNext();)
					{
						Element secondElement = secondIterator.next();
						if("poten".equals(secondElement.getName()))
						{
							final StatsSet set = new StatsSet();
							for(Attribute attr : secondElement.attributes())
								set.set(attr.getName(), attr.getValue());

							final int id = Integer.parseInt(secondElement.attributeValue("id"));
							final int slotId = Integer.parseInt(secondElement.attributeValue("slotId"));
							final int maxSkillLevel = Integer.parseInt(secondElement.attributeValue("maxSkillLevel"));
							final int skillId = Integer.parseInt(secondElement.attributeValue("skillId"));
							getHolder().addPotentials(id, new DyePotential(id, slotId, skillId, maxSkillLevel));
						}
					}
					break;
				}
				default:
					break;
			}
		}
	}

}