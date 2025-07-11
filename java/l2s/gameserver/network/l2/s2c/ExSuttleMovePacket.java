package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.entity.boat.Shuttle;

/**
 * @author Bonux
 **/
public class ExSuttleMovePacket implements IClientOutgoingPacket
{
	private final Shuttle _shuttle;
	private final Location _destination;

	public ExSuttleMovePacket(Shuttle shuttle)
	{
		_shuttle = shuttle;
		_destination = shuttle.getMovement().getDestination();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_shuttle.getBoatId()); // Shuttle ObjID
		packetWriter.writeD(_shuttle.getMoveSpeed()); // Speed
		packetWriter.writeD(_shuttle.getRotationSpeed()); // Rotation Speed
		packetWriter.writeD(_destination.getX()); // Destination X
		packetWriter.writeD(_destination.getY()); // Destination Y
		packetWriter.writeD(_destination.getZ()); // Destination Z
		return true;
	}
}