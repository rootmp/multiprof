package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;

public class RidePacket implements IClientOutgoingPacket
{
	private int _mountType, _id, _rideClassID;
	private Location _loc;

	public RidePacket(Player cha)
	{
		_id = cha.getObjectId();
		_mountType = cha.getMountType().ordinal();
		_rideClassID = cha.getMountNpcId() + 1000000;
		_loc = cha.getLoc();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_id);
		packetWriter.writeD(_mountType);
		packetWriter.writeD(_mountType);
		packetWriter.writeD(_rideClassID);
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);
		return true;
	}
}