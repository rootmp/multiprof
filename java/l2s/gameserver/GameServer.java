package l2s.gameserver;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import l2s.commons.lang.StatsUtils;
import l2s.commons.listener.Listener;
import l2s.commons.listener.ListenerList;
import l2s.commons.net.HostInfo;
import l2s.commons.net.nio.impl.SelectorStats;
import l2s.commons.net.nio.impl.SelectorThread;
import l2s.gameserver.cache.CrestCache;
import l2s.gameserver.cache.ImagesCache;
import l2s.gameserver.config.FloodProtectorConfigs;
import l2s.gameserver.config.xml.ConfigParsers;
import l2s.gameserver.config.xml.holder.HostsConfigHolder;
import l2s.gameserver.dao.CharacterDAO;
import l2s.gameserver.dao.CustomHeroDAO;
import l2s.gameserver.dao.FencesDAO;
import l2s.gameserver.dao.HidenItemsDAO;
import l2s.gameserver.dao.ItemsDAO;
import l2s.gameserver.data.BoatHolder;
import l2s.gameserver.data.xml.Parsers;
import l2s.gameserver.data.xml.holder.EventHolder;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.data.xml.holder.StaticObjectHolder;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.handler.admincommands.AdminCommandHandler;
import l2s.gameserver.handler.bbs.BbsHandlerHolder;
import l2s.gameserver.handler.bypass.BypassHolder;
import l2s.gameserver.handler.dailymissions.DailyMissionHandlerHolder;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.handler.onshiftaction.OnShiftActionHolder;
import l2s.gameserver.handler.usercommands.UserCommandHandler;
import l2s.gameserver.handler.voicecommands.VoicedCommandHandler;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.instancemanager.BotCheckManager;
import l2s.gameserver.instancemanager.BotReportManager;
import l2s.gameserver.instancemanager.CoupleManager;
import l2s.gameserver.instancemanager.GameBanManager;
import l2s.gameserver.instancemanager.PetitionManager;
import l2s.gameserver.instancemanager.PlayerMessageStack;
import l2s.gameserver.instancemanager.PrivateStoreHistoryManager;
import l2s.gameserver.instancemanager.RaidBossSpawnManager;
import l2s.gameserver.instancemanager.RankManager;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.instancemanager.SpawnManager;
import l2s.gameserver.instancemanager.TrainingCampManager;
import l2s.gameserver.instancemanager.clansearch.ClanSearchManager;
import l2s.gameserver.instancemanager.games.MiniGameScoreManager;
import l2s.gameserver.listener.GameListener;
import l2s.gameserver.listener.game.OnShutdownListener;
import l2s.gameserver.listener.game.OnStartListener;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.model.entity.events.fightclubmanager.FightClubEventManager;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.network.authcomm.AuthServerCommunication;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.GamePacketHandler;
import l2s.gameserver.scripts.Scripts;
import l2s.gameserver.security.HWIDBan;
import l2s.gameserver.tables.ClanTable;
import l2s.gameserver.tables.EnchantHPBonusTable;
import l2s.gameserver.tables.FakePlayersTable;
import l2s.gameserver.tables.SubClassTable;
import l2s.gameserver.taskmanager.AutomaticTasks;
import l2s.gameserver.taskmanager.ItemsAutoDestroy;
import l2s.gameserver.utils.TradeHelper;
import net.sf.ehcache.CacheManager;

public class GameServer
{
	private static final Logger _log = LoggerFactory.getLogger(GameServer.class);

	public static GameServer _instance;
	private final List<SelectorThread<GameClient>> _selectorThreads = new ArrayList<SelectorThread<GameClient>>();
	private final SelectorStats _selectorStats = new SelectorStats();
	private final GameServerListenerList _listeners;
	private long _serverStartTimeMillis;
	private String _licenseHost;
	private int _onlineLimit;
	public static boolean DEVELOP = false;

	public static final int AUTH_SERVER_PROTOCOL = 4;

