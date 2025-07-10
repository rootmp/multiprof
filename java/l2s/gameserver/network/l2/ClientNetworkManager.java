package l2s.gameserver.network.l2;

import l2s.commons.network.NetworkManager;
import l2s.gameserver.network.EventLoopGroupManager;

/**
 * @author Nos
 */
public class ClientNetworkManager extends NetworkManager
{
	protected ClientNetworkManager()
	{
		super(EventLoopGroupManager.getInstance().getBossGroup(),
				EventLoopGroupManager.getInstance().getWorkerGroup(),
				new ClientInitializer());
	}

	@Override
	public void stop() throws InterruptedException {
		super.stop();
		EventLoopGroupManager.getInstance().shutdown();
	}

	public static ClientNetworkManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final ClientNetworkManager _instance = new ClientNetworkManager();
	}
}
