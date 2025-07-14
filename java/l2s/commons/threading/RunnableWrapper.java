package l2s.commons.threading;

//import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author UnAfraid
 */
public final class RunnableWrapper implements Runnable
{
	private static final Logger _log = LoggerFactory.getLogger(RunnableWrapper.class);

	private final Runnable _runnable;

	public RunnableWrapper(Runnable runnable)
	{
		_runnable = runnable;
	}

	@Override
	public void run()
	{
		try
		{
			_runnable.run();
		}
		catch(Exception e)
		{
			_log.error("Exception: " + e, e);
		}
		/*
		 * catch(final Throwable e) { final Thread t = Thread.currentThread(); final
		 * UncaughtExceptionHandler h = t.getUncaughtExceptionHandler(); if (h != null)
		 * { h.uncaughtException(t, e); } }
		 */
	}
}
