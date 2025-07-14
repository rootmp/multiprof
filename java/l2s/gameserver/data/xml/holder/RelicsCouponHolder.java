package l2s.gameserver.data.xml.holder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.templates.relics.RelicsCoupon;
import l2s.gameserver.templates.relics.RelicsProb;

public final class RelicsCouponHolder extends AbstractHolder
{
	private static final RelicsCouponHolder _instance = new RelicsCouponHolder();

	private final Map<Integer, RelicsCoupon> coupons = new HashMap<>();

	public static RelicsCouponHolder getInstance()
	{
		return _instance;
	}

	public void addCoupon(RelicsCoupon coupon)
	{
		coupons.put(coupon.getId(), coupon);
	}

	public RelicsCoupon getCoupon(int id)
	{
		return coupons.get(id);
	}

	@Override
	public int size()
	{
		return coupons.size();
	}

	@Override
	public void clear()
	{
		coupons.clear();
	}

	public List<RelicsProb> getCouponProb(int key)
	{
		RelicsCoupon coupon = getCoupon(key);
		return coupon != null ? coupon.getRelicsProb() : Collections.emptyList();
	}
}
