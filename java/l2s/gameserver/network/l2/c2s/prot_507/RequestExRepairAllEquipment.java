package l2s.gameserver.network.l2.c2s.prot_507;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import l2s.commons.dao.JdbcEntityState;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.PcInventory;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.prot_507.ExRepairAllEquipment;
import l2s.gameserver.utils.ItemFunctions;

public class RequestExRepairAllEquipment implements IClientIncomingPacket
{
	private static final int REPAIR_ITEM_ID = 3031;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		final PcInventory inventory = player.getInventory();

		List<ItemInstance> brokenItems = inventory.getItemsList().stream().filter(Objects::nonNull).filter(ItemInstance::isDamaged).collect(Collectors.toList());

		int count = brokenItems.size();
		if (count == 0)
			return;

		int cost;
		if (count == 1)
			cost = 3;
		else if (count == 2)
			cost = 6;
		else if (count == 3)
			cost = 13;
		else if (count == 4)
			cost = 20;
		else
			cost = 30;

		boolean paid = ItemFunctions.deleteItem(player, REPAIR_ITEM_ID, cost, true);
		if (!paid)
		{
			player.sendPacket(new ExRepairAllEquipment(false));
			return;
		}
		
		inventory.writeLock();
		try
		{
			for (ItemInstance item : brokenItems)
			{
				item.setDamaged(false);
				item.setJdbcState(JdbcEntityState.UPDATED);
				item.update();
			}
			inventory.refreshEquip();
			player.sendPacket(new ExRepairAllEquipment(true));
		}
		finally
		{
			inventory.writeUnlock();
			player.updateStats();
		}
	}
}
