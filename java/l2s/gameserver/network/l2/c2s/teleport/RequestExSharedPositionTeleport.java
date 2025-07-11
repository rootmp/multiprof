package l2s.gameserver.network.l2.c2s.teleport;

import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.variables.PlayerVariables;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.network.l2.s2c.SystemMessagePacket;
import l2s.gameserver.templates.item.ItemTemplate;

public class RequestExSharedPositionTeleport implements IClientIncomingPacket
{
	private int _allow, _tpId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_allow = packet.readC();
		_tpId = packet.readH();
		packet.readC(); // ?
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		if (_allow == 1)
		{
			Player player = client.getActiveChar();

			int previousRank = player.getPreviousPvpRank();
			boolean allowFree = false;

			ItemInstance l2coin = player.getInventory().getItemByItemId(ItemTemplate.ITEM_ID_MONEY_L);

			if ((previousRank > 0) && (previousRank < 4))
			{
				allowFree = true;
			}

			if (!allowFree && ((l2coin == null) || (l2coin.getCount() < Config.SHARED_TELEPORT_TO_LOCATION)))
			{
				player.sendPacket(new SystemMessage(SystemMsg.NOT_ENOUGH_L2_COINS));
				return;
			}

			if (allowFree)
			{
				manageTeleport(player, true);
			}
			else if (player.getInventory().destroyItem(l2coin, Config.SHARED_TELEPORT_TO_LOCATION))
			{
				manageTeleport(player, false);
			}
		}
	}

	private void manageTeleport(Player player, boolean free)
	{
		int x = ServerVariables.getInt("tpId_" + _tpId + "_x");
		int y = ServerVariables.getInt("tpId_" + _tpId + "_y");
		int z = ServerVariables.getInt("tpId_" + _tpId + "_z");
		player.teleToLocation(x, y, z);
		int tpCounts = player.getVarInt(PlayerVariables.SHARED_POSITION_TELEPORTS, Config.SHARED_TELEPORTS_PER_DAY) - 1;
		player.setVar(PlayerVariables.SHARED_POSITION_TELEPORTS, tpCounts);

		if (!free)
		{
			player.sendPacket(SystemMessagePacket.removeItems(ItemTemplate.ITEM_ID_MONEY_L, Config.SHARED_TELEPORT_TO_LOCATION));
		}
	}
}