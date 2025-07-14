package l2s.gameserver.network.l2.s2c.enchant;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExResetEnchantChallengePoint implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket FAIL = new ExResetEnchantChallengePoint(0);
	public static final IClientOutgoingPacket SUCCESS = new ExResetEnchantChallengePoint(1);
	private int _bResult;

	public ExResetEnchantChallengePoint(int bResult)
	{
		_bResult = bResult;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_bResult);//bResult
		return true;
	}
}