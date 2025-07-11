package l2s.gameserver.network.l2.s2c.itemrestore;

import java.util.Collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExPenaltyItemList implements IClientOutgoingPacket
{
	private final Player _player;
	private final Collection<ItemInstance> _itemsToRestore;

	public ExPenaltyItemList(Player player)
	{
		_player = player;
		_itemsToRestore = _player.getItemsToRestore();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		if (_itemsToRestore.size() == 0)
			packetWriter.writeD(0);
		else
		{
			packetWriter.writeD(_itemsToRestore.size());
			for (ItemInstance item : _itemsToRestore)
			{
				packetWriter.writeD(item.getObjectId()); // on off unknown value special for any item, maybe id from items to restore
											// global list
				packetWriter.writeD(item.getLostDate());
				packetWriter.writeQ(item.getTemplate().getReferencePrice() > 0 ? item.getTemplate().getReferencePrice() : 10000); // adena
																														// price
				packetWriter.writeD(5); // lcoin price
				packetWriter.writeD(49); // ? 49 on JP.. 0 cause not shown item, and next item row are wrong
				packetWriter.writeD(0); // ?
				packetWriter.writeH(0); // ?
				packetWriter.writeD(item.getItemId()); // item id
				packetWriter.writeC(0); // ?
				packetWriter.writeQ(1); // count?
				packetWriter.writeD(1); // ?
				packetWriter.writeD(64); // on JP 64/ 2nd 4096.. 0 cause not shown item, and next item row are wrong
				packetWriter.writeD(0); // ?
				packetWriter.writeD(item.getEnchantLevel()); // enchant level
				packetWriter.writeH(0); // ?
				packetWriter.writeC(0); // ?
				packetWriter.writeD(-9999); // ? -9999 on JP
				packetWriter.writeH(1); // ?
				packetWriter.writeC(0); // ?
				packetWriter.writeD(item.getObjectId()); // on off unknown value special for any item, maybe id from items to restore
											// global list
			}
		}
		return true;
	}
}