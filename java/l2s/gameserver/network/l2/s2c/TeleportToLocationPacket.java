package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.GameObject;

/**
 * format dddd sample 0000: 3a 69 08 10 48 02 c1 00 00 f7 56 00 00 89 ea ff
 * :i..H.....V..... 0010: ff 0c b2 d8 61 ....a
 */
public class TeleportToLocationPacket implements IClientOutgoingPacket
{
	private int _targetId;
	private Location _loc;

	public TeleportToLocationPacket(GameObject cha, Location loc)
	{
		_targetId = cha.getObjectId();
		_loc = loc;
	}

	public TeleportToLocationPacket(GameObject cha, int x, int y, int z)
	{
		_targetId = cha.getObjectId();
		_loc = new Location(x, y, z, cha.getHeading());
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_targetId);
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);
		packetWriter.writeD(0x00); // IsValidation
		packetWriter.writeD(_loc.h);
		packetWriter.writeD(0); // ??? 0
		return true;
	}
}