package l2s.gameserver.network.l2.c2s.collection;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.s2c.collection.ExCollectionUpdateFavorite;

/**
 * @author nexvill
 */
public class RequestExCollectionUpdateFavorite implements IClientIncomingPacket
{
	private boolean _add;
	private int _collectionId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_add = packet.readC() == 1 ? true : false;
		_collectionId = packet.readH();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;

		player.sendPacket(new ExCollectionUpdateFavorite(player, _add, _collectionId));
	}
}