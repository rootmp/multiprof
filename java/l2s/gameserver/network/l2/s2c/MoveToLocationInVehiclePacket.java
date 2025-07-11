package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.boat.Boat;

public class MoveToLocationInVehiclePacket implements IClientOutgoingPacket
{
	private int _playerObjectId, _boatObjectId;
	private Location _origin, _destination;

	public MoveToLocationInVehiclePacket(Player cha, Boat boat, Location origin, Location destination)
	{
		_playerObjectId = cha.getObjectId();
		_boatObjectId = boat.getBoatId();
		_origin = origin;
		_destination = destination;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_playerObjectId);
		packetWriter.writeD(_boatObjectId);
		packetWriter.writeD(_destination.x);
		packetWriter.writeD(_destination.y);
		packetWriter.writeD(_destination.z);
		packetWriter.writeD(_origin.x);
		packetWriter.writeD(_origin.y);
		packetWriter.writeD(_origin.z);
		return true;
	}
}