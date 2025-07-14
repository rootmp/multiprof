package l2s.commons.net.netty;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;

/**
 * @author Java-man
 */
public abstract class ReceivablePacket<T> implements Runnable
{
	private final Logger LOGGER = LoggerFactory.getLogger(ReceivablePacket.class);
	private final T client;

	private final ByteBuf byteBuf;

	public ReceivablePacket(T client, ByteBuf byteBuf)
	{
		this.client = client;
		this.byteBuf = byteBuf;
	}

	public boolean isReadable()
	{
		return byteBuf.isReadable();
	}

	protected String readString(ByteBuf byteBuf)
	{
		short length = byteBuf.readShort();
		return byteBuf.readCharSequence(length, StandardCharsets.UTF_8).toString();
	}

	@Override
	public void run()
	{
		try
		{
			runImpl(client);
		}
		catch(Exception e)
		{
			LOGGER.error("Can't read packet." + e);
		}
	}

	protected abstract void runImpl(T client);
}
