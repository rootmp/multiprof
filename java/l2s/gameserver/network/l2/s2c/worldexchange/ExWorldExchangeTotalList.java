package l2s.gameserver.network.l2.s2c.worldexchange;

import java.util.Collection;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExWorldExchangeTotalList implements IClientOutgoingPacket
{
	private final Collection<Integer> _itemIds;

	public ExWorldExchangeTotalList(Collection<Integer> itemIds)
	{
		_itemIds = itemIds;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_itemIds.size());
		for(int id : _itemIds)
		{
			packetWriter.writeD(id); // ItemClassID
			packetWriter.writeQ(0); // MinPricePerPiece
			packetWriter.writeQ(0);//nMinAdenaPricePerPiece
			packetWriter.writeQ(0); // Price
			packetWriter.writeQ(1); // Amount
		}
		return true;
	}
}
