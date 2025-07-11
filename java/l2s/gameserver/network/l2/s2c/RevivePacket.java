package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.GameObject;

/**
 * sample 0000: 0c 9b da 12 40 ....@ format d
 */
public class RevivePacket implements IClientOutgoingPacket
{
	private int _objectId;

	public RevivePacket(GameObject obj)
	{
		_objectId = obj.getObjectId();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objectId);
		return true;
	}
}