package l2s.authserver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.authserver.dao.AuthBansDAO;
import l2s.authserver.network.gamecomm.GameServer;
import l2s.authserver.network.gamecomm.as2gs.CheckBans;
import l2s.commons.ban.BanBindType;
import l2s.commons.ban.BanInfo;
import l2s.commons.ban.BanManager;

/**
 * @author Bonux (Head Developer L2-scripts.com) 10.04.2019 Developed for
 *         L2-Scripts.com
 **/
public class AuthBanManager extends BanManager
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthBanManager.class);

	private final Lock lock = new ReentrantLock();
	private ScheduledFuture<?> checkBansTask = null;

	public void init()
	{
		AuthBansDAO.getInstance().cleanUp();
		startCheckBansTask();
		LOGGER.info("AuthBanManager: Initialized.");
	}

	private void startCheckBansTask()
	{
		if(checkBansTask != null)
			return;

		long interval = TimeUnit.MINUTES.toMillis(Config.CHECK_BANS_INTERVAL);
		checkBansTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> checkBans(), 0, interval);
	}

	private void checkBans()
	{
		lock.lock();
		try
		{
			for(BanBindType bindType : BanBindType.VALUES)
			{
				if(!bindType.isAuth())
					continue;

				Map<String, BanInfo> bans = new HashMap<>();
				AuthBansDAO.getInstance().select(bans, bindType);

				CheckBans checkBans = new CheckBans(bindType, bans);
				for(GameServer gameServer : GameServerManager.getInstance().getGameServers())
					gameServer.sendPacket(checkBans);

				getCachedBans().put(bindType, bans);
			}
		}
		finally
		{
			lock.unlock();
		}
	}

	public boolean giveBan(BanBindType bindType, String bindValue, int endTime, String reason)
	{
		if(!bindType.isAuth())
			return false;

		if(StringUtils.isEmpty(bindValue))
			return false;

		if(endTime != -1 && endTime < (System.currentTimeMillis() / 1000))
			return false;

		lock.lock();
		try
		{
			BanInfo banInfo = new BanInfo(endTime, reason);
			if(!AuthBansDAO.getInstance().insert(bindType, bindValue, banInfo))
				return false;

			getCachedBans().computeIfAbsent(bindType, (b) -> new HashMap<>()).put(bindValue, banInfo);
		}
		finally
		{
			lock.unlock();
		}
		return true;
	}

	public boolean removeBan(BanBindType bindType, String bindValue)
	{
		if(!bindType.isAuth())
			return false;

		if(StringUtils.isEmpty(bindValue))
			return false;

		lock.lock();
		try
		{
			Map<String, BanInfo> bans = getCachedBans().get(bindType);
			if(bans == null)
				return false;

			if(!AuthBansDAO.getInstance().delete(bindType, bindValue))
				return false;

			bans.remove(bindValue);
			return true;
		}
		finally
		{
			lock.unlock();
		}
	}

	private static final AuthBanManager INSTANCE = new AuthBanManager();

	public static AuthBanManager getInstance()
	{
		return INSTANCE;
	}
}
