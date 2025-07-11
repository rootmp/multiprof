package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;

public class GMViewWarehouseWithdrawListPacket implements IClientOutgoingPacket
{
	private final int _type;
	private final ItemInstance[] _items;
	private String _charName;
	private long _charAdena;

	public GMViewWarehouseWithdrawListPacket(int type, Player cha)
	{
		_type = type;
		_charName = cha.getName();
		_charAdena = cha.getWarehouse().getAdena();
		_items = cha.getWarehouse().getItems();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_type);
		if (_type == 1)
		{
			packetWriter.writeS(_charName);
			packetWriter.writeQ(_charAdena);
			packetWriter.writeD(_items.length);
		}
		else if (_type == 2)
		{
			packetWriter.writeD(_items.length);
			packetWriter.writeD(_items.length);
			for (ItemInstance temp : _items)
			{
				writeItemInfo(packetWriter, temp);
				packetWriter.writeD(temp.getObjectId());
			}
		}
		return true;
	}
}