package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class ExStopMoveInShuttlePacket implements IClientOutgoingPacket
{
	private int _playerObjectId, _shuttleId, _playerHeading;
	private Location _loc;

	public ExStopMoveInShuttlePacket(Player cha)
	{
		_playerObjectId = cha.getObjectId();
		_shuttleId = cha.getBoat().getBoatId();
		_loc = cha.getInBoatPosition();
		_playerHeading = cha.getHeading();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_playerObjectId);// Player ObjID
		packetWriter.writeD(_shuttleId);// Shuttle ObjID
		packetWriter.writeD(_loc.x); // X in shuttle
		packetWriter.writeD(_loc.y); // Y in shuttle
		packetWriter.writeD(_loc.z); // Z in shuttle
		packetWriter.writeD(_playerHeading); // H in shuttle
	}
}