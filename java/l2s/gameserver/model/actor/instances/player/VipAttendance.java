package l2s.gameserver.model.actor.instances.player;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.dao.VipAttendanceDAO;
import l2s.gameserver.data.xml.holder.AttendanceRewardHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExVipAttendanceCheck;
import l2s.gameserver.network.l2.s2c.ExVipAttendanceItemList;
import l2s.gameserver.network.l2.s2c.ExVipAttendanceNotify;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.StatsSet;
import l2s.gameserver.templates.item.data.AttendanceRewardData;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.TimeUtils;

public class VipAttendance
{
	private static final int AFTER_LOGIN_RECEIVE_REWARD_DELAY = 30; // In minutes.

	private final Player _owner;
	private StatsSet vipAttendanceData = new StatsSet();

	private ScheduledFuture<?> _loginDelayTask = null;
	private ScheduledFuture<?> _getNextRewardIndexTask = null;

	public VipAttendance(Player owner)
	{
		_owner = owner;
	}
	
	public void restore()
	{
		if(!Config.VIP_ATTENDANCE_REWARDS_ENABLED)
			return;
		vipAttendanceData = VipAttendanceDAO.getInstance().select(_owner.getAccountName());
		vipAttendanceData.put("account_name", _owner.getAccountName());
	}

	//количество "доступных" дней с момента старта ивента 
	public int daysPassedSinceStartDate() 
	{
		if(Config.VIP_ATTENDANCE_REWARDS_START_DATE == null)
			return 0;
		LocalDateTime currentDateTime = LocalDateTime.now();
		return (int) ChronoUnit.DAYS.between(Config.VIP_ATTENDANCE_REWARDS_START_DATE, currentDateTime)+1;
	}

	public boolean isAvailable()
	{
		long nextRewardTime = TimeUtils.DAILY_DATE_PATTERN.next(vipAttendanceData.getInteger("dateLastReward",0) * 1000L);
		if(nextRewardTime <= System.currentTimeMillis())
			return true;
		return false;
	}

	public boolean isReceived()
	{
		return getRewardDay() == daysPassedSinceStartDate();
	}
	
	public void receiveReward(int bResult)
	{
		List<AttendanceRewardData> _reward = new ArrayList<AttendanceRewardData>();
		if(getAttendanceDay() - getRewardDay() <= 0)
			return;
		
		int _bResult = Math.max(getAttendanceDay(), bResult);
		for(int i = getRewardDay()+1; i <= _bResult;i++)
		{
			AttendanceRewardData reward = AttendanceRewardHolder.getInstance().getReward(i, _owner.hasPremiumAccount());
			if(reward == null)
			{
				_owner.sendPacket(SystemMsg.DUE_TO_A_SYSTEM_ERROR_THE_ATTENDANCE_REWARD_CANNOT_BE_RECEIVED_PLEASE_TRY_AGAIN_LATER_BY_GOING_TO_MENU__ATTENDANCE_CHECK);
				return;
			}
			
			if(Config.VIP_ATTENDANCE_REWARDS_REWARD_ONLY_PREMIUM && !_owner.hasPremiumAccount())
				return;

			if(_owner.isInventoryFull())
			{
				_owner.sendPacket(SystemMsg.THE_ATTENDANCE_REWARD_CANNOT_BE_RECEIVED_BECAUSE_THE_INVENTORY_WEIGHTQUANTITY_LIMIT_HAS_BEEN_EXCEEDED);
				return;
			}
			
			if(!_owner.hasPremiumAccount())
				_owner.sendPacket(new SystemMessagePacket(SystemMsg.YOUVE_RECEIVED_YOUR_ATTENDANCE_REWARD_FOR_DAY_S1_).addInteger(i));
			else
				_owner.sendPacket(new SystemMessagePacket(SystemMsg.YOUVE_RECEIVED_YOUR_PC_CAF_ATTENDANCE_REWARD_FOR_DAY_S1_).addInteger(i));
			_reward.add(reward);
			
		}
		vipAttendanceData.set("cRewardDay", _bResult);
		_reward.forEach( r->ItemFunctions.addItem(_owner, r.getId(), r.getCount(), true));
		 VipAttendanceDAO.getInstance().insert(vipAttendanceData);
		 startTasks();
	}

