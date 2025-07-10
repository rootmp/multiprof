package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.boat.Shuttle;

/**
 * @author Bonux
 **/
public class ExMTLInSuttlePacket implements IClientOutgoingPacket
{
	private int _playableObjectId, _shuttleId;
	private Location _origin, _destination;

	public ExMTLInSuttlePacket(Player player, Shuttle shuttle, Location origin, Location destination)
	{
		_playableObjectId = player.getObjectId();
		_shuttleId = shuttle.getBoatId();
		_origin = origin;
		_destination = destination;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_playableObjectId); // Player ObjID
		packetWriter.writeD(_shuttleId); // Shuttle ObjID
		packetWriter.writeD(_destination.x); // Destination X in shuttle
		packetWriter.writeD(_destination.y); // Destination Y in shuttle
		packetWriter.writeD(_destination.z); // Destination Z in shuttle
		packetWriter.writeD(_origin.x); // X in shuttle
		packetWriter.writeD(_origin.y); // Y in shuttle
		packetWriter.writeD(_origin.z); // Z in shuttle
	}
}