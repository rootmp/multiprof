package l2s.gameserver.network.l2.s2c.collection;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExCollectionComplete implements IClientOutgoingPacket
{
	private final int _collectionId;
	
	public ExCollectionComplete(int collectionId)
	{
		_collectionId = collectionId;
	}
	
	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(_collectionId);
		packetWriter.writeD(0);
		return true;
	}
}
