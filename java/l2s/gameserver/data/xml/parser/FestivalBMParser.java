package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.FestivalBMHolder;
import l2s.gameserver.templates.FestivalBMTemplate;

/**
 * @author nexvill
 **/
public class FestivalBMParser extends AbstractParser<FestivalBMHolder>
{
	private static FestivalBMParser _instance = new FestivalBMParser();

	public static FestivalBMParser getInstance()
	{
		return _instance;
	}

	private FestivalBMParser()
	{
		super(FestivalBMHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/essence/festival_data/FestivalBMData.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "FestivalBMData.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		int i = 1;
		for (Iterator<Element> iterator = rootElement.elementIterator("item"); iterator.hasNext();)
		{
			Element element = iterator.next();

			int itemId = parseInt(element, "id");
			int count = parseInt(element, "count");
			int locationId = parseInt(element, "locationId");

			getHolder().addFestivalBMInfo(i, new FestivalBMTemplate(itemId, count, locationId));
			i++;
		}
	}
}
