package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.ItemTemplate;

/**
 * @author nexvill
**/
public class ExBloodyCoinCount implements IClientOutgoingPacket
{
	private final long _coins;

	public ExBloodyCoinCount(Player player)
	{
		if (player.getInventory().getItemByItemId(ItemTemplate.ITEM_ID_MONEY_L) == null)
			_coins = 0;
		else
			_coins = player.getInventory().getItemByItemId(ItemTemplate.ITEM_ID_MONEY_L).getCount();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeQ(_coins);
		return true;
	}
}