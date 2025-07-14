package l2s.gameserver.data.xml.holder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;

import l2s.commons.data.xml.AbstractHolder;
import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.templates.item.data.AttendanceRewardData;

public final class AttendanceRewardHolder extends AbstractHolder
{
	private static final AttendanceRewardHolder _instance = new AttendanceRewardHolder();

	private final IntObjectMap<AttendanceRewardData> _normalRewards = new TreeIntObjectMap<AttendanceRewardData>();
	private final IntObjectMap<AttendanceRewardData> _premiumRewards = new TreeIntObjectMap<AttendanceRewardData>();

	public static AttendanceRewardHolder getInstance()
	{
		return _instance;
	}

	public void addNormalReward(AttendanceRewardData reward)
	{
		_normalRewards.put(_normalRewards.size() + 1, reward);
	}

	public void addPremiumReward(AttendanceRewardData reward)
	{
		_premiumRewards.put(_premiumRewards.size() + 1, reward);
	}

	public Collection<AttendanceRewardData> getRewards(boolean premium)
	{
		return premium ? _premiumRewards.valueCollection() : _normalRewards.valueCollection();
	}

	public AttendanceRewardData getReward(int index, boolean premium)
	{
		return premium ? _premiumRewards.get(index) : _normalRewards.get(index);
	}

	@Override
	public void afterParsing()
	{
		super.afterParsing();
		newStartDate();
	}

	public void newStartDate()
	{
		String start_date = ServerVariables.getString("VipAttendanceStart", Config.VIP_ATTENDANCE_REWARDS_START_DATE.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
		if(start_date == null || !isValidDate(start_date))
		{
			LocalDateTime startDate = generateStartDate();
			start_date = startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

			Config.VIP_ATTENDANCE_REWARDS_START_DATE = startDate;
			ServerVariables.set("VipAttendanceStart", start_date);

			LocalDateTime endDate = startDate.plusMonths(1);
			endDate = endDate.withHour(6).withMinute(30).withSecond(0);
			Config.VIP_ATTENDANCE_REWARDS_END_DATE = endDate;
		}
		else
		{
			Config.VIP_ATTENDANCE_REWARDS_START_DATE = getDateFromString(start_date);

			LocalDateTime endDate = Config.VIP_ATTENDANCE_REWARDS_START_DATE.plusMonths(1);
			endDate = endDate.withHour(6).withMinute(30).withSecond(0);
			Config.VIP_ATTENDANCE_REWARDS_END_DATE = endDate;
		}
	}

	@Override
	public int size()
	{
		return _normalRewards.size() + _premiumRewards.size();
	}

	@Override
	public void clear()
	{
		_normalRewards.clear();
		_premiumRewards.clear();
	}

	private static LocalDateTime getDateFromString(String datetime)
	{
		return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
	}

	private static boolean isValidDate(String datetime)
	{
		LocalDateTime startDate = getDateFromString(datetime);

		LocalDateTime now = LocalDateTime.now();
		if(now.isBefore(startDate) || now.isAfter(startDate.plusMonths(1)))
			return false;

		return true;
	}

	private static LocalDateTime generateStartDate()
	{
		return LocalDateTime.now().withDayOfMonth(Config.VIP_ATTENDANCE_REWARDS_START_DATE.getDayOfMonth()).withHour(6).withMinute(30).withSecond(0);
	}

	public String getEndDate()
	{
		return Config.VIP_ATTENDANCE_REWARDS_END_DATE.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
	}
}
