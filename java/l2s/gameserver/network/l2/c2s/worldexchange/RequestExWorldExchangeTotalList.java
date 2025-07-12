package l2s.gameserver.network.l2.c2s.worldexchange;

import java.util.LinkedList;
import java.util.List;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.worldexchange.ExWorldExchangeTotalList;

public class RequestExWorldExchangeTotalList implements IClientIncomingPacket
{
	private final List<Integer> itemIds = new LinkedList<>();
	
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		final int size = packet.readD();
		for (int index = 0; index < size; index++)
		{
			itemIds.add(packet.readD());
		}
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		player.sendPacket(new ExWorldExchangeTotalList(itemIds));
	}
}
