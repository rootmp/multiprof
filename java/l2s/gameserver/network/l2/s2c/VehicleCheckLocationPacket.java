package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.entity.boat.Boat;

public class VehicleCheckLocationPacket implements IClientOutgoingPacket
{
	private int _boatObjectId;
	private Location _loc;

	public VehicleCheckLocationPacket(Boat instance)
	{
		_boatObjectId = instance.getBoatId();
		_loc = instance.getLoc();
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