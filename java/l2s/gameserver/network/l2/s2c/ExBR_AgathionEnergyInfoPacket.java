package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.items.ItemInstance;

/**
 * @author VISTALL
 */
public class ExBR_AgathionEnergyInfoPacket implements IClientOutgoingPacket
{
	private int _size;
	private ItemInstance[] _itemList = null;

	public ExBR_AgathionEnergyInfoPacket(int size, ItemInstance... item)
	{
		_itemList = item;
		_size = size;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_size);
		for (ItemInstance item : _itemList)
		{
			packetWriter.writeD(item.getObjectId());
			packetWriter.writeD(item.getItemId());
			packetWriter.writeQ(0x200000);
			packetWriter.writeD(item.getAgathionEnergy());// current energy
			packetWriter.writeD(item.getTemplate().getAgathionMaxEnergy()); // max energy
		}
	}
}