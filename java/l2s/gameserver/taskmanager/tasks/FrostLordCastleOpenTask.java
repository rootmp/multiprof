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
public class FrostLordCastleOpenTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(FrostLordCastleOpenTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("00 18 * * tue,thu,sat");

	public FrostLordCastleOpenTask()
	{
		super();
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		if((now.getDayOfWeek() == DayOfWeek.TUESDAY) || (now.getDayOfWeek() == DayOfWeek.THURSDAY) || (now.getDayOfWeek() == DayOfWeek.SATURDAY))
		{
			if(now.getHour() >= 18)
			{
				ServerVariables.set("frost_lord_castle_open", true);
			}
			else
			{
				ServerVariables.set("frost_lord_castle_open", false);
			}
		}
		_log.info("Frost Lord Castle Task: Started.");
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Frost Lord Castle Open Task: launched.");
		ServerVariables.set("frost_lord_castle_open", true);
		_log.info("Frost Lord Castle Open Task: completed.");
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}