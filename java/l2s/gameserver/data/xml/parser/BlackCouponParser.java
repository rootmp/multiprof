package l2s.gameserver.data.xml.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.dom4j.Element;

import l2s.commons.data.xml.AbstractParser;
import l2s.gameserver.Config;
import l2s.gameserver.data.xml.holder.BlackCouponHolder;
import l2s.gameserver.templates.BlackCoupon;
import l2s.gameserver.utils.TimeUtils;

/**
 * @author Bonux (bonuxq@gmail.com) 06.01.2022
 **/
public final class BlackCouponParser extends AbstractParser<BlackCouponHolder>
{
	private static final BlackCouponParser INSTANCE = new BlackCouponParser();

	private BlackCouponParser()
	{
		super(BlackCouponHolder.getInstance());
	}

	public static BlackCouponParser getInstance()
	{
		return INSTANCE;
	}

	@Override
	public File getXMLPath()
	{
		return new File(Config.DATAPACK_ROOT, "data/parser/black_coupon/black_coupon.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "black_coupon.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for(Element element : rootElement.elements("coupon"))
		{
			int itemId = parseInt(element, "item_id");
			BlackCoupon blackCoupon = new BlackCoupon(itemId);
			for(Element element1 : element.elements("restorable_items"))
			{
				int lostMinTime = (int) TimeUtils.getTimeFromString(parseString(element1, "lost_min_time", null), "yyyy/MM/dd HH:mm", TimeUnit.SECONDS, 0);
				int lostMaxTime = (int) TimeUtils.getTimeFromString(parseString(element1, "lost_max_time", null), "yyyy/MM/dd HH:mm", TimeUnit.SECONDS, Integer.MAX_VALUE);
				Map<Integer, Integer> restorableItems = new HashMap<>();
				for(Element element2 : element1.elements("item"))
				{
					int brokenId = parseInt(element2, "broken_id");
					int fixedId = parseInt(element2, "fixed_id");
					restorableItems.put(brokenId, fixedId);
				}
				blackCoupon.addRestorableItems(lostMinTime, lostMaxTime, restorableItems);
			}
			getHolder().addBlackCoupon(blackCoupon);
		}
	}
}
