package l2s.gameserver.network.l2.s2c.collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExCollectionUpdateFavorite extends L2GameServerPacket
{
	private Player _player;
	private boolean _add;
	private int _collectionId;

	public ExCollectionUpdateFavorite(Player player, boolean add, int collectionId)
	{
		_player = player;
		_add = add;
		_collectionId = collectionId;
	}

	@Override
	protected final void writeImpl()
	{
		if (_add)
			_player.addCollectionFavorite(_collectionId);
		else
			_player.removeCollectionFavorite(_collectionId);
		writeC(_add); // add or remove
		writeH(_collectionId); // collection id
	}
}