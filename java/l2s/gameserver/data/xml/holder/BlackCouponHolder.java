package l2s.gameserver.data.xml.holder;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.BlackCoupon;

/**
 * @author Bonux (bonuxq@gmail.com) 06.01.2022
 **/
public final class BlackCouponHolder extends AbstractHolder
{
	private static final BlackCouponHolder INSTANCE = new BlackCouponHolder();

	public static BlackCouponHolder getInstance()
	{
		return INSTANCE;
	}

	private final Map<Integer, BlackCoupon> blackCoupons = new HashMap<>();

	public void addBlackCoupon(BlackCoupon blackCoupon)
	{
		blackCoupons.put(blackCoupon.getItemId(), blackCoupon);
	}

	public BlackCoupon getBlackCoupon(int itemId)
	{
		return blackCoupons.get(itemId);
	}

	@Override
	public int size()
	{
		return blackCoupons.size();
	}

	@Override
	public void clear()
	{
		blackCoupons.clear();
	}
}
