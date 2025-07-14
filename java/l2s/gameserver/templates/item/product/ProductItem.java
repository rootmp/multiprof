package l2s.gameserver.templates.item.product;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;

public class ProductItem implements Comparable<ProductItem>
{
	// Базовые параметры, если продукт не имеет лимита времени продаж
	public static final long NOT_LIMITED_START_TIME = 315547200000L;
	public static final long NOT_LIMITED_END_TIME = 2127445200000L;
	public static final int NOT_LIMITED_START_HOUR = 0;
	public static final int NOT_LIMITED_END_HOUR = 23;
	public static final int NOT_LIMITED_START_MIN = 0;
	public static final int NOT_LIMITED_END_MIN = 59;

	public static final SchedulingPattern DEFAULT_LIMIT_REFRESH_PATTERN = new SchedulingPattern("30 6 * * *");

	private final int _id;
	private final int _category;
	private final int _price;
	private final int _silverCoinCount;
	private final int _goldCoinCount;
	private final int _minVipLevel;
	private final int _maxVipLevel;
	private final int _limit;
	private final SchedulingPattern _limitRefreshPattern;
	private final boolean _isHot;
	private final boolean _isNew;
	private final int _locationId;

	private final long _startTimeSale;
	private final long _endTimeSale;

	private final boolean _onSale;

	private final List<ProductItemComponent> _components = new ArrayList<ProductItemComponent>();
	private int _groupLimit;

	private final int _tabId;
	private final int _startHour;
	private final int _endHour;
	private final int _startMin;
	private final int _endMin;
	private final int _discount;
	private final int _mainCategory;

	private final ProductPointsType _pointsType;
	private int _repurchase_interval;

	public ProductItem(int id, int category, int price, int silverCoinCount, int goldCoinCount, int minVipLevel, int maxVipLevel, int limit, int groupLimit, String limitRefreshPattern, boolean isHot, boolean isNew, long startTimeSale, long endTimeSale, boolean onSale, int locationId, ProductPointsType pointsType, int tabId, int mainCategoryId, int discount, int repurchase_interval)
	{
		_id = id;
		_category = category;
		_price = price;
		_silverCoinCount = silverCoinCount;
		_goldCoinCount = goldCoinCount;
		_minVipLevel = minVipLevel;
		_maxVipLevel = maxVipLevel;
		_limit = limit;
		_groupLimit = groupLimit;
		_limitRefreshPattern = limitRefreshPattern
				== null ? DEFAULT_LIMIT_REFRESH_PATTERN : (limitRefreshPattern.equals("-1") ? null : new SchedulingPattern(limitRefreshPattern));
		_isHot = isHot;
		_isNew = isNew;
		_onSale = onSale;
		_startTimeSale = startTimeSale > 0 ? startTimeSale : NOT_LIMITED_START_TIME;
		_endTimeSale = endTimeSale > 0 ? endTimeSale : NOT_LIMITED_END_TIME;
		_locationId = locationId;

		_tabId = tabId;
		_discount = discount;
		_mainCategory = mainCategoryId;

		_pointsType = pointsType;

		Calendar calendar;
		if(startTimeSale > 0)
		{
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(startTimeSale);
			_startHour = calendar.get(Calendar.HOUR_OF_DAY);
			_startMin = calendar.get(Calendar.MINUTE);
		}
		else
		{
			_startHour = NOT_LIMITED_START_HOUR;
			_startMin = NOT_LIMITED_START_MIN;
		}

		if(endTimeSale > 0)
		{
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(endTimeSale);

			_endHour = calendar.get(Calendar.HOUR_OF_DAY);
			_endMin = calendar.get(Calendar.MINUTE);
		}
		else
		{
			_endHour = NOT_LIMITED_END_HOUR;
			_endMin = NOT_LIMITED_END_MIN;
		}
		_repurchase_interval = repurchase_interval;
	}

	public void addComponent(ProductItemComponent component)
	{
		_components.add(component);
	}

	public List<ProductItemComponent> getComponents()
	{
		return _components;
	}

	public int getId()
	{
		return _id;
	}

	public int getCategory()
	{
		return _category;
	}

	public int getPrice()
	{
		return _price;
	}

	public int getSilverCoinCount()
	{
		return _silverCoinCount;
	}

	public int getGoldCoinCount()
	{
		return _goldCoinCount;
	}

	public int getMinVipLevel()
	{
		return _minVipLevel;
	}

	public int getMaxVipLevel()
	{
		return _maxVipLevel;
	}

	public int getLimit()
	{
		return _limit;
	}

	public int getAvailable(Player player)
	{
		return Math.max(0, getLimit() - player.getAccVar().getVarInt(PlayerVariables.VIP_SHOP_PRODUCT_COUNT_GROUP + getGroupLimit(), 0));
	}

	public SchedulingPattern getLimitRefreshPattern()
	{
		return _limitRefreshPattern;
	}

	public boolean isHot()
	{
		return _isHot;
	}

	public boolean isNew()
	{
		return _isNew;
	}

	public int getLocationId()
	{
		return _locationId;
	}

	public long getStartTimeSale()
	{
		return _startTimeSale;
	}

	public long getEndTimeSale()
	{
		return _endTimeSale;
	}

	public boolean isOnSale()
	{
		return _onSale/* && (_limit == -1 || _limit > 0)*/;
	}

	@Override
	public int compareTo(ProductItem o)
	{
		return getId() - o.getId();
	}

	public int getGroupLimit()
	{
		return _groupLimit;
	}

	public ProductPointsType getPointsType()
	{
		return _pointsType;
	}

	public int getPrice(boolean withDiscount)
	{
		if(withDiscount)
			return (int) (getPrice() * ((100 - _discount) * 0.01));
		return getPrice();
	}

	public int getTabId()
	{
		return _tabId;
	}

	public int getMainCategory()
	{
		return _mainCategory;
	}

	public int getStartHour()
	{
		return _startHour;
	}

	public int getStartMin()
	{
		return _startMin;
	}

	public int getEndHour()
	{
		return _endHour;
	}

	public int getEndMin()
	{
		return _endMin;
	}

	public int getDiscount()
	{
		return _discount;
	}

	public int getRepurchaseInterval()
	{
		return _repurchase_interval;
	}
}