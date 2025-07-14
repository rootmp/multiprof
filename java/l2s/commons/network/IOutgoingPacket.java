package l2s.commons.network;

import io.netty.buffer.ByteBuf;

/**
 * @author Nos
 */
public interface IOutgoingPacket
{
	boolean canBeWritten();

	ByteBuf getOpcodes();

	/**
	 * @param packetWriter the packet writer
	 * @return {@code true} if packet was writen successfully, {@code false} otherwise.
	 */
	boolean write(PacketWriter packetWriter);

	String getType();
}
