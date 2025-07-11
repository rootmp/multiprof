package l2s.gameserver.network.l2.s2c.blessing;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExBlessOptionPutItem implements IClientOutgoingPacket
{
	private Player _player;
	private int _itemObjId;

	public ExBlessOptionPutItem(Player player, int itemObjId)
	{
		_player = player;
		_itemObjId = itemObjId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		ItemInstance item = _player.getInventory().getItemByObjectId(_itemObjId);
		if (item == null)
		{
			packetWriter.writeC(0);
			return true;
		}

		if (item.isBlessed() || !item.getTemplate().isBlessable())
		{
			packetWriter.writeC(0);
			return true;
		}

		packetWriter.writeC(1); // success put or no
		return true;
	}
}