package l2s.gameserver.network.l2.s2c;

import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.items.ItemInstance;

public class DropItemPacket extends L2GameServerPacket
{
	private final Location _loc;
	private final int _playerId;
	private final int item_obj_id;
	private final int item_id;
	private final int _stackable;
	private final long _count;
	private final int _enchantLevel;
	private final boolean _augmented;
	private final int _ensoulCount;

	/**
	 * Constructor<?> of the DropItem server packet
	 * 
	 * @param item     : L2ItemInstance designating the item
	 * @param playerId : int designating the player ID who dropped the item
	 */
	public DropItemPacket(ItemInstance item, int playerId)
	{
		_playerId = playerId;
		item_obj_id = item.getObjectId();
		item_id = item.getItemId();
		_loc = item.getLoc();
		_stackable = item.isStackable() ? 1 : 0;
		_count = item.getCount();
		_enchantLevel = item.getEnchantLevel();
		_augmented = item.isAugmented();
		_ensoulCount = item.getNormalEnsouls().length + item.getSpecialEnsouls().length;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_playerId);
		writeD(item_obj_id);
		writeD(item_id);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeC(_stackable);
		writeQ(_count);
		writeC(1); // unknown
		writeC(_enchantLevel);
		writeC(_augmented);
		writeC(_ensoulCount);
	}
}