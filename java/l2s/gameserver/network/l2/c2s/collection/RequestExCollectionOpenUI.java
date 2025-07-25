package l2s.gameserver.network.l2.c2s.collection;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionOpenUI;

/**
 * @author nexvill
 */
public class RequestExCollectionOpenUI implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		packet.readC(); // not used
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		player.setInCollectionUI(true);
		player.sendPacket(new ExCollectionOpenUI());
	}
}