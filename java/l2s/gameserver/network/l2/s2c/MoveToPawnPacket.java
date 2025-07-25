package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Creature;

public class MoveToPawnPacket implements IClientOutgoingPacket
{
	private final int _objectId;
	private final int _targetId;
	private final int _distance;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _tx;
	private final int _ty;
	private final int _tz;

	public MoveToPawnPacket(Creature cha, Creature target, int distance)
	{
		_objectId = cha.getObjectId();
		_targetId = target.getObjectId();
		_distance = distance;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
		_tx = target.getX();
		_ty = target.getY();
		_tz = target.getZ();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_objectId);
		packetWriter.writeD(_targetId);
		packetWriter.writeD(_distance);
		packetWriter.writeD(_x);
		packetWriter.writeD(_y);
		packetWriter.writeD(_z);
		packetWriter.writeD(_tx);
		packetWriter.writeD(_ty);
		packetWriter.writeD(_tz);
		return true;
	}
}