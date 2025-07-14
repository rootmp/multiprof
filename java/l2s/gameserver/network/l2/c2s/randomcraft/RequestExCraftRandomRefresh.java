package l2s.gameserver.network.l2.c2s.randomcraft;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.PlayerRandomCraft;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExCraftRandomRefresh implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}
	
	@Override
	public void run(GameClient client) throws Exception
	{
		if (!Config.RANDOM_CRAFT_SYSTEM_ENABLED)
		{
			return;
		}
		
		final Player player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		final PlayerRandomCraft rc = player.getRandomCraft();
		rc.refresh();
	}

}
