package l2s.gameserver.network.l2.c2s.enchant;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.GameClient;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.s2c.enchant.ExResultMultiEnchantItemList;

/**
 * @author nexvill
 */
public class RequestExViewMultiEnchantResult implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		player.sendPacket(new ExResultMultiEnchantItemList(player, player.getMultiSuccessEnchantList(), player.getMultiFailureEnchantList(), true));
	}
}