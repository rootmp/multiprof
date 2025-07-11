package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.geometry.ILocation;
import l2s.gameserver.model.Player;

/**
 * @author Bonux
 **/
public class ExFlyMoveBroadcast implements IClientOutgoingPacket
{
	private int _objId;
	// private final WayType _type;
	private final int _trackId;
	private ILocation _loc;
	private ILocation _destLoc;

	public ExFlyMoveBroadcast(Player player, /* WayType type, */int trackId, ILocation destLoc)
	{
		_objId = player.getObjectId();
		// _type = type;
		_trackId = trackId;
		_loc = player;
		_destLoc = destLoc;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objId);

		packetWriter.writeD(1/* _type.ordinal() */);
		packetWriter.writeD(_trackId);

		packetWriter.writeD(_loc.getX());
		packetWriter.writeD(_loc.getY());
		packetWriter.writeD(_loc.getZ());

		packetWriter.writeD(0x00); // TODO: [Bonux]

		packetWriter.writeD(_destLoc.getX());
		packetWriter.writeD(_destLoc.getY());
		packetWriter.writeD(_destLoc.getZ());
		return true;
	}
}
