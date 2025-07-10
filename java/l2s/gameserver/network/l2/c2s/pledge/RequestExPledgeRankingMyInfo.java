package l2s.gameserver.network.l2.c2s.pledge;

import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.c2s.L2GameClientPacket;
import l2s.gameserver.network.l2.s2c.pledge.ExPledgeRankingMyInfo;

/**
 * @author nexvill
 */
public class RequestExPledgeRankingMyInfo extends L2GameClientPacket
{
	@Override
	protected boolean readImpl()
	{
		readC(); // unk 0
		return true;
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		activeChar.sendPacket(new ExPledgeRankingMyInfo(activeChar));
	}
}