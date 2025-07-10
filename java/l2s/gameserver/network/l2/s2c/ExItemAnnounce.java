package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

/**
 * @author NviX
 */
public class ExItemAnnounce implements IClientOutgoingPacket
{
	public static final int ENCHANT = 0;
	public static final int BOX = 1;
	public static final int RANDOM_CRAFT = 2;
	public static final int SPECIAL_CRAFT = 3;
	public static final int WORKSHOP = 4;
	public static final int STEEL_DOOR_GUILD_SECRET_SHOP = 5;
	public static final int AUCTION_ANNOUNCE = 6;

	private final String _name;
	private final int _itemId;
	private final int _enchantLevel;
	private final int _type;
	private final int _misc;

	public ExItemAnnounce(String name, int itemId, int enchantLevel, int type, int misc)
	{
		_name = name;
		_itemId = itemId;
		_enchantLevel = enchantLevel;
		_type = type;
		_misc = misc;
	}

	public ExItemAnnounce(Player player, ItemInstance item, int type)
	{
		this(player, item, type, 0);
	}

	public ExItemAnnounce(Player player, ItemInstance item, int type, int misc)
	{
		_name = player.getName();
		_type = type;
		_itemId = item.getItemId();
		_enchantLevel = item.getEnchantLevel();
		_misc = misc;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		// _type
		// 0 - enchant
		// 1 - item get from container
		// 2 - item get from random creation
		// 3 - item get from special creation
		// 4 - item get from workbench?
		// 5 - item get from festival
		// 6 - item get from "limited random creation"
		// 7 - fire and item get from container
		// 8 and others - null item name by item_id and icon from chest.
		
	    packetWriter.writeC(_type); // announce type
	    packetWriter.writeString(_name); // name of player
	    packetWriter.writeD(_itemId); // item id
	    packetWriter.writeC(_enchantLevel); // enchant level
	    packetWriter.writeD(_misc); // chest item id
	}
}