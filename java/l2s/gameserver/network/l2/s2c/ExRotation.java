package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class ExRotation implements IClientOutgoingPacket
{
	private int _charObjId, _degree;

	public ExRotation(int charId, int degree)
	{
		_charObjId = charId;
		_degree = degree;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_charObjId);
		packetWriter.writeD(_degree);
	}
}
