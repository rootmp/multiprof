package l2s.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.utils.ReflectionUtils;

/**
 * @author nexvill
 */
public class HellboundCloseTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(HellboundCloseTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("00 22 * * sat");

	public HellboundCloseTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Hellbound Close Task: launched.");
		ServerVariables.set("hellbound_open", false);
		ReflectionUtils.getZone("[hellbound_debuff]").setActive(true);
		_log.info("Hellbound Close Task: completed.");
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}