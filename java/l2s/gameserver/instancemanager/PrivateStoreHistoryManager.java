package l2s.gameserver.instancemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import l2s.commons.math.SafeMath;
import l2s.gameserver.dao.PrivateStoreHistoryDAO;
import l2s.gameserver.model.items.PrivateStoreHistoryItem;
import l2s.gameserver.model.items.TradeItem;
import l2s.gameserver.utils.TimeUtils;

/**
 * @author Bonux (bonuxq@gmail.com)
 * @date 23.12.2021
 **/
public class PrivateStoreHistoryManager
{
	public static final int STORE_TYPE_SELL = 0;
	public static final int STORE_TYPE_BUY = 1;

	private static final PrivateStoreHistoryManager INSTANCE = new PrivateStoreHistoryManager();

	public static PrivateStoreHistoryManager getInstance()
	{
		return INSTANCE;
	}

	private static final int HISTORY_MAX_SIZE = 5;

	private final List<PrivateStoreHistoryItem> history = new CopyOnWriteArrayList<>();

	private final List<PrivateStoreHistoryItem> mostItems = new ArrayList<>(HISTORY_MAX_SIZE);
	private final List<PrivateStoreHistoryItem> highestItems = new ArrayList<>(HISTORY_MAX_SIZE);

	private PrivateStoreHistoryManager()
	{
		//
	}

	public void init()
	{
		long currCycleMillis = TimeUtils.WEEKLY_MONDAY_DATE_PATTERN.prev(System.currentTimeMillis());
		long prevCycleMillis = TimeUtils.WEEKLY_MONDAY_DATE_PATTERN.prev(currCycleMillis);

		PrivateStoreHistoryDAO.getInstance().deleteExpired(prevCycleMillis);
		PrivateStoreHistoryDAO.getInstance().select(prevCycleMillis, currCycleMillis, history);

		reCalcHistory();

		PrivateStoreHistoryDAO.getInstance().select(currCycleMillis, System.currentTimeMillis(), history);
	}

	public List<PrivateStoreHistoryItem> getHistory()
	{
		return history;
	}

	public List<PrivateStoreHistoryItem> getMostItems()
	{
		return mostItems;
	}

	public List<PrivateStoreHistoryItem> getHighestItems()
	{
		return highestItems;
	}

	public void reCalcHistory()
	{
		List<PrivateStoreHistoryItem> tempList = new ArrayList<>();
		Map<Integer, PrivateStoreHistoryItem> mostItemsMap = new HashMap<>();

		synchronized (history)
		{
			for(PrivateStoreHistoryItem historyItem : history)
			{
				if(historyItem.getStoreType() != STORE_TYPE_SELL)
					continue;

				PrivateStoreHistoryItem item = new PrivateStoreHistoryItem(historyItem);
				item.setPrice(SafeMath.mulAndCheck(item.getPrice(), item.getCount()));
				tempList.add(item);

				PrivateStoreHistoryItem mostItem = mostItemsMap.computeIfAbsent(historyItem.getItemId(), k -> new PrivateStoreHistoryItem(historyItem));
				mostItem.setTradesCount(mostItem.getTradesCount() + 1);
			}
			history.clear();
		}

		tempList.sort((o1, o2) -> Long.compare(o2.getPrice(), o1.getPrice()));

		highestItems.clear();
		highestItems.addAll(tempList.subList(0, Math.min(tempList.size(), HISTORY_MAX_SIZE)));

		tempList = new ArrayList<>(mostItemsMap.values());
		tempList.sort((o1, o2) -> Long.compare(o2.getTradesCount(), o1.getTradesCount()));

		mostItems.clear();
		mostItems.addAll(tempList.subList(0, Math.min(tempList.size(), HISTORY_MAX_SIZE)));
	}

	public void addHistory(int storeType, TradeItem tradeItem)
	{
		PrivateStoreHistoryItem historyItem = new PrivateStoreHistoryItem(tradeItem, storeType, (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
		if(PrivateStoreHistoryDAO.getInstance().insert(historyItem))
		{
			synchronized (history)
			{
				history.add(historyItem);
			}
		}
	}
}
