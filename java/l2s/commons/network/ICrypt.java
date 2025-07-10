package l2s.commons.network;

import io.netty.buffer.ByteBuf;

/**
 * @author Nos
 */
public interface ICrypt
{
	void setKey(byte[] key);

	void encrypt(ByteBuf buf);

	void decrypt(ByteBuf buf);
}
