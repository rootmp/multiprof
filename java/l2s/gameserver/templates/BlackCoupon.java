package l2s.gameserver.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import l2s.gameserver.model.items.EnchantBrokenItem;

/**
 * @author Bonux (bonuxq@gmail.com)
 * @date 06.01.2022
 **/
public class BlackCoupon
{
	private final int itemId;
	private final List<RestorableItems> restorableItems = new ArrayList<>();

	public BlackCoupon(int itemId)
	{
		this.itemId = itemId;
	}

	public int getItemId()
	{
		return itemId;
	}

	public void addRestorableItems(int lostMinTime, int lostMaxTime, Map<Integer, Integer> items)
	{
		restorableItems.add(new RestorableItems(lostMinTime, lostMaxTime, items));
	}

	public int getFixedId(EnchantBrokenItem item)
	{
		for(RestorableItems r : restorableItems)
		{
			if(r.lostMinTime > item.getTime())
				continue;

			if(r.lostMaxTime < item.getTime())
				continue;

			return r.items.getOrDefault(item.getId(), -1);
		}
		return -1;
	}

	private static class RestorableItems
	{
		public final int lostMinTime, lostMaxTime;
		public final Map<Integer, Integer> items;

		public RestorableItems(int lostMinTime, int lostMaxTime, Map<Integer, Integer> items)
		{
			this.lostMinTime = lostMinTime;
			this.lostMaxTime = lostMaxTime;
			this.items = items;
		}
	}
}
