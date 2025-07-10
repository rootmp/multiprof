package events;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.database.MySqlDataInsert;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.listener.actor.OnAbnormalStartEndListener;
import l2s.gameserver.listener.actor.OnDeathListener;
import l2s.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2s.gameserver.listener.script.OnInitScriptListener;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.network.l2.s2c.events.ExBalthusEvent;
import l2s.gameserver.network.l2.s2c.events.ExBalthusEventJackpotUser;
import l2s.gameserver.utils.Functions;

/**
 * @author nexvill
 */
public class BalthusEvent implements OnInitScriptListener
{
	private static final Logger _log = LoggerFactory.getLogger(BalthusEvent.class);

	private static final String EVENT_NAME = "BalthusEvent";
	private static final int BUFF_ID = Config.BALTHUS_EVENT_PARTICIPATE_BUFF_ID;
	private EventListeners EVENT_LISTENERS = new EventListeners();
	private final OnAbnormalStartEndListener _abnormalStartEndListener = new AbnormalStartEndListener();

	// Misc
	private int _round = 1;
	private int _stage = 20;
	private int _time = 0;
	private int _jackpotId = 49800;
	private int _hour;
	private boolean _receivedThisHour = Config.BALTHUS_EVENT_ENABLE;
	private static final int BASIC_REWARD_COUNT = Config.BALTHUS_EVENT_BASIC_REWARD_COUNT;
	private static final int BASIC_REWARD_ID = Config.BALTHUS_EVENT_BASIC_REWARD_ID;

	private static boolean _active = true;
	private static long _timeStart, _timeEnd;

