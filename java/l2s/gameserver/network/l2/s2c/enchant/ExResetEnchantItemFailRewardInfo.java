package l2s.gameserver.network.l2.s2c.enchant;

import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExResetEnchantItemFailRewardInfo extends L2GameServerPacket
{
	private final ItemInstance _enchantedItem;
	final int _crystalId, _crystalCount;
	final boolean _saved;

	public ExResetEnchantItemFailRewardInfo(ItemInstance enchantedItem, int crystalId, int crystalCount, boolean saved)
	{
		_enchantedItem = enchantedItem;
		_crystalId = crystalId;
		_crystalCount = crystalCount;
		_saved = saved;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_enchantedItem.getItemId()); // item id
		writeD(0); // enchant challenge point group id
		writeD(0); // enchant challenge points
		if (!_saved)
		{
			writeD(1); // loop count, if not only crystals
			writeD(_crystalId); // item Id
			writeD(_crystalCount); // item count
		}
		else
		{
			writeD(1);
			writeD(_enchantedItem.getItemId());
			writeD(1);
		}
	}
}