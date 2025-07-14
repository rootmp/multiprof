package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import l2s.commons.math.random.RndSelector;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.data.xml.holder.OptionDataHolder;
import l2s.gameserver.data.xml.holder.SkillHolder;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.base.Element;
import l2s.gameserver.skills.SkillEntry;
import l2s.gameserver.skills.enums.SkillEntryType;
import l2s.gameserver.stats.conditions.Condition;
import l2s.gameserver.templates.OptionDataTemplate;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.agathion.AgathionData;
import l2s.gameserver.templates.agathion.AgathionEnchantData;
import l2s.gameserver.templates.item.ArmorTemplate;
import l2s.gameserver.templates.item.Bodypart;
import l2s.gameserver.templates.item.EtcItemTemplate;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.WeaponTemplate;
import l2s.gameserver.templates.item.data.CapsuledItemData;
import l2s.gameserver.templates.item.data.ChancedItemData;

/**
 * @author VISTALL
 * @date 11:26/15.01.2011
 */
public final class ItemParser extends StatParser<ItemHolder>
{
	private static final ItemParser _instance = new ItemParser();

	public static ItemParser getInstance()
	{
		return _instance;
	}

	protected ItemParser()
	{
		super(ItemHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/items/");
	}

	@Override
	public File getCustomXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/items/custom/");
	}

	@Override
	public String getDTDFileName()
	{
		return "item.dtd";
	}