	@Override
	public void onInit()
	{
		if (isActive())
		{
			_active = true;
			CharListenerList.addGlobal(EVENT_LISTENERS);
			CharListenerList.addGlobal(_abnormalStartEndListener);
			_hour = ZonedDateTime.now().getHour();
			_round = _hour + 1;
			_time = ZonedDateTime.now().getMinute() * 60;
			ThreadPoolManager.getInstance().schedule(new UpdateEvent(), _timeEnd);
			ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> updateTimer(), 1000L, 1000L);
			_log.info("Loaded Event: Balthus Event [state: activated]");
		}
		else
		{
			ThreadPoolManager.getInstance().schedule(new UpdateEvent(), _timeStart);
			_log.info("Loaded Event: Balthus Event [state: deactivated]");
		}
	}

	private static boolean isActive()
	{
		LocalDateTime localDateTime = LocalDateTime.parse(Config.BALTHUS_EVENT_TIME_START, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
		_timeStart = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		localDateTime = LocalDateTime.parse(Config.BALTHUS_EVENT_TIME_END, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
		_timeEnd = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		long currentTime = System.currentTimeMillis();
		if ((currentTime > _timeStart) && (currentTime < _timeEnd))
		{
			Functions.SetActive(EVENT_NAME, true);
		}
		else
		{
			Functions.SetActive(EVENT_NAME, false);
			MySqlDataInsert.set("DELETE FROM `character_variables` WHERE `value` = \"balthus_received_amount\";");
		}
		return Functions.IsActive(EVENT_NAME);
	}

	private void updateTimer()
	{
		_time++;
		if (_time >= 3600)
		{
			_time = 0;
			_stage = 20;
			_round++;
			_receivedThisHour = false;
			if (_round > 24)
			{
				_round = 1;
			}
			manageJackpot();
			updateEvent();
			saveVars();
		}
		else if (((_time == 3500) || (_time == 2880) || (_time == 2160) || (_time == 1440) || (_time == 720)) && !_receivedThisHour)
		{
			if (_time != 3500)
			{
				_stage += 20;
			}
			eventRun();
			saveVars();
		}
	}

	private void saveVars()
	{
		ServerVariables.set("balthus_round", _round);
		ServerVariables.set("balthus_stage", _stage);
		ServerVariables.set("balthus_jackpot", _jackpotId);
		ServerVariables.set("balthus_received_hour", _receivedThisHour);
	}

	private void eventRun()
	{
		int participantsCount = 0;
		if (!_receivedThisHour && (Rnd.get(100) < Config.BALTHUS_EVENT_JACKPOT_CHANCE))
		{
			_receivedThisHour = true;
			for (Player player : GameObjectsStorage.getPlayers(false, false))
			{
				if (player.getAbnormalList().contains(BUFF_ID))
				{
					participantsCount++;
					player.setVar(PlayerVariables.BALTHUS_RECEIVED_AMOUNT, player.getVarInt(PlayerVariables.BALTHUS_RECEIVED_AMOUNT, 0) + BASIC_REWARD_COUNT);
				}
				player.sendPacket(new ExBalthusEventJackpotUser());
				player.sendPacket(new SystemMessagePacket(SystemMsg.THE_SECRET_SUPPLIES_OF_THE_BALTHUS_KNIGHTS_ARRIVED_SOMEONE_RECEIVED_S1).addItemName(_jackpotId));
			}
			if (!(participantsCount < 1))
			{
				int number = Rnd.get(participantsCount);
				int i = 0;
				for (Player player : GameObjectsStorage.getPlayers(false, false))
				{
					if (player.getAbnormalList().contains(BUFF_ID))
					{
						if (number == i)
						{
							player.getInventory().addItem(_jackpotId, 1);
							break;
						}
						i++;
					}
				}
			}
		}
		updateEvent();
	}

	private void updateEvent()
	{
		for (Player player : GameObjectsStorage.getPlayers(false, false))
		{
			boolean participate = false;
			if (player.getAbnormalList().contains(BUFF_ID))
			{
				participate = true;
			}
			int receivedAmount = player.getVarInt(PlayerVariables.BALTHUS_RECEIVED_AMOUNT, 0);
			player.sendPacket(new ExBalthusEvent(_round, _stage, _jackpotId, BASIC_REWARD_COUNT, receivedAmount, participate, _receivedThisHour, _time));
		}
	}

	private void manageJackpot()
	{
		if (_hour < 1)
			_jackpotId = 95876; // Balthus Knights' High-grade Spellbook Chest
		else if (_hour < 17)
			_jackpotId = 49800; // Sibi's Coin Pack
		else if (_hour < 18)
			_jackpotId = 94875; // Balthus Knights' Enchanted Talisman Pack
		else if (_hour < 19)
			_jackpotId = 95872; // Balthus Knights' Master Book Chest
		else if (_hour < 20)
			_jackpotId = 94873; // Balthus Knights' Blessed Dragon Belt
		else if (_hour < 21)
			_jackpotId = 95873; // Balthus Knights' Enchanted Armor Pack
		else if (_hour < 22)
			_jackpotId = 95874; // Balthus Knights' Package: Hardin's Soul Crystal
		else if (_hour < 23)
			_jackpotId = 94877; // Balthus Knight's Package: Enhanced Einhasad's Pendant
		else if (_hour == 23)
			_jackpotId = 95875; // Balthus Knight's Enchanted Weapon Pack
	}

	private class EventListeners implements OnPlayerEnterListener
	{
		@Override
		public void onPlayerEnter(Player player)
		{
			if (_active)
			{
				if (player.getAbnormalList().contains(BUFF_ID))
				{
					int receivedAmount = player.getVarInt(PlayerVariables.BALTHUS_RECEIVED_AMOUNT, 0);
					player.sendPacket(new ExBalthusEvent(_round, _stage, _jackpotId, BASIC_REWARD_COUNT, receivedAmount, true, false, _time));
				}
				else
				{
					int receivedAmount = player.getVarInt(PlayerVariables.BALTHUS_RECEIVED_AMOUNT, 0);
					player.sendPacket(new ExBalthusEvent(_round, _stage, _jackpotId, BASIC_REWARD_COUNT, receivedAmount, false, false, _time));
				}
			}
		}
	}

	private class AbnormalStartEndListener implements OnAbnormalStartEndListener
	{
		@Override
		public void onAbnormalStart(Creature actor, Abnormal a)
		{
			if (actor.isPlayer() && (a.getId() == BUFF_ID) && _active)
			{
				int receivedAmount = actor.getPlayer().getVarInt(PlayerVariables.BALTHUS_RECEIVED_AMOUNT, 0);
				actor.sendPacket(new ExBalthusEvent(_round, _stage, _jackpotId, BASIC_REWARD_COUNT, receivedAmount, true, false, _time));
			}
		}

		@Override
		public void onAbnormalEnd(Creature actor, Abnormal a)
		{
			if (actor.isPlayer() && (a.getId() == BUFF_ID) && _active)
			{
				int receivedAmount = actor.getPlayer().getVarInt(PlayerVariables.BALTHUS_RECEIVED_AMOUNT, 0);
				actor.sendPacket(new ExBalthusEvent(_round, _stage, _jackpotId, BASIC_REWARD_COUNT, receivedAmount, false, false, _time));
			}
		}
	}

	public class MonsterDeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature actor, Creature killer)
		{
			if (!_active)
				return;

			if (killer == null || !actor.isMonster())
				return;

			Player player = killer.getPlayer();
			if (player == null)
				return;

			if ((killer.getLevel() - actor.getLevel()) > 15)
				return;

			if (Rnd.get(100) < 2)
				player.getInventory().addItem(BASIC_REWARD_ID, 1);
		}

	}

	private class UpdateEvent implements Runnable
	{
		@Override
		public void run()
		{
			long currentTime = System.currentTimeMillis();
			if ((currentTime > _timeStart) && (currentTime < _timeEnd))
			{
				Functions.SetActive(EVENT_NAME, true);
			}
			else
			{
				Functions.SetActive(EVENT_NAME, false);
			}
		}

	}
}
