package l2s.gameserver.network.l2.s2c.enchant;

import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExResetEnchantItemFailRewardInfo implements IClientOutgoingPacket
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
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_enchantedItem.getItemId()); // item id
		packetWriter.writeD(0); // enchant challenge point group id
		packetWriter.writeD(0); // enchant challenge points
		if (!_saved)
		{
			packetWriter.writeD(1); // loop count, if not only crystals
			packetWriter.writeD(_crystalId); // item Id
			packetWriter.writeD(_crystalCount); // item count
		}
		else
		{
			packetWriter.writeD(1);
			packetWriter.writeD(_enchantedItem.getItemId());
			packetWriter.writeD(1);
		}
		return true;
	}
}