package handler.dailymissions;

import l2s.commons.time.cron.SchedulingPattern;

/**
 * @author Bonux
 **/
public class WeeklyHunting extends DailyHunting
{
	private static final SchedulingPattern REUSE_PATTERN = new SchedulingPattern("30 6 * * 1");

	@Override
	public SchedulingPattern getReusePattern()
	{
		return REUSE_PATTERN;
	}
}
