package l2s.commons.threading;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author NB4L1
 */
public final class RejectedExecutionHandlerImpl implements RejectedExecutionHandler
{
	private static final Logger _log = LoggerFactory.getLogger(RejectedExecutionHandlerImpl.class);

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
	{
		if(executor.isShutdown())
			return;

		_log.warn(r + " from " + executor, new RejectedExecutionException());

		if(Thread.currentThread().getPriority() > Thread.NORM_PRIORITY)
		{
			new Thread(r).start();
		}
		else
			r.run();
	}
}
