package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.items.ItemInstance;

/**
 * 0000: 17 1a 95 20 48 9b da 12 40 44 17 02 00 03 f0 fc ff 98 f1 ff ff .....
 * format ddddd
 */
public class GetItemPacket implements IClientOutgoingPacket
{
	private int _playerId, _itemObjId;
	private Location _loc;

	public GetItemPacket(ItemInstance item, int playerId)
	{
		_itemObjId = item.getObjectId();
		_loc = item.getLoc();
		_playerId = playerId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_playerId);
		packetWriter.writeD(_itemObjId);
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);
	}
}