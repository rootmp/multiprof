package l2s.commons.network.codecs;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @author Nos
 */
@Sharable
public class LengthFieldBasedFrameEncoder extends MessageToMessageEncoder<ByteBuf>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out)
	{
		final ByteBuf buf = ctx.alloc().buffer(2);
		final short length = (short) (msg.readableBytes() + 2);
		buf.writeShortLE(length);
		out.add(buf);
		out.add(msg.retain());
	}
}
