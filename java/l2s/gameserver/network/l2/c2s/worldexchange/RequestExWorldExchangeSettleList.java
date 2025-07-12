package l2s.gameserver.network.l2.c2s.worldexchange;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.worldexchange.ExWorldExchangeItemList;
import l2s.gameserver.network.l2.s2c.worldexchange.ExWorldExchangeSettleList;

public class RequestExWorldExchangeSettleList implements IClientIncomingPacket
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
		if (!Config.ENABLE_WORLD_EXCHANGE)
			return;
		
		
		final Player player = client.getActiveChar();
		if (player == null)
			return;
		
		
		player.sendPacket(ExWorldExchangeItemList.EMPTY_LIST);
		player.sendPacket(new ExWorldExchangeSettleList(player));
	}
}
