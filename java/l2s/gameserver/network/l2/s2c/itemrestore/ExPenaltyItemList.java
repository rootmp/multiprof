package l2s.gameserver.network.l2.s2c.itemrestore;

import java.util.Collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExPenaltyItemList extends L2GameServerPacket
{
	private final Player _player;
	private final Collection<ItemInstance> _itemsToRestore;

	public ExPenaltyItemList(Player player)
	{
		_player = player;
		_itemsToRestore = _player.getItemsToRestore();
	}

	@Override
	protected final void writeImpl()
	{
		if (_itemsToRestore.size() == 0)
			writeD(0);
		else
		{
			writeD(_itemsToRestore.size());
			for (ItemInstance item : _itemsToRestore)
			{
				writeD(item.getObjectId()); // on off unknown value special for any item, maybe id from items to restore
											// global list
				writeD(item.getLostDate());
				writeQ(item.getTemplate().getReferencePrice() > 0 ? item.getTemplate().getReferencePrice() : 10000); // adena
																														// price
				writeD(5); // lcoin price
				writeD(49); // ? 49 on JP.. 0 cause not shown item, and next item row are wrong
				writeD(0); // ?
				writeH(0); // ?
				writeD(item.getItemId()); // item id
				writeC(0); // ?
				writeQ(1); // count?
				writeD(1); // ?
				writeD(64); // on JP 64/ 2nd 4096.. 0 cause not shown item, and next item row are wrong
				writeD(0); // ?
				writeD(item.getEnchantLevel()); // enchant level
				writeH(0); // ?
				writeC(0); // ?
				writeD(-9999); // ? -9999 on JP
				writeH(1); // ?
				writeC(0); // ?
				writeD(item.getObjectId()); // on off unknown value special for any item, maybe id from items to restore
											// global list
			}
		}
	}
}