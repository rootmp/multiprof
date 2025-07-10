package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.IIncomingPacket;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;

/**
 * Packets received by the game server from clients
 * @author KenM
 */
public interface IClientIncomingPacket extends IIncomingPacket<GameClient>
{
	@Override
	default boolean canBeRead(GameClient client)
	{
		return client.checkFloodProtection(getFloodProtectorType(), getClass().getSimpleName());
	}

	/**
	 * Reads a packet.
	 * @param client the client
	 * @param packet the packet reader
	 * @return {@code true} if packet was read successfully, {@code false} otherwise.
	 */
	@Override
	default boolean read(GameClient client, PacketReader packet)
	{
		return readImpl(client, packet);
	}

	/**
	 * Reads a packet.
	 * @param client the client
	 * @param packet the packet reader
	 * @return {@code true} if packet was read successfully, {@code false} otherwise.
	 */
	boolean readImpl(GameClient client, PacketReader packet);

	default String getType()
	{
		return "[C] " + getClass().getSimpleName();
	}

	default String getFloodProtectorType()
	{
		return getClass().getSimpleName();
	}
}
