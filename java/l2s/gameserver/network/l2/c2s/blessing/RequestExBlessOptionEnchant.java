package l2s.gameserver.network.l2.c2s.blessing;

import l2s.commons.dao.JdbcEntityState;
import l2s.commons.network.PacketReader;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.items.PcInventory;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.InventoryUpdatePacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.blessing.ExBlessOptionCancel;
import l2s.gameserver.network.l2.s2c.blessing.ExBlessOptionEnchant;

public class RequestExBlessOptionEnchant implements IClientIncomingPacket
{
	private static final int BLESSING_DELAY = 1500;
	private static final int SUCCESS_VISUAL_EFF_ID = 5965;
	private static final int FAIL_VISUAL_EFF_ID = 5949;
	private int _itemObjId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_itemObjId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		if (player.isActionsDisabled())
		{
			player.setBlessingScroll(null);
			player.sendActionFailed();
			return;
		}

		if (player.isInTrade())
		{
			player.setBlessingScroll(null);
			player.sendActionFailed();
			return;
		}

		if (System.currentTimeMillis() <= (player.getLastBlessingItemTime() + BLESSING_DELAY))
		{
			player.setBlessingScroll(null);
			player.sendActionFailed();
			return;
		}

		if (player.isInStoreMode())
		{
			player.setBlessingScroll(null);
			player.sendPacket(ExBlessOptionCancel.STATIC);
			player.sendPacket(SystemMsg.YOU_CANNOT_ENCHANT_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			player.sendActionFailed();
			return;
		}

		final PcInventory inventory = player.getInventory();
		inventory.writeLock();
		try
		{
			final ItemInstance item = inventory.getItemByObjectId(_itemObjId);
			final ItemInstance scroll = player.getBlessingScroll();

			if (item == null || scroll == null)
			{
				player.sendActionFailed();
				return;
			}

			if (item.isBlessed())
			{
				player.sendActionFailed();
				return;
			}

			if (!inventory.destroyItem(scroll, 1L))
			{
				player.sendPacket(ExBlessOptionCancel.STATIC);
				player.sendActionFailed();
				return;
			}

			if (Rnd.get(100) < Config.BLESSING_ITEM_CHANCE)
			{
				item.setBlessed(true);
				item.setJdbcState(JdbcEntityState.UPDATED);
				item.update();

				player.getInventory().refreshEquip(item);

				player.sendPacket(new InventoryUpdatePacket().addModifiedItem(player, item));
				player.sendPacket(new ExBlessOptionEnchant(true));
				player.broadcastPacket(new MagicSkillUse(player, player, SUCCESS_VISUAL_EFF_ID, 1, 500, 1500));
				player.getListeners().onBlessingItem(item, true);
			}
			else
			{
				player.sendPacket(new ExBlessOptionEnchant(false));
				player.broadcastPacket(new MagicSkillUse(player, player, FAIL_VISUAL_EFF_ID, 1, 500, 1500));
				player.getListeners().onBlessingItem(item, false);
			}
		}
		finally
		{
			inventory.writeUnlock();

			player.updateStats();
		}

		player.setLastBlessingItemTime(System.currentTimeMillis());
	}
}