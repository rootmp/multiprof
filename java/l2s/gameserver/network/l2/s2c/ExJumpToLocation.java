package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;

public class ExJumpToLocation implements IClientOutgoingPacket
{
	private int _objectId;
	private Location _current;
	private Location _destination;

	public ExJumpToLocation(Creature cha)
	{
		_objectId = cha.getObjectId();
		_current = cha.getLoc();
		_destination = cha.getLoc();
	}

	public ExJumpToLocation(int objectId, Location from, Location to)
	{
		_objectId = objectId;
		_current = from;
		_destination = to;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objectId);

		packetWriter.writeD(_destination.x);
		packetWriter.writeD(_destination.y);
		packetWriter.writeD(_destination.z);

		packetWriter.writeD(_current.x);
		packetWriter.writeD(_current.y);
		packetWriter.writeD(_current.z);
		return true;
	}
}