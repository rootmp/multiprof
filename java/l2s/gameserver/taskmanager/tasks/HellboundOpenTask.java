package l2s.gameserver.taskmanager.tasks;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.utils.ReflectionUtils;

/**
 * @author nexvill
 */
public class HellboundOpenTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(HellboundOpenTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("00 10 * * sat");

	public HellboundOpenTask()
	{
		super();
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		if(now.getDayOfWeek() == DayOfWeek.SATURDAY)
		{
			if((now.getHour() >= 10) && now.getHour() <= 22)
			{
				ServerVariables.set("hellbound_open", true);
				ReflectionUtils.getZone("[hellbound_debuff]").setActive(false);
			}
			else
			{
				ServerVariables.set("hellbound_open", false);
				ReflectionUtils.getZone("[hellbound_debuff]").setActive(true);
			}
		}
		_log.info("Hellbound Portal Task: Started.");
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Hellbound Open Task: launched.");
		ServerVariables.set("hellbound_open", true);
		ReflectionUtils.getZone("[hellbound_debuff]").setActive(false);
		_log.info("Hellbound Open Task: completed.");
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}