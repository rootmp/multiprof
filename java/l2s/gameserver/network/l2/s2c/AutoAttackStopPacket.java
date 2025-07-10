package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

public class AutoAttackStopPacket implements IClientOutgoingPacket
{
	// dh
	private int _targetId;

	/**
	 * @param _characters
	 */
	public AutoAttackStopPacket(int targetId)
	{
		_targetId = targetId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_targetId);
	}
}