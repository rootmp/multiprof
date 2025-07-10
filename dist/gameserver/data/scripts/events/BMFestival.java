package events;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2s.gameserver.listener.script.OnInitScriptListener;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.network.l2.s2c.events.ExFestivalBMTopItemInfo;
import l2s.gameserver.utils.Functions;

/**
 * @author nexvill
 */
public class BMFestival implements OnInitScriptListener
{
	private static final Logger _log = LoggerFactory.getLogger(BMFestival.class);

	private static final String EVENT_NAME = "BMFestival";
	private static EventListeners EVENT_LISTENERS = new EventListeners();

	private static boolean _active = false;
	private static long _timeStart, _timeEnd;

	private static boolean isActive()
	{
		LocalDateTime localDateTime = LocalDateTime.parse(Config.BM_FESTIVAL_TIME_START, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
		_timeStart = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		localDateTime = LocalDateTime.parse(Config.BM_FESTIVAL_TIME_END, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
		_timeEnd = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		long currentTime = System.currentTimeMillis();
		if ((currentTime > _timeStart) && (currentTime < _timeEnd))
		{
			Functions.SetActive(EVENT_NAME, true);
		}
		else
		{
			Functions.SetActive(EVENT_NAME, false);
		}
		return Functions.IsActive(EVENT_NAME);
	}

	@Override
	public void onInit()
	{
		if (isActive())
		{
			_active = true;
			CharListenerList.addGlobal(EVENT_LISTENERS);
			ThreadPoolManager.getInstance().schedule(new UpdateEvent(), _timeEnd);
			_log.info("Loaded Event: BM Festival [state: activated]");
		}
		else
		{
			try (Connection con = DatabaseFactory.getInstance().getConnection();)
			{
				PreparedStatement statement = con.prepareStatement("DELETE FROM character_variables WHERE name=?");
				statement.setString(1, "%FESTIVAL_BM_%");
				statement.executeUpdate();
				statement.close();
				statement = con.prepareStatement("DELETE FROM server_variables WHERE name=?");
				statement.setString(1, "%FESTIVAL_BM_%");
				statement.executeUpdate();

			}
			catch (Exception e)
			{
				_log.error("Could not remove BM Festival data from db!", e);
			}

			ThreadPoolManager.getInstance().schedule(new UpdateEvent(), _timeStart);

			_log.info("Loaded Event: BM Festival [state: deactivated]");
		}
	}

	private static class EventListeners implements OnPlayerEnterListener
	{

		@Override
		public void onPlayerEnter(Player player)
		{
			if (_active)
			{
				long time = (_timeEnd - System.currentTimeMillis()) / 1000;
				player.sendPacket(new ExFestivalBMTopItemInfo(true, (int) time));
			}
			else
			{
				player.sendPacket(new ExFestivalBMTopItemInfo(false, 0));
			}
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