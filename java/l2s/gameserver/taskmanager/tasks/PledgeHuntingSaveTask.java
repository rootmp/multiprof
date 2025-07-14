package l2s.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.tables.ClanTable;

/**
 * @author Bonux
 **/
public class PledgeHuntingSaveTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(PledgeHuntingSaveTask.class);

	private static final long SAVE_DELAY = 10 * 60 * 1000; // 10 minutes

	public PledgeHuntingSaveTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		long t = System.currentTimeMillis();

		_log.info("Clan Hunting Task: Database save started.");
		ClanTable.getInstance().saveClanHuntingProgress();
		_log.info("Clan Hunting Task: Database saved finished in: " + (System.currentTimeMillis() - t) + " ms.");
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return System.currentTimeMillis() + SAVE_DELAY;
	}
}
