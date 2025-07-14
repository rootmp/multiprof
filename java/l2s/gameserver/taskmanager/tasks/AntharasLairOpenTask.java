package l2s.gameserver.taskmanager.tasks;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.instancemanager.ServerVariables;

/**
 * @author nexvill
 */
public class AntharasLairOpenTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(AntharasLairOpenTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("00 18 * * mon,wed,fri");

	public AntharasLairOpenTask()
	{
		super();
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		if((now.getDayOfWeek() == DayOfWeek.MONDAY) || (now.getDayOfWeek() == DayOfWeek.WEDNESDAY) || (now.getDayOfWeek() == DayOfWeek.FRIDAY))
		{
			if(now.getHour() >= 18)
			{
				ServerVariables.set("antharas_lair_open", true);
			}
			else
			{
				ServerVariables.set("antharas_lair_open", false);
			}
		}
		_log.info("Lair of Antharas Open Task: Started.");
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Antharas' Lair Open Task: launched.");
		ServerVariables.set("antharas_lair_open", true);
		_log.info("Antharas' Lair Open Task: completed.");
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}