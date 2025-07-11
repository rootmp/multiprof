package l2s.gameserver.network.l2.s2c.timerestrictfield;

import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author nexvill
 */
public class ExTimeRestrictFieldUserChargeResult implements IClientOutgoingPacket
{
	private final int _fieldId;
	private final int _remainTime;
	private final int _remainTimeBase;

	public ExTimeRestrictFieldUserChargeResult(int fieldId, int remainTime, int remainTimeBase)
	{
		_fieldId = fieldId;
		_remainTime = remainTime;
		_remainTimeBase = remainTimeBase;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_fieldId);
		packetWriter.writeD(_remainTime);
		packetWriter.writeD(_remainTimeBase);
		return true;
	}
}