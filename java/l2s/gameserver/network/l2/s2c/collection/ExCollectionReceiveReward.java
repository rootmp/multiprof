package l2s.gameserver.network.l2.s2c.collection;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExCollectionReceiveReward implements IClientOutgoingPacket
{
	private final int _collectionId;
	private boolean _success;

	public ExCollectionReceiveReward(int collectionId, boolean success)
	{
		_collectionId = collectionId;
		_success = success;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(_collectionId);
		packetWriter.writeC(_success);
		return true;
	}
}
