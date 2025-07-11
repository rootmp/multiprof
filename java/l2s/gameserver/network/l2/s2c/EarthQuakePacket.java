package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.geometry.Location;

/**
 * format dddddd
 */
public class EarthQuakePacket implements IClientOutgoingPacket
{
	private Location _loc;
	private int _intensity;
	private int _duration;

	public EarthQuakePacket(Location loc, int intensity, int duration)
	{
		_loc = loc;
		_intensity = intensity;
		_duration = duration;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);
		packetWriter.writeD(_intensity);
		packetWriter.writeD(_duration);
		packetWriter.writeD(0x00); // Unknown
		return true;
	}
}