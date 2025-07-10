package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.Creature;

public class FinishRotatingPacket implements IClientOutgoingPacket
{
	private int _charId, _degree, _speed;

	public FinishRotatingPacket(Creature player, int degree, int speed)
	{
		_charId = player.getObjectId();
		_degree = degree;
		_speed = speed;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_charId);
		packetWriter.writeD(_degree);
		packetWriter.writeD(_speed);
		packetWriter.writeD(0x00); // ??
	}
}