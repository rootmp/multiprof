package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.entity.boat.Shuttle;

/**
 * @author Bonux
 **/
public class ExSuttleGetOffPacket implements IClientOutgoingPacket
{
	private int _playerObjectId, _shuttleId;
	private Location _loc;

	public ExSuttleGetOffPacket(Playable cha, Shuttle shuttle, Location loc)
	{
		_playerObjectId = cha.getObjectId();
		_shuttleId = shuttle.getBoatId();
		_loc = loc;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_playerObjectId); // Player ObjID
		packetWriter.writeD(_shuttleId); // Shuttle ObjID
		packetWriter.writeD(_loc.x); // X in shuttle
		packetWriter.writeD(_loc.y); // Y in shuttle
		packetWriter.writeD(_loc.z); // Z in shuttle
	}
}