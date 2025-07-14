package l2s.gameserver.network.l2.c2s.locked;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExRequestUnlockedItem implements IClientIncomingPacket
{
	private int nTargetItemId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		nTargetItemId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		/*	if(player == null)
				return;
			ItemInstance item = player.getInventory().getItemByObjectId(nTargetItemId);
			if(!item.getTemplate().possiblyLock() || !item.isLocked())
				return;
			
			if(ItemFunctions.deleteItem(player, 48401, 1, "ExRequestLockedItem"))
			{
				item.setLocked(false);
				item.setJdbcState(JdbcEntityState.UPDATED);
				item.update();
				player.getInventory().refreshEquip(item);
				player.sendPacket(new InventoryUpdatePacket(player).addModifiedItem(item));
				player.sendPacket(new ExLockedResult(0,1));
			}*/
	}
}