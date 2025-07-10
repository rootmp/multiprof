package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.items.ItemInstance;

public class ExResponseCommissionInfo implements IClientOutgoingPacket
{
	private ItemInstance _item;

	public ExResponseCommissionInfo(ItemInstance item)
	{
		_item = item;
	}

	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_item.getItemId()); // ItemId
		packetWriter.writeD(_item.getObjectId());
		packetWriter.writeQ(_item.getCount()); // TODO
		packetWriter.writeQ(0/* _item.getCount() */); // TODO
		packetWriter.writeD(0); // TODO
	}
}
