package l2s.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.model.entity.olympiad.OlympiadDatabase;

/**
 * @author VISTALL
 * @date 20:11/24.06.2011
 */
public class OlympiadSaveTask extends AutomaticTask
{
	private static final Logger _log = LoggerFactory.getLogger(OlympiadSaveTask.class);

	public OlympiadSaveTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		long t = System.currentTimeMillis();

		_log.info("OlympiadSaveTask: Database save started.");
		OlympiadDatabase.save();
		_log.info("OlympiadSaveTask: Database saved finished in: " + (System.currentTimeMillis() - t) + " ms.");
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return System.currentTimeMillis() + 600000L;
	}
}
