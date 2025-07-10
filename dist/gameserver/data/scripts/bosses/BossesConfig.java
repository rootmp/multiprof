package bosses;

import l2s.commons.configuration.ExProperties;
import l2s.gameserver.Config;
import l2s.gameserver.listener.script.OnLoadScriptListener;

/**
 * @author Bonux
 **/
public class BossesConfig implements OnLoadScriptListener
{
	private static final String PROPERTIES_FILE = "config/bosses.properties";

	// Baium
	public static long BAIUM_RESPAWN_TIME;
	public static int BAIUM_RAID_DURATION;
	public static int BAIUM_SLEEP_TIME;

	@Override
	public void onLoad()
	{
		ExProperties properties = Config.load(PROPERTIES_FILE);

		// Baium
		BAIUM_RESPAWN_TIME = properties.getProperty("BAIUM_RESPAWN_TIME", 28800);
		BAIUM_RAID_DURATION = properties.getProperty("BAIUM_RAID_DURATION", 60);
		BAIUM_SLEEP_TIME = properties.getProperty("BAIUM_SLEEP_TIME", 30);
	}
}