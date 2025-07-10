package l2s.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.commons.util.Rnd;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author nexvill
 */
public class FrostLordCastleFirstRbTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(FrostLordCastleFirstRbTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("30 21 * * tue,thu,sat");

	public FrostLordCastleFirstRbTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Frost Lord Castle First RB spawn Task: Started.");
		if (Rnd.get(100) < 50)
		{
			NpcUtils.spawnSingle(25942, 149224, 143816, -12232, 25769, ReflectionManager.FROST_LORD_CASTLE, 1_800_000); // Reggiesys
			ServerVariables.set("frost_lord_castle_first_rb", "reggiesys");
		}
		else
		{
			NpcUtils.spawnSingle(25943, 149224, 143816, -12232, 25769, ReflectionManager.FROST_LORD_CASTLE, 1_800_000); // Slicing
			ServerVariables.set("frost_lord_castle_first_rb", "slicing");
		}
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}