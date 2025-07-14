package l2s.gameserver.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import l2s.commons.time.cron.SchedulingPattern;

/**
 * @author VISTALL
 * @date 16:18/14.02.2011
 */
public class TimeUtils
{
	public static final SimpleDateFormat SIMPLE_FORMAT = new SimpleDateFormat("HH:mm dd.MM.yyyy");

	public static final SchedulingPattern DAILY_DATE_PATTERN = new SchedulingPattern("30 6 * * *");
	public static final SchedulingPattern WEEKLY_MONDAY_DATE_PATTERN = new SchedulingPattern("30 6 * * 1");
	public static final SchedulingPattern MONTHLY_DATE_PATTERN = new SchedulingPattern("30 6 1 * *");
	public static final SchedulingPattern WEEKLY_WEDNESDAY_DATE_PATTERN = new SchedulingPattern("30 6 * * 3");

	public static String toSimpleFormat(Calendar cal)
	{
		return SIMPLE_FORMAT.format(cal.getTime());
	}

	public static String toSimpleFormat(long cal)
	{
		return SIMPLE_FORMAT.format(cal);
	}

	public static Calendar getCalendarFromString(String datetime, String format)
	{
		DateFormat df = new SimpleDateFormat(format);
		try
		{
			Date time = df.parse(datetime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);

			return calendar;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static long getTimeFromString(String datetime, String dateFormat, TimeUnit timeUnit, long defaultValue)
	{
		if(datetime != null)
		{
			try
			{
				Date time = new SimpleDateFormat(dateFormat).parse(datetime);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(time);
				return timeUnit.convert(calendar.getTimeInMillis(), TimeUnit.MILLISECONDS);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return defaultValue;
	}

	public static long getTimeFromString(String datetime, String dateFormat, long defaultValue)
	{
		return getTimeFromString(datetime, dateFormat, TimeUnit.MILLISECONDS, defaultValue);
	}

	public static long getTimeFromString(String datetime, String dateFormat, TimeUnit timeUnit)
	{
		return getTimeFromString(datetime, dateFormat, timeUnit, 0L);
	}

	public static long getTimeFromString(String datetime, String dateFormat)
	{
		return getTimeFromString(datetime, dateFormat, 0L);
	}
}
