package l2s.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.instancemanager.PrivateStoreHistoryManager;
import l2s.gameserver.utils.TimeUtils;

public class WeeklyMondayTask extends AutomaticTask
{
	private static final Logger LOGGER = LoggerFactory.getLogger(WeeklyMondayTask.class);

	public WeeklyMondayTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		LOGGER.info("Weekly Global Task (Monday): launched.");
		PrivateStoreHistoryManager.getInstance().reCalcHistory();
		LOGGER.info("Weekly Global Task (Monday): completed.");
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return TimeUtils.WEEKLY_MONDAY_DATE_PATTERN.next(System.currentTimeMillis());
	}
}