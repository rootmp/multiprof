package l2s.gameserver.network.l2.s2c.collection;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExCollectionUpdateFavorite implements IClientOutgoingPacket
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
	public boolean write(PacketWriter packetWriter)
	{
		if (_add)
			_player.addCollectionFavorite(_collectionId);
		else
			_player.removeCollectionFavorite(_collectionId);
		packetWriter.writeC(_add); // add or remove
		packetWriter.writeH(_collectionId); // collection id
	}
}