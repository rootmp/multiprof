package l2s.gameserver.network.l2.s2c.collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExCollectionFavoriteList extends L2GameServerPacket
{
	private Player _player;
	private int[] _collectionFavorites;

	public ExCollectionFavoriteList(Player player)
	{
		_player = player;
		_collectionFavorites = _player.getCollectionFavorites().toArray();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_collectionFavorites.length);
		for (int i = 0; i < _collectionFavorites.length; i++)
		{
			writeH(_collectionFavorites[i]); // collection id
			writeD(0); // expiration time?
		}
	}
}