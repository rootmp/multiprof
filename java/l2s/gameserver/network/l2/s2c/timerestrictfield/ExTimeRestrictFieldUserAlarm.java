package l2s.gameserver.network.l2.s2c.timerestrictfield;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExTimeRestrictFieldUserAlarm implements IClientOutgoingPacket
{
	private final int _fieldId;
	private final int _time;

	public ExTimeRestrictFieldUserAlarm(int fieldId, int time)
	{
		_fieldId = fieldId;
		_time = time;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_fieldId);
		packetWriter.writeD(_time);
		return true;
	}
}