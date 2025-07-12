package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.RelicsCouponHolder;
import l2s.gameserver.templates.relics.RelicsCoupon;
import l2s.gameserver.templates.relics.RelicsProb;

public final class RelicsCouponParser extends AbstractParser<RelicsCouponHolder> 
{
	private static final RelicsCouponParser _instance = new RelicsCouponParser();

	public static RelicsCouponParser getInstance() 
	{
		return _instance;
	}

	private RelicsCouponParser() 
	{
		super(RelicsCouponHolder.getInstance());
	}

	@Override
	public File getXMLPath() 
	{
		return new File(Config.DATAPACK_ROOT, "data/relics/relics_coupon.xml");
	}

	@Override
	public String getDTDFileName() 
	{
		return "relics_coupon.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception 
	{
		for (Iterator<Element> iterator = rootElement.elementIterator("coupon"); iterator.hasNext();) 
		{
			Element cuponElement = iterator.next();
			int id = Integer.parseInt(cuponElement.attributeValue("id"));
			int count = Integer.parseInt(cuponElement.attributeValue("count", "1"));

			RelicsCoupon coupon = new RelicsCoupon(id, count);

			for (Iterator<Element> relicIterator = cuponElement.elementIterator("relic"); relicIterator.hasNext();) 
			{
				Element relicElement = relicIterator.next();
				coupon.addRelicProb(new RelicsProb(Integer.parseInt(relicElement.attributeValue("id")),Long.parseLong(relicElement.attributeValue("chance", "0"))));
			}

			getHolder().addCoupon(coupon);
		}
	}
}
