package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.CapsuleHolder;
import l2s.gameserver.templates.item.capsule.CreateItemGroupInfo;
import l2s.gameserver.templates.item.capsule.CreateItemInfo;

public final class CapsuleParser extends AbstractParser<CapsuleHolder>
{
	private static final CapsuleParser _instance = new CapsuleParser();

	public static CapsuleParser getInstance()
	{
		return _instance;
	}

	private CapsuleParser()
	{
		super(CapsuleHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/capsule/capsule.xml");
	}

	@Override
	public File getCustomXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/capsule/capsule_custom.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "capsule.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		boolean ptsChanceFormat = false;
		Element configElement = rootElement.element("config");
		if(configElement != null)
		{
			String ptsAttr = configElement.attributeValue("pts_chance");
			if(ptsAttr != null && ptsAttr.equalsIgnoreCase("true"))
				ptsChanceFormat = true;
		}

		for(Iterator<Element> iterator = rootElement.elementIterator("capsule"); iterator.hasNext();)
		{
			Element capsuleElement = iterator.next();
			int itemId = Integer.parseInt(capsuleElement.attributeValue("item_id"));

			List<CreateItemInfo> createItems = new ArrayList<>();
			List<CreateItemInfo> createRandomItems = new ArrayList<>();
			List<CreateItemGroupInfo> createMultiItems = new ArrayList<>();

			Element itemsElement = capsuleElement.element("items");
			if(itemsElement != null)
			{
				for(Iterator<Element> itemIterator = itemsElement.elementIterator("item"); itemIterator.hasNext();)
				{
					Element itemElement = itemIterator.next();
					int id = Integer.parseInt(itemElement.attributeValue("id"));
					int enchant = itemElement.attributeValue("enchant") != null ? Integer.parseInt(itemElement.attributeValue("enchant")) : 0;
					long count = Long.parseLong(itemElement.attributeValue("count"));
					long chance = ptsChanceFormat ? Long.parseLong(itemElement.attributeValue("chance")) : (long) (Double.parseDouble(itemElement.attributeValue("chance"))
							* 100000000);
					createItems.add(new CreateItemInfo(id, enchant, count, chance));
				}
			}

			Element randomItemsElement = capsuleElement.element("randomItems");
			if(randomItemsElement != null)
			{
				long totalChance = 0;
				List<CreateItemInfo> tempList = new ArrayList<>();
				long maxChance = 0;
				int maxIndex = -1;

				for(Iterator<Element> itemIterator = randomItemsElement.elementIterator("item"); itemIterator.hasNext();)
				{
					Element itemElement = itemIterator.next();
					int id = Integer.parseInt(itemElement.attributeValue("id"));
					int enchant = itemElement.attributeValue("enchant") != null ? Integer.parseInt(itemElement.attributeValue("enchant")) : 0;
					long count = Long.parseLong(itemElement.attributeValue("count"));
					long chance = ptsChanceFormat ? Long.parseLong(itemElement.attributeValue("chance")) : Math.round(Double.parseDouble(itemElement.attributeValue("chance"))
							* 100000000);
					if(chance > maxChance)
					{
						maxChance = chance;
						maxIndex = tempList.size();
					}

					totalChance += chance;
					tempList.add(new CreateItemInfo(id, enchant, count, chance));
				}

				if(totalChance != 10000000000L)
				{
					_log.warn("Capsule item id " + itemId + " has total chance " + totalChance + " in createRandomItems not equal to 10000000000.");

					if(totalChance < 10000000000L && maxIndex != -1)
					{
						long diff = 10000000000L - totalChance;
						CreateItemInfo maxItem = tempList.get(maxIndex);

						CreateItemInfo updated = new CreateItemInfo(maxItem.getItemId(), maxItem.getEnchant(), maxItem.getItemCount(), maxItem.getProb() + diff);
						tempList.set(maxIndex, updated);

						_log.info("Adjusted chance for item id " + maxItem.getItemId() + " by adding " + diff + " to meet total 10000000000.");
					}
				}

				createRandomItems.addAll(tempList);
			}

			Element multiItemsElement = capsuleElement.element("multiItems");
			if(multiItemsElement != null)
			{
				for(Iterator<Element> groupIterator = multiItemsElement.elementIterator("group"); groupIterator.hasNext();)
				{
					Element groupElement = groupIterator.next();
					int groupId = Integer.parseInt(groupElement.attributeValue("id"));

					List<CreateItemInfo> items = new ArrayList<>();
					long totalGroupChance = 0;
					for(Iterator<Element> itemIterator = groupElement.elementIterator("item"); itemIterator.hasNext();)
					{
						Element itemElement = itemIterator.next();
						int id = Integer.parseInt(itemElement.attributeValue("id"));
						int enchant = itemElement.attributeValue("enchant") != null ? Integer.parseInt(itemElement.attributeValue("enchant")) : 0;
						long count = Long.parseLong(itemElement.attributeValue("count"));
						long chance = ptsChanceFormat ? Long.parseLong(itemElement.attributeValue("chance")) : (long) (Double.parseDouble(itemElement.attributeValue("chance"))
								* 100000000);
						totalGroupChance += chance;
						items.add(new CreateItemInfo(id, enchant, count, chance));
					}
					if(totalGroupChance > 10000000000L)
					{
						_log.warn("Capsule item id " + itemId + " has total chance " + totalGroupChance + " in group id " + groupId + " exceeding 10000000000.");
					}
					createMultiItems.add(new CreateItemGroupInfo(groupId, items));
				}
			}

			CapsuleHolder.getInstance().addCapsule(itemId, createItems, createRandomItems, createMultiItems);
		}
	}
}
