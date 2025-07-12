package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

public class ExVipAttendanceCheck implements IClientOutgoingPacket
{
	private int _bResult;

	public ExVipAttendanceCheck(int bResult)
	{
		_bResult = bResult;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_bResult);
		return true;
	}
}