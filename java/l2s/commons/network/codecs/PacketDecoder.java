package l2s.commons.network.codecs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import l2s.commons.network.ChannelInboundHandler;
import l2s.commons.network.IConnectionState;
import l2s.commons.network.IIncomingPacket;
import l2s.commons.network.IIncomingPackets;
import l2s.commons.network.PacketReader;

/**
 * @author Nos
 * @author Java-man
 *
 * @param <T>
 */
public class PacketDecoder<T extends ChannelInboundHandler<?>> extends ByteToMessageDecoder
{
	private static final Logger logger = LoggerFactory.getLogger(PacketDecoder.class);

	private final IIncomingPackets<T>[] _incomingPackets;
	private final T _client;

	public PacketDecoder(IIncomingPackets<T>[] incomingPackets, T client)
	{
		_incomingPackets = incomingPackets;
		_client = client;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
	{
		if(in == null || !in.isReadable())
			return;

		try
		{
			final short packetId = in.readUnsignedByte();
			if(packetId >= _incomingPackets.length)
			{
				logger.debug("{}: Unknown packet: {}", _client, Integer.toHexString(packetId));
				return;
			}

			final IIncomingPackets<T> incomingPacket = _incomingPackets[packetId];
			if(incomingPacket == null)
			{
				logger.debug("{}: Unknown packet: {}", _client, Integer.toHexString(packetId));
				_client.onUnknownPacket();
				return;
			}

			final IConnectionState connectionState = ctx.channel().attr(IConnectionState.CONNECTION_STATE_ATTRIBUTE_KEY).get();

			ChannelInboundHandler<?> channelHandler = null;

			if((_client instanceof ChannelInboundHandler))
				channelHandler = (ChannelInboundHandler<?>) _client;

			if(channelHandler == null)
				return;

			if(!channelHandler.getIgnoreInvalidConnectionState()
					&& (connectionState == null || !incomingPacket.getConnectionStates().contains(connectionState)))
			{
				logger.warn("{}: Connection at invalid state: {} Required States: {}", incomingPacket, connectionState, incomingPacket.getConnectionStates());
				_client.onUnknownPacket();
				return;
			}

			final IIncomingPacket<T> packet = incomingPacket.newIncomingPacket();
			if(packet != null && packet.canBeRead(_client) && packet.read(_client, new PacketReader(in)))
			{
				if(channelHandler.isLogCPackets())
				{
					if(!packet.getClass().getSimpleName().equalsIgnoreCase("ExPacket"))
						logger.info("Client packet: {}", packet.getClass().getSimpleName());
				}
				out.add(packet);
			}
		}
		finally
		{
			// We always consider that we read whole packet.
			in.readerIndex(in.writerIndex());
		}
	}
}
