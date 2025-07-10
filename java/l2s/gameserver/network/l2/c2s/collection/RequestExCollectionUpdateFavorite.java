package l2s.gameserver.network.l2.c2s.collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
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
		_add = readC() == 1 ? true : false;
		_collectionId = readH();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player player = client.getActiveChar();
		if (player == null)
			return;

		player.sendPacket(new ExCollectionUpdateFavorite(player, _add, _collectionId));
	}
}