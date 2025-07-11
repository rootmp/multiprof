package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;

public class ValidateLocationInVehiclePacket implements IClientOutgoingPacket
{
	private int _playerObjectId, _boatObjectId;
	private Location _loc;

	public ValidateLocationInVehiclePacket(Player player)
	{
		_playerObjectId = player.getObjectId();
		_boatObjectId = player.getBoat().getBoatId();
		_loc = player.getInBoatPosition();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_playerObjectId);
		packetWriter.writeD(_boatObjectId);
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);
		packetWriter.writeD(_loc.h);
		return true;
	}
}