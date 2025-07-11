package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;

/**
 * Format: ch ddcdc Args: player, points to add, type of period (default 1),
 * type of points (1-double, 2-integer), time left to the end of period
 */
public class ExPCCafePointInfoPacket implements IClientOutgoingPacket
{
	private int _mAddPoint, _mPeriodType, _pointType, _pcBangPoints, _remainTime;

	public ExPCCafePointInfoPacket(Player player, int mAddPoint, int mPeriodType, int pointType, int remainTime)
	{
		_pcBangPoints = player.getPcBangPoints();
		_mAddPoint = mAddPoint;
		_mPeriodType = mPeriodType;
		_pointType = pointType;
		_remainTime = remainTime;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_pcBangPoints);
		packetWriter.writeD(_mAddPoint);
		packetWriter.writeC(_mPeriodType);
		packetWriter.writeD(_remainTime);
		packetWriter.writeC(_pointType);
		packetWriter.writeD(0); // TODO: online time
		return true;
	}
}