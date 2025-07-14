package l2s.gameserver.network.l2.c2s.worldexchange;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.worldexchange.ExWorldExchangeAveragePrice;

public class RequestExWorldExchangeAveragePrice implements IClientIncomingPacket
{
	private int _itemId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_itemId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
		{ return; }

		player.sendPacket(new ExWorldExchangeAveragePrice(_itemId));
	}
}
