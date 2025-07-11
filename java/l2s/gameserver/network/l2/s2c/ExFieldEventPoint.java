package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExFieldEventPoint implements IClientOutgoingPacket
{
	private final int _points;

	public ExFieldEventPoint(int points)
	{
		_points = points;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_points);
		return true;
	}
}