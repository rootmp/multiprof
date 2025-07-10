package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.CollectionsHolder;
import l2s.gameserver.templates.CollectionTemplate;
import l2s.gameserver.templates.item.data.CollectionItemData;

/**
 * @author nexvill
 */
public class CollectionsParser extends AbstractParser<CollectionsHolder>
{
	private static final CollectionsParser INSTANCE = new CollectionsParser();

	public static CollectionsParser getInstance()
	{
		return INSTANCE;
	}

	private CollectionsParser()
	{
		super(CollectionsHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/essence/collections.xml"); 
	}

	@Override
	public String getDTDFileName()
	{
		return "collections.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> iterator = rootElement.elementIterator("collection"); iterator.hasNext();)
		{
			Element firstElement = iterator.next();
			int id = parseInt(firstElement, "id");
			int tabId = parseInt(firstElement, "tab_id");
			int optionId = parseInt(firstElement, "option_id");
			CollectionTemplate template = new CollectionTemplate(id, tabId, optionId);

			for (Iterator<Element> secondIterator = firstElement.elementIterator("item"); secondIterator.hasNext();)
			{
				Element secondElement = secondIterator.next();
				int itemId = parseInt(secondElement, "id");
				int count = parseInt(secondElement, "count");
				int enchantLevel = parseInt(secondElement, "enchant", 0);
				int alternativeId = parseInt(secondElement, "alternative_id", 0);
				int slotId = parseInt(secondElement, "slot_id");

				template.addItem(new CollectionItemData(itemId, count, enchantLevel, alternativeId, slotId));
			}

			getHolder().addCollection(template);
		}
	}
}
