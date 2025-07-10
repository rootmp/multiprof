package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.impl.DuelEvent;

public class RequestDuelSurrender implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		DuelEvent duelEvent = player.getEvent(DuelEvent.class);
		if (duelEvent == null)
			return;

		duelEvent.packetSurrender(player);
	}
}