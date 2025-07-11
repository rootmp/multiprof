package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.entity.boat.Boat;

public class VehicleInfoPacket implements IClientOutgoingPacket
{
	private int _boatObjectId;
	private Location _loc;

	public VehicleInfoPacket(Boat boat)
	{
		_boatObjectId = boat.getBoatId();
		_loc = boat.getLoc();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_boatObjectId);
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);
		packetWriter.writeD(_loc.h);
		return true;
	}
}