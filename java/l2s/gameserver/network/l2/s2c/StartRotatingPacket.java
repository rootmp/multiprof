package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Creature;

public class StartRotatingPacket implements IClientOutgoingPacket
{
	private int _charId, _degree, _side, _speed;

	public StartRotatingPacket(Creature cha, int degree, int side, int speed)
	{
		_charId = cha.getObjectId();
		_degree = degree;
		_side = side;
		_speed = speed;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_charId);
		packetWriter.writeD(_degree);
		packetWriter.writeD(_side);
		packetWriter.writeD(_speed);
		return true;
	}
}