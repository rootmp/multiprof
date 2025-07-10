package l2s.gameserver.network.l2.c2s.collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionList;

/**
 * @author nexvill
 */
public class RequestExCollectionList implements IClientIncomingPacket
{
	private int _tabId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_tabId = packet.readC();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		player.sendPacket(new ExCollectionList(player, _tabId));
	}
}