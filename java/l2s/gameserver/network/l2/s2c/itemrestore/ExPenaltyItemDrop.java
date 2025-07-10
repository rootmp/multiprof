package l2s.gameserver.network.l2.s2c.itemrestore;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPenaltyItemDrop extends L2GameServerPacket
{
	private final Location _loc;
	private final int _itemId;

	public ExPenaltyItemDrop(Location loc, int itemId)
	{
		_loc = loc;
		_itemId = itemId;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_loc.getX()); // x
		writeD(_loc.getY()); // y
		writeD(_loc.getZ()); // z
		writeD(_itemId);
	}
}