package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.TeleportListHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.templates.TeleportTemplate;

public class TeleportListParser extends AbstractParser<TeleportListHolder>
{
	private static TeleportListParser _instance = new TeleportListParser();

	public static TeleportListParser getInstance()
	{
		return _instance;
	}

	private TeleportListParser()
	{
		super(TeleportListHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/teleporter_data/teleport_list.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "teleport_list.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> iterator = rootElement.elementIterator("teleport"); iterator.hasNext();)
		{
			Element element = iterator.next();

			int id = parseInt(element, "id");
			int itemId = parseInt(element, "itemId");
			long price = parseInt(element, "price");

			TeleportTemplate teleport = new TeleportTemplate(id, itemId, price);

			for(Iterator<Element> coordsIterator = element.elementIterator("coords"); coordsIterator.hasNext();)
			{
				Element coordsElement = coordsIterator.next();
				if(coordsElement.getName().equals("coords"))
					for(Element e : coordsElement.elements())
					{
						int x = Integer.parseInt(e.attributeValue("x"));
						int y = Integer.parseInt(e.attributeValue("y"));
						int z = Integer.parseInt(e.attributeValue("z"));
						teleport.addLocation(new Location(x, y, z));
					}
			}

			getHolder().addTeleportInfo(teleport);
		}
	}
}
