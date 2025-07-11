package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;

public class ObserverEndPacket implements IClientOutgoingPacket
{
	// ddSS
	private Location _loc;

	public ObserverEndPacket(Location loc)
	{
		_loc = loc;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);
		return true;
	}
}