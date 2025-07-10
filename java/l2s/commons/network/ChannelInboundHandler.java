package l2s.commons.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @param <T>
 *
 * @author Nos
 * @author Java-man
 */
public abstract class ChannelInboundHandler<T extends ChannelInboundHandler<?>> extends SimpleChannelInboundHandler<IIncomingPacket<T>>
{
	private static final Logger logger = LoggerFactory.getLogger(ChannelInboundHandler.class);

	private Channel _channel;

	private int _failedPackets = 0;
	private int _unknownPackets = 0;
	private boolean _ignoreInvalidConnectionState = false;
	private boolean _logSPackets = false;
	private boolean _logCPackets = false;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx)
	{
		_channel = ctx.channel();
	}

	public void setConnectionState(IConnectionState connectionState)
	{
		_channel.attr(IConnectionState.CONNECTION_STATE_ATTRIBUTE_KEY).set(connectionState);
	}

	public IConnectionState getConnectionState()
	{
		return _channel != null ? _channel.attr(IConnectionState.CONNECTION_STATE_ATTRIBUTE_KEY).get() : null;
	}

	public void setIgnoreInvalidConnectionState(boolean ignore)
	{
		_ignoreInvalidConnectionState = ignore;
	}

	public boolean getIgnoreInvalidConnectionState()
	{
		return _ignoreInvalidConnectionState;
	}

	public void onPacketReadFail()
	{
		if(_failedPackets++ >= 5)
		{
			logger.info("Too many client packet fails, connection closed : %s", toString());
			if(_channel != null)
			{
				_channel.close();
			}
		}
	}

	public void onUnknownPacket()
	{
		if(_unknownPackets++ >= 5)
		{
			logger.warn("Too many client unknown packets, connection closed : %s", toString());
			if(_channel != null)
			{
				_channel.close();
			}
		}
	}
	
	public boolean isLogSPackets()
	{
		return _logSPackets;
	}

	public void setLogSPackets(boolean log)
	{
		_logSPackets = log;
	}
	
	public boolean isLogCPackets()
	{
		return _logCPackets;
	}

	public void setLogCPackets(boolean log)
	{
		_logCPackets = log;
	}
}
