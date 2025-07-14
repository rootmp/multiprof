package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.EnsoulHolder;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.support.Ensoul;
import l2s.gameserver.templates.item.support.EnsoulFee;
import l2s.gameserver.templates.item.support.EnsoulFee.EnsoulFeeInfo;
import l2s.gameserver.templates.item.support.EnsoulFee.EnsoulFeeItem;

/**
 * @author Bonux
**/
public final class EnsoulParser extends AbstractParser<EnsoulHolder>
{
	private static final EnsoulParser _instance = new EnsoulParser();

	public static EnsoulParser getInstance()
	{
		return _instance;
	}

	private EnsoulParser()
	{
		super(EnsoulHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/ensoul_data.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "ensoul_data.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> iterator = rootElement.elementIterator("ensoul_fee_data"); iterator.hasNext();)
		{
			Element element = iterator.next();

			for(Iterator<Element> feeIterator = element.elementIterator("ensoul_fee"); feeIterator.hasNext();)
			{
				Element feeElement = feeIterator.next();
				if(feeElement.attributeValue("weapon") !=null)
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("weapon").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_R_HAND,_option_id, ensoulFee);
					getHolder().addEnsoulFee(ItemTemplate.SLOT_LR_HAND,_option_id, ensoulFee);
				}
				
				if(feeElement.attributeValue("legs") !=null)
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("legs").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_LEGS,_option_id, ensoulFee);
				}
				
				if(feeElement.attributeValue("feet") !=null)
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("feet").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_FEET,_option_id, ensoulFee);
				}
				
				if(feeElement.attributeValue("head") !=null)
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("head").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_HEAD,_option_id, ensoulFee);
				}
				if(feeElement.attributeValue("gloves") !=null)
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("gloves").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_GLOVES,_option_id, ensoulFee);
				}
				
				if(feeElement.attributeValue("onepiece") !=null)
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("onepiece").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_FULL_ARMOR,_option_id, ensoulFee);
				}
				if(feeElement.attributeValue("chest") !=null)
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("chest").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_CHEST,_option_id, ensoulFee);
				}
				if(feeElement.attributeValue("finger") !=null) //TODO
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("finger").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_R_FINGER,_option_id, ensoulFee);
					getHolder().addEnsoulFee(ItemTemplate.SLOT_L_FINGER,_option_id, ensoulFee);
				}
				if(feeElement.attributeValue("ear") !=null)
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("ear").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_R_EAR,_option_id, ensoulFee);
					getHolder().addEnsoulFee(ItemTemplate.SLOT_L_EAR,_option_id, ensoulFee);
				}
				if(feeElement.attributeValue("neck") !=null)
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("neck").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_NECK,_option_id, ensoulFee);
				}
			/*	if(feeElement.attributeValue("shield") !=null)
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("shield").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_NECK,_option_id, ensoulFee);
				}
				if(feeElement.attributeValue("sigil") !=null)
				{
					List<Integer> _option_id = new ArrayList<Integer>();
					Stream.of(feeElement.attributeValue("sigil").split(",")).forEach(o->_option_id.add(Integer.parseInt(o)));

					EnsoulFee ensoulFee = new EnsoulFee();

					for(Iterator<Element> feeInfoIterator = feeElement.elementIterator("ensoul_fee_info"); feeInfoIterator.hasNext();)
					{
						Element feeInfoElement = feeInfoIterator.next();

						int type = Integer.parseInt(feeInfoElement.attributeValue("type"));

						ensoulFee.addFeeInfo(type, parseFeeInfo(feeInfoElement));
					}
					getHolder().addEnsoulFee(ItemTemplate.SLOT_NECK,_option_id, ensoulFee);
				}*/
			}
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("ensoul_data"); iterator.hasNext();)
		{
			Element element = iterator.next();

			for(Iterator<Element> ensoulIterator = element.elementIterator("ensoul"); ensoulIterator.hasNext();)
			{
				Element ensoulElement = ensoulIterator.next();

				int id = Integer.parseInt(ensoulElement.attributeValue("id"));
				int itemId = ensoulElement.attributeValue("item_id") == null ? 0 : Integer.parseInt(ensoulElement.attributeValue("item_id"));
				int extractionItemId = ensoulElement.attributeValue("extraction_item_id") == null ? 0 : Integer.parseInt(ensoulElement.attributeValue("extraction_item_id"));
				Ensoul ensoul = new Ensoul(id, itemId, extractionItemId);

				for(Iterator<Element> skillIterator = ensoulElement.elementIterator("skill"); skillIterator.hasNext();)
				{
					Element skillElement = skillIterator.next();

					int skillId = Integer.parseInt(skillElement.attributeValue("id"));
					int skillLevel = Integer.parseInt(skillElement.attributeValue("level"));

					ensoul.addSkill(skillId, skillLevel);
				}
				getHolder().addEnsoul(ensoul);
			}
		}
	}

	private static EnsoulFeeInfo parseFeeInfo(Element rootElement)
	{
		EnsoulFeeInfo feeInfo = new EnsoulFeeInfo();
		for(Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();

			if(element.getName().equals("insert"))
				feeInfo.setInsertFee(parseFeeItems(element));
			else if(element.getName().equals("change"))
				feeInfo.setChangeFee(parseFeeItems(element));
			else if(element.getName().equals("remove"))
				feeInfo.setRemoveFee(parseFeeItems(element));

		}
		return feeInfo;
	}

	//TODO убрать лист
	private static List<EnsoulFeeItem> parseFeeItems(Element rootElement)
	{
		List<EnsoulFeeItem> items = new ArrayList<EnsoulFeeItem>();
			int itemId = Integer.parseInt(rootElement.attributeValue("id"));
			long itemCount = Long.parseLong(rootElement.attributeValue("count"));
			items.add(new EnsoulFeeItem(itemId, itemCount));
		
		return items;
	}
}
