package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 */
public class ExStopScenePlayerPacket implements IClientOutgoingPacket
{
	private final int _movieId;

	public ExStopScenePlayerPacket(int movieId)
	{
		_movieId = movieId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_movieId);
		return true;
	}
}
