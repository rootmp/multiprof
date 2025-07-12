package l2s.gameserver.network.l2.c2s.worldexchange;

import l2s.commons.network.PacketReader;
import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.WorldExchangeManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExWorldExchangeSettleRecvResult implements IClientIncomingPacket
{
	private long _worldExchangeIndex;
	
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_worldExchangeIndex = packet.readQ();
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

		WorldExchangeManager.getInstance().getItemStatusAndMakeAction(player, _worldExchangeIndex);
	}
}