	@Override
	protected void readData(org.dom4j.Element rootElement) throws Exception
	{
		for(Iterator<org.dom4j.Element> itemIterator = rootElement.elementIterator(); itemIterator.hasNext();)
		{
			org.dom4j.Element itemElement = itemIterator.next();
			StatsSet set = new StatsSet();
			set.set("item_id", itemElement.attributeValue("id"));
			set.set("name", itemElement.attributeValue("name"));
			set.set("add_name", itemElement.attributeValue("add_name", StringUtils.EMPTY));

			long slot = 0;
			for(Iterator<org.dom4j.Element> subIterator = itemElement.elementIterator(); subIterator.hasNext();)
			{
				org.dom4j.Element subElement = subIterator.next();
				String subName = subElement.getName();
				if(subName.equalsIgnoreCase("set"))
				{
					set.set(subElement.attributeValue("name"), subElement.attributeValue("value"));
				}
				else if(subName.equalsIgnoreCase("equip"))
				{
					for(Iterator<org.dom4j.Element> slotIterator = subElement.elementIterator(); slotIterator.hasNext();)
					{
						org.dom4j.Element slotElement = slotIterator.next();
						Bodypart bodypart = Bodypart.valueOf(slotElement.attributeValue("id"));
						if(bodypart.getReal() != null)
							slot = bodypart.mask();
						else
							slot |= bodypart.mask();
					}
				}
			}

			set.set("bodypart", slot);

			ItemTemplate template = null;
			try
			{
				if(itemElement.getName().equalsIgnoreCase("weapon"))
					template = new WeaponTemplate(set);
				else if(itemElement.getName().equalsIgnoreCase("armor"))
					template = new ArmorTemplate(set);
				else
					// if(itemElement.getName().equalsIgnoreCase("etcitem"))
					template = new EtcItemTemplate(set);
			}
			catch(Exception e)
			{
				// for(Map.Entry<String, Object> entry : set.entrySet())
				// {
				// info("set " + entry.getKey() + ":" + entry.getValue());
				// }
				warn("Fail create item: " + set.get("item_id"), e);
				continue;
			}

			for(Iterator<org.dom4j.Element> subIterator = itemElement.elementIterator(); subIterator.hasNext();)
			{
				org.dom4j.Element subElement = subIterator.next();
				String subName = subElement.getName();
				if(subName.equalsIgnoreCase("for"))
				{
					parseFor(subElement, template);
				}
				else if(subName.equalsIgnoreCase("triggers"))
				{
					parseTriggers(subElement, template);
				}
				else if(subName.equalsIgnoreCase("skills"))
				{
					for(Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator("skill"); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();
						int id = Integer.parseInt(nextElement.attributeValue("id"));
						int level = Integer.parseInt(nextElement.attributeValue("level"));

						Skill skill = SkillHolder.getInstance().getSkill(id, level);

						if(skill != null)
						{
							template.attachSkill(skill);
						}
						else
							warn("Skill not found(" + id + "," + level + ") for item:" + set.getObject("item_id") + "; file:" + getCurrentFileName());
					}

					for(Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator("enchant_skill"); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();
						int id = Integer.parseInt(nextElement.attributeValue("id"));
						int level = Integer.parseInt(nextElement.attributeValue("level"));
						int enchant = Integer.parseInt(nextElement.attributeValue("enchant"));

						Skill skill = SkillHolder.getInstance().getSkill(id, level);

						if(skill != null)
						{
							template.addEnchantSkill(enchant, skill);
						}
						else
							warn("Skill not found(" + id + "," + level + ") for item:" + set.getObject("item_id") + "; file:" + getCurrentFileName());
					}
				}
				else if(subName.equalsIgnoreCase("cond"))
				{
					Condition condition = parseFirstCond(subElement);
					if(condition != null)
					{
						if(subElement.attributeValue("msgId") != null)
						{
							int msgId = parseTableNumber(subElement.attributeValue("msgId")).intValue();
							condition.setSystemMsg(msgId);
						}

						template.addCondition(condition);
					}
				}
				else if(subName.equalsIgnoreCase("attributes"))
				{
					int[] attributes = new int[6];
					for(Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();
						Element element;
						if(nextElement.getName().equalsIgnoreCase("attribute"))
						{
							element = Element.getElementByName(nextElement.attributeValue("element"));
							attributes[element.getId()] = Integer.parseInt(nextElement.attributeValue("value"));
						}
					}
					template.setBaseAtributeElements(attributes);
				}
				else if(subName.equalsIgnoreCase("capsuled_items"))
				{
					for(Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();
						if(nextElement.getName().equalsIgnoreCase("capsuled_item"))
						{
							int c_item_id = Integer.parseInt(nextElement.attributeValue("id"));
							long c_min_count = Long.parseLong(nextElement.attributeValue("min_count"));
							long c_max_count = Long.parseLong(nextElement.attributeValue("max_count"));
							double c_chance = nextElement.attributeValue("chance") == null ? 100. : Double.parseDouble(nextElement.attributeValue("chance"));
							int enchant = nextElement.attributeValue("enchant") == null ? 0 : Integer.parseInt(nextElement.attributeValue("enchant"));
							boolean announce = nextElement.attributeValue("announce") != null && Boolean.parseBoolean(nextElement.attributeValue("announce"));
							template.addCapsuledItem(new CapsuledItemData(c_item_id, c_min_count, c_max_count, c_chance, enchant, announce));
						}
					}
				}
				else if(subName.equalsIgnoreCase("create_items"))
				{
					for(org.dom4j.Element ciElement : subElement.elements("create_item"))
					{
						int c_item_id = parseInt(ciElement, "id");
						long c_count = parseLong(ciElement, "count");
						double c_chance = parseDouble(ciElement, "chance", 100.);
						int c_enchant = parseInt(ciElement, "enchant", 0);
						template.getCreateItems().add(new ChancedItemData(c_item_id, c_count, c_enchant, c_chance), c_chance);
					}
				}
				else if(subName.equalsIgnoreCase("create_item_groups"))
				{
					for(org.dom4j.Element cigElement : subElement.elements("create_item_group"))
					{
						RndSelector<ChancedItemData> createItems = new RndSelector<>();
						for(org.dom4j.Element ciElement : cigElement.elements("create_item"))
						{
							int c_item_id = parseInt(ciElement, "id");
							long c_count = parseLong(ciElement, "count");
							double c_chance = parseDouble(ciElement, "chance", 100.);
							int c_enchant = parseInt(ciElement, "enchant", 0);
							createItems.add(new ChancedItemData(c_item_id, c_count, c_enchant, c_chance), c_chance);
						}
						template.getCreateItemsGroups().add(createItems);
					}
				}
				else if(subName.equalsIgnoreCase("enchant_options"))
				{
					for(Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();

						if(nextElement.getName().equalsIgnoreCase("level"))
						{
							int val = Integer.parseInt(nextElement.attributeValue("value"));

							int i = 0;
							int[] options = new int[3];
							for(org.dom4j.Element optionElement : nextElement.elements())
							{
								OptionDataTemplate optionData = OptionDataHolder.getInstance().getTemplate(Integer.parseInt(optionElement.attributeValue("id")));
								if(optionData == null)
								{
									error("Not found option_data for id: " + optionElement.attributeValue("id") + "; item_id: " + set.get("item_id"));
									continue;
								}
								options[i++] = optionData.getId();
							}
							template.addEnchantOptions(val, options);
						}
					}
				}
				else if(subName.equalsIgnoreCase("agathion_data"))
				{
					AgathionData agathionData = new AgathionData();
					for(org.dom4j.Element element2 : subElement.elements("enchant"))
					{
						AgathionEnchantData itemEnchant = new AgathionEnchantData(parseInt(element2, "level"));
						for(org.dom4j.Element element3 : element2.elements("main_skills"))
						{
							for(org.dom4j.Element element4 : element3.elements("skill"))
							{
								itemEnchant.getMainSkills().add(SkillEntry.makeSkillEntry(SkillEntryType.ITEM, parseInt(element4, "id"), parseInt(element4, "level")));
							}
						}
						for(org.dom4j.Element element3 : element2.elements("sub_skills"))
						{
							for(org.dom4j.Element element4 : element3.elements("skill"))
							{
								itemEnchant.getSubSkills().add(SkillEntry.makeSkillEntry(SkillEntryType.ITEM, parseInt(element4, "id"), parseInt(element4, "level")));
							}
						}
						agathionData.addEnchant(itemEnchant);
					}
					template.setAgathionData(agathionData);
				}
			}
			getHolder().addItem(template);
		}
	}

	@Override
	protected Object getTableValue(String name, int... arg)
	{
		return null;
	}

	@Override
	protected void onParsed()
	{
		getHolder().callInit();
	}
}
