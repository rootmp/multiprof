package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class AutoAttackStartPacket implements IClientOutgoingPacket
{
	// dh
	private int _targetId;

	public AutoAttackStartPacket(int targetId)
	{
		_targetId = targetId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_targetId);
	}
}