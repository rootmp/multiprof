package l2s.authserver.network.l2.c2s;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.authserver.network.l2.L2LoginClient;
import l2s.commons.net.nio.impl.ReceivablePacket;

public abstract class L2LoginClientPacket extends ReceivablePacket<L2LoginClient>
{
	private static Logger _log = LoggerFactory.getLogger(L2LoginClientPacket.class);

	@Override
	protected final boolean read()
	{
		try
		{
			return readImpl();
		}
		catch(Exception e)
		{
			_log.error("", e);
			return false;
		}
	}

	@Override
	public void run()
	{
		try
		{
			runImpl();
		}
		catch(Exception e)
		{
			_log.error("", e);
		}
	}

	protected abstract boolean readImpl();

	protected abstract void runImpl() throws Exception;
}
