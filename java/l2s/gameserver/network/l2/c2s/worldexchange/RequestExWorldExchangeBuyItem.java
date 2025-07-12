package l2s.gameserver.network.l2.c2s.worldexchange;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.WorldExchangeManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.stats.Stats;

public class RequestExWorldExchangeBuyItem implements IClientIncomingPacket
{
	private long _worldExchangeIndex;
	private long nCount;
	
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_worldExchangeIndex = packet.readQ();
		nCount = packet.readQ();
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
		if(player.getStat().calc(Stats.P_BLOCK_WORLD_TRADE) != 0)
		{
			player.sendPacket(SystemMsg.S_19020);
			player.sendActionFailed();
			return;
		}
		WorldExchangeManager.getInstance().buyItem(player, _worldExchangeIndex, nCount);
	}
}
