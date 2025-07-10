package l2s.gameserver.network.l2.c2s.enchant;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.enchant.ExChangedEnchantTargetItemProbabilityList;
import l2s.gameserver.network.l2.s2c.enchant.ExResultSetMultiEnchantItemList;

/**
 * @author nexvill
 */
public class RequestExSetMultiEnchantItemList implements IClientIncomingPacket
{
	private int _slotId;
	private final Map<Integer, Integer> _itemObjId = new HashMap<>();

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_slotId = packet.readD();
		for (int i = 1; getAvaliableBytes() != 0; i++)
		{
			_itemObjId.put(i, readD());
		}
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		if (player.getMultiEnchantingItemsBySlot(_slotId) != -1)
		{
			player.clearMultiEnchantingItemsBySlot();
			for (int i = 1; i <= _slotId; i++)
			{
				player.addMultiEnchantingItems(i, _itemObjId.get(i));
			}
		}
		else
		{
			player.addMultiEnchantingItems(_slotId, _itemObjId.get(_slotId));
		}

		_itemObjId.clear();
		player.sendPacket(new ExResultSetMultiEnchantItemList(0));
		player.sendPacket(new ExChangedEnchantTargetItemProbabilityList(player, true));
	}
}