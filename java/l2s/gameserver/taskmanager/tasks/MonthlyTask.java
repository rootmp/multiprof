package l2s.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;

/**
 * @author nexvill
 */
public class MonthlyTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(MonthlyTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("30 6 1 * *");

	public MonthlyTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Monthly Global Task: launched.");
		for(Player player : GameObjectsStorage.getPlayers(true, true))
			player.restartMonthlyCounters(false);
		_log.info("Monthly Global Task: completed.");
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}