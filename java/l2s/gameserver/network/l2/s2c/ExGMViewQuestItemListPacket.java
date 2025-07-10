package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

/**
 * @author VISTALL
 * @date 4:20/06.05.2011
 */
public class ExGMViewQuestItemListPacket implements IClientOutgoingPacket
{
	private final int _type;
	private int _size;
	private ItemInstance[] _items;

	private int _limit;
	private String _name;

	public ExGMViewQuestItemListPacket(int type, Player player, ItemInstance[] items, int size)
	{
		_type = type;
		_items = items;
		_size = size;
		_name = player.getName();
		_limit = Config.QUEST_INVENTORY_MAXIMUM;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_type);
		if (_type == 1)
		{
			packetWriter.writeS(_name);
			packetWriter.writeD(_limit);
			packetWriter.writeD(_size);
		}
		else if (_type == 2)
		{
			packetWriter.writeD(_size);
			packetWriter.writeD(_size);
			for (ItemInstance temp : _items)
			{
				if (temp.getTemplate().isQuest())
					writeItemInfo(temp);
			}
		}
	}
}
