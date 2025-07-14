package l2s.gameserver.network.l2.c2s.randomcraft;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.PlayerRandomCraft;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.randomcraft.ExCraftRandomInfo;
import l2s.gameserver.network.l2.s2c.randomcraft.ExCraftRandomLockSlot;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExCraftRandomLockSlot implements IClientIncomingPacket
{
	private static final int[] LOCK_PRICE = {
			100,
			500,
			1000
	};

	private int _id;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_id = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		if(!Config.RANDOM_CRAFT_SYSTEM_ENABLED)
		{ return; }

		final Player player = client.getActiveChar();
		if(player == null)
		{ return; }

		if((_id >= 0) && (_id < 5))
		{
			final PlayerRandomCraft rc = player.getRandomCraft();
			int lockedItemCount = rc.getLockedSlotCount();
			if(((rc.getRewards().size() - 1) >= _id) && (lockedItemCount < 3))
			{
				int price = LOCK_PRICE[Math.min(lockedItemCount, 2)];
				ItemInstance lcoin = player.getInventory().getItemByItemId(ItemTemplate.ITEM_ID_MONEY_L);
				if((lcoin != null) && (lcoin.getCount() >= price))
				{
					ItemFunctions.deleteItem(player, lcoin, price);
					rc.getRewards().get(_id).lock();
					player.sendPacket(new ExCraftRandomLockSlot());
					player.sendPacket(new ExCraftRandomInfo(player));
				}
			}
		}
	}
}
