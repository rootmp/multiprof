package l2s.gameserver.network.l2.c2s.enchant;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.enchant.ExResetEnchantItemFailRewardInfo;

/**
 * @author nexvill
 */
public class RequestExEnchantFailRewardInfo implements IClientIncomingPacket
{
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

		if (player.getEnchantItem() == null || player.getEnchantScroll() == null)
			return;
		
		player.sendPacket(new ExResetEnchantItemFailRewardInfo(player, _itemObjId));
	}
}