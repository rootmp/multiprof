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
public class FrostLordCastleCloseTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(FrostLordCastleCloseTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("59 23 * * tue,thu,sat");

	public FrostLordCastleCloseTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Frost Lord Castle Close Task: launched.");
		ServerVariables.set("frost_lord_castle_open", false);
		_log.info("Frost Lord Castle Close Task: completed.");
		ServerVariables.unset("frost_lord_castle_first_rb");
		for (Player player : GameObjectsStorage.getPlayers(false, false))
		{
			if (player.getReflectionId() == ReflectionManager.FROST_LORD_CASTLE.getId())
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