package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.entity.boat.Boat;

public class VehicleDeparturePacket implements IClientOutgoingPacket
{
	private int _moveSpeed, _rotationSpeed;
	private int _boatObjId;
	private Location _loc;

	public VehicleDeparturePacket(Boat boat)
	{
		_boatObjId = boat.getBoatId();
		_moveSpeed = boat.getMoveSpeed();
		_rotationSpeed = boat.getRotationSpeed();
		_loc = boat.getMovement().getDestination();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_boatObjId);
		packetWriter.writeD(_moveSpeed);
		packetWriter.writeD(_rotationSpeed);
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);
	}
}