	public void startTasks()
	{
		stopTasks();
		if(isAvailable() && daysPassedSinceStartDate() > getAttendanceDay())
		{
			_loginDelayTask = ThreadPoolManager.getInstance().schedule(() -> 
			{
				if(Config.VIP_ATTENDANCE_REWARDS_REWARD_ONLY_PREMIUM)
				{
					if(_owner.hasPremiumAccount())
						_owner.sendPacket(SystemMsg.YOU_CAN_REDEEM_YOUR_REWARD_NOW);
				}
				else
					_owner.sendPacket(SystemMsg.YOU_CAN_REDEEM_YOUR_REWARD_NOW);

				vipAttendanceData.incInt("cAttendanceDay",0, 1);
				vipAttendanceData.set("dateLastReward", (int) (System.currentTimeMillis() / 1000));

				update(vipAttendanceData);

				_owner.sendPacket(new ExVipAttendanceCheck(1));
				_owner.sendPacket(new ExVipAttendanceNotify());
				startTasks();
			}, AFTER_LOGIN_RECEIVE_REWARD_DELAY * 60 * 1000);
		}
		else
		{
			long nextRewardDelay = TimeUtils.DAILY_DATE_PATTERN.next(System.currentTimeMillis()) - System.currentTimeMillis();
			_getNextRewardIndexTask = ThreadPoolManager.getInstance().schedule(() -> {
				VipAttendanceDAO.getInstance().insert(vipAttendanceData);
				startTasks();
			}, nextRewardDelay+1000);
		}
	}

	public void stopTasks()
	{
		update(vipAttendanceData);
		if(_loginDelayTask != null)
		{
			_loginDelayTask.cancel(false);
			_loginDelayTask = null;
		}
		if(_getNextRewardIndexTask != null)
		{
			_getNextRewardIndexTask.cancel(false);
			_getNextRewardIndexTask = null;
		}
	}

	public void onReceivePremiumAccount()
	{
		//restore();
		startTasks();
	}

	public void onRemovePremiumAccount()
	{
		//restore();
		startTasks();
	}

	public int getLoginDelayTimeRemainingInSeconds()
	{
		if(_loginDelayTask != null && !_loginDelayTask.isDone())
			return (int) TimeUnit.SECONDS.convert(_loginDelayTask.getDelay(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
		return 0;
	}

	/**
	 * активная награда
	 * @return
	 */
	public int getAttendanceDay() 
	{
		return vipAttendanceData.getInteger("cAttendanceDay", 0);
	}
	
	public void IncAttendanceDay() 
	{
		if(getAttendanceDay()<28)
			vipAttendanceData.incInt("cAttendanceDay",0, 1);
		update(vipAttendanceData);
	}

	private void update(StatsSet data)
	{
		VipAttendanceDAO.getInstance().insert(data);
	}

	/**
	 * завершено, забрал награду
	 * @return
	 */
	public int getRewardDay() 
	{
		return vipAttendanceData.getInteger("cRewardDay", 0);
	}

	public int getFollowBaseDay() 
	{
		return vipAttendanceData.getInteger("cFollowBaseDay", 0);
	}

	
	public void onEnterWorld()
	{
		_owner.sendPacket(new ExVipAttendanceItemList(_owner));
	}

	public boolean getAvailableRewards()
	{
		return getAttendanceDay() - getRewardDay() > 0;
	}

	public void reset()
	{
		vipAttendanceData.clear();
		vipAttendanceData.put("account_name", _owner.getAccountName());
		startTasks();
	}

}
