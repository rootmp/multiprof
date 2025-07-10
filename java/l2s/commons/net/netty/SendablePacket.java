package l2s.commons.net.netty;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;

/**
 * @author Java-man
 */
public abstract class SendablePacket
{
  private final Logger logger = LoggerFactory.getLogger(ReceivablePacket.class);
	protected abstract byte getOpCode();

	public ByteBuf write(ByteBuf byteBuf)
	{
		try
		{
			byteBuf.writeByte(getOpCode());
			return writeImpl(byteBuf);
		}
		catch(RuntimeException e)
		{
			logger.error("Can't write buffer."+e);
			throw new RuntimeException("Can't write buffer.", e);
		}
	}

	protected abstract ByteBuf writeImpl(ByteBuf byteBuf);

	protected void writeString(ByteBuf byteBuf, CharSequence charSequence)
	{
		byteBuf.writeShort(charSequence.length());
		byteBuf.writeCharSequence(charSequence, StandardCharsets.UTF_8);
	}
}
