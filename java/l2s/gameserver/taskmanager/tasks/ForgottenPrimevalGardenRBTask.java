package l2s.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.time.cron.SchedulingPattern;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author nexvill
 */
public class ForgottenPrimevalGardenRBTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(ForgottenPrimevalGardenRBTask.class);

	private static final SchedulingPattern PATTERN = new SchedulingPattern("00 22 * * *");

	public ForgottenPrimevalGardenRBTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		_log.info("Forgotten Primeval Garden spawn RB Task: launched.");
		NpcUtils.spawnSingle(25938, -115000, 212008, -13152, 0, ReflectionManager.FORGOTTEN_PRIMEVAL_GARDEN, 3_600_000); // Tanas
		NpcUtils.spawnSingle(25927, -114728, 207528, -12960, 0, ReflectionManager.FORGOTTEN_PRIMEVAL_GARDEN, 3_600_000); // Kadin
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return PATTERN.next(System.currentTimeMillis());
	}
}