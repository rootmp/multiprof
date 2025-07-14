package l2s.gameserver.network.l2.s2c.itemrestore;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

/**
 * @author nexvill
 */
public class ExPenaltyItemDrop implements IClientOutgoingPacket
{
	private final Location _loc;
	private final int _itemId;

	public ExPenaltyItemDrop(Location loc, int itemId)
	{
		_loc = loc;
		_itemId = itemId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_loc.getX()); // x
		packetWriter.writeD(_loc.getY()); // y
		packetWriter.writeD(_loc.getZ()); // z
		packetWriter.writeD(_itemId);
		return true;
	}
}