package l2s.gameserver.network.l2.s2c.collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExCollectionFavoriteList implements IClientOutgoingPacket
{
	private Player _player;
	private int[] _collectionFavorites;

	public ExCollectionFavoriteList(Player player)
	{
		_player = player;
		_collectionFavorites = _player.getCollectionFavorites().toArray();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_collectionFavorites.length);
		for (int i = 0; i < _collectionFavorites.length; i++)
		{
			packetWriter.writeH(_collectionFavorites[i]); // collection id
			packetWriter.writeD(0); // expiration time?
		}
		return true;
	}
}