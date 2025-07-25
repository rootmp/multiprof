package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.geometry.Location;

/**
 * Примеры пакетов: Ставит флажок на карте и показывает стрелку на компасе: EB
 * 00 00 00 00 01 00 00 00 40 2B FF FF 8C 3C 02 00 A0 F6 FF FF Убирает флажок и
 * стрелку EB 02 00 00 00 02 00 00 00 40 2B FF FF 8C 3C 02 00 A0 F6 FF FF
 */
public class RadarControlPacket implements IClientOutgoingPacket
{
	private int _x, _y, _z, _type, _showRadar;

	public RadarControlPacket(int showRadar, int type, Location loc)
	{
		this(showRadar, type, loc.x, loc.y, loc.z);
	}

	public RadarControlPacket(int showRadar, int type, int x, int y, int z)
	{
		_showRadar = showRadar; // showRadar?? 0 = showRadar; 1 = delete radar;
		_type = type; // 1 - только стрелка над головой, 2 - флажок на карте
		_x = x;
		_y = y;
		_z = z;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_showRadar);
		packetWriter.writeD(_type); // maybe type
		packetWriter.writeD(_x); // x
		packetWriter.writeD(_y); // y
		packetWriter.writeD(_z); // z
		return true;
	}
}