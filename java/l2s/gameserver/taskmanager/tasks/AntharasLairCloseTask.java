package l2s.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;

/**
 * @author nexvill
 */
public class AntharasLairCloseTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(AntharasLairCloseTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("59 23 * * mon,wed,fri");

	public AntharasLairCloseTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Antharas' Lair Close Task: launched.");
		ServerVariables.set("antharas_lair_open", false);
		_log.info("Antharas' Lair Close Task: completed.");
		for (Player player : GameObjectsStorage.getPlayers(false, false))
		{
			if (player.getReflectionId() == ReflectionManager.ANTHARAS_LAIR.getId())
			{
				player.teleToLocation(player.getReflection().getReturnLoc(), ReflectionManager.MAIN);
			}
		}
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}