package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.HuntPassHolder;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.templates.item.data.ItemData;

public class HuntPassParser extends AbstractParser<HuntPassHolder>
{	
	private static HuntPassParser _instance = new HuntPassParser();

	public static HuntPassParser getInstance()
	{
		return _instance;
	}
	
	private HuntPassParser()
	{
		super(HuntPassHolder.getInstance());
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/HuntPass.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "HuntPass.dtd";
	}


	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();
			
			final int itemId = Integer.parseInt(element.attributeValue("id"));
			final int itemCount = Integer.parseInt(element.attributeValue("count"));
			final int premiumitemId = Integer.parseInt(element.attributeValue("premiumId"));
			final int premiumitemCount = Integer.parseInt(element.attributeValue("premiumCount"));
			
			if(ItemHolder.getInstance().getTemplate(itemId)!=null )
				getHolder().addRewards(new ItemData(itemId,itemCount));
			
			if(ItemHolder.getInstance().getTemplate(premiumitemId)!=null)
				getHolder().addPremiumRewards(new ItemData(premiumitemId, premiumitemCount));

		}
		
	}
}
