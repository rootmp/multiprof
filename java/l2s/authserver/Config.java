package l2s.authserver;

import java.io.File;
import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.authserver.crypt.PasswordHash;
import l2s.authserver.crypt.ScrambledKeyPair;
import l2s.commons.configuration.ExProperties;
import l2s.commons.util.Rnd;

public class Config
{
	private final static Logger LOG = LoggerFactory.getLogger(Config.class);

	public static final String LOGIN_CONFIGURATION_FILE = "config/authserver.properties";
	public static final String SERVER_NAMES_FILE = "config/servername.xml";

	public static String LOGIN_HOST;
	public static int PORT_LOGIN;

	public static String GAME_SERVER_LOGIN_HOST;
	public static int GAME_SERVER_LOGIN_PORT;
	public static long GAME_SERVER_PING_DELAY;
	public static int GAME_SERVER_PING_RETRY;

	public static String DATABASE_DRIVER;
	public static int DATABASE_MAX_CONNECTIONS;
	public static int DATABASE_MAX_IDLE_TIMEOUT;
	public static int DATABASE_IDLE_TEST_PERIOD;
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;

	public static String DEFAULT_PASSWORD_HASH;
	public static String LEGACY_PASSWORD_HASH;

	public static int LOGIN_BLOWFISH_KEYS;
	public static int LOGIN_RSA_KEYPAIRS;

	public static boolean ACCEPT_NEW_GAMESERVER;
	public static boolean AUTO_CREATE_ACCOUNTS;

	public static String ANAME_TEMPLATE;
	public static String APASSWD_TEMPLATE;

	public static final Map<Integer, String> SERVER_NAMES = new HashMap<Integer, String>();

	public final static long LOGIN_TIMEOUT = 60 * 1000L;

	public static int LOGIN_TRY_BEFORE_BAN;
	public static long LOGIN_TRY_TIMEOUT;
	public static long IP_BAN_TIME;

	private static ScrambledKeyPair[] _keyPairs;
	private static byte[][] _blowfishKeys;

	public static PasswordHash DEFAULT_CRYPT;
	public static PasswordHash[] LEGACY_CRYPT;

	public static boolean LOGIN_LOG;

	public static boolean CHEAT_PASSWORD_CHECK;

	public static int CHECK_BANS_INTERVAL;

	public static boolean SHOW_LICENCE;

	// it has no instancies
	private Config()
	{}

	public final static void load()
	{
		loadConfiguration();
		loadServerNames();
	}

	public final static void initCrypt() throws Exception
	{
		DEFAULT_CRYPT = new PasswordHash(Config.DEFAULT_PASSWORD_HASH);
		List<PasswordHash> legacy = new ArrayList<PasswordHash>();
		for(String method : Config.LEGACY_PASSWORD_HASH.split(";"))
			if(!method.equalsIgnoreCase(Config.DEFAULT_PASSWORD_HASH))
				legacy.add(new PasswordHash(method));
		LEGACY_CRYPT = legacy.toArray(new PasswordHash[legacy.size()]);

		LOG.info("Loaded " + Config.DEFAULT_PASSWORD_HASH + " as default crypt.");

		_keyPairs = new ScrambledKeyPair[Config.LOGIN_RSA_KEYPAIRS];

		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4);
		keygen.initialize(spec);

		for(int i = 0; i < _keyPairs.length; i++)
			_keyPairs[i] = new ScrambledKeyPair(keygen.generateKeyPair());

		LOG.info("Cached " + _keyPairs.length + " KeyPairs for RSA communication");

		_blowfishKeys = new byte[Config.LOGIN_BLOWFISH_KEYS][16];

		for(int i = 0; i < _blowfishKeys.length; i++)
			for(int j = 0; j < _blowfishKeys[i].length; j++)
				_blowfishKeys[i][j] = (byte) (Rnd.get(255) + 1);

