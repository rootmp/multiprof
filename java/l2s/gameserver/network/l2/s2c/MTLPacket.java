package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Creature;
import l2s.gameserver.utils.Log;

public class MTLPacket implements IClientOutgoingPacket
{
	private int _objectId;
	private Location _current;
	private Location _destination;

	public MTLPacket(Creature cha)
	{
		_objectId = cha.getObjectId();
		_current = cha.getLoc();
		_destination = cha.getMovement().getDestination();

		if (_destination == null)
		{
			Log.debug("MTLPacket: desc is null, but moving. L2Character: " + cha.getObjectId() + ":" + cha.getName() + "; Loc: " + _current);
			_destination = _current;
		}
	}

	public MTLPacket(int objectId, Location from, Location to)
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