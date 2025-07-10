package l2s.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.Config;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;

/**
 * @author nexvill
 **/
public class SubjugationStartTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(SubjugationStartTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("00 00 * * mon");

	public SubjugationStartTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Subjugation Start Global Task: launched.");
		Config.SUBJUGATION_ENABLED = true;
		for (int i = 1; i < 8; i++)
		{
			for (Player player : GameObjectsStorage.getPlayers(true, true))
			{
				player.setVar(PlayerVariables.SUBJUGATION_ZONE_POINTS + "_" + i, 0);
				player.setVar(PlayerVariables.SUBJUGATION_ZONE_KEYS_USED + "_" + i, 0);
			}
		}
		_log.info("Subjugation Start Global Task: completed.");
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}