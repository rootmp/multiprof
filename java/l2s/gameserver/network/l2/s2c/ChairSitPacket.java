package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.StaticObjectInstance;

/**
 * format: d
 */
public class ChairSitPacket implements IClientOutgoingPacket
{
	private int _objectId;
	private int _staticObjectId;

	public ChairSitPacket(Player player, StaticObjectInstance throne)
	{
		_objectId = player.getObjectId();
		_staticObjectId = throne.getUId();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objectId);
		packetWriter.writeD(_staticObjectId);
		return true;
	}
}