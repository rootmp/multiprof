package l2s.commons.network.codecs;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import l2s.commons.network.ICrypt;

/**
 * @author Nos
 */
public class CryptCodec extends ByteToMessageCodec<ByteBuf>
{
	private final ICrypt _crypt;

	public CryptCodec(ICrypt crypt)
	{
		super();
		_crypt = crypt;
	}

	/*
	 * (non-Javadoc)
	 * @see io.netty.handler.codec.ByteToMessageCodec#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out)
	{
		// Check if there are any data to encrypt.
		if(!msg.isReadable())
		{ return; }

		msg.resetReaderIndex();
		_crypt.encrypt(msg);
		msg.resetReaderIndex();
		out.writeBytes(msg);
	}

	/*
	 * (non-Javadoc)
	 * @see io.netty.handler.codec.ByteToMessageCodec#decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List)
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
	{
		in.resetReaderIndex();
		_crypt.decrypt(in);
		in.readerIndex(in.writerIndex());
		out.add(in.copy(0, in.writerIndex()));
	}
}