	public GameServer() throws Exception
	{
		_instance = this;
		_serverStartTimeMillis = System.currentTimeMillis();
		_listeners = new GameServerListenerList();
		_licenseHost = Config.EXTERNAL_HOSTNAME;
		_onlineLimit = Config.MAXIMUM_ONLINE_USERS;

		new File("./log/").mkdir();

		// Initialize config
		ConfigParsers.parseAllOnLoad();

		Config.load();

		final HostInfo[] hosts = HostsConfigHolder.getInstance().getGameServerHosts();
		if (hosts.length == 0)
		{
			throw new Exception("Server hosts list is empty!");
		}

		final TIntSet ports = new TIntHashSet();
		for (HostInfo host : hosts)
		{
			if (host.getAddress() != null)
			{
				ports.add(host.getPort());
			}
		}

		if (ports.isEmpty())
		{
			throw new Exception("Server ports list is empty!");
		}

		checkFreePorts(ports);

		final int[] portsArray = ports.toArray();

		String licenseHost = "";
		int onlineLimit = 0;

		if (!DEVELOP)
		{
			boolean licensed = false;
			if (Player.CLIENTS_HASHCODE == Player.CLIENTS.hashCode())
			{
				for (Entry<String, Integer> license : Player.CLIENTS.entrySet())
				{
					if (!checkOpenPort(license.getKey(), portsArray[0]))
					{
						ServerSocket ss = null;
						try
						{
							ss = new ServerSocket(portsArray[0]);
						}
						catch (Exception e)
						{
							//
						}
						finally
						{
							if (checkOpenPort(license.getKey(), portsArray[0]))
							{
								licensed = true;
							}
							try
							{
								ss.close();
							}
							catch (Exception e)
							{
								//
							}
						}
					}

					if (licensed)
					{
						licenseHost = license.getKey();
						onlineLimit = license.getValue() == -1 ? Config.MAXIMUM_ONLINE_USERS : Math.min(Config.MAXIMUM_ONLINE_USERS, license.getValue());
						printSection("L2Studio Manager");
						_log.info("Project Base: ............ " + "L2Scripts [SVN 41784]");
						_log.info("Project Revision: ........ " + "L2Studio [GIT 113]");
						_log.info("Licensed Type: ........... " + (license.getValue() == -1 ? "UNLIMITED" : license.getValue()));
						_log.info("Update: .................. " + "Lineage 2 Essence - Vanguard (362)");
						break;
					}
				}
			}

			if (!licensed)
			{
				throw new Exception("You not licensed for run server!");
			}
		}
		else
		{
			licenseHost = "127.0.0.1";
			onlineLimit = 10;
		}

		_licenseHost = licenseHost; // Config.EXTERNAL_HOSTNAME;
		_onlineLimit = onlineLimit; // Config.MAXIMUM_ONLINE_USERS;

		if (_onlineLimit == 0)
		{
			throw new Exception("Server online limit is zero!");
		}

		FloodProtectorConfigs.load();

		// Initialize database
		Class.forName(Config.DATABASE_DRIVER).getDeclaredConstructor().newInstance();
		DatabaseFactory.getInstance().getConnection().close();

		// UpdatesInstaller.checkAndInstall();

		printSection("Id Factory");
		IdFactory _idFactory = IdFactory.getInstance();
		if (!_idFactory.isInitialized())
		{
			_log.error("Could not read object IDs from DB. Please Check Your Data.");
			throw new Exception("Could not initialize the ID factory");
		}

		CacheManager.getInstance();

		ThreadPoolManager.getInstance();

		BotCheckManager.loadBotQuestions();

		HidenItemsDAO.LoadAllHiddenItems();

		CustomHeroDAO.getInstance();

		HWIDBan.getInstance().load();

		ItemHandler.getInstance();

		DailyMissionHandlerHolder.getInstance();

		printSection("Server Scripts");
		Scripts.getInstance();

		printSection("Geo Engine");
		GeoEngine.load();

		printSection("GameTime Controller");
		GameTimeController.getInstance();
		World.init();

		Parsers.parseAll();

		ItemsDAO.getInstance();

		ThreadPoolManager.getInstance().execute(() ->
		{
			CrestCache.getInstance();
			ImagesCache.getInstance();
		});

		CharacterDAO.getInstance();

		ClanTable.getInstance();

		SubClassTable.getInstance();

		EnchantHPBonusTable.getInstance();

		FencesDAO.getInstance().restore();

		StaticObjectHolder.getInstance().spawnAll();

		printSection("Spawn System");
		SpawnManager.getInstance().spawnAll();
		RaidBossSpawnManager.getInstance();

		ConfigParsers.parseAllOnInit();

		Scripts.getInstance().init();

		Announcements.getInstance();

		PlayerMessageStack.getInstance();

		if (Config.AUTODESTROY_ITEM_AFTER > 0)
		{
			ItemsAutoDestroy.getInstance();
		}

		// MonsterRace.getInstance();

		if (Config.ENABLE_OLYMPIAD)
		{
			printSection("Olympiads");
			Olympiad.load();
			Hero.getInstance();
		}

		PetitionManager.getInstance();

		if (Config.ALLOW_WEDDING)
		{
			CoupleManager.getInstance();
		}

		printSection("Handlers");
		AdminCommandHandler.getInstance().log();
		UserCommandHandler.getInstance().log();
		VoicedCommandHandler.getInstance().log();
		BbsHandlerHolder.getInstance().log();
		BypassHolder.getInstance().log();
		OnShiftActionHolder.getInstance().log();

		printSection("Tasks");
		AutomaticTasks.init();

		ClanTable.getInstance().checkClans();

		printSection("Events");
		ResidenceHolder.getInstance().callInit();
		EventHolder.getInstance().callInit();

		BoatHolder.getInstance().spawnAll();

		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

		_log.info("IdFactory: Free ObjectID's remaining: " + IdFactory.getInstance().size());

		MiniGameScoreManager.getInstance();

		ClanSearchManager.getInstance().load();

		BotReportManager.getInstance();

		TrainingCampManager.getInstance().init();

		RankManager.getInstance();
		PrivateStoreHistoryManager.getInstance().init();

		ServerVariables.unset("buffNpcActive");
		ServerVariables.unset("buffNpcX");
		ServerVariables.unset("buffNpcY");
		ServerVariables.unset("buffNpcZ");
		ServerVariables.unset("last_tp_id");
		ServerVariables.unset("frost_lord_castle_first_rb");

		Shutdown.getInstance().schedule(Config.RESTART_AT_TIME, Shutdown.RESTART);

		if (Config.FIGHT_CLUB_ENABLED)
		{
			FightClubEventManager.getInstance();
		}
		else
		{
			_log.info("FCE Events are disabled.");
		}

		_log.info("GameServer Started");
		_log.info("Maximum Numbers of Connected Players: " + getOnlineLimit());

		GameBanManager.getInstance().init();

		registerSelectorThreads(ports);

		getListeners().onStart();

		if (Config.BUFF_STORE_ENABLED)
		{
			_log.info("Restoring offline buffers...");
			int count = TradeHelper.restoreOfflineBuffers();
			_log.info("Restored " + count + " offline buffers.");
		}

		if (Config.SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART)
		{
			_log.info("Restoring offline traders...");
			int count = TradeHelper.restoreOfflineTraders();
			_log.info("Restored " + count + " offline traders.");
		}

		AuthServerCommunication.getInstance().start();
		System.gc();
		printSection("Loading Info");
		String memUsage = new StringBuilder().append(StatsUtils.getMemUsage()).toString();
		for (String line : memUsage.split("\n"))
		{
			_log.info(line);
		}

		FakePlayersTable.getInstance();
	}

