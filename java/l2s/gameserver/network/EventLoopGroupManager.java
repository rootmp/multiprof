package l2s.gameserver.network;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author Nos
 */
public class EventLoopGroupManager
{
	// TODO config
	private static int IO_PACKET_THREAD_CORE_SIZE = 4;

	private final EventLoopGroup _bossGroup;
	private final EventLoopGroup _workerGroup;

	public EventLoopGroupManager()
	{
		if(Epoll.isAvailable())
		{
			_bossGroup = new EpollEventLoopGroup(1);
			_workerGroup = new EpollEventLoopGroup(IO_PACKET_THREAD_CORE_SIZE);
		}
		else
		{
			_bossGroup = new NioEventLoopGroup(1);
			_workerGroup = new NioEventLoopGroup(IO_PACKET_THREAD_CORE_SIZE);
		}
	}

	public EventLoopGroup getBossGroup()
	{
		return _bossGroup;
	}

	public EventLoopGroup getWorkerGroup()
	{
		return _workerGroup;
	}

	public void shutdown()
	{
		_bossGroup.shutdownGracefully();
		_workerGroup.shutdownGracefully();
	}

	public static EventLoopGroupManager getInstance()
	{
		return SingletonHolder._instance;
	}

	private static class SingletonHolder
	{
		protected static final EventLoopGroupManager _instance = new EventLoopGroupManager();
	}
}
