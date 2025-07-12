package l2s.gameserver.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.dao.HuntPassDAO;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.huntpass.HuntPassSimpleInfo;

public class HuntPass
{
	private final Player _user;
	private int _availableSayhaTime;
	private int _points;
	private boolean _isPremium = false;
	private boolean _rewardAlert = false;

	private int _rewardStep;
	private int _currentStep;
	private int _premiumRewardStep;

	private boolean _toggleSayha = false;
	private ScheduledFuture<?> _sayhasSustentionTask = null;
	private int _toggleStartTime = 0;
	private int _usedSayhaTime;

	private static int _dayEnd = 0;

	public void load()
	{
		if(Config.ENABLE_HUNT_PASS)
		{
			HuntPassDAO.getInstance().restore(this);
			huntPassDayEnd();
			store();
		}
	}

	public HuntPass(Player user)
	{
		_user = user;
	}

	public String getAccountName()
	{
		return _user.getAccountName();
	}

	public void store()
	{
		HuntPassDAO.getInstance().store(this);
	}

	public void deleteHuntPass()
	{
		setCurrentStep(0);
		setRewardStep(0);
		setPremiumRewardStep(0);
		setAvailableSayhaTime(0);
		setUsedSayhaTime(0);
		setRewardAlert(false);
		store();
	}

	public int getHuntPassDayEnd()
	{
		return _dayEnd;
	}

	public void huntPassDayEnd()
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.DAY_OF_MONTH, Config.HUNT_PASS_PERIOD);
		calendar.set(Calendar.HOUR_OF_DAY, 6);
		calendar.set(Calendar.MINUTE, 30);
		if(calendar.getTimeInMillis() < System.currentTimeMillis())
		{
			calendar.add(Calendar.MONTH, 1);
		}

		_dayEnd = (int) (calendar.getTimeInMillis() / 1000);
	}

	public boolean toggleSayha()
	{
		return _toggleSayha;
	}

	public int getPoints()
	{
		return _points;
	}

	public void addPassPoint(MonsterInstance mob)
	{
		if(Config.ENABLE_HUNT_PASS)
		{
      DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
      boolean isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
      
			int gm_mod = isWeekend ? 2:1;
			int seasonPasspoint = Config.HUNT_PASS_POINTS_FOR_MOB * gm_mod;

			if(!mob.getReflection().isMain() && mob.getReflection().getInstancedZone() != null && (mob.getReflection().getInstancedZone().isStatic() || mob.getReflection().getInstancedZoneId() == 228))
				seasonPasspoint = Config.HUNT_PASS_POINTS_FOR_MOB_INSTANCE * gm_mod;

			int calculate = seasonPasspoint + getPoints();
			// Процесс пошагового начисления очков
			while(calculate >= Config.HUNT_PASS_POINTS_FOR_STEP)
			{
				calculate -= Config.HUNT_PASS_POINTS_FOR_STEP;
				setCurrentStep(getCurrentStep() + 1);
				setRewardAlert(true);
				_user.sendPacket(new HuntPassSimpleInfo(_user));
			}

			setPoints(calculate);
			store();
		}
	}

	public void setPoints(int points)
	{
		_points = points;
	}

	public int getCurrentStep()
	{
		return _currentStep;
	}

	public void setCurrentStep(int step)
	{
		_currentStep = step;
	}

	public int getRewardStep()
	{
		return _rewardStep;
	}

	public void setRewardStep(int step)
	{
		_rewardStep = step;
	}

	public boolean isPremium()
	{
		return _isPremium;
	}

	public void setPremium(boolean premium)
	{
		_isPremium = premium;
	}

	public int getPremiumRewardStep()
	{
		return _premiumRewardStep;
	}

	public void setPremiumRewardStep(int step)
	{
		_premiumRewardStep = step;
	}

	public boolean rewardAlert()
	{
		return _rewardAlert;
	}

	public void setRewardAlert(boolean enable)
	{
		_rewardAlert = enable;
	}

	public int getAvailableSayhaTime()
	{
		return _availableSayhaTime;
	}

	public void setAvailableSayhaTime(int time)
	{
		_availableSayhaTime = time;
	}

	public void addSayhaTime(int time)
	{
		// microsec to sec to database. 1 hour 3600 sec
		_availableSayhaTime += time * 60;
	}

	public int getUsedSayhaTime()
	{
		return _usedSayhaTime;
	}

	private void onSayhaEndTime()
	{
		setSayhasSustention(false);
	}

	public void setUsedSayhaTime(int time)
	{
		_usedSayhaTime = time;
	}

	public void addSayhasSustentionTimeUsed(int time)
	{
		_usedSayhaTime += time;
	}

	public int getToggleStartTime()
	{
		return _toggleStartTime;
	}

	public void setSayhasSustention(boolean active)
	{
		_toggleSayha = active;
		if(active)
		{
			_toggleStartTime = (int) (System.currentTimeMillis() / 1000);
			if(_sayhasSustentionTask != null)
			{
				_sayhasSustentionTask.cancel(true);
				_sayhasSustentionTask = null;
			}
			_user.sendPacket(new SystemMessage(SystemMsg.SAYHA_S_GRACE_SUSTENTION_EFFECT_OF_THE_SEASON_PASS_IS_ACTIVATED_AVAILABLE_SAYHA_S_GRACE_SUSTENTION_TIME_IS_RUNNING));
			_sayhasSustentionTask = ThreadPoolManager.getInstance().schedule(this::onSayhaEndTime, Math.max(0, getAvailableSayhaTime() - getUsedSayhaTime())
					* 1000L);
		}
		else
		{
			if(_sayhasSustentionTask != null)
			{
				addSayhasSustentionTimeUsed((int) ((System.currentTimeMillis() / 1000) - _toggleStartTime));
				_toggleStartTime = 0;
				_sayhasSustentionTask.cancel(true);
				_sayhasSustentionTask = null;
				_user.sendPacket(new SystemMessage(SystemMsg.SAYHA_S_GRACE_SUSTENTION_EFFECT_OF_THE_SEASON_PASS_HAS_BEEN_DEACTIVATED_THE_SUSTENTION_TIME_YOU_HAVE_DOES_NOT_DECREASE));
			}
		}
	}
}