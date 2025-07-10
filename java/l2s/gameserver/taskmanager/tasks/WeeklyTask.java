package l2s.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.database.MySqlDataInsert;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class WeeklyTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(WeeklyTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("30 6 * * wed");

	public WeeklyTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Weekly Global Task: launched.");
		for (Player player : GameObjectsStorage.getPlayers(true, true))
			player.restartWeeklyCounters(false);
		MySqlDataInsert.set("UPDATE character_variables SET value = 0 WHERE name = \"weekly_contribution\"");
		_log.info("Weekly Global Task: completed.");
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}