		LOG.info("Stored " + _blowfishKeys.length + " keys for Blowfish communication");
	}

	public final static void loadServerNames()
	{
		SERVER_NAMES.clear();

		try
		{
			SAXReader reader = new SAXReader(true);
			Document document = reader.read(new File(SERVER_NAMES_FILE));

			Element root = document.getRootElement();

			for(Iterator<Element> itr = root.elementIterator(); itr.hasNext();)
			{
				Element node = itr.next();
				if(node.getName().equalsIgnoreCase("server"))
				{
					Integer id = Integer.valueOf(node.attributeValue("id"));
					String name = node.attributeValue("name");
					SERVER_NAMES.put(id, name);
				}
			}

			LOG.info("Loaded " + SERVER_NAMES.size() + " server names");
		}
		catch(Exception e)
		{
			LOG.error("", e);
		}
	}

	public final static void loadConfiguration()
	{
		ExProperties serverSettings = load(LOGIN_CONFIGURATION_FILE);

		LOGIN_HOST = serverSettings.getProperty("LoginserverHostname", "127.0.0.1");
		PORT_LOGIN = serverSettings.getProperty("LoginserverPort", 2106);

		GAME_SERVER_LOGIN_HOST = serverSettings.getProperty("LoginHost", "127.0.0.1");
		GAME_SERVER_LOGIN_PORT = serverSettings.getProperty("LoginPort", 9014);

		DATABASE_DRIVER = serverSettings.getProperty("DATABASE_DRIVER", "org.mariadb.jdbc.Driver");

		String databaseHost = serverSettings.getProperty("DATABASE_HOST", "localhost");
		int databasePort = serverSettings.getProperty("DATABASE_PORT", 3306);
		String databaseName = serverSettings.getProperty("DATABASE_NAME", "l2auth");

		DATABASE_URL = serverSettings.getProperty("DATABASE_URL", "jdbc:mariadb://" + databaseHost + ":" + databasePort + "/" + databaseName
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
		DATABASE_LOGIN = serverSettings.getProperty("DATABASE_LOGIN", "root");
		DATABASE_PASSWORD = serverSettings.getProperty("DATABASE_PASSWORD", "root");

		DATABASE_MAX_CONNECTIONS = serverSettings.getProperty("MaximumDbConnections", 3);
		DATABASE_MAX_IDLE_TIMEOUT = serverSettings.getProperty("MaxIdleConnectionTimeout", 600);
		DATABASE_IDLE_TEST_PERIOD = serverSettings.getProperty("IdleConnectionTestPeriod", 60);

		LOGIN_BLOWFISH_KEYS = serverSettings.getProperty("BlowFishKeys", 20);
		LOGIN_RSA_KEYPAIRS = serverSettings.getProperty("RSAKeyPairs", 10);

		ACCEPT_NEW_GAMESERVER = serverSettings.getProperty("AcceptNewGameServer", true);
		DEFAULT_PASSWORD_HASH = serverSettings.getProperty("PasswordHash", "whirlpool2");
		LEGACY_PASSWORD_HASH = serverSettings.getProperty("LegacyPasswordHash", "sha1");

		AUTO_CREATE_ACCOUNTS = serverSettings.getProperty("AutoCreateAccounts", true);
		ANAME_TEMPLATE = serverSettings.getProperty("AccountTemplate", "[A-Za-z0-9]{4,14}");
		APASSWD_TEMPLATE = serverSettings.getProperty("PasswordTemplate", "[A-Za-z0-9]{4,16}");

		LOGIN_TRY_BEFORE_BAN = serverSettings.getProperty("LoginTryBeforeBan", 10);
		LOGIN_TRY_TIMEOUT = serverSettings.getProperty("LoginTryTimeout", 5) * 1000L;
		IP_BAN_TIME = serverSettings.getProperty("IpBanTime", 300) * 1000L;
		GAME_SERVER_PING_DELAY = serverSettings.getProperty("GameServerPingDelay", 30) * 1000L;
		GAME_SERVER_PING_RETRY = serverSettings.getProperty("GameServerPingRetry", 4);

		LOGIN_LOG = serverSettings.getProperty("LoginLog", true);
		CHEAT_PASSWORD_CHECK = serverSettings.getProperty("CheatPasswordCheck", false);
		CHECK_BANS_INTERVAL = serverSettings.getProperty("CHECK_BANS_INTERVAL", 5);
		SHOW_LICENCE = serverSettings.getProperty("ShowLicence", true);
	}

	public static ExProperties load(String filename)
	{
		return load(new File(filename));
	}

	public static ExProperties load(File file)
	{
		ExProperties result = new ExProperties();

		try
		{
			result.load(file);
		}
		catch(IOException e)
		{
			LOG.error("", e);
		}

		return result;
	}

	public static ScrambledKeyPair getScrambledRSAKeyPair()
	{
		return _keyPairs[Rnd.get(_keyPairs.length)];
	}

	public static byte[] getBlowfishKey()
	{
		return _blowfishKeys[Rnd.get(_blowfishKeys.length)];
	}
}