	public GameServerListenerList getListeners()
	{
		return _listeners;
	}

	public <T extends GameListener> boolean addListener(T listener)
	{
		return _listeners.add(listener);
	}

	public <T extends GameListener> boolean removeListener(T listener)
	{
		return _listeners.remove(listener);
	}

	public static GameServer getInstance()
	{
		return _instance;
	}

	private void checkFreePorts(TIntSet ports)
	{
		for (int port : ports.toArray())
		{
			while (!checkFreePort(null, port))
			{
				_log.warn("Port '" + port + "' is allready binded. Please free it and restart server.");
				try
				{
					Thread.sleep(1000L);
				}
				catch (InterruptedException e2)
				{
					//
				}
			}
		}
	}

	private static boolean checkFreePort(String hostname, int port)
	{
		ServerSocket ss = null;
		try
		{
			if (StringUtils.isEmpty(hostname) || hostname.equalsIgnoreCase("*") || hostname.equalsIgnoreCase("0.0.0.0"))
			{
				ss = new ServerSocket(port);
			}
			else
			{
				ss = new ServerSocket(port, 50, InetAddress.getByName(hostname));
			}
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			try
			{
				ss.close();
			}
			catch (Exception e)
			{
				//
			}
		}
		return true;
	}

	private static boolean checkOpenPort(String ip, int port)
	{
		Socket socket = null;
		try
		{
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), 100);
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			try
			{
				socket.close();
			}
			catch (Exception e)
			{
				//
			}
		}
		return true;
	}

	private void registerSelectorThreads(TIntSet ports)
	{
		final GamePacketHandler gph = new GamePacketHandler();

		for (int port : ports.toArray())
		{
			registerSelectorThread(gph, null, port);
		}
	}

	private void registerSelectorThread(GamePacketHandler gph, String ip, int port)
	{
		try
		{
			SelectorThread<GameClient> selectorThread = new SelectorThread<GameClient>(Config.SELECTOR_CONFIG, _selectorStats, gph, gph, gph, null);
			selectorThread.openServerSocket(ip == null ? null : InetAddress.getByName(ip), port);
			selectorThread.start();
			_selectorThreads.add(selectorThread);
		}
		catch (Exception e)
		{
			//
		}
	}

	public static void main(String[] args) throws Exception
	{
		new GameServer();
	}

	public List<SelectorThread<GameClient>> getSelectorThreads()
	{
		return _selectorThreads;
	}

	public SelectorStats getSelectorStats()
	{
		return _selectorStats;
	}

	public long getServerStartTime()
	{
		return _serverStartTimeMillis;
	}

	public String getLicenseHost()
	{
		return _licenseHost;
	}

	public int getOnlineLimit()
	{
		return _onlineLimit;
	}

	public class GameServerListenerList extends ListenerList<GameServer>
	{
		public void onStart()
		{
			for (Listener<GameServer> listener : getListeners())
			{
				if (OnStartListener.class.isInstance(listener))
				{
					((OnStartListener) listener).onStart();
				}
			}
		}

		public void onShutdown()
		{
			for (Listener<GameServer> listener : getListeners())
			{
				if (OnShutdownListener.class.isInstance(listener))
				{
					((OnShutdownListener) listener).onShutdown();
				}
			}
		}
	}

	/**
	 * printSection the print game info
	 * 
	 * @param section
	 */
	public static void printSection(String section)
	{
		String s = "-=/ " + section + " /";
		while (s.length() < 64)
		{
			s = "-" + s;
		}
		_log.info(s);
	}
}