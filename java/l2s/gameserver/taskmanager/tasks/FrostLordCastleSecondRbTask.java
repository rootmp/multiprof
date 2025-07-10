package l2s.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author nexvill
 */
public class FrostLordCastleSecondRbTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(FrostLordCastleSecondRbTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("00 22 * * tue,thu,sat");

	public FrostLordCastleSecondRbTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Frost Lord Castle Second RB spawn Task: Started.");
		NpcUtils.spawnSingle(29135, 149224, 143816, -12232, 25769, ReflectionManager.FROST_LORD_CASTLE, 7_200_000); // Tiron
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}