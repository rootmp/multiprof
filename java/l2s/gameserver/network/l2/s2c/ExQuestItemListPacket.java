package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.enums.LockType;
import l2s.gameserver.model.items.ItemInstance;

/**
 * @author JIV
 */
public class ExQuestItemListPacket implements IClientOutgoingPacket
{
	private int _size;
	private ItemInstance[] _items;
	private final int _sendType;

	public ExQuestItemListPacket(int type, int size, ItemInstance[] items, LockType lockType, int[] lockItems)
	{
		_sendType = type;
		_size = size;
		_items = items;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_sendType);
		if (_sendType == 1)
		{
			packetWriter.writeH(0x00); // UNK
			packetWriter.writeD(_size);
		}
		else if (_sendType == 2)
		{
			packetWriter.writeD(_size);
			packetWriter.writeD(_size);
			for (ItemInstance temp : _items)
			{
				if (!temp.getTemplate().isQuest())
				{
					continue;
				}
				writeItemInfo(packetWriter, temp);
			}
		}
		return true;
	}
